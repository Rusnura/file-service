package service.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileService {
    @Value("${service.base-dir}")
    private String baseDirectoryPropertyValue;
    private File baseDirectory;

    @PostConstruct
    private void init() {
        this.baseDirectory = new File(baseDirectoryPropertyValue);
    }

    public List<service.models.File> getFiles(String path) throws IllegalAccessException, IOException {
        File directory = new File(baseDirectory, path);
        if (!directory.getCanonicalPath().contains(baseDirectory.getCanonicalPath()))
            throw new IllegalAccessException("Directory: " + path + " isn't allowed!");

        if (!directory.exists())
            throw new IllegalAccessException("Directory: " + path + " doesn't exists!");

        if (!directory.canRead())
            throw new IllegalAccessException("Directory: " + path + " doesn't readable!");

        if (!directory.isDirectory())
            throw new IllegalAccessException(path + " isn't directory, but file!");

        File[] directoryFiles = directory.listFiles();
        List<service.models.File> files = new LinkedList<>();
        if (directoryFiles != null) {
            for (File file : directoryFiles) {
                files.add(new service.models.File(file));
            }
        }
        return files;
    }
}
