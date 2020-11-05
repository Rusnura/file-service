package service.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;

import java.io.File;
import java.io.IOException;

public class FileEntity extends File {
  private String password = null;
  private boolean canUpload = true;
  private boolean canDelete = true;
  private boolean canDownload = true;

//  public FileEntity(String pathname) {
//    super(pathname);
//  }

  public FileEntity(String pathname) {
    super(pathname);
  }

  public String getExtension() {
    return FilenameUtils.getExtension(this.getAbsolutePath());
  }

  public long getFileSize() {
    return isFile() ? FileUtils.sizeOf(this) : 0;
  }

  @JsonIgnore
  public String getMimeType() throws IOException {
    return new Tika().detect(this);
  }

  @JsonIgnore
  public void setPassword(String password) {
    this.password = password;
  }

  @JsonIgnore
  public String getPassword() {
    return password;
  }

  public boolean isCanUpload() {
    return canUpload;
  }

  @JsonIgnore
  public void setCanUpload(boolean canUpload) {
    this.canUpload = canUpload;
  }

  public boolean isCanDelete() {
    return canDelete;
  }

  @JsonIgnore
  public void setCanDelete(boolean canDelete) {
    this.canDelete = canDelete;
  }

  public boolean isCanDownload() {
    return canDownload;
  }

  @JsonIgnore
  public void setCanDownload(boolean canDownload) {
    this.canDownload = canDownload;
  }
}
