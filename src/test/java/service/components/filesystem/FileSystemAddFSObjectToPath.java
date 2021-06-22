package service.components.filesystem;

import org.junit.jupiter.api.AfterEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import service.components.FileSystem;
import service.entities.FSDirectory;
import service.entities.FSFile;
import service.entities.FSObject;
import java.util.Optional;
import java.util.UUID;

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

  @Test
  public void addNotUniqueFile() {
    final String filename = UUID.randomUUID().toString();
    FSFile file = new FSFile(filename);
    Assertions.assertTrue(fileSystem.addFSObjectToPath(file, "/"));

    Optional<? extends FSObject> uniqueFileOpt = fileSystem.getFSObjectByPath("/" + filename);
    Assertions.assertTrue(uniqueFileOpt.isPresent());

    FSFile nonUniqueFile = new FSFile(filename);
    Assertions.assertFalse(fileSystem.addFSObjectToPath(nonUniqueFile, "/"));

    Optional<? extends FSObject> nonUniqueFileOpt = fileSystem.getFSObjectByPath("/" + filename);
    Assertions.assertTrue(nonUniqueFileOpt.isPresent());
    Assertions.assertEquals(1, FileSystem.ROOT.getChildren().size());
  }

  @Test
  public void addNotUniqueDirectory() {
    final String filename = UUID.randomUUID().toString();
    FSDirectory directory = new FSDirectory(filename);
    Assertions.assertTrue(fileSystem.addFSObjectToPath(directory, "/"));

    Optional<? extends FSObject> uniqueFileOpt = fileSystem.getFSObjectByPath("/" + filename);
    Assertions.assertTrue(uniqueFileOpt.isPresent());

    FSDirectory nonUniqueDirectory = new FSDirectory(filename);
    Assertions.assertFalse(fileSystem.addFSObjectToPath(nonUniqueDirectory, "/"));

    Optional<? extends FSObject> nonUniqueDirectoryOpt = fileSystem.getFSObjectByPath("/" + filename);
    Assertions.assertTrue(nonUniqueDirectoryOpt.isPresent());
    Assertions.assertEquals(1, FileSystem.ROOT.getChildren().size());
  }

  @Test
  public void addNotUniqueFSObject() {
    final String filename = UUID.randomUUID().toString();
    FSDirectory directory = new FSDirectory(filename);
    Assertions.assertTrue(fileSystem.addFSObjectToPath(directory, "/"));

    Optional<? extends FSObject> uniqueDirectoryOpt = fileSystem.getFSObjectByPath("/" + filename);
    Assertions.assertTrue(uniqueDirectoryOpt.isPresent());

    FSFile nonUniqueFile = new FSFile(filename);
    Assertions.assertFalse(fileSystem.addFSObjectToPath(nonUniqueFile, "/"));

    Optional<? extends FSObject> nonUniqueFileOpt = fileSystem.getFSObjectByPath("/" + filename);
    Assertions.assertTrue(nonUniqueFileOpt.isPresent());
    Assertions.assertEquals(1, FileSystem.ROOT.getChildren().size());
  }

  @AfterEach
  public void destruct() {
    FileSystem.ROOT.getChildren().clear();
  }
}
