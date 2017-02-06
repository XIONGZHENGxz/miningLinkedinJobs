package uta.shan.mining.createDataBase;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.io.FileInputStream;
import java.util.Properties;
import java.io.FileNotFoundException;
import java.io.IOException;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;

public class DataBase{
	//use data source 
	private String property_path;
	private String name;
	private Properties props;
	private List<String> tables;
	public DataBase(String path,String name,List<String> tabs){
		this.property_path=path;
		this.name=name;
		this.tables=tabs;
	}
	public Properties  getProps(String file){
		props=new Properties();
		try{
			FileInputStream fis=new FileInputStream(file);
			props.load(fis);
		} catch(FileNotFoundException e){
			e.printStackTrace();
		} catch(IOException ex){
			ex.printStackTrace();
		}
		return props;
	}
		
	public MysqlDataSource getDataSource(){
		MysqlDataSource mds=null;
		try{
			mds=new MysqlConnectionPoolDataSource();
			mds.setURL(props.getProperty("db.url"));
			mds.setUser(props.getProperty("db.user"));
			mds.setPassword(props.getProperty("db.password"));
		} catch(IOException ex){
			ex.printStackTrace();
		}
		return mds;
	}
	
	//create a database
	public void CreateDB(){
		Connection con=null;
		PreparedStatement st=null;
		try{
			MysqlDataSource mds=getDataSource();
			con=mds.getConnection();
			String query="CREATE DATABASE "+this.name;
			st=con.prepareStatement();
			st.executeQuery();
			this.props.setProperty("db.url",props.getProperty("db.url")+this.name);
		} catch(Exception e){
			Logger logger=Logger.getLogger(DataBase.class.getName());
			logger.log(Level.SEVERE,e.getMessage(),e);
		} finally{
			try{
				if(st!=null) st.close();
				if(con!=null) con.close();
			} catch(SQLException e){
				Logger logr=Logger.getLogger(DataBase.class.getName());
				logr.log(Level.WARNING,e.getMessage(),e);
			}
		}
	}

	//execute a query
	public List<ResultSet> ExecuteQueries(List<String> query){
		Connection con=null;
		PreparedStatement st=null;
		List<ResultSet> rs=new ArrayList<>();
		try{
			MysqlDataSource mds=this.getDataSource();
			con=mds.getConnection();
			for(String q:query){
				st=con.prepareStatement();
				rs.add(st.executeQuery());
			}
		} catch(Exception e){
			Logger logger=Logger.getLogger(DataBase.class.getName());
			logger.log(Level.SEVERE,e.getMessage(),e);
		} finally{
			try{
				if(st!=null) st.close();
				if(con!=null) con.close();
			} catch(SQLException e){
				Logger logr=Logger.getLogger(DataBase.class.getName());
				logr.log(Level.WARNING,e.getMessage(),e);
			}
		}
		return rs;
	}

	//execute query 
	public ResultSet ExecuteQuery(String query){
		Connection con=null;
		PreparedStatement st=null;
		ResultSet rs=null;
		try{
			MysqlDataSource mds=this.getDataSource();
			con=mds.getConnection();
			st=con.prepareStatement(query);
			rs=st.executeQuery();
		} catch(Exception e){
			Logger logger=Logger.getLogger(DataBase.class.getName());
			logger.log(Level.SEVERE,e.getMessage(),e);
		} finally{
			try{
				if(st!=null) st.close();
				if(con!=null) con.close();
				if(rs!=null) rs.close();
			} catch(SQLException e){
				Logger logr=Logger.getLogger(DataBase.class.getName());
				logr.log(Level.WARNING,e.getMessage(),e);
			}
		}
		return rs;
	}

	//execute a transaction 
	public void ExecuteTransac(List<String> queries){
		Connection con=null;
		Statement st=null;
		try{
			MysqlDataSource mds=this.getDataSource();
			con=mds.getConnection();
			st=con.createStatement();
			con.setAutoCommit(false);
			for(String query:queries){
				st.executeUpdate(query);
			}
			con.commit();
		} catch(Exception e){
			Logger logger=Logger.getLogger(DataBase.class.getName());
			logger.log(Level.SEVERE,e.getMessage(),e);
		} finally{
			try{
				if(st!=null) st.close();
				if(con!=null) con.close();
			} catch(SQLException e){
				Logger logr=Logger.getLogger(DataBase.class.getName());
				logr.log(Level.WARNING,e.getMessage(),e);
			}
		}
	}
	
	//create tables
	public void CreateTables(){
		for(String table:this.tables){
			this.ExecuteQuery("CREATE TABLE"+table);
		}
	}

	//build database 
	public void BuildDB(String table,List<String> records){
		for(String record:records){
			this.ExecuteQuery("INSERT INTO "+table +record);
		}
	}
}

