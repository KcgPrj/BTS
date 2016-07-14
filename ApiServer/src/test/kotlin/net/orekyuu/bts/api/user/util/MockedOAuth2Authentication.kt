package net.orekyuu.bts.api.user.util

import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.OAuth2Request

class MockedOAuth2Authentication(clientId: String, val userName: String) : OAuth2Authentication(
        OAuth2Request(emptyMap(), clientId, emptyList(), false, emptySet(), emptySet(), "mock", emptySet(), emptyMap()),
        null) {
    override fun getName() = userName
}
