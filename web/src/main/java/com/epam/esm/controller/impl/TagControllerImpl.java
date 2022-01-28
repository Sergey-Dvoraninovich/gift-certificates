package com.epam.esm.controller.impl;

import com.epam.esm.controller.TagController;
import com.epam.esm.dto.PageDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.provider.PageModelProvider;
import com.epam.esm.provider.impl.TagLinksProvider;
import com.epam.esm.service.RequestService;
import com.epam.esm.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
public class TagControllerImpl implements TagController {

    private final TagService tagService;
    private final TagLinksProvider tagLinksProvider;

    private final RequestService requestService;
    private final PageModelProvider pageModelProvider;

    @Override
    public ResponseEntity<PagedModel<TagDto>> getAll(@RequestParam Map<String, Object> params) {

        long tagsAmount = tagService.countAll();
        PageDto pageDto = requestService.createPageDTO(params, tagsAmount);

        List<TagDto> tagsDto = tagService.findAll(pageDto);
        tagsDto.forEach(tagDto -> tagDto.add(tagLinksProvider.provide(tagDto)));

        PagedModel<TagDto> pagedModel = pageModelProvider.provide(this.getClass(),
                                                                  params, tagsDto,
                                                                  tagsAmount, pageDto);

        return new ResponseEntity<>(pagedModel, OK);
    }

    @Override
    public ResponseEntity<TagDto> getTag(@PathVariable("id") @Min(1) long id) {
        TagDto tagDto = tagService.findById(id);
        tagDto.add(tagLinksProvider.provide(tagDto));
        return new ResponseEntity<>(tagDto, OK);
    }

    @Override
    public ResponseEntity<TagDto> createTag(@RequestBody @NotNull TagDto tagDto) {
        TagDto createdTag = tagService.create(tagDto);
        createdTag.add(tagLinksProvider.provide(createdTag));
        return new ResponseEntity<>(createdTag, CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteTag(@PathVariable("id") @Min(1) long id) {
        tagService.delete(id);
        return new ResponseEntity<>(NO_CONTENT);
    }

    @Override
    public ResponseEntity<TagDto> getMostWidelyUsedTag() {
        TagDto tagDto = tagService.findMostWidelyUsedTag();
        tagDto.add(tagLinksProvider.provide(tagDto));
        return new ResponseEntity<>(tagDto, OK);
    }

}
