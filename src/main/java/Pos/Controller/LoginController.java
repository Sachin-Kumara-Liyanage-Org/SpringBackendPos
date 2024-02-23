package Pos.Controller;

import Pos.Gui.LoginForm;
import Pos.Gui.MainManuForm;
import Pos.Gui.ShowMessage;
import Pos.Main.MainStarter;
import Pos.Model.User;
import Pos.Service.UserService;
import org.apache.log4j.Logger;

import java.sql.SQLException;

public class LoginController {

    private LoginForm loginForm;
    private final Logger logger = Logger.getLogger(LoginController.class.getName());
    public static User user = null;

    public LoginController(LoginForm loginForm) {
        this.loginForm = loginForm;
    }

    public LoginController() {
    }

    public Boolean checkLogin() throws SQLException {

        LoginController.user = new UserService().checkLogin(loginForm.getUname().getText(), String.valueOf(loginForm.getPassword().getPassword()));
        if (LoginController.user != null) {
            logger.info("User login id: " + LoginController.user.getId());
            this.fullClear();
            new UserService().addLoginLog(LoginController.user.getId(),1);
            if(LoginController.user.getRank()==1){
                    MainStarter.mainFrame.getContentPane().removeAll();
                    MainStarter.mainFrame.setContentPane(new MainManuForm().getMainManuPane());
                    MainStarter.mainFrame.getContentPane().setVisible(false);
                    MainStarter.mainFrame.getContentPane().setVisible(true);

            }
            return true;
        }

        logger.warn("Can't find User try again ");
        ShowMessage.ShowWorn("Can't Find User Try Again ");
        this.fullClear();
        return false;
    }

    public void fullClear() {
        this.passwordClear();
        this.unameClear();
    }

    public void passwordClear() {
        this.loginForm.getPassword().setText("");
    }

    public void unameClear() {
        this.loginForm.getUname().setText("");
    }

    public void Logout() throws SQLException {
        new UserService().addLoginLog(LoginController.user.getId(),0);
        logger.info("User logout id: " + LoginController.user.getId());
        MainStarter.mainFrame.getContentPane().removeAll();
        MainStarter.mainFrame.setContentPane(new LoginForm().getLoginPane());
        MainStarter.mainFrame.getContentPane().setVisible(false);
        MainStarter.mainFrame.getContentPane().setVisible(true);
        LoginController.user=null;

    }
}
