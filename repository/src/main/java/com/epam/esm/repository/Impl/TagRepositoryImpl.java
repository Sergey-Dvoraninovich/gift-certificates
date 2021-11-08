package com.epam.esm.repository.Impl;

import com.epam.esm.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import com.epam.esm.entity.Tag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {
    private static final String ID_PARAM = "id";
    private static final String NAME_PARAM = "name";
    private static final String CERTIFICATE_ID_PARAM = "idGiftCertificate";

    private static final String SELECT_ALL_TAGS
            = "SELECT tags.id, tags.name "
            + "FROM tags;";

    private static final String SELECT_TAG_BY_ID
            = "SELECT tags.id, tags.name "
            + "FROM tags "
            + "WHERE tags.id = :id;";

    private static final String SELECT_TAG_BY_NAME
            = "SELECT tags.id, tags.name "
            + "FROM tags "
            + "WHERE tags.name = :name;";

    private static final String SELECT_TAGS_BY_CERTIFICATE_ID
            = "SELECT tags.id, tags.name "
            + "FROM tags "
            + "INNER JOIN gift_certificates_tags AS gct "
            + "ON gct.id_tag = tags.id "
            + "WHERE gct.id_gift_certificate = :idGiftCertificate;";

    private static final String INSERT_TAG
            = "INSERT INTO tags(name) "
            + "VALUES (:name);";

    private static final String DELETE_TAG
            = "DELETE FROM tags "
            + "WHERE tags.id = :id;";

    private final RowMapper<Tag> rowMapper;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    @Override
    public List<Tag> findAll() {
        return namedJdbcTemplate.query(SELECT_ALL_TAGS, rowMapper);
    }

    @Override
    public Optional<Tag> findById(long id) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue(ID_PARAM, id);

        List<Tag> tags = namedJdbcTemplate.query(SELECT_TAG_BY_ID, parameters, rowMapper);
        return Optional.ofNullable(tags.size() == 1 ? tags.get(0) : null);
    }

    @Override
    public Optional<Tag> findByName(String name) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue(NAME_PARAM, name);

        List<Tag> tags = namedJdbcTemplate.query(SELECT_TAG_BY_NAME, parameters, rowMapper);
        return Optional.ofNullable(tags.size() == 1 ? tags.get(0) : null);
    }

    @Override
    public List<Tag> findByCertificateId(long certificateId) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue(CERTIFICATE_ID_PARAM, certificateId);

        List<Tag> tags = namedJdbcTemplate.query(SELECT_TAGS_BY_CERTIFICATE_ID, parameters, rowMapper);
        return tags;
    }

    @Override
    public long create(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource parameters = new MapSqlParameterSource().addValue(NAME_PARAM, tag.getName());
        namedJdbcTemplate.update(INSERT_TAG, parameters, keyHolder);

        Number generatedKey = Objects.requireNonNull(keyHolder).getKey();
        return Objects.requireNonNull(generatedKey).longValue();
    }

    @Override
    public boolean delete(long id) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue(ID_PARAM, id);
        int result = namedJdbcTemplate.update(DELETE_TAG, parameters);
        return result > 0;
    }
}
