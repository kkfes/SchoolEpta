package bot;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBManager {

    public static final String DB = "school";
    private static final String USER = "root";
    private static final String PASS = "";

    public static Connection connection;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DB + "?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC", USER, PASS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
