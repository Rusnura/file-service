package service.components.filesystem;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import service.components.FileSystem;

@SpringBootTest
public class FileSystemTest {
  @Test
  public void fileSystemRootIsNotNullTest() { // Test: FileSystem.root is not null
    Assertions.assertNotNull(FileSystem.root);
  }

  @Test
  public void fileSystemRootNameIsEmpty() { // Test: FileSystem.root has empty name
    Assertions.assertEquals("", FileSystem.root.getName());
  }

  @Test
  public void fileSystemRootParentIsNull() { // Test: FileSystem.root has null parent
    Assertions.assertNull(FileSystem.root.getParent());
  }
}
