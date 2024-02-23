package Pos.Controller;
import Pos.Gui.ShowMessage;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {
    private final Logger logger = Logger.getLogger(PropertiesReader.class.getName());
    private Properties prop = null;
    private static PropertiesReader propertisReader;

    public static PropertiesReader getPropertisReader() {
        if (PropertiesReader.propertisReader == null) {
            PropertiesReader.propertisReader = new PropertiesReader();
            PropertiesReader.propertisReader.logger.info("create new PropertisReader object");
        }
        return PropertiesReader.propertisReader;
    }

    public String getPropValues(String name) throws IOException {
        if (this.prop == null) {
            PropertiesReader.propertisReader.logger.info("load new props values");
            this.prop = this.load_props();

        }
//        PropertiesReader.propertisReader.logger.info("get props value for"+name);
        return prop.getProperty(name);
    }

    private Properties load_props() {
        Properties pr = new Properties();
        String propFileName = "config.properties";
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {

                pr.load(inputStream);

            }
        } catch (IOException e) {
            PropertiesReader.propertisReader.logger.error(e.toString());
            ShowMessage.ShowError(e.toString());

        }
        return pr;
    }
}
