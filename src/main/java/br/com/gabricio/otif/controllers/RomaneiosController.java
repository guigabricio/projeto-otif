package br.com.gabricio.otif.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.gabricio.otif.dto.CarregamentoDto;
import br.com.gabricio.otif.dto.CarregamentoResponseDto;
import br.com.gabricio.otif.dto.EntregaArrayDto;
import br.com.gabricio.otif.dto.EntregaResponseDto;
import br.com.gabricio.otif.dto.RomaneiosConsultaDto;
import br.com.gabricio.otif.dto.SaidaDto;
import br.com.gabricio.otif.dto.SaidaResponseDto;
import br.com.gabricio.otif.services.RomaneiosService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@RestController
@Api(value = "Operações de romaneios", tags = "Romaneios", description = "Operações de romaneios")
@Slf4j
@Validated
public class RomaneiosController {
	
	@Autowired
	RomaneiosService romaneiosService;

	@ApiOperation(
			value = "Registro de Carregamento", 
			notes = "Atualiza o agrupamento de romaneios com Cpf e Nome do motorista e Cnpj da transpostadora ao qual o motorista pertence.",
			response = CarregamentoResponseDto.class
			)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Sucesso.", response = CarregamentoResponseDto.class),
			@ApiResponse(code = 403, message = "Não autorizado."),
			@ApiResponse(code = 404, message = "Agrupamento não encontrado."),
			@ApiResponse(code = 500, message = "Erro interno do servidor.") })
	@PostMapping("/romaneios/registrar-carregamento")
	public ResponseEntity<CarregamentoResponseDto> registroCarregamento(@Valid @RequestBody CarregamentoDto carregamentoDto) throws Exception {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String cpfUsarioLogado = authentication.getName();

			romaneiosService.registraCarregamento(carregamentoDto, cpfUsarioLogado);;
			
			CarregamentoResponseDto responseDto = new CarregamentoResponseDto("Registro de carregamento efetuado com sucesso.");
			
			return new ResponseEntity<>(responseDto, HttpStatus.OK);
		} catch (Exception ex) {
			log.error("registroCarregamento", ex);
			throw ex;
		}
	}
	
	@ApiOperation(
			value = "Registro de Sáida", 
			notes = "Atualiza os dados do agrupamento com o horário de saída.",
			response = SaidaResponseDto.class
			)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Sucesso.", response = SaidaResponseDto.class),
			@ApiResponse(code = 403, message = "Não autorizado."),
			@ApiResponse(code = 404, message = "Agrupamento não encontrado."),
			@ApiResponse(code = 500, message = "Erro interno do servidor.") })
	@PostMapping("/romaneios/registrar-saida")
	public ResponseEntity<SaidaResponseDto> registrarSaida(@Valid @RequestBody SaidaDto saidaDto) throws Exception {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String cpfUsarioLogado = authentication.getName();
			
			romaneiosService.registraSaida(saidaDto, cpfUsarioLogado);
			
			SaidaResponseDto responseDto = new SaidaResponseDto("Registro de saída efetuado com sucesso.");
			
			return new ResponseEntity<>(responseDto, HttpStatus.OK);
		} catch (Exception ex) {
			log.error("registrarSaida", ex);
			throw ex;
		}
	}
	
	@ApiOperation(
			value = "Recibo de entrega", 
			notes = "Atualiza os dados do romaneio com a data da entrega e os dados do usuário logado.",
			response = EntregaResponseDto.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Sucesso."),
			@ApiResponse(code = 403, message = "Não autorizado."),
			@ApiResponse(code = 404, message = "Romaneio não encontrado."),
			@ApiResponse(code = 500, message = "Erro interno do servidor.") })
	@PostMapping("/romaneios/registrar-entrega")
	public ResponseEntity<EntregaResponseDto> registrarEntrega(@Valid @RequestBody EntregaArrayDto entregaArrayDto) throws Exception {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String cpfUsarioLogado = authentication.getName();
			
			romaneiosService.registraEntrega(entregaArrayDto, cpfUsarioLogado);
			
			EntregaResponseDto responseDto = new EntregaResponseDto("Registro de entrega efetuado com sucesso.");
			
			return new ResponseEntity<>(responseDto, HttpStatus.OK);
		} catch (Exception ex) {
			log.error("registrarEntrega", ex);
			throw ex;
		}
	}
	
	
	@ApiOperation(
			value = "Consulta romaneio", 
			notes = "Consulta os dados do romaneio.",
			response = RomaneiosConsultaDto.class
			)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Sucesso.", response = RomaneiosConsultaDto.class),
			@ApiResponse(code = 403, message = "Não autorizado."),
			@ApiResponse(code = 404, message = "Romaneio não encontrado."),
			@ApiResponse(code = 500, message = "Erro interno do servidor.") })
	@GetMapping("/romaneios/registrar-carregamento/{idAgrupamento}")
	public ResponseEntity<List<RomaneiosConsultaDto>> consultaRomaneio(@PathVariable(value = "idAgrupamento") Integer idAgrupamento) throws Exception{
		try {
			List<RomaneiosConsultaDto> listaRomaneiosDto = romaneiosService.consultaRomaneio(idAgrupamento);

			return new ResponseEntity<>(listaRomaneiosDto, HttpStatus.OK);
		} catch (Exception ex) {
			log.error("registroCarregamento", ex);
			throw ex;
		}
	}
	
}
