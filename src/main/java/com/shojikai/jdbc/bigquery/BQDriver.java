package com.shojikai.jdbc.bigquery;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

public class BQDriver implements Driver {
	
	private static final boolean JDBC_COMPLIANT = false;
	private static final String URL_PREFIX = "jdbc:bq:";
	private static final int MAJOR_VERSION = 0;
	private static final int MINOR_VERSION = 1;
	
	static {
		BQDriver driver = new BQDriver();
        try {
			DriverManager.registerDriver(driver);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Connection connect(String url, Properties info) throws SQLException {
		if (acceptsURL(url)) {
			return new BQConnection(url, info);
		} else {
			return null;
		}
	}

	@Override
	public boolean acceptsURL(String url) throws SQLException {
		return url.startsWith(URL_PREFIX);
	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public int getMajorVersion() {
		return BQDriver.MAJOR_VERSION;
	}

	@Override
	public int getMinorVersion() {
		return BQDriver.MINOR_VERSION;
	}

	@Override
	public boolean jdbcCompliant() {
		return BQDriver.JDBC_COMPLIANT;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new SQLFeatureNotSupportedException("Method not supported");
	}

}
