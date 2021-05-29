package service.entities;

public abstract class FSObject {
  private String name;
  private FSDirectory parent;

  public FSObject(String name, FSDirectory parent) {
    this.name = name;
    this.parent = parent;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public FSDirectory getParent() {
    return parent;
  }

  public void setParent(FSDirectory parent) {
    this.parent = parent;
  }
}
