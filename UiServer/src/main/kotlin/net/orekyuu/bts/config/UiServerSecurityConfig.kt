package net.orekyuu.bts.config

import net.orekyuu.bts.message.user.CreateUserMessage
import net.orekyuu.bts.message.user.UserInfo
import net.orekyuu.bts.message.user.UserType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices
import org.springframework.boot.context.embedded.FilterRegistrationBean
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.OAuth2ClientContext
import org.springframework.security.oauth2.client.OAuth2RestTemplate
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.filter.CompositeFilter
import javax.servlet.Filter
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Configuration
open class UiServerSecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var oauth2ClientContext: OAuth2ClientContext
    @Value("\${api.url}")
    private lateinit var hostUrl: String

    override fun configure(http: HttpSecurity?) {
        http!!.authorizeRequests()
                .antMatchers("/login**", "/ico/favicon.ico", "/webjars/**", "/error/**", "/assets/**").permitAll()
                .anyRequest().authenticated()
                .and().exceptionHandling().authenticationEntryPoint(LoginUrlAuthenticationEntryPoint("/"))
                .and().logout().logoutSuccessUrl("/").permitAll()
                .and().addFilterBefore(ssoFilter(), BasicAuthenticationFilter::class.java)
    }

    fun ssoFilter(): Filter {
        val filter = CompositeFilter()
        filter.setFilters(arrayListOf(githubFilter()))
        return filter
    }

    fun githubFilter(): Filter {
        val resources = github()
        val template = githubRestTemplate(resources)

        val filter = OAuth2ClientAuthenticationProcessingFilter("/login/github")
        filter.setAuthenticationSuccessHandler(GithubSuccessHandler(template, hostUrl))
        filter.setRestTemplate(template)
        filter.setTokenServices(UserInfoTokenServices(resources.resource.userInfoUri, resources.client.clientId))
        return filter
    }

    @Bean
    @Qualifier("github")
    open internal fun githubRestTemplate(resource: ClientResources) = OAuth2RestTemplate(resource.client, oauth2ClientContext)

    @Bean
    @ConfigurationProperties("github")
    open internal fun github() = ClientResources()

    @Bean
    open fun oauth2ClientFilterRegistration(filter: OAuth2ClientContextFilter): FilterRegistrationBean {
        val registration = FilterRegistrationBean()
        registration.setFilter(filter)
        registration.order = -100
        return registration
    }

    internal class ClientResources {
        val client: AuthorizationCodeResourceDetails = AuthorizationCodeResourceDetails()
        val resource: ResourceServerProperties = ResourceServerProperties()
    }
}

abstract class AbstractSuccessHandler(
        val restTemplate: OAuth2RestTemplate,
        val userType: UserType,
        val hostUrl: String
) : SavedRequestAwareAuthenticationSuccessHandler() {

    override fun handle(request: HttpServletRequest?, response: HttpServletResponse?, authentication: Authentication?) {
        if(response == null || authentication == null) {
            logger.warn("response or authentication is null [response=$response, authentication=$authentication]")
            super.handle(request, response, authentication)
            return
        }
        //ログインしたらトップページへリダイレクト
        response.sendRedirect("/app")

        //Cookieを保存する前にhandleを呼ばないと保存されない
        super.handle(request, response, authentication)
    }

    override fun onAuthenticationSuccess(request: HttpServletRequest?, response: HttpServletResponse?, authentication: Authentication?) {
        logger.info("onAuthenticationSuccess")

        if(response == null || authentication == null) {
            logger.warn("response or authentication is null [response=$response, authentication=$authentication]")
            super.onAuthenticationSuccess(request, response, authentication)
            return
        }

        val userInfo: UserInfo = try {
            val userInfoEndpoint = "http://localhost:18080/api/user/"
            restTemplate.getForEntity(userInfoEndpoint, UserInfo::class.java).body
        } catch(e: HttpClientErrorException) {
            //500エラーならユーザーが見つからなかったので新しく作成
            if(e.statusCode.is4xxClientError) {
                logger.info("User not found")
                val url = "http://localhost:18080/open/user/create/${endpointPostfix(userType)}"
                val name = authentication.name
                restTemplate.postForEntity(url, CreateUserMessage(name), UserInfo::class.java).body.let {
                    logger.info("User created")
                    it
                }
            } else {
                //その他のエラーは想定していないのでスタックトレースを出してスロー
                e.printStackTrace()
                throw RuntimeException(e)
            }
        }

        //クッキーでアクセストークンとユーザーの情報を送る
        response.addCookie(createCookie("access_token", restTemplate.accessToken.value))
        response.addCookie(createCookie("user_type", userType.name))
        response.addCookie(createCookie("user_id", userInfo.id.toString()))
        response.addCookie(createCookie("user_name", userInfo.name))
        response.addCookie(createCookie("user_name", userInfo.name))
        response.addCookie(createCookie("host", hostUrl))

        super.onAuthenticationSuccess(request, response, authentication)
    }

    fun createCookie(name: String, value: String) = Cookie(name, value).apply { path = "/" }

    fun endpointPostfix(userType: UserType)
            = when(userType) {
        UserType.GITHUB -> "github"
        else -> throw IllegalArgumentException(userType.name)
    }
}

class GithubSuccessHandler(
        restTemplate: OAuth2RestTemplate,
        hostUrl: String
): AbstractSuccessHandler(restTemplate, UserType.GITHUB, hostUrl)
