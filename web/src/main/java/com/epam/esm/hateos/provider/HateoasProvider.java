package com.epam.esm.hateos.provider;

import org.springframework.hateoas.Link;

import java.util.List;

public interface HateoasProvider<T> {
    List<Link> provide(T t);
}
