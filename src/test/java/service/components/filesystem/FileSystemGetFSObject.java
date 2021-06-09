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
  public void getRootDirectory() {
    FSObjectHelper.assertFSObject(fileSystem, "///////////", FSDirectory.class, true);

    Optional<? extends FSObject> fsRootOpt = fileSystem.getFSObjectByPath("///////////");
    Assertions.assertTrue(fsRootOpt.isPresent());
    Assertions.assertTrue(fsRootOpt.get() instanceof FSDirectory);
    Assertions.assertEquals(fsRootOpt.get(), FileSystem.ROOT);
  }
}
