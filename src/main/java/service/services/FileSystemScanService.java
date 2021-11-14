package service.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import service.components.FileSystem;
import service.entities.FSDirectory;
import service.entities.FSFile;
import service.services.interfaces.IScanService;
import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Service
public class FileSystemScanService implements IScanService {
  private static final Logger LOGGER = LoggerFactory.getLogger(FileSystemScanService.class);

  @Value("${service.scanner.enable:false}")
  private boolean isScanEnabled;

  @Value("${service.scanner.path:}")
  private String path;

  @Value("${service.scanner.scanTo:/}")
  private String scanTo;

  @Value("${includeHiddenFiles:false}")
  private boolean includeHiddenFiles;

  @Autowired
  private FileSystem fileSystem;

  @PostConstruct
  public void init() {
    if (!isScanEnabled)
      return;
    File startDirectory = new File(path);
    try {
      scan(startDirectory, scanTo);
    } catch (IOException e) {
      LOGGER.error("Scan is failure! Check your options!", e);
    }
  }

  @Override
  public void scan(File directoryToScan, String fsDirectoryPathToAdding) throws IOException {
    LOGGER.info("Starting scanning directory: '{}' and put all files in '{}'", directoryToScan.getAbsolutePath(), fsDirectoryPathToAdding);
    if (!directoryToScan.exists() || !directoryToScan.isDirectory() || !directoryToScan.canRead())
      throw new IOException("Cannot start scan directory: " + directoryToScan.getAbsolutePath());

    for (File file: Objects.requireNonNull(directoryToScan.listFiles())) {
      if (!file.canRead()) {
        LOGGER.warn("Can't read file '{}'. Scan it will be skipped!", file.getAbsolutePath());
        continue;
      }

      if (!includeHiddenFiles && file.isHidden()) {
        LOGGER.warn("File '{}' is hidden. Scan it will be skipped!", file.getAbsolutePath());
        continue;
      }

      if (file.isDirectory()) {
        boolean fsDirectoryCreation = fileSystem.addFSObjectToPath(new FSDirectory(file.getName()), fsDirectoryPathToAdding);
        if (!fsDirectoryCreation) {
          LOGGER.warn("Can't create directory with name '{}' in FSDirectory '{}'. Skip...", file.getName(), fsDirectoryPathToAdding);
          continue;
        }
        if (fsDirectoryPathToAdding.equals(FileSystem.SEPARATOR))
          scan(file, fsDirectoryPathToAdding + file.getName());
        else
          scan(file, fsDirectoryPathToAdding + FileSystem.SEPARATOR + file.getName());
      } else {
        boolean fsFileCreation = fileSystem.addFSObjectToPath(new FSFile(file.getName(), file.toPath()), fsDirectoryPathToAdding);
        if (!fsFileCreation) {
          LOGGER.warn("Can't create file with name '{}' in FSDirectory '{}'. Skip...", file.getName(), fsDirectoryPathToAdding);
        }
      }
    }
    LOGGER.info("Scanning of directory: '{}' and put all files in '{}' completed!", directoryToScan.getAbsolutePath(), fsDirectoryPathToAdding);
  }
}
