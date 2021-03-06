package com.prueba.usuarios.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.prueba.usuarios.filter.JwtFilter;

import lombok.RequiredArgsConstructor;

@SuppressWarnings("deprecation")
@Configuration @EnableWebSecurity @RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private final UserDetailsService userDetailsService;
	
	@Autowired
	private JwtFilter jwtFilter;
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

	@Override @Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		
		return super.authenticationManager();
	}
    
	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().disable().authorizeRequests().antMatchers("/authenticate").permitAll().anyRequest().authenticated().
		and().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint()).and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
	}
	
	@Override
    public void configure(WebSecurity web) throws Exception {
        web
            .ignoring()
            .antMatchers("/h2-console/**");
    }
    
	@Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){
        return new CustomAuthenticationEntryPoint();
    }
    
    


	
}
