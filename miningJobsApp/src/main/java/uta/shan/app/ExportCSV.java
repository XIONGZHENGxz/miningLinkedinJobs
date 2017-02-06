package uta.shan.app;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;

public class ExportCSV{
	//us edata source 
	public static 	MysqlDataSource getDataSource(String path){
		MysqlDataSource mds=null;
		Properties props=new Properties();
		try{
			FileInputStream fis=new FileInputStream(path);
			props.load(fis);
			mds=new MysqlConnectionPoolDataSource();
			mds.setURL(props.getProperty("db.url"));
			mds.setUser(props.getProperty("db.user"));
			mds.setPassword(props.getProperty("db.password"));
		} catch(FileNotFoundException e){
			e.printStackTrace();
		} catch(IOException ex){
			ex.printStackTrace();
		}
		return mds;
	}

	public static void main(String...args){
		Connection con=null;
		PreparedStatement st=null;
		try{
			MysqlDataSource mds=getDataSource(args[0]);
			con=mds.getConnection();
			String query="SELECT Name,Title INTO OUTFILE '/tmp/author_book.csv'"
			+" FIELDS TERMINATED BY ',' FROM Authors,Books WHERE Authors.Id=Books.AuthorId";
			st=con.prepareStatement(query);
			st.execute();
		} catch(Exception e){
			Logger logger=Logger.getLogger(Version.class.getName());
			logger.log(Level.SEVERE,e.getMessage(),e);
		} finally{
			try{
				if(st!=null) st.close();
				if(con!=null) con.close();
			} catch(SQLException e){
				Logger logr=Logger.getLogger(Version.class.getName());
				logr.log(Level.WARNING,e.getMessage(),e);
			}
		}
	}
}

