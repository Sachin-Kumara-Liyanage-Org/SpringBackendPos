package Pos.Service;

import Pos.Controller.LoginController;
import Pos.Gui.ShowMessage;
import Pos.Model.BillModel;
import Pos.Model.StockModel;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BillModelService {
    private static List<BillModel> currentBill = new ArrayList<>();
    private final Logger logger = Logger.getLogger(BillModelService.class.getName());

    public void addToCurrentBill(BillModel model){
        currentBill.add(model);
    }

    public void removeFromCurrentBill(BillModel model){
        for (BillModel m:currentBill) {
            if(!m.getName().equalsIgnoreCase(model.getName())){
                continue;
            }else if(!m.getWarrant().equalsIgnoreCase(model.getWarrant())){
                continue;
            }else if(m.getSid()==model.getSid()&& m.getPreUnit().equals(model.getPreUnit()) && m.getQty().equals(model.getQty())){

                model=m;
                break;
            }

        }
        currentBill.remove(model);

    }

    public  List<BillModel> getCurrentBill(){
        return currentBill;
    }

    public String[] getDataForBillTable(BillModel model) {
        String[] data = new String[6];
        data[0] = String.valueOf(model.getSid());
        data[1] = String.valueOf(model.getName());
        data[2] = String.valueOf(model.getWarrant());
        data[3] = String.valueOf(model.getQty());
        data[4] = String.valueOf(model.getPreUnit());
        data[5] = String.valueOf(model.getAmount());
        return data;
    }

    public boolean savebill(){
        PreparedStatement stmt=null;
        ResultSet rs=null;
        try {
            double tot=0.0;
            for (BillModel b: currentBill) {
                tot+=b.getAmount();
            }
            stmt = DBService.get_DBservice().getCon()
                    .prepareStatement("insert into mst_bill" +
                            "(`Price`,`SaleBy`) " +
                            "values " +
                            "(?,?)");
            stmt.setDouble(1,tot);
            stmt.setInt(2, LoginController.user.getId());
            stmt.executeUpdate();
            stmt = DBService.get_DBservice().getCon()
                    .prepareStatement("select Id from mst_bill order by Id desc limit 1");
            rs=stmt.executeQuery();
            if(rs.next()){
                int id=rs.getInt(rs.findColumn("Id"));
                System.out.println("sachin");
                System.out.println(id);
                for (BillModel b: currentBill) {
                    stmt = DBService.get_DBservice().getCon()
                            .prepareStatement("insert into bill_details" +
                                    "(`Bid`,`Sid`,`Qty`,`Price`,`Warranty`) " +
                                    "values " +
                                    "(?,?,?,?,?)");
                    stmt.setInt(1,id);
                    stmt.setInt(2,b.getSid());
                    stmt.setDouble(3,b.getQty());
                    stmt.setDouble(4,b.getAmount());
                    stmt.setString(5,b.getWarrant());
                    stmt.executeUpdate();
                }
            }else{
                return false;
            }


        } catch (SQLException e) {
            logger.error(e);
            ShowMessage.ShowError(e.toString());
            return false;
        }finally {
            try {
                assert stmt != null;
                stmt.close();
                assert rs != null;
                rs.close();
            } catch (SQLException ignored) {
            }
        }
        return true;
    }

    public void emptybill(){
        currentBill=new ArrayList<>();
    }



}
