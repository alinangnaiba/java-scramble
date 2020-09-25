package com.alyn.sample.app.data;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvHandler {

    private final String TRANSACTION_ID = "transactionId";
    private final String PRODUCT_ID = "productId";
    private String path;
    public CsvHandler(String path) {
        this.path = path;
    }

    public List<String[]> read() throws IOException {
        List<String[]> data = new ArrayList<String[]>();
        CSVReader csvReader = new CSVReader(new FileReader(path));

        try{
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                if (isHeader(line)) {
                    continue;
                }

                data.add(line);
            }
            csvReader.close();
        } catch (IOException e) {
            csvReader.close();
            e.printStackTrace();
            return null;
        } catch (CsvValidationException e) {
            csvReader.close();
            e.printStackTrace();
            return null;
        }

        return data;
    }

    private boolean isHeader(String[] line) {
        if (line[0].equals(TRANSACTION_ID) || line[0].equals(PRODUCT_ID)) {
            return true;
        }
        return false;
    }
}
