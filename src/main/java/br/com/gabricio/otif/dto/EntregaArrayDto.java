package br.com.gabricio.otif.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class EntregaArrayDto {
	
	@NotEmpty(message = "Lista de de romaneios não pode ser vazia.")
	private List<EntregaDto> listaIdRomaneios;

}
