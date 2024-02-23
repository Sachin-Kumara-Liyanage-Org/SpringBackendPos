package Pos.Main;

import Pos.Controller.OsDataController;
import Pos.Controller.PropertiesReader;
import Pos.Gui.LoginForm;
import Pos.Gui.ShowMessage;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.IOException;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class MainStarter {
    private final Logger logger = Logger.getLogger(MainStarter.class.getName());
    public static int DisplayWidth;
    public static int DisplayHeight;
    public static JFrame mainFrame;

    public ImageIcon getimge() {
        return new ImageIcon(getClass().getClassLoader().getResource("logo.png"));
    }

    public static void main(String[] args) throws IOException {

        OsDataController os = new OsDataController();
        MainStarter.DisplayWidth = os.getDisplayWidth();
        MainStarter.DisplayHeight = os.getDisplayHeight();
        MainStarter ms = new MainStarter();
        ms.logger.info("DisplayHeight:" + MainStarter.DisplayHeight);
        ms.logger.info("DisplayWidth:" + MainStarter.DisplayWidth);
        String sn = os.getgetSerialNumber();
        ms.logger.info("PC serial No: " + sn);
        MainStarter.mainFrame = new JFrame(PropertiesReader.getPropertisReader().getPropValues("title"));
//        if (sn != null && PropertiesReader.getPropertisReader().getPropValues("serial").equalsIgnoreCase(sn)) {
//            ms.logger.info("reg using bios");
            MainStarter.mainFrame.setContentPane(new LoginForm().getLoginPane());
            MainStarter.mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            MainStarter.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            MainStarter.mainFrame.setUndecorated(Boolean.valueOf(PropertiesReader.getPropertisReader().getPropValues("fullScreen")));
            MainStarter.mainFrame.setIconImage(ms.getimge().getImage());
            MainStarter.mainFrame.setSize(1200,800);
            MainStarter.mainFrame.setVisible(true);
//        }else {
//            ms.logger.info("Serial Didn't Match:");
//            ShowMessage.ShowError(PropertiesReader.getPropertisReader().getPropValues("contactMessage"));
//            MainStarter.mainFrame.dispose();
//        }

        //create main fame


    }
}
