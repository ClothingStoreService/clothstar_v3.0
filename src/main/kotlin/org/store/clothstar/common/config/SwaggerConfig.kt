package org.store.clothstar.common.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    private val BEARER_TOKEN = "Bearer Token"
    private val BEARER = "Bearer"
    private val AUTHORIZATION = "Authorization"
    private val JWT = "JWT"

    @Bean
    fun groupedAllOpenApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("All")
            .pathsToMatch("/v1/**", "/v2/**", "/v3/**")
            .build()
    }

    @Bean
    fun groupedMemberOpenApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("Member")
            .pathsToMatch("/v1/members/**", "/v1/sellers/**")
            .build()
    }

    @Bean
    fun springShopOpenAPI(): OpenAPI {
        val info = Info().title("clothstar-v3 Project API")
            .description("의류 쇼핑몰 3차 프로젝트 입니다.")
            .version("v0.3")

        val apiKey = SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .`in`(SecurityScheme.In.HEADER)
            .name(AUTHORIZATION)
            .scheme(BEARER)
            .bearerFormat(JWT)

        val securityRequirement = SecurityRequirement()
            .addList(BEARER_TOKEN)

        return OpenAPI().info(info)
            .components(Components().addSecuritySchemes(BEARER_TOKEN, apiKey))
            .addSecurityItem(securityRequirement)
    }
}