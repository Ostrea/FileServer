package my.ostrea.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUploadController {
    private static final String ROOT_SERVER_FOLDER = "/home/ostrea/serverFolder";

    @RequestMapping(value="/upload", method=RequestMethod.GET)
    public @ResponseBody String provideUploadInfo() {
        return "You can upload a file by posting to this same URL.";
    }

    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload(@RequestParam("name") String name,
                                                 @RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();

                if (checkForExistingFiles(UUID.nameUUIDFromBytes(bytes))) {
                    throw new RuntimeException("File with such content already exists!");
                }

                try (BufferedOutputStream stream =
                             new BufferedOutputStream(
                                     new FileOutputStream(new File(ROOT_SERVER_FOLDER + "/" + name)))) {
                    stream.write(bytes);
                }

                return "You successfully uploaded " + name + "!";
            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + name + " because the file was empty.";
        }
    }

    private boolean checkForExistingFiles(UUID userUploadedFileUuid) {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(ROOT_SERVER_FOLDER))) {
            for (Path currentFile : directoryStream) {
                if (!Files.isDirectory(currentFile)) {
                    UUID currentFileUuid = UUID.nameUUIDFromBytes(Files.readAllBytes(currentFile));
                    if (currentFileUuid.compareTo(userUploadedFileUuid) == 0) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

}
