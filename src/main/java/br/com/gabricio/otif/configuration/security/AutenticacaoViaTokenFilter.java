package br.com.gabricio.otif.configuration.security;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.gabricio.otif.model.UsuariosModel;
import br.com.gabricio.otif.repositories.UsuariosRepository;

public class AutenticacaoViaTokenFilter extends OncePerRequestFilter{

	TokenService tokenService;

	UsuariosRepository usuarioRepository;

	public AutenticacaoViaTokenFilter(TokenService tokenService, UsuariosRepository usuarioRepository) {
		super();
		this.tokenService = tokenService;
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "*");
		response.setHeader("Access-Control-Allow-Headers", "*");
		response.setHeader("Access-Control-Max-Age", "86400");

		String token = recuperarToken(request);

		boolean valido = tokenService.isTokenValido(token);
		if(valido) {
			autenticarUsuario(token);
		}

		filterChain.doFilter(request, response);
	}

	private void autenticarUsuario(String token) {
		String cpf = tokenService.getCpfUsuario(token);
		Optional<UsuariosModel> usuarioOptional = usuarioRepository.findByCpf(cpf);

		if(usuarioOptional.isPresent()) {
			UsuariosModel usuario = usuarioOptional.get();
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getPerfis());
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
	}

	private String recuperarToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");

		if(token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
			return null;
		}

		return token.substring(7, token.length());
	}

}
