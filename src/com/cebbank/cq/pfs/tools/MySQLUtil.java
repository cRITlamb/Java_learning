package com.cebbank.cq.pfs.tools;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public final class MySQLUtil
{
	private MySQLUtil()
	{
	}

	private static ComboPooledDataSource dataSource;
	static
	{
		try
		{
			dataSource = new ComboPooledDataSource();
			dataSource.setUser("root");
			dataSource.setPassword("CEB");
			dataSource.setJdbcUrl("jdbc:mysql://PreSaleSupervision.cq.cebbank:3306/PreSaleSupervisionTest?autoReconnect=true");
			dataSource.setDriverClass("com.mysql.jdbc.Driver");
	   
			dataSource.setInitialPoolSize(3);
			dataSource.setMinPoolSize(3);
			dataSource.setMaxPoolSize(10);
			dataSource.setMaxIdleTime(25000);
			dataSource.setIdleConnectionTestPeriod(18000);
			dataSource.setMaxConnectionAge(400);
			dataSource.setAcquireIncrement(1);
			
			dataSource.setAcquireRetryAttempts(30);
			dataSource.setAcquireRetryDelay(1000);
			
			dataSource.setTestConnectionOnCheckin(true);
			dataSource.setAutomaticTestTable("C3P0TestTable");

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static Connection getConnection()
	{
		try
		{
			return dataSource.getConnection();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static void freeConnection(ResultSet rs, Statement st, Connection conn)
			throws SQLException
	{
		try
		{
			if(rs != null)
				rs.close();
		}
		finally
		{
			try
			{
				if(st != null)
					st.close();
			}
			finally
			{
				if(conn != null)
					conn.close();
			}
		}
	}
}
