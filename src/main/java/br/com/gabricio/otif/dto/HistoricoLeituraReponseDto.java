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
public class HistoricoLeituraReponseDto {

	private Integer idLeitura;
	
	private String tipoLeitura;

	@JsonSerialize(using = TrataDataSerializer.class)
	private LocalDateTime dataHoraLeitura;

}
