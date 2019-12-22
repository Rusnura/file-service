package service.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;

public class File {
    private String name;
    private boolean isDirectory;
    private boolean isHidden;
    private java.io.File file;

    public File(@NotNull java.io.File file) {
        this.file = file;
        this.name = file.getName();
        this.isDirectory = file.isDirectory();
        this.isHidden = file.isHidden();
    }

    public String getName() {
        return name;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public boolean isHidden() {
        return isHidden;
    }

    @JsonIgnore
    public java.io.File getFile() {
        return file;
    }

    @Override
    public String toString() {
        return "File{" +
                "name='" + name + '\'' +
                ", isDirectory=" + isDirectory +
                ", isHidden=" + isHidden +
                ", file=" + file +
                '}';
    }
}
