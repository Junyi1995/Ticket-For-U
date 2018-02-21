package db;

// import always java.sql
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

// reset your database table
public class MySQLTableCreation {
	// Run this as Java application to reset db schema.
	public static void main(String[] args) {
		try {
			Connection conn = null;
			// Step 1 Connect to MySQL.
			try {
				System.out.println("Connecting to \n" + MySQLDBUtil.URL);
				conn = DriverManager.getConnection(MySQLDBUtil.URL);//connect web
			} catch (SQLException e) { // handle exception
				System.out.println("SQLException " + e.getMessage());
				System.out.println("SQLState " + e.getSQLState());
				System.out.println("VendorError " + e.getErrorCode());
			}
			if (conn == null) { // if there is no connection
				return;
			}
			// Step 2 Drop tables in case they exist.
			Statement stmt = conn.createStatement();
			String sql = "DROP TABLE IF EXISTS history";
			stmt.executeUpdate(sql);
			sql = "DROP TABLE IF EXISTS categories";
			stmt.executeUpdate(sql);
			sql = "DROP TABLE IF EXISTS items";
			stmt.executeUpdate(sql);
			sql = "DROP TABLE IF EXISTS users";
			stmt.executeUpdate(sql); // 执行语言上有区别
						
			// Step 3. Create new tables.
			// 每个加号代表一列
			sql = "CREATE TABLE items " + "(item_id VARCHAR(255) NOT NULL, " + " name VARCHAR(255), "
					+ "city VARCHAR(255), " + "state VARCHAR(255), " + "country VARCHAR(255), "
					+ "zipcode VARCHAR(255), " + "rating FLOAT," + "address VARCHAR(255), " + "latitude FLOAT, "
					+ " longitude FLOAT, " + "description VARCHAR(255), " + "image_url VARCHAR(255),"
					+ "url VARCHAR(255)," + "distance FLOAT," + " PRIMARY KEY ( item_id ))";
			stmt.executeUpdate(sql);
			sql = "CREATE TABLE categories " + "(item_id VARCHAR(255) NOT NULL, " + " category VARCHAR(255), "
					+ " PRIMARY KEY ( item_id, category), " + "FOREIGN KEY (item_id) REFERENCES items(item_id))";
			//以后可以通过item_id查找到这个table，以及哪一列作为primary key
			stmt.executeUpdate(sql);
			sql = "CREATE TABLE users " + "(user_id VARCHAR(255) NOT NULL, " + " password VARCHAR(255) NOT NULL, "
					+ " first_name VARCHAR(255), last_name VARCHAR(255), " + " PRIMARY KEY ( user_id ))";
			stmt.executeUpdate(sql);
			sql = "CREATE TABLE history " + "(user_id VARCHAR(255) NOT NULL, " + " item_id VARCHAR(255) NOT NULL, "
					+ " last_favor_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, "
					+ " PRIMARY KEY (item_id, user_id), " + " FOREIGN KEY (item_id) REFERENCES items(item_id), "
					+ " FOREIGN KEY (user_id) REFERENCES users(user_id))";
			stmt.executeUpdate(sql);
			
			// Step 4: insert data
			// Create a fake user
			sql = "INSERT INTO users " + "VALUES (\"1111\", \"3229c1097c00d497a0fd282d586be050\", \"John\", \"Smith\")";
			System.out.println("Executing query:\n" + sql);
			stmt.executeUpdate(sql);

			System.out.println("Import is done successfully.");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
