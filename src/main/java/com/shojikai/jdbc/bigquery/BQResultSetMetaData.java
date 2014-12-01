package com.shojikai.jdbc.bigquery;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import com.google.api.services.bigquery.model.QueryResponse;
import com.google.api.services.bigquery.model.TableFieldSchema;
import com.google.api.services.bigquery.model.TableSchema;

public class BQResultSetMetaData implements ResultSetMetaData {

	private QueryResponse res;

	public BQResultSetMetaData(QueryResponse res) {
		this.res = res;
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
	public int getColumnCount() throws SQLException {
		TableSchema schema = res.getSchema();
		if (schema != null) {
			List<TableFieldSchema> schemaFieldList = schema.getFields();
			if (schemaFieldList != null) {
				return schemaFieldList.size();
			}
		}
		return 0;
	}

	@Override
	public boolean isAutoIncrement(int column) throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public boolean isCaseSensitive(int column) throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public boolean isSearchable(int column) throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public boolean isCurrency(int column) throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public int isNullable(int column) throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public boolean isSigned(int column) throws SQLException {
		return false;
	}

	@Override
	public int getColumnDisplaySize(int column) throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public String getColumnLabel(int column) throws SQLException {
		return res.getSchema().getFields().get(column - 1).getName();
	}

	@Override
	public String getColumnName(int column) throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public String getSchemaName(int column) throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public int getPrecision(int column) throws SQLException {
		int columnType = getColumnType(column);
		if (columnType == java.sql.Types.FLOAT) {
			return Float.MAX_EXPONENT;
		} else if (columnType == java.sql.Types.BOOLEAN) {
			return 1;
		} else if (columnType == java.sql.Types.BIGINT) {
			return Long.SIZE;
		} else if (columnType == java.sql.Types.VARCHAR) {
			return 64 * 1024;
		} else if (columnType == java.sql.Types.TIMESTAMP) {
			return 4;
		} else {
			return 0;
		}
	}

	@Override
	public int getScale(int column) throws SQLException {
		if (getColumnType(column) == java.sql.Types.FLOAT) {
			int max = 0;
			for (int i = 0; i < res.getRows().size(); i++) {
				String rowdata = res.getRows().get(i).getF().get(column - 1).getV().toString();
				if (rowdata.contains(".")) {
					int pointback = rowdata.length() - rowdata.indexOf(".");
					if (pointback > max) pointback = max;
				}
			}
			return max;
		} else {
			return 0;
		}
	}

	@Override
	public String getTableName(int column) throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public String getCatalogName(int column) throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public int getColumnType(int column) throws SQLException {
		String columnType = res.getSchema().getFields().get(column - 1).getType();
		if (columnType.equals("FLOAT")) {
			return java.sql.Types.FLOAT;
		} else if (columnType.equals("BOOLEAN")) {
			return java.sql.Types.BOOLEAN;
		} else if (columnType.equals("INTEGER")) {
			return java.sql.Types.BIGINT;
		} else if (columnType.equals("STRING")) {
			return java.sql.Types.VARCHAR;
		} else if (columnType.equals("TIMESTAMP")) {
			return java.sql.Types.TIMESTAMP;
		} else {
			return 0;
		}
	}

	@Override
	public String getColumnTypeName(int column) throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public boolean isReadOnly(int column) throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public boolean isWritable(int column) throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public boolean isDefinitelyWritable(int column) throws SQLException {
		throw new SQLException("Method not supported");
	}

	@Override
	public String getColumnClassName(int column) throws SQLException {
		throw new SQLException("Method not supported");
	}

}
