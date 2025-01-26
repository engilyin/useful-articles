/*
 Copyright 2022-2025 engilyin

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

import java.io.File;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Util {

    private static final DateTimeFormatter sqlTimestampFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.nnnnnx");

    public static String snakeToCamel(String str) {
        if (str == null || str.isBlank()) {
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

    // 2022-11-18 12:53:39.108318-08
    public static Instant instantFromString(String value) {
        if (value == null || value.isBlank()) {
            return null;
        } else {
            return Instant.from(sqlTimestampFormatter.parse(value, ZonedDateTime::from));
        }
    }

    public static String correctFileSeparator(String path) {
        if (File.pathSeparatorChar != '/') {
            return path.replace('/', '\\');
        }
        return path;
    }
}
