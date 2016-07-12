package net.orekyuu.bts.api.user.util

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class MockOAuth2Util {

    @Value("\${security.oauth2.client.clientId}")
    private lateinit var githubClientId: String

    fun enableGithubMock(githubUserId: String) {
        SecurityContextHolder.getContext().authentication = MockedOAuth2Authentication(githubClientId, githubUserId)
    }
}
