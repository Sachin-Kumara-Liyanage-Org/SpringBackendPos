package Pos.Service;

import Pos.Controller.PropertiesReader;
import Pos.Gui.ShowMessage;
import org.apache.log4j.Logger;

import java.awt.*;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.ComputerSystem;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

public class OsData {
    private final Logger logger = Logger.getLogger(OsData.class.getName());
    private String sn;

    public int getDisplayWidth(){
        return this.getGraphicsDevice().getDisplayMode().getWidth();
    }

    public int getDisplayHeight(){
        return this.getGraphicsDevice().getDisplayMode().getHeight();
    }

    public GraphicsDevice getGraphicsDevice(){
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    }


    public String getmotherid()
    {
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardwareAbstractionLayer = systemInfo.getHardware();
        ComputerSystem computerSystem = hardwareAbstractionLayer.getComputerSystem();
        return computerSystem.getBaseboard().getSerialNumber();
    }


}
