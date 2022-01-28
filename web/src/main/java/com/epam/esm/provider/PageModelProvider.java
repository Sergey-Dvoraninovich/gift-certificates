package com.epam.esm.provider;

import com.epam.esm.controller.PagedController;
import com.epam.esm.controller.PagedItemsController;
import com.epam.esm.dto.PageDto;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PageModelProvider {

    private static final String FIRST_PAGE_REL = "firstPage";
    private static final String PREV_PAGE_REL = "prevPage";
    private static final String NEXT_PAGE_REL = "nextPage";
    private static final String LAST_PAGE_REL = "lastPage";

    private static final String PAGE_NUMBER_PARAM = "pageNumber";

    public <D, C extends PagedController<D>> PagedModel<D> provide(Class<C> controllerClass,
                                                                   Map<String, Object> pageParams, List<D> dtoList,
                                                                   long totalAmount, PageDto pageDto) {

        PagedModel.PageMetadata pageMetadata;
        pageMetadata= new PagedModel.PageMetadata(pageDto.getPageSize(), pageDto.getPageNumber(), totalAmount);
        PagedModel<D> pagedModel = PagedModel.of(dtoList, pageMetadata);

        List<Link> links = provideLinks(controllerClass, pageParams, totalAmount, pageDto);
        pagedModel.add(links);

        return pagedModel;
    }

    public <D, C extends PagedItemsController<D>> PagedModel<D> provide(Class<C> controllerClass, long id,
                                                                        Map<String, Object> pageParams, List<D> dtoList,
                                                                        long totalAmount, PageDto pageDto) {

        PagedModel.PageMetadata pageMetadata;
        pageMetadata= new PagedModel.PageMetadata(pageDto.getPageSize(), pageDto.getPageNumber(), totalAmount);
        PagedModel<D> pagedModel = PagedModel.of(dtoList, pageMetadata);

        List<Link> links = provideLinks(controllerClass, id,
                pageParams, totalAmount, pageDto);
        pagedModel.add(links);

        return pagedModel;
    }

    private  <D, C extends PagedController<D>> List<Link> provideLinks(Class<C> controllerClass,
                                                                       Map<String, Object> pageParams,
                                                                       long totalAmount, PageDto pageDto) {
        Map<String, Object> params = new HashMap<>(pageParams);
        List<Link> links = new ArrayList<>();

        params.put(PAGE_NUMBER_PARAM, 1);
        Link firstLink = linkTo(methodOn(controllerClass).getAll(params)).withRel(FIRST_PAGE_REL);
        links.add(firstLink);

        if (pageDto.getPageNumber() > 1) {
            params.put(PAGE_NUMBER_PARAM, pageDto.getPageNumber() - 1);
            Link prevLink = linkTo(methodOn(controllerClass).getAll(params)).withRel(PREV_PAGE_REL);
            links.add(prevLink);
        }

        params.put(PAGE_NUMBER_PARAM, pageDto.getPageNumber());
        Link selfLink = linkTo(methodOn(controllerClass).getAll(params)).withSelfRel();
        links.add(selfLink);

        if (totalAmount > (long) pageDto.getPageNumber() * pageDto.getPageSize()) {
            params.put(PAGE_NUMBER_PARAM, pageDto.getPageNumber() + 1);
            Link nextLink = linkTo(methodOn(controllerClass).getAll(params)).withRel(NEXT_PAGE_REL);
            links.add(nextLink);
        }

        int lastPageNum = (int) Math.ceil((double) totalAmount / pageDto.getPageSize());
        params.put(PAGE_NUMBER_PARAM, lastPageNum);
        Link lastLink = linkTo(methodOn(controllerClass).getAll(params)).withRel(LAST_PAGE_REL);
        links.add(lastLink);

        return links;
    }

    private  <D, C extends PagedItemsController<D>> List<Link> provideLinks(Class<C> controllerClass, long id,
                                                                            Map<String, Object> pageParams,
                                                                            long totalAmount, PageDto pageDto) {
        Map<String, Object> params = new HashMap<>(pageParams);
        List<Link> links = new ArrayList<>();

        params.put(PAGE_NUMBER_PARAM, 1);
        Link firstLink = linkTo(methodOn(controllerClass).getAllItems(id, params)).withRel(FIRST_PAGE_REL);
        links.add(firstLink);

        if (pageDto.getPageNumber() > 1) {
            params.put(PAGE_NUMBER_PARAM, pageDto.getPageNumber() - 1);
            Link prevLink = linkTo(methodOn(controllerClass).getAllItems(id, params)).withRel(PREV_PAGE_REL);
            links.add(prevLink);
        }

        params.put(PAGE_NUMBER_PARAM, pageDto.getPageNumber());
        Link selfLink = linkTo(methodOn(controllerClass).getAllItems(id, params)).withSelfRel();
        links.add(selfLink);

        if (totalAmount > (long) pageDto.getPageNumber() * pageDto.getPageSize()) {
            params.put(PAGE_NUMBER_PARAM, pageDto.getPageNumber() + 1);
            Link nextLink = linkTo(methodOn(controllerClass).getAllItems(id, params)).withRel(NEXT_PAGE_REL);
            links.add(nextLink);
        }

        int lastPageNum = (int) Math.ceil((double) totalAmount / pageDto.getPageSize());
        params.put(PAGE_NUMBER_PARAM, lastPageNum);
        Link lastLink = linkTo(methodOn(controllerClass).getAllItems(id, params)).withRel(LAST_PAGE_REL);
        links.add(lastLink);

        return links;
    }
}
