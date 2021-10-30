package br.com.gabricio.otif.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.gabricio.otif.model.PerfilEnum;
import br.com.gabricio.otif.validation.ValidarCpfCnpj;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class UsuariosDto {

	@NotBlank(message = "Nome do usuário não informado.")
	@Size(min = 1, max = 30, message = "O nome deve conter no máximo 30 caracteres.")
	private String nome;

	@NotBlank(message = "CPF do usuário não informado.")
    @Size(min = 11, max = 14, message = "O Cpf deve conter de 11 a 15 caracteres quando formatado.")
	@ValidarCpfCnpj
	private String cpf;

	@NotBlank(message = "CNPJ não informado!")
	@Size(min = 14, max = 18, message = "O CNPJ deve conter de 14 a 18 caracteres quando formatado.")
	@ValidarCpfCnpj
	private String cnpj;

	@NotNull(message = "Perfil não informado.")
	private PerfilEnum perfil;

	@NotBlank(message = "Senha do usuário não informada.")
	@Size(min = 6, max = 12, message = "A Senha deve conter entre 6 a 12 caracteres.")
	private String senha;

}
