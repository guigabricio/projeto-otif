package br.com.gabricio.otif.configuration.security;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.gabricio.otif.model.PerfilEnum;
import br.com.gabricio.otif.model.StatusUsuario;
import br.com.gabricio.otif.model.UsuariosModel;
import br.com.gabricio.otif.repositories.ConfigPrevisaoEntregaRepository;
import br.com.gabricio.otif.repositories.RomaneiosRepository;
import br.com.gabricio.otif.repositories.UsuariosRepository;

@Component
public class InicializacaoSistema implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	UsuariosRepository usuarioRepository;

	@Autowired
	ConfigPrevisaoEntregaRepository configuracaoPrevisaoEntregaRepository;

	@Autowired
	MongoTemplate mongoTemplate;
	
	
	@Autowired
	RomaneiosRepository repository;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		
		boolean usuarioExistente = usuarioRepository.existsByCpf("admin");
		if(!usuarioExistente){
			UsuariosModel usuarioModel = new UsuariosModel(
					null, 
					null,
					"Usuário admin", 
					"admin", 
					null, 
					new BCryptPasswordEncoder().encode("admin"),
					Arrays.asList(PerfilEnum.ADMINISTRADOR), 
					LocalDateTime.now(), 
					StatusUsuario.ATIVO, 
					null, 
					null);

			usuarioRepository.save(usuarioModel);

		}

		usuarioExistente = usuarioRepository.existsByCpf("root");
		if(!usuarioExistente){
			UsuariosModel usuarioModel = new UsuariosModel(
					null, 
					null,
					"Usuário Root", 
					"root", 
					null, 
					new BCryptPasswordEncoder().encode("root"),
					Arrays.asList(PerfilEnum.ADMINISTRADOR), 
					LocalDateTime.now(), 
					StatusUsuario.ATIVO, 
					null, 
					null);

			usuarioRepository.save(usuarioModel);
		}


		//		long count = configuracaoPrevisaoEntregaRepository.count();
		//		if(count == 0) {
		//			ConfiguracaoPrevisaoEntregaResponseDto config = new ConfiguracaoPrevisaoEntregaResponseDto(
		//					null, 
		//					"00:00", 
		//					"03:00", 
		//					0, 
		//					LocalDateTime.now(), 
		//					"admin", 
		//					null, 
		//					null);
		//			
		//			configuracaoPrevisaoEntregaRepository.save(config);
		//			
		//			ConfiguracaoPrevisaoEntregaResponseDto config2 = new ConfiguracaoPrevisaoEntregaResponseDto(
		//					null, 
		//					"03:00", 
		//					"23:59", 
		//					1, 
		//					LocalDateTime.now(), 
		//					"admin", 
		//					null, 
		//					null);
		//			
		//			configuracaoPrevisaoEntregaRepository.save(config2);
		//		}
		//		
	}
}

