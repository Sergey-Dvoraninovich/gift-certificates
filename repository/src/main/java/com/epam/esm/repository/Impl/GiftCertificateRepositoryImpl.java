package com.epam.esm.repository.Impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderingType;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {
    private static final String ID_PARAM = "id";
    private static final String NAME_PARAM = "name";
    private static final String DESCRIPTION_PARAM = "description";
    private static final String PRICE_PARAM = "price";
    private static final String DURATION_PARAM = "duration";

    private static final String TAG_NAME_PARAM = "tagName";
    private static final String ID_GIFT_CERTIFICATE_PARAM = "idGiftCertificate";
    private static final String ID_TAG_PARAM = "idTag";
    private static final String CERTIFICATE_NAME_PARAM = "certificateName";
    private static final String CERTIFICATE_DESCRIPTION_PARAM = "certificateDescription";

    private static final String PARTIAL_REQUEST_BEGINNING
            = "SELECT gift_certificates.id, gift_certificates.name, gift_certificates.description, gift_certificates.price, "
            +        "gift_certificates.duration, gift_certificates.create_date, gift_certificates.last_update_date "
            + "FROM gift_certificates ";

    private static final String PARTIAL_REQUEST_TAGS_JOIN
            = "SELECT gift_certificates.id, gift_certificates.name, gift_certificates.description, gift_certificates.price, "
            +        "gift_certificates.duration, gift_certificates.create_date, gift_certificates.last_update_date "
            + "FROM gift_certificates_tags "
            + "INNER JOIN gift_certificates "
            + "ON gift_certificates.id = gift_certificates_tags.id_gift_certificate "
            + "INNER JOIN tags "
            + "ON tags.id = gift_certificates_tags.id_tag ";

    private static final String WHERE = "WHERE ";
    private static final String ORDER_BY = "ORDER BY ";
    private static final String WHERE_DELIMITER = " AND ";
    private static final String ORDERING_DELIMITER = ", ";
    private static final String PARAMETERS_LINE_APPENDER = " ";
    private static final String REQUEST_APPENDER = ";";
    private static final String TAG_NAME_SEARCH = "tags.name LIKE :tagName";
    private static final String CERTIFICATE_NAME_SEARCH = "gift_certificates.name LIKE :certificateName";
    private static final String CERTIFICATE_DESCRIPTION_SEARCH = "gift_certificates.description LIKE :certificateDescription";
    private static final String CERTIFICATE_NAME_ORDERING = "gift_certificates.name ";
    private static final String CERTIFICATE_DATE_ORDERING = "gift_certificates.create_date ";

    private static final String SELECT_GIFT_CERTIFICATE_BY_ID
            = "SELECT gift_certificates.id, gift_certificates.name, gift_certificates.description, gift_certificates.price, "
            +        "gift_certificates.duration, gift_certificates.create_date, gift_certificates.last_update_date "
            + "FROM gift_certificates "
            + "WHERE gift_certificates.id = :id;";

    private static final String SELECT_GIFT_CERTIFICATE_BY_NAME
            = "SELECT gift_certificates.id, gift_certificates.name, gift_certificates.description, gift_certificates.price, "
            +        "gift_certificates.duration, gift_certificates.create_date, gift_certificates.last_update_date "
            + "FROM gift_certificates "
            + "WHERE gift_certificates.name = :name;";

    private static final String INSERT_GIFT_CERTIFICATE
            = "INSERT INTO gift_certificates(name, description, price, duration, create_date, last_update_date) "
            + "VALUES (:name, :description, :price, :duration, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3));";

    private static final String UPDATE_GIFT_CERTIFICATE
            = "UPDATE gift_certificates SET gift_certificates.name = :name, gift_certificates.description = :description, "
            +        "gift_certificates.price = :price, gift_certificates.duration = :duration, "
            +        "gift_certificates.last_update_date = CURRENT_TIMESTAMP(3) "
            + "WHERE gift_certificates.id = :id;";

    private static final String DELETE_GIFT_CERTIFICATE
            = "DELETE FROM gift_certificates "
            + "WHERE gift_certificates.id = :id;";

    private static final String ADD_GIFT_CERTIFICATE_TAG
            = "INSERT INTO gift_certificates_tags(id_gift_certificate, id_tag) "
            + "VALUES(:idGiftCertificate, :idTag);";

    private static final String DELETE_GIFT_CERTIFICATE_TAG
            = "DELETE FROM gift_certificates_tags "
            + "WHERE gift_certificates_tags.id_gift_certificate = :idGiftCertificate AND "
            +       "gift_certificates_tags.id_tag = :idTag;";


    private RowMapper<GiftCertificate> rowMapper;
    private NamedParameterJdbcTemplate namedJdbcTemplate;

    public GiftCertificateRepositoryImpl(DataSource dataSource, RowMapper<GiftCertificate> rowMapper) {
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.rowMapper = rowMapper;
    }

    @Override
    public List<GiftCertificate> findAll(String tagName, String certificateName, OrderingType orderingName,
                                         String certificateDescription, OrderingType orderingCreateDate) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        String request = PARTIAL_REQUEST_BEGINNING;

        String lineWhere = "";
        if (tagName != null) {
            lineWhere = WHERE;
            lineWhere += TAG_NAME_SEARCH;
            request = PARTIAL_REQUEST_TAGS_JOIN;
            parameters.addValue(TAG_NAME_PARAM, "%" + tagName + "%");
        }

        if (certificateName != null){
            lineWhere = lineWhere.equals("") ? WHERE : lineWhere + WHERE_DELIMITER;
            lineWhere += CERTIFICATE_NAME_SEARCH;
            parameters.addValue(CERTIFICATE_NAME_PARAM, "%" + certificateName + "%");
        }

        if (certificateDescription != null){
            lineWhere = lineWhere.equals("") ? WHERE : lineWhere + WHERE_DELIMITER;
            lineWhere += CERTIFICATE_DESCRIPTION_SEARCH;
            parameters.addValue(CERTIFICATE_DESCRIPTION_PARAM, "%" + certificateDescription  + "%");
        }
        lineWhere += PARAMETERS_LINE_APPENDER;

        String lineOrderBy = "";
        if (orderingName != null){
            lineOrderBy = ORDER_BY;
            lineOrderBy += CERTIFICATE_NAME_ORDERING + orderingName;
        }

        if (orderingCreateDate != null){
            lineOrderBy = lineOrderBy.equals("") ? ORDER_BY : lineOrderBy + ORDERING_DELIMITER;
            lineOrderBy += CERTIFICATE_DATE_ORDERING + orderingCreateDate;
        }
        lineOrderBy += PARAMETERS_LINE_APPENDER;

        request += lineWhere + lineOrderBy + REQUEST_APPENDER;
        List<GiftCertificate> giftCertificates = namedJdbcTemplate.query(request, parameters, rowMapper);
        return giftCertificates;
    }

    @Override
    public Optional<GiftCertificate> findById(long id) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue(ID_PARAM, id);

        List<GiftCertificate> giftCertificates = namedJdbcTemplate.query(SELECT_GIFT_CERTIFICATE_BY_ID, parameters, rowMapper);
        return Optional.ofNullable(giftCertificates.size() == 1 ? giftCertificates.get(0) : null);
    }

    @Override
    public Optional<GiftCertificate> findByName(String name) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue(NAME_PARAM, name);

        List<GiftCertificate> giftCertificates = namedJdbcTemplate.query(SELECT_GIFT_CERTIFICATE_BY_NAME, parameters, rowMapper);
        return Optional.ofNullable(giftCertificates.size() == 1 ? giftCertificates.get(0) : null);
    }

    @Override
    public boolean addCertificateTag(long certificateId, long tagId){
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue(ID_GIFT_CERTIFICATE_PARAM, certificateId);
        parameters.addValue(ID_TAG_PARAM, tagId);

        int result = namedJdbcTemplate.update(ADD_GIFT_CERTIFICATE_TAG, parameters);
        return result > 0;
    }

    @Override
    public boolean removeCertificateTag(long certificateId, long tagId){
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue(ID_GIFT_CERTIFICATE_PARAM, certificateId);
        parameters.addValue(ID_TAG_PARAM, tagId);

        int result = namedJdbcTemplate.update(DELETE_GIFT_CERTIFICATE_TAG, parameters);
        return result > 0;
    }

    @Override
    public long create(GiftCertificate certificate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue(NAME_PARAM, certificate.getName());
        parameters.addValue(DESCRIPTION_PARAM, certificate.getDescription());
        parameters.addValue(PRICE_PARAM, certificate.getPrice());
        parameters.addValue(DURATION_PARAM, certificate.getDuration().toDays());

        namedJdbcTemplate.update(INSERT_GIFT_CERTIFICATE, parameters, keyHolder);

        Number generatedKey = Objects.requireNonNull(keyHolder).getKey();
        return Objects.requireNonNull(generatedKey).longValue();
    }

    @Override
    public boolean update(GiftCertificate certificate) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue(NAME_PARAM, certificate.getName());
        parameters.addValue(DESCRIPTION_PARAM, certificate.getDescription());
        parameters.addValue(PRICE_PARAM, certificate.getPrice());
        parameters.addValue(DURATION_PARAM, certificate.getDuration().toDays());
        parameters.addValue(ID_PARAM, certificate.getId());

        int result = namedJdbcTemplate.update(UPDATE_GIFT_CERTIFICATE, parameters);
        return result > 0;
    }

    @Override
    public boolean delete(long id) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue(ID_PARAM, id);
        int result = namedJdbcTemplate.update(DELETE_GIFT_CERTIFICATE, parameters);
        return result > 0;
    }
}
