package service.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.components.FileSystem;
import service.entities.FSDirectory;
import service.entities.FSFile;
import service.services.interfaces.IScanService;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Service
public class FileSystemScanService implements IScanService {
  private static final Logger LOGGER = LoggerFactory.getLogger(FileSystemScanService.class);

  @Autowired
  private FileSystem fileSystem;

  @Override
  public void scan(File directoryToScan, String fsDirectoryPathToAdding) throws IOException {
    if (!directoryToScan.exists() || !directoryToScan.isDirectory() || !directoryToScan.canRead())
      throw new IOException("Cannot start scan directory: " + directoryToScan.getAbsolutePath());

    for (File file: Objects.requireNonNull(directoryToScan.listFiles())) {
      if (!file.canRead()) {
        LOGGER.warn("Can't read file '{}'. Scan it will be skipped!", file.getAbsolutePath());
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
        boolean fsFileCreation = fileSystem.addFSObjectToPath(new FSFile(file.getName()), fsDirectoryPathToAdding);
        if (!fsFileCreation) {
          LOGGER.warn("Can't create file with name '{}' in FSDirectory '{}'. Skip...", file.getName(), fsDirectoryPathToAdding);
        }
      }
    }
  }
}
