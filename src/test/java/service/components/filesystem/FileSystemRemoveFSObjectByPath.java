package service.components.filesystem;

import org.junit.jupiter.api.AfterEach;
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
public class FileSystemRemoveFSObjectByPath {
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
    fileSystem.addFSObjectToPath(new FSFile("subFile1"), "/directory1");
    fileSystem.addFSObjectToPath(new FSFile("subFile2"), "/directory2");

    fileSystem.addFSObjectToPath(new FSDirectory("subSubdirectory1"), "/directory1/subdirectory1");
    fileSystem.addFSObjectToPath(new FSDirectory("subSubdirectory2"), "/directory2/subdirectory2");
    fileSystem.addFSObjectToPath(new FSFile("subSubFile1"), "/directory1/subdirectory1");
    fileSystem.addFSObjectToPath(new FSFile("subSubFile2"), "/directory2/subdirectory2");
  }

  @Test
  public void deleteFileFromRoot() {
    Optional<? extends FSObject> secondFileInRootBeforeDeletionOpt = fileSystem.getFSObjectByPath("/file2");
    Assertions.assertTrue(secondFileInRootBeforeDeletionOpt.isPresent());
    Assertions.assertTrue(fileSystem.removeFSObjectByPath("/file2"));
    Optional<? extends FSObject> secondFileInRootAfterDeletionOpt = fileSystem.getFSObjectByPath("/file2");
    Assertions.assertTrue(secondFileInRootAfterDeletionOpt.isEmpty());
  }

  @Test
  public void deleteFilesFromRoot() {
    Optional<? extends FSObject> firstFileInRootBeforeDeletionOpt = fileSystem.getFSObjectByPath("/file1");
    Assertions.assertTrue(firstFileInRootBeforeDeletionOpt.isPresent());
    Assertions.assertTrue(fileSystem.removeFSObjectByPath("/file1"));
    Optional<? extends FSObject> firstFileInRootAfterDeletionOpt = fileSystem.getFSObjectByPath("/file1");
    Assertions.assertTrue(firstFileInRootAfterDeletionOpt.isEmpty());

    Optional<? extends FSObject> secondFileInRootBeforeDeletionOpt = fileSystem.getFSObjectByPath("/file2");
    Assertions.assertTrue(secondFileInRootBeforeDeletionOpt.isPresent());
    Assertions.assertTrue(fileSystem.removeFSObjectByPath("/file2"));
    Optional<? extends FSObject> secondFileInRootAfterDeletionOpt = fileSystem.getFSObjectByPath("/file2");
    Assertions.assertTrue(secondFileInRootAfterDeletionOpt.isEmpty());
  }

  @Test
  public void deleteEmptyFromRoot() {
    fileSystem.addFSObjectToPath(new FSDirectory("an empty folder"), "/");

    Optional<? extends FSObject> folderInRootBeforeDeletionOpt = fileSystem.getFSObjectByPath("/an empty folder");
    Assertions.assertTrue(folderInRootBeforeDeletionOpt.isPresent());
    Assertions.assertTrue(folderInRootBeforeDeletionOpt.get() instanceof FSDirectory);
    Assertions.assertTrue(fileSystem.removeFSObjectByPath("/an empty folder"));
    Optional<? extends FSObject> folderInRootAfterDeletionOpt = fileSystem.getFSObjectByPath("/an empty folder");
    Assertions.assertTrue(folderInRootAfterDeletionOpt.isEmpty());
  }

  @Test
  public void deleteNonEmptyFromRoot() {
    Optional<? extends FSObject> folderInRootBeforeDeletionOpt = fileSystem.getFSObjectByPath("/directory1");
    Assertions.assertTrue(folderInRootBeforeDeletionOpt.isPresent());
    Assertions.assertTrue(folderInRootBeforeDeletionOpt.get() instanceof FSDirectory);
    Assertions.assertTrue(((FSDirectory) folderInRootBeforeDeletionOpt.get()).getChildren().size() > 0);
    Assertions.assertTrue(fileSystem.removeFSObjectByPath("/directory1"));
    Optional<? extends FSObject> folderInRootAfterDeletionOpt = fileSystem.getFSObjectByPath("/directory1");
    Assertions.assertTrue(folderInRootAfterDeletionOpt.isEmpty());
  }

  @AfterEach
  public void destruct() {
    FileSystem.ROOT.getChildren().clear();
  }
}
