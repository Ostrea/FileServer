package my.ostrea.controllers;

import my.ostrea.Constants;
import my.ostrea.exceptions.FileDoesNotExist;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
public class FileDeleteController {
    @RequestMapping(value="/delete", method= RequestMethod.GET)
    public @ResponseBody String provideDeleteInfo() {
        return "You can delete a file by posting to this same URL.";
    }

    @RequestMapping(value="/delete", method=RequestMethod.POST)
    public @ResponseBody String  handleFileDelete(@RequestParam("uuid") String uuid) {
        UUID targetFileUuid = UUID.fromString(uuid);
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(
                Paths.get(Constants.ROOT_SERVER_FOLDER))) {
            for (Path currentFile : directoryStream) {
                if (!Files.isDirectory(currentFile)) {
                    UUID currentFileUuid = UUID.nameUUIDFromBytes(Files.readAllBytes(currentFile));
                    if (currentFileUuid.compareTo(targetFileUuid) == 0) {
                        Files.delete(currentFile);
                        return "File successfully deleted!";
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new FileDoesNotExist(uuid);
    }
}
