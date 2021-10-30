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

import br.com.gabricio.otif.dto.ListaCargaUsuariosDto;
import br.com.gabricio.otif.exception.NenhumItemEncontradoException;
import br.com.gabricio.otif.services.CargaUsuariosService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@RestController
@Api(value = "Carga de integração usuário", tags = "Carga de usuários", description = "Carga de usuários")
@Slf4j
@Validated
public class UsuariosCargaController {

	@Autowired
	CargaUsuariosService cargaUsuarioService;
	

	@ApiOperation(value = "Carga de usuários", notes = "Realiza a inclusão e alteração dos usuários através do CPF.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Sucesso."),
			@ApiResponse(code = 403, message = "Não autorizado."),
			@ApiResponse(code = 404, message = "Erro de validação dos campos."),
			@ApiResponse(code = 500, message = "Erro interno do servidor.")}
			)
	@PostMapping("/usuarios/insere-altera-carga")
	public ResponseEntity<?> insereCargaUsuario(@Valid @RequestBody ListaCargaUsuariosDto listaUsuarios) throws Exception {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String cpfUsarioLogado = authentication.getName();
			
			if(listaUsuarios.getData() == null || listaUsuarios.getData().isEmpty()) {
				throw new NenhumItemEncontradoException("Lista de usuários não pode ser vazia.");
			}
			
			cargaUsuarioService.insereCargaDeUsuarios(listaUsuarios, cpfUsarioLogado);
			
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (Exception ex) {
			log.error("salvarUsuario", ex);
			throw ex;
		}
	}

}
