package br.com.gabricio.otif.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import br.com.gabricio.otif.validation.ValidarHora;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class ConfigPrevisaoEntregaDto {

	@ValidarHora(message = "Hora inicio inválida.")
	private String horaInicio;

	@ValidarHora(message = "Hora limite inválida.")
	private String horaLimite;

	@NotNull(message = "Prazo de entrega não informado")
	private Integer prazoEntrega;
}
