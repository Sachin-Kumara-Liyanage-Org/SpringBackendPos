package Pos.Gui;

import Pos.Controller.LoginController;
import Pos.Controller.MainManuController;
import Pos.Service.AllShortcuts;
import Pos.Service.UserService;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class MainManuForm implements AllShortcuts {
    private JPanel MainManuPane;
    private JLabel posImg;
    private JLabel stocksImg;
    private JLabel perbillImg;
    private JLabel salesImg;
    private JLabel pos;
    private JPanel pospanel;
    private JPanel stock;
    private JPanel perbill;
    private JPanel sales;
    private JButton logout;
    public static MainManuController mainManuController;

    public MainManuForm() {
        MainManuForm.mainManuController=new MainManuController(this);
        IconFontSwing.register(FontAwesome.getIconFont());
        posImg.setIcon(IconFontSwing.buildIcon(FontAwesome.MONEY, 150));
        stocksImg.setIcon(IconFontSwing.buildIcon(FontAwesome.CUBES, 150));
        perbillImg.setIcon(IconFontSwing.buildIcon(FontAwesome.CREDIT_CARD, 150));
        salesImg.setIcon(IconFontSwing.buildIcon(FontAwesome.AREA_CHART, 150));
        logout.setIcon(IconFontSwing.buildIcon(FontAwesome.POWER_OFF, 40,new Color(255, 255, 255)));


        pospanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                mainManuController.openPosForm();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                pospanel.setBackground(Color.WHITE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                pospanel.setBackground(new Color(244,246,249));
            }
        });

        stock.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                mainManuController.openStocksForm();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                stock.setBackground(Color.WHITE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                stock.setBackground(new Color(244,246,249));
            }
        });

        perbill.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                mainManuController.openPerBillForm();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                perbill.setBackground(Color.WHITE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                perbill.setBackground(new Color(244,246,249));
            }
        });

        sales.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                mainManuController.openSalesForm();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                sales.setBackground(Color.WHITE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                sales.setBackground(new Color(244,246,249));
            }
        });


        MainManuPane.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                System.out.println(e);
            }
        });

        //create shortcuts
        KeyboardFocusManager keyManager=KeyboardFocusManager.getCurrentKeyboardFocusManager();
        keyManager.removeKeyEventDispatcher(StockShortcut);
        keyManager.removeKeyEventDispatcher(Login);
        keyManager.removeKeyEventDispatcher(PosShortcut);
        keyManager.addKeyEventDispatcher(MainManu);

        logout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    new LoginController().Logout();
                } catch (SQLException throwables) {
                }
            }
        });
    }



    public JPanel getMainManuPane() {
        return MainManuPane;
    }
}
