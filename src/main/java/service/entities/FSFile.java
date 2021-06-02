package service.entities;

public class FSFile extends FSObject {
  private long size = 0;

  public FSFile(String name, FSDirectory parent) {
    super(name, parent);
  }

  public long getSize() {
    return size;
  }

  public void setSize(long size) {
    this.size = size;
  }
}
