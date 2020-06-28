package com.kropotov.asrd.controllers;

import com.kropotov.asrd.dto.items.DeviceDto;
import com.kropotov.asrd.entities.docs.File;
import com.kropotov.asrd.facades.FileServiceFacade;
import com.kropotov.asrd.services.StorageService;
import com.kropotov.asrd.facades.FileServiceFacadeImp;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

@RequiredArgsConstructor
@Controller
@RequestMapping("/files")
public class FileController {

    private final StorageService storageService;
    private final FileServiceFacadeImp fileServiceFacadeImp;
    private final FileServiceFacade fileServiceFacade;


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
    public String showAddFile (Model model){
        model.addAttribute("fileTypes",
                fileServiceFacade.getAllFileTypes().orElse(new ArrayList<>()));
        return "minio";
    }

    // TODO: 21.06.2020 сделать добавление сохраненного файла в список файлов прибора 
//    @ApiOperation(value = "Добавить новый продукт на витрину.", response = String.class)
    @PostMapping("/minioAdd")
    public String addOne(@RequestParam("doc") MultipartFile doc, File file) {
        fileServiceFacade.uploadFile(doc,file);
        return "redirect:/files/";
    }

    @PostMapping("/minioAdd2")
    public String addOne2(@ModelAttribute("device") DeviceDto deviceDto, @RequestParam("doc") MultipartFile doc,
                          File file, Model model, HttpServletRequest request) {

        DeviceDto sessionDevice = (DeviceDto) request.getSession().getAttribute("device");
        sessionDevice.addFile(fileServiceFacade.uploadFile(doc,file));
        deviceDto.setFiles(sessionDevice.getFiles());
        model.addAttribute("device",deviceDto);

        return "devices/edit-device";
    }

    @PostMapping("/minioAdd1")
    public String addOne1(@RequestParam("doc") MultipartFile doc, @RequestParam("title") String filename) {
        try {
            fileServiceFacadeImp.up(doc,filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/files/";
    }
}
