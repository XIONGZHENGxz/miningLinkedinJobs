import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import uta.shan.mining.createDataBase.DataBase;
public class TestDataBase{
	private final static String property_path=System.getProperty("property_path");
	private final static String fields="(Id INT PRIMARY KEY NOT NULL,title VARCHAR(20) NOT NULL)";
	private DataBase db;

	@Before
	public void initDB(){
		db=new DataBase(property_path,"testdb1","testjob",fields);
	}

	@Test
	public void testDB(){
		assertNotNull(db);
	}

	@Test
	public void testInit(){
		assertTrue(db.name.equals("testdb1"));
		assertTrue(db.table.equals("testjob"));
		assertTrue(db.fields.equals(fields));
	}

	@Test
	public void testGetProps(){
		db.getProps();
		assertNotNull(db.props);
	}

	@Test 
	public void testGetDataSource(){
		assertNotNull(db.getDataSource());
	}

	@Test
	public void testCreateDB(){
		db.createDB();
		MysqlDataSource mds=db.getDataSource();
		Connection con=null;
		try{
			con=mds.getConnection();
		} catch(Exception e){
			e.printStackTrace();
		}
		assertNotNull(con);
	}

	@After
	public void clean(){
		MysqlDataSource mds=db.getDataSource();
		Connection con=null;
		PreparedStatement pst=null;
		try{
			con=mds.getConnection();
			pst=con.prepareStatement("DROP DATABASE "+db.name);
			pst.execute();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}

