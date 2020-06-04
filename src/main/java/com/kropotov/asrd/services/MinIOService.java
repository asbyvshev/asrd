package com.kropotov.asrd.services;

import io.minio.Result;
import io.minio.messages.Item;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface MinIOService {

    void uploadFile(MultipartFile file, String bucketname, String filename) throws IOException;
    byte[] downloadFile(String bucketname,String filename);
    void deleteFile(String bucketname,String filename);
    void makeBucket(String bucketname);
    void removeBucket(String bucketname);
    Iterable<Result<Item>> listFiles(String bucketname);
}
