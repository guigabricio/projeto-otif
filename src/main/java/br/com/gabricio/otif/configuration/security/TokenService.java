package br.com.gabricio.otif.configuration.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.gabricio.otif.model.UsuariosModel;
import br.com.gabricio.otif.utils.CpfCnpjUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {
	
	@Value("${otif.jwt.expiration}")
	private String expiration;
	
	@Value("${otif.jwt.secret}")
	private String secret;
	

	public String gerarToken(Authentication authentication) {
		
		UsuariosModel usuario = (UsuariosModel) authentication.getPrincipal();
		
		Date agora = new Date();
		
		Date dataExpiracao = new Date(agora.getTime() + Long.parseLong(expiration));
		
		return Jwts.builder()
				.setIssuer("API OTIF")
				.setSubject(CpfCnpjUtil.removeFormatacao(usuario.getCpf()))
				.setIssuedAt(agora)
				.setExpiration(dataExpiracao)
				.signWith(SignatureAlgorithm.HS256, secret)
				.compact()
				;
	}

	public boolean isTokenValido(String token) {
		try {
			Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String getCpfUsuario(String token) {
		Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		return claims.getSubject();
	}

}
