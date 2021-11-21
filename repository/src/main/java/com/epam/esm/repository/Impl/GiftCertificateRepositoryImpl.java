package com.epam.esm.repository.Impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderingType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.repository.OrderingType.ASC;
import static com.epam.esm.repository.OrderingType.DESC;

@Repository
@RequiredArgsConstructor
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {
    private static final String GIFT_CERTIFICATE_TAGS = "giftCertificateTags";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String CREATE_DATE = "createDate";

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Long countAll(List<String> tagNames, String certificateName, OrderingType orderingName,
                         String certificateDescription, OrderingType orderingCreateDate) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<GiftCertificate> certificateRoot = query.from(GiftCertificate.class);
        query.select(criteriaBuilder.count(query.from(GiftCertificate.class)));
        Join<GiftCertificate, Tag> tags = certificateRoot.join(GIFT_CERTIFICATE_TAGS);

        query.select(criteriaBuilder.count(query.from(GiftCertificate.class)));

        List<Predicate> predicates = new ArrayList<>();
        Optional<Predicate> tagsOptionalPredicate = createTagsPredicate(criteriaBuilder, tags, tagNames);
        tagsOptionalPredicate.ifPresent(predicates::add);

        predicates.addAll(createCertificatePredicate(criteriaBuilder, certificateRoot,
                certificateName, certificateDescription));
        if (predicates.size() != 0) {
            Predicate resultPredicate = predicates.get(0);
            for (Predicate predicate : predicates) {
                resultPredicate = criteriaBuilder.and(resultPredicate, predicate);
            }
            query.where(resultPredicate);
        }

        List<Order> orders = createOrderingPredicate(criteriaBuilder, certificateRoot,
                orderingName, orderingCreateDate);
        for (Order order: orders) {
            query.orderBy(order);
        }
        query.groupBy(certificateRoot.get(ID));

        return (long) entityManager.createQuery(query).getResultList().size();
    }

    @Override
    public List<GiftCertificate> findAll(List<String> tagNames, String certificateName, OrderingType orderingName,
                                         String certificateDescription, OrderingType orderingCreateDate,
                                         int pageNumber, int pageSize) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> query = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> certificateRoot = query.from(GiftCertificate.class);
        Join<GiftCertificate, Tag> tags = certificateRoot.join(GIFT_CERTIFICATE_TAGS);

        query.select(certificateRoot);

        List<Predicate> predicates = new ArrayList<>();
        Optional<Predicate> tagsOptionalPredicate = createTagsPredicate(criteriaBuilder, tags, tagNames);
        tagsOptionalPredicate.ifPresent(predicates::add);

        predicates.addAll(createCertificatePredicate(criteriaBuilder, certificateRoot,
                certificateName, certificateDescription));
        if (predicates.size() != 0) {
            Predicate resultPredicate = predicates.get(0);
            for (Predicate predicate : predicates) {
                resultPredicate = criteriaBuilder.and(resultPredicate, predicate);
            }
            query.where(resultPredicate);
        }

        List<Order> orders = createOrderingPredicate(criteriaBuilder, certificateRoot,
                orderingName, orderingCreateDate);
        for (Order order: orders) {
            query.orderBy(order);
        }
        query.groupBy(certificateRoot.get(ID));

        return entityManager.createQuery(query)
                .setFirstResult((pageNumber - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public Optional<GiftCertificate> findById(long id) {
        GiftCertificate giftCertificate = entityManager.find(GiftCertificate.class, id);
        return Optional.ofNullable(giftCertificate);
    }

    @Override
    public Optional<GiftCertificate> findByName(String name) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> query = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> certificateRoot = query.from(GiftCertificate.class);

        query.select(certificateRoot);
        query.where(criteriaBuilder.equal(certificateRoot.get(NAME), name));

        List<GiftCertificate> certificates = entityManager.createQuery(query).getResultList();

        return certificates.size() == 0
                ? Optional.empty()
                : Optional.of(certificates.get(0));
    }

    @Override
    public GiftCertificate create(GiftCertificate certificate) {
        certificate.setCreateDate(Instant.now());
        certificate.setLastUpdateDate(Instant.now());

        entityManager.persist(certificate);
        return certificate;
    }

    @Override
    public GiftCertificate update(GiftCertificate certificate) {
        certificate.setLastUpdateDate(Instant.now());
        entityManager.merge(certificate);
        return certificate;
    }

    @Override
    public boolean delete(GiftCertificate certificate) {
        entityManager.remove(certificate);
        return entityManager.find(Tag.class, certificate.getId()) == null;
    }

    private Optional<Predicate> createTagsPredicate(CriteriaBuilder criteriaBuilder, Join<GiftCertificate,Tag> tags,
                                                    List<String> tagNames) {
        Predicate resultPredicate = null;
        if (tagNames != null){
            if (tagNames.size() != 0) {
                List<Predicate> tagPredicates = new ArrayList<>();
                for (String tagName: tagNames) {
                    tagPredicates.add(criteriaBuilder.like(tags.get(NAME), "%" + tagName + "%"));
                }
                resultPredicate = tagPredicates.get(0);
                for (Predicate predicate: tagPredicates) {
                    resultPredicate = criteriaBuilder.and(resultPredicate, predicate);
                }
            }
        }
        return Optional.ofNullable(resultPredicate);
    }

    private List<Predicate> createCertificatePredicate(CriteriaBuilder criteriaBuilder, Root<GiftCertificate> certificateRoot,
                                                       String certificateName, String certificateDescription) {
        List<Predicate> predicates = new ArrayList<>();
        if (certificateName != null){
            predicates.add(criteriaBuilder.like(certificateRoot.get(NAME), "%" + certificateName + "%"));
        }
        if (certificateDescription != null){
            predicates.add(criteriaBuilder.like(certificateRoot.get(DESCRIPTION), "%" + certificateDescription + "%"));
        }
        return predicates;
    }

    private List<Order> createOrderingPredicate(CriteriaBuilder criteriaBuilder, Root<GiftCertificate> certificateRoot,
                                                    OrderingType orderingName, OrderingType orderingCreateDate) {
        List<Order> orders = new ArrayList<>();
        if (orderingName != null){
            if (orderingName.equals(ASC)) {
                orders.add(criteriaBuilder.asc(certificateRoot.get(NAME)));
            }
            if (orderingName.equals(DESC)) {
                orders.add(criteriaBuilder.desc(certificateRoot.get(NAME)));
            }
        }
        if (orderingCreateDate != null){
            if (orderingCreateDate.equals(ASC)) {
                orders.add(criteriaBuilder.asc(certificateRoot.get(CREATE_DATE)));
            }
            if (orderingCreateDate.equals(DESC)) {
                orders.add(criteriaBuilder.desc(certificateRoot.get(CREATE_DATE)));
            }
        }
        return orders;
    }
}
