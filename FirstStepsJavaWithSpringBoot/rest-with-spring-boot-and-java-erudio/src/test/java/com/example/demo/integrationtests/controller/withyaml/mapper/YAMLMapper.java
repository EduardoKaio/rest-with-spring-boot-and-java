package com.example.demo.integrationtests.controller.withyaml.mapper;

import java.util.logging.Logger;

import org.hibernate.query.sqm.mutation.internal.cte.CteDeleteHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import groovyjarjarantlr4.v4.gui.TestRig;
import io.restassured.mapper.ObjectMapper;
import io.restassured.mapper.ObjectMapperDeserializationContext;
import io.restassured.mapper.ObjectMapperSerializationContext;

public class YAMLMapper implements ObjectMapper{
	
	private Logger logger = Logger.getLogger(YAMLMapper.class.getName());
	
	private com.fasterxml.jackson.databind.ObjectMapper objectMapper;
	protected TypeFactory typeFactory;

	public YAMLMapper() {
		objectMapper = new com.fasterxml.jackson.databind.ObjectMapper(new YAMLFactory());
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		typeFactory = TypeFactory.defaultInstance();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object deserialize(ObjectMapperDeserializationContext context) {
		try {
			String dataToDeserialize = context.getDataToDeserialize().asString();
			Class type = (Class)context.getType();
			
            logger.info("Trying deserialize object of type" + type);

			return objectMapper.readValue(dataToDeserialize, typeFactory.constructType(type));
		} catch (JsonMappingException e) {
            logger.severe("Error deserializing object");
			e.printStackTrace();
		} catch (JsonProcessingException e) {
            logger.severe("Error deserializing object");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Object serialize(ObjectMapperSerializationContext context) {
		try {
			return objectMapper.writeValueAsString(context.getObjectToSerialize());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

}
