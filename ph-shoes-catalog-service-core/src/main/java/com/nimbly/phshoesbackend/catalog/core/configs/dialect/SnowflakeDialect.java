package com.nimbly.phshoesbackend.catalog.core.configs.dialect;

import org.hibernate.dialect.DatabaseVersion;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.pagination.LimitHandler;
import org.hibernate.dialect.pagination.LimitOffsetLimitHandler;

/**
 * Minimal Snowflake dialect focused on read-only usage for catalog queries.
 */
public class SnowflakeDialect extends Dialect {

    public SnowflakeDialect() {
        super(DatabaseVersion.make(1));
    }

    @Override
    public LimitHandler getLimitHandler() {
        return LimitOffsetLimitHandler.INSTANCE;
    }

    @Override
    public boolean supportsUnionAll() {
        return true;
    }

    @Override
    public boolean supportsNullPrecedence() {
        return true;
    }
}
