package com.example.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter

@Configuration
@EnableWebSecurity
class SecurityConfiguration {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .headers { headers ->
                headers
                    // HSTS (ALB終端でHTTPS → HTTP変換されるため、プロキシ対応が必要)
                    .httpStrictTransportSecurity { hstsConfig ->
                        hstsConfig
                            .maxAgeInSeconds(31536000) // 1年
                            .includeSubDomains(true)
                            .preload(true)
                    }
                    // X-Frame-Options (クリックジャッキング対策)
                    .frameOptions { frameOptions ->
                        frameOptions.deny()
                    }
                    // X-Content-Type-Options (MIME sniffing攻撃対策)
                    .contentTypeOptions { }
                    // Referrer Policy
                    .referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
                    // X-XSS-Protection (レガシーブラウザ対応)
                    .and()
                    .addHeaderWriter { request, response ->
                        response.setHeader("X-XSS-Protection", "1; mode=block")
                        // Permissions Policy (旧Feature Policy)
                        response.setHeader(
                            "Permissions-Policy", 
                            "geolocation=(), camera=(), microphone=(), payment=(), usb=(), vr=(), accelerometer=(), gyroscope=(), magnetometer=(), clipboard-read=(), clipboard-write=()"
                        )
                        // Content Security Policy
                        response.setHeader(
                            "Content-Security-Policy",
                            "default-src 'self'; " +
                            "script-src 'self'; " +
                            "style-src 'self' 'unsafe-inline'; " +
                            "img-src 'self' data: https:; " +
                            "connect-src 'self'; " +
                            "font-src 'self'; " +
                            "object-src 'none'; " +
                            "media-src 'self'; " +
                            "frame-src 'none'"
                        )
                    }
            }
            .authorizeHttpRequests { auth ->
                auth
                    // ヘルスチェックエンドポイント（ALB用）
                    .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                    // GraphQLエンドポイント（将来的に認証必須にする予定）
                    .requestMatchers("/graphql").permitAll()
                    // その他すべてのリクエストは認証必須
                    .anyRequest().authenticated()
            }
            .csrf { csrf ->
                // GraphQL APIのためCSRFを無効化（JWTで認証予定）
                csrf.disable()
            }
            .cors { cors ->
                // CORSは application.yml で設定
                cors.configurationSource { null }
            }
            .build()
    }
}