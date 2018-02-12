package com.cpf.fulltextsearch.dir.interesting;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

@ConditionalOnMissingBean(InterestingBox.class)
@Data
public class DefaultInterestingBox implements InterestingBox {
    private Set<String> suffix = new HashSet<>();

    private SuffixSetGetter suffixSetGetter;

    public DefaultInterestingBox(){
        suffix = suffixSetGetter.getSuffixSet();
    }

    @Override
    public boolean isInteresting(Path path) {
        String actualSuffix = path.toString().substring(path.toString().lastIndexOf("."));
        return suffix.contains(actualSuffix);
    }

}
