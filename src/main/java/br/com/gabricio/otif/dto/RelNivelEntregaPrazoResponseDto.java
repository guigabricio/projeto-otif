package br.com.gabricio.otif.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelNivelEntregaPrazoResponseDto {

	private String periodo;
	
	private Integer idLoja;
	private String loja;
	
	private double volumesEntreguesNoPrazo;
	private double volumesEntreguesNaQtdeCorreta;
	private double indicador;
}
