package com.kropotov.asrd.services.storages;

import com.kropotov.asrd.services.MinIOService;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.errors.XmlParserException;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class MinIOSeviceImp implements MinIOService {

    private final MinioClient minioClient;

    @Override
    public void uploadFile(String bucketname, String filename, byte[] content) {

    }

    @Override
    public byte[] downloadFile(String bucketname, String filename) {
        return new byte[0];
    }

    @Override
    public void deleteFile(String bucketname, String filename) {

    }

    @Override
    public void makeBucket(String bucketname) {

    }

    @Override
    public void removeBucket(String bucketname) {

    }


    @Override
    public Iterable<Result<Item>> listFiles(String bucketname) {

        Iterable<Result<Item>> results = null;
        try {
            results = minioClient.listObjects(bucketname);
        } catch (XmlParserException e) {
            e.printStackTrace();
        }
        return results;
    }


}
