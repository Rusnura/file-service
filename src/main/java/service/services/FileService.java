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

    public List<service.models.File> getFiles(String path, boolean showHiddenFiles) throws IOException {
        File directory = new File(baseDirectory, path);
        checkAvailability(baseDirectory, directory);
        if (!directory.isDirectory())
            throw new IOException(directory.getName() + " isn't directory, but file!");

        File[] directoryFiles = directory.listFiles();
        List<service.models.File> files = new LinkedList<>();
        if (directoryFiles != null) {
            for (File file : directoryFiles) {
                if (showHiddenFiles || !file.isHidden()) {
                    files.add(new service.models.File(file));
                }
            }
        }
        return files;
    }

    private void checkAvailability(File baseDirectory, File file) throws IOException {
        if (!baseDirectory.exists() || !baseDirectory.canRead() || !baseDirectory.isDirectory())
            throw new IOException("Base directory isn't available!");

        if (!file.getCanonicalPath().contains(baseDirectory.getCanonicalPath()))
            throw new IOException("File: " + file.getName() + " isn't allowed!");

        if (!file.exists())
            throw new IOException("File: " + file.getName() + " doesn't exists!");

        if (!file.canRead())
            throw new IOException("File: " + file.getName() + " doesn't readable!");
    }
}
