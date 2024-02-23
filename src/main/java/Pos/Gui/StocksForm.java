package Pos.Gui;

import Pos.Controller.LoginController;
import Pos.Controller.MainManuController;
import Pos.Controller.StockFormController;
import Pos.Model.StockModel;
import Pos.Service.AllShortcuts;
import Pos.Service.FomatFactory;
import Pos.Service.StockModelService;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.text.NumberFormat;

public class StocksForm implements AllShortcuts {
    private JPanel Stockspanel;
    private JTextField barcode;
    private JTextField name;
    private JButton addToStockButton;
    private JLabel barLab;
    private JLabel nameLab;
    private JLabel qtylab;
    private JLabel bplab;
    private JLabel splab;
    private JLabel warrlab;
    private JTextField warranty;
    private JTable stockTable;
    private JButton home;
    private JButton logout;
    private JFormattedTextField qty;
    private JFormattedTextField bprice;
    private JFormattedTextField sprice;
    private JButton EditToStockButton;
    private JButton RemoveToStockButton;
    public static StockFormController stockFormController;
    private DefaultTableModel stockTableModel;
    private StockModelService stockModelService;

    public StocksForm() {
        stockFormController = new StockFormController(this);
        stockModelService = new StockModelService();
        IconFontSwing.register(FontAwesome.getIconFont());
        barLab.setIcon(IconFontSwing.buildIcon(FontAwesome.BARCODE, 28));
        nameLab.setIcon(IconFontSwing.buildIcon(FontAwesome.CUBE, 28));
        qtylab.setIcon(IconFontSwing.buildIcon(FontAwesome.CUBES, 28));
        bplab.setIcon(IconFontSwing.buildIcon(FontAwesome.MONEY, 28));
        splab.setIcon(IconFontSwing.buildIcon(FontAwesome.MONEY, 28));
        warrlab.setIcon(IconFontSwing.buildIcon(FontAwesome.CALENDAR, 28));
        addToStockButton.setIcon(IconFontSwing.buildIcon(FontAwesome.PLUS_CIRCLE, 28, new Color(255, 255, 255)));
        EditToStockButton.setIcon(IconFontSwing.buildIcon(FontAwesome.PENCIL_SQUARE, 28, new Color(255, 255, 255)));
        RemoveToStockButton.setIcon(IconFontSwing.buildIcon(FontAwesome.WINDOW_CLOSE, 28, new Color(255, 255, 255)));
        home.setIcon(IconFontSwing.buildIcon(FontAwesome.HOME, 40, new Color(255, 255, 255)));
        logout.setIcon(IconFontSwing.buildIcon(FontAwesome.POWER_OFF, 40, new Color(255, 255, 255)));
        stockTableModel=new DefaultTableModel(){
            public boolean isCellEditable(int row, int column)
            {
                return false;//This causes all cells to be not editable
            }
        };
        stockTable.setModel(stockTableModel);
        stockTable.setRowHeight(40);
        createColl();
        stockTable.getTableHeader().setReorderingAllowed(false);

        KeyboardFocusManager keyManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        keyManager.removeKeyEventDispatcher(MainManu);
        keyManager.addKeyEventDispatcher(StockShortcut);
        EditToStockButton.setVisible(false);
        RemoveToStockButton.setVisible(false);

        try {
            stockFormController.LoadTable();
        } catch (SQLException throwables) {

        }


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
        home.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                new MainManuController().comeback();
            }
        });

        //qty formater
        qty.setFormatterFactory(new FomatFactory().Double2FormatterFactory());
        bprice.setFormatterFactory(new FomatFactory().Double2FormatterFactory());
        sprice.setFormatterFactory(new FomatFactory().Double2FormatterFactory());
        addToStockButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    stockFormController.addToStock();
                } catch (SQLException throwables) {
                }
            }
        });
        barcode.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                stockFormController.checkBarcodeAndName();
            }
        });
        name.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                stockFormController.checkBarcodeAndName();
            }
        });
        stockTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting()) {

                    stockFormController.RowSelect();

                }
            }
        });
        RemoveToStockButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                stockFormController.RemoveStockData();
            }
        });
        EditToStockButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                stockFormController.EditStockData();
            }
        });
    }

    public JPanel getStockspanel() {
        return Stockspanel;
    }

    public JTextField getBarcode() {
        return barcode;
    }


    public JTextField getName() {
        return name;
    }


    public JFormattedTextField getQty() {
        return qty;
    }


    public JFormattedTextField getBprice() {
        return bprice;
    }


    public JFormattedTextField getSprice() {
        return sprice;
    }


    public JTextField getWarranty() {
        return warranty;
    }


    private void createColl() {
        stockTableModel = (DefaultTableModel) stockTable.getModel();
        stockTableModel.addColumn("Id");
        stockTableModel.addColumn("BarCode");
        stockTableModel.addColumn("Name");
        stockTableModel.addColumn("Qty");
        stockTableModel.addColumn("inStock");
        stockTableModel.addColumn("B.Price");
        stockTableModel.addColumn("S.Price");
        stockTableModel.addColumn("Warranty");

    }

    //crate table rows
    public void createDataRow(StockModel model) {
        stockTableModel = (DefaultTableModel) stockTable.getModel();
        stockTableModel.addRow(stockModelService.getDataForStockTable(model));

    }

    public void clearDataRow() {
        stockTableModel = (DefaultTableModel) stockTable.getModel();
        stockTableModel.getDataVector().removeAllElements();
//        stockTableModel.fireTableDataChanged();
    }

    public JButton getEditToStockButton() {
        return EditToStockButton;
    }

    public JButton getRemoveToStockButton() {
        return RemoveToStockButton;
    }

    public JTable getStockTable() {
        return stockTable;
    }

}
