package service.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class FSObject {
  private String name;
  private FSDirectory parent;

  public FSObject(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

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
