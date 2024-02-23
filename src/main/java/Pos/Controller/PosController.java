package Pos.Controller;

import Pos.Gui.PosForm;
import Pos.Gui.ShowMessage;
import Pos.Model.BillModel;
import Pos.Model.StockModel;
import Pos.Service.*;
import org.apache.log4j.Logger;


import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PosController implements AllShortcuts {
    private PosForm posForm;
    private final Logger logger = Logger.getLogger(PosController.class.getName());
    public static  StockModel selectedStock;
    public static  BillModel selectedbill;
    private StockModelService stockModelService;
    private BillModelService billModelService;

    public PosController(){


    }

    public PosController(PosForm posForm) {
        this.posForm = posForm;
        this.stockModelService=new StockModelService();
        billModelService=new BillModelService();
    }


    public StockModel getTextInForm(){
        return new StockModel(
                posForm.getBarcode().getText(),
                posForm.getName().getText(),
                posForm.getWarranty().getText(),
                Double.valueOf(posForm.getQty().getText()),
                Double.valueOf(posForm.getSprice().getText())
        );
    }

    public void ForceBarCode(){
        posForm.getBarcode().requestFocus();
    }
    public void ForceName(){
        posForm.getName().requestFocus();
    }
    public void ForceQty(){
        posForm.getQty().requestFocus();
    }
    public void ForceSprice(){
        posForm.getSprice().requestFocus();
    }
    public void ForceWarranty(){
        posForm.getWarranty().requestFocus();
    }
    public void ClearBarCode(){
        posForm.getBarcode().setText("");
    }
    public void ClearName(){
        posForm.getName().setText("");
    }
    public void ClearQty(){
        posForm.getQty().setValue(null);
    }
    public void ClearSprice(){
        posForm.getSprice().setValue(null);
    }
    public void ClearWarranty(){
        posForm.getWarranty().setText("");
    }

    public void ClearAll(){
        ClearBarCode();
        ClearName();
        ClearQty();
        ClearSprice();
        ClearWarranty();
    }

    public void setTextinForm(StockModel model){
        posForm.getBarcode().setText(model.getBarCode());
        posForm.getName().setText(model.getName());
        posForm.getWarranty().setText(model.getWarranty());
        posForm.getQty().setValue(model.getInStock());
        posForm.getSprice().setValue(model.getS_Price());

    }


    public void checkBarcodeAndName(){
        posForm.clearStockRows();
        try {
            for (StockModel model: stockModelService.findWithBarCodeAndName(posForm.getBarcode().getText(),posForm.getName().getText())) {
                posForm.createStockRow(model);
            }
        } catch (SQLException throwables) {
        }
    }

    public void loadStockTable(){
        posForm.clearStockRows();
        try {
            for (StockModel model: stockModelService.getAllStock()) {
//                System.out.println(model.getB_Price());
                posForm.createStockRow(model);
            }
        } catch (SQLException throwables) {
        }
    }

    public void getSelectedRowInStock(){
        int rowid= posForm.getStockTable().getSelectedRow();
//        int id, String barCode, String name, String warranty, Double inStock, Double s_Price
        posForm.getQty().getFormatter().uninstall();
        posForm.getQty().setFormatterFactory(new FomatFactory().Double2FormatterFactory(Double.valueOf(posForm.getStockTable().getValueAt(rowid,3).toString())));
        selectedStock=new StockModel(
                Integer.parseInt(posForm.getStockTable().getValueAt(rowid,0).toString()),
                posForm.getStockTable().getValueAt(rowid,1).toString(),
                posForm.getStockTable().getValueAt(rowid,2).toString(),
                posForm.getStockTable().getValueAt(rowid,6).toString(),
                Double.valueOf(posForm.getStockTable().getValueAt(rowid,3).toString()),
                Double.valueOf(posForm.getStockTable().getValueAt(rowid,5).toString())
        );
        setTextinForm(selectedStock);

    }

    public void getSelectedRowInBill(){
        int rowid= posForm.getBillTable().getSelectedRow();
//      String name, String warrant, int sid, Double preUnit, Double qty
        selectedbill=new BillModel(
                posForm.getBillTable().getValueAt(rowid,1).toString(),
                posForm.getBillTable().getValueAt(rowid,2).toString(),
                Integer.parseInt(posForm.getBillTable().getValueAt(rowid,0).toString()),
                Double.valueOf(posForm.getBillTable().getValueAt(rowid,4).toString()),
                Double.valueOf(posForm.getBillTable().getValueAt(rowid,3).toString())
        );
        posForm.getRemoveButton().setVisible(true);

    }

    public boolean addBill(){
        KeyboardFocusManager keyManager=KeyboardFocusManager.getCurrentKeyboardFocusManager();
        keyManager.removeKeyEventDispatcher(StockShortcut);


        if ("".equalsIgnoreCase(posForm.getQty().getText()) ||"0.00".equalsIgnoreCase(posForm.getQty().getText())) {
            ShowMessage.ShowWorn("You Must Add Product Quantity");
            keyManager.addKeyEventDispatcher(StockShortcut);
            return false;
        }
        if ("".equalsIgnoreCase(posForm.getSprice().getText())) {
            ShowMessage.ShowWorn("You Must Add Product Sale Price");
            keyManager.addKeyEventDispatcher(StockShortcut);
            return false;
        }
        if(selectedStock == null){
            ShowMessage.ShowWorn("Please Select Stock Again");
            keyManager.addKeyEventDispatcher(StockShortcut);
            return false;
        }

        keyManager.addKeyEventDispatcher(StockShortcut);
//        String name, String warrant, int sid, Double preUnit, Double qty
        billModelService.addToCurrentBill(new BillModel(
                selectedStock.getName(),
                posForm.getWarranty().getText(),
                selectedStock.getId(),
                Double.valueOf(posForm.getSprice().getText()),
                Double.valueOf(posForm.getQty().getText())
        ));

        LoadBillTable(billModelService.getCurrentBill());
        ClearAll();
        ForceBarCode();
        selectedStock=null;
        posForm.getRemoveButton().setVisible(false);
        return true;
    }

    public void LoadBillTable(List<BillModel> modellist){
        posForm.clearBillRows();
        double tot=0.0;
        for (BillModel model: modellist) {
            posForm.createBillRow(model);
            tot+=model.getAmount();
        }
        posForm.getTotal().setText("Total : "+tot);

    }

    public void printBill(){
        if(saveBill()){
            //add printing part hear
        }

    }

    public boolean saveBill(){
        if(billModelService.savebill()){
            billModelService.emptybill();
            posForm.getRemoveButton().setVisible(false);
            LoadBillTable(billModelService.getCurrentBill());
            loadStockTable();
            ClearAll();
            ForceBarCode();
            return true;
        }
        return false;

    }

    public void removeBill(){
        billModelService.removeFromCurrentBill(selectedbill);
        LoadBillTable(billModelService.getCurrentBill());
        selectedbill=null;
        posForm.getRemoveButton().setVisible(false);
    }
}
