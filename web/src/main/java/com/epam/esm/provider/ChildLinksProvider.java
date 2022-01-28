package com.epam.esm.provider;

import org.springframework.hateoas.Link;

import java.util.List;

public interface ChildLinksProvider<K, T> {
    List<Link> provide(K k, T t);
}
