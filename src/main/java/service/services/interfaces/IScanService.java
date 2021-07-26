package service.services.interfaces;

import service.entities.FSDirectory;

import java.io.File;
import java.io.IOException;

public interface IScanService {
  void scan(File directoryToScan, String fsDirectoryPathToAdding) throws IOException;
}
