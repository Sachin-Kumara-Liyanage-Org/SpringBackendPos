package Pos.Service;

import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class FomatFactory {
    NumberFormatter formatter;

    public FomatFactory() {


    }

    public DefaultFormatterFactory IntFormatterFactory(){
        NumberFormat format = NumberFormat.getInstance();
        formatter =new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);
        return new DefaultFormatterFactory(formatter);
    }
    public DefaultFormatterFactory DoubleFormatterFactory(){
        DecimalFormat dc = new DecimalFormat("0.0000");
        formatter= new NumberFormatter(dc);
        formatter.setValueClass(Double.class);
        formatter.setMinimum(0.0);
        formatter.setMaximum(Double.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);
        return new DefaultFormatterFactory(formatter);
    }

    public DefaultFormatterFactory Double2FormatterFactory(){
        DecimalFormat dc = new DecimalFormat("0.00");
        formatter= new NumberFormatter(dc);
        formatter.setValueClass(Double.class);
        formatter.setMinimum(0.0);
        formatter.setMaximum(Double.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);
        return new DefaultFormatterFactory(formatter);

    }

    public DefaultFormatterFactory Double2FormatterFactory(double max){
        DecimalFormat dc = new DecimalFormat("0.00");
        formatter= new NumberFormatter(dc);
        formatter.setValueClass(Double.class);
        formatter.setMinimum(0.0);
        formatter.setMaximum(max);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);
        return new DefaultFormatterFactory(formatter);

    }

}
