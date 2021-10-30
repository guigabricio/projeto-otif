package br.com.gabricio.otif.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "romaneios")
public class RomaneiosModel {

	@Id
	private String id;

	@Indexed
	private Integer idAgrupamentoRomaneio;

	@Indexed
	private Integer romaneioNr;

	@Indexed
	private Integer notaNr;

	@Indexed
	private Integer volumeNr;

	@Indexed
	private LocalDateTime datahoraSaidaRomaneio;

	private Integer idLojaDestino;

	private String lojaDestino;

	private String statusEntrega;

	@Indexed
	private Integer volumeEntregue;

	private Integer idTransportadora;

	private String transportadora;

	private String cnpjTransportadora;

	@Indexed
	private LocalDateTime datahoraEntregue;

	private Integer rotaId;

	private String rotaNome;

	// romaneios/registrar-carregamento
	private String nomeMotorista;

	private String cpfMotorista;

	private String cnpjTransportadorMotorista;

	private LocalDateTime datahoraRegistroCarregamento;

	// romaneios/registrar-saida
	private String nomeUsuarioGuarita;

	private String cpfUsuarioGuarita;

	private LocalDateTime datahoraSaidaFabrica;

	private LocalDateTime datahoraPrevisaoEntrega;

	// romaneios/recibo-entrega
	private String nomeMotoristaEntrega;

	private String cpfMotoristaEntrega;

	private String cnpjTransportadoraEntrega;

	private LocalDateTime datahoraConfirmacaoEntrega;

	private LocalDateTime datahoraInclusao;

	private String usuarioInclusao;

	private LocalDateTime datahoraAtualizacao;

	private String usuarioAtualizacao;

	public RomaneiosModel(Integer idAgrupamentoRomaneio, Integer romaneioNr, Integer notaNr, Integer volumeNr,
			LocalDateTime datahoraSaidaRomaneio, Integer idLojaDestino, String lojaDestino, String statusEntrega,
			Integer volumeEntregue, Integer idTransportadora, String transportadora, String cnpjTransportadora,
			LocalDateTime datahoraEntregue, Integer rotaId, String rotaNome, LocalDateTime datahoraInclusao,
			String usuarioInclusao) {
		super();
		this.idAgrupamentoRomaneio = idAgrupamentoRomaneio;
		this.romaneioNr = romaneioNr;
		this.notaNr = notaNr;
		this.volumeNr = volumeNr;
		this.datahoraSaidaRomaneio = datahoraSaidaRomaneio;
		this.idLojaDestino = idLojaDestino;
		this.lojaDestino = lojaDestino;
		this.statusEntrega = statusEntrega;
		this.volumeEntregue = volumeEntregue;
		this.idTransportadora = idTransportadora;
		this.transportadora = transportadora;
		this.cnpjTransportadora = cnpjTransportadora;
		this.datahoraEntregue = datahoraEntregue;
		this.rotaId = rotaId;
		this.rotaNome = rotaNome;
		this.datahoraInclusao = datahoraInclusao;
		this.usuarioInclusao = usuarioInclusao;
	}

}
