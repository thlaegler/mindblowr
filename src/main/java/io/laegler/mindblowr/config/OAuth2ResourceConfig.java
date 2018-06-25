// package io.laegler.mindblowr.config;
//
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.core.annotation.Order;
// import
// org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
// import
// org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
// import
// org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
// import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
//
// @Configuration
// @EnableResourceServer
// @Order(-1)
// public class OAuth2ResourceConfig extends ResourceServerConfigurerAdapter {
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
// @Autowired
// private JwtTokenStore tokenStore;
//
// @Override
// public void configure(ResourceServerSecurityConfigurer resources) {
// resources.tokenStore(tokenStore).resourceId(resourceId).stateless(true);
// }
//
// }
