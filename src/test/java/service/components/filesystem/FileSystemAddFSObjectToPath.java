package service.components.filesystem;

import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import service.components.FileSystem;
import service.entities.FSFile;
import service.entities.FSObject;
import java.util.Optional;

@SpringBootTest
public class FileSystemAddFSObjectToPath {
  @Autowired
  private FileSystem fileSystem;

  @Test
  public void addFile() { // Just put a new file to root directory
    FSFile file = new FSFile("file_in_root");
    fileSystem.addFSObjectToPath(file, "/");

    Optional<? extends FSObject> fileFromFileSystemOpt = fileSystem.getFSObjectByPath("/file_in_root");
    Assertions.assertTrue(fileFromFileSystemOpt.isPresent());

    FSObject fileFromFileSystem = fileFromFileSystemOpt.get();
    Assertions.assertTrue(fileFromFileSystem instanceof FSFile);
  }
}
