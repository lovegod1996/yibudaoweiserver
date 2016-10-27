package dbUnit;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

	private final String drive="com.mysql.jdbc.Driver";
	 private final String url="jdbc:mysql://localhost:3306/jdbc?";  
	 private final String user="root";  
	 private final String password="root";
	
	public Connection connect() throws Exception{
		Class.forName(drive);
		Connection conn = DriverManager.getConnection(url,user,password);
       //Statement stmt=conn.createStatement();
       return conn;
	}
	public ResultSet select(Connection conn,String sql) throws Exception{
		if(conn==null){
			conn = DriverManager.getConnection(url,user,password);
		}
		Statement stmt=conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);// executeQuery会返回结果的集合，否则返回空值
		return rs;
	}
	public int delete(Connection conn,String sql) throws Exception{
		if(conn==null){
			conn = DriverManager.getConnection(url,user,password);
		}
		Statement stmt=conn.createStatement();
		int result = stmt.executeUpdate(sql);
		return result;
	}
	public int update(Connection conn,String sql) throws Exception{
		if(conn==null){
			conn = DriverManager.getConnection(url,user,password);
		}
		Statement stmt=conn.createStatement();
		int result = stmt.executeUpdate(sql);
		return result;
	}
	public int insert(Connection conn,String sql) throws Exception{
		if(conn==null){
			conn = DriverManager.getConnection(url,user,password);
		}
		Statement stmt=conn.createStatement();
		int result = stmt.executeUpdate(sql);
		return result;
	}
	/*public ResultSet select(Statement stmt,String pnum) throws SQLException{
		String sql="select Pasw from PerInfor where Pnum="+pnum;
		ResultSet rs = stmt.executeQuery(sql);// executeQuery会返回结果的集合，否则返回空值
		return rs;
	}*/
	/*public int insert(Statement stmt,String pnum,String pasw,String name,String cnum) throws SQLException{
		String sql="insert into PerInfor(Pnum,Pasw,Name,Cnum) values('"+pnum+"','"+pasw+"','"+name+"','"+cnum+"')";
		int result = stmt.executeUpdate(sql);
		return result;
	}*/

       
	

}
