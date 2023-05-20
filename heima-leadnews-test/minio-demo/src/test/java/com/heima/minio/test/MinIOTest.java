package com.heima.minio.test;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;

@SpringBootTest
public class MinIOTest {

    @Test
    public void testMinio() {
        FileInputStream fileInputStream = null;
        try {

            fileInputStream = new FileInputStream("C:\\Users\\redhe\\Pictures\\previewFix.jpg");

            //1.创建minio链接客户端
            MinioClient minioClient = MinioClient.builder().credentials("minioadmin", "minioadmin").endpoint("http://localhost:9000").build();
            //2.上传
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .object("previewFix.jpg")//文件名
                    .contentType("image/jpg")//文件类型
                    .bucket("test-bucket")//桶名词  与minio创建的名词一致
                    .stream(fileInputStream, fileInputStream.available(), -1) //文件流
                    .build();
            minioClient.putObject(putObjectArgs);

            String url = minioClient.getObjectUrl("test-bucket", "previewFix.jpg");

            System.out.println(url);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}