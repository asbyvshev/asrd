package com.kropotov.asrd.services;

import io.minio.Result;
import io.minio.messages.Item;

public interface MinIOService {

    void uploadFile(String bucketname,String filename, byte[] content);
    byte[] downloadFile(String bucketname,String filename);
    void deleteFile(String bucketname,String filename);
    void makeBucket(String bucketname);
    void removeBucket(String bucketname);
    Iterable<Result<Item>> listFiles(String bucketname);
}
