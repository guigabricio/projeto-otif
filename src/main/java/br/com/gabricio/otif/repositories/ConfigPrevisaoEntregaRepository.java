package br.com.gabricio.otif.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.gabricio.otif.model.ConfigPrevisaoEntregaModel;

@Repository
public interface ConfigPrevisaoEntregaRepository extends MongoRepository<ConfigPrevisaoEntregaModel, String> {
	
	boolean existsByHoraInicioAndHoraLimiteAndPrazoEntrega(String horaInicio,String horaLimite,Integer prazoEntrega);
	
}
