package br.com.gabricio.otif.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.gabricio.otif.dto.CarregamentoDto;
import br.com.gabricio.otif.dto.EntregaArrayDto;
import br.com.gabricio.otif.dto.EntregaDto;
import br.com.gabricio.otif.dto.RomaneiosCargaDto;
import br.com.gabricio.otif.dto.RomaneiosConsultaDto;
import br.com.gabricio.otif.dto.RomaneiosListaCargaDto;
import br.com.gabricio.otif.dto.SaidaDto;
import br.com.gabricio.otif.exception.NenhumItemEncontradoException;
import br.com.gabricio.otif.exception.RecursoNaoEncontradoException;
import br.com.gabricio.otif.model.HistoricoLeituraModel;
import br.com.gabricio.otif.model.RomaneiosModel;
import br.com.gabricio.otif.model.TipoLeituraEnum;
import br.com.gabricio.otif.model.UsuariosModel;
import br.com.gabricio.otif.repositories.HistoricoLeituraRepository;
import br.com.gabricio.otif.repositories.RomaneiosRepository;
import br.com.gabricio.otif.repositories.UsuariosRepository;
import br.com.gabricio.otif.utils.CpfCnpjUtil;
import br.com.gabricio.otif.utils.DataUtil;

@Service
public class RomaneiosService {

	@Autowired
	private RomaneiosRepository romaneioRepository;

	@Autowired
	private UsuariosRepository usuariosRepository;

	@Autowired
	private HistoricoLeituraRepository historicoLeituraRepository;

	@Autowired
	private ConfigPrevisaoEntregaService previsaoEntregaService;

	public void insereAlteraCargaDeRomaneios(RomaneiosListaCargaDto listaRomaneiosDto, String cpfUsuarioLogado) throws NenhumItemEncontradoException {
		List<RomaneiosCargaDto> data = listaRomaneiosDto.getData();

		if(data == null || data.isEmpty()) {
			throw new NenhumItemEncontradoException("Lista de romaneios vazia.");
		}

		RomaneiosModel romaneioModel = null;

		for (RomaneiosCargaDto romaneioCargaDto : data) {
			Optional<RomaneiosModel> existeRomaneio = romaneioRepository.findByIdAgrupamentoRomaneioAndRomaneioNrAndNotaNrAndVolumeNr(
					romaneioCargaDto.getIdAgrupamentoRomaneio(), 
					romaneioCargaDto.getRomaneioNr(), 	
					romaneioCargaDto.getNotaNr(), 
					romaneioCargaDto.getVolumeNr());

			if(existeRomaneio.isPresent()) {

				romaneioModel = existeRomaneio.get();

				romaneioModel.setDatahoraSaidaRomaneio(DataUtil.converteStringToLocalDateTime(romaneioCargaDto.getDatahoraSaidaRomaneio(),DataUtil.DDMMYYY_HHMMSS));		
				romaneioModel.setIdLojaDestino(romaneioCargaDto.getIdLojaDestino());
				romaneioModel.setLojaDestino(romaneioCargaDto.getLojaDestino());
				romaneioModel.setStatusEntrega(romaneioCargaDto.getStatusEntrega());
				romaneioModel.setVolumeEntregue(romaneioCargaDto.getVolumeEntregue());
				romaneioModel.setIdTransportadora(romaneioCargaDto.getIdTransportadora());
				romaneioModel.setTransportadora(romaneioCargaDto.getTransportadora());
				romaneioModel.setCnpjTransportadora(romaneioCargaDto.getCnpjTransportadora());

				romaneioModel.setDatahoraEntregue(DataUtil.converteStringToLocalDateTime(romaneioCargaDto.getDatahoraEntregue(),DataUtil.DDMMYYY_HHMMSS));
				romaneioModel.setRotaId(romaneioCargaDto.getRotaId());
				romaneioModel.setRotaNome(romaneioCargaDto.getRotaNome());

				romaneioModel.setUsuarioAtualizacao(cpfUsuarioLogado);
				romaneioModel.setDatahoraAtualizacao(LocalDateTime.now());

			} else {
				romaneioModel = new RomaneiosModel(
						romaneioCargaDto.getIdAgrupamentoRomaneio(), 
						romaneioCargaDto.getRomaneioNr(), 
						romaneioCargaDto.getNotaNr(), 
						romaneioCargaDto.getVolumeNr(),  
						DataUtil.converteStringToLocalDateTime(romaneioCargaDto.getDatahoraSaidaRomaneio(),DataUtil.DDMMYYY_HHMMSS), 
						romaneioCargaDto.getIdLojaDestino(), 
						romaneioCargaDto.getLojaDestino(), 
						romaneioCargaDto.getStatusEntrega(), 
						romaneioCargaDto.getVolumeEntregue(), 
						romaneioCargaDto.getIdTransportadora(), 
						romaneioCargaDto.getTransportadora(), 
						romaneioCargaDto.getCnpjTransportadora(), 
						DataUtil.converteStringToLocalDateTime(romaneioCargaDto.getDatahoraEntregue(),DataUtil.DDMMYYY_HHMMSS),
						romaneioCargaDto.getRotaId(),
						romaneioCargaDto.getRotaNome(),
						LocalDateTime.now(),
						cpfUsuarioLogado);
			}

			romaneioRepository.save(romaneioModel);
		}
	}

	public void registraCarregamento(CarregamentoDto carregamentoDto, String cpfUsarioLogado) throws RecursoNaoEncontradoException, NenhumItemEncontradoException {
		HistoricoLeituraModel histoLeituraModel = new HistoricoLeituraModel(
				null,//id, 
				carregamentoDto.getIdAgrupamento(),//idLeitura, 
				cpfUsarioLogado,//cpfUsuarioLoado, 
				TipoLeituraEnum.IDAGRUPAMENTO,//tipoLeitura, 
				LocalDateTime.now());//dataHoraLeitura);
		historicoLeituraRepository.save(histoLeituraModel);

		UsuariosModel usuarioModel = null;
		Optional<UsuariosModel> usuario = usuariosRepository.findByCpf(CpfCnpjUtil.removeFormatacao(cpfUsarioLogado));
		if(usuario.isPresent()){
			usuarioModel = usuario.get();

			List<RomaneiosModel> listaRomaneios = romaneioRepository.findByIdAgrupamentoRomaneio(carregamentoDto.getIdAgrupamento());
			if(listaRomaneios.isEmpty()) {
				throw new NenhumItemEncontradoException("Nenhum romaneio encontrado para o Id. Agrupamento informado.");
			}

			for (RomaneiosModel romaneiosModel : listaRomaneios) {
				romaneiosModel.setNomeMotorista(usuarioModel.getNome());
				romaneiosModel.setCnpjTransportadorMotorista(usuarioModel.getCnpj());
				romaneiosModel.setCpfMotorista(usuarioModel.getCpf());
				romaneiosModel.setDatahoraRegistroCarregamento(LocalDateTime.now());

				romaneiosModel.setDatahoraAtualizacao(LocalDateTime.now());
				romaneiosModel.setUsuarioAtualizacao(cpfUsarioLogado);

				romaneioRepository.save(romaneiosModel);
			}
		} else {
			throw new RecursoNaoEncontradoException("Usuário não encontrado.");
		}
	}

	public void registraSaida(SaidaDto saidaDto, String cpfUsarioLogado) throws RecursoNaoEncontradoException, NenhumItemEncontradoException {
		HistoricoLeituraModel histoLeituraModel = new HistoricoLeituraModel(
				null,//id, 
				saidaDto.getIdAgrupamento(),//idLeitura, 
				cpfUsarioLogado,//cpfUsuarioLoado, 
				TipoLeituraEnum.IDAGRUPAMENTO,//tipoLeitura, 
				LocalDateTime.now());//dataHoraLeitura);
		historicoLeituraRepository.save(histoLeituraModel);

		UsuariosModel usuarioModel = null;
		Optional<UsuariosModel> usuario = usuariosRepository.findByCpf(CpfCnpjUtil.removeFormatacao(cpfUsarioLogado));
		if(usuario.isPresent()){
			usuarioModel = usuario.get();

			List<RomaneiosModel> listaRomaneios = romaneioRepository.findByIdAgrupamentoRomaneio(saidaDto.getIdAgrupamento());
			if(listaRomaneios.isEmpty()) {
				throw new NenhumItemEncontradoException("Nenhum romaneio encontrado para o Id. Agrupamento informado.");
			}

			for (RomaneiosModel romaneiosModel : listaRomaneios) {
				romaneiosModel.setNomeUsuarioGuarita(usuarioModel.getNome());
				romaneiosModel.setCpfUsuarioGuarita(usuarioModel.getCpf());
				romaneiosModel.setDatahoraSaidaFabrica(LocalDateTime.now());

				romaneiosModel.setDatahoraPrevisaoEntrega(previsaoEntregaService.calculaDataPrevisaoEntrega());

				romaneiosModel.setDatahoraAtualizacao(LocalDateTime.now());
				romaneiosModel.setUsuarioAtualizacao(cpfUsarioLogado);

				romaneioRepository.save(romaneiosModel);
			}
		} else {
			throw new RecursoNaoEncontradoException("Usuário não encontrado.");
		}
	}

	public void registraEntrega(EntregaArrayDto entregaArrayDto, String cpfUsarioLogado) throws RecursoNaoEncontradoException, NenhumItemEncontradoException {
		Optional<UsuariosModel> usuario = usuariosRepository.findByCpf(CpfCnpjUtil.removeFormatacao(cpfUsarioLogado));
		if(usuario.isPresent()){

			UsuariosModel usuarioModel = usuario.get();

			List<EntregaDto> listaIdRomaneio = entregaArrayDto.getListaIdRomaneios();

			for (EntregaDto entregaDto : listaIdRomaneio) {
				if(!romaneioRepository.existsByRomaneioNr(entregaDto.getIdRomaneio())) {
					throw new NenhumItemEncontradoException("Nenhum romaneio encontrado para o Id. Romaneio informado." + String.valueOf(entregaDto.getIdRomaneio()));	
				}
			}

			for (EntregaDto entregaDto : listaIdRomaneio) {

				HistoricoLeituraModel histoLeituraModel = new HistoricoLeituraModel(
						null,//id, 
						entregaDto.getIdRomaneio(),//idLeitura, 
						cpfUsarioLogado,//cpfUsuarioLoado, 
						TipoLeituraEnum.IDROMANEIO,//tipoLeitura, 
						LocalDateTime.now());//dataHoraLeitura);
				historicoLeituraRepository.save(histoLeituraModel);

				List<RomaneiosModel> listaRomaneios = romaneioRepository.findByRomaneioNr(entregaDto.getIdRomaneio());
				if(listaRomaneios.isEmpty()) {
					throw new NenhumItemEncontradoException("Nenhum romaneio encontrado para o Id. Romaneio informado.");
				}

				for (RomaneiosModel romaneiosModel : listaRomaneios) {

					romaneiosModel.setNomeMotoristaEntrega(usuarioModel.getNome());
					romaneiosModel.setCpfMotoristaEntrega(usuarioModel.getCpf());
					romaneiosModel.setCnpjTransportadoraEntrega(usuarioModel.getCnpj());
					romaneiosModel.setDatahoraConfirmacaoEntrega(LocalDateTime.now());

					romaneiosModel.setDatahoraAtualizacao(LocalDateTime.now());
					romaneiosModel.setUsuarioAtualizacao(cpfUsarioLogado);

					romaneioRepository.save(romaneiosModel);
				}
			}
		} else {
			throw new RecursoNaoEncontradoException("Usuário não encontrado.");
		}
	}

	public List<RomaneiosConsultaDto> consultaRomaneio(Integer idAgrupamento) throws NenhumItemEncontradoException {
		List<RomaneiosModel> listaRomaneios = romaneioRepository.findByIdAgrupamentoRomaneio(idAgrupamento, Sort.by(Sort.Direction.ASC,"idAgrupamentoRomaneio","romaneioNr","notaNr","volumeNr"));
		if(listaRomaneios.isEmpty()) {
			throw new NenhumItemEncontradoException("Nenhum romaneio encontrado.");
		}

		List<RomaneiosConsultaDto> listaRomaneiosDto = new ArrayList<>();

		for (RomaneiosModel romaneiosModel : listaRomaneios) {
			listaRomaneiosDto.add(new RomaneiosConsultaDto(
					romaneiosModel.getId(), 
					romaneiosModel.getIdAgrupamentoRomaneio(), 
					romaneiosModel.getRomaneioNr(), 
					romaneiosModel.getNotaNr(), 
					romaneiosModel.getVolumeNr(), 
					romaneiosModel.getIdLojaDestino(), 
					romaneiosModel.getLojaDestino(), 
					romaneiosModel.getStatusEntrega(), 
					romaneiosModel.getVolumeEntregue(), 
					romaneiosModel.getIdTransportadora(), 
					romaneiosModel.getTransportadora(), 
					romaneiosModel.getCnpjTransportadora(), 
					romaneiosModel.getRotaId(), 
					romaneiosModel.getRotaNome(), 
					romaneiosModel.getNomeMotorista(), 
					romaneiosModel.getCpfMotorista(), 
					romaneiosModel.getCnpjTransportadorMotorista(), 
					romaneiosModel.getNomeUsuarioGuarita(), 
					romaneiosModel.getCpfUsuarioGuarita(), 
					romaneiosModel.getNomeMotoristaEntrega(), 
					romaneiosModel.getCpfMotoristaEntrega(), 
					romaneiosModel.getCnpjTransportadoraEntrega(), 
					romaneiosModel.getUsuarioInclusao(), 
					romaneiosModel.getUsuarioAtualizacao(), 
					romaneiosModel.getDatahoraSaidaFabrica(), 
					romaneiosModel.getDatahoraSaidaRomaneio(), 
					romaneiosModel.getDatahoraPrevisaoEntrega(), 
					romaneiosModel.getDatahoraEntregue(), 
					romaneiosModel.getDatahoraRegistroCarregamento(), 
					romaneiosModel.getDatahoraConfirmacaoEntrega(), 
					romaneiosModel.getDatahoraInclusao(), 
					romaneiosModel.getDatahoraAtualizacao()
					));

		}
		return listaRomaneiosDto;
	}
}
