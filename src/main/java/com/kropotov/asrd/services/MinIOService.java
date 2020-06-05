package com.kropotov.asrd.services;

import io.minio.Result;
import io.minio.errors.*;
import io.minio.messages.Item;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface MinIOService {

    void uploadFile(MultipartFile file, String bucketname, String filename) throws IOException;
    byte[] downloadFile(String bucketname,String filename) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException;
    void deleteFile(String bucketname,String filename) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException;
    void makeBucket(String bucketname) throws IOException, InvalidKeyException, InvalidResponseException, RegionConflictException, InsufficientDataException, NoSuchAlgorithmException, InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException;
    void removeBucket(String bucketname) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException;
    Iterable<Result<Item>> listFiles(String bucketname);
}
