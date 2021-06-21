package service.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.*;

public class FSDirectory extends FSObject {
  private final Set<FSObject> children = new LinkedHashSet<>();

  public FSDirectory(String name) {
    super(name);
  }

  @JsonIgnore
  public Set<FSObject> getChildren() {
    return children;
  }
}
