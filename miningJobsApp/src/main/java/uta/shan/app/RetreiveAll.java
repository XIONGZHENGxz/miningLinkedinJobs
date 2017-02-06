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
import java.io.IOException;

public class RetreiveAll{
	public static void main(String...args){
		Connection con=null;
		PreparedStatement st=null;
		PreparedStatement st2=null;
		Properties property=new Properties();
		ResultSet rs=null;
		FileInputStream fis=null;
		try{
			fis=new FileInputStream(args[0]);
			property.load(fis);
			Class.forName(property.getProperty("db.driver"));
			String url=property.getProperty("db.url");
			String user=property.getProperty("db.user");
			String password=property.getProperty("db.password");
			con=DriverManager.getConnection(url,user,password);
			st=con.prepareStatement("INSERT INTO Testing(Id) VALUES(?)");
			st2=con.prepareStatement("SELECT * FROM Authors");
			rs=st2.executeQuery();
			while(rs.next()){
				System.out.println(rs.getInt(1)+": ");
				System.out.println(rs.getString(2));
			}
			for(int i=0;i<50;i++){
				st.setInt(1,2*i);
				st.executeUpdate();
			}
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

