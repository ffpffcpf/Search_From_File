package com.cpf.fulltextsearch.search;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class IndexFilesTest {


    @Test
    public void should_return_contain_text_file(){
        //given
        File expectedFile = new File("D:\\file_watcher_test\\hello.txt");
        File file = new File("D:\\file_watcher_test\\nice.txt");
        String searchWord = "好";
        //when
//        IndexUtils.addFileIntoIndex(expectedFile);
//        IndexUtils.addFileIntoIndex(file);

        List<HitResult> results = IndexUtils.searchFiles(searchWord);

        //then
        for(HitResult hit: results){
            Assert.assertTrue(hit.getContent().contains(searchWord));
            Assert.assertEquals(expectedFile.getAbsolutePath(), hit.getFile().getAbsolutePath());
        }
    }

}
