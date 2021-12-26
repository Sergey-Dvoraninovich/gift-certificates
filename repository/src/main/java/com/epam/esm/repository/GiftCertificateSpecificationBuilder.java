package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.List;

public class GiftCertificateSpecificationBuilder {
    private static final String NAME = "name";
    private static final String IS_AVAILABLE = "isAvailable";
    private static final String DESCRIPTION = "description";
    private static final String GIFT_CERTIFICATE_TAGS = "giftCertificateTags";

    private Specification<GiftCertificate> composedSpecification;

    public GiftCertificateSpecificationBuilder() {
        composedSpecification = Specification.where(null);
    }

    public Specification<GiftCertificate> build() {
        return composedSpecification;
    }

    public GiftCertificateSpecificationBuilder certificateName(String name) {
        if (name != null) {
            String partialName = "%" + name + "%";
            Specification<GiftCertificate> partialSpecification
                    = (certificateRoot, criteriaQuery, criteriaBuilder)
                    -> criteriaBuilder.like(certificateRoot.get(NAME), partialName);
            composedSpecification = composedSpecification.and(partialSpecification);
        }

        return this;
    }

    public GiftCertificateSpecificationBuilder certificateAvailability(Boolean isAvailable) {
        if (isAvailable != null) {
            Specification<GiftCertificate> partialSpecification
                    = (certificateRoot, criteriaQuery, criteriaBuilder)
                    -> criteriaBuilder.equal(certificateRoot.get(IS_AVAILABLE), isAvailable);
            composedSpecification = composedSpecification.and(partialSpecification);
        }

        return this;
    }

    public GiftCertificateSpecificationBuilder certificateDescription(String description) {
        if (description != null) {
            String partialDescription = "%" + description + "%";
            Specification<GiftCertificate> partialSpecification
                    = (certificateRoot, criteriaQuery, criteriaBuilder)
                    -> criteriaBuilder.like(certificateRoot.get(DESCRIPTION), partialDescription);
            composedSpecification = composedSpecification.and(partialSpecification);
        }

        return this;
    }

    public GiftCertificateSpecificationBuilder certificateTagNames(List<Tag> tags) {
        if (tags != null) {
            Specification<GiftCertificate> partialSpecification
                    = (certificateRoot, criteriaQuery, criteriaBuilder) -> {
                Predicate result;
                if (tags.size() != 0) {
                    result = criteriaBuilder.isMember(tags.get(0), certificateRoot.get(GIFT_CERTIFICATE_TAGS));
                }
                else {
                    result = criteriaBuilder.disjunction();
                }
                for (Tag tag : tags) {
                    Predicate memberPredicate = criteriaBuilder.isMember(tag, certificateRoot.get(GIFT_CERTIFICATE_TAGS));
                    result = criteriaBuilder.and(result, memberPredicate);
                }

                return result;
            };
            composedSpecification = composedSpecification.and(partialSpecification);
        }

        return this;
    }
}
