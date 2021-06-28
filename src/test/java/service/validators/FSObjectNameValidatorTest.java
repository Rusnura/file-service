package service.validators;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import service.entities.FSDirectory;
import service.entities.FSFile;
import service.validators.interfaces.IValidator;

@SpringBootTest
public class FSObjectNameValidatorTest {
  public static final IValidator fsObjectNameValidator = new FSObjectNameValidator();

  @Test
  public void correctFSFileValidation() {
    Assertions.assertTrue(fsObjectNameValidator.validate(new FSFile("correct")));
    Assertions.assertTrue(fsObjectNameValidator.validate(new FSFile("correct.file")));
    Assertions.assertTrue(fsObjectNameValidator.validate(new FSFile("correct.file.file")));
    Assertions.assertTrue(fsObjectNameValidator.validate(new FSFile("correct with spaces.file.file")));
    Assertions.assertTrue(fsObjectNameValidator.validate(new FSFile("correct_with_underscores.file.file")));
    Assertions.assertTrue(fsObjectNameValidator.validate(new FSFile(".correct")));
  }

  @Test
  public void incorrectFSFileValidation() {
    Assertions.assertFalse(fsObjectNameValidator.validate(new FSFile("")));
    Assertions.assertFalse(fsObjectNameValidator.validate(new FSFile("_*+")));
    Assertions.assertFalse(fsObjectNameValidator.validate(new FSFile("*|<>:?")));
    Assertions.assertFalse(fsObjectNameValidator.validate(new FSFile("\\")));
    Assertions.assertFalse(fsObjectNameValidator.validate(new FSFile("\\.file")));
    Assertions.assertFalse(fsObjectNameValidator.validate(new FSFile("//")));
    Assertions.assertFalse(fsObjectNameValidator.validate(new FSFile("//.file")));
    Assertions.assertFalse(fsObjectNameValidator.validate(new FSFile("\\//")));
    Assertions.assertFalse(fsObjectNameValidator.validate(new FSFile("\\//.file")));
  }

  @Test
  public void correctFSDirectoryValidation() {
    Assertions.assertTrue(fsObjectNameValidator.validate(new FSDirectory("correct")));
    Assertions.assertTrue(fsObjectNameValidator.validate(new FSDirectory("correct.file")));
    Assertions.assertTrue(fsObjectNameValidator.validate(new FSDirectory("correct.file.file")));
    Assertions.assertTrue(fsObjectNameValidator.validate(new FSDirectory("correct with spaces.file.file")));
    Assertions.assertTrue(fsObjectNameValidator.validate(new FSDirectory("correct_with_underscores.file.file")));
    Assertions.assertTrue(fsObjectNameValidator.validate(new FSDirectory(".correct")));
  }

  @Test
  public void incorrectFSDirectoryValidation() {
    Assertions.assertFalse(fsObjectNameValidator.validate(new FSDirectory("")));
    Assertions.assertFalse(fsObjectNameValidator.validate(new FSDirectory("_*+")));
    Assertions.assertFalse(fsObjectNameValidator.validate(new FSDirectory("*|<>:?")));
    Assertions.assertFalse(fsObjectNameValidator.validate(new FSDirectory("\\")));
    Assertions.assertFalse(fsObjectNameValidator.validate(new FSDirectory("\\.file")));
    Assertions.assertFalse(fsObjectNameValidator.validate(new FSDirectory("//")));
    Assertions.assertFalse(fsObjectNameValidator.validate(new FSDirectory("//.file")));
    Assertions.assertFalse(fsObjectNameValidator.validate(new FSDirectory("\\//")));
    Assertions.assertFalse(fsObjectNameValidator.validate(new FSDirectory("\\//.file")));
  }
}
