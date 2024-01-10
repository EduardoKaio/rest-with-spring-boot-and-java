package com.example.demo.excepetions;

import java.io.Serializable;
import java.util.Date;

public class ExceptionResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date timestampDate;
	private String message;
	private String details;
	
	public ExceptionResponse(Date timestampDate, String message, String details) {
		this.timestampDate = timestampDate;
		this.message = message;
		this.details = details;
	}

	public Date getTimestampDate() {
		return timestampDate;
	}

	public String getMessage() {
		return message;
	}

	public String getDetails() {
		return details;
	}
	
}
