package com.cpf.fulltextsearch.watcher;

import com.cpf.fulltextsearch.dir.FirstFileWatcher;
import org.junit.Test;

public class FirstFileWatcherTest {

    @Test
    public void testHandle() throws InterruptedException {
        FirstFileWatcher watcher = new FirstFileWatcher();
        watcher.handleEvent();
    }

}
