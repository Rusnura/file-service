package service.helpers;

import org.junit.jupiter.api.Assertions;
import service.components.FileSystem;
import service.entities.FSDirectory;
import service.entities.FSFile;
import service.entities.FSObject;
import java.util.Optional;

public class FSObjectHelper {
  public static<T> void assertFSObject(FileSystem fs, String path, T type, boolean isExists) {
    final Optional<? extends FSObject> fsObjectOpt = fs.getFSObjectByPath(path);
    if (!isExists) {
      Assertions.assertFalse(fsObjectOpt.isPresent());
      return;
    }
    Assertions.assertTrue(fsObjectOpt.isPresent());

    if (type instanceof FSDirectory) {
      Assertions.assertTrue(fsObjectOpt.get() instanceof FSDirectory);
    } else if (type instanceof FSFile) {
      Assertions.assertTrue(fsObjectOpt.get() instanceof FSFile);
    }

    String[] segments = path.split("/");
    String name = segments.length > 0 ? segments[segments.length - 1] : "";
    Assertions.assertEquals(name, fsObjectOpt.get().getName());
  }
}
