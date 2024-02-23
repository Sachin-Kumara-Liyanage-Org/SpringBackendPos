package Pos.Service;

import Pos.Controller.PropertiesReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import Pos.Gui.ShowMessage;
import org.apache.log4j.Logger;

public class DBService {
    private final Logger logger = Logger.getLogger(DBService.class.getName());
    private static Connection con=null;
    private static DBService dbService=null;
    private DBService() {

    }

    public static DBService get_DBservice(){
        try {
            if(DBService.dbService==null || DBService.con==null) {
                DBService.dbService = new DBService();
                DBService.dbService.logger.info("Create new db con object");
                String sqlurl="jdbc:mysql://"+
                        PropertiesReader.getPropertisReader().getPropValues("dbHost")+
                        ":"+
                        PropertiesReader.getPropertisReader().getPropValues("dbPort")+
                        "/"+
                        PropertiesReader.getPropertisReader().getPropValues("dbName")+
                        "?useUnicode="+
                        PropertiesReader.getPropertisReader().getPropValues("dbuseUnicode")+
                        "&useJDBCCompliantTimezoneShift="+
                        PropertiesReader.getPropertisReader().getPropValues("dbuseJDBCCompliantTimezoneShift")+
                        "&useLegacyDatetimeCode="+
                        PropertiesReader.getPropertisReader().getPropValues("dbuseLegacyDatetimeCode")+
                        "&serverTimezone="+
                        PropertiesReader.getPropertisReader().getPropValues("dbserverTimezone")
                        ;
//                Class.forName("com.mysql.jdbc.Driver");
                DBService.con = DriverManager.getConnection(
                        sqlurl,
                        PropertiesReader.getPropertisReader().getPropValues("dbUname"),
                        PropertiesReader.getPropertisReader().getPropValues("dbPassword"));

            }
            return DBService.dbService;
        } catch ( SQLException | IOException e) {
            DBService.dbService.logger.error(e.toString());
            ShowMessage.ShowError(e.toString());
        }

        return null;
    }

    public Connection getCon() {
        return DBService.con;
    }
}
