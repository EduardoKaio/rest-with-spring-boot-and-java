package com.example.demo.integrationtests.controller.withyaml;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.configs.TestConfigs;
import com.example.demo.integrationTests.vo.AccountCredentialsVO;
import com.example.demo.integrationTests.vo.BookVO;
import com.example.demo.integrationTests.vo.PersonVO;
import com.example.demo.integrationTests.vo.TokenVO;
import com.example.demo.integrationtests.controller.withyaml.mapper.YAMLMapper;
import com.example.demo.integrationtests.testcontainers.AbstractIntegrationTest;
import com.example.demo.model.Book;
import com.example.demo.unittests.mapper.mocks.MockBook;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class BookControllerYamlTest extends AbstractIntegrationTest{

	private static RequestSpecification specification;
	private static YAMLMapper objectMapper;
	
	private static BookVO book;
	
	@BeforeAll
	public static void setup() {
		objectMapper = new YAMLMapper();		
		book = new BookVO();
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
				.setBasePath("/api/book/v1")
				.setPort(TestConfigs.SERVER_PORT)
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();

	}
	
	@Test
	@Order(1)
	public void testCreate() throws JsonMappingException, JsonProcessingException {
		mockBook();
		
		var persistedBook = given().spec(specification).config(RestAssuredConfig.config()
				.encoderConfig(EncoderConfig.encoderConfig()
						.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
					.body(book, objectMapper)
					.when()
					.post()
				.then()
					.statusCode(200)
						.extract()
							.body()
								.as(BookVO.class, objectMapper);
		
		book = persistedBook;
		
		assertNotNull(persistedBook);
		assertNotNull(persistedBook.getId());
		assertNotNull(persistedBook.getAuthor());
		assertNotNull(persistedBook.getLaunchDate());
		assertNotNull(persistedBook.getPrice());
		assertNotNull(persistedBook.getTitle());
		
		assertTrue(persistedBook.getId() > 0);
		
		assertEquals("Robert C. Martin", persistedBook.getAuthor());
//		assertEquals(new Date(), persistedBook.getLaunchDate());
		assertEquals(77.00, persistedBook.getPrice());
		assertEquals("Clean Code", persistedBook.getTitle());

		
	}
	@Test
	@Order(2)
	public void testUpdate() throws JsonMappingException, JsonProcessingException {
		book.setTitle("Clean Code X");;
		
		var persistedBook = given().spec(specification).config(RestAssuredConfig.config()
				.encoderConfig(EncoderConfig.encoderConfig()
						.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
					.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_KAIO)
					.body(book, objectMapper)
				.when()
					.put()
				.then()
					.statusCode(200)
						.extract()
							.body()
							.as(BookVO.class, objectMapper);
		
		book = persistedBook;
		
		assertNotNull(persistedBook);
		assertNotNull(persistedBook.getAuthor());
		assertNotNull(persistedBook.getLaunchDate());
		assertNotNull(persistedBook.getPrice());
		assertNotNull(persistedBook.getTitle());
		
		assertEquals(book.getId(), persistedBook.getId());
		
		assertEquals("Robert C. Martin", persistedBook.getAuthor());
//		assertEquals(new Date(), persistedBook.getLaunchDate());
		assertEquals(77.00, persistedBook.getPrice());
		assertEquals("Clean Code X", persistedBook.getTitle());

		
	}
	
	
	@Test
	@Order(3)
	public void testFindById() throws JsonMappingException, JsonProcessingException {
		mockBook();
		
		var persistedBook = given().spec(specification).config(RestAssuredConfig.config()
				.encoderConfig(EncoderConfig.encoderConfig()
						.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
					.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_KAIO)
					.pathParam("id", book.getId())
				.when()
					.get("{id}")
				.then()
					.statusCode(200)
						.extract()
							.body()
							.as(BookVO.class, objectMapper);
		
		book = persistedBook;
		
		assertNotNull(persistedBook);
		assertNotNull(persistedBook.getId());
		assertNotNull(persistedBook.getAuthor());
		assertNotNull(persistedBook.getLaunchDate());
		assertNotNull(persistedBook.getPrice());
		assertNotNull(persistedBook.getTitle());
		
		assertEquals(book.getId(), persistedBook.getId());
		
		assertEquals("Robert C. Martin", persistedBook.getAuthor());
//		assertEquals(new Date(), persistedBook.getLaunchDate());
		assertEquals(77.00, persistedBook.getPrice());
		assertEquals("Clean Code X", persistedBook.getTitle());

		
	}
	

	
	@Test
	@Order(4)
	public void testDelete() throws JsonMappingException, JsonProcessingException {
		
		given().spec(specification).config(RestAssuredConfig.config()
				.encoderConfig(EncoderConfig.encoderConfig()
						.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.pathParam("id", book.getId())
				.when()
				.delete("{id}")
				.then()
				.statusCode(204);
		
	}

	@Test
	@Order(5)
	public void testFindAll() throws JsonMappingException, JsonProcessingException {		
		
		var content = given().spec(specification).config(RestAssuredConfig.config()
				.encoderConfig(EncoderConfig.encoderConfig()
						.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.when()
				.get()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.as(BookVO[].class, objectMapper);
		
		List<BookVO> books = Arrays.asList(content);
		
		BookVO foundBookOne = books.get(0);
		
		assertNotNull(foundBookOne.getId());
		assertNotNull(foundBookOne.getAuthor());
		assertNotNull(foundBookOne.getLaunchDate());
		assertNotNull(foundBookOne.getPrice());
		assertNotNull(foundBookOne.getTitle());
		
		assertEquals(1, foundBookOne.getId());
		
		assertEquals("Michael C. Feathers", foundBookOne.getAuthor());
//		assertEquals("", foundBookOne.getLaunchDate());
		assertEquals(49.00, foundBookOne.getPrice());
		assertEquals("Working effectively with legacy code", foundBookOne.getTitle());
		
		BookVO foundBookFive = books.get(4);
		
		assertNotNull(foundBookFive.getId());
		assertNotNull(foundBookFive.getAuthor());
		assertNotNull(foundBookFive.getLaunchDate());
		assertNotNull(foundBookFive.getPrice());
		assertNotNull(foundBookFive.getTitle());
		
		assertEquals(5, foundBookFive.getId());
		
		assertEquals("Steve McConnell", foundBookFive.getAuthor());
//		assertEquals("sales", foundBookFive.getLaunchDate());
		assertEquals(58.00, foundBookFive.getPrice());
		assertEquals("Code complete", foundBookFive.getTitle());
		
	}
	
	@Test
	@Order(6)
	public void testFindAllWitoutToken() throws JsonMappingException, JsonProcessingException {		
		
		RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
				.setBasePath("/api/book/v1")
				.setPort(TestConfigs.SERVER_PORT)
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		
			given().spec(specificationWithoutToken)
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.when()
				.get()
				.then()
				.statusCode(403);
		
	}
	
	private void mockBook() {
		book.setAuthor("Robert C. Martin");
		book.setLaunchDate(new Date());;
		book.setPrice(77.00);
		book.setTitle("Clean Code");
		
	}

}
