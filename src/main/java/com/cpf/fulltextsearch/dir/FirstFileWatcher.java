package com.cpf.fulltextsearch.dir;

import com.cpf.fulltextsearch.dir.interesting.InterestingBox;
import com.cpf.fulltextsearch.dir.listener.EventListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.file.*;

@Data
@Slf4j
public class FirstFileWatcher {


    private WatchService watchService;

    private EventListener createEventListener;
    private EventListener modifyEventListener;
    private EventListener deleteEventListener;

    @Autowired
    private InterestingBox box;

    public FirstFileWatcher(String fileName) {
        try {
            watchService = FileSystems.getDefault().newWatchService();
            Paths.get(fileName).register(watchService,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


    public void start() throws InterruptedException {
        while (true) {
            WatchKey watchKey = watchService.take();//thread block when there are not event in the directory
            Thread.sleep(50);

            for (WatchEvent<?> event : watchKey.pollEvents()) {
                if (!box.isInteresting(((WatchEvent<Path>) event).context())) {
                    continue;
                }

                if (StandardWatchEventKinds.ENTRY_CREATE.name().equals(event.kind().name())) {
                    createEventListener.handleEvent(event);
                } else if (StandardWatchEventKinds.ENTRY_DELETE.name().equals(event.kind().name())) {
                    deleteEventListener.handleEvent(event);
                } else if (StandardWatchEventKinds.ENTRY_MODIFY.name().equals(event.kind().name())) {
                    modifyEventListener.handleEvent(event);
                }
            }
            watchKey.reset();
        }
    }

    public void stop() {
        try {
            watchService.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }



}
