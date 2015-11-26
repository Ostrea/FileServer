package my.ostrea.controllers;

import my.ostrea.Constants;
import my.ostrea.exceptions.CanNotFindFileWithSpecifiedName;
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
public class FileSearchController {
    @RequestMapping(value="/search", method= RequestMethod.GET)
    public @ResponseBody
    String provideSearchInfo() {
        return "You can search a file by posting to this same URL.";
    }

    @RequestMapping(value="/search", method=RequestMethod.POST)
    public @ResponseBody String  handleFileSearch(@RequestParam("name") String name) {
        StringBuilder result = new StringBuilder();
        int numberOfFiles = 0;
        
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(
                Paths.get(Constants.ROOT_SERVER_FOLDER))) {
            for (Path currentFile : directoryStream) {
                if (!Files.isDirectory(currentFile)) {
                    String fileName = currentFile.getFileName().toString();
                    if (fileName.contains(name) && numberOfFiles < 25) {
                        numberOfFiles++;
                        UUID currentFileUuid = UUID.nameUUIDFromBytes(Files.readAllBytes(currentFile));
                        result.append("File name: ").append(fileName).append(" UUID: ")
                                .append(currentFileUuid).append('\n');
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (result.length() == 0) {
            throw new CanNotFindFileWithSpecifiedName(name);
        }
        return result.toString();
    }
}
