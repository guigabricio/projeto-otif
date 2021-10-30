package br.com.gabricio.otif.dto;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import br.com.gabricio.otif.model.UsuariosModel;

@Component
public class ObjectWrapper {

	public UsuariosDto getUsuariosDto(UsuariosModel usuarioModel) {
		return new UsuariosDto(
				usuarioModel.getNome(), 
				usuarioModel.getCpf(),
				usuarioModel.getCnpj(), 
				usuarioModel.getPerfis().get(0), 
				usuarioModel.getSenha());
	}
	
	public UsuariosAlteracaoResponseDto getUsuariosAlteracaoResponseDto(UsuariosModel usuarioModel) {
		return new UsuariosAlteracaoResponseDto(
				usuarioModel.getNome(), 
				usuarioModel.getCpf(),
				usuarioModel.getCnpj(), 
				usuarioModel.getPerfis().get(0),
				usuarioModel.getStatusUsuario());
	}
	
	
	public UsuariosResponseDto getUsuariosResponseDto(UsuariosModel usuarioModel) {
		return new UsuariosResponseDto(
				usuarioModel.getId(), 
				usuarioModel.getNome(), 
				usuarioModel.getCpf(),
				usuarioModel.getCnpj(), 
				usuarioModel.getPerfis().get(0), 
				usuarioModel.getDataHoraCriacao(),
				usuarioModel.getStatusUsuario(),
				usuarioModel.getDatahoraAtualizacao(),
				usuarioModel.getCpfUsuarioAlteracao());
	}

	public UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(LoginDto login) {
		return new UsernamePasswordAuthenticationToken(login.getCpf(), login.getSenha());
	}
}
