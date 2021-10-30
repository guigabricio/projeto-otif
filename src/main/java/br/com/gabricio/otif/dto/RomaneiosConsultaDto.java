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
public class RomaneiosConsultaDto {

	private String id;

	private Integer idAgrupamentoRomaneio;

	private Integer romaneioNr;

	private Integer notaNr;

	private Integer volumeNr;

	private Integer idLojaDestino;

	private String lojaDestino;

	private String statusEntrega;

	private Integer volumeEntregue;

	private Integer idTransportadora;

	private String transportadora;

	private String cnpjTransportadora;

	private Integer rotaId;

	private String rotaNome;

	private String nomeMotorista;

	private String cpfMotorista;

	private String cnpjTransportadorMotorista;

	private String nomeUsuarioGuarita;

	private String cpfUsuarioGuarita;

	private String nomeMotoristaEntrega;

	private String cpfMotoristaEntrega;

	private String cnpjTransportadoraEntrega;

	private String usuarioInclusao;

	private String usuarioAtualizacao;

	@JsonSerialize(using = TrataDataSerializer.class)
	private LocalDateTime datahoraSaidaFabrica;
	
	@JsonSerialize(using = TrataDataSerializer.class)
	private LocalDateTime datahoraSaidaRomaneio;
	
	@JsonSerialize(using = TrataDataSerializer.class)
	private LocalDateTime datahoraPrevisaoEntrega;
	
	@JsonSerialize(using = TrataDataSerializer.class)
	private LocalDateTime datahoraEntregue;
	
	@JsonSerialize(using = TrataDataSerializer.class)
	private LocalDateTime datahoraRegistroCarregamento;
	
	@JsonSerialize(using = TrataDataSerializer.class)
	private LocalDateTime datahoraConfirmacaoEntrega;
	
	@JsonSerialize(using = TrataDataSerializer.class)
	private LocalDateTime datahoraInclusao;
	
	@JsonSerialize(using = TrataDataSerializer.class)
	private LocalDateTime datahoraAtualizacao;
}
