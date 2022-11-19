package com.engilyin.usefularticles.util;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Util {
	
	private static final DateTimeFormatter sqlTimestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.nnnnnnx");

	public static String snakeToCamel(String str) {
		if(str == null || str.isBlank()) {
			return str;
		}
		StringBuilder result = new StringBuilder();
		boolean wordBeginning = false;
		for (int i = 0; i < str.length(); i++) {
			char current = str.charAt(i);
			if (current == '_') {
				wordBeginning = true;
			} else {
				if (wordBeginning) {
					current = Character.toUpperCase(current);
					wordBeginning = false;
				}
				result.append(current);
			}
		}
		return result.toString();
	}
	
	
	//2022-11-18 12:53:39.108318-08
	public static Instant instantFromString(String value) {
		if(value == null || value.isBlank()) {
			return null;
		} else {
			return Instant.from(sqlTimestampFormatter.parse(value, ZonedDateTime::from));
		}
	}
}
