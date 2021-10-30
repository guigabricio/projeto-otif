package br.com.gabricio.otif.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelNivelEntregaTransportadoraResponseDto {

	private String periodo;
	
	private String rota;
	private String filial;
	private String transportadora;

	private double volumesEntreguesNoPrazo;
	private double volumesEntreguesNaQtdeCorreta;

	private double indicador;
}
