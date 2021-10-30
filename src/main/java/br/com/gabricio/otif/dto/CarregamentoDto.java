package br.com.gabricio.otif.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class CarregamentoDto {
	
	@NotNull(message = "Id. do agrupamento deve ser informado.")
	private Integer idAgrupamento;

}
