package org.store.clothstar.common.config

import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.annotation.web.configurers.*
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.store.clothstar.common.config.jwt.JwtAuthenticationFilter
import org.store.clothstar.common.config.jwt.JwtUtil

@EnableWebSecurity
@Configuration
class SecurityConfiguration(
    private val authenticationConfiguration: AuthenticationConfiguration,
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val jwtUtil: JwtUtil,
) {
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    @Throws(Exception::class)
    fun authenticationManager(): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun configure(): WebSecurityCustomizer {
        return WebSecurityCustomizer { web ->
            web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
        }
    }

    @Bean
    @Throws(java.lang.Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.cors { obj: CorsConfigurer<HttpSecurity> -> obj.disable() }
            .csrf { obj: CsrfConfigurer<HttpSecurity> -> obj.disable() }
            .httpBasic { obj: HttpBasicConfigurer<HttpSecurity> -> obj.disable() }
            .formLogin { obj: FormLoginConfigurer<HttpSecurity> -> obj.disable() }

        http.authorizeHttpRequests(
            Customizer { auth ->
                auth
                    .requestMatchers(
                        "/", "/login", "/userPage", "/sellerPage", "/adminPage", "/main",
                        "/v1/members/login", "/signup", "/v1/members/email/**", "/v1/access",
                        "/v1/categories/**", "/v1/products/**", "/v1/productLines/**", "/v2/productLines/**",
                        "/productLinePagingSlice", "/productLinePagingOffset",
                        "/v3/products/**", "v3/sellers/products/**",
                        "/v1/orderdetails", "/v1/orders", "membersPagingOffset", "membersPagingSlice",
                        "/v1/orderdetails", "/v1/orders", "/v2/orders", "/v3/orders", "/v1/orders/list",
                        "/v1/orders/list", "/ordersPagingOffset", "/ordersPagingSlice", "/v2/orders/list",
                        "/v1/seller/orders/**", "/v1/seller/orders", "/v1/orders/**", "/v1/orderdetails/**",
                        "/swagger-resources/**", "/swagger-ui/**", "/v3/api-docs/**", "/v1/members/auth/**",
                        "/auth/**", "/kakaoLogin/**", "/kakao_login_medium_narrow.png", "/v1/members?signUpType=KAKAO"
                        "/auth/**","/kakaoLogin/**", "/kakao_login_medium_narrow.png",
                        "/swagger-resources/**", "/swagger-ui/**", "/v3/api-docs/**", "/v1/members/auth/**",
                        "config-service/**"
                        "/auth/**","/kakaoLogin/**", "/kakao_login_medium_narrow.png", "/v1/members?signUpType=KAKAO",
                        "/auth/**", "/kakaoLogin/**", "/kakao_login_medium_narrow.png", "/v1/members?signUpType=KAKAO",
                        "/v1/members?signUpType=KAKAO",
                        "/v1/members/**",
                        "/v1/members/auth/**",
                    ).permitAll()
                    .requestMatchers(HttpMethod.POST, "/v1/members").permitAll()
                    .requestMatchers(HttpMethod.POST, "/v1/sellers/**").authenticated()
                    .requestMatchers("/seller/**", "/v1/sellers/**").hasRole("SELLER")
                    .requestMatchers("/admin/**", "/v1/admin/**").hasRole("ADMIN")
                    .requestMatchers("v2/members", "v3/members").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/v1/members").hasRole("ADMIN")
                    .anyRequest().authenticated()
            }
        )

        //JWT 토큰 인증 방식 사용하기에 session 유지 비활성화
        http.sessionManagement { sessionManagement ->
            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }

        //UsernamePasswordAuthenticationFilter 대신에 LoginFilter가 실행된다.
        //LoginFilter 이전에 jwtAhthenticationFilter가 실행된다.
        http.addFilterBefore(jwtAuthenticationFilter, LoginFilter::class.java)
        http.addFilterAt(
            LoginFilter(authenticationManager(), jwtUtil),
            UsernamePasswordAuthenticationFilter::class.java
        )

        return http.build()
    }
}