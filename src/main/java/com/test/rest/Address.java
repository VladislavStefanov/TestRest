package com.test.rest;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(force = true)
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Address {
	@Getter
	private String street = "no-street";
	@Getter
	private String city = "no-city";
	
	public void setStreet(String street) {
		if (street == null) {
			this.street = "no-street";
			return;
		}
		this.street = street;
	}
	public void setCity(String city) {
		if (city == null) {
			this.city = "no-city";
			return;
		}
		this.city = city;
	}
	
	
}
