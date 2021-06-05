package service.components.filesystem;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import service.components.FileSystem;
import service.entities.FSDirectory;
import service.entities.FSFile;
import service.entities.FSObject;

import java.util.Optional;

@SpringBootTest
public class FileSystemGetFSObject {
  @Autowired
  private FileSystem fileSystem;

  @BeforeEach
  public void init() {
    fileSystem.addFSObjectToPath(new FSDirectory("directory1"), "/");
    fileSystem.addFSObjectToPath(new FSDirectory("directory2"), "/");
    fileSystem.addFSObjectToPath(new FSFile("file1"), "/");
    fileSystem.addFSObjectToPath(new FSFile("file2"), "/");

    fileSystem.addFSObjectToPath(new FSDirectory("subdirectory1"), "/directory1");
    fileSystem.addFSObjectToPath(new FSDirectory("subdirectory2"), "/directory2");
    fileSystem.addFSObjectToPath(new FSFile("subfile1"), "/directory1");
    fileSystem.addFSObjectToPath(new FSFile("subfile2"), "/directory2");

    fileSystem.addFSObjectToPath(new FSDirectory("subSubdirectory1"), "/directory1/subdirectory1");
    fileSystem.addFSObjectToPath(new FSDirectory("subSubdirectory2"), "/directory2/subdirectory2");
    fileSystem.addFSObjectToPath(new FSFile("subSubfile1"), "/directory1/subdirectory1");
    fileSystem.addFSObjectToPath(new FSFile("subSubfile2"), "/directory2/subdirectory2");
  }

  @Test
  public void getFSDirectories() { // Get a directories
    getFSDirectory("/");
    getFSDirectory("/directory1");
    getFSDirectory("/directory2");
    getFSDirectory("/directory1/subdirectory1");
    getFSDirectory("/directory2/subdirectory2");
  }

  @Test
  public void getFSFiles() { // Get a files
    getFSFile("/file1");
    getFSFile("/file2");
    getFSFile("/directory1/subfile1");
    getFSFile("/directory2/subfile2");
    getFSFile("/directory1/subdirectory1/subSubfile1");
    getFSFile("/directory2/subdirectory2/subSubfile2");
  }

  private void getFSDirectory(String path) {
    final Optional<? extends FSObject> directoryOpt = fileSystem.getFSObjectByPath(path);
    Assertions.assertTrue(directoryOpt.isPresent());
    Assertions.assertTrue(directoryOpt.get() instanceof FSDirectory);
  }

  private void getFSFile(String path) {
    final Optional<? extends FSObject> directoryOpt = fileSystem.getFSObjectByPath(path);
    Assertions.assertTrue(directoryOpt.isPresent());
    Assertions.assertTrue(directoryOpt.get() instanceof FSFile);
  }
}
