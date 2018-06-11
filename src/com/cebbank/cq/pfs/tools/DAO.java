package com.cebbank.cq.pfs.tools;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DAO
{
	static Logger log = LogManager.getLogger(DAO.class);
	
	public static List<String> getAllStockCodes4Yahoo(String date)
	{
		ResultSet rs = null;
		Connection con = null;
		Statement stmt = null;
		ArrayList<String> ret = new ArrayList<String>();
	
		String sql = "select code,market from code where date<'"+date+"'";
		try
		{
			con = MySQLUtil.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next())
			{
				ret.add(rs.getString("code")+"."+rs.getString("market"));
			}
		}
		catch (Exception e)
		{
			log.error("SQLException:"+sql);
			e.printStackTrace();
		}
		finally
		{
			MySQLUtil.freeConnection(rs,stmt,con);
		}
		return ret;
	}
	
	public static List<String> getAllStockCodes4Sina(String date)
	{
		ResultSet rs = null;
		Connection con = null;
		Statement stmt = null;
		ArrayList<String> ret = new ArrayList<String>();
	
		String sql = "select code,market from code where date<'"+date+"'";
		try
		{
			con = MySQLUtil.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next())
			{
				String market=rs.getString("market");
				if(market.equals("ss"))
				{
					ret.add("sh"+rs.getString("code"));
				}
				else
				{
					ret.add("sz"+rs.getString("code"));
				}
			}
		}
		catch (Exception e)
		{
			log.error("SQLException:"+sql);
			e.printStackTrace();
		}
		finally
		{
			MySQLUtil.freeConnection(rs,stmt,con);
		}
		return ret;
	}
	
	public static Map<String,Float> getAdjAvgVol(String year)
	{
		ResultSet rs = null;
		Connection con = null;
		Statement stmt = null;
		Map<String,Float> ret = new HashMap<String,Float>();
	
		String sql = "select code,adjvol from adjavgvol where year='"+year+"'";
		try
		{
			con = MySQLUtil.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next())
			{
				ret.put(rs.getString("code"),rs.getFloat("adjvol"));
			}
		}
		catch (Exception e)
		{
			log.error("SQLException:"+sql);
			e.printStackTrace();
		}
		finally
		{
			MySQLUtil.freeConnection(rs,stmt,con);
		}
		return ret;
	}
	
	public static List<String> getLastCloseIsNull()
	{
		ResultSet rs = null;
		Connection con = null;
		Statement stmt = null;
		ArrayList<String> ret = new ArrayList<String>();
	
		String sql = "select code,date from price where lastclose is null and volume!=0";
		try
		{
			con = MySQLUtil.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next())
			{
				ret.add(rs.getString("code")+";"+rs.getString("date"));
			}
		}
		catch (Exception e)
		{
			log.error("SQLException:"+sql);
			e.printStackTrace();
		}
		finally
		{
			MySQLUtil.freeConnection(rs,stmt,con);
		}
		return ret;
	}
	
	public static List<String> getNextDateIsNull()
	{
		ResultSet rs = null;
		Connection con = null;
		Statement stmt = null;
		ArrayList<String> ret = new ArrayList<String>();
	
		String sql = "select code,date from price where nextdate is null and volume!=0";
		try
		{
			con = MySQLUtil.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next())
			{
				ret.add(rs.getString("code")+";"+rs.getString("date"));
			}
		}
		catch (Exception e)
		{
			log.error("SQLException:"+sql);
			e.printStackTrace();
		}
		finally
		{
			MySQLUtil.freeConnection(rs,stmt,con);
		}
		return ret;
	}
	
	public static String getLastPrice(String code,String date)
	{
		ResultSet rs = null;
		Connection con = null;
		Statement stmt = null;
		String ret = null;
	
		String sql = "select date,close from price where code='"+code+"' and volume!=0 and date<'"+date+"' order by date desc limit 0,1";
		try
		{
			con = MySQLUtil.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			if(rs.next())
			{
				ret=(rs.getString("date")+";"+rs.getString("close"));
			}
		}
		catch (Exception e)
		{
			log.error("SQLException:"+sql);
			e.printStackTrace();
		}
		finally
		{
			MySQLUtil.freeConnection(rs,stmt,con);
		}
		return ret;
	}
	
	public static String getNextDate(String code,String date)
	{
		ResultSet rs = null;
		Connection con = null;
		Statement stmt = null;
		String ret = null;
	
		String sql = "select date from price where code='"+code+"' and volume!=0 and date>'"+date+"' order by date limit 0,1";
		try
		{
			con = MySQLUtil.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			if(rs.next())
			{
				ret=rs.getString("date");
			}
		}
		catch (Exception e)
		{
			log.error("SQLException:"+sql);
			e.printStackTrace();
		}
		finally
		{
			MySQLUtil.freeConnection(rs,stmt,con);
		}
		return ret;
	}
	
	public static List<Price> getPricesByDateFilter(String date)
	{
		ResultSet rs = null;
		Connection con = null;
		Statement stmt = null;
		ArrayList<Price> ret = new ArrayList<Price>();
	
		String sql = "select * from price t1 left join adjavgvol t2 on t1.code=t2.code where date='"+date+"' and volume!=0 and actad='a' and upstick<60 and t1.volume>3*t2.adjvol and t1.volume<5*t2.adjvol and t1.adrate>3.5";
		try
		{
			con = MySQLUtil.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next())
			{
				ret.add(new Price(rs.getString("code"),date,rs.getFloat("open"),rs.getFloat("high"),rs.getFloat("low"),rs.getFloat("close"),rs.getInt("volume"),rs.getFloat("adjclose"),rs.getString("ad"),rs.getString("actad"),rs.getFloat("lastclose"),rs.getFloat("rate"),rs.getFloat("adrate")));
			}
		}
		catch (Exception e)
		{
			log.error("SQLException:"+sql);
			e.printStackTrace();
		}
		finally
		{
			MySQLUtil.freeConnection(rs,stmt,con);
		}
		return ret;
	}
	
	public static Price getPricesByCodeAndDateFilter(String code,String date)
	{
		ResultSet rs = null;
		Connection con = null;
		Statement stmt = null;
		Price ret = null;
	
		String sql = "select * from price t1 left join adjavgvol t2 on t1.code=t2.code where date='"+date+"' and volume!=0 and t1.code='"+code+"' and (t1.ad!='a' or t1.actad!='a' or t1.upstick>60 or t1.volume<2*t2.adjvol or t1.adrate<1.5)";
		try
		{
			con = MySQLUtil.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			if(rs.next())
			{
				ret=new Price(rs.getString("code"),date,rs.getFloat("open"),rs.getFloat("high"),rs.getFloat("low"),rs.getFloat("close"),rs.getInt("volume"),rs.getFloat("adjclose"),rs.getString("ad"),rs.getString("actad"),rs.getFloat("lastclose"),rs.getFloat("rate"),rs.getFloat("adrate"));
			}
		}
		catch (Exception e)
		{
			log.error("SQLException:"+sql);
			e.printStackTrace();
		}
		finally
		{
			MySQLUtil.freeConnection(rs,stmt,con);
		}
		return ret;
	}
	
	public static Price getPricesByCodeAndDateWithoutFilter(String code,String date)
	{
		ResultSet rs = null;
		Connection con = null;
		Statement stmt = null;
		Price ret = null;
	
		String sql = "select * from price t1 where date>='"+date+"' and code='"+code+"' order by date limit 0,1";
		try
		{
			con = MySQLUtil.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			if(rs.next())
			{
				ret=new Price(rs.getString("code"),date,rs.getFloat("open"),rs.getFloat("high"),rs.getFloat("low"),rs.getFloat("close"),rs.getInt("volume"),rs.getFloat("adjclose"),rs.getString("ad"),rs.getString("actad"),rs.getFloat("lastclose"),rs.getFloat("rate"),rs.getFloat("adrate"));
			}
		}
		catch (Exception e)
		{
			log.error("SQLException:"+sql);
			e.printStackTrace();
		}
		finally
		{
			MySQLUtil.freeConnection(rs,stmt,con);
		}
		return ret;
	}
	
	public static int getAvgVol(String code,String date)
	{
		ResultSet rs = null;
		Connection con = null;
		Statement stmt = null;
	
		int ret=0;
		String sql = "select adjavgvol90d from avgvol where code='"+code+"' and date='"+date+"'";
		try
		{
			con = MySQLUtil.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			if(rs.next())
			{
				ret=rs.getInt("adjavgvol90d");
			}
			else
			{
				sql = "";
			}
		}
		catch (Exception e)
		{
			log.error("SQLException:"+sql);
			e.printStackTrace();
		}
		finally
		{
			MySQLUtil.freeConnection(rs,stmt,con);
		}
		return ret;
	}
	
	public static int put(String sql)
	{
		Connection con = null;
		Statement stmt = null;
		int ret = 0;
		try
		{
			con = MySQLUtil.getConnection();
			stmt = con.createStatement();
			ret = stmt.executeUpdate(sql);
		}
		catch (Exception e)
		{
			log.error("SQLException:"+sql);
			e.printStackTrace();
		}
		finally
		{
			MySQLUtil.freeConnection(null,stmt,con);
		}
		return ret;
	}
	
	public static void main(String[] args)
	{
		log.trace("main");
	}
}
