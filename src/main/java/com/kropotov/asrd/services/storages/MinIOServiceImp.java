package com.kropotov.asrd.services.storages;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.kropotov.asrd.services.MinIOService;
import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import io.minio.Result;
import io.minio.errors.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class MinIOServiceImp implements MinIOService {

    private final MinioClient minioClient;

    @Override
    public void uploadFile(MultipartFile file, String bucketName, String filename) throws IOException {
        try (InputStream stream = file.getInputStream()){

            if (!minioClient.bucketExists(bucketName)) {
                minioClient.makeBucket(bucketName);
            }
            Long fileSize = file.getSize();
            PutObjectOptions options = new PutObjectOptions(fileSize, -1);
            minioClient.putObject(bucketName,filename,stream,options);
            log.info("File {} has been succesfully uploaded!", filename);

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
        } catch (RegionConflictException e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] downloadFile(String bucketName, String filename) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException {
        InputStream inputStream = minioClient.getObject(bucketName,filename);
        byte[] bytes = StreamUtils.copyToByteArray(inputStream);
        return bytes;
    }

    @Override
    public void deleteFile(String bucketName, String filename) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, InternalException, XmlParserException, ErrorResponseException, InvalidBucketNameException {
        minioClient.removeObject(bucketName,filename);
    }

    @Override
    public void makeBucket(String bucketName) throws IOException, InvalidKeyException, InvalidResponseException, RegionConflictException, InsufficientDataException, NoSuchAlgorithmException, InternalException, XmlParserException, ErrorResponseException, InvalidBucketNameException {
        minioClient.makeBucket(bucketName);
    }

    @Override
    public void removeBucket(String bucketName) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, InternalException, XmlParserException, ErrorResponseException, InvalidBucketNameException {

            minioClient.removeBucket(bucketName);
    }

    @Override
    public Iterable<Result<Item>> listFiles(String bucketName) {

        Iterable<Result<Item>> results = null;
        try {
            results = minioClient.listObjects(bucketName);
        } catch (XmlParserException e) {
            e.printStackTrace();
        }
        return results;
    }

    public String getUrl (String b,String f) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException {
//        return minioClient.getObjectUrl(b,f);
        return null;
    }

    public boolean fileExists (String bucket,String file){
        try {
            Iterable<Result<Item>> list = minioClient.listObjects(bucket);
            for (Result r:list) {
                Item item = (Item) r.get();
                System.out.println(item.objectName());
                if (item.objectName().equals(file)){return true;}
            }
        } catch (XmlParserException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        } catch (InvalidBucketNameException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
