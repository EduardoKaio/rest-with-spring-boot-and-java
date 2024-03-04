package com.example.demo.integrationTests.vo.wrappers;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class WrapperBookVO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("_embedded")
	private BookEmbeddedVo embedded;

	@Override
	public int hashCode() {
		return Objects.hash(embedded);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WrapperBookVO other = (WrapperBookVO) obj;
		return Objects.equals(embedded, other.embedded);
	}

	public WrapperBookVO() {}

	public BookEmbeddedVo getEmbedded() {
		return embedded;
	}

	public void setEmbedded(BookEmbeddedVo embedded) {
		this.embedded = embedded;
	}
}
