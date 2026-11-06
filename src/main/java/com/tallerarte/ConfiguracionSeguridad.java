package com.tallerarte;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class ConfiguracionSeguridad {

    // Las contrasenas se guardan cifradas con BCrypt
    @Bean
    public PasswordEncoder cifradoContrasena() {
        return new BCryptPasswordEncoder();
    }

       // Despues de iniciar sesion, cada rol va a su pagina
    @Bean
    public AuthenticationSuccessHandler redireccionPorRol() {
        return (request, response, autenticacion) -> {
            String destino = "/";
            for (GrantedAuthority rol : autenticacion.getAuthorities()) {
                if (rol.getAuthority().equals("ROLE_ADMINISTRADOR")) {
                    destino = "/admin/panel";
                } else if (rol.getAuthority().equals("ROLE_INSTRUCTOR")) {
                    destino = "/taller/mis-talleres";
                }
            }
            response.sendRedirect(request.getContextPath() + destino);
        };
    }

    // Reglas de acceso: que rutas son publicas y cuales exigen un rol
    @Bean
    public SecurityFilterChain reglasDeAcceso(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(acceso -> acceso
                .requestMatchers("/", "/login", "/registro", "/error").permitAll()
                .requestMatchers("/css/**", "/js/**", "/webjars/**").permitAll()
                .requestMatchers("/taller/catalogo", "/taller/detalle/**").permitAll()
                .requestMatchers("/taller/**").hasRole("INSTRUCTOR")
                .requestMatchers("/categoria/**", "/admin/**").hasRole("ADMINISTRADOR")
                .requestMatchers("/reserva/**").hasRole("CLIENTE")
                .anyRequest().authenticated())
                .formLogin(login -> login
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("correo")
                .passwordParameter("contrasena")
                .successHandler(redireccionPorRol())
                .failureUrl("/login?error")
                .permitAll())
                .logout(salida -> salida
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?salio")
                .permitAll());
        return http.build();
    }
}