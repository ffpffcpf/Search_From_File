package com.cpf.fulltextsearch.dir.configuration;

import com.cpf.fulltextsearch.dir.interesting.DefaultInterestingBox;
import com.cpf.fulltextsearch.dir.interesting.DefaultSuffixSetGetter;
import com.cpf.fulltextsearch.dir.interesting.InterestingBox;
import com.cpf.fulltextsearch.dir.interesting.SuffixSetGetter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DirConfiguration {

    @Value("${suffix.path}")
    private String configFile;

    @Bean
    @ConditionalOnMissingBean(InterestingBox.class)
    public InterestingBox getDefaultInterestingBox(){
        return new DefaultInterestingBox();
    }

    @Bean
    @ConditionalOnMissingBean(SuffixSetGetter.class)
    public SuffixSetGetter getDefaultSuffixSetGetter(){
        return new DefaultSuffixSetGetter(configFile);
    }

}
