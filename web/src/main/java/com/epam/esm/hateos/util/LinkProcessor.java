package com.epam.esm.hateos.util;

import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

public class LinkProcessor {
    public static String process(WebMvcLinkBuilder linkBuilder){
        StringBuilder stringBuilder = new StringBuilder(linkBuilder.toString());
        int pos = stringBuilder.indexOf("{");
        return stringBuilder.substring(0, pos);
    }
}
