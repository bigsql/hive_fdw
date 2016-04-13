import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HiveJdbcClient {

    private static final String url      = "jdbc:hive2://192.168.1.97:10000";
    private static final String user     = "";
    private static final String password = "";
    private static final String query    = "SHOW DATABASES";

    private static final String driverName = "org.apache.hive.jdbc.HiveDriver";

    public static void main(String[] args) throws SQLException {

        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }

        Connection con = DriverManager.getConnection(url, user, password);
        Statement stmt = con.createStatement();

        System.out.println("Running: " + query);
        ResultSet res = stmt.executeQuery(query);

        while (res.next()) {
            System.out.println(res.getString(1));
        }
    }
}
