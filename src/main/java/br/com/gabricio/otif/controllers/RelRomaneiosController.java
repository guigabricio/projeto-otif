package br.com.gabricio.otif.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.gabricio.otif.dto.RelEntregaPendentesResponseDto;
import br.com.gabricio.otif.dto.RelFaltaVolumeResponseDto;
import br.com.gabricio.otif.dto.RelNivelEntregaPrazoResponseDto;
import br.com.gabricio.otif.dto.RelNivelEntregaTransportadoraResponseDto;
import br.com.gabricio.otif.dto.RelRomaneioCargaResponseDto;
import br.com.gabricio.otif.services.RelEntregasPendentesService;
import br.com.gabricio.otif.services.RelNivelEntregaPrazoService;
import br.com.gabricio.otif.services.RelNivelEntregaService;
import br.com.gabricio.otif.services.RelRomaneiosCargasService;
import br.com.gabricio.otif.services.RelFaltaVolumeService;
import br.com.gabricio.otif.utils.DataUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@RestController
@Api(value = "Relatórios", tags = "Relatórios", description = "Relatórios de romaneios.")
@Validated
@Slf4j
public class RelRomaneiosController {

	@Autowired
	RelFaltaVolumeService relRomaneiosService;
	
	@Autowired
	RelNivelEntregaService relNivelEntregaService;
	
	@Autowired
	RelNivelEntregaPrazoService relNivelEntregaPrazoService;
	
	@Autowired
	RelEntregasPendentesService relEntregasPendentesService;
	
	@Autowired
	RelRomaneiosCargasService relRomaneiosCargasService;

	@ApiOperation(
			value = "Romaneios de Carga", 
			notes = "Serão apresentados todos os romaneios gerados com as informações de Período, NF, data/hora de geração, Filial, Rota, Agrupamento, data/hora do carregamento, data/hora da saída da portaria, transportadora vinculada a carga  e o status de entrega"
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Sucesso."),
			@ApiResponse(code = 403, message = "Não autorizado."),
			@ApiResponse(code = 404, message = "Erro de validação dos campos."),
			@ApiResponse(code = 500, message = "Erro interno do servidor.")}
			)
	@GetMapping("/relatorios/carga-romaneios")
	public ResponseEntity<List<RelRomaneioCargaResponseDto>> relCargaRomaneios(
				@RequestParam(name = "dataInicio") String dataInicio, 
				@RequestParam(name = "dataFim") String dataFim ) throws Exception {
		try {
			
			dataInicio = dataInicio.concat(" 00:00:00");
			dataFim = dataFim.concat(" 23:59:59");

			List<RelRomaneioCargaResponseDto> relRomaneios = relRomaneiosCargasService.relRomaneiosCarga(
					DataUtil.converteStringToLocalDateTime(dataInicio, DataUtil.DDMMYYYY_PADRAO), 
					DataUtil.converteStringToLocalDateTime(dataFim, DataUtil.DDMMYYYY_PADRAO));

			return new ResponseEntity<>(relRomaneios, HttpStatus.OK);
		} catch (Exception ex) {
			log.error("relCargaRomaneios", ex);
			throw ex;
		}
	}


	@ApiOperation(
			value = "Entregas pendentes", 
			notes = "Serão apresentados os romaneios que ainda se encontram pendentes de entrega. Com as informações de Período, NF, data/hora de geração, Filial, Rota, Agrupamento, data/hora do carregamento, data/hora da saída da portaria, além da transportadora vinculada a carga."
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Sucesso."),
			@ApiResponse(code = 403, message = "Não autorizado."),
			@ApiResponse(code = 404, message = "Erro de validação dos campos."),
			@ApiResponse(code = 500, message = "Erro interno do servidor.")}
			)
	@GetMapping("/relatorios/entregas-pendentes")
	public ResponseEntity<List<RelEntregaPendentesResponseDto>> relEntregasPendentes( @RequestParam(name = "dataInicio") String dataInicio, @RequestParam(name = "dataFim") String dataFim ) throws Exception {
		try {
			
			dataInicio = dataInicio.concat(" 00:00:00");
			dataFim = dataFim.concat(" 23:59:59");

			List<RelEntregaPendentesResponseDto> relEntregasPendentes = relEntregasPendentesService.relEntregasPendentes(
					DataUtil.converteStringToLocalDateTime(dataInicio,DataUtil.DDMMYYYY_PADRAO), 
					DataUtil.converteStringToLocalDateTime(dataFim,DataUtil.DDMMYYYY_PADRAO));

			return new ResponseEntity<>(relEntregasPendentes, HttpStatus.OK);
		} catch (Exception ex) {
			log.error("relEntregasPendentes", ex);
			throw ex;
		}
	}

	@ApiOperation(
			value = "Falta de volume", 
			notes = "De acordo com os romaneios gerados serão apresentados aqueles foram entregues com alguma pendência. Com as informações de Período, NF, data/hora de geração, Filial, Rota, Agrupamento, data/hora do carregamento, data/hora da saída da portaria, data/hora de entrega na filial, além da transportadora vinculada a carga."
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Sucesso."),
			@ApiResponse(code = 403, message = "Não autorizado."),
			@ApiResponse(code = 404, message = "Erro de validação dos campos."),
			@ApiResponse(code = 500, message = "Erro interno do servidor.")}
			)
	@GetMapping("/relatorios/falta-volume")
	public ResponseEntity<List<RelFaltaVolumeResponseDto>> relFaltaVolume(@RequestParam(name = "dataInicio") String dataInicio, @RequestParam(name = "dataFim") String dataFim ) throws Exception {
		try {
			
			dataInicio = dataInicio.concat(" 00:00:00");
			dataFim = dataFim.concat(" 23:59:59");

			List<RelFaltaVolumeResponseDto> rel = relRomaneiosService.relFaltaVolume(
					DataUtil.converteStringToLocalDateTime(dataInicio,DataUtil.DDMMYYYY_PADRAO), 
					DataUtil.converteStringToLocalDateTime(dataFim,DataUtil.DDMMYYYY_PADRAO)
					);

			return new ResponseEntity<>(rel, HttpStatus.OK);
		} catch (Exception ex) {
			log.error("relFaltaVolume", ex);
			throw ex;
		}
	}

	@ApiOperation(
			value = "Nível entrega transportadora", 
			notes = "De acordo as transportadoras, rotas e filiais disponíveis serão calculados os percentuais de entregas realizadas no prazo, os porcentuais de entregas com o volume correto e seus indicadores."
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Sucesso."),
			@ApiResponse(code = 403, message = "Não autorizado."),
			@ApiResponse(code = 404, message = "Erro de validação dos campos."),
			@ApiResponse(code = 500, message = "Erro interno do servidor.")}
			)
	@GetMapping("/relatorios/nivel-entrega-transportadora")
	public ResponseEntity<List<RelNivelEntregaTransportadoraResponseDto>> relNivelEntregaTransportadora(@RequestParam(name = "dataInicio") String dataInicio, @RequestParam(name = "dataFim") String dataFim ) throws Exception {
		try {
			
			dataInicio = dataInicio.concat(" 00:00:00");
			dataFim = dataFim.concat(" 23:59:59");

			List<RelNivelEntregaTransportadoraResponseDto> rel = relNivelEntregaService.relNivelEntregaTransportadora(
					DataUtil.converteStringToLocalDateTime(dataInicio,DataUtil.DDMMYYYY_PADRAO), 
					DataUtil.converteStringToLocalDateTime(dataFim,DataUtil.DDMMYYYY_PADRAO));

			return new ResponseEntity<>(rel, HttpStatus.OK);
		} catch (Exception ex) {
			log.error("relNivelEntregaTransportadora", ex);
			throw ex;
		}
	}

	@ApiOperation(
			value = "Entrega prazo", 
			notes = "De acordo com as filiais disponíveis serão calculados os percentuais de entregas realizadas no prazo, os porcentuais de entregas com o volume correto e seus indicadores."
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Sucesso."),
			@ApiResponse(code = 403, message = "Não autorizado."),
			@ApiResponse(code = 404, message = "Erro de validação dos campos."),
			@ApiResponse(code = 500, message = "Erro interno do servidor.")}
			)
	@GetMapping("/relatorios/entrega-prazo")
	public ResponseEntity<List<RelNivelEntregaPrazoResponseDto>> relEntregaPrazo(@RequestParam(name = "dataInicio") String dataInicio, @RequestParam(name = "dataFim") String dataFim ) throws Exception {
		try {
			
			dataInicio = dataInicio.concat(" 00:00:00");
			dataFim = dataFim.concat(" 23:59:59");

			List<RelNivelEntregaPrazoResponseDto> rel = relNivelEntregaPrazoService.relNivelEntregaPrazo(
					DataUtil.converteStringToLocalDateTime(dataInicio,DataUtil.DDMMYYYY_PADRAO), 
					DataUtil.converteStringToLocalDateTime(dataFim,DataUtil.DDMMYYYY_PADRAO));

			return new ResponseEntity<>(rel, HttpStatus.OK);
		} catch (Exception ex) {
			log.error("relEntregaPrazo", ex);
			throw ex;
		}
	}
}
