package gen;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.FileInputStream;
import java.util.Properties;

public class Main {

    final static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {

        try {
                String config = System.getProperty("config.file");
                if (StringUtils.isEmpty(System.getProperty("config.file"))) {
                    config = "config.properties";
                }

                Properties prop = new Properties();
                prop.load(new FileInputStream(config));
                //custom log file
                if (!StringUtils.isEmpty(prop.getProperty("log.config.file"))){
                    PropertyConfigurator.configure(prop.getProperty("log.config.file"));
                }

                logger.info("Load configuration file");
                logger.debug("from "+config);

                String fileName = prop.getProperty("file.input");
                logger.debug("File name is "+fileName);

                new GenImgByData(prop,fileName);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        }
    }

//    private static void usage() {
//        System.out.println("Usage command");
//        System.out.println("\tjava -Dconfig.file=${config.properties} -jar ${PaymentHub.jar} ${mode} ${fileName}");
//        System.out.println("\tUse -Dconfig.file=${config.properties} to get your config");
//        System.out.println("\tUse -jar ${PaymentHub.jar} to get your jarfile to run");
//        System.out.println("\tUse ${mode} to set your mode to run");
//        System.out.println("\t\tuse \"write\" to Write data from database to file");
//        System.out.println("\t\tuse \"read\" to Read data from to file to database");
////        System.out.println("\tUse ${fileName} to set your file name");
//    }

}
