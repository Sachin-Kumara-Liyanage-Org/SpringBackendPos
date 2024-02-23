package Pos.Service;

import Pos.Gui.ShowMessage;
import Pos.Model.User;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {
    private final Logger logger = Logger.getLogger(UserService.class.getName());

    public User checkLogin(String uname, String pass) throws SQLException {
        PreparedStatement stmt=null;
        ResultSet rs=null;
        try {
             stmt = DBService.get_DBservice().getCon()
                    .prepareStatement("select * from mst_user where Uname=? and Password=MD5(?) and Status=1 limit 1");
            stmt.setString(1, uname);
            stmt.setString(2, pass);
             rs = stmt.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt(rs.findColumn("Id")));
                user.setName(rs.getString(rs.findColumn("Name")));
                user.setUname(rs.getString(rs.findColumn("Uname")));
                user.setRank(rs.getInt(rs.findColumn("Rank")));
                return user;
            }
        } catch (SQLException | NullPointerException e) {
            logger.error(e.toString());
            ShowMessage.ShowError(e.toString());
        }finally {
            rs.close();
            stmt.close();

        }
        return null;
    }

    public void addLoginLog(int id,int type) throws SQLException {
        PreparedStatement stmt=null;
        try {
             stmt = DBService.get_DBservice().getCon()
                    .prepareStatement("insert into login_log(`Uid`,`Type`) values (?,?)");
            stmt.setInt(1,id);
            stmt.setInt(2,type);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.toString());
            ShowMessage.ShowError(e.toString());
        }finally {
            stmt.close();
        }
    }
}
