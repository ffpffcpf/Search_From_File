package com.cpf.fulltextsearch.dir.listener;

import java.nio.file.WatchEvent;

public interface EventListener {

    void handleEvent(WatchEvent<?> event);

}
