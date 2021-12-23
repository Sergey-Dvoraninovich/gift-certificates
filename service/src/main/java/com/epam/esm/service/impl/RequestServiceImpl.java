package com.epam.esm.service.impl;

import com.epam.esm.dto.GiftCertificateFilterDto;
import com.epam.esm.dto.PageDto;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.exception.InvalidPaginationException;
import com.epam.esm.repository.OrderingType;
import com.epam.esm.service.RequestService;
import com.epam.esm.validator.GiftCertificateSearchParamsValidator;
import com.epam.esm.validator.PaginationValidator;
import com.epam.esm.validator.ValidationError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.epam.esm.validator.ValidationError.PAGE_IS_OUT_OF_RANGE;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private static final String DELIMITER = ",";

    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String TAG_NAMES = "tagNames";
    private static final String ORDERING_NAME = "orderingName";
    private static final String ORDERING_CREATE_DATE = "orderingCreateDate";

    private static final String PAGE_NUMBER = "pageNumber";
    private static final String PAGE_SIZE = "pageSize";
    private static final int DEFAULT_PAGE_SIZE = 10;

    private final PaginationValidator paginationValidator;
    private final GiftCertificateSearchParamsValidator searchParamsValidator;

    @Override
    public PageDto createPageDTO(Map<String, Object> params, Long totalAmount) {
        String pageNumberParam = (String) params.get(PAGE_NUMBER);
        String pageSizeParam = (String) params.get(PAGE_SIZE);

        List<ValidationError> validationErrors = paginationValidator.validateParams(pageNumberParam, pageSizeParam);
        if (!validationErrors.isEmpty()) {
            throw new InvalidPaginationException(pageNumberParam, pageSizeParam, validationErrors);
        }

        int pageNumber = pageNumberParam == null ? 1 : Integer.parseInt(pageNumberParam);
        int pageSize = pageSizeParam == null ? DEFAULT_PAGE_SIZE : Integer.parseInt(pageSizeParam);

        if (totalAmount <= (long) (pageNumber - 1) * pageSize) {
            throw new InvalidPaginationException(pageNumberParam, pageSizeParam,
                    Collections.singletonList(PAGE_IS_OUT_OF_RANGE));
        }

        return PageDto.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .build();
    }

    @Override
    public GiftCertificateFilterDto createGiftCertificateFilterDTO(Map<String, Object> params) {
        String certificateNameParam = (String) params.get(NAME);
        String certificateDescriptionParam = (String) params.get(DESCRIPTION);

        String certificateTagNamesParam = (String) params.get(TAG_NAMES);
        List<String> tagNames;
        tagNames = certificateTagNamesParam == null ? null : List.of(certificateTagNamesParam.split(DELIMITER));

        String certificateNameOrderingParam = (String) params.get(ORDERING_NAME);
        String certificateCreateDateOrderingParam = (String) params.get(ORDERING_CREATE_DATE);

        List<ValidationError> validationErrors = searchParamsValidator.validate(tagNames, certificateNameParam, certificateNameOrderingParam,
                certificateDescriptionParam, certificateCreateDateOrderingParam);
        if (!validationErrors.isEmpty()) {
            throw new InvalidEntityException(validationErrors, String.class);
        }

        OrderingType nameOrdering = certificateNameOrderingParam == null
                ? null
                : OrderingType.valueOf(certificateNameOrderingParam);

        OrderingType createDateOrdering = certificateCreateDateOrderingParam == null
                ? null
                : OrderingType.valueOf(certificateCreateDateOrderingParam);

        return GiftCertificateFilterDto.builder()
                .name(certificateNameParam)
                .description(certificateDescriptionParam)
                .tagNames(tagNames)
                .orderingName(nameOrdering)
                .orderingCreateDate(createDateOrdering)
                .build();
    }
}
