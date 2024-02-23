package Pos.Controller;

import Pos.Gui.ShowMessage;
import Pos.Gui.StocksForm;
import Pos.Model.StockModel;
import Pos.Service.AllShortcuts;
import Pos.Service.DBService;
import Pos.Service.StockModelService;
import org.apache.log4j.Logger;

import java.awt.*;
import java.sql.SQLException;

public class StockFormController implements AllShortcuts {
    private StocksForm stocksForm;
    private StockModelService stockModelService;
    private static StockModel SelectedRow;
    private final Logger logger = Logger.getLogger(StockFormController.class.getName());

    public StockFormController(StocksForm stocksForm) {
        this.stocksForm = stocksForm;
        this.stockModelService=new StockModelService();
    }

    public  Boolean addToStock() throws SQLException {

        KeyboardFocusManager keyManager=KeyboardFocusManager.getCurrentKeyboardFocusManager();
        keyManager.removeKeyEventDispatcher(StockShortcut);


        if ("".equalsIgnoreCase(stocksForm.getName().getText())) {
            ShowMessage.ShowWorn("You Must Add Product Name");
            keyManager.addKeyEventDispatcher(StockShortcut);
            return false;
        }
        if ("".equalsIgnoreCase(stocksForm.getQty().getText())||"0.00".equalsIgnoreCase(stocksForm.getQty().getText())) {
            ShowMessage.ShowWorn("You Must Add Product Quantity");
            keyManager.addKeyEventDispatcher(StockShortcut);
            return false;
        }
        if ("".equalsIgnoreCase(stocksForm.getBprice().getText())) {
            ShowMessage.ShowWorn("You Must Add Product Buy Price");
            keyManager.addKeyEventDispatcher(StockShortcut);
            return false;
        }
        if ("".equalsIgnoreCase(stocksForm.getSprice().getText())) {
            ShowMessage.ShowWorn("You Must Add Product Sale Price");
            keyManager.addKeyEventDispatcher(StockShortcut);
            return false;
        }
        StockModel model = getTextInForm();

        if(stockModelService.addStock(model)){
            ClearAll();
            LoadTable();
            ForceBarCode();
            stocksForm.getEditToStockButton().setVisible(false);
            stocksForm.getRemoveToStockButton().setVisible(false);
        }

        keyManager.addKeyEventDispatcher(StockShortcut);
        return true;

    }

    public StockModel getTextInForm(){
        return new StockModel(
                stocksForm.getBarcode().getText(),
                stocksForm.getName().getText(),
                stocksForm.getWarranty().getText(),
                Double.valueOf(stocksForm.getQty().getText()),
                Double.valueOf(stocksForm.getBprice().getText()),
                Double.valueOf(stocksForm.getSprice().getText())
        );
    }



    public void ForceBarCode(){
        stocksForm.getBarcode().requestFocus();
    }

    public void ForceName(){
        stocksForm.getName().requestFocus();
    }
    public void ForceQty(){
        stocksForm.getQty().requestFocus();
    }
    public void ForceBprice(){
        stocksForm.getBprice().requestFocus();
    }
    public void ForceSprice(){
        stocksForm.getSprice().requestFocus();
    }
    public void ForceWarranty(){
        stocksForm.getWarranty().requestFocus();
    }

    public void ClearAll(){
        ClearBarCode();
        ClearName();
        ClearQty();
        ClearBprice();
        ClearSprice();
        ClearWarranty();
    }
    public void ClearBarCode(){
        stocksForm.getBarcode().setText("");
    }

    public void ClearName(){
        stocksForm.getName().setText("");
    }
    public void ClearQty(){
        stocksForm.getQty().setValue(null);
    }
    public void ClearBprice(){
        stocksForm.getBprice().setValue(null);
    }
    public void ClearSprice(){
        stocksForm.getSprice().setValue(null);
    }
    public void ClearWarranty(){
        stocksForm.getWarranty().setText("");
    }


    public void LoadTable() throws SQLException {
        stocksForm.clearDataRow();
        for (StockModel model: stockModelService.getAllStock()) {
            stocksForm.createDataRow(model);
        }

    }

    public void checkBarcodeAndName(){
        stocksForm.clearDataRow();
        try {
            for (StockModel model: stockModelService.findWithBarCodeAndName(stocksForm.getBarcode().getText(),stocksForm.getName().getText())) {
                stocksForm.createDataRow(model);
            }
        } catch (SQLException throwables) {
        }
    }

    public void RowSelect(){
        int rowid= stocksForm.getStockTable().getSelectedRow();
//        stockTable.getValueAt(stockTable.getSelectedRow(), 0).toString()
        SelectedRow=new StockModel(
                Integer.parseInt(stocksForm.getStockTable().getValueAt(rowid,0).toString()),
                stocksForm.getStockTable().getValueAt(rowid,1).toString(),
                stocksForm.getStockTable().getValueAt(rowid,2).toString(),
                stocksForm.getStockTable().getValueAt(rowid,7).toString(),
                Double.valueOf(stocksForm.getStockTable().getValueAt(rowid,3).toString()),
                Double.valueOf(stocksForm.getStockTable().getValueAt(rowid,4).toString()),
                Double.valueOf(stocksForm.getStockTable().getValueAt(rowid,5).toString()),
                Double.valueOf(stocksForm.getStockTable().getValueAt(rowid,6).toString())
        );
        setTextinForm(SelectedRow);
        stocksForm.getEditToStockButton().setVisible(true);
        stocksForm.getRemoveToStockButton().setVisible(true);

    }

    public void setTextinForm(StockModel model){
        stocksForm.getBarcode().setText(model.getBarCode());
        stocksForm.getName().setText(model.getName());
        stocksForm.getWarranty().setText(model.getWarranty());
        stocksForm.getQty().setValue(model.getQty());
        stocksForm.getBprice().setValue(model.getB_Price());
        stocksForm.getSprice().setValue(model.getS_Price());

    }

    public void RemoveStockData(){
        if(stockModelService.removeData(SelectedRow.getId())){
            SelectedRow=null;
            ClearAll();
            try {
                LoadTable();
            } catch (SQLException throwables) {
            }
            ForceBarCode();
            stocksForm.getEditToStockButton().setVisible(false);
            stocksForm.getRemoveToStockButton().setVisible(false);
        }
    }

    public boolean EditStockData(){
        KeyboardFocusManager keyManager=KeyboardFocusManager.getCurrentKeyboardFocusManager();
        keyManager.removeKeyEventDispatcher(StockShortcut);


        if ("".equalsIgnoreCase(stocksForm.getName().getText())) {
            ShowMessage.ShowWorn("You Must Add Product Name");
            keyManager.addKeyEventDispatcher(StockShortcut);
            return false;
        }
        if ("".equalsIgnoreCase(stocksForm.getQty().getText())||"0.00".equalsIgnoreCase(stocksForm.getQty().getText())) {
            ShowMessage.ShowWorn("You Must Add Product Quantity");
            keyManager.addKeyEventDispatcher(StockShortcut);
            return false;
        }
        if ("".equalsIgnoreCase(stocksForm.getBprice().getText())) {
            ShowMessage.ShowWorn("You Must Add Product Buy Price");
            keyManager.addKeyEventDispatcher(StockShortcut);
            return false;
        }
        if ("".equalsIgnoreCase(stocksForm.getSprice().getText())) {
            ShowMessage.ShowWorn("You Must Add Product Sale Price");
            keyManager.addKeyEventDispatcher(StockShortcut);
            return false;
        }

        keyManager.addKeyEventDispatcher(StockShortcut);

        if(stockModelService.EditData(SelectedRow.getId(),getTextInForm())){
            SelectedRow=null;
            ClearAll();
            try {
                LoadTable();
            } catch (SQLException throwables) {
            }
            ForceBarCode();
            stocksForm.getEditToStockButton().setVisible(false);
            stocksForm.getRemoveToStockButton().setVisible(false);
        }
        return true;
    }


}
