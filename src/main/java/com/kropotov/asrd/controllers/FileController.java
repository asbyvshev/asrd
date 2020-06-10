package com.kropotov.asrd.controllers;

import com.kropotov.asrd.services.StorageService;
import com.kropotov.asrd.services.storages.FileServiceFasadImp;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;

@RequiredArgsConstructor
@Controller
@RequestMapping("/files")
public class FileController {
    private final StorageService storageService;
    private final FileServiceFasadImp fileServiceFasadImp;

    @GetMapping(value = "/{path}/{filename}")
    public ResponseEntity<byte[]> redirectToGetFile(@PathVariable String path, @PathVariable("filename") String filename) {
        String extension = filename.substring(filename.lastIndexOf('.') + 1);

        HttpHeaders headers = new HttpHeaders();

        switch (extension) {
            case "pdf":
                headers.setContentType(MediaType.APPLICATION_PDF);
                break;
            case "png":
                headers.setContentType(MediaType.IMAGE_PNG);
                break;
            case "jpg":
                headers.setContentType(MediaType.IMAGE_JPEG);
                break;
            case "jpeg":
                headers.setContentType(MediaType.IMAGE_JPEG);
                break;
            default:
                Resource file = storageService.loadAsResource(path, filename);
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.set(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFilename() + "\"");
        }
        try {
            return new ResponseEntity<>(Files.readAllBytes(storageService.load(path, filename)),
                    headers, HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException();
        }

    }

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String showAddFile (){
        return "minio";
    }


//    @ApiOperation(value = "Добавить новый продукт на витрину.", response = String.class)
    @PostMapping("/minioAdd")
    public String addOne(@RequestParam("doc") MultipartFile file,@RequestParam("title") String filename) throws IOException {
        fileServiceFasadImp.uploadFile(file,filename,null);
        return "redirect:/files/";
    }
}
