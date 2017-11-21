package com.cpf.fulltextsearch.contract;

import com.github.macdao.moscow.http.RestExecutor;
import com.github.macdao.moscow.http.RestResponse;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.PathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URI;
import java.nio.file.Path;
import java.util.Map;

public class RestTemplateExecutor implements RestExecutor {

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    public RestResponse execute(String method, URI uri, Map<String, String> headers, Object body) {
        final HttpMethod httpMethod = HttpMethod.valueOf(method.toUpperCase());
        HttpEntity<Object> objectHttpEntity = new HttpEntity<>(body(body), headers(headers));
        final ResponseEntity<String> responseEntity = restTemplate.exchange(uri, httpMethod, objectHttpEntity, String.class);
        return new RestResponse(responseEntity.getStatusCode().value(), responseEntity.getHeaders().toSingleValueMap(), responseEntity.getBody());
    }

    private Object body(Object body) {
        if (body instanceof Path) {
            return new PathResource((Path) body);
        }
        return body;
    }

    private MultiValueMap<String, String> headers(Map<String, String> headers) {
        final LinkedMultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJnb2xkQXBwIiwicm9sZXMiOiJhcHB1c2VyIiwiaWF0IjoxNTA4OTIwNTQ3fQ.fLL-MCizUuzT-yF7DfRD-3Idf8KfyKdxVjKl8tYjv8Q");
        multiValueMap.setAll(headers);
        return multiValueMap;
    }

}
