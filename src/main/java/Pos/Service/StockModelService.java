package Pos.Service;

import Pos.Controller.LoginController;
import Pos.Gui.ShowMessage;
import Pos.Model.StockModel;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StockModelService {
    private final Logger logger = Logger.getLogger(StockModelService.class.getName());

    public String[] getDataForStockTable(StockModel model) {
        String[] data = new String[8];
        data[0] = String.valueOf(model.getId());
        data[1] = String.valueOf(model.getBarCode());
        data[2] = String.valueOf(model.getName());
        data[3] = String.valueOf(model.getQty());
        data[4] = String.valueOf(model.getInStock());
        data[6] = String.valueOf(model.getS_Price());
        data[5] = String.valueOf(model.getB_Price());
        data[7] = String.valueOf(model.getWarranty());
        return data;
    }

    public String[] getDataForPosStockTable(StockModel model) {
//        System.out.println(model.getB_Price());
        String[] data = new String[7];
        data[0] = String.valueOf(model.getId());
        data[1] = String.valueOf(model.getBarCode());
        data[2] = String.valueOf(model.getName());
        data[3] = String.valueOf(model.getInStock());
        data[4] = String.valueOf(model.getB_Price());
        data[5] = String.valueOf(model.getS_Price());
        data[6] = String.valueOf(model.getWarranty());
        return data;
    }

    public boolean addStock(StockModel model) throws SQLException {
        PreparedStatement stmt=null;
        try {
             stmt = DBService.get_DBservice().getCon()
                    .prepareStatement("insert into mst_stock" +
                            "(`Barcode`,`Name`,`Qty`,`Bprice`,`Sprice`,`Warranty`,`addby`) " +
                            "values " +
                            "(?,?,?,?,?,?,?)");
            stmt.setString(1, model.getBarCode());
            stmt.setString(2, model.getName());
            stmt.setDouble(3, model.getQty());
            stmt.setDouble(4, model.getB_Price());
            stmt.setDouble(5, model.getS_Price());
            stmt.setString(6, model.getWarranty());
            stmt.setInt(7, LoginController.user.getId());
            stmt.executeUpdate();
            logger.info("Add new Recode To Stock");
            ShowMessage.ShowInfo("Add "+model.getName()+" To Stock");
        } catch (SQLException e) {
            logger.error(e);
            ShowMessage.ShowError(e.toString());
            return false;
        }finally {
            stmt.close();
        }
        return true;
    }

    public List<StockModel> getAllStock() throws SQLException {
        List <StockModel>list=new ArrayList<StockModel>();
        PreparedStatement stmt=null;
        ResultSet rs=null;
        try {
            stmt = DBService.get_DBservice().getCon()
                    .prepareStatement("SELECT ms.*,(ms.Qty-(CASE WHEN bd.sell IS NOT NULL THEN bd.sell ELSE 0.0 END))as instock FROM (select b.Sid,SUM(b.Qty) as sell from bill_details b GROUP BY b.Sid) bd RIGHT JOIN (select * from mst_stock s where `state`=1) ms ON bd.Sid = ms.Id order by ms.`time` desc");
             rs = stmt.executeQuery();
            while (rs.next()){
                if(rs.getDouble(rs.findColumn("instock"))>0){
                    StockModel temp = new StockModel(

                            rs.getInt(rs.findColumn("Id")),
                            rs.getString(rs.findColumn("Barcode")),
                            rs.getString(rs.findColumn("Name")),
                            rs.getString(rs.findColumn("Warranty")),
                            rs.getDouble(rs.findColumn("Qty")),
                            rs.getDouble(rs.findColumn("instock")),
                            rs.getDouble(rs.findColumn("Bprice")),
                            rs.getDouble(rs.findColumn("Sprice"))
                    );
                    list.add(temp);
                }

            }

        } catch (SQLException e) {
            logger.error(e);
            ShowMessage.ShowError(e.toString());
        }finally {
            stmt.close();
            rs.close();
        }
        return list;
    }

    public List<StockModel> findWithBarCodeAndName(String bar,String Name) throws SQLException {
        List <StockModel>list=new ArrayList<StockModel>();
        PreparedStatement stmt=null;
        ResultSet rs=null;
        try {
            if("".equalsIgnoreCase(bar)&&"".equalsIgnoreCase(Name)){
                return getAllStock();
            }else if("".equalsIgnoreCase(bar)){
                stmt = DBService.get_DBservice().getCon()
                                    .prepareStatement("SELECT ms.*,(ms.Qty-(CASE WHEN bd.sell IS NOT NULL THEN bd.sell ELSE 0.0 END))as instock FROM (select b.Sid,SUM(b.Qty) as sell from bill_details b GROUP BY b.Sid) bd RIGHT JOIN (select * from mst_stock s where `state`=1 and LOWER(Name) LIKE ?) ms ON bd.Sid = ms.Id order by ms.`time` desc");



                stmt.setString(1,"%"+Name+"%");
            }else if("".equalsIgnoreCase(Name)){
                stmt = DBService.get_DBservice().getCon()
                        .prepareStatement("SELECT ms.*,(ms.Qty-(CASE WHEN bd.sell IS NOT NULL THEN bd.sell ELSE 0.0 END))as instock FROM (select b.Sid,SUM(b.Qty) as sell from bill_details b GROUP BY b.Sid) bd RIGHT JOIN (select * from mst_stock s where `state`=1 and Barcode LIKE ?) ms ON bd.Sid = ms.Id order by ms.`time` desc");


                stmt.setString(1,bar+"%");
            }else{
                stmt = DBService.get_DBservice().getCon()
                        .prepareStatement("SELECT ms.*,(ms.Qty-(CASE WHEN bd.sell IS NOT NULL THEN bd.sell ELSE 0.0 END))as instock FROM (select b.Sid,SUM(b.Qty) as sell from bill_details b GROUP BY b.Sid) bd RIGHT JOIN (select * from mst_stock s where `state`=1 and Barcode LIKE ? and LOWER(Name) LIKE ?) ms ON bd.Sid = ms.Id order by ms.`time` desc");

                stmt.setString(1,bar+"%");
                stmt.setString(2,"%"+Name+"%");
            }
            rs = stmt.executeQuery();
            while (rs.next()){
                StockModel temp = new StockModel(
                        rs.getInt(rs.findColumn("Id")),
                        rs.getString(rs.findColumn("Barcode")),
                        rs.getString(rs.findColumn("Name")),
                        rs.getString(rs.findColumn("Warranty")),
                        rs.getDouble(rs.findColumn("Qty")),
                        rs.getDouble(rs.findColumn("instock")),
                        rs.getDouble(rs.findColumn("Bprice")),
                        rs.getDouble(rs.findColumn("Sprice"))
                );
                list.add(temp);
            }

        } catch (SQLException e) {
            logger.error(e);
            ShowMessage.ShowError(e.toString());
        }finally {
            try {
                stmt.close();
                rs.close();
            }catch (Exception e){

            }

        }
        return list;
    }

    public Boolean removeData(int id){
        PreparedStatement stmt=null;
        try {
            stmt = DBService.get_DBservice().getCon()
                    .prepareStatement("UPDATE mst_stock set `state`=0 where Id=?");
            stmt.setInt(1,id);
            stmt.executeUpdate();
            logger.info("Stock Recode Removed id: "+id);
            ShowMessage.ShowInfo("Stock Recode Removed id: "+id);
        } catch (SQLException e) {
            logger.error(e);
            ShowMessage.ShowError(e.toString());
            return false;
        }finally {
            try {
                stmt.close();
            } catch (SQLException throwables) {
            }
        }
        return true;
    }

    public Boolean EditData(int id, StockModel model){
        PreparedStatement stmt=null;
        try {
            stmt = DBService.get_DBservice().getCon()
                    .prepareStatement("UPDATE mst_stock set `Barcode`=?,`Name`=?,`Qty`=?,`Bprice`=?,`Sprice`=?,`Warranty`=?,`addby`=? where Id=?");
            stmt.setString(1, model.getBarCode());
            stmt.setString(2, model.getName());
            stmt.setDouble(3, model.getQty());
            stmt.setDouble(4, model.getB_Price());
            stmt.setDouble(5, model.getS_Price());
            stmt.setString(6, model.getWarranty());
            stmt.setInt(7, LoginController.user.getId());
            stmt.setInt(8,id);
            stmt.executeUpdate();
            logger.info("Stock Recode Edited id: "+id);
            ShowMessage.ShowInfo("Stock Edited id: "+id);
        } catch (SQLException e) {
            logger.error(e);
            ShowMessage.ShowError(e.toString());
            return false;
        }finally {
            try {
                stmt.close();
            } catch (SQLException throwables) {
            }
        }
        return true;
    }

    public Double getInStock(int id){
        return 0.0;
    }
}
