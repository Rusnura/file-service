package service.entities;

import java.util.ArrayList;
import java.util.List;

public class FSDirectory extends FSObject {
  private final List<FSDirectory> children = new ArrayList<>();

  public List<FSDirectory> getChildren() {
    return children;
  }
}
