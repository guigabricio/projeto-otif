package br.com.gabricio.otif.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.gabricio.otif.dto.HistoricoLeituraReponseDto;
import br.com.gabricio.otif.model.HistoricoLeituraModel;
import br.com.gabricio.otif.repositories.HistoricoLeituraRepository;

@Service
public class HistoricoLeituraService {

	@Autowired
	private HistoricoLeituraRepository historicoLeituraRepository;

	public List<HistoricoLeituraReponseDto> listarTodasAsLeituras(String cpf){

		LocalDateTime dataInicio = LocalDateTime.now().minusMonths(6);

		List<HistoricoLeituraModel> listarTodos = historicoLeituraRepository.findByCpfUsuarioLogadoAndDataHoraLeituraBetween(cpf, dataInicio, LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "dataHoraLeitura"));

		List<HistoricoLeituraReponseDto> listaLeiturasDto = new ArrayList<>();
		for (HistoricoLeituraModel historicoLeituraModel : listarTodos) {
			listaLeiturasDto.add(new HistoricoLeituraReponseDto(
					historicoLeituraModel.getIdLeitura(), 
					historicoLeituraModel.getTipoLeitura().toString(),
					historicoLeituraModel.getDataHoraLeitura()));
		}
		return listaLeiturasDto;

	}
}
