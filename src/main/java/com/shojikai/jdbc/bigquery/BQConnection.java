package com.shojikai.jdbc.bigquery;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.bigquery.Bigquery;
import com.google.api.services.bigquery.BigqueryScopes;

public class BQConnection implements Connection {

	private static final Pattern URL_PATTERN = Pattern.compile("jdbc:bq:(?://([a-zA-Z0-9_-]+)(?:/([a-zA-Z0-9_-]+))?)?");

	private Bigquery bq;
	private String projectId;
	private String datasetId;
	private boolean isClosed;

	public BQConnection(String url, Properties info) throws SQLException {
		try {
			String clientEmail = info.getProperty("user");
			String privateKeyFile = info.getProperty("password");
			HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
			GoogleCredential credential = new GoogleCredential.Builder()
				.setTransport(httpTransport)
				.setJsonFactory(jsonFactory)
				.setServiceAccountId(clientEmail)
				.setServiceAccountPrivateKeyFromP12File(new File(privateKeyFile))
				.setServiceAccountScopes(Collections.singleton(BigqueryScopes.BIGQUERY))
				.build();
			bq = new Bigquery.Builder(httpTransport, jsonFactory, credential)
				.setApplicationName(getClass().getSimpleName())
				.build();
			Matcher matcher = URL_PATTERN.matcher(url);
			if (matcher.find()) {
				if (matcher.group(1) != null) projectId = matcher.group(1);
				if (matcher.group(2) != null) datasetId = matcher.group(2);
			}
			isClosed = true;
		} catch (GeneralSecurityException e) {
			throw new SQLException(e);
		} catch (IOException e) {
			throw new SQLException(e);
		}
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public Statement createStatement() throws SQLException {
		return new BQStatement(bq, projectId, datasetId);
	}

	@Override
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		return new BQPreparedStatement(bq, projectId, datasetId, sql);
	}

	@Override
	public CallableStatement prepareCall(String sql) throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public String nativeSQL(String sql) throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		// nop
	}

	@Override
	public boolean getAutoCommit() throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public void commit() throws SQLException {
		// nop
	}

	@Override
	public void rollback() throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public void close() throws SQLException {
		isClosed = false;
	}

	@Override
	public boolean isClosed() throws SQLException {
		return isClosed;
	}

	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		return new BQDatabaseMetaData(bq);
	}

	@Override
	public void setReadOnly(boolean readOnly) throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public boolean isReadOnly() throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public void setCatalog(String catalog) throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public String getCatalog() throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public void setTransactionIsolation(int level) throws SQLException {
		// nop
	}

	@Override
	public int getTransactionIsolation() throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public void clearWarnings() throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency)
			throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		return new BQPreparedStatement(bq, projectId, datasetId, sql);
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public void setHoldability(int holdability) throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public int getHoldability() throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public Savepoint setSavepoint() throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public Savepoint setSavepoint(String name) throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public void rollback(Savepoint savepoint) throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public Statement createStatement(int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
					throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
					throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
					throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
			throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
			throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public PreparedStatement prepareStatement(String sql, String[] columnNames)
			throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public Clob createClob() throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public Blob createBlob() throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public NClob createNClob() throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public boolean isValid(int timeout) throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public void setClientInfo(String name, String value)
			throws SQLClientInfoException {
		throw new SQLClientInfoException("Method not supported", null);
	}

	@Override
	public void setClientInfo(Properties properties)
			throws SQLClientInfoException {
		throw new SQLClientInfoException("Method not supported", null);
	}

	@Override
	public String getClientInfo(String name) throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public Properties getClientInfo() throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public Array createArrayOf(String typeName, Object[] elements)
			throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public Struct createStruct(String typeName, Object[] attributes)
			throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public void setSchema(String schema) throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public String getSchema() throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public void abort(Executor executor) throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public void setNetworkTimeout(Executor executor, int milliseconds)
			throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public int getNetworkTimeout() throws SQLException {
		throw new SQLException("Method not supported");
	}

}
