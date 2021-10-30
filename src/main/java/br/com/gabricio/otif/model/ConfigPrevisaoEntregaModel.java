package br.com.gabricio.otif.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "config_previsao_entrega")
public class ConfigPrevisaoEntregaModel {

	@Id
	private String id;

	private String horaInicio;

	private String horaLimite;

	private Integer prazoEntrega;

	private LocalDateTime datahoraInclusao;

	private String usuarioInclusao;

	private LocalDateTime datahoraAtualizacao;

	private String usuarioAtualizacao;
}
