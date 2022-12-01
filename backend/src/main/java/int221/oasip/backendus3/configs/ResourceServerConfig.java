package int221.oasip.backendus3.configs;

import int221.oasip.backendus3.configs.aad.AadConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final AadConfiguration aadConfiguration;
    private final OasipJwtAuthenticationProviderConfigurer oasipJwtAuthenticationProviderConfigurer;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        MyJwtIssuerAuthenticationManagerResolver authenticationManagerResolver = new MyJwtIssuerAuthenticationManagerResolver();
        authenticationManagerResolver
                .register(aadConfiguration)
                .register(oasipJwtAuthenticationProviderConfigurer);

        http
                .authorizeHttpRequests()
                .antMatchers("/api/auth/private").authenticated()
                .antMatchers("/api/users/**").hasAnyAuthority("ROLE_ADMIN", "APPROLE_Admin")
                .antMatchers("/api/auth/match").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/events").permitAll()
                .antMatchers(HttpMethod.GET, "/api/events/allocatedTimeSlots").permitAll()
                .antMatchers("/api/events/test-lecturer").hasRole("LECTURER")
                .antMatchers(HttpMethod.GET, "/api/events/files/**").permitAll()
                .antMatchers("/api/events/**").authenticated()
                .anyRequest().permitAll()
                .and()
                .csrf().disable()
                .oauth2ResourceServer()
                .authenticationManagerResolver(authenticationManagerResolver)
                .and()
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        provider.setHideUserNotFoundExceptions(false);
        return provider;
    }
}
