package br.com.gabricio.otif.dto;

import br.com.gabricio.otif.model.PerfilEnum;
import br.com.gabricio.otif.model.StatusUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuariosAlteracaoResponseDto {

	private String nome;

	private String cpf;

	private String cnpj;

	private PerfilEnum perfil;
	
	private StatusUsuario statusUsuario;
	
}
