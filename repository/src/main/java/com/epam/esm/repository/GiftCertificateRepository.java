package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface Gift certificate repository.
 */
@Repository
public interface GiftCertificateRepository extends PagingAndSortingRepository<GiftCertificate, Long>,
        JpaSpecificationExecutor<GiftCertificate> {

    /**
     * Count Gift Certificate amount.
     *
     * @param specification the specification
     * @return the long amount of Gift Certificates
     */
    long count(Specification<GiftCertificate> specification);

    /**
     * Find all Gift Certificates.
     *
     * @param specification the specification
     * @param pageable the Pageable
     * @return the list of Gift Certificates
     */
    Page<GiftCertificate> findAll(Specification<GiftCertificate> specification, Pageable pageable);

    /**
     * Find Gift Certificate by id.
     *
     * @param id the Gift Certificate id
     * @return the optional Gift Certificate
     */
    Optional<GiftCertificate> findById(long id);

    /**
     * Find Gift Certificate by name.
     *
     * @param name the Gift Certificate name
     * @return the optional Gift Certificate
     */
    Optional<GiftCertificate> findByName(String name);


    /**
     * Create or update Gift Certificate.
     *
     * @param certificate the Gift Certificate
     * @return the created Gift Certificate
     */
    GiftCertificate save(GiftCertificate certificate);

    /**
     * Delete Gift Certificate.
     *
     * @param certificate the Gift Certificate
     */
    void delete(GiftCertificate certificate);
}
