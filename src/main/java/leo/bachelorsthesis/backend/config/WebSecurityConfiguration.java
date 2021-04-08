package leo.bachelorsthesis.backend.config;

import leo.bachelorsthesis.backend.authorization.AuthenticationFilter;
import leo.bachelorsthesis.backend.authorization.AuthorizationFilter;
import leo.bachelorsthesis.backend.authorization.CorsConfigFilter;
import leo.bachelorsthesis.backend.error.FilterChainExceptionHandler;
import leo.bachelorsthesis.backend.service.UserService.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final FilterChainExceptionHandler filterChainExceptionHandler;
    private final int hours;
    private final String secret;
    private final UserService userService;

    private static final String[] AUTH_WHITELIST = {
            "/error"
    };

    public WebSecurityConfiguration(
            PasswordEncoder passwordEncoder,
            @Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService,
            FilterChainExceptionHandler filterChainExceptionHandler,
            @Value("${lifespanInHours}") int hours,
            @Value("${secret}") String secret,
            UserService userService){
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.filterChainExceptionHandler = filterChainExceptionHandler;
        this.hours = hours;
        this.secret = secret;
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .antMatchers(HttpMethod.POST, "/users").permitAll()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers(HttpMethod.GET, "/image/**").permitAll()
                .anyRequest().authenticated()
                .and().addFilter(new AuthenticationFilter(authenticationManager(), secret, hours, userService))
                .addFilterBefore(new CorsConfigFilter(), AuthenticationFilter.class)
                .addFilterBefore(filterChainExceptionHandler, AuthenticationFilter.class)
                .addFilter(new AuthorizationFilter(authenticationManager(), secret))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}
