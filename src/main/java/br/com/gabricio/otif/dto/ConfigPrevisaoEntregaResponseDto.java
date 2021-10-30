package br.com.gabricio.otif.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.gabricio.otif.configuration.converters.TrataDataSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigPrevisaoEntregaResponseDto {

	private String id;

	private String horaInicio;

	private String horaLimite;

	private Integer prazoEntrega;

	@JsonSerialize(using = TrataDataSerializer.class)
	private LocalDateTime datahoraInclusao;

	private String usuarioInclusao;

	@JsonSerialize(using = TrataDataSerializer.class)
	private LocalDateTime datahoraAtualizacao;

	private String usuarioAtualizacao;
}
