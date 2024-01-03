package br.com.marianadmoreira.bibliotecaob.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import br.com.marianadmoreira.bibliotecaob.repository.IRoleRepository;
import br.com.marianadmoreira.bibliotecaob.repository.IUserRepository;
import lombok.RequiredArgsConstructor;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@DependsOn("dataInitializer")
public class SecurityConfig {
    
    @Autowired
    private final CustomUserDetailsService userDetailsService;

    @Autowired
    IUserRepository userRepository;

    @Autowired
    IRoleRepository roleRepository;

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());

    }

    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
        .authorizeHttpRequests()
        .requestMatchers("/", "/home","/css/**","/assets/**").permitAll()
        .requestMatchers("/dashboard").hasAnyRole("ADMIN","USER")
        .anyRequest().authenticated()
        .and()
        .formLogin(login -> login
        .loginPage("/login")
        .defaultSuccessUrl("/dashboard",true).permitAll())
        .logout(logout -> logout
        .logoutSuccessUrl("/")
        .invalidateHttpSession(true)
        .clearAuthentication(true)
        .permitAll());

        return http.build();
    }
    
}
