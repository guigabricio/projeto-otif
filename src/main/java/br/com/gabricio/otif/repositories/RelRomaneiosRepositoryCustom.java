package br.com.gabricio.otif.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import br.com.gabricio.otif.dto.OutRelNivelEntrega;
import br.com.gabricio.otif.model.RomaneiosModel;

@Service
public class RelRomaneiosRepositoryCustom {

	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	RomaneiosRepository repository;


	public List<OutRelNivelEntrega> getDistinctIdLojaDestino(LocalDateTime dataini,LocalDateTime dataFim ){
		MatchOperation empIdgt10 = Aggregation.match(new Criteria("datahoraSaidaRomaneio").gte(dataini).lte(dataFim).and("statusEntrega").is("Concluido"));
				
		GroupOperation groupOperation = Aggregation.group("idLojaDestino").count().as("qtde");

		ProjectionOperation projectionOperation = Aggregation.project("qtde").and("idLojaDestino").previousOperation();

		SortOperation sortOperation = Aggregation.sort(Sort.by(Sort.Direction.ASC, "idLojaDestino"));
		Aggregation aggregation = Aggregation.newAggregation(empIdgt10, groupOperation, projectionOperation, sortOperation);
		AggregationResults<OutRelNivelEntrega> list = mongoTemplate.aggregate(aggregation, RomaneiosModel.class, OutRelNivelEntrega.class);

		return list.getMappedResults();
	}
	
//	public List<String> queryAllCategory() {
//		MongoCollection<Document> mongoCollection = mongoTemplate.getCollection("romaneios");
//	    DistinctIterable<Integer> distinctIterable = mongoCollection.distinct("idLojaDestino",Integer.class);
//	    MongoCursor cursor = distinctIterable.iterator();
//	    while (cursor.hasNext()) {
//	        Integer category = (Integer)cursor.next();
//	        System.out.println(category);
//	    }
//	}
	
	
	
//	public RelNivelEntregaTransportadoraResponseDto preenche() {
//		
//		OutRelNivelEntrega out = new OutRelNivelEntrega();
//		
//		out.getIdLojaDestino();
//		
//		
//		
//		return new RelNivelEntregaTransportadoraResponseDto(
//				periodo, 
//				rota, 
//				filial, 
//				transportadora, 
//				volumesEntreguesNoPrazo, 
//				volumesEntreguesNaQtdeCorreta, 
//				indicador, 
//				dataHoraSaidaRomaneio);
//		
//	}
	
	
	
	

}
