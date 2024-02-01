package com.example.demo.data.vo.v2;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


public class PersonVOV2 implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	public long getId() {
		return id;
	}

	private long id;
	private String firstName;
	private String lastName;
	private String adress;
	private String gender;
	private Date birthDay;
	
	public PersonVOV2() {
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public Date getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}

	@Override
	public int hashCode() {
		return Objects.hash(adress, birthDay, firstName, gender, id, lastName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersonVOV2 other = (PersonVOV2) obj;
		return Objects.equals(adress, other.adress) && Objects.equals(birthDay, other.birthDay)
				&& Objects.equals(firstName, other.firstName) && Objects.equals(gender, other.gender) && id == other.id
				&& Objects.equals(lastName, other.lastName);
	}


	
}
