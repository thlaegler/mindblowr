// package io.laegler.mindblowr.config;
//
// import static org.slf4j.LoggerFactory.getLogger;
// import static org.springframework.http.HttpMethod.OPTIONS;
// import org.slf4j.Logger;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.core.annotation.Order;
// import
// org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.builders.WebSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import
// org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
// import org.springframework.security.config.http.SessionCreationPolicy;
//
// @Configuration
// @EnableWebSecurity // (debug = true)
// @EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // jsr250Enabled =
// true)
// @Order(98)
// public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//
// private static final Logger LOG = getLogger(WebSecurityConfig.class);
//
// @Override
// public void configure(HttpSecurity http) throws Exception {
// LOG.trace("Configuring: " + this.getClass().getName());
//
// http//
// .csrf().disable()//
// .anonymous().disable()//
// // .cors().and()//
// .formLogin().disable()//
// .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER).and()//
// .authorizeRequests()//
// .antMatchers(OPTIONS, "/**").permitAll()//
// .antMatchers("/actuator/**", //
// "/error", //
// "/swagger-ui.html", //
// "/swagger-resources/**", //
// "/webjars/**", //
// "/v2/api-docs/**", //
// "/favicon.ico")
// .permitAll()//
// .anyRequest().authenticated();
// }
//
// @Override
// public void configure(WebSecurity web) throws Exception {
// web//
// .ignoring().antMatchers(OPTIONS, "/**").and()//
// .ignoring().antMatchers("/actuator/**", //
// "/swagger-ui.html", //
// "/swagger-resources/**", //
// "/webjars/**", //
// "/v2/api-docs/**", //
// "/favicon.ico");
// }
//
// }
