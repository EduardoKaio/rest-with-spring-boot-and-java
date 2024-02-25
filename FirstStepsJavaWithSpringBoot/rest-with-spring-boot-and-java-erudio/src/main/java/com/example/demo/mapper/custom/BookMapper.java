//package com.example.demo.mapper.custom;
//
//import java.util.Date;
//
//import org.springframework.stereotype.Service;
//
//import com.example.demo.data.vo.v1.BookVO;
//import com.example.demo.data.vo.v2.PersonVOV2;
//import com.example.demo.model.Book;
//import com.example.demo.model.Person;
//
//@Service
//public class BookMapper {
//
//	public BookVO convertEntityToVo(Book book) {
//		BookVO vo = new BookVO();
//		
//		vo.setKey(book.getId());
//		vo.setAuthor(book.getAuthor());
//		vo.setLaunchDate(new Date());
//		vo.setPrice(book.getPrice());
//		vo.setTitle(book.getTitle());
//		
//		return vo;
//	}
//	
//	public Book convertVoToEntity(BookVO book) {
//		Book entity = new Book();
//		
//		entity.setId(book.getKey());
//		entity.setAuthor(book.getAuthor());
//		entity.setLaunchDate(new Date());
//		entity.setPrice(book.getPrice());
//		entity.setTitle(book.getTitle());
//		
//		return entity;
//	}
//}
