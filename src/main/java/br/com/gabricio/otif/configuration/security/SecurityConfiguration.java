package br.com.gabricio.otif.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.gabricio.otif.repositories.UsuariosRepository;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	AutenticacaoService autenticacaoService;
	
	@Autowired
	TokenService tokenService;
	
	@Autowired
	UsuariosRepository usuarioRepository;
	
	
	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	//configurar autenticacao
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(autenticacaoService).passwordEncoder(new BCryptPasswordEncoder());
	}

	//configuracao de autorizacao(url e perfil de acesso
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and()
		.csrf().disable()
		.authorizeRequests()
		.antMatchers(HttpMethod.POST, "/login").permitAll()
		.antMatchers(HttpMethod.POST, "/usuarios").permitAll()
		.antMatchers(HttpMethod.PUT, "/usuarios/solicitacao-recuperacao-senha/**").permitAll()
		.antMatchers(HttpMethod.PUT, "/usuarios/recuperar-senha/**").permitAll()
		.antMatchers(HttpMethod.GET, "/usuarios/**/status").permitAll()
		.antMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll()
		.anyRequest().authenticated()
		.and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and().addFilterBefore(new AutenticacaoViaTokenFilter(tokenService, usuarioRepository), UsernamePasswordAuthenticationFilter.class)
		;
	}


	//configuracao de recursos estaticos(css. js, png)
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
		.antMatchers(
				"/v2/api-docs",
				"/swagger-resources/**",
				"/swagger-ui.html**",
				"/webjars/**");
	}
	
	

}


