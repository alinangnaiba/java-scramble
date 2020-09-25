package com.alyn.sample.app.data;

import com.alyn.sample.app.AppConfig;
import com.alyn.sample.app.service.DataLoaderService;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader {
    private List<String> files;
    private static DataLoader instance;
    private AppConfig config;
    private DataLoaderService loaderService;
    private DataStore dataStore;

    private DataLoader() {
        files = new ArrayList<>();
        config = AppConfig.getInstance();
        loaderService = new DataLoaderService(config);
        dataStore = DataStore.getInstance();

        List<ProductReference> productReference = loaderService.loadProductReference();
        dataStore.addProductReference(productReference);
        List<Transaction> transactions = loaderService.loadTransactions();
        dataStore.addTransactions(transactions);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    watch();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    private void watch() throws IOException, InterruptedException {
        WatchService watchService
                = FileSystems.getDefault().newWatchService();
        Path path = Paths.get(config.getTransactionDirectory());
        System.out.println("will read from these directories:");
        System.out.println(config.getTransactionDirectory());
        System.out.println(config.getReferenceDirectory());
        path.register(
                watchService,
                StandardWatchEventKinds.ENTRY_CREATE);

        WatchKey key;
        while ((key = watchService.take()) != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                this.files.add(event.context().toString());
                String filePath = config.getTransactionDirectory() + "\\" + event.context().toString();
                Thread.sleep(10);
                List<Transaction> newTransactions = loaderService.loadTransactions(filePath);
                dataStore.addTransactions(newTransactions);
            }
            key.reset();
        }
    }

    public static DataLoader getInstance() {
        if (instance == null) {
            instance = new DataLoader();
        }

        return instance;
    }
}
