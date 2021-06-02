package service.entities;

import java.util.ArrayList;
import java.util.List;

public class FSDirectory extends FSObject {
  private final List<FSObject> children = new ArrayList<>();

  public FSDirectory(String name, FSDirectory parent) {
    super(name, parent);
  }

  public List<FSObject> getChildren() {
    return children;
  }
}
