package br.com.olist.olistly.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import br.com.olist.olistly.model.JWTInfo;
import br.com.olist.olistly.security.filter.JWTAuthenticationFilter;
import br.com.olist.olistly.security.filter.JWTLogoutFilter;
import br.com.olist.olistly.security.filter.JWTUsernamePasswordAuthenticationFilter;
import br.com.olist.olistly.security.service.LoginUserDetailsService;
import br.com.olist.olistly.service.system.SessionService;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private Environment env;
	private JWTInfo jwtInfo;
	private SessionService sessionService;
	private LoginUserDetailsService userDetailsService;
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public WebSecurityConfig(LoginUserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder, SessionService sessionService, JWTInfo jwtInfo) {
		this.jwtInfo = jwtInfo;
		this.sessionService= sessionService;
		this.userDetailsService = userDetailsService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors()
			.and()
				.csrf().disable().authorizeRequests()
				.antMatchers(HttpMethod.GET, "/public/url/view/**").permitAll()
				.antMatchers(HttpMethod.GET, "/public/user/activate/**").permitAll()
				.antMatchers(HttpMethod.GET, "/public/user/resetpassword/**").permitAll()
				.antMatchers(HttpMethod.POST, "/public/user/create").permitAll()
				.antMatchers(HttpMethod.POST, "/public/user/resetpassword").permitAll()
				.antMatchers(HttpMethod.PUT, "/public/user/resetpassword/**").permitAll()
				.anyRequest().authenticated()
			.and()
				.addFilter(new JWTUsernamePasswordAuthenticationFilter(authenticationManager(), sessionService))
				.addFilter(new JWTAuthenticationFilter(authenticationManager(), sessionService, jwtInfo))
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
				.logout()
				.logoutUrl("/logout")
				.logoutSuccessHandler(new JWTLogoutFilter(sessionService));
	}

	@Bean
	protected CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
	    configuration.setAllowCredentials(true);
	    configuration.setAllowedOrigins(Arrays.asList(env.getProperty("frontend.server")));
	    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
	    configuration.setAllowedHeaders(Arrays.asList("X-Requested-With","Origin","Content-Type","Accept","Authorization"));
	    configuration.setExposedHeaders(Arrays.asList("Access-Control-Allow-Headers", "Authorization, Access-Control-Allow-Headers, Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers"));

	    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", configuration);
	    return source;
	}
}
