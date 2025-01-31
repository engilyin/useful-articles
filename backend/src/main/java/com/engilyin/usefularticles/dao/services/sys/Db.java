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
package com.engilyin.usefularticles.dao.services.sys;

import com.engilyin.usefularticles.util.Util;
import io.r2dbc.spi.Row;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Db {

    public static Map<String, String> rowToMap(Row row) {
        return row.getMetadata().getColumnMetadatas().stream()
                .collect(Collectors.toMap(col -> Util.snakeToCamel(col.getName()), col -> Optional.ofNullable(
                                row.get(col.getName(), String.class))
                        .orElse("")));
    }
}
