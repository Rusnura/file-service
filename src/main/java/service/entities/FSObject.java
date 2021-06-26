package service.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import service.validators.FSObjectNameValidator;
import service.validators.interfaces.IValidator;

public abstract class FSObject {
  private static final IValidator[] NAME_VALIDATORS = new IValidator[] { new FSObjectNameValidator() };

  private String name;
  private FSDirectory parent;

  public FSObject(String name) {
    this.setName(name);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    for (IValidator validator : NAME_VALIDATORS) {
      if (!validator.validate(name))
        throw new IllegalArgumentException("Name '" + name + "' isn't allowed!");
    }
    this.name = name;
  }

  @JsonIgnore
  public FSDirectory getParent() {
    return parent;
  }

  public void setParent(FSDirectory parent) {
    this.parent = parent;
  }
}
