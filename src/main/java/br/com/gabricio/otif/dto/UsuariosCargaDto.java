/**
 * Autor: gabricio
 * Criado em: 2020-10-24
 */
package br.com.gabricio.otif.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.gabricio.otif.validation.ValidarDataHora;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class UsuariosCargaDto {

	@NotNull(message = "USUARIO_ID não informado.")
	@JsonProperty("USUARIO_ID")
	Integer idUsuario;
	
	@NotBlank(message = "USUARIO_NOME não informado.")
	@JsonProperty("USUARIO_NOME")
	String nomeUsuario;

	@NotBlank(message = "USUARIO_CPF não informado.")
	@JsonProperty("USUARIO_CPF")
	String cpfUsuario;

	@NotBlank(message = "USUARIO_PASSWORD não informado.")
	@JsonProperty("USUARIO_PASSWORD")
	String senhaUsuario;

	@NotBlank(message = "DATAHORA_CRIACAO não informado.")
	@JsonProperty("DATAHORA_CRIACAO")
	@ValidarDataHora(message = "DATAHORA_CRIACAO inválida.")
	String dataHoraCriacao;

	@NotBlank(message = "DATAHORA_ATUALIZAÇÃO não informado.")
	@JsonProperty("DATAHORA_ATUALIZACAO")
	@ValidarDataHora(message = "DATAHORA_ATUALIZACAO inválida.")
	String dataHoraAtualizacao;

	@NotBlank(message = "STATUS não informado.")
	@JsonProperty("STATUS")
	String status;
	
}
