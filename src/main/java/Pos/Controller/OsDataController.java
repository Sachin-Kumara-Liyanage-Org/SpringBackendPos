package Pos.Controller;

import Pos.Gui.ShowMessage;
import Pos.Service.OsData;
import org.apache.log4j.Logger;

public class OsDataController {
    private final Logger logger = Logger.getLogger(OsData.class.getName());
    private OsData os;

    public OsDataController() {
        this.os = new OsData();
    }

    public int getDisplayWidth(){
        return os.getDisplayWidth();
    }

    public int getDisplayHeight(){
        return os.getDisplayHeight();
    }

    public String getgetSerialNumber(){
        return os.getmotherid();
    }

}
