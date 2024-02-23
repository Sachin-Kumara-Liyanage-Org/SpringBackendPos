package Pos.Model;

public class StockModel {
    private int Id;
    private String BarCode,Name,Warranty;
    private Double Qty,inStock,B_Price,S_Price;

    public StockModel(String barCode, String name, String warranty, Double qty, Double b_Price, Double s_Price) {
        BarCode = barCode;
        Name = name;
        Warranty = warranty;
        Qty = qty;
        B_Price = b_Price;
        S_Price = s_Price;
    }

    public StockModel(String barCode, String name, String warranty, Double qty, Double s_Price) {
        BarCode = barCode;
        Name = name;
        Warranty = warranty;
        Qty = qty;
        S_Price = s_Price;
    }

    public StockModel(int id, String barCode, String name, String warranty, Double qty, Double inStock, Double b_Price, Double s_Price) {
        Id = id;
        BarCode = barCode;
        Name = name;
        Warranty = warranty;
        Qty = qty;
        this.inStock = inStock;
        B_Price = b_Price;
        S_Price = s_Price;
    }
    public StockModel(int id, String barCode, String name, String warranty, Double inStock, Double s_Price) {
        Id = id;
        BarCode = barCode;
        Name = name;
        Warranty = warranty;
        this.inStock = inStock;
        S_Price = s_Price;
    }

    public StockModel() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getBarCode() {
        return BarCode;
    }

    public void setBarCode(String barCode) {
        BarCode = barCode;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getWarranty() {
        return Warranty;
    }

    public void setWarranty(String warranty) {
        Warranty = warranty;
    }

    public Double getQty() {
        return Qty;
    }

    public void setQty(Double qty) {
        Qty = qty;
    }

    public Double getInStock() {
        return inStock;
    }

    public void setInStock(Double inStock) {
        this.inStock = inStock;
    }

    public Double getB_Price() {
        return B_Price;
    }

    public void setB_Price(Double b_Price) {
        B_Price = b_Price;
    }

    public Double getS_Price() {
        return S_Price;
    }

    public void setS_Price(Double s_Price) {
        S_Price = s_Price;
    }
}
