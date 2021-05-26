package service.entities;

public class FSFile extends FSObject {
  private long size = 0;

  public FSFile(String name) {
    super(name);
  }

  public long getSize() {
    return size;
  }

  public void setSize(long size) {
    this.size = size;
  }
}
