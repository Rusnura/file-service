package service.entities;

public abstract class FSObject {
  private String name;

  public FSObject(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
