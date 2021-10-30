package br.com.gabricio.otif.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelNivelEntregaPrazo {

	private String periodo;
	
	private Integer idLoja;
	private String loja;
	
	private int volumesEntreguesNoPrazo;
	private int volumesEntreguesNaQtdeCorreta;
	private int qtde;
	
}
