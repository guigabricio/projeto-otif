package br.com.gabricio.otif.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class LoginDto {

	@NotBlank(message = "Cpf não informado.")
	private String cpf;
	
	@NotBlank(message = "Senha não informada.")
	private String senha;
}
