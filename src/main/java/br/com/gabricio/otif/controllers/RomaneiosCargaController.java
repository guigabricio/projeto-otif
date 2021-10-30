package br.com.gabricio.otif.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.gabricio.otif.dto.RomaneiosListaCargaDto;
import br.com.gabricio.otif.exception.NenhumItemEncontradoException;
import br.com.gabricio.otif.services.RomaneiosService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@RestController
@Api(value = "Carga de integração de romaneios", tags = "Carga de romaneios", description = "Realiza a inclusão e alteração da carga de romaneios.")
@Validated
@Slf4j
public class RomaneiosCargaController {

	@Autowired
	RomaneiosService romaneioService;


	@ApiOperation(value = "Carga de romaneios", notes = "Realiza a inclusão e alteração da carga de romaneios, utilizandos os campos como chave (AGRUPAMENTOROMANEIO_ID,ROMANEIO_NR,NOTA_NR,VOLUME_NR)")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Sucesso."),
			@ApiResponse(code = 403, message = "Não autorizado."),
			@ApiResponse(code = 404, message = "Erro de validação dos campos."),
			@ApiResponse(code = 500, message = "Erro interno do servidor.")}
			)
	@PostMapping("/romaneios/insere-altera-carga")
	public ResponseEntity<?> insereAlterarCargaRomaneios(@Valid @RequestBody RomaneiosListaCargaDto listaCargaRomaneiosDto) throws Exception {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String cpfUsarioLogado = authentication.getName();
			
			if(listaCargaRomaneiosDto.getData() == null || listaCargaRomaneiosDto.getData().isEmpty()) {
				throw new NenhumItemEncontradoException("Lista de romaneios não pode ser vazia."); 
			}

			romaneioService.insereAlteraCargaDeRomaneios(listaCargaRomaneiosDto, cpfUsarioLogado);

			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (Exception ex) {
			log.error("insereAlterarCargaRomaneios", ex);
			throw ex;
		}
	}
}
