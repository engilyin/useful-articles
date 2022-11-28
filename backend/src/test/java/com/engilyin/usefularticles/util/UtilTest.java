/*
 Copyright 2022 engilyin

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

      https://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
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

    @Test
    void testInstantParse() {
        assertThat(Util.instantFromString("2022-11-18 12:53:39.108318-08").getNano(), equalTo(108318));
        assertThat(Util.instantFromString("2022-11-28 14:05:49.85091-08").getNano(), equalTo(85091));
    }
    
   
}
