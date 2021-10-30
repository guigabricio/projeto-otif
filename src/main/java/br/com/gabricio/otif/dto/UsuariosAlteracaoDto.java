package br.com.gabricio.otif.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuariosAlteracaoDto {

	private String nome;

	private String cnpj;

	private String perfil;
	
	private String statusUsuario;
	
	private String senha;
	
}
