package com.epam.esm.controller;

import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface PagedItemsController<T> {
    ResponseEntity<PagedModel<T>> getAllItems(long id, Map<String, Object> params);
}
