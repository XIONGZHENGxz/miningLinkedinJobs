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
	public  String property_path;
	public  String name;
	public  Properties props;
	public  String table;
	public  String fields;
	public DataBase(String path,String name,String table,String fields){
		this.property_path=path;
		this.name=name;
		this.getProps();
		this.table=table;
		this.fields=fields;
		this.createDB();
	}
	public void getProps(){
		props=new Properties();
		try{
			FileInputStream fis=new FileInputStream(this.property_path);
			props.load(fis);
		} catch(FileNotFoundException e){
			e.printStackTrace();
		} catch(IOException ex){
			ex.printStackTrace();
		}
	}
		
	public MysqlDataSource getDataSource(){
		MysqlDataSource mds=null;
		mds=new MysqlConnectionPoolDataSource();
		mds.setURL(props.getProperty("db.url"));
		mds.setUser(props.getProperty("db.user"));
		mds.setPassword(props.getProperty("db.password"));
		return mds;
	}
	
	//create a database
	public void createDB(){
		Connection con=null;
		PreparedStatement st=null;
		try{
			MysqlDataSource mds=getDataSource();
			mds.setURL(mds.getURL()+"?useSSL=false");
			con=mds.getConnection();	
			String query="CREATE DATABASE IF NOT EXISTS "+this.name;
			st=con.prepareStatement(query);
			st.execute();
			st=con.prepareStatement("USE "+this.name);
			st.execute();
			st=con.prepareStatement("CREATE TABLE IF NOT EXISTS "+this.table+this.fields);
			st.execute();
			this.props.setProperty("db.url",props.getProperty("db.url")+"/"+this.name+"?useSSL=false");
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
				st=con.prepareStatement(q);
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

	//insert records
	public void insert(String statement){
		Connection con=null;
		PreparedStatement pst=null;
		try{
			MysqlDataSource mds=this.getDataSource();
			con=mds.getConnection();
			pst=con.prepareStatement(statement);
			pst.execute();
		} catch(Exception e){
			Logger logger=Logger.getLogger(DataBase.class.getName());
			logger.log(Level.SEVERE,e.getMessage(),e);
		} finally{
			try{	
				if(con!=null) con.close();
				if(pst!=null) pst.close();
			} catch(SQLException e){
				e.printStackTrace();
			}
		}
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


	//drop a database
	public void drop(){
		MysqlDataSource mds=this.getDataSource();
		PreparedStatement pst=null;
		Connection con=null;
		try{
			con=mds.getConnection();
			pst=con.prepareStatement("DROP DATABASE "+this.name);
			pst.execute();
		} catch(Exception e){
			e.printStackTrace();
		} finally{
			try{
				if(con!=null) con.close();
				if(pst!=null) pst.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
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

}

