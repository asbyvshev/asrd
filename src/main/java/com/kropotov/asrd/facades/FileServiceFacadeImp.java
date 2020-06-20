package com.kropotov.asrd.facades;

import com.kropotov.asrd.entities.docs.File;
import com.kropotov.asrd.exceptions.StorageException;
import com.kropotov.asrd.services.FileService;
import com.kropotov.asrd.services.MinIOService;

import io.minio.errors.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceFacadeImp implements FileServiceFacade {

    private final FileService fileService;
    private final MinIOService minIOService;

    @Override
    public File uploadFile (MultipartFile doc, File file)  {

        if (doc.isEmpty()) {
            throw new StorageException("No file selected!");
        }

        String bucket = file.getType().getDirectory();
        String fileName = createFileName(bucket,doc.getOriginalFilename());
        file.setTitle(fileName);

        try {
            minIOService.uploadFile(doc, bucket, fileName);
            return fileService.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public byte[] downloadFile(Long id) {
        File file = fileService.getById(id).get();
        String bucket = file.getType().getDirectory();
        String filename = file.getTitle();
        try {
            return minIOService.downloadFile(bucket,filename);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (XmlParserException e) {
            e.printStackTrace();
        } catch (InvalidBucketNameException e) {
            e.printStackTrace();
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteFile(Long id) {
        File file = fileService.getById(id).get();
        String bucket = file.getType().getDirectory();
        String filename = file.getTitle();
        try {
            minIOService.deleteFile(bucket,filename);
            fileService.deleteById(id);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (XmlParserException e) {
            e.printStackTrace();
        } catch (InvalidBucketNameException e) {
            e.printStackTrace();
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        }
    }

    // TODO: 20.06.2020 если потребуется возвращать ссылку
    private void findObject(String b,String f){
            System.out.println(minIOService.getUrl(b, f));
    }

//    тестовый метод
    public void up (MultipartFile doc, String userFileName) throws IOException {
        if (doc.isEmpty()) {
            throw new IOException("no file selected");
        }
        System.out.println(UUID.randomUUID() + "_" + doc.getOriginalFilename());


        String bucket ="asbyvshev-test-bucket";
        if (minIOService.fileExists(bucket,userFileName)){
            throw new StorageException("file is already exist!");
        }else {
            try {
                minIOService.uploadFile(doc, bucket, userFileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


//        String fileName = type.getDirectory() + currentDate() + "." + determineFileExtension(file);
//        minIOService.uploadFile(file, fileName, type.getDirectory());
//        fileService.save(new File(fileName, userFileName, type));
//        String bucket ="asbyvshev-test-bucket";
//        minIOService.uploadFile(file,bucket,"1234/"+userFileName);
//        findObject(bucket,userFileName);
    }

    private String createFileName(String bucket,String filename){
       if (!minIOService.fileExists(bucket,filename)){
           return filename;
       }
       filename = UUID.randomUUID() +  filename.substring(filename.lastIndexOf('.'));
       return createFileName(bucket,filename);
    }
}
