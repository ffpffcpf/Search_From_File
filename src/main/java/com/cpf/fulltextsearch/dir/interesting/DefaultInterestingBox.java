package com.cpf.fulltextsearch.dir.interesting;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.file.Path;

@Data
public class DefaultInterestingBox implements InterestingBox {

    @Autowired
    private SuffixSetGetter suffixSetGetter;


    @Override
    public boolean isInteresting(Path path) {
        String actualSuffix = path.toString().substring(path.toString().lastIndexOf(".") + 1);
        return suffixSetGetter.getSuffixSet().contains(actualSuffix);
    }

}
