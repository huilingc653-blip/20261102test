package com.bytter.core.jdbc;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Map;

/**
 * 租户PreparedStatement包装器。
 *
 * @author zhaohc
 * @since 2025/12/18 9:32
 */
@Slf4j
public class TenantPreparedStatement implements PreparedStatement {
    private final PreparedStatement delegate;
    private final ParsedSQL parsedSQL;
    private final TenantConnection connection;

    public TenantPreparedStatement(PreparedStatement delegate, ParsedSQL parsedSQL, TenantConnection connection) {
        this.delegate = delegate;
        this.parsedSQL = parsedSQL;
        this.connection = connection;
    }

    // 添加 protected 方法让子类可以访问 delegate
    protected PreparedStatement getDelegate() {
        return delegate;
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        return delegate.executeQuery();
    }

    @Override
    public int executeUpdate() throws SQLException {
        return delegate.executeUpdate();
    }

    @Override
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setNull(adjustedIndex, sqlType);
        }
    }

    @Override
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setBoolean(adjustedIndex, x);
        }
    }

    @Override
    public void setByte(int parameterIndex, byte x) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setByte(adjustedIndex, x);
        }
    }

    @Override
    public void setShort(int parameterIndex, short x) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setShort(adjustedIndex, x);
        }
    }

    @Override
    public void setInt(int parameterIndex, int x) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setInt(adjustedIndex, x);
        }
    }

    @Override
    public void setLong(int parameterIndex, long x) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setLong(adjustedIndex, x);
        }
    }

    @Override
    public void setFloat(int parameterIndex, float x) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setFloat(adjustedIndex, x);
        }
    }

    @Override
    public void setDouble(int parameterIndex, double x) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setDouble(adjustedIndex, x);
        }
    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setBigDecimal(adjustedIndex, x);
        }
    }

    @Override
    public void setString(int parameterIndex, String x) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setString(adjustedIndex, x);
        }
    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setBytes(adjustedIndex, x);
        }
    }

    @Override
    public void setDate(int parameterIndex, Date x) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setDate(adjustedIndex, x);
        }
    }

    @Override
    public void setTime(int parameterIndex, Time x) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setTime(adjustedIndex, x);
        }
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setTimestamp(adjustedIndex, x);
        }
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setAsciiStream(adjustedIndex, x, length);
        }
    }

    @Override
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setUnicodeStream(adjustedIndex, x, length);
        }
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setBinaryStream(adjustedIndex, x, length);
        }
    }

    @Override
    public void clearParameters() throws SQLException {
        delegate.clearParameters();
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setObject(adjustedIndex, x, targetSqlType);
        }
    }

    @Override
    public void setObject(int parameterIndex, Object x) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setObject(adjustedIndex, x);
        }
    }

    @Override
    public boolean execute() throws SQLException {
        return delegate.execute();
    }

    @Override
    public void addBatch() throws SQLException {
        delegate.addBatch();
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setCharacterStream(adjustedIndex, reader, length);
        }
    }

    @Override
    public void setRef(int parameterIndex, Ref x) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setRef(adjustedIndex, x);
        }
    }

    @Override
    public void setBlob(int parameterIndex, Blob x) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setBlob(adjustedIndex, x);
        }
    }

    @Override
    public void setClob(int parameterIndex, Clob x) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setClob(adjustedIndex, x);
        }
    }

    @Override
    public void setArray(int parameterIndex, Array x) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setArray(adjustedIndex, x);
        }
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return delegate.getMetaData();
    }

    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setDate(adjustedIndex, x, cal);
        }
    }

    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setTime(adjustedIndex, x, cal);
        }
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setTimestamp(adjustedIndex, x, cal);
        }
    }

    @Override
    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setNull(adjustedIndex, sqlType, typeName);
        }
    }

    @Override
    public void setURL(int parameterIndex, URL x) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setURL(adjustedIndex, x);
        }
    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        return delegate.getParameterMetaData();
    }

    @Override
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setRowId(adjustedIndex, x);
        }
    }

    @Override
    public void setNString(int parameterIndex, String value) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setNString(adjustedIndex, value);
        }
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setNCharacterStream(adjustedIndex, value, length);
        }
    }

    @Override
    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setNClob(adjustedIndex, value);
        }
    }

    @Override
    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setClob(adjustedIndex, reader, length);
        }
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setBlob(adjustedIndex, inputStream, length);
        }
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setNClob(adjustedIndex, reader, length);
        }
    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setSQLXML(adjustedIndex, xmlObject);
        }
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setObject(adjustedIndex, x, targetSqlType, scaleOrLength);
        }
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setAsciiStream(adjustedIndex, x, length);
        }
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setBinaryStream(adjustedIndex, x, length);
        }
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setCharacterStream(adjustedIndex, reader, length);
        }
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setAsciiStream(adjustedIndex, x);
        }
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setBinaryStream(adjustedIndex, x);
        }
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setCharacterStream(adjustedIndex, reader);
        }
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setNCharacterStream(adjustedIndex, value);
        }
    }

    @Override
    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setClob(adjustedIndex, reader);
        }
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setBlob(adjustedIndex, inputStream);
        }
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        // 调整参数索引
        int adjustedIndex = adjustParameterIndex(parameterIndex);
        if (adjustedIndex > 0) {
            delegate.setNClob(adjustedIndex, reader);
        }
    }

    // 以下是PreparedStatement的其他方法委托给底层Statement
    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        String tenantId = connection.getTenantId();
        if (tenantId != null) {
            ParsedSQL parsed = connection.getInterceptor().interceptSQL(sql, tenantId);
            return delegate.executeQuery(parsed.getSql());
        }
        connection.getInterceptor().checkEmptyTenant(sql);
        return delegate.executeQuery(sql);
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        String tenantId = connection.getTenantId();
        if (tenantId != null) {
            ParsedSQL parsed = connection.getInterceptor().interceptSQL(sql, tenantId);
            return delegate.executeUpdate(parsed.getSql());
        }
        connection.getInterceptor().checkEmptyTenant(sql);
        return delegate.executeUpdate(sql);
    }

    @Override
    public void close() throws SQLException {
        delegate.close();
    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        return delegate.getMaxFieldSize();
    }

    @Override
    public void setMaxFieldSize(int max) throws SQLException {
        delegate.setMaxFieldSize(max);
    }

    @Override
    public int getMaxRows() throws SQLException {
        return delegate.getMaxRows();
    }

    @Override
    public void setMaxRows(int max) throws SQLException {
        delegate.setMaxRows(max);
    }

    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {
        delegate.setEscapeProcessing(enable);
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        return delegate.getQueryTimeout();
    }

    @Override
    public void setQueryTimeout(int seconds) throws SQLException {
        delegate.setQueryTimeout(seconds);
    }

    @Override
    public void cancel() throws SQLException {
        delegate.cancel();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return delegate.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        delegate.clearWarnings();
    }

    @Override
    public void setCursorName(String name) throws SQLException {
        delegate.setCursorName(name);
    }

    @Override
    public boolean execute(String sql) throws SQLException {
        String tenantId = connection.getTenantId();
        if (tenantId != null) {
            ParsedSQL parsed = connection.getInterceptor().interceptSQL(sql, tenantId);
            return delegate.execute(parsed.getSql());
        }
        connection.getInterceptor().checkEmptyTenant(sql);
        return delegate.execute(sql);
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return delegate.getResultSet();
    }

    @Override
    public int getUpdateCount() throws SQLException {
        return delegate.getUpdateCount();
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        return delegate.getMoreResults();
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {
        delegate.setFetchDirection(direction);
    }

    @Override
    public int getFetchDirection() throws SQLException {
        return delegate.getFetchDirection();
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        delegate.setFetchSize(rows);
    }

    @Override
    public int getFetchSize() throws SQLException {
        return delegate.getFetchSize();
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        return delegate.getResultSetConcurrency();
    }

    @Override
    public int getResultSetType() throws SQLException {
        return delegate.getResultSetType();
    }

    @Override
    public void addBatch(String sql) throws SQLException {
        String tenantId = connection.getTenantId();
        if (tenantId != null) {
            ParsedSQL parsed = connection.getInterceptor().interceptSQL(sql, tenantId);
            delegate.addBatch(parsed.getSql());
            return;
        }
        connection.getInterceptor().checkEmptyTenant(sql);
        delegate.addBatch(sql);
    }

    @Override
    public void clearBatch() throws SQLException {
        delegate.clearBatch();
    }

    @Override
    public int[] executeBatch() throws SQLException {
        return delegate.executeBatch();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connection;
    }

    @Override
    public boolean getMoreResults(int current) throws SQLException {
        return delegate.getMoreResults(current);
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        return delegate.getGeneratedKeys();
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        String tenantId = connection.getTenantId();
        if (tenantId != null) {
            ParsedSQL parsed = connection.getInterceptor().interceptSQL(sql, tenantId);
            return delegate.executeUpdate(parsed.getSql(), autoGeneratedKeys);
        }
        connection.getInterceptor().checkEmptyTenant(sql);
        return delegate.executeUpdate(sql, autoGeneratedKeys);
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        String tenantId = connection.getTenantId();
        if (tenantId != null) {
            ParsedSQL parsed = connection.getInterceptor().interceptSQL(sql, tenantId);
            return delegate.executeUpdate(parsed.getSql(), columnIndexes);
        }
        connection.getInterceptor().checkEmptyTenant(sql);
        return delegate.executeUpdate(sql, columnIndexes);
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        String tenantId = connection.getTenantId();
        if (tenantId != null) {
            ParsedSQL parsed = connection.getInterceptor().interceptSQL(sql, tenantId);
            return delegate.executeUpdate(parsed.getSql(), columnNames);
        }
        connection.getInterceptor().checkEmptyTenant(sql);
        return delegate.executeUpdate(sql, columnNames);
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        String tenantId = connection.getTenantId();
        if (tenantId != null) {
            ParsedSQL parsed = connection.getInterceptor().interceptSQL(sql, tenantId);
            return delegate.execute(parsed.getSql(), autoGeneratedKeys);
        }
        connection.getInterceptor().checkEmptyTenant(sql);
        return delegate.execute(sql, autoGeneratedKeys);
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        String tenantId = connection.getTenantId();
        if (tenantId != null) {
            ParsedSQL parsed = connection.getInterceptor().interceptSQL(sql, tenantId);
            return delegate.execute(parsed.getSql(), columnIndexes);
        }
        connection.getInterceptor().checkEmptyTenant(sql);
        return delegate.execute(sql, columnIndexes);
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        String tenantId = connection.getTenantId();
        if (tenantId != null) {
            ParsedSQL parsed = connection.getInterceptor().interceptSQL(sql, tenantId);
            return delegate.execute(parsed.getSql(), columnNames);
        }
        connection.getInterceptor().checkEmptyTenant(sql);
        return delegate.execute(sql, columnNames);
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        return delegate.getResultSetHoldability();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return delegate.isClosed();
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {
        delegate.setPoolable(poolable);
    }

    @Override
    public boolean isPoolable() throws SQLException {
        return delegate.isPoolable();
    }

    @Override
    public void closeOnCompletion() throws SQLException {
        delegate.closeOnCompletion();
    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        return delegate.isCloseOnCompletion();
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

    /**
     * 根据原始SQL和修改后的SQL计算参数索引。
     *
     * @param originalIndex 原始SQL参数索引。
     * @return 修改后的SQL参数索引。
     */
    private int adjustParameterIndex(int originalIndex) {
        // 如果SQL没有修改，直接返回原索引
        if (parsedSQL == null || parsedSQL.getOriginalSql().equals(parsedSQL.getSql())) {
            return originalIndex;
        }

        return parsedSQL.getParameterIndex(originalIndex);
    }

}