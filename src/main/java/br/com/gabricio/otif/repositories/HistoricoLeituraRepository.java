package br.com.gabricio.otif.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.gabricio.otif.model.HistoricoLeituraModel;

@Repository
public interface HistoricoLeituraRepository extends MongoRepository<HistoricoLeituraModel, String> {
	
	List<HistoricoLeituraModel> findByCpfUsuarioLogadoAndDataHoraLeituraBetween(String cpf, LocalDateTime dataInicio, LocalDateTime dataFIm, Sort sort );
	
}
