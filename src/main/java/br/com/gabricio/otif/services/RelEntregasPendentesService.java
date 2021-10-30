package br.com.gabricio.otif.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.gabricio.otif.dto.RelEntregaPendentesResponseDto;
import br.com.gabricio.otif.model.RomaneiosModel;
import br.com.gabricio.otif.repositories.RomaneiosRepository;
import br.com.gabricio.otif.utils.DataUtil;

@Service
public class RelEntregasPendentesService {

	@Autowired
	RomaneiosRepository romaneiosRepository;


	public List<RelEntregaPendentesResponseDto> relEntregasPendentes(LocalDateTime dataIni, LocalDateTime dataFim) {
		List<RomaneiosModel> listaRomaneiosSalvos = romaneiosRepository.findByDatahoraEntregueIsNullAndDatahoraSaidaRomaneioBetween(dataIni, dataFim, Sort.by(Sort.Direction.ASC,
				"datahoraSaidaRomaneio",
				"datahoraEntregue",
				"datahoraRegistroCarregamento",
				"datahoraSaidaFabrica",
				"idAgrupamentoRomaneio","romaneioNr","notaNr","volumeNr"));

		List<RelEntregaPendentesResponseDto> listaRomaneiosDto = preencheListRelEntregaPendentesResponseDto(listaRomaneiosSalvos);

		return listaRomaneiosDto;
	}


	private List<RelEntregaPendentesResponseDto> preencheListRelEntregaPendentesResponseDto(List<RomaneiosModel> listaRomaneiosSalvos) {
		List<RelEntregaPendentesResponseDto> listDto = new ArrayList<>();


		Map<String, RelEntregaPendentesResponseDto>  map = new HashMap<>();
		if(!listaRomaneiosSalvos.isEmpty()) {

			for (RomaneiosModel romaneio : listaRomaneiosSalvos) {
				String chave = getChaveRelatorio(romaneio);

				RelEntregaPendentesResponseDto rel = new RelEntregaPendentesResponseDto(
						DataUtil.imprimeData(romaneio.getDatahoraSaidaRomaneio(), DataUtil.MMMYY),
						romaneio.getIdAgrupamentoRomaneio(), 
						romaneio.getRomaneioNr(), 
						romaneio.getNotaNr(), 
						0, 
						0, 
						romaneio.getLojaDestino(), 
						romaneio.getRotaNome(), 
						romaneio.getTransportadora(), 
						romaneio.getStatusEntrega(), 
						romaneio.getDatahoraSaidaRomaneio(), 
						romaneio.getDatahoraRegistroCarregamento(), 
						romaneio.getDatahoraSaidaFabrica(), 
						romaneio.getDatahoraPrevisaoEntrega(), 
						romaneio.getDatahoraEntregue(), 
						romaneio.getDatahoraSaidaFabrica(), 
						romaneio.getDatahoraSaidaRomaneio(), 
						romaneio.getDatahoraRegistroCarregamento(), 
						romaneio.getDatahoraConfirmacaoEntrega());

				map.put(chave, rel);
			}
		}

		for (Map.Entry<String, RelEntregaPendentesResponseDto> entry : map.entrySet()) {
			RelEntregaPendentesResponseDto value = entry.getValue();

			int entregasPendentes = romaneiosRepository.countByIdAgrupamentoRomaneioAndRomaneioNrAndNotaNrAndDatahoraEntregueIsNull(value.getAgrupamentoRomaneioId(),value.getRomaneioNr(),value.getNotaNr()); 
			int entregasRealizadas = romaneiosRepository.countByIdAgrupamentoRomaneioAndRomaneioNrAndNotaNrAndDatahoraEntregueIsNotNull(value.getAgrupamentoRomaneioId(),value.getRomaneioNr(),value.getNotaNr());

			RelEntregaPendentesResponseDto rel = new RelEntregaPendentesResponseDto(
					value.getPeriodo(), 
					value.getAgrupamentoRomaneioId(), 
					value.getRomaneioNr(), 
					value.getNotaNr(), 
					entregasPendentes, 
					entregasRealizadas, 
					value.getLojaDestino(), 
					value.getRotaNome(), 
					value.getTransportadora(), 
					entregasPendentes == 0 ? "Concluído" : "Pendente", 
							value.getDataHoraSaidaRomaneio(), 
							value.getDataHoraRegistroCarregamento(), 
							value.getDataHoraSaidaFabrica(), 
							value.getDatahoraPrevisaoEntrega(), 
							value.getDatahoraEntregue(), 
							value.getDataHoraSaidaFabrica(), 
							value.getDataHoraSaidaRomaneio(), 
							value.getDataHoraRegistroCarregamento(), 
							value.getDatahoraConfirmacaoEntrega()
					);

			listDto.add(rel);
		}

		return listDto;
	}


	private String getChaveRelatorio (RomaneiosModel romaneios) {
		return String.valueOf(romaneios.getIdAgrupamentoRomaneio()).concat(String.valueOf(romaneios.getNotaNr())).concat(String.valueOf(romaneios.getRomaneioNr()));
	}
	
}
