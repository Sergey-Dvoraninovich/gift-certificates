package com.epam.esm.controller;

import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface PagedController<T> {
    ResponseEntity<PagedModel<T>> getAll(Map<String, Object> params);
}
