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
public class RelFaltaVolumeResponseDto {

	private String periodo;
	private Integer agrupamentoRomaneioId;
	private Integer romaneioNr;
	private Integer notaNr;
	private Integer faltaVolume;
	private Integer volumeEntregue;
	private String volumeEntregueDesc;
	private String lojaDestino;
	private String rotaNome;
	private String transportadora;
	private String statusEntrega;

	@JsonSerialize(using = TrataDataSerializer.class)
	private LocalDateTime dataHoraSaidaRomaneio;

	@JsonSerialize(using = TrataDataSerializer.class)
	private LocalDateTime dataHoraRegistroCarregamento;

	@JsonSerialize(using = TrataDataSerializer.class)
	private LocalDateTime dataHoraSaidaFabrica;

	@JsonSerialize(using = TrataDataSerializer.class)
	private LocalDateTime datahoraPrevisaoEntrega;

	@JsonSerialize(using = TrataDataSerializer.class)
	private LocalDateTime datahoraEntregue;

	@JsonSerialize(using = TrataDataSerializer.class)
	private LocalDateTime datahoraSaidaFabrica;

	@JsonSerialize(using = TrataDataSerializer.class)
	private LocalDateTime datahoraSaidaRomaneio;

	@JsonSerialize(using = TrataDataSerializer.class)
	private LocalDateTime datahoraRegistroCarregamento;

	@JsonSerialize(using = TrataDataSerializer.class)
	private LocalDateTime datahoraConfirmacaoEntrega;
	
}
