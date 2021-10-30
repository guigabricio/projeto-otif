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
public class SaidaDto {
	
	@NotNull(message = "Id. Agrupamento não informado.")
	private Integer idAgrupamento;

}
