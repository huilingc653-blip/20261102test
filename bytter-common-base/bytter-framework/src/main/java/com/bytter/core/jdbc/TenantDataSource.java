package com.bytter.core.jdbc;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.*;
import java.util.logging.Logger;

/**
 * 多租户数据源包装器。
 *
 * @author zhaohc
 * @since 2025/12/18 9:26
 */
public class TenantDataSource implements DataSource {
    private final DataSource delegate;
    private final TenantSQLHandler interceptor;

    public TenantDataSource(DataSource delegate, TenantProperties tenantProperties) {
        this.delegate = delegate;
        this.interceptor = new TenantSQLHandler();
        interceptor.initTenantProperties(tenantProperties);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return new TenantConnection(delegate.getConnection(), interceptor);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return new TenantConnection(delegate.getConnection(username, password), interceptor);
    }

    // 其他DataSource方法委托给底层数据源
    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return delegate.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        delegate.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        delegate.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return delegate.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (iface.isInstance(this)) {
            return iface.cast(this);
        }
        return delegate.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return iface.isInstance(this) || delegate.isWrapperFor(iface);
    }
}
