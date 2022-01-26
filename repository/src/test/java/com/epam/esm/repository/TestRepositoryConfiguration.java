package com.epam.esm.repository;

public class TestRepositoryConfiguration {
    private static final String INIT_DDL = "classpath:init_schema.sql";
    private static final String INIT_DML = "classpath:init_data.sql";
    private static final String DB_NAME = "gift_certificates";


//    @Autowired
//    private DataSource dataSource;
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//    @Autowired
//    private QueryBuilder queryBuilder;
//
//
//    @Bean
//    public DataSource dataSource() {
//        return new EmbeddedDatabaseBuilder()
//                .setType(EmbeddedDatabaseType.HSQL)
//                .setName(DB_NAME)
//                .addScript(INIT_DDL)
//                .addScript(INIT_DML)
//                .build();
//    }
//
//
//    @Bean
//    public JdbcTemplate jdbcTemplate() {
//        return new JdbcTemplate(dataSource);
//    }
//
//
//    @Bean
//    public TagMapper tagMapper() {
//        return new TagMapper();
//    }
//
//
//    @Bean
//    public TagDao tagDao() {
//        return new TagJdbc(jdbcTemplate, tagMapper);
//    }
//
//
//    @Bean
//    public GiftCertificateMapper giftCertificateMapper() {
//        return new GiftCertificateMapper(tagMapper);
//    }
//
//
//    @Bean
//    public QueryBuilder queryBuilder() {
//        return new QueryBuilder();
//    }
//
//
//    @Bean
//    public GiftCertificateDao giftCertificateDao() {
//        return new GiftCertificateJdbc(jdbcTemplate, giftCertificateMapper, queryBuilder);
//    }
}
