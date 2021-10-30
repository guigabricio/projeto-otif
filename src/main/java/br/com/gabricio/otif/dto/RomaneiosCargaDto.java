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
public class RomaneiosCargaDto {

	@NotNull(message = "AGRUPAMENTOROMANEIO_ID não informado.")
	@JsonProperty("AGRUPAMENTOROMANEIO_ID")
	Integer idAgrupamentoRomaneio;

	@NotNull(message = "ROMANEIO_NR não informado.")
	@JsonProperty("ROMANEIO_NR")
	Integer romaneioNr;
	
	@NotNull(message = "NOTA_NR não informado.")
	@JsonProperty("NOTA_NR")
	Integer notaNr;

	@NotNull(message = "VOLUME_NR não informado.")
	@JsonProperty("VOLUME_NR")
	Integer volumeNr;
	
	@NotBlank(message = "DATAHORA_SAIDA_ROMANEIO não informado.")
	@JsonProperty("DATAHORA_SAIDA_ROMANEIO")
	@ValidarDataHora(message = "DATAHORA_SAIDA_ROMANEIO inválida")
	String datahoraSaidaRomaneio;

	@NotBlank(message = "LOJA_DESTINO não informado.")
	@JsonProperty("LOJA_DESTINO")
	String lojaDestino;
	
	@NotNull(message = "LOJA_DESTINO_ID não informado.")
	@JsonProperty("LOJA_DESTINO_ID")
	Integer idLojaDestino;
	
	@NotBlank(message = "STATUS_ENTREGA não informado.")
	@JsonProperty("STATUS_ENTREGA")
	String statusEntrega;
	
	@NotNull(message = "VOLUME_ENTREGUE não informado.")
	@JsonProperty("VOLUME_ENTREGUE")
	Integer volumeEntregue;
	
	@NotNull(message = "TRANSPORTADORA_ID não informado.")
	@JsonProperty("TRANSPORTADORA_ID")
	Integer idTransportadora;

	@NotBlank(message = "TRANSPORTADORA_CNPJ não informado.")
	@JsonProperty("TRANSPORTADORA_CNPJ")
	String cnpjTransportadora;

	@NotBlank(message = "TRANSPORTADORA não informado.")
	@JsonProperty("TRANSPORTADORA")
	String transportadora;
	
	@JsonProperty("DATAHORA_ENTREGUE")
	@ValidarDataHora(message = "DATAHORA_ENTREGUE inválida")
	String datahoraEntregue;
	
	@NotNull(message = "ROTA_ID não informado.")
	@JsonProperty("ROTA_ID")
	Integer rotaId;
	
	@NotBlank(message = "ROTA_NOME não informado.")
	@JsonProperty("ROTA_NOME")
	String rotaNome;
}
