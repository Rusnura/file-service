//package service.services;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import service.components.FileSystem;
//import service.entities.FSDirectory;
//import service.services.interfaces.IScanService;
//import java.io.File;
//import java.io.IOException;
//import java.util.Objects;
//
//@Service
//public class FileSystemScanService implements IScanService {
//  private static final Logger LOGGER = LoggerFactory.getLogger(FileSystemScanService.class);
//
//  @Autowired
//  private FileSystem fileSystem;
//
//  @Override
//  public void scan(File directoryToScan, String fsDirectoryPathToAdding, boolean recursively) throws IOException {
//    if (!directoryToScan.exists() || !directoryToScan.isDirectory() || !directoryToScan.canRead())
//      throw new IOException("Cannot start scan directory: " + directoryToScan.getAbsolutePath());
//
//    for (File file: Objects.requireNonNull(directoryToScan.listFiles())) {
//      if (recursively && file.isDirectory()) {
//        if (!file.canRead()) {
//          LOGGER.warn("Can't read directory '{}'. Scan it will be skipped!", file.getAbsolutePath());
//          continue;
//        }
//
//        boolean fsDirectoryCreation = fileSystem.addFSObjectToPath(new FSDirectory(file.getName()), fsDirectoryPathToAdding);
//        if (!fsDirectoryCreation) {
//          LOGGER.warn("Can't create directory with name '{}' in FSDirectory '{}'. Skip...", file.getName(), fsDirectoryPathToAdding);
//          continue;
//        }
//
//        scan(file,  recursively);
//      } else if (file.isFile()) {
//        if (!file.canRead()) {
//          LOGGER.warn("Can't read file '{}'. Adding !", file.getAbsolutePath());
//          continue;
//        }
//
//      }
//    }
//  }
//}
