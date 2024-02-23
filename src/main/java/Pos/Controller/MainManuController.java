package Pos.Controller;

import Pos.Gui.LoginForm;
import Pos.Gui.MainManuForm;
import Pos.Gui.PosForm;
import Pos.Gui.StocksForm;
import Pos.Main.MainStarter;
import org.apache.log4j.Logger;

public class MainManuController {
    private final Logger logger = Logger.getLogger(MainManuController.class.getName());
    private MainManuForm mainManuForm;

    public MainManuController(MainManuForm mainManuForm) {
        this.mainManuForm = mainManuForm;
    }
    public MainManuController() {

    }

    public void openStocksForm(){
        logger.info("open stock form");
        MainStarter.mainFrame.getContentPane().removeAll();
        MainStarter.mainFrame.setContentPane(new StocksForm().getStockspanel());
        MainStarter.mainFrame.getContentPane().setVisible(false);
        MainStarter.mainFrame.getContentPane().setVisible(true);
    }

    public void openPosForm(){
        logger.info("open Pos form");
        MainStarter.mainFrame.getContentPane().removeAll();
        MainStarter.mainFrame.setContentPane(new PosForm().getPospane());
        MainStarter.mainFrame.getContentPane().setVisible(false);
        MainStarter.mainFrame.getContentPane().setVisible(true);
    }

    public void openPerBillForm(){
        System.out.println("open openPerBillForm");
    }

    public void openSalesForm(){
        System.out.println("open openSalesForm");
    }

    public void comeback(){
        MainStarter.mainFrame.getContentPane().removeAll();
        MainStarter.mainFrame.setContentPane(new MainManuForm().getMainManuPane());
        MainStarter.mainFrame.getContentPane().setVisible(false);
        MainStarter.mainFrame.getContentPane().setVisible(true);
    }
}
