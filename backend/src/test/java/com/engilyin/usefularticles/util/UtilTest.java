package com.engilyin.usefularticles.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat; 

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class UtilTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void test() {
		assertThat(Util.snakeToCamel(null), nullValue());
		assertThat(Util.snakeToCamel(""), equalTo(""));
		assertThat(Util.snakeToCamel("hello"), equalTo("hello"));
		assertThat(Util.snakeToCamel("hello_there"), equalTo("helloThere"));
		assertThat(Util.snakeToCamel("_hello_guys"), equalTo("HelloGuys"));
		assertThat(Util.snakeToCamel("_hello__folks"), equalTo("HelloFolks"));
	}

}
