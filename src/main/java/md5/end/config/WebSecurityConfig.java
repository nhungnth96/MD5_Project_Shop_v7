package md5.end.config;

import md5.end.exception.AccessDeniedException;
import md5.end.security.jwt.JwtEntryPoint;
import md5.end.security.jwt.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import md5.end.security.principal.UserDetailService;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // phân quyền trực tiếp
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private JwtEntryPoint jwtEntryPoint;
    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // cấu hình phân quyền đường dẫn
        http.cors().and().csrf().disable() // tắt cấu hình csrf
                .authorizeRequests()
                .antMatchers("/api/v1/auth/**").permitAll()
                .antMatchers("/api/v1/products/**").permitAll() // CRUD admin
                .antMatchers("/api/v1/brands/**").hasAnyRole("ADMIN")
                .antMatchers("/api/v1/categories/**").hasAnyRole("ADMIN")
                .antMatchers("/api/v1/specifications/**").hasAnyRole("ADMIN")
                .antMatchers("/api/v1/users/**").hasAnyRole("ADMIN","SELLER","BUYER")

                .antMatchers("/api/v1/cart/**").hasRole("BUYER")

                .antMatchers("/api/v1/orders/**").hasAnyRole("ADMIN","SELLER","BUYER")

                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtEntryPoint)
                .accessDeniedHandler(new AccessDeniedException())
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // yêu cầu người dùng luôn xác thực = token
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
