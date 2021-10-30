package br.com.gabricio.otif.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.gabricio.otif.dto.HistoricoLeituraReponseDto;
import br.com.gabricio.otif.exception.NenhumItemEncontradoException;
import br.com.gabricio.otif.services.HistoricoLeituraService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@RestController
@Api(value = "Historico de Leitura", tags = "Historico de Leitura", description = "Histórico de Leitura dos usuários.")
@Slf4j
@Validated
public class HistoricoLeituraController {

	@Autowired
	HistoricoLeituraService historicoLeituraService;

	@ApiOperation(value = "Listar todos as leituras do usuário", notes = "Listar todos as leituras do usuário", response = HistoricoLeituraReponseDto.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Sucesso.", response = HistoricoLeituraReponseDto.class),
			@ApiResponse(code = 403, message = "Não autorizado."),
			@ApiResponse(code = 404, message = "Usuário não encontrado."),
			@ApiResponse(code = 500, message = "Erro interno do servidor.") 
	})
	@GetMapping("/historico/leituras")
	public ResponseEntity<List<HistoricoLeituraReponseDto>> listarTodosOsHistoricos() throws NenhumItemEncontradoException {
		try {
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String cpfUsarioLogado = authentication.getName();
			
			List<HistoricoLeituraReponseDto> listarTodasAsLeituras = historicoLeituraService.listarTodasAsLeituras(cpfUsarioLogado);
			
			return new ResponseEntity<>(listarTodasAsLeituras, HttpStatus.OK);
		} catch (Exception ex) {
			log.error("listarTodosOsHistoricos", ex);
			throw ex;
		}
	}
}
