package com.cpf.fulltextsearch.dir;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class FirstFileWatcher {

    private static final Logger log = LoggerFactory.getLogger(FirstFileWatcher.class);

    private WatchService watchService;

    private WatchKey watchKey;

    public FirstFileWatcher() {
        try {
            watchService = FileSystems.getDefault().newWatchService();
            Path des = Paths.get("D:\\file_watcher_test");
            des.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


    public void handleEvent() throws InterruptedException {
        while (true){
            WatchKey watchKey = watchService.take();

            for (WatchEvent<?>  event : watchKey.pollEvents()){

                Path filename = ((WatchEvent<Path>)event).context();
                log.info("=================================="+event.kind().name());
                log.info("----------------------------------"+filename.toString());
            }

            watchKey.reset();
        }

    }

}
