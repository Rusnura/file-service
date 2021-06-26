package service.validators;

import org.springframework.util.ObjectUtils;
import service.entities.FSObject;
import service.validators.interfaces.IValidator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FSObjectNameValidator implements IValidator {
  private static final Pattern incorrectNamePattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);

  @Override
  public <T> boolean validate(T object) {
    if (object instanceof FSObject) {
      FSObject fsObject = (FSObject) object;
      if (ObjectUtils.isEmpty(fsObject.getName()))
        return false;

      Matcher matcher = incorrectNamePattern.matcher(fsObject.getName());
      return matcher.find();
    }
    return false;
  }
}
