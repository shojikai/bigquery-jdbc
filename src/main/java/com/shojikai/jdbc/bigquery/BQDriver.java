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
		Connection con = null;
		if (this.acceptsURL(url)) {
			con = new BQConnection(url, info);
		}
		return con;
	}

	@Override
	public boolean acceptsURL(String url) throws SQLException {
		return url.startsWith(URL_PREFIX);
	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

}
