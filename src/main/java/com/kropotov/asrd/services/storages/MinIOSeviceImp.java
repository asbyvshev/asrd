package com.kropotov.asrd.services.storages;

import com.kropotov.asrd.services.MinIOService;
import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import io.minio.Result;
import io.minio.errors.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


@Slf4j
@Service
@RequiredArgsConstructor
public class MinIOSeviceImp implements MinIOService {

    private final MinioClient minioClient;

    @Override
    public void uploadFile(MultipartFile file, String bucketname, String filename) throws IOException {

        InputStream stream = file.getInputStream();
        Long fileSize = file.getSize();
        PutObjectOptions options = new PutObjectOptions(fileSize, fileSize);
        try {
            minioClient.putObject(bucketname,filename,stream,options);
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (InvalidBucketNameException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (XmlParserException e) {
            e.printStackTrace();
        }

        log.info("File {} has been succesfully uploaded!", filename);
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
