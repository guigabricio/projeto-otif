package br.com.gabricio.otif.model;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "historico_leitura")
public class HistoricoLeituraModel {
	
	private String id;
	
	private Integer idLeitura;
	
	private String cpfUsuarioLogado;
	
	private TipoLeituraEnum tipoLeitura;
	
	private LocalDateTime dataHoraLeitura;

}
