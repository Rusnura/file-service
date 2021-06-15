package service.entities;

import java.io.File;

public class FSFile extends FSObject {
  private File file = null;

  public FSFile(String name) {
    super(name);
  }

  public File getFile() {
    return file;
  }

  public void setFile(File file) {
    this.file = file;
  }
}
