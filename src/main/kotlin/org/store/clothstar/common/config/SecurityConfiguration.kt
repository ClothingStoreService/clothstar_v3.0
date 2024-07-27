package org.store.clothstar.common.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.store.clothstar.common.config.jwt.JwtAuthenticationFilter

@EnableWebSecurity
@Configuration
class SecurityConfiguration(
    private val authenticationConfiguration: AuthenticationConfiguration,
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,

    ) {

}