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
package com.engilyin.usefularticles.ui.validation;

import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.stereotype.Component;

import com.engilyin.usefularticles.ui.errorhandling.ObjectValidationException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ObjectValidator {

    private final Validator validator;

    public <T> T validate(T object) {
        var errors = validator.validate(object);
        if (errors.isEmpty()) {
            return object;
        } else {
            String errorDetails = errors.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            throw new ObjectValidationException(errorDetails);
        }
    }
}
