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
package com.engilyin.usefularticles.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AppPropertiesLoader {

    public InputStream getResource(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        return classLoader.getResourceAsStream(fileName);
    }

    public Properties properties(String propertiesName) {

        Properties properties = new Properties();
        try {
            properties.load(getResource(propertiesName));
        } catch (IOException e) {
            log.error("Please supply the valid properties {}", propertiesName);

        }

        return properties;
    }

}