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
import service.helpers.FSObjectHelper;
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
    fileSystem.addFSObjectToPath(new FSFile("subFile1"), "/directory1");
    fileSystem.addFSObjectToPath(new FSFile("subFile2"), "/directory2");

    fileSystem.addFSObjectToPath(new FSDirectory("subSubdirectory1"), "/directory1/subdirectory1");
    fileSystem.addFSObjectToPath(new FSDirectory("subSubdirectory2"), "/directory2/subdirectory2");
    fileSystem.addFSObjectToPath(new FSFile("subSubFile1"), "/directory1/subdirectory1");
    fileSystem.addFSObjectToPath(new FSFile("subSubFile2"), "/directory2/subdirectory2");
  }

  @Test
  public void getFSDirectories() { // Get a directories
    FSObjectHelper.assertFSObject(fileSystem, "/", FSDirectory.class, true);
    FSObjectHelper.assertFSObject(fileSystem, "/directory1", FSDirectory.class, true);
    FSObjectHelper.assertFSObject(fileSystem, "/directory2", FSDirectory.class, true);
    FSObjectHelper.assertFSObject(fileSystem, "/directory1/subdirectory1", FSDirectory.class, true);
    FSObjectHelper.assertFSObject(fileSystem, "/directory2/subdirectory2", FSDirectory.class, true);
  }

  @Test
  public void getFSFiles() { // Get a files
    FSObjectHelper.assertFSObject(fileSystem, "/file1", FSFile.class, true);
    FSObjectHelper.assertFSObject(fileSystem, "/file2", FSFile.class, true);
    FSObjectHelper.assertFSObject(fileSystem, "/directory1/subFile1", FSFile.class, true);
    FSObjectHelper.assertFSObject(fileSystem, "/directory2/subFile2", FSFile.class, true);
    FSObjectHelper.assertFSObject(fileSystem, "/directory1/subdirectory1/subSubFile1", FSFile.class, true);
    FSObjectHelper.assertFSObject(fileSystem, "/directory2/subdirectory2/subSubFile2", FSFile.class, true);
  }

  @Test
  public void getFSFileUsingDotsWhenAfterDotsPathPresent() {
    Optional<? extends FSObject> subdirectory1Opt = fileSystem.getFSObjectByPath("/directory1/subdirectory1/../subdirectory1");
    Assertions.assertTrue(subdirectory1Opt.isPresent());
    FSObject subdirectory1 = subdirectory1Opt.get();
    Assertions.assertEquals("subdirectory1", subdirectory1.getName());
    Assertions.assertEquals("/directory1/subdirectory1", subdirectory1.getPath());
  }

  @Test
  public void getFSFileUsingTwoDotsWhenAfterDotsPathPresent() {
    Optional<? extends FSObject> directory1Opt = fileSystem.getFSObjectByPath("/directory1/subdirectory1/..");
    Assertions.assertTrue(directory1Opt.isPresent());
    FSObject subdirectory1 = directory1Opt.get();
    Assertions.assertEquals("directory1", subdirectory1.getName());
    Assertions.assertEquals("/directory1", subdirectory1.getPath());
  }

  @Test
  public void getFSFileUsingDotsWhenAfterDotsPathAbsent() {
    Optional<? extends FSObject> rootOpt = fileSystem.getFSObjectByPath("/directory1/subdirectory1/../..");
    Assertions.assertTrue(rootOpt.isPresent());
    FSObject subdirectory1 = rootOpt.get();
    Assertions.assertEquals("", subdirectory1.getName());
    Assertions.assertEquals("/", subdirectory1.getPath());
  }

  @Test
  public void getFSFileUsingDotsOnly() {
    Optional<? extends FSObject> rootOpt = fileSystem.getFSObjectByPath("..");
    Assertions.assertTrue(rootOpt.isPresent());
    FSObject subdirectory1 = rootOpt.get();
    Assertions.assertEquals("", subdirectory1.getName());
    Assertions.assertEquals("/", subdirectory1.getPath());
  }

  @Test
  public void getFSFileUsing2DotsOnly() {
    Optional<? extends FSObject> rootOpt = fileSystem.getFSObjectByPath("../..");
    Assertions.assertTrue(rootOpt.isPresent());
    FSObject subdirectory1 = rootOpt.get();
    Assertions.assertEquals("", subdirectory1.getName());
    Assertions.assertEquals("/", subdirectory1.getPath());
  }

  @Test
  public void getFSFileUsingDotsWhenAfterDotsPathAbsentAndPathWrong() {
    Optional<? extends FSObject> rootOpt = fileSystem.getFSObjectByPath("/directory1/subdirectory1/../../..");
    Assertions.assertTrue(rootOpt.isPresent());
    FSObject subdirectory1 = rootOpt.get();
    Assertions.assertEquals("", subdirectory1.getName());
    Assertions.assertEquals("/", subdirectory1.getPath());
  }

  @Test
  public void getRootDirectory() {
    FSObjectHelper.assertFSObject(fileSystem, "///////////", FSDirectory.class, false);

    Optional<? extends FSObject> fsRootOpt = fileSystem.getFSObjectByPath("///////////");
    Assertions.assertTrue(fsRootOpt.isEmpty());
  }

  @AfterEach
  public void destruct() {
    FileSystem.ROOT.getChildren().clear();
  }
}
