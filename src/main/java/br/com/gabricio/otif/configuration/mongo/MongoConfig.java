package br.com.gabricio.otif.configuration.mongo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.client.MongoClients;

@Configuration
public class MongoConfig {

	@Value("${spring.data.mongodb.uri}")
	String dataBaseUri;
	
	@Value("${otif.database}")
	String dataBase;

	@Bean
	public MongoTemplate mongoTemplate() {

		MongoTemplate mongoTemplate = new MongoTemplate(MongoClients.create(dataBaseUri), "otif");

		return mongoTemplate;
	}

}