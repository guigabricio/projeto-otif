package br.com.gabricio.otif.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.gabricio.otif.dto.ConfigPrevisaoEntregaDto;
import br.com.gabricio.otif.dto.ConfigPrevisaoEntregaResponseDto;
import br.com.gabricio.otif.exception.NenhumItemEncontradoException;
import br.com.gabricio.otif.services.ConfigPrevisaoEntregaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@RestController
@Api(value = "Config. Previsão Entrega", tags = "Config. Previsão Entrega", description = "Config. Previsão Entrega")
@Slf4j
@Validated
public class ConfigPrevisaoEntregaController {

	@Autowired
	ConfigPrevisaoEntregaService service;

	@ApiOperation(value = "Listar todos as configurações", notes = "Listar todos as configurações", response = ConfigPrevisaoEntregaResponseDto.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Sucesso.", response = ConfigPrevisaoEntregaResponseDto.class),
			@ApiResponse(code = 403, message = "Não autorizado."),
			@ApiResponse(code = 500, message = "Erro interno do servidor.") 
	})
	@GetMapping("/config-previsao-entrega")
	public ResponseEntity<List<ConfigPrevisaoEntregaResponseDto>> listarTodosConfiguracaoPrevisaoEntregas() throws NenhumItemEncontradoException {
		try {
			List<ConfigPrevisaoEntregaResponseDto> listaConfiguracaoPrevisaoEntregas = service.listarConfiguracaoPrevisaoEntrega();
			return new ResponseEntity<>(listaConfiguracaoPrevisaoEntregas, HttpStatus.OK);
		} catch (Exception ex) {
			log.error("listarTodosConfiguracaoPrevisaoEntregas", ex);
			throw ex;
		}
	}

	@ApiOperation(value = "Consulta dados da config", notes = "Retorna os dados da config", response = ConfigPrevisaoEntregaResponseDto.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Sucesso.", response = ConfigPrevisaoEntregaResponseDto.class),
			@ApiResponse(code = 403, message = "Não autorizado."),
			@ApiResponse(code = 404, message = "Configuração não encontrada."),
			@ApiResponse(code = 500, message = "Erro interno do servidor.") 
	})
	@GetMapping("/config-previsao-entrega/{id}")
	public ResponseEntity<ConfigPrevisaoEntregaResponseDto> getConfiguracaoPrevisaoEntrega(@PathVariable(value = "id") String id)
			throws Exception {
		try {
			ConfigPrevisaoEntregaResponseDto configDto = service.getConfiguracaoPrevisaoEntrega(id);
			if (configDto == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				return new ResponseEntity<>(configDto, HttpStatus.OK);
			}
		} catch (Exception ex) {
			log.error("getConfiguracaoPrevisaoEntrega", ex);
			throw ex;
		}
	}

	@ApiOperation(value = "Insere um config", notes = "Retorna os dados da config cadastrada", response = ConfigPrevisaoEntregaResponseDto.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Sucesso.", response = ConfigPrevisaoEntregaResponseDto.class),
			@ApiResponse(code = 403, message = "Não autorizado."),
			@ApiResponse(code = 404, message = "Erro de validação dos campos."),
			@ApiResponse(code = 500, message = "Erro interno do servidor.") 
	})
	@PostMapping("/config-previsao-entrega")
	public ResponseEntity<ConfigPrevisaoEntregaResponseDto> salvarConfiguracaoPrevisaoEntrega(@Valid @RequestBody ConfigPrevisaoEntregaDto configDto) throws Exception {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String cpfUsarioLogado = authentication.getName();

			ConfigPrevisaoEntregaResponseDto configSalva = service.salvarConfiguracaoPrevisaoEntrega(configDto, cpfUsarioLogado);
			return new ResponseEntity<>(configSalva, HttpStatus.CREATED);
		} catch (Exception ex) {
			log.error("salvarConfiguracaoPrevisaoEntrega", ex);
			throw ex;
		}
	}

	@ApiOperation(value = "Altera uma config", notes = "Altera uma config", response = ConfigPrevisaoEntregaResponseDto.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Sucesso.", response = ConfigPrevisaoEntregaResponseDto.class),
			@ApiResponse(code = 403, message = "Não autorizado."),
			@ApiResponse(code = 404, message = "Erro de validação do campos."),
			@ApiResponse(code = 500, message = "Erro interno do servidor.") 
	})
	@PutMapping("/config-previsao-entrega/{id}")
	public ResponseEntity<ConfigPrevisaoEntregaResponseDto> alterarConfiguracaoPrevisaoEntrega(@PathVariable(value = "id") String id, 
			@Valid @RequestBody ConfigPrevisaoEntregaDto configDto) throws Exception {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String cpfUsarioLogado = authentication.getName();

			ConfigPrevisaoEntregaResponseDto configAlterada = service.alterarConfiguracaoPrevisaoEntrega(id, configDto, cpfUsarioLogado);
			if (configAlterada == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				return new ResponseEntity<>(configAlterada, HttpStatus.OK);
			}
		} catch (Exception ex) {
			log.error("alterarConfiguracaoPrevisaoEntrega", ex);
			throw ex;
		}
	}

	@ApiOperation(value = "Exclui um configuração")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Sucesso."),
			@ApiResponse(code = 403, message = "Não autorizado."),
			@ApiResponse(code = 404, message = "Erro de validação."),
			@ApiResponse(code = 500, message = "Erro interno do servidor.") })
	@DeleteMapping("/config-previsao-entrega/{id}")
	public ResponseEntity<?> excluirConfiguracaoPrevisaoEntrega(@PathVariable(value = "id") String id) throws Exception {
		try {
			service.excluirConfiguracaoPrevisaoEntrega(id);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			log.error("excluirConfiguracaoPrevisaoEntrega", ex);
			throw ex;
		}
	}

}
