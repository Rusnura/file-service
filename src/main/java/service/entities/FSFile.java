package service.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.core.io.InputStreamResource;

public class FSFile extends FSObject {
  private InputStreamResource resource = null;

  public FSFile(String name) {
    super(name);
  }

  @JsonIgnore
  public InputStreamResource getResource() {
    return resource;
  }

  public void setResource(InputStreamResource resource) {
    this.resource = resource;
  }
}
