package br.com.gabricio.otif.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.gabricio.otif.dto.RelNivelEntregaTransportadora;
import br.com.gabricio.otif.dto.RelNivelEntregaTransportadoraResponseDto;
import br.com.gabricio.otif.model.RomaneiosModel;
import br.com.gabricio.otif.repositories.RomaneiosRepository;
import br.com.gabricio.otif.utils.DataUtil;

@Service
public class RelNivelEntregaService {

	@Autowired
	RomaneiosRepository romaneiosRepository;


	public List<RelNivelEntregaTransportadoraResponseDto> relNivelEntregaTransportadora(LocalDateTime dataIni, LocalDateTime dataFim) {
		List<RomaneiosModel> listaRomaneiosSalvos = romaneiosRepository.findByDatahoraEntregueIsNotNullAndDatahoraSaidaRomaneioBetween(dataIni, dataFim, Sort.by(Sort.Direction.ASC, "datahoraSaidaRomaneio"));

		List<RelNivelEntregaTransportadoraResponseDto> listaRomaneiosDto = preencheListRelNivelEntregaTransportadoraResponseDto(listaRomaneiosSalvos);

		return listaRomaneiosDto;
	}


	private List<RelNivelEntregaTransportadoraResponseDto> preencheListRelNivelEntregaTransportadoraResponseDto(List<RomaneiosModel> listaRomaneiosSalvos) {
		List<RelNivelEntregaTransportadoraResponseDto> listDto = new ArrayList<>();
		
		
		Map<String, RelNivelEntregaTransportadora>  map = new HashMap<>();
		if(!listaRomaneiosSalvos.isEmpty()) {

			for (RomaneiosModel romaneio : listaRomaneiosSalvos) {
				String chave = getChaveRelatorio(romaneio);

				if(map.containsKey(chave)) {
					RelNivelEntregaTransportadora rel = map.get(chave);

					rel.setVolumesEntreguesNoPrazo(
							ehEntregaDentroDoPrazo(romaneio) ?
									rel.getVolumesEntreguesNoPrazo() + 1 : 0
							);

					rel.setVolumesEntreguesNaQtdeCorreta(
							ehEntregaQuantidadeCorreta(romaneio) ?
									rel.getVolumesEntreguesNaQtdeCorreta() + 1 : 0
							);
					
					rel.setQtdeEntregas(rel.getQtdeEntregas() + 1);

					map.put(chave, rel);
				} else {
					RelNivelEntregaTransportadora rel = new RelNivelEntregaTransportadora(
							DataUtil.imprimeData(romaneio.getDatahoraSaidaRomaneio(), DataUtil.MMMYY),
							romaneio.getRotaNome(), 
							romaneio.getLojaDestino(), 
							romaneio.getTransportadora(), 
							ehEntregaDentroDoPrazo(romaneio) ? 1 : 0, 
							ehEntregaQuantidadeCorreta(romaneio) ? 1 : 0, 
							1 
							);

					map.put(chave, rel);
				}
			}
						
			for (Map.Entry<String, RelNivelEntregaTransportadora> entry : map.entrySet()) {
				RelNivelEntregaTransportadora value = entry.getValue();
				
			    listDto.add(new RelNivelEntregaTransportadoraResponseDto(
			    		value.getPeriodo(), 
			    		value.getRota(), 
			    		value.getFilial(), 
			    		value.getTransportadora(), 
			    		value.getVolumesEntreguesNoPrazo() * 100 / value.getQtdeEntregas(), 
			    		value.getVolumesEntreguesNaQtdeCorreta() * 100 / value.getQtdeEntregas(), 
			    		((value.getVolumesEntreguesNoPrazo() * 100 / value.getQtdeEntregas()) + 
			    		(value.getVolumesEntreguesNaQtdeCorreta() * 100 / value.getQtdeEntregas())) /2
			    		));
			}
		}

		return listDto;
	}


	private String getChaveRelatorio (RomaneiosModel romaneios) {
		return String.valueOf(romaneios.getRotaId()).concat(String.valueOf(romaneios.getIdLojaDestino())).concat(String.valueOf(romaneios.getIdTransportadora()));
	}

	private boolean ehEntregaDentroDoPrazo(RomaneiosModel romaneio) {
		try {
			
			if(romaneio.getDatahoraPrevisaoEntrega() == null) {
				return true;
			}
			
			return romaneio.getDatahoraEntregue().isBefore(romaneio.getDatahoraPrevisaoEntrega()) || romaneio.getDatahoraEntregue().isEqual(romaneio.getDatahoraPrevisaoEntrega());
			
		} catch (Exception e) {
			return false;
		}
	}

	private boolean ehEntregaQuantidadeCorreta(RomaneiosModel romaneio) {
		try {
			return romaneio.getVolumeEntregue().intValue() == 1;
		} catch (Exception e) {
			return false;
		}
	}
}
