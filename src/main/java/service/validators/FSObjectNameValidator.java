package service.validators;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import service.entities.FSObject;
import service.validators.interfaces.IValidator;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FSObjectNameValidator implements IValidator {
  @SuppressWarnings("ResultOfMethodCallIgnored")
  @Override
  public <T> boolean validate(T object) {
    if (object instanceof FSObject) {
      FSObject fsObject = (FSObject) object;
      if (ObjectUtils.isEmpty(fsObject.getName()))
        return false;

      try {
        new File(fsObject.getName()).getCanonicalPath();
        return true;
      } catch (IOException e) {
        // nop
      }
    }
    return false;
  }
}
