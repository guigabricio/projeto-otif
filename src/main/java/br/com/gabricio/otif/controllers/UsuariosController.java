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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.gabricio.otif.dto.SenhaDto;
import br.com.gabricio.otif.dto.UsuariosAlteracaoDto;
import br.com.gabricio.otif.dto.UsuariosAlteracaoResponseDto;
import br.com.gabricio.otif.dto.UsuariosDto;
import br.com.gabricio.otif.dto.UsuariosResponseDto;
import br.com.gabricio.otif.exception.NenhumItemEncontradoException;
import br.com.gabricio.otif.model.StatusUsuario;
import br.com.gabricio.otif.services.UsuariosService;
import br.com.gabricio.otif.utils.CpfCnpjUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@RestController
@Api(value = "Usuários", tags = "Usuários", description = "Cadastro de usuários")
@Slf4j
@Validated
public class UsuariosController {

	@Autowired
	UsuariosService usuarioService;

	@ApiOperation(value = "Listar todos os usuários", notes = "Retorna todos os usuários", response = UsuariosResponseDto.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Sucesso.", response = UsuariosResponseDto.class),
			@ApiResponse(code = 403, message = "Não autorizado."),
			@ApiResponse(code = 500, message = "Erro interno do servidor.") 
	})
	@GetMapping("/usuarios/all")
	public ResponseEntity<List<UsuariosResponseDto>> listarTodosUsuarios() throws NenhumItemEncontradoException {
		try {
			List<UsuariosResponseDto> listaUsuarios = usuarioService.listarUsuarios();
			return new ResponseEntity<>(listaUsuarios, HttpStatus.OK);
		} catch (Exception ex) {
			log.error("listarTodosUsuarios", ex);
			throw ex;
		}
	}

	@ApiOperation(value = "Consulta dados do usuário através do Cpf", notes = "Retorna os dados do usuário", response = UsuariosResponseDto.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Sucesso.", response = UsuariosResponseDto.class),
			@ApiResponse(code = 403, message = "Não autorizado."),
			@ApiResponse(code = 404, message = "Usuário não encontrado."),
			@ApiResponse(code = 500, message = "Erro interno do servidor.") 
	})
	@GetMapping("/usuarios/{cpf}")
	public ResponseEntity<UsuariosResponseDto> getUsuario(@PathVariable(value = "cpf") String cpf)
			throws Exception {
		try {
			UsuariosResponseDto usuarioConsultaDto = usuarioService.getUsuario(CpfCnpjUtil.removeFormatacao(cpf));
			if (usuarioConsultaDto == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				return new ResponseEntity<>(usuarioConsultaDto, HttpStatus.OK);
			}
		} catch (Exception ex) {
			log.error("getUsuario", ex);
			throw ex;
		}
	}

	@ApiOperation(value = "Insere um usuário", notes = "Retorna os dados do usuário cadastrado", response = UsuariosDto.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Sucesso.", response = UsuariosDto.class),
			@ApiResponse(code = 403, message = "Não autorizado."),
			@ApiResponse(code = 404, message = "Erro de validação dos campos."),
			@ApiResponse(code = 500, message = "Erro interno do servidor.") 
	})
	@PostMapping("/usuarios")
	public ResponseEntity<UsuariosDto> salvarUsuario(@Valid @RequestBody UsuariosDto usuarioDto) throws Exception {
		try {
			UsuariosDto usuarioSalvo = usuarioService.salvarUsuario(usuarioDto);
			return new ResponseEntity<>(usuarioSalvo, HttpStatus.CREATED);
		} catch (Exception ex) {
			log.error("salvarUsuario", ex);
			throw ex;
		}
	}

	@ApiOperation(value = "Altera um usuário", notes = "Retorna os dados do usuário alterado", response = UsuariosAlteracaoResponseDto.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Sucesso.", response = UsuariosAlteracaoResponseDto.class),
			@ApiResponse(code = 403, message = "Não autorizado."),
			@ApiResponse(code = 404, message = "Usuário não encontrado ou erro de validação do campos."),
			@ApiResponse(code = 500, message = "Erro interno do servidor.") 
	})
	@PutMapping("/usuarios/{cpf}")
	public ResponseEntity<UsuariosAlteracaoResponseDto> alterarUsuario(@PathVariable(value = "cpf") String cpf, @Valid @RequestBody UsuariosAlteracaoDto alterarUsuarioDto) throws Exception {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String cpfUsarioLogado = authentication.getName();
			
			UsuariosAlteracaoResponseDto usuarioAlterado = usuarioService.alterarUsuario(alterarUsuarioDto, cpf, cpfUsarioLogado);
			if (usuarioAlterado == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				return new ResponseEntity<>(usuarioAlterado, HttpStatus.OK);
			}
		} catch (Exception ex) {
			log.error("alterarUsuario", ex);
			throw ex;
		}
	}

	@ApiOperation(value = "Exclui um usuário")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Sucesso."),
			@ApiResponse(code = 403, message = "Não autorizado."),
			@ApiResponse(code = 404, message = "Usuário não encontrado ou erro de validação do cpf."),
			@ApiResponse(code = 500, message = "Erro interno do servidor.") })
	@DeleteMapping("/usuarios/{cpf}")
	public ResponseEntity<?> excluirUsuario(@PathVariable(value = "cpf") String cpf) throws Exception {
		try {
			usuarioService.excluirUsuario(cpf);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			log.error("excluirUsuario", ex);
			throw ex;
		}
	}

	@ApiOperation(value = "Ativar usuário", notes = "Ativar usuário")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Sucesso."),
			@ApiResponse(code = 403, message = "Não autorizado."),
			@ApiResponse(code = 404, message = "Erro de validação dos campos."),
			@ApiResponse(code = 500, message = "Erro interno do servidor.") })
	@PutMapping("/usuarios/ativar-usuario/{cpf}")
	public ResponseEntity<?> ativarUsuario(@PathVariable(value = "cpf") String cpf) throws Exception {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String cpfUsuarioLogado = authentication.getName();

			usuarioService.ativarUsuario(cpf, cpfUsuarioLogado);

			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			log.error("ativarUsuario", ex);
			throw ex;
		}
	}

	@ApiOperation(value = "Desativar usuário", notes = "Desativar usuário")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Sucesso."),
			@ApiResponse(code = 403, message = "Não autorizado."),
			@ApiResponse(code = 404, message = "Erro de validação dos campos."),
			@ApiResponse(code = 500, message = "Erro interno do servidor.") })
	@PutMapping("/usuarios/desativar-usuario/{cpf}")
	public ResponseEntity<?> desativarUsuario(@PathVariable(value = "cpf") String cpf) throws Exception {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String cpfUsuarioLogado = authentication.getName();

			usuarioService.desativarUsuario(cpf, cpfUsuarioLogado);

			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			log.error("desativarUsuario", ex);
			throw ex;
		}
	}

	@ApiOperation(value = "Alterar senha do usuário", notes = "Altera a senha do usuário logado, presente na autenticação JWT.")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Sucesso."),
			@ApiResponse(code = 403, message = "Não autorizado."),
			@ApiResponse(code = 404, message = "Erro de validação dos campos."),
			@ApiResponse(code = 500, message = "Erro interno do servidor.") 
	})
	@PutMapping("/usuarios/alterar-senha")
	public ResponseEntity<?> alterarSenha(@Valid @RequestBody SenhaDto senhaDto) throws Exception {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String cpfUsuarioLogado = authentication.getName();

			usuarioService.alterarSenha(senhaDto, cpfUsuarioLogado);

			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			log.error("alterarSenha", ex);
			throw ex;
		}
	}
	
	
	@ApiOperation(value = "Solicitação de permissão para recuperar senha do usuário", notes = "Solicitação de permissão para recuperar senha do usuário.")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Sucesso."),
			@ApiResponse(code = 404, message = "Erro de validação dos campos."),
			@ApiResponse(code = 500, message = "Erro interno do servidor.") 
	})
	@PutMapping("/usuarios/solicitacao-recuperacao-senha/{cpf}")
	public ResponseEntity<?> solicitaRecuperacaoSenha(@PathVariable(value = "cpf") String cpf) throws Exception {
		try {
			usuarioService.solicitaRecuperacaoSenhaUsuario(cpf);

			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			log.error("solicitaRecuperacaoSenha", ex);
			throw ex;
		}
	}
	
	
	@ApiOperation(value = "Autorização para alteração de senha", notes = "Autorização para alteração de senha.")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Sucesso."),
			@ApiResponse(code = 404, message = "Erro de validação dos campos."),
			@ApiResponse(code = 500, message = "Erro interno do servidor.") 
	})
	@PutMapping("/usuarios/autoriza-solicitacao-recuperacao-senha/{cpf}")
	public ResponseEntity<?> autorizaSolicitaRecuperacaoSenha(@PathVariable(value = "cpf") String cpf) throws Exception {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String cpfUsuarioLogado = authentication.getName();
			
			usuarioService.autorizaSolicitaRecuperacaoSenhaUsuario(cpf, cpfUsuarioLogado);

			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			log.error("autorizaSolicitaRecuperacaoSenha", ex);
			throw ex;
		}
	}
	
	@ApiOperation(value = "Recuperação de senha", notes = "Recuperação de senha.")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Sucesso."),
			@ApiResponse(code = 404, message = "Erro de validação dos campos."),
			@ApiResponse(code = 500, message = "Erro interno do servidor.") 
	})
	@PutMapping("/usuarios/recuperar-senha/{cpf}")
	public ResponseEntity<?> autorizaSolicitaRecuperacaoSenha(@Valid @RequestBody SenhaDto senhaDto , @PathVariable(value = "cpf") String cpf ) throws Exception {
		try {
			usuarioService.recuperarSenha(senhaDto, cpf);

			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			log.error("autorizaSolicitaRecuperacaoSenha", ex);
			throw ex;
		}
	}
	
	
	@ApiOperation(value = "Listar todos os usuários por Status", notes = "Retorna todos os usuários por status", response = UsuariosResponseDto.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Sucesso.", response = UsuariosResponseDto.class),
			@ApiResponse(code = 403, message = "Não autorizado."),
			@ApiResponse(code = 500, message = "Erro interno do servidor.") 
	})
	@GetMapping("/usuarios")
	public ResponseEntity<List<UsuariosResponseDto>> listarTodosUsuariosPorStatus(@RequestParam(name = "statusUsuario") StatusUsuario statusUsuario) throws NenhumItemEncontradoException {
		try {
			List<UsuariosResponseDto> listaUsuarios = usuarioService.listarUsuariosPorStatus(statusUsuario);
			return new ResponseEntity<>(listaUsuarios, HttpStatus.OK);
		} catch (Exception ex) {
			log.error("listarTodosUsuariosPorStatus", ex);
			throw ex;
		}
	}
	
	
	@ApiOperation(value = "Consulta os dados do usuário", notes = "Consulta os dados do usuário", response = UsuariosResponseDto.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Sucesso.", response = UsuariosResponseDto.class),
			@ApiResponse(code = 404, message = "Usuário não encontrado."),
			@ApiResponse(code = 500, message = "Erro interno do servidor.") 
	})
	@GetMapping("/usuarios/{cpf}/status")
	public ResponseEntity<UsuariosResponseDto> getConsultaUsuario(@PathVariable(value = "cpf") String cpf) throws Exception {
		try {
			UsuariosResponseDto usuarioConsultaDto = usuarioService.getUsuario(CpfCnpjUtil.removeFormatacao(cpf));
			if (usuarioConsultaDto == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				return new ResponseEntity<>(usuarioConsultaDto, HttpStatus.OK);
			}
		} catch (Exception ex) {
			log.error("getUsuario", ex);
			throw ex;
		}
	}
}
