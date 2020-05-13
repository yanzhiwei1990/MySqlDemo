package MysqlDemo;

import java.sql.*;

import com.mysql.cj.jdbc.Driver;

public class DbManager {
    
	public static final String TAG = DbManager.class.getSimpleName() + " : %s\n";
	
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String JDBC_NEW_DRIVER = "com.mysql.cj.jdbc.Driver";
    
    //String url="jdbc:mysql://localhost:3306/javaTest?useUnicode=true&characterEncoding=UTF8";
    private static final String DB_HEAD = "jdbc:mysql://";
    private static final String DB_NAME = "test_db";

    private static final String USER = "root";
    private static final String PASS = "$Zhiwei.Yan+@1990#";

    private Connection mConnection = null;
    private String mAddress = null;
    private int mPort = -1;
    private String mDbName = null;
    private String mTable = null;
    private String mUser = null;
    private String mPassCode = null;
    
	public DbManager(String address, int port, String dbname, String table, String user, String passcode) {
		setFullParameter(address, port, dbname, table, user, passcode);
	}
	
	public DbManager() {
		setFullParameter("temptry.top", 3306, "test_db", "test_table1", "root", "$Zhiwei.Yan+@1990#");
	}
	
	private void setFullParameter(String address, int port, String dbname, String table, String user, String passcode) {
		mAddress = address;
		mPort = port;
		mDbName = dbname;
		mTable = table;
		mUser = user;
		mPassCode = passcode;
	}
	
	private boolean isParameterFull() {
		boolean result = false;
		if (mAddress != null && mAddress.length() > 0 && mPort != -1 &&
				mDbName != null && mDbName.length() > 0 &&
				mTable != null && mTable.length() > 0 &&
			    mUser != null && mUser.length() > 0 &&
				mPassCode != null && mPassCode.length() > 0) {
			result = true;
		}
		return result;
	}
	
	private String getDbUrl() {
		return DB_HEAD + mAddress + ":" + mPort + "/" + mDbName + "?useUnicode=true&characterEncoding=UTF8";
	}
	
	public boolean startConnect() {
		boolean result = false;
		try {
			if (isParameterFull()) {
				mConnection = (Connection)DriverManager.getConnection(getDbUrl(), mUser, mPassCode);
		        if(mConnection != null && !mConnection.isClosed()){
		        	result = true;
		            Log.PrintLog(TAG, "startConnect sucessful");
		        } 
			}
	    } catch (Exception e) {
	    	Log.PrintError(TAG, "startConnect Exception:" + e.getMessage());
	    }
		return result;
	}
	
	public void closeConnect() {
	    try {
	        if (mConnection != null) {
	    	    mConnection.close(); 
	        }
	    } catch(Exception e){
	    	Log.PrintError(TAG, "closeConnect Exception:" + e.getMessage());
	    }
	}

	public void insert(String name,int age,String gender) {
		String sql="insert into test_table1(name,age,gender) values(?,?,?)";
	    //String sql="insert into usrInfo(age,gender,username) values('"+age+"','"+gender+"','"+name+"')";
		PreparedStatement preStmt = null;
	    try {
	        preStmt = (PreparedStatement)mConnection.prepareStatement(sql);
            preStmt.setString(1, name);
            preStmt.setInt(2, age);
            preStmt.setString(3, gender);
            
	        preStmt.executeUpdate();
	        preStmt.close();
	        //Log.PrintLog(TAG, "insert sucessful");
	    } catch(Exception e) {
	    	Log.PrintError(TAG, "add Exception:" + e.getMessage());
	    }
	    try {
	    	if (preStmt != null) {
	    		preStmt.close();
	    	}
	    } catch (Exception e) {
	    	Log.PrintError(TAG, "insert close Exception:" + e.getMessage());
		}
	}
	  
	public void select(){
	    String sql="select * from test_table1";
	    Statement stmt = null;
	    ResultSet result = null;
	    try {
            stmt = (Statement)mConnection.createStatement();
            result = (ResultSet)stmt.executeQuery(sql);
            Log.PrintLog(TAG, "--------------------------------");
            Log.PrintLog(TAG, "id\t" + "姓名"+"\t"+"年龄"+"\t"+"性别");
            Log.PrintLog(TAG, "--------------------------------");
            int id = -1;
	        String name = null;
            int age = -1;
            String gender = null;
	        while(result.next()){
	        	id = result.getInt("id");
	            name = result.getString("name");
	            age = result.getInt("age");
	            gender = result.getString("gender");
	            Log.PrintLog(TAG, id + "\t" + name+"\t"+age+"\t"+gender);
	        }
	    } catch(Exception e) {
	    	Log.PrintError(TAG, "select Exception:" + e.getMessage());
	    }
	    try {
	    	if (stmt != null) {
	    		stmt.close();
	    	}
	    } catch (Exception e) {
	    	Log.PrintError(TAG, "select close Exception:" + e.getMessage());
		}
	}
	  
	public void update(String name,int age){
	    String sql="update test_table1 set age=? where name=?";
	    //String sql="update usrInfo set age="+age+" where username='"+name+"'";
	    PreparedStatement prestmt = null;
	    try {
	        prestmt = (PreparedStatement)mConnection.prepareStatement(sql);
	        prestmt.setInt(1, age);
	        prestmt.setString(2,name);
	        prestmt.executeUpdate();
	        //Log.PrintLog(TAG, "update sucessful");
	    } catch(Exception e) {
	    	Log.PrintError(TAG, "update Exception:" + e.getMessage());
	    }
	    try {
	    	if (prestmt != null) {
	    		prestmt.close();
	    	}
	    } catch (Exception e) {
	    	Log.PrintError(TAG, "update close Exception:" + e.getMessage());
		}
	}

	public void delete(String name){
	    String sql="delete from test_table1 where name=?";
	    PreparedStatement prestmt = null;
	    try{
	        prestmt = (PreparedStatement)mConnection.prepareStatement(sql);
	        prestmt.setString(1, name);
	        prestmt.executeUpdate();
	        //Log.PrintLog(TAG, "delete sucessful");
	    } catch(Exception e) {
	    	Log.PrintError(TAG, "delete Exception:" + e.getMessage());
	    }
	    try {
	    	if (prestmt != null) {
	    		prestmt.close();
	    	}
	    } catch (Exception e) {
	    	Log.PrintError(TAG, "delete close Exception:" + e.getMessage());
		}
	}
	
	public void dropTable() {
		String sql="drop table test_table1";
		PreparedStatement prestmt = null;
	    try{
	        prestmt = (PreparedStatement)mConnection.prepareStatement(sql);
	        prestmt.executeUpdate();
	        //Log.PrintLog(TAG, "dropTable sucessful");
	    } catch(Exception e) {
	    	Log.PrintError(TAG, "dropTable Exception:" + e.getMessage());
	    }
	    try {
	    	if (prestmt != null) {
	    		prestmt.close();
	    	}
	    } catch (Exception e) {
	    	Log.PrintError(TAG, "dropTable close Exception:" + e.getMessage());
		}
	}
	
	public void createTable() {
		String sql = "select table_name FROM information_schema.TABLES WHERE table_name = 'test_table1'";
		PreparedStatement prestmt = null;
		ResultSet result = null;
		boolean exist = false;
	    try{
	        prestmt = (PreparedStatement)mConnection.prepareStatement(sql);
	        result = (ResultSet)prestmt.executeQuery(sql);
	        //Log.PrintLog(TAG, "createTable check sucessful");
	    } catch(Exception e) {
	    	Log.PrintError(TAG, "createTable check Exception:" + e.getMessage());
	    }
	    if (result != null) {
	    	try {
	    		exist = result.next();
			} catch (Exception e) {
				Log.PrintError(TAG, "createTable check exist Exception:" + e.getMessage());
			}
	    }
	    try {
	    	if (prestmt != null) {
	    		prestmt.close();
	    	}
	    } catch (Exception e) {
	    	Log.PrintError(TAG, "createTable check close Exception:" + e.getMessage());
		}

	    if (exist) {
	    	Log.PrintLog(TAG, "createTable check existed");
	    } else {
	    	Log.PrintLog(TAG, "createTable check need create");
	    	
	    	sql = "create table test_table1(id int NOT NULL AUTO_INCREMENT PRIMARY KEY, name varchar(255), age int(255), gender varchar(255))";/*id integer(255), */
		    try{
		        prestmt = (PreparedStatement)mConnection.prepareStatement(sql);
		        prestmt.executeUpdate();
		        Log.PrintLog(TAG, "createTable sucessful");
		    } catch(Exception e) {
		    	Log.PrintError(TAG, "createTable Exception:" + e.getMessage());
		    }
	    }
	    try {
	    	if (prestmt != null) {
	    		prestmt.close();
	    	}
	    } catch (Exception e) {
	    	Log.PrintError(TAG, "createTable close Exception:" + e.getMessage());
		}
	}
	
	public void deleteAll(){
	    String sql="delete from test_table1";
	    PreparedStatement prestmt = null;
	    try{
	        prestmt = (PreparedStatement)mConnection.prepareStatement(sql);
	        prestmt.executeUpdate();
	        //Log.PrintLog(TAG, "deleteAll sucessful");
	    } catch(Exception e) {
	    	Log.PrintError(TAG, "deleteAll Exception:" + e.getMessage());
	    }
	    try {
	    	if (prestmt != null) {
	    		prestmt.close();
	    	}
	    } catch (Exception e) {
	    	Log.PrintError(TAG, "deleteAll close Exception:" + e.getMessage());
		}
	}
}
