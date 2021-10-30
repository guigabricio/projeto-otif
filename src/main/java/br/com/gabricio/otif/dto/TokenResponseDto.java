package br.com.gabricio.otif.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponseDto {

	private String access_token;
	private String token_type;
	private Long expiration;
	private UsuarioTokenResponseDto usuario;
}
