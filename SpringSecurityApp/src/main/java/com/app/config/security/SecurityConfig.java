package com.app.config.security;

import com.app.service.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

// Seguridad basica
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // @Autowired
    // AuthenticationConfiguration authenticationConfiguration;

    // Filtros
    // httpSecurity: objeto que pasa por los filtros
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // csrf =  falsificación de petición en sitios cruzados vulnerabilidad
        // trabajar la aplicacion sin estado
        // stateless =  el tiempo del usuario logueado va a depender del tiempo del token
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                // Loguear con usuario y contraseña
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(http -> {
                    // Configurar EndPoints publicos
                    http.requestMatchers(HttpMethod.GET, "/auth/get").permitAll();

                    // Configurar EndPoints Privados
                    http.requestMatchers(HttpMethod.POST, "auth/post").hasAnyAuthority("CREATE", "READ");



                    // denyAll y autentichated
                    // Rechaza todo endpoint
                    // http.anyRequest().denyAll();

                    // Configurar el resto de endpoints
                    // Cualquier autenticado
                    http.anyRequest().authenticated();

                })
                .build();
    }

    /*@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }
     */

    // AuthenticationManagmet= administra la autentificacion
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Proveedor de autenticacion
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailServiceImpl userDetailServiceImpl) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailServiceImpl);

        return provider;
    }

    //@Bean
    public PasswordEncoder passwordEncoder() {
        // NoOpPasswordEncoder = retorna una contraseña no encriptada, PARA PRUEBAS
        // return NoOpPasswordEncoder.getInstance();

        return new BCryptPasswordEncoder();

        // Esto si encripta las contraseñas
        // return BCryptPasswordEncoder();
    }

    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("1234"));
    }

    /*@Bean
    public UserDetailsService userDetailsService() {
        // Spring valida los usuarios a travez de UserDetails
        // Cuando traemos los datos de la base de datos se debe convertir en un UserDetails

        List<UserDetails> userDetailsList = new ArrayList<>();

        userDetailsList.add( User.withUsername("Breiner")
                .password("1234")
                .roles("ADMIN")
                .authorities("READ", "CREATE")
                .build());

        userDetailsList.add( User.withUsername("Ramiro")
                .password("0529")
                .roles("USER")
                .authorities("READ")
                .build());

        // InMemoryDetailsManager = guarda en la base de datos los usuarios en memoria
        return new InMemoryUserDetailsManager(userDetailsList);


    }

     */
}
