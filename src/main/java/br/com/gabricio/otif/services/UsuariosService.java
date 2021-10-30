package br.com.gabricio.otif.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.gabricio.otif.dto.ObjectWrapper;
import br.com.gabricio.otif.dto.SenhaDto;
import br.com.gabricio.otif.dto.UsuariosAlteracaoDto;
import br.com.gabricio.otif.dto.UsuariosAlteracaoResponseDto;
import br.com.gabricio.otif.dto.UsuariosDto;
import br.com.gabricio.otif.dto.UsuariosResponseDto;
import br.com.gabricio.otif.exception.NenhumItemEncontradoException;
import br.com.gabricio.otif.exception.RecursoJaCadastradoException;
import br.com.gabricio.otif.exception.RecursoNaoEncontradoException;
import br.com.gabricio.otif.model.PerfilEnum;
import br.com.gabricio.otif.model.StatusUsuario;
import br.com.gabricio.otif.model.UsuariosModel;
import br.com.gabricio.otif.repositories.UsuariosRepository;
import br.com.gabricio.otif.utils.CpfCnpjUtil;

@Service
public class UsuariosService {

	@Autowired
	private UsuariosRepository usuarioRepository;

	@Autowired
	private ObjectWrapper objectWrapper; 

	public List<UsuariosResponseDto> listarUsuarios() throws NenhumItemEncontradoException {
		List<UsuariosModel> listarTodos = usuarioRepository.findAll();

		List<UsuariosResponseDto> listaUsuariosDto = new ArrayList<>();

		for (UsuariosModel usuarioModel : listarTodos) {

			listaUsuariosDto.add(objectWrapper.getUsuariosResponseDto(usuarioModel));
		}
		return listaUsuariosDto;
	}

	public UsuariosResponseDto getUsuario(String cpf) throws RecursoNaoEncontradoException {
		Optional<UsuariosModel> usuario = usuarioRepository.findByCpf(CpfCnpjUtil.removeFormatacao(cpf));
		if(usuario.isPresent()){
			return objectWrapper.getUsuariosResponseDto(usuario.get());
		}
		throw new RecursoNaoEncontradoException("Usuário não encontrado.");
	}
	
	public UsuariosDto salvarUsuario(UsuariosDto usuarioDto) throws RecursoJaCadastradoException {
		boolean usuarioExistente = usuarioRepository.existsByCpf(CpfCnpjUtil.removeFormatacao(usuarioDto.getCpf()));
		if(usuarioExistente){
			throw new RecursoJaCadastradoException("Cpf já cadastrado para outro usuário.");
		}

		StatusUsuario statusUsuario = StatusUsuario.ATIVO;
		if(usuarioDto.getPerfil().equals(PerfilEnum.MOTORISTA)) {
			statusUsuario = StatusUsuario.INATIVO;
		} 

		UsuariosModel usuarioModel = new UsuariosModel(
				null, 
				null,
				usuarioDto.getNome(), 
				CpfCnpjUtil.removeFormatacao(usuarioDto.getCpf()), 
				CpfCnpjUtil.removeFormatacao(usuarioDto.getCnpj()), 
				new BCryptPasswordEncoder().encode(usuarioDto.getSenha()),
				Arrays.asList(usuarioDto.getPerfil()), 
				LocalDateTime.now(), 
				statusUsuario, 
				null, 
				null);

		String idSalvo = usuarioRepository.save(usuarioModel).getId();

		usuarioModel.setId(idSalvo);

		return objectWrapper.getUsuariosDto(usuarioModel);
	}

	public UsuariosAlteracaoResponseDto alterarUsuario(UsuariosAlteracaoDto usuarioDto,String cpf, String cpfUsuarioAlteracao) throws RecursoNaoEncontradoException {
		Optional<UsuariosModel> usuarioSalvo = usuarioRepository.findByCpf(CpfCnpjUtil.removeFormatacao(cpf));
		if(!usuarioSalvo.isPresent()){
			throw new RecursoNaoEncontradoException("Usuário não encontrado.");
		}
		UsuariosModel usuarioAlterado = usuarioSalvo.get();
		
		
		if(usuarioDto.getNome() != null && !"".equals(usuarioDto.getNome())) {
			usuarioAlterado.setNome(usuarioDto.getNome());
		}
		
		if(usuarioDto.getCnpj() != null && !"".equals(usuarioDto.getCnpj())) {
			if(!CpfCnpjUtil.isCPForCPNJ(usuarioDto.getCnpj())) {
				throw new RuntimeException("CPF/CNPJ inválido");
			}
			usuarioAlterado.setCnpj(CpfCnpjUtil.removeFormatacao(usuarioDto.getCnpj()));		
		}
		
		if(usuarioDto.getPerfil() != null && !"".equals(usuarioDto.getPerfil())) {
			PerfilEnum perfil = PerfilEnum.valueOf(usuarioDto.getPerfil());
			usuarioAlterado.setPerfis(Arrays.asList(perfil));
		}
		
		if(usuarioDto.getStatusUsuario() != null && !"".equals(usuarioDto.getStatusUsuario())) {
			StatusUsuario status = StatusUsuario.valueOf(usuarioDto.getStatusUsuario());
			usuarioAlterado.setStatusUsuario(status);
		}
		
		if(usuarioDto.getSenha() != null && !"".equals(usuarioDto.getSenha())) {
			usuarioAlterado.setSenha(new BCryptPasswordEncoder().encode(usuarioDto.getSenha()));
		}

		usuarioAlterado.setDatahoraAtualizacao(LocalDateTime.now());
		usuarioAlterado.setCpfUsuarioAlteracao(CpfCnpjUtil.removeFormatacao(cpfUsuarioAlteracao));

		usuarioRepository.save(usuarioAlterado);

		return objectWrapper.getUsuariosAlteracaoResponseDto(usuarioAlterado);
	}

	public void excluirUsuario(String cpf) throws RecursoNaoEncontradoException {
		Optional<UsuariosModel> usuario = usuarioRepository.findByCpf(CpfCnpjUtil.removeFormatacao(cpf));
		if(!usuario.isPresent()){
			throw new RecursoNaoEncontradoException("Usuário não encontrado.");
		}
		usuarioRepository.deleteByCpf(cpf);
	}

	public void ativarUsuario(String cpfUsuario, String cpfUsuarioAdmin) throws RecursoNaoEncontradoException {
		Optional<UsuariosModel> usuarioAdmin = usuarioRepository.findByCpf(CpfCnpjUtil.removeFormatacao(cpfUsuarioAdmin));
		if(!usuarioAdmin.isPresent()){
			throw new RecursoNaoEncontradoException("Usuário não encontrado.");
		}
		UsuariosModel userAdmin = usuarioAdmin.get();

		if(!userAdmin.getPerfis().get(0).equals(PerfilEnum.ADMINISTRADOR)) {
			throw new RecursoNaoEncontradoException("Usuário não tem permissão para ativar a usuário.");
		}

		Optional<UsuariosModel> usuarioDesativado = usuarioRepository.findByCpf(CpfCnpjUtil.removeFormatacao(cpfUsuario));
		if(!usuarioDesativado.isPresent()){
			throw new RecursoNaoEncontradoException("Usuário não encontrado.");
		}


		if(usuarioDesativado.get().getStatusUsuario().equals(StatusUsuario.ATIVO)) {
			throw new RecursoNaoEncontradoException("Usuário já se encontra ativado.");
		}

		UsuariosModel usuarioModel = usuarioDesativado.get();
		usuarioModel.setStatusUsuario(StatusUsuario.ATIVO);
		usuarioModel.setDatahoraAtualizacao(LocalDateTime.now());
		usuarioModel.setCpfUsuarioAlteracao(CpfCnpjUtil.removeFormatacao(cpfUsuarioAdmin));

		usuarioRepository.save(usuarioModel);
	}

	public void desativarUsuario(String cpfUsuario, String cpfUsuarioAdmin) throws RecursoNaoEncontradoException {
		Optional<UsuariosModel> usuarioAdmin = usuarioRepository.findByCpf(CpfCnpjUtil.removeFormatacao(cpfUsuarioAdmin));
		if(!usuarioAdmin.isPresent()){
			throw new RecursoNaoEncontradoException("Usuário não encontrado.");
		}
		UsuariosModel userAdmin = usuarioAdmin.get();

		if(!userAdmin.getPerfis().get(0).equals(PerfilEnum.ADMINISTRADOR)) {
			throw new RecursoNaoEncontradoException("Usuário não tem permissão para desativar a usuário.");
		}

		Optional<UsuariosModel> usuarioDesativado = usuarioRepository.findByCpf(CpfCnpjUtil.removeFormatacao(cpfUsuario));
		if(!usuarioDesativado.isPresent()){
			throw new RecursoNaoEncontradoException("Usuário não encontrado.");
		}

		if(usuarioDesativado.get().getStatusUsuario().equals(StatusUsuario.INATIVO)) {
			throw new RecursoNaoEncontradoException("Usuário já se encontra desativado.");
		}

		UsuariosModel usuarioModel = usuarioDesativado.get();
		usuarioModel.setStatusUsuario(StatusUsuario.INATIVO);
		usuarioModel.setDatahoraAtualizacao(LocalDateTime.now());
		usuarioModel.setCpfUsuarioAlteracao(CpfCnpjUtil.removeFormatacao(cpfUsuarioAdmin));

		usuarioRepository.save(usuarioModel);
	}

	public List<UsuariosResponseDto> listarUsuariosPorPerfilEStatus(PerfilEnum perfil) throws NenhumItemEncontradoException {
		List<UsuariosModel> listarTodos = usuarioRepository.findByPerfisAndStatusUsuario(perfil, StatusUsuario.INATIVO);

		List<UsuariosResponseDto> listaUsuariosDto = new ArrayList<>();

		for (UsuariosModel usuarioModel : listarTodos) {

			listaUsuariosDto.add(objectWrapper.getUsuariosResponseDto(usuarioModel));
		}
		return listaUsuariosDto;
	}

	public void alterarSenha(SenhaDto senhaDto, String cpfUsuarioLogado) throws RecursoNaoEncontradoException {
		Optional<UsuariosModel> usuarioSalvo = usuarioRepository.findByCpf(CpfCnpjUtil.removeFormatacao(cpfUsuarioLogado));
		if(!usuarioSalvo.isPresent()){
			throw new RecursoNaoEncontradoException("Usuário não encontrado.");
		}
		UsuariosModel usuarioAlterado = usuarioSalvo.get();
		
		usuarioAlterado.setSenha(new BCryptPasswordEncoder().encode(senhaDto.getSenha()));

		usuarioAlterado.setDatahoraAtualizacao(LocalDateTime.now());
		usuarioAlterado.setCpfUsuarioAlteracao(CpfCnpjUtil.removeFormatacao(cpfUsuarioLogado));

		usuarioRepository.save(usuarioAlterado);
	}
	
	public void solicitaRecuperacaoSenhaUsuario(String cpfUsuario) throws RecursoNaoEncontradoException {
		Optional<UsuariosModel> usuario = usuarioRepository.findByCpf(CpfCnpjUtil.removeFormatacao(cpfUsuario));
		if(!usuario.isPresent()){
			throw new RecursoNaoEncontradoException("Usuário não encontrado.");
		}
		UsuariosModel user = usuario.get();

		if(!user.getStatusUsuario().equals(StatusUsuario.ATIVO)) {
			throw new RecursoNaoEncontradoException("Usuário não se encontra ATIVO.");
		}

		user.setStatusUsuario(StatusUsuario.SOLICITA_RECUPERACAO_SENHA);
		user.setDatahoraAtualizacao(LocalDateTime.now());
		user.setCpfUsuarioAlteracao(CpfCnpjUtil.removeFormatacao(cpfUsuario));

		usuarioRepository.save(user);
	}

	public void autorizaSolicitaRecuperacaoSenhaUsuario(String cpf, String cpfUsuarioLogado) throws RecursoNaoEncontradoException {
		Optional<UsuariosModel> usuarioAdmin = usuarioRepository.findByCpf(CpfCnpjUtil.removeFormatacao(cpfUsuarioLogado));
		if(!usuarioAdmin.isPresent()){
			throw new RecursoNaoEncontradoException("Usuário logado não encontrado.");
		}
		UsuariosModel userAdmin = usuarioAdmin.get();

		if(!userAdmin.getPerfis().get(0).equals(PerfilEnum.ADMINISTRADOR)) {
			throw new RecursoNaoEncontradoException("Usuário não tem permissão para autorizar solicitação do usuário.");
		}
		
		Optional<UsuariosModel> usuario = usuarioRepository.findByCpf(CpfCnpjUtil.removeFormatacao(cpf));
		if(!usuario.isPresent()){
			throw new RecursoNaoEncontradoException("Usuário não encontrado.");
		}
		UsuariosModel user = usuario.get();

		if(!user.getStatusUsuario().equals(StatusUsuario.SOLICITA_RECUPERACAO_SENHA)) {
			throw new RecursoNaoEncontradoException("Status do usuário é diferente de recuperar senha.");
		}

		user.setStatusUsuario(StatusUsuario.AUTORIZA_RECUPERACAO_SENHA);
		user.setDatahoraAtualizacao(LocalDateTime.now());
		user.setCpfUsuarioAlteracao(CpfCnpjUtil.removeFormatacao(cpf));

		usuarioRepository.save(user);
	}
	
	public void recuperarSenha(SenhaDto senhaDto, String cpf) throws RecursoNaoEncontradoException {
		Optional<UsuariosModel> usuarioSalvo = usuarioRepository.findByCpf(CpfCnpjUtil.removeFormatacao(cpf));
		if(!usuarioSalvo.isPresent()){
			throw new RecursoNaoEncontradoException("Usuário logado não encontrado.");
		}
		UsuariosModel usuarioAlterado = usuarioSalvo.get();
		
		if(!usuarioAlterado.getStatusUsuario().equals(StatusUsuario.AUTORIZA_RECUPERACAO_SENHA)) {
			throw new RecursoNaoEncontradoException("Usuário não está autorizado a recuperar a senha.");
		}
		
		usuarioAlterado.setSenha(new BCryptPasswordEncoder().encode(senhaDto.getSenha()));
		usuarioAlterado.setStatusUsuario(StatusUsuario.ATIVO);

		usuarioAlterado.setDatahoraAtualizacao(LocalDateTime.now());
		usuarioAlterado.setCpfUsuarioAlteracao(CpfCnpjUtil.removeFormatacao(cpf));

		usuarioRepository.save(usuarioAlterado);
	}

	public List<UsuariosResponseDto> listarUsuariosPorStatus(StatusUsuario statusUsuario) {
		List<UsuariosModel> listarTodos = usuarioRepository.findByStatusUsuario(statusUsuario);

		List<UsuariosResponseDto> listaUsuariosDto = new ArrayList<>();

		for (UsuariosModel usuarioModel : listarTodos) {

			listaUsuariosDto.add(objectWrapper.getUsuariosResponseDto(usuarioModel));
		}
		return listaUsuariosDto;
	}
}
