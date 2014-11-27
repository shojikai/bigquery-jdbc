package com.shojikai.jdbc.bigquery;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

public class BQSQLException extends SQLException {

	private static final long serialVersionUID = 1L;

	public BQSQLException(Throwable cause) {
		super(cause);
	}

	public BQSQLException(String reason) {
		super(reason);
		StringWriter sw = new StringWriter();
		super.printStackTrace(new PrintWriter(sw));
	}

	public BQSQLException(String reason, Throwable cause) {
		super(reason, cause);
	}
}
