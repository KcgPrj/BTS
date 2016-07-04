package net.orekyuu.bts.config

import net.orekyuu.bts.message.user.CreateUserMessage
import net.orekyuu.bts.message.user.UserInfo
import net.orekyuu.bts.message.user.UserType
import org.springframework.beans.factory.annotation.Autowired
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
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.filter.CompositeFilter
import javax.servlet.Filter
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Configuration
open class UiServerSecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var oauth2ClientContext: OAuth2ClientContext

    override fun configure(http: HttpSecurity?) {
        http!!.authorizeRequests()
                .antMatchers("/login**", "/ico/favicon.ico", "/webjars/**", "/error/**").permitAll()
                .anyRequest().authenticated()
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
        val template = OAuth2RestTemplate(resources.client, oauth2ClientContext)

        val filter = OAuth2ClientAuthenticationProcessingFilter("/login/github")
        filter.setAuthenticationSuccessHandler(GithubSuccessHandler(template))
        filter.setRestTemplate(template)
        filter.setTokenServices(UserInfoTokenServices(resources.resource.userInfoUri, resources.client.clientId))
        return filter
    }

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
        val userType: UserType
) : SavedRequestAwareAuthenticationSuccessHandler() {

    override fun handle(request: HttpServletRequest?, response: HttpServletResponse?, authentication: Authentication?) {
        super.handle(request, response, authentication)

        if(response == null) {
            return
        }
        if(authentication == null) {
            return
        }

        val userInfo: UserInfo
        try {
            val userInfoEndpoint = "http://localhost:18080/user/github"
            userInfo = restTemplate.getForEntity(userInfoEndpoint, UserInfo::class.java).body
        } catch(e: Exception) {
            e.printStackTrace()
            val url = "http://localhost:18080/open/user/create/${endpointPostfix(userType)}"
            userInfo = restTemplate.postForEntity(url, CreateUserMessage(authentication.name!!), UserInfo::class.java).body
        }

        //クッキーでアクセストークンとユーザーのタイプを送る
        response.addCookie(Cookie("access_token", restTemplate.accessToken.value).apply {
            path = "/"
            maxAge = Integer.MAX_VALUE
        })
        response.addCookie(Cookie("user_type", userType.name).apply {
            path = "/"
            maxAge = Integer.MAX_VALUE
        })
        response.addCookie(Cookie("user_id", userInfo.id.toString()).apply {
            path = "/"
            maxAge = Integer.MAX_VALUE
        })
        response.addCookie(Cookie("user_name", userInfo.name).apply {
            path = "/"
            maxAge = Integer.MAX_VALUE
        })
    }

    fun endpointPostfix(userType: UserType)
            = when(userType) {
        UserType.GITHUB -> "github"
        else -> throw IllegalArgumentException(userType.name)
    }
}

class GithubSuccessHandler(
        restTemplate: OAuth2RestTemplate
): AbstractSuccessHandler(restTemplate, UserType.GITHUB)
