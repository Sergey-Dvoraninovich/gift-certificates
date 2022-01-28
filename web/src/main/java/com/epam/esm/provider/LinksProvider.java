package com.epam.esm.provider;

import org.springframework.hateoas.Link;

import java.util.List;

public interface LinksProvider<T> {
    List<Link> provide(T t);
}
