package Cloud.Server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class SQLConnect {
    private static Connection connection;
    private static Statement statement;
    private static Logger logger = LoggerFactory.getLogger(SQLConnect.class);



    SQLConnect(){
        try {
            connect();

            createBD();

            createTestUser();

            checkAvtorisation("LoginTest","taram pam pam");
            checkAvtorisation("1LoginTest","taram pam pam");

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            disconnect();
        }
    }


    private static void createTestUser() {
        try {
            statement.executeUpdate("INSERT INTO Users (User,Pass) VALUES ('LoginTest','taram pam pam');");
            logger.info("Tests user create in BD");

        } catch (SQLException e) {
            logger.error("Tests user already exist in BD");

        }
    }

    private static void createBD() {
        try {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Users (" +
                    "ID   INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL, " +
                    "User STRING  UNIQUE NOT NULL, Pass STRING  NOT NULL);");
            logger.info("BD create successful");

        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Cannot create BD");
        }
    }

    public static boolean checkAvtorisation(String login,String pass) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT ID,User,Pass FROM Users WHERE User = ? AND Pass = ?;");
            ps.setString(1,login);
            ps.setString(2,pass);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                //System.out.println(rs.getString(1) + " " +
                //        rs.getString(2) + " " +
                //        rs.getString(3));
                logger.info("User "+login+" authorized");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getSQLState());
        }
        logger.error("User "+login+" cannot authorized");
        return false;
    }


    public static void connect() throws Exception{
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:Server.db");
        statement = connection.createStatement();

        logger.info("Error while connect to BD: "+connection.isClosed());
    }

    private static void disconnect(){
        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
