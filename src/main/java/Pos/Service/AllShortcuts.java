package Pos.Service;

import Pos.Controller.LoginController;
import Pos.Controller.MainManuController;
import Pos.Gui.LoginForm;
import Pos.Gui.MainManuForm;
import Pos.Gui.PosForm;
import Pos.Gui.StocksForm;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.sql.SQLException;

public interface AllShortcuts {
    KeyEventDispatcher Login=new KeyEventDispatcher() {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if(e.getID()==KeyEvent.KEY_PRESSED && e.getKeyCode()==KeyEvent.VK_ENTER){
                try {
                    LoginForm.loginController.checkLogin();
                } catch (SQLException throwables) {

                }
                return true;
            }
            return false;
        }

    };
    KeyEventDispatcher MainManu=new KeyEventDispatcher() {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if(e.getID()==KeyEvent.KEY_PRESSED && e.getKeyCode()==KeyEvent.VK_F1){
                MainManuForm.mainManuController.openPosForm();
                return true;
            }else if(e.getID()==KeyEvent.KEY_PRESSED && e.getKeyCode()==KeyEvent.VK_F2){
                MainManuForm.mainManuController.openStocksForm();
                return true;
            }else if(e.getID()==KeyEvent.KEY_PRESSED && e.getKeyCode()==KeyEvent.VK_F3){
                MainManuForm.mainManuController.openPerBillForm();
                return true;
            }else if(e.getID()==KeyEvent.KEY_PRESSED && e.getKeyCode()==KeyEvent.VK_F4){
                MainManuForm.mainManuController.openSalesForm();
                return true;
            }else if(e.getID()==KeyEvent.KEY_PRESSED && e.getKeyCode()==KeyEvent.VK_F12){
                try {
                    new LoginController().Logout();
                } catch (SQLException throwables) {
                }
                return true;
            }
            return false;
        }

    };
    KeyEventDispatcher StockShortcut=new KeyEventDispatcher() {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if(e.getID()==KeyEvent.KEY_PRESSED && e.getKeyCode()==KeyEvent.VK_F1){
                StocksForm.stockFormController.ForceBarCode();
                return true;
            }else if(e.getID()==KeyEvent.KEY_PRESSED && e.getKeyCode()==KeyEvent.VK_F2){
                StocksForm.stockFormController.ForceName();
                return true;
            }else if(e.getID()==KeyEvent.KEY_PRESSED && e.getKeyCode()==KeyEvent.VK_F3){
                StocksForm.stockFormController.ForceQty();
                return true;
            }else if(e.getID()==KeyEvent.KEY_PRESSED && e.getKeyCode()==KeyEvent.VK_F4){
                StocksForm.stockFormController.ForceBprice();
                return true;
            }else if(e.getID()==KeyEvent.KEY_PRESSED && e.getKeyCode()==KeyEvent.VK_F5){
                StocksForm.stockFormController.ForceSprice();
                return true;
            }else if(e.getID()==KeyEvent.KEY_PRESSED && e.getKeyCode()==KeyEvent.VK_F6){
                StocksForm.stockFormController.ForceWarranty();
                return true;
            }else if(e.getID()==KeyEvent.KEY_PRESSED && e.getKeyCode()==KeyEvent.VK_F12){
                try {
                    new LoginController().Logout();
                } catch (SQLException throwables) {
                }
                return true;
            }else if(e.getID()==KeyEvent.KEY_PRESSED && e.getKeyCode()==KeyEvent.VK_F11){
                new MainManuController().comeback();
                return true;
            }else if(e.getID()==KeyEvent.KEY_PRESSED && e.getKeyCode()==KeyEvent.VK_ENTER){
                try {
                    StocksForm.stockFormController.addToStock();
                } catch (SQLException throwables) {

                }
                return true;
            }

            return false;
        }

    };

    KeyEventDispatcher PosShortcut=new KeyEventDispatcher() {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if(e.getID()==KeyEvent.KEY_PRESSED && e.getKeyCode()==KeyEvent.VK_F1){
                PosForm.posController.ForceBarCode();
                return true;
            }else if(e.getID()==KeyEvent.KEY_PRESSED && e.getKeyCode()==KeyEvent.VK_F2){
                PosForm.posController.ForceName();
                return true;
            }else if(e.getID()==KeyEvent.KEY_PRESSED && e.getKeyCode()==KeyEvent.VK_F3){
                PosForm.posController.ForceQty();
                return true;
            }else if(e.getID()==KeyEvent.KEY_PRESSED && e.getKeyCode()==KeyEvent.VK_F4){
                PosForm.posController.ForceSprice();
                return true;
            }else if(e.getID()==KeyEvent.KEY_PRESSED && e.getKeyCode()==KeyEvent.VK_F5){
                PosForm.posController.ForceWarranty();
                return true;
            }else if(e.getID()==KeyEvent.KEY_PRESSED && e.getKeyCode()==KeyEvent.VK_F10){
                PosForm.posController.printBill();
                return true;
            }else if(e.getID()==KeyEvent.KEY_PRESSED && e.getKeyCode()==KeyEvent.VK_F12){
                try {
                    new LoginController().Logout();
                } catch (SQLException throwables) {
                }
                return true;
            }else if(e.getID()==KeyEvent.KEY_PRESSED && e.getKeyCode()==KeyEvent.VK_F11){
                new MainManuController().comeback();
                return true;
            }else if(e.getID()==KeyEvent.KEY_PRESSED && e.getKeyCode()==KeyEvent.VK_ENTER){

                PosForm.posController.addBill();

                return true;
            }

            return false;
        }

    };


}
