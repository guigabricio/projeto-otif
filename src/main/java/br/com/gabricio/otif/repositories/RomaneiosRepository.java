package br.com.gabricio.otif.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.gabricio.otif.model.RomaneiosModel;

@Repository
public interface RomaneiosRepository extends MongoRepository<RomaneiosModel, String> {
	
	boolean existsByRomaneioNr(Integer romaneioNr);
	
	List<RomaneiosModel> findByIdAgrupamentoRomaneio(Integer idAgrupamentoRomaneio);
	
	List<RomaneiosModel> findByRomaneioNr(Integer romaneioNr);
	
	List<RomaneiosModel> findByIdAgrupamentoRomaneio(Integer idAgrupamentoRomaneio, Sort sort);
	
	Optional<RomaneiosModel> findByIdAgrupamentoRomaneioAndRomaneioNrAndNotaNrAndVolumeNr(Integer idAgrupamentoRomaneio, Integer romaneioNr, Integer notaNr, Integer volumeNr);
	
	List<RomaneiosModel> findByDatahoraConfirmacaoEntregaIsNotNullAndDatahoraEntregueIsNotNullAndDatahoraSaidaRomaneioBetween(LocalDateTime dataIni, LocalDateTime dataFim, Sort by);
	
	List<RomaneiosModel> findByVolumeEntregueAndDatahoraEntregueIsNotNullAndDatahoraSaidaRomaneioBetween(Integer volumeEntregue, LocalDateTime dataInicio, LocalDateTime dataFim, Sort sort);
	
	List<RomaneiosModel> findByDatahoraEntregueIsNotNullAndDatahoraSaidaRomaneioBetween(LocalDateTime dataInicio, LocalDateTime dataFim, Sort sort);
	
	List<RomaneiosModel> findByDatahoraEntregueIsNullAndDatahoraSaidaRomaneioBetween(LocalDateTime dataInicio, LocalDateTime dataFim, Sort sort);
	
	@Query("{ 'datahoraSaidaRomaneio' : { $gte: ?0, $lte: ?1 } }")
	List<RomaneiosModel> findByDatahoraSaidaRomaneioBetween(LocalDateTime dataInicio, LocalDateTime dataFim, Sort sort);
	
	@Query("{ 'datahoraSaidaRomaneio' : { $gte: ?0, $lte: ?1 } }")
	List<RomaneiosModel> findByDatahoraSaidaRomaneioBetweenAndVolumeEntregue(LocalDateTime dataInicio, LocalDateTime dataFim, Integer volumeEntregue, Sort sort);
	
	int countByIdAgrupamentoRomaneioAndRomaneioNrAndNotaNrAndDatahoraEntregueIsNull(Integer idAgrupamentoRomaneio, Integer romaneioNr, Integer notaNr);
	
	int countByIdAgrupamentoRomaneioAndRomaneioNrAndNotaNrAndDatahoraEntregueIsNotNull(Integer idAgrupamentoRomaneio, Integer romaneioNr, Integer notaNr);
	
	int countByIdAgrupamentoRomaneioAndRomaneioNrAndNotaNrAndRotaNomeAndVolumeEntregue(Integer idAgrupamentoRomaneio, Integer romaneioNr, Integer notaNr, String rotaNome, Integer volumeEntregue);
}
