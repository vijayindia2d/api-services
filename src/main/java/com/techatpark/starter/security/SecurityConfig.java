package com.techatpark.starter.security;

import com.techatpark.starter.security.filter.TokenFilter;
import com.techatpark.starter.security.utils.TokenUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * declare tokenUtil.
     */
    private final TokenUtil tokenUtil;

    /**
     * constructor.
     *
     * @param aTokenUtil
     */
    public SecurityConfig(final TokenUtil aTokenUtil) {
        this.tokenUtil = aTokenUtil;
    }

    /**
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/swagger-ui.html", "/swagger-ui/**",
                "/v3/api-docs/**", "/resources/**",
                "/static/**", "/css/**", "/js/**", "/images/**",
                "/practices/**",
                "/courses/**", "/courses/**/**", "/courses/**/**/**/",
                "/subjects/**", "/books/**");
    }

    /**
     * @param httpSecurity
     * @throws Exception
     */
    @Override
    protected void configure(final HttpSecurity httpSecurity)
            throws Exception {
        //@formatter:off
        httpSecurity
                .requiresChannel()
                    .anyRequest()
                    .requiresSecure()
                .and()
                .httpBasic().disable()
                .csrf().disable()
                .logout().disable()
                .formLogin().disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                    .antMatchers("/", "/api/info",
                            "/api/auth/login")
                        .permitAll()
                    .anyRequest()
                        .authenticated();
        //@formatter:on

        httpSecurity.addFilterBefore(new TokenFilter(tokenUtil),
                UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * @return
     */
    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
                User.withUsername("tom")
                        .password(passwordEncoder().encode("password"))
                        .roles("USER").build(),
                User.withUsername("jerry")
                        .password(passwordEncoder().encode("password"))
                        .roles("USER").build());
    }

    /**
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * @return
     * @throws Exception
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
