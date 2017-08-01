package com.flying.emailsend.factory;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import com.flying.emailsend.util.Encrypt;

/**
 * auth:flying date:2017年7月31日
 **/
public class ConnectionFactory {
	private static Connection connection = null;

	/**
	 * 构造函数,初始化数据库连接
	 */
	public ConnectionFactory() {
	}

	/**
	 * 初始化数据库链接
	 */
	static {
		InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
		Properties properties = new Properties();
		try {
			properties.load(inputStream);
			String username = properties.getProperty("user").trim();
			String password = Encrypt.aesDecrypt(properties.getProperty("password").trim());
			String jdbcurl = properties.getProperty("jdbcUrl").trim();
			String drivercalss = properties.getProperty("driverclass").trim();
			Driver driver = (Driver) Class.forName(drivercalss).newInstance();
			Properties dbinfo = new Properties();
			dbinfo.setProperty("user", username);
			dbinfo.setProperty("password", password);
			connection = driver.connect(jdbcurl, dbinfo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Get
	 * 
	 * @return Connection
	 */
	public static Connection getConnection() {
		return connection;
	}

	/**
	 * 关闭数据库链接
	 */
	public static void closeConnection() {
		try {
			if (connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
