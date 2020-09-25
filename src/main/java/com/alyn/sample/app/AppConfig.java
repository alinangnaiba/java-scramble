package com.alyn.sample.app;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {

    private final String CONFIG_PROPERTIES = "config.properties";
    private final String TRANSACTION_DIRECTORY = "transactionDirectory";
    private final String PRODUCT_REFERENCE_DIRECTORY = "productReferenceDirectory";

    private String transactionDirectory;
    private String referenceDirectory;
    private static AppConfig instance;

    private AppConfig() { }

    public static AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
            try (InputStream in = new FileInputStream(instance.CONFIG_PROPERTIES)) {
                if (in == null) {
                    System.out.println("Unable to get config.properties.");
                    throw new FileNotFoundException();
                }

                Properties prop = new Properties();
                prop.load(in);

                instance.transactionDirectory = prop.getProperty(instance.TRANSACTION_DIRECTORY);
                instance.referenceDirectory = prop.getProperty(instance.PRODUCT_REFERENCE_DIRECTORY);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return instance;
    }

    public String getTransactionDirectory() {
        if (transactionDirectory == null) {
            return "";
        }
        return transactionDirectory;
    }

    public String getReferenceDirectory() {
        if (referenceDirectory == null) {
            return "";
        }
        return referenceDirectory;
    }
}
