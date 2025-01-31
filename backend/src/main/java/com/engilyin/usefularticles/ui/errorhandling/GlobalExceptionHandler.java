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
package com.engilyin.usefularticles.ui.errorhandling;

import java.util.Map;
import org.springframework.boot.autoconfigure.web.WebProperties.Resources;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/*
 * We can try simply override and customize ErrorAttributes. However it would be harder to mock error handler beans at integration testing.
 */
@Component
@Order(-2)
public class GlobalExceptionHandler extends AbstractErrorWebExceptionHandler {

    public GlobalExceptionHandler(
            ErrorAttributes errorAttributes,
            ApplicationContext applicationContext,
            ServerCodecConfigurer codecConfigurer) {
        super(errorAttributes, new Resources(), applicationContext);
        this.setMessageReaders(codecConfigurer.getReaders());
        this.setMessageWriters(codecConfigurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Map<String, Object> error = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));
        return ServerResponse.status(getHttpStatus(error))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(error));
    }

    /**
     * Get the HTTP error status information from the error map.
     *
     * @param errorAttributes the current error information
     * @return the error HTTP status
     */
    protected int getHttpStatus(Map<String, Object> errorAttributes) {
        return (int) errorAttributes.get("status");
    }

    protected ErrorAttributeOptions getErrorAttributeOptions(ServerRequest request, MediaType mediaType) {
        return ErrorAttributeOptions.defaults()
                .including(Include.MESSAGE)
                .including(Include.BINDING_ERRORS)
                .including(Include.EXCEPTION);
    }
}
