package com.shojikai.jdbc.bigquery;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.Test;

public class BQJDBCTest extends BQDriver {

	@Test
	public void test() {
		String clientEmail = System.getenv("CLIENT_EMAIL");
		String privateKeyFile = System.getenv("PRIVATE_KEY_FILE");
		String projectId = System.getenv("PROJECT_ID");
		String datasetId = System.getenv("DATASET_ID");
		
		if (clientEmail == null || privateKeyFile == null || projectId == null) {
			fail("You must set the following environment variables: CLIENT_EMAIL, PRIVATE_KEY_FILE and PROJECT_ID");
			return;
		}

		String url = "jdbc:bq:";
		if (projectId != null) url = url + "//" + projectId;
		if (datasetId != null) url = url + "/" + datasetId;
		
		Properties info = new Properties();
		info.setProperty("user", clientEmail);
		info.setProperty("password", privateKeyFile);

		Connection con = null;
		try {
			Class.forName("com.shojikai.jdbc.bigquery.BQDriver");
			con = DriverManager.getConnection(url, info);

			assertThat(con, instanceOf(Connection.class));

			String query = "SELECT TOP(title, 10) as title, COUNT(*) as revision_count FROM [publicdata:samples.wikipedia] WHERE wp_namespace = 0";
			ResultSet rs = con.createStatement().executeQuery(query);
			
			int count = 0;
			while (rs.next()) {
				count++;
			}
			assertThat(count, is(10));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
