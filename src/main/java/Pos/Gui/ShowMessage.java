package Pos.Gui;

import Pos.Main.MainStarter;

import static javax.swing.JOptionPane.*;

public class ShowMessage {
    public static void ShowError(String message){
        showMessageDialog(MainStarter.mainFrame, message, "Error", ERROR_MESSAGE);
    }
    public static void ShowWorn(String message){
        showMessageDialog(MainStarter.mainFrame, message, "Warning", WARNING_MESSAGE);
    }

    public static void ShowInfo(String message){
        showMessageDialog(MainStarter.mainFrame, message, "Info", INFORMATION_MESSAGE);
    }
}
