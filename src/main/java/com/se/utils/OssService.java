package com.se.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

@Service
public class OssService {
    @Autowired
    private OSS ossClient;

    private String BUCKETNAME = "se-resource-bucket";

    @Value("${alibaba.cloud.oss.endpoint}")
    private String ENDPOINT;

    public String uploadFile(String objectName, InputStream in) {
        objectName = UUID.randomUUID().toString() + objectName.substring(objectName.lastIndexOf("."));
//        System.out.println("filename = " + objectName);
        PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKETNAME, objectName, in);
        ossClient.putObject(putObjectRequest);
        ossClient.setObjectAcl(BUCKETNAME, objectName, CannedAccessControlList.PublicRead);
        return getFileUrl(objectName);
    }

    public String getFileUrl(String fileName) {
        return "https://" + BUCKETNAME + "." + ENDPOINT.substring(ENDPOINT.lastIndexOf("/") + 1) + "/" + fileName;
    }
}
