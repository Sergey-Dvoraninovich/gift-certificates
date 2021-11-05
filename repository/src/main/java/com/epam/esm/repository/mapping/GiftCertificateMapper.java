package com.epam.esm.repository.mapping;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;

import static com.epam.esm.repository.mapping.TableColumn.*;

@Component
public class GiftCertificateMapper implements RowMapper<GiftCertificate> {
    @Override
    public GiftCertificate mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        GiftCertificate giftCertificate = new GiftCertificate();

        giftCertificate.setId(resultSet.getLong(CERTIFICATE_ID));
        giftCertificate.setName(resultSet.getString(CERTIFICATE_NAME));
        giftCertificate.setDescription(resultSet.getString(CERTIFICATE_DESCRIPTION));
        giftCertificate.setPrice(resultSet.getBigDecimal(CERTIFICATE_PRICE));
        giftCertificate.setDuration(Duration.ofDays(resultSet.getShort(CERTIFICATE_DURATION)));
        giftCertificate.setCreateDate(resultSet.getTimestamp(CERTIFICATE_CREATE_DATE).toLocalDateTime());
        giftCertificate.setLastUpdateDate(resultSet.getTimestamp(CERTIFICATE_LAST_UPDATE_DATE).toLocalDateTime());

        return giftCertificate;
    }
}
