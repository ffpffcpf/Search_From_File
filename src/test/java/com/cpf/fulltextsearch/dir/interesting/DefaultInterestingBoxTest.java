package com.cpf.fulltextsearch.dir.interesting;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.nio.file.Path;
import java.nio.file.Paths;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class DefaultInterestingBoxTest {

    @Autowired
    private InterestingBox box;

    @Test
    public void should_return_true_when_watcher_interesting_the_file(){
       Path path =  Paths.get("D:\\GitResource\\full-text-search\\src\\test\\resources\\hello.json");
       Assert.assertEquals(true, box.isInteresting(path));
    }
}
