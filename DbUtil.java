import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class DBUtil {
	
	String HOSTNAME = "127.0.0.1";
	String dbUser = "root";
	String dbPassword = "root";
	String productionDBName  = "proddb";
	String backupDBName  = "backupdb";
	String tableName = "patient_follow_up";

	public static void main(String[] args) {
		DBUtil dBUtil = new DBUtil();
		dBUtil.updateDB();
	}
	
	private void updateDB(){
		System.out.println("updateDB ENTER");
		///
		Connection productionDBConnection=null;
		Connection backupDBConnection=null;
		PreparedStatement stProduction = null;
		PreparedStatement stUpdateProduction = null; 
		ResultSet rsProduction = null;
		ResultSet rsBackup = null;
		ResultSetMetaData metaProduction = null;
		PreparedStatement stBackup = null; 
		//-9999 9999
		try{
			productionDBConnection = createConnection(productionDBName, dbUser, dbPassword);
			backupDBConnection = createConnection(backupDBName, dbUser, dbPassword);
			String selectQuery = "select * from "+tableName +" where id = ? ";
			String selectAllQuery = "select * from "+tableName;
			//String updateQuery = "update table "+tableName ;
			stProduction = productionDBConnection.prepareStatement(selectAllQuery);
			rsProduction = stProduction.executeQuery();
			//rsBackup = stProduction.executeQuery();
			metaProduction = rsProduction.getMetaData();
			int colCount = metaProduction.getColumnCount();
			stBackup = backupDBConnection.prepareStatement(selectQuery);
			while(null !=rsProduction && rsProduction.next()){
				System.out.println("CHECKING FOR ID:"+rsProduction.getObject("id"));
				for(int col=1;col<=colCount;col++){
					Object val = rsProduction.getObject(col);
					String columnName  = metaProduction.getColumnName(col);
					if(null != val){
						String valStr = ""+val;
						if("9999".equals(valStr) || "-9999".equals(valStr)|| "-777".equals(valStr) ){
							System.out.print("Value is  :"+valStr); 
							stBackup.setObject(1, rsProduction.getObject("id"));
							rsBackup = stBackup.executeQuery();
							if(null != rsBackup && rsBackup.next()){
								Object valueBackup = rsBackup.getObject(col);
								String valBackupStr = ""+valueBackup;
								if(!("9999".equals(valBackupStr) || "-9999".equals(valBackupStr)|| "-777".equals(valStr)) ){
									String updateQuery = "update table "+tableName + "set "+columnName + "=? where id =" +rsProduction.getObject("id");
									System.out.println("UPDATE QUERY:"+updateQuery);
									stUpdateProduction= productionDBConnection.prepareStatement(updateQuery);
									stUpdateProduction.setObject(1,valueBackup );
									stUpdateProduction.executeUpdate();
								}
							}
						}
					}
				}
				System.out.println();
			}
			System.out.println("updateDB DONE");
		}catch(Exception e){ 
			System.out.println("updateDB ERROR:");
			e.printStackTrace();
		}finally{
			try {
				productionDBConnection.close();
				backupDBConnection.close();
			} catch (SQLException e) {}
		}
	}


	private Connection createConnection(String dbName,String dbUser,String dbPassword) throws Exception{
		Connection con=null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://"+HOSTNAME+"/"+dbName,dbUser,dbPassword);
		}catch(Exception e){ 
			System.out.println("Error  While Creating connection to database");
			e.printStackTrace();
			throw e;
		}
		return con;
	}
}
