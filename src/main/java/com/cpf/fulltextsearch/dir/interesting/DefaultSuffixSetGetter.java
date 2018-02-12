package com.cpf.fulltextsearch.dir.interesting;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class DefaultSuffixSetGetter implements SuffixSetGetter {

    private String configFile;

    public DefaultSuffixSetGetter(String configFile) {
        this.configFile = configFile;
    }

    @Override
    public Set<String> getSuffixSet() {
        try (BufferedReader reader = new BufferedReader(new FileReader(getClass().getClassLoader().getResource(configFile).getFile()))) {
            String suffixes = reader.readLine();
            return Arrays.stream(suffixes.split(",")).collect(Collectors.toCollection(HashSet::new));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new HashSet<>();
    }

}
