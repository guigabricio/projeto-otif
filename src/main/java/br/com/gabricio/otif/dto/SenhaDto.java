package br.com.gabricio.otif.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class SenhaDto {
	
	@NotBlank(message = "Senha do usuário não informada.")
	@Size(min = 6, max = 12, message = "A Senha deve conter entre 6 a 12 caracteres.")
	private String senha;
}
