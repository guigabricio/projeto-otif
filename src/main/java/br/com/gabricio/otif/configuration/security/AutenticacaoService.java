package br.com.gabricio.otif.configuration.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.gabricio.otif.model.UsuariosModel;
import br.com.gabricio.otif.repositories.UsuariosRepository;
import br.com.gabricio.otif.utils.CpfCnpjUtil;

@Service
public class AutenticacaoService implements UserDetailsService {

	@Autowired
	UsuariosRepository UsuarioRepository;
	
	@Override
	public UserDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {
		Optional<UsuariosModel> usuario = UsuarioRepository.findByCpf(CpfCnpjUtil.removeFormatacao(cpf));
		if(usuario.isPresent()) {
			return usuario.get();
		}
		throw new UsernameNotFoundException("Usuário não encontrado.");
	}

}
