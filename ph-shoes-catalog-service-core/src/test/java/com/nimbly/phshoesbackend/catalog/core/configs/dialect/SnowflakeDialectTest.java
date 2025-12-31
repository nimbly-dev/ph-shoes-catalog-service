package com.nimbly.phshoesbackend.catalog.core.configs.dialect;

import org.hibernate.dialect.pagination.LimitHandler;
import org.hibernate.dialect.pagination.LimitOffsetLimitHandler;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SnowflakeDialectTest {
    @Test
    void supportsExpectedPaginationAndFeatures() {
        // Arrange
        SnowflakeDialect dialect = new SnowflakeDialect();

        // Act
        LimitHandler limitHandler = dialect.getLimitHandler();

        // Assert
        assertThat(limitHandler).isSameAs(LimitOffsetLimitHandler.INSTANCE);
        assertThat(dialect.supportsUnionAll()).isTrue();
        assertThat(dialect.supportsNullPrecedence()).isTrue();
    }
}
