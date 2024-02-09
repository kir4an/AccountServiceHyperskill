package account.config;

import account.model.UserRoleType;
import account.config.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.ResponseStatus;



@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    public SecurityConfig(RestAuthenticationEntryPoint restAuthenticationEntryPoint) {
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(val->val.authenticationEntryPoint(restAuthenticationEntryPoint))
                .exceptionHandling(ex->ex.accessDeniedHandler(getAccessDeniedHandler()))
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions().disable())
                .authorizeRequests(auth -> auth
                        .requestMatchers("/api/auth/signup").permitAll()
                        .requestMatchers("api/hr/signup").permitAll()
                        .requestMatchers("/api/intern/signup").permitAll()
                        .requestMatchers("/api/intern/access").permitAll()
                        .requestMatchers("/api/intern/refresh").permitAll()
                        .requestMatchers("/api/auth/changepass").authenticated()
                        .requestMatchers("/api/empl/payment").hasAnyRole("USER", "ACCOUNTANT")
                        .requestMatchers("/api/acct/**").hasRole("ACCOUNTANT")
                        .requestMatchers("/api/admin/**").hasRole("ADMINISTRATOR")
                        .requestMatchers("/api/security/**").hasRole("AUDITOR")
                        .requestMatchers("/api/intern/{task}").hasRole("INTERN")
                        .requestMatchers("/api/hr/addIntern").hasRole("HR")
                        .and().addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                )

                .sessionManagement(sessions -> sessions
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(13);
    }
    @Bean
    public AccessDeniedHandler getAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

}
