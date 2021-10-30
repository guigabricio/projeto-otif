package br.com.gabricio.otif.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.gabricio.otif.dto.RelFaltaVolumeResponseDto;
import br.com.gabricio.otif.model.RomaneiosModel;
import br.com.gabricio.otif.repositories.RelRomaneiosRepositoryCustom;
import br.com.gabricio.otif.repositories.RomaneiosRepository;
import br.com.gabricio.otif.utils.DataUtil;


@Service
public class RelFaltaVolumeService {

	@Autowired
	RomaneiosRepository romaneiosRepository;

	@Autowired
	RelRomaneiosRepositoryCustom relRomaneiosRepository;

	public List<RelFaltaVolumeResponseDto> relFaltaVolume(LocalDateTime dataIni, LocalDateTime dataFim) {

		//volume_entregue = 1 -> Foi entregue corretamnte
		//volume_entregue = 0 -> Foi entregue  com pendencias
		List<RomaneiosModel> listaRomaneiosSalvos = romaneiosRepository.findByVolumeEntregueAndDatahoraEntregueIsNotNullAndDatahoraSaidaRomaneioBetween(0, dataIni, dataFim, Sort.by(Sort.Direction.ASC,"datahoraSaidaRomaneio","idAgrupamentoRomaneio","romaneioNr","notaNr","volumeNr"));
		
		List<RelFaltaVolumeResponseDto> listaRomaneiosDto = preencheListRelFaltaVolumeResponseDto(listaRomaneiosSalvos);
	
		return listaRomaneiosDto;
	}
	
	private List<RelFaltaVolumeResponseDto> preencheListRelFaltaVolumeResponseDto(List<RomaneiosModel> listaRomaneiosSalvos) {
		List<RelFaltaVolumeResponseDto> listDto = new ArrayList<>();


		Map<String, RelFaltaVolumeResponseDto>  map = new HashMap<>();
		if(!listaRomaneiosSalvos.isEmpty()) {

			for (RomaneiosModel romaneio : listaRomaneiosSalvos) {
				String chave = getChaveRelatorio(romaneio);

				if(map.containsKey(chave)) {
					RelFaltaVolumeResponseDto rel = map.get(chave);

					rel.setFaltaVolume(rel.getFaltaVolume() + 1);

					map.put(chave, rel);
				} else {
					
					RelFaltaVolumeResponseDto rel = new RelFaltaVolumeResponseDto(
							DataUtil.imprimeData(romaneio.getDatahoraSaidaRomaneio(), DataUtil.MMMYY), 
							romaneio.getIdAgrupamentoRomaneio(), 
							romaneio.getRomaneioNr(), 
							romaneio.getNotaNr(), 
							1, 
							romaneio.getVolumeEntregue(), 
							romaneio.getVolumeEntregue() == 1 ? "Entrega sem pendências " : "Entrega com pendências", 
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
			

			for (Map.Entry<String, RelFaltaVolumeResponseDto> entry : map.entrySet()) {
				RelFaltaVolumeResponseDto value = entry.getValue();

				listDto.add(value);
			}
		}

		return listDto;
	}
	
	private String getChaveRelatorio (RomaneiosModel romaneios) {
		return String.valueOf(romaneios.getIdAgrupamentoRomaneio()).concat(String.valueOf(romaneios.getNotaNr())).concat(String.valueOf(romaneios.getRomaneioNr()));
	}

}
