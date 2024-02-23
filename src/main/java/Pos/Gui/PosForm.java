package Pos.Gui;

import Pos.Controller.PosController;
import Pos.Model.BillModel;
import Pos.Model.StockModel;
import Pos.Service.AllShortcuts;
import Pos.Service.BillModelService;
import Pos.Service.FomatFactory;
import Pos.Service.StockModelService;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PosForm implements AllShortcuts {
    private JPanel Pospane;
    private JLabel barLab;
    private JLabel nameLab;
    private JLabel qtylab;
    private JLabel splab;
    private JLabel warrlab;
    private JTextField barcode;
    private JTextField name;
    private JFormattedTextField qty;
    private JFormattedTextField sprice;
    private JTextField warranty;
    private JTable stockTable;
    private JTable billTable;
    private JButton addButton;
    private JButton PrintButton;
    private JLabel total;
    private JButton RemoveButton;
    private DefaultTableModel stockTableModel;
    private DefaultTableModel billTableModel;
    private StockModelService stockModelService;
    private BillModelService billModelService;
    public static PosController posController;

    public PosForm(){
        stockModelService=new StockModelService();
        posController = new PosController(this);
        billModelService=new BillModelService();
        KeyboardFocusManager keyManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        keyManager.removeKeyEventDispatcher(MainManu);
        keyManager.addKeyEventDispatcher(PosShortcut);
        IconFontSwing.register(FontAwesome.getIconFont());
        barLab.setIcon(IconFontSwing.buildIcon(FontAwesome.BARCODE, 28));
        nameLab.setIcon(IconFontSwing.buildIcon(FontAwesome.CUBE, 28));
        qtylab.setIcon(IconFontSwing.buildIcon(FontAwesome.CUBES, 28));
        splab.setIcon(IconFontSwing.buildIcon(FontAwesome.MONEY, 28));
        total.setIcon(IconFontSwing.buildIcon(FontAwesome.CREDIT_CARD_ALT, 28));
        warrlab.setIcon(IconFontSwing.buildIcon(FontAwesome.CALENDAR, 28));
        addButton.setIcon(IconFontSwing.buildIcon(FontAwesome.PLUS_CIRCLE, 28, new Color(255, 255, 255)));
        RemoveButton.setIcon(IconFontSwing.buildIcon(FontAwesome.WINDOW_CLOSE, 28, new Color(255, 255, 255)));
        PrintButton.setIcon(IconFontSwing.buildIcon(FontAwesome.PRINT, 28, new Color(255, 255, 255)));
        RemoveButton.setVisible(false);
        stockTableModel=new DefaultTableModel(){
            public boolean isCellEditable(int row, int column)
            {
                return false;//This causes all cells to be not editable
            }
        };
        stockTable.setModel(stockTableModel);
        stockTable.setRowHeight(40);
        stockTableModel.addColumn("#");
        stockTableModel.addColumn("BarCode");
        stockTableModel.addColumn("Name");
        stockTableModel.addColumn("inStock");
        stockTableModel.addColumn("B.Price");
        stockTableModel.addColumn("S.Price");
        stockTableModel.addColumn("Warranty");
        stockTable.getTableHeader().setReorderingAllowed(false);

        billTableModel=new DefaultTableModel(){
            public boolean isCellEditable(int row, int column)
            {
                return false;//This causes all cells to be not editable
            }
        };
        billTable.setModel(billTableModel);
        billTable.setRowHeight(40);
        billTableModel.addColumn("SId");
        billTableModel.addColumn("Name");
        billTableModel.addColumn("Warranty");
        billTableModel.addColumn("Qty");
        billTableModel.addColumn("Per Unit");
        billTableModel.addColumn("Amount");
        billTable.getTableHeader().setReorderingAllowed(false);

        qty.setFormatterFactory(new FomatFactory().Double2FormatterFactory());
        sprice.setFormatterFactory(new FomatFactory().Double2FormatterFactory());

        posController.loadStockTable();
        barcode.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                posController.checkBarcodeAndName();
            }
        });
        name.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                posController.checkBarcodeAndName();
            }
        });

        stockTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting()) {

                    posController.getSelectedRowInStock();

                }
            }
        });
        billTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting()) {

                    posController.getSelectedRowInBill();

                }
            }
        });

        addButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                posController.addBill();
            }
        });
        RemoveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                posController.removeBill();
            }
        });
        PrintButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                posController.printBill();
            }
        });
    }

    public void createStockRow(StockModel model) {
        stockTableModel = (DefaultTableModel) stockTable.getModel();
        stockTableModel.addRow(stockModelService.getDataForPosStockTable(model));
    }

    public void clearStockRows() {
        stockTableModel = (DefaultTableModel) stockTable.getModel();
        stockTableModel.getDataVector().removeAllElements();
    }

    public void createBillRow(BillModel model) {
        billTableModel = (DefaultTableModel) billTable.getModel();
        billTableModel.addRow(billModelService.getDataForBillTable(model));
    }

    public void clearBillRows() {
        billTableModel = (DefaultTableModel) billTable.getModel();
        billTableModel.getDataVector().removeAllElements();
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

    public JFormattedTextField getSprice() {
        return sprice;
    }

    public JTextField getWarranty() {
        return warranty;
    }

    public JTable getStockTable() {
        return stockTable;
    }

    public JTable getBillTable() {
        return billTable;
    }

    public JLabel getTotal() {
        return total;
    }

    public JPanel getPospane() {
        return Pospane;

    }

    public JButton getRemoveButton() {
        return RemoveButton;
    }
}
