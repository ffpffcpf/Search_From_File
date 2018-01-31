package com.cpf.fulltextsearch.dir;

import com.cpf.fulltextsearch.dir.listener.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class FirstFileWatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(FirstFileWatcher.class);

    private WatchService watchService;

    private EventListener createEventListener;
    private EventListener modifyEventListener;
    private EventListener deleteEventListener;

    private InterestingBox box;

    public FirstFileWatcher(String fileName) {
        try {
            watchService = FileSystems.getDefault().newWatchService();
            Paths.get(fileName).register(watchService,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
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
            LOGGER.error(e.getMessage());
        }
    }

    private boolean isIgnoreFile(File file) {
        return file.isHidden() || file.getName().contains("~") || file.getName().startsWith(".");
    }


    public EventListener getCreateEventListener() {
        return createEventListener;
    }

    public void setCreateEventListener(EventListener createEventListener) {
        this.createEventListener = createEventListener;
    }

    public EventListener getModifyEventListener() {
        return modifyEventListener;
    }

    public void setModifyEventListener(EventListener modifyEventListener) {
        this.modifyEventListener = modifyEventListener;
    }

    public EventListener getDeleteEventListener() {
        return deleteEventListener;
    }

    public void setDeleteEventListener(EventListener deleteEventListener) {
        this.deleteEventListener = deleteEventListener;
    }
}
