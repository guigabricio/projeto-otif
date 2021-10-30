package br.com.gabricio.otif.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.gabricio.otif.dto.RelNivelEntregaPrazo;
import br.com.gabricio.otif.dto.RelNivelEntregaPrazoResponseDto;
import br.com.gabricio.otif.model.RomaneiosModel;
import br.com.gabricio.otif.repositories.RomaneiosRepository;
import br.com.gabricio.otif.utils.DataUtil;

@Service
public class RelNivelEntregaPrazoService {

	@Autowired
	RomaneiosRepository romaneiosRepository;


	public List<RelNivelEntregaPrazoResponseDto> relNivelEntregaPrazo(LocalDateTime dataIni, LocalDateTime dataFim) {
		List<RomaneiosModel> listaRomaneiosSalvos = romaneiosRepository.findByDatahoraEntregueIsNotNullAndDatahoraSaidaRomaneioBetween(dataIni, dataFim, Sort.by(Sort.Direction.ASC, "datahoraSaidaRomaneio"));

		List<RelNivelEntregaPrazoResponseDto> listaRomaneiosDto = preencheListRelNivelEntregaPrazoResponseDto(listaRomaneiosSalvos);

		return listaRomaneiosDto;
	}


	private List<RelNivelEntregaPrazoResponseDto> preencheListRelNivelEntregaPrazoResponseDto(List<RomaneiosModel> listaRomaneiosSalvos) {
		List<RelNivelEntregaPrazoResponseDto> listDto = new ArrayList<>();


		Map<String, RelNivelEntregaPrazo>  map = new HashMap<>();
		if(!listaRomaneiosSalvos.isEmpty()) {

			for (RomaneiosModel romaneio : listaRomaneiosSalvos) {
				String chave = getChaveRelatorio(romaneio);

				if(map.containsKey(chave)) {
					RelNivelEntregaPrazo rel = map.get(chave);

					rel.setVolumesEntreguesNoPrazo(
							ehEntregaDentroDoPrazo(romaneio) ?
									rel.getVolumesEntreguesNoPrazo() + 1 : 0
							);

					rel.setVolumesEntreguesNaQtdeCorreta(
							ehEntregaDentroDoPrazoEVolumeCorreto(romaneio) ?
									rel.getVolumesEntreguesNaQtdeCorreta() + 1 : 0
							);

					rel.setQtde(rel.getQtde() + 1);

					map.put(chave, rel);
				} else {
					RelNivelEntregaPrazo rel = new RelNivelEntregaPrazo(
							DataUtil.imprimeData(romaneio.getDatahoraSaidaRomaneio(), DataUtil.MMMYY), 
							romaneio.getIdLojaDestino(), 
							romaneio.getLojaDestino(), 
							ehEntregaDentroDoPrazo(romaneio) ? 1 : 0, 
									ehEntregaDentroDoPrazoEVolumeCorreto(romaneio) ? 1 : 0,
											1);

					map.put(chave, rel);
				}
			}

			for (Map.Entry<String, RelNivelEntregaPrazo> entry : map.entrySet()) {
				RelNivelEntregaPrazo value = entry.getValue();

				listDto.add(new RelNivelEntregaPrazoResponseDto(
						value.getPeriodo(), 
						value.getIdLoja(), 
						value.getLoja(), 
						value.getVolumesEntreguesNoPrazo() * 100 / value.getQtde(), 
						value.getVolumesEntreguesNaQtdeCorreta() * 100 / value.getQtde(), 
						((value.getVolumesEntreguesNoPrazo() * 100 / value.getQtde()) + 
								(value.getVolumesEntreguesNaQtdeCorreta() * 100 / value.getQtde())) /2
						));
			}
		}

		return listDto;
	}


	private String getChaveRelatorio (RomaneiosModel romaneios) {
		return String.valueOf(romaneios.getIdLojaDestino());
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

	private boolean ehEntregaDentroDoPrazoEVolumeCorreto(RomaneiosModel romaneio) {
		try {
			return romaneio.getVolumeEntregue().intValue() == 1;
		} catch (Exception e) {
			return false;
		}
	}
}
