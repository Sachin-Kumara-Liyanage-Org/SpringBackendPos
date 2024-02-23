package Pos.Model;

import java.util.List;

public class BillModel {
    private String Name,Warrant;
    private int sid;
    private Double PreUnit,Qty,Amount;

    public BillModel(String name, String warrant, int sid, Double preUnit, Double qty) {
        Name = name;
        Warrant = warrant;
        this.sid = sid;
        PreUnit = preUnit;
        Qty = qty;
        Amount=preUnit*qty;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getWarrant() {
        return Warrant;
    }

    public void setWarrant(String warrant) {
        Warrant = warrant;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public Double getPreUnit() {
        return PreUnit;
    }

    public void setPreUnit(Double preUnit) {
        PreUnit = preUnit;
    }

    public Double getQty() {
        return Qty;
    }

    public void setQty(Double qty) {
        Qty = qty;
    }

    public Double getAmount() {
        return Amount;
    }

    public void setAmount(Double amount) {
        Amount = amount;
    }

    public void printall(){
        System.out.println(Name+"  ,  "+Warrant+"  ,  "+sid+"  ,  "+PreUnit+"  ,  "+Qty+"  ,  "+Amount);
    }
}
