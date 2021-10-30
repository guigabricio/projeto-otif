package br.com.gabricio.otif.services;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.gabricio.otif.dto.ConfigPrevisaoEntregaDto;
import br.com.gabricio.otif.dto.ConfigPrevisaoEntregaResponseDto;
import br.com.gabricio.otif.exception.NenhumItemEncontradoException;
import br.com.gabricio.otif.exception.RecursoJaCadastradoException;
import br.com.gabricio.otif.exception.RecursoNaoEncontradoException;
import br.com.gabricio.otif.model.ConfigPrevisaoEntregaModel;
import br.com.gabricio.otif.repositories.ConfigPrevisaoEntregaRepository;
import br.com.gabricio.otif.utils.DataUtil;

@Service
public class ConfigPrevisaoEntregaService {

	@Autowired
	ConfigPrevisaoEntregaRepository repository;

	public LocalDateTime calculaDataPrevisaoEntrega() {

		List<ConfigPrevisaoEntregaModel> listaIntervalos = repository.findAll();
		if(!listaIntervalos.isEmpty()) {

			for (ConfigPrevisaoEntregaModel configuracaoPrevisaoEntrega : listaIntervalos) {
					
				try {
					if(DataUtil.pertenceAoLimite(configuracaoPrevisaoEntrega.getHoraInicio(), configuracaoPrevisaoEntrega.getHoraLimite())) {
						return LocalDateTime.now().plusDays(configuracaoPrevisaoEntrega.getPrazoEntrega()).withHour(23).withMinute(59).withSecond(59);
					}
				} catch (ParseException e) {
				}

			}
		}
		return LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
	}

	public List<ConfigPrevisaoEntregaResponseDto> listarConfiguracaoPrevisaoEntrega() throws NenhumItemEncontradoException {
		List<ConfigPrevisaoEntregaModel> listarTodos = repository.findAll();

		List<ConfigPrevisaoEntregaResponseDto> listaDto = new ArrayList<>();

		for (ConfigPrevisaoEntregaModel model : listarTodos) {
			listaDto.add(new ConfigPrevisaoEntregaResponseDto(
					model.getId(), 
					model.getHoraInicio(), 
					model.getHoraLimite(), 
					model.getPrazoEntrega(), 
					model.getDatahoraInclusao(), 
					model.getUsuarioInclusao(), 
					model.getDatahoraAtualizacao(), 
					model.getUsuarioAtualizacao()
					));
		}
		
		return listaDto;
	}

	public ConfigPrevisaoEntregaResponseDto getConfiguracaoPrevisaoEntrega(String id) throws RecursoNaoEncontradoException {
		Optional<ConfigPrevisaoEntregaModel> config = repository.findById(id);
		if(!config.isPresent()){
			throw new RecursoNaoEncontradoException("Não foi encontrado registro para o id informado.");
		}
		
		ConfigPrevisaoEntregaModel model = config.get();
		
		return new ConfigPrevisaoEntregaResponseDto(
				model.getId(), 
				model.getHoraInicio(), 
				model.getHoraLimite(), 
				model.getPrazoEntrega(), 
				model.getDatahoraInclusao(), 
				model.getUsuarioInclusao(), 
				model.getDatahoraAtualizacao(), 
				model.getUsuarioAtualizacao());
	}

	public ConfigPrevisaoEntregaResponseDto salvarConfiguracaoPrevisaoEntrega(ConfigPrevisaoEntregaDto dto, String cpfLogado) throws RecursoJaCadastradoException {
		boolean configExistente = repository.existsByHoraInicioAndHoraLimiteAndPrazoEntrega(dto.getHoraInicio(), dto.getHoraLimite(), dto.getPrazoEntrega());
		if(configExistente){
			throw new RecursoJaCadastradoException("Configuração já cadastrada.");
		}
		
		ConfigPrevisaoEntregaModel model = new ConfigPrevisaoEntregaModel(
				null, 
				dto.getHoraInicio(), 
				dto.getHoraLimite(), 
				dto.getPrazoEntrega(), 
				LocalDateTime.now(), 
				cpfLogado, 
				null, 
				null);

		String idSalvo = repository.save(model).getId();

		return new ConfigPrevisaoEntregaResponseDto(
				idSalvo, 
				model.getHoraInicio(), 
				model.getHoraLimite(), 
				model.getPrazoEntrega(), 
				model.getDatahoraInclusao(), 
				model.getUsuarioInclusao(), 
				model.getDatahoraAtualizacao(), 
				model.getUsuarioAtualizacao());
	}

	public ConfigPrevisaoEntregaResponseDto alterarConfiguracaoPrevisaoEntrega(String id, ConfigPrevisaoEntregaDto configDto, String cpfUsuarioAlteracao) throws RecursoNaoEncontradoException {
		Optional<ConfigPrevisaoEntregaModel> config = repository.findById(id);
		if(!config.isPresent()){
			throw new RecursoNaoEncontradoException("Não foi encontrado registro para o id informado.");
		}
		
		ConfigPrevisaoEntregaModel configModel = config.get();
		
		configModel.setHoraInicio(configDto.getHoraInicio());
		configModel.setHoraLimite(configDto.getHoraLimite());
		configModel.setPrazoEntrega(configDto.getPrazoEntrega());

		configModel.setUsuarioAtualizacao(cpfUsuarioAlteracao);
		configModel.setDatahoraAtualizacao(LocalDateTime.now());
		
		repository.save(configModel);
		
		return new ConfigPrevisaoEntregaResponseDto(
				configModel.getId(), 
				configModel.getHoraInicio(), 
				configModel.getHoraLimite(), 
				configModel.getPrazoEntrega(), 
				configModel.getDatahoraInclusao(), 
				configModel.getUsuarioInclusao(), 
				configModel.getDatahoraAtualizacao(), 
				configModel.getUsuarioAtualizacao());
	}

	public void excluirConfiguracaoPrevisaoEntrega(String id) throws RecursoNaoEncontradoException {
		Optional<ConfigPrevisaoEntregaModel> config = repository.findById(id);
		if(!config.isPresent()){
			throw new RecursoNaoEncontradoException("Não foi encontrado registro para o id informado.");
		}
		repository.deleteById(id);
	}
}
