package com.example.demo.integrationtests.controller.withyaml;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.configs.TestConfigs;
import com.example.demo.integrationTests.vo.AccountCredentialsVO;
import com.example.demo.integrationTests.vo.PersonVO;
import com.example.demo.integrationTests.vo.TokenVO;
import com.example.demo.integrationTests.vo.pagedmodels.PagedModelPerson;
import com.example.demo.integrationtests.controller.withyaml.mapper.YAMLMapper;
import com.example.demo.integrationtests.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class PersonControllerYamlTest extends AbstractIntegrationTest{

	private static RequestSpecification specification;
	private static YAMLMapper objectMapper;

	private static PersonVO person;
	
	@BeforeAll
	public static void setup() {
		objectMapper = new YAMLMapper();
		person = new PersonVO();
	}
	
	@Test
	@Order(0)
	public void authorization() throws JsonMappingException, JsonProcessingException {
		
		AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");
				
		var accessToken = given().config(RestAssuredConfig.config()
				.encoderConfig(EncoderConfig.encoderConfig()
						.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.basePath("/auth/signin")
					.port(TestConfigs.SERVER_PORT)
					.contentType(TestConfigs.CONTENT_TYPE_YML)
					.accept(TestConfigs.CONTENT_TYPE_YML)
				.body(user, objectMapper)
					.when()
				.post()
					.then()
						.statusCode(200)
							.extract()
							.body()
							.as(TokenVO.class, objectMapper)
							.getAccessToken();
		
		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
				.setBasePath("/api/person/v1")
				.setPort(TestConfigs.SERVER_PORT)
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();

	}
	
	@Test
	@Order(1)
	public void testCreate() throws JsonMappingException, JsonProcessingException {
		mockPerson();
		
		var persistedPerson = given().spec(specification)
				.config(RestAssuredConfig.config()
						.encoderConfig(EncoderConfig.encoderConfig()
						.encodeContentTypeAs(
						TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
					.body(person, objectMapper)
					.when()
					.post()
				.then()
					.statusCode(200)
						.extract()
						.body()
							.as(PersonVO.class, objectMapper);
		
		person = persistedPerson;
		
		assertNotNull(persistedPerson);
		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAdress());
		assertNotNull(persistedPerson.getGender());
		assertTrue(persistedPerson.getEnabled());
		
		assertTrue(persistedPerson.getId() > 0);
		
		assertEquals("Kaio", persistedPerson.getFirstName());
		assertEquals("Eduardo", persistedPerson.getLastName());
		assertEquals("Natal - RN", persistedPerson.getAdress());
		assertEquals("Male", persistedPerson.getGender());

		
	}
	@Test
	@Order(2)
	public void testUpdate() throws JsonMappingException, JsonProcessingException {
		person.setLastName("Eduardo Alves de Lima");
		
		var persistedPerson = given().spec(specification)
				.config(
						RestAssuredConfig
							.config()
							.encoderConfig(EncoderConfig.encoderConfig()
								.encodeContentTypeAs(
									TestConfigs.CONTENT_TYPE_YML,
									ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
					.body(person, objectMapper)
					.when()
					.post()
				.then()
					.statusCode(200)
						.extract()
						.body()
						.as(PersonVO.class, objectMapper);
		
		person = persistedPerson;
		
		assertNotNull(persistedPerson);
		
		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAdress());
		assertNotNull(persistedPerson.getGender());
		assertTrue(persistedPerson.getEnabled());
		
		assertEquals(person.getId(), persistedPerson.getId());
		
		assertEquals("Kaio", persistedPerson.getFirstName());
		assertEquals("Eduardo Alves de Lima", persistedPerson.getLastName());
		assertEquals("Natal - RN", persistedPerson.getAdress());
		assertEquals("Male", persistedPerson.getGender());

		
	}
	
	@Test
	@Order(3)
	public void testDisablePersonById() throws JsonMappingException, JsonProcessingException {
		
		var persistedPerson = given().spec(specification)
				.config(
						RestAssuredConfig
							.config()
							.encoderConfig(EncoderConfig.encoderConfig()
								.encodeContentTypeAs(
									TestConfigs.CONTENT_TYPE_YML,
									ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
					.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_KAIO)
					.pathParam("id", person.getId())
				.when()
					.patch("{id}")
				.then()
					.statusCode(200)
						.extract()
							.body()
								.as(PersonVO.class, objectMapper);
		
		person = persistedPerson;
		
		assertNotNull(persistedPerson);
		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAdress());
		assertNotNull(persistedPerson.getGender());
		assertFalse(persistedPerson.getEnabled());
		
		assertEquals(person.getId(), persistedPerson.getId());
		
		assertEquals("Kaio", persistedPerson.getFirstName());
		assertEquals("Eduardo Alves de Lima", persistedPerson.getLastName());
		assertEquals("Natal - RN", persistedPerson.getAdress());
		assertEquals("Male", persistedPerson.getGender());

		
	}
	
	@Test
	@Order(4)
	public void testFindById() throws JsonMappingException, JsonProcessingException {
		mockPerson();
		
		var persistedPerson = given().spec(specification)
				.config(
						RestAssuredConfig
							.config()
							.encoderConfig(EncoderConfig.encoderConfig()
								.encodeContentTypeAs(
									TestConfigs.CONTENT_TYPE_YML,
									ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
					.pathParam("id", person.getId())
					.when()
					.get("{id}")
				.then()
					.statusCode(200)
						.extract()
						.body()
						.as(PersonVO.class, objectMapper);
		
		person = persistedPerson;
		
		assertNotNull(persistedPerson);
		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAdress());
		assertNotNull(persistedPerson.getGender());
		assertFalse(persistedPerson.getEnabled());
		
		assertTrue(persistedPerson.getId() > 0);
		
		assertEquals("Kaio", persistedPerson.getFirstName());
		assertEquals("Eduardo Alves de Lima", persistedPerson.getLastName());
		assertEquals("Natal - RN", persistedPerson.getAdress());
		assertEquals("Male", persistedPerson.getGender());
		
	}
	@Test
	@Order(5)
	public void testDelete() throws JsonMappingException, JsonProcessingException {
		
		given().spec(specification)
		.config(
			RestAssuredConfig
				.config()
				.encoderConfig(EncoderConfig.encoderConfig()
					.encodeContentTypeAs(
						TestConfigs.CONTENT_TYPE_YML,
						ContentType.TEXT)))
		.contentType(TestConfigs.CONTENT_TYPE_YML)
		.accept(TestConfigs.CONTENT_TYPE_YML)
			.pathParam("id", person.getId())
			.when()
			.delete("{id}")
		.then()
			.statusCode(204);
		
	}

	@Test
	@Order(6)
	public void testFindAll() throws JsonMappingException, JsonProcessingException {		
		
		var wrapper = given().spec(specification)
				.config(
						RestAssuredConfig
							.config()
							.encoderConfig(EncoderConfig.encoderConfig()
								.encodeContentTypeAs(
									TestConfigs.CONTENT_TYPE_YML,
									ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.queryParams("page", 2, "size", 6, "direction", "asc")
					.when()
					.get()
				.then()
					.statusCode(200)
						.extract()
						.body()
						.as(PagedModelPerson.class, objectMapper);
		
		var people = wrapper.getContent();
		
		PersonVO foundPersonOne = people.get(0);
		
		assertNotNull(foundPersonOne.getId());
		assertNotNull(foundPersonOne.getFirstName());
		assertNotNull(foundPersonOne.getLastName());
		assertNotNull(foundPersonOne.getAdress());
		assertNotNull(foundPersonOne.getGender());
		assertTrue(foundPersonOne.getEnabled());
		
		assertEquals(432, foundPersonOne.getId());
		
		assertEquals("Agathe", foundPersonOne.getFirstName());
		assertEquals("Elsey", foundPersonOne.getLastName());
		assertEquals("41 Anthes Park", foundPersonOne.getAdress());
		assertEquals("Female", foundPersonOne.getGender());
		
		
		PersonVO foundPersonFive = people.get(4);
		
		assertNotNull(foundPersonFive.getId());
		assertNotNull(foundPersonFive.getFirstName());
		assertNotNull(foundPersonFive.getLastName());
		assertNotNull(foundPersonFive.getAdress());
		assertNotNull(foundPersonFive.getGender());
		assertTrue(foundPersonFive.getEnabled());
		
		assertEquals(671, foundPersonFive.getId());
		
		assertEquals("Ailis", foundPersonFive.getFirstName());
		assertEquals("Linnock", foundPersonFive.getLastName());
		assertEquals("942 Mcguire Plaza", foundPersonFive.getAdress());
		assertEquals("Female", foundPersonFive.getGender());
		
	}
	@Test
	@Order(7)
	public void testFindByName() throws JsonMappingException, JsonProcessingException {		
		
		var wrapper = given().spec(specification)
				.config(
						RestAssuredConfig
						.config()
						.encoderConfig(EncoderConfig.encoderConfig()
								.encodeContentTypeAs(
										TestConfigs.CONTENT_TYPE_YML,
										ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.pathParam("firstName", "otis")
				.queryParams("page", 0, "size", 6, "direction", "asc")
				.when()
				.get("findPersonByName/{firstName}")
				.then()
				.statusCode(200)
				.extract()
				.body()
				.as(PagedModelPerson.class, objectMapper);
		
		var people = wrapper.getContent();
		
		PersonVO foundPersonOne = people.get(0);
		
		assertNotNull(foundPersonOne.getId());
		assertNotNull(foundPersonOne.getFirstName());
		assertNotNull(foundPersonOne.getLastName());
		assertNotNull(foundPersonOne.getAdress());
		assertNotNull(foundPersonOne.getGender());
		assertFalse(foundPersonOne.getEnabled());
		
		assertEquals(682, foundPersonOne.getId());
		
		assertEquals("Otis", foundPersonOne.getFirstName());
		assertEquals("Bassam", foundPersonOne.getLastName());
		assertEquals("56025 Dayton Parkway", foundPersonOne.getAdress());
		assertEquals("Male", foundPersonOne.getGender());
		
	}
	
	@Test
	@Order(8)
	public void testFindAllWitoutToken() throws JsonMappingException, JsonProcessingException {		
		
		RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
				.setBasePath("/api/person/v1")
				.setPort(TestConfigs.SERVER_PORT)
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		
			given().spec(specificationWithoutToken)
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.when()
				.get()
				.then()
				.statusCode(403);
		
	}
	@Test
	@Order(9)
	public void testHATEOAS() throws JsonMappingException, JsonProcessingException {		
		
		var unthreatedContent = given().spec(specification)
				.config(
						RestAssuredConfig
							.config()
							.encoderConfig(EncoderConfig.encoderConfig()
								.encodeContentTypeAs(
									TestConfigs.CONTENT_TYPE_YML,
									ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.queryParams("page", 2, "size", 6, "direction", "asc")
					.when()
					.get()
				.then()
					.statusCode(200)
						.extract()
						.body()
						.asString();
		
		var content = unthreatedContent.replace("\n", "").replace("\r", "");
		
		assertTrue(content.contains("rel: \"self\"    href: \"http://localhost:8081/api/person/v1/432\""));
		assertTrue(content.contains("rel: \"self\"    href: \"http://localhost:8081/api/person/v1/58\""));
		assertTrue(content.contains("rel: \"self\"    href: \"http://localhost:8081/api/person/v1/671\""));
		
		assertTrue(content.contains("rel: \"first\"  href: \"http://localhost:8081/api/person/v1?direction=asc&page=0&size=6&sort=firstName,asc\""));
		assertTrue(content.contains("rel: \"prev\"  href: \"http://localhost:8081/api/person/v1?direction=asc&page=1&size=6&sort=firstName,asc\""));
		assertTrue(content.contains("rel: \"self\"  href: \"http://localhost:8081/api/person/v1?page=2&size=6&direction=asc\""));
		assertTrue(content.contains("rel: \"next\"  href: \"http://localhost:8081/api/person/v1?direction=asc&page=3&size=6&sort=firstName,asc\""));
		assertTrue(content.contains("rel: \"last\"  href: \"http://localhost:8081/api/person/v1?direction=asc&page=167&size=6&sort=firstName,asc\""));
		
		assertTrue(content.contains("page:  size: 6  totalElements: 1008  totalPages: 168  number: 2"));
		
//		assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8081/api/person/v1/432\"}}}"));
//		assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8081/api/person/v1/58\"}}}"));
//		assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8081/api/person/v1/671\"}}}"));
//				
//		assertTrue(content.contains("\"last\":{\"href\":\"http://localhost:8081/api/person/v1?direction=asc&page=167&size=6&sort=firstName,asc\"}}"));
//		assertTrue(content.contains("\"next\":{\"href\":\"http://localhost:8081/api/person/v1?direction=asc&page=3&size=6&sort=firstName,asc\"}"));
//		assertTrue(content.contains("\"self\":{\"href\":\"http://localhost:8081/api/person/v1?page=2&size=6&direction=asc\"}"));
//		assertTrue(content.contains("\"prev\":{\"href\":\"http://localhost:8081/api/person/v1?direction=asc&page=1&size=6&sort=firstName,asc\"}"));
//		assertTrue(content.contains("{\"first\":{\"href\":\"http://localhost:8081/api/person/v1?direction=asc&page=0&size=6&sort=firstName,asc\"}"));
//		
//		assertTrue(content.contains("\"page\":{\"size\":6,\"totalElements\":1008,\"totalPages\":168,\"number\":2}}"));
		
	}
	

	private void mockPerson() {
		person.setFirstName("Kaio");
		person.setLastName("Eduardo");
		person.setAdress("Natal - RN");
		person.setGender("Male");
		person.setEnabled(true);
	}

}
