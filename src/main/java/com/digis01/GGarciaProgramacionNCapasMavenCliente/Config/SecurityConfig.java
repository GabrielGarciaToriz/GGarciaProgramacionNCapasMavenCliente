package com.digis01.GGarciaProgramacionNCapasMavenCliente.Config;

import com.digis01.GGarciaProgramacionNCapasMavenCliente.Security.BackendAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
            BackendAuthenticationProvider authenticationProvider) {

        http
                .authenticationProvider(authenticationProvider)
                .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/css/**", "/js/**", "/img/**", "/login", "/error", "/403").permitAll()
                .requestMatchers(HttpMethod.GET, "/usuario", "/usuario/detail/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.POST, "/usuario/buscar").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/usuario/form", "/usuario/add", "/usuario/addDirection/**", "/usuario/cargar/**")
                .hasRole("ADMIN")
                .anyRequest().authenticated())
                .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/usuario", true)
                .permitAll())
                .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll())
                .exceptionHandling(ex -> ex.accessDeniedPage("/403"))
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}

