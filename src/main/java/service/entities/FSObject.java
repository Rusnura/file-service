package service.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import service.components.FileSystem;

public abstract class FSObject {
  private String name;
  private final String path;
  private FSDirectory parent;

  public FSObject(String name) {
    this.setName(name);
    if ("".equals(parent.getName())) {
      this.path = FileSystem.SEPARATOR + name;
    } else {
      this.path = parent.getPath() + FileSystem.SEPARATOR + name;
    }
  }

  public String getName() {
    return name;
  }

  public String getPath() { return path; }

  public void setName(String name) {
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
