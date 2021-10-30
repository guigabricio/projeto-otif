package br.com.gabricio.otif.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.gabricio.otif.dto.ListaCargaUsuariosDto;
import br.com.gabricio.otif.dto.UsuariosCargaDto;
import br.com.gabricio.otif.exception.NenhumItemEncontradoException;
import br.com.gabricio.otif.model.PerfilEnum;
import br.com.gabricio.otif.model.StatusUsuario;
import br.com.gabricio.otif.model.UsuariosModel;
import br.com.gabricio.otif.repositories.UsuariosRepository;
import br.com.gabricio.otif.utils.CpfCnpjUtil;
import br.com.gabricio.otif.utils.DataUtil;

@Service
public class CargaUsuariosService {

	@Autowired
	private UsuariosRepository usuarioRepository;

	public void insereCargaDeUsuarios(ListaCargaUsuariosDto listaUsuarios, String cpfUsuarioLogado) throws NenhumItemEncontradoException {
		List<UsuariosCargaDto> data = listaUsuarios.getData();

		if(data == null || data.isEmpty()) {
			throw new NenhumItemEncontradoException("Lista de romaneios vazia");
		}

		for (UsuariosCargaDto usuarioCarga : data) {
			Optional<UsuariosModel> existeUsuario = usuarioRepository.findByCpf(CpfCnpjUtil.removeFormatacao(usuarioCarga.getCpfUsuario()));

			UsuariosModel usuarioModel =  null; 

			if(existeUsuario.isPresent()) {
				usuarioModel = existeUsuario.get();
				usuarioModel.setIdUsuarioExterno(String.valueOf(usuarioCarga.getIdUsuario()));
				usuarioModel.setNome(usuarioCarga.getNomeUsuario());
				usuarioModel.setCpf(CpfCnpjUtil.removeFormatacao(usuarioCarga.getCpfUsuario()));
				usuarioModel.setCnpj("");;
				usuarioModel.setSenha(new BCryptPasswordEncoder().encode(usuarioCarga.getSenhaUsuario()));;
				usuarioModel.setPerfis(Arrays.asList(PerfilEnum.ADMINISTRADOR));
				usuarioModel.setDataHoraCriacao(DataUtil.converteStringToLocalDateTime(usuarioCarga.getDataHoraCriacao(),DataUtil.DDMMYYY_HHMMSS));    
				usuarioModel.setStatusUsuario(StatusUsuario.valueOf((usuarioCarga.getStatus())));
				usuarioModel.setDatahoraAtualizacao(DataUtil.converteStringToLocalDateTime(usuarioCarga.getDataHoraAtualizacao(),DataUtil.DDMMYYY_HHMMSS));
				usuarioModel.setCpfUsuarioAlteracao(cpfUsuarioLogado);

			} else {//insere
				usuarioModel = new UsuariosModel(
						null, 
						String.valueOf(usuarioCarga.getIdUsuario()),
						usuarioCarga.getNomeUsuario(), 
						CpfCnpjUtil.removeFormatacao(usuarioCarga.getCpfUsuario()), 
						"", 
						new BCryptPasswordEncoder().encode(usuarioCarga.getSenhaUsuario()),
						Arrays.asList(PerfilEnum.ADMINISTRADOR), 
						DataUtil.converteStringToLocalDateTime(usuarioCarga.getDataHoraCriacao(),DataUtil.DDMMYYY_HHMMSS), 
						StatusUsuario.valueOf((usuarioCarga.getStatus())), 
						DataUtil.converteStringToLocalDateTime(usuarioCarga.getDataHoraAtualizacao(),DataUtil.DDMMYYY_HHMMSS), 
						cpfUsuarioLogado);
			}
			usuarioRepository.save(usuarioModel);
		}
	}
}
