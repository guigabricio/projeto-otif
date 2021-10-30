package br.com.gabricio.otif.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelNivelEntregaTransportadora {

	private String periodo;
	private String rota;
	private String filial;
	private String transportadora;

	private int volumesEntreguesNoPrazo = 0;
	private int volumesEntreguesNaQtdeCorreta = 0;
	private int qtdeEntregas = 0;
}
