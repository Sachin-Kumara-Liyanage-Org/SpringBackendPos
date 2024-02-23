package Pos.Gui;

import Pos.Controller.LoginController;
import Pos.Main.MainStarter;
import Pos.Service.AllShortcuts;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;
import jiconfont.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class LoginForm implements AllShortcuts {
    private JPanel LoginPane;
    private JLabel companyName;
    private JTextField uname;
    private JPasswordField password;
    private JButton login_btn;
    private JLabel unameLable;
    private JLabel passwordLable;
    private JButton logout;
    private JButton home;
    public static LoginController loginController=null;


    public LoginForm() {

        loginController=new LoginController(this);

//        uname.setText("sachin");
//        password.setText("123");

        IconFontSwing.register(FontAwesome.getIconFont());
        unameLable.setIcon(IconFontSwing.buildIcon(FontAwesome.USER, 32));
        passwordLable.setIcon(IconFontSwing.buildIcon(FontAwesome.LOCK, 32));
        home.setIcon(IconFontSwing.buildIcon(FontAwesome.WINDOW_MINIMIZE, 40,new Color(255, 255, 255)));
        logout.setIcon(IconFontSwing.buildIcon(FontAwesome.TIMES, 40,new Color(255, 255, 255)));
        login_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    loginController.checkLogin();
                } catch (SQLException throwables) {
                }
            }
        });

        //create shortcuts
        KeyboardFocusManager keyManager=KeyboardFocusManager.getCurrentKeyboardFocusManager();
        keyManager.removeKeyEventDispatcher(MainManu);
        keyManager.removeKeyEventDispatcher(StockShortcut);
        keyManager.removeKeyEventDispatcher(PosShortcut);
        keyManager.addKeyEventDispatcher(Login);

        uname.requestFocus();
        logout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                MainStarter.mainFrame.dispose();
            }
        });
        home.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                MainStarter.mainFrame.setState(JFrame.ICONIFIED);
            }
        });
    }

    public JPanel getLoginPane() {
        return this.LoginPane;
    }

    public JTextField getUname() {
        return this.uname;
    }

    public void setUname(JTextField uname) {
        this.uname = uname;
    }

    public JPasswordField getPassword() {
        return password;
    }

    public void setPassword(JPasswordField password) {
        this.password = password;
    }

}
