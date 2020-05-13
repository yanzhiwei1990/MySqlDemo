package MysqlDemo;
 
public class MySqlDemo {
 
	public static final String TAG = MySqlDemo.class.getSimpleName() + " : %s\n";
	
	private DbManager mDbManager = null;
	
    public static void main(String[] args) {
    	MySqlDemo mySqlDemo = new MySqlDemo();
    	mySqlDemo.mDbManager = new DbManager();
    	mySqlDemo.test();
    }
    
    private void test() {
    	mDbManager.startConnect();
    	mDbManager.dropTable();
    	mDbManager.createTable();
    	mDbManager.select();
    	long current1 = System.currentTimeMillis();
    	long current2 = System.currentTimeMillis();
    	for (int i = 0; i < 10000; i++) {
    		mDbManager.insert("name" + i, i, i / 2 == 0 ? "male" : "female");
    		if (i % 50 == 0) {
    			Log.PrintLog(TAG, "test" + (current2 - current1));
    			current2 = System.currentTimeMillis();
    		}
    	}
    	Log.PrintLog(TAG, "test" + (System.currentTimeMillis() - current1));
    	//mDbManager.select();
    	/*for (int i = 5; i < 10; i++) {
    		mDbManager.insert("name" + i, i, i / 2 == 0 ? "male" : "female");
    	}
    	mDbManager.select();
    	for (int i = 5; i < 10; i++) {
    		mDbManager.update("name" + i, 5 - i);
    	}
    	mDbManager.select();*/
    	mDbManager.closeConnect();
    }
}