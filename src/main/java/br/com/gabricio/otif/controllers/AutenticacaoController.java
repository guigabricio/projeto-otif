package br.com.gabricio.otif.controllers;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.gabricio.otif.configuration.security.TokenService;
import br.com.gabricio.otif.dto.LoginDto;
import br.com.gabricio.otif.dto.ObjectWrapper;
import br.com.gabricio.otif.dto.TokenResponseDto;
import br.com.gabricio.otif.dto.UsuarioTokenResponseDto;
import br.com.gabricio.otif.exception.UsuarioSemAcessoException;
import br.com.gabricio.otif.model.PerfilEnum;
import br.com.gabricio.otif.model.StatusUsuario;
import br.com.gabricio.otif.model.UsuariosModel;
import br.com.gabricio.otif.services.UsuariosService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "Autenticação de Usuário", tags = "Autenticação", description = "Autenticação de usuário.")
public class AutenticacaoController {

	@Value("${otif.jwt.expiration}")
	private String expiration;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	ObjectWrapper objectWrapper;

	@Autowired
	TokenService tokenService;

	@Autowired
	UsuariosService usuarioService;

	@ApiOperation(value = "Efetua login do usuário", notes = "Retorna o token do tipo Bearer", response = TokenResponseDto.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Sucesso.", response = TokenResponseDto.class),
			@ApiResponse(code = 400, message = "Dados de login inválidos."),
			@ApiResponse(code = 403, message = "Não autorizado."),
			@ApiResponse(code = 404, message = "Usuário não encontrado."),
			@ApiResponse(code = 500, message = "Erro interno do servidor") })
	@PostMapping("/login")
	public ResponseEntity<TokenResponseDto> autenticar(@RequestBody @Valid LoginDto login) throws UsuarioSemAcessoException {
		try {
			UsernamePasswordAuthenticationToken dadosLogin = objectWrapper.getUsernamePasswordAuthenticationToken(login);

			Authentication authentication = authenticationManager.authenticate(dadosLogin);

			String token = tokenService.gerarToken(authentication);

			UsuariosModel usuarioModel = (UsuariosModel) authentication.getPrincipal();

			if(usuarioModel.getStatusUsuario().equals(StatusUsuario.INATIVO)) {
				throw new UsuarioSemAcessoException("Usuário inativo.");		
			}

			Collection<? extends GrantedAuthority> authorities = usuarioModel.getAuthorities();
			PerfilEnum perfil = null;
			for (GrantedAuthority grantedAuthority : authorities) {
				perfil = (PerfilEnum) grantedAuthority;
			}

			UsuarioTokenResponseDto usuario = new UsuarioTokenResponseDto(
					usuarioModel.getId(), 
					usuarioModel.getNome(), 
					perfil,
					usuarioModel.getStatusUsuario());

			return new ResponseEntity<>(new TokenResponseDto(token, "Bearer", Long.parseLong(expiration), usuario), HttpStatus.OK);

		} catch (AuthenticationException e) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
	}
}