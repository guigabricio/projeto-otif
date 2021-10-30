/**
 * Autor: gabricio
 * Criado em: 2020-10-24
 */
package br.com.gabricio.otif.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class ListaCargaUsuariosDto {
	
	@Valid
	@NotEmpty(message = "Lista de usuários não pode ser vazia.")
	List<UsuariosCargaDto> data;

}
