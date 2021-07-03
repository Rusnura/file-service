package service.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import service.components.FileSystem;

public abstract class FSObject {
  private String name;
  private String path;
  private FSDirectory parent;

  public FSObject(String name) {
    this.setName(name);
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
    setPath(parent.getPath() + FileSystem.SEPARATOR + name);
  }

  private void setPath(String path) {
    this.path = path;
  }
}
