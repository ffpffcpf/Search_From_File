package com.cpf.fulltextsearch.search;

import lombok.Data;

import java.io.File;

@Data
public class HitResult {

    private String content;

    private File file;


}
