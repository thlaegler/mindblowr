// package io.laegler.mindblowr.config;
//
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.context.annotation.Primary;
// import org.springframework.security.oauth2.provider.OAuth2Authentication;
// import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
// import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
// import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
// import java.util.Map;
//
// @Slf4j
// @Configuration
// public class JwtTokenConfig {
//
// @Value("${spring.application.name}")
// private String resourceId;
//
// @Value("${oauth2.tokenKey}")
// private String tokenSigningKey;
//
// @Value("${oauth2.accessTokenValidity:30}")
// private int accessTokenValiditySeconds;
//
// @Value("${oauth2.refreshTokenValidity:60}")
// private int refreshTokenValiditySeconds;
//
// @Bean
// @Primary
// public DefaultTokenServices tokenServices() {
// DefaultTokenServices tokenServices = new DefaultTokenServices();
// tokenServices.setTokenStore(tokenStore());
// tokenServices.setSupportRefreshToken(true);
// tokenServices.setReuseRefreshToken(false);
// tokenServices.setTokenEnhancer(tokenConverter());
// tokenServices.setRefreshTokenValiditySeconds(refreshTokenValiditySeconds);
// tokenServices.setAccessTokenValiditySeconds(accessTokenValiditySeconds);
// return tokenServices;
// }
//
// @Bean
// @Primary
// public JwtTokenStore tokenStore() {
// JwtTokenStore tokenStore = new JwtTokenStore(tokenConverter());
// return tokenStore;
// }
//
// @Bean
// public JwtAccessTokenConverter tokenConverter() {
// JwtAccessTokenConverter converter = new JwtAccessTokenConverter() {
// @Override
// public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
// OAuth2Authentication auth = super.extractAuthentication(map);
// auth.setDetails(map);
// return auth;
// }
// };
// converter.setSigningKey(tokenSigningKey);
// return converter;
// }
//
// }
