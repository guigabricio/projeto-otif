package br.com.gabricio.otif.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.gabricio.otif.dto.RelRomaneioCargaResponseDto;
import br.com.gabricio.otif.model.RomaneiosModel;
import br.com.gabricio.otif.repositories.RelRomaneiosRepositoryCustom;
import br.com.gabricio.otif.repositories.RomaneiosRepository;
import br.com.gabricio.otif.utils.DataUtil;

@Service
public class RelRomaneiosCargasService {

	@Autowired
	RomaneiosRepository romaneiosRepository;

	@Autowired
	RelRomaneiosRepositoryCustom relRomaneiosRepository;

	public List<RelRomaneioCargaResponseDto> relRomaneiosCarga(LocalDateTime dataIni, LocalDateTime dataFim) {
		List<RelRomaneioCargaResponseDto> listaRomaneiosDto = new ArrayList<>();

		List<RomaneiosModel> listaRomaneiosSalvos = romaneiosRepository.findByDatahoraSaidaRomaneioBetween(dataIni, dataFim, Sort.by(Sort.Direction.ASC,"datahoraSaidaRomaneio","idAgrupamentoRomaneio","romaneioNr","notaNr","volumeNr"));

		Map<String, RelRomaneioCargaResponseDto>  map = new HashMap<>();
		if(!listaRomaneiosSalvos.isEmpty()) {

			for (RomaneiosModel romaneiosModel : listaRomaneiosSalvos) {
				String chave = getChaveRelatorio(romaneiosModel);

				RelRomaneioCargaResponseDto rel = new RelRomaneioCargaResponseDto(
						DataUtil.imprimeData(romaneiosModel.getDatahoraSaidaRomaneio(), DataUtil.MMMYY),
						romaneiosModel.getIdAgrupamentoRomaneio(), 
						romaneiosModel.getRomaneioNr(), 
						romaneiosModel.getNotaNr(), 
						romaneiosModel.getVolumeNr(), 
						romaneiosModel.getLojaDestino(), 
						romaneiosModel.getRotaNome(), 
						romaneiosModel.getTransportadora(), 
						romaneiosModel.getStatusEntrega(), 
						romaneiosModel.getDatahoraSaidaRomaneio(), 
						romaneiosModel.getDatahoraRegistroCarregamento(), 
						romaneiosModel.getDatahoraSaidaFabrica(), 
						romaneiosModel.getDatahoraPrevisaoEntrega(), 
						romaneiosModel.getDatahoraEntregue(), 
						romaneiosModel.getDatahoraSaidaFabrica(), 
						romaneiosModel.getDatahoraSaidaRomaneio(), 
						romaneiosModel.getDatahoraRegistroCarregamento(), 
						romaneiosModel.getDatahoraConfirmacaoEntrega());

				map.put(chave, rel);
			}


			for (Map.Entry<String, RelRomaneioCargaResponseDto> entry : map.entrySet()) {
				RelRomaneioCargaResponseDto value = entry.getValue();

				listaRomaneiosDto.add(value);
			}
		}

		return listaRomaneiosDto;
	}

	private String getChaveRelatorio (RomaneiosModel romaneios) {
		return String.valueOf(romaneios.getIdAgrupamentoRomaneio()).concat(String.valueOf(romaneios.getNotaNr())).concat(String.valueOf(romaneios.getRomaneioNr()));
	}
}
