package com.heima.wemedia;


import com.heima.common.aliyun.GreenImageScan;
import com.heima.common.aliyun.GreenTextScan;
import com.heima.file.service.FileStorageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class ScanTest {
    @Autowired
    GreenTextScan greenTextScan;
    @Autowired
    GreenImageScan greenImageScan;
    @Autowired
    FileStorageService fileStorageService;

    //文本测试
    @Test
    void textTest() throws Exception {
        Map map = greenTextScan.greeTextScan("吸毒");
        System.out.println("map = " + map);
    }

    //图片测试
    @Test
    void imageTest() throws Exception {
        List<byte[]> imageList = new ArrayList<>();
        byte[] bytes = fileStorageService.downLoadFile("http://localhost:9000/leadnews/b318389e879861d58bb76dd252248ab2.jpeg");
        imageList.add(bytes);
        Map map = greenImageScan.imageScan(imageList);
        System.out.println("map = " + map);

    }
}
