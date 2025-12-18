package pl.hellopoland.bo;

import pl.hellopoland.exception.conflict.ConflictingException;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.File;
import java.time.LocalDateTime;

@Entity

public class FileDescriptor extends ModelSuperclass {

  private static final long serialVersionUID = 1L;

  public enum MimeType {
    PDF("application/pdf");
    private String type;

    private MimeType(String type) {
      this.type = type;
    }

    public String getType() {
      return this.type;
    }
  }

  @ManyToOne
  private Partner partner;
  @NotNull
  @Enumerated(EnumType.STRING)
  private MimeType mimeType;
  @NotNull
  private String path;
  @NotNull
  private LocalDateTime created;
  @Column(name = "original_name", length = 250)
  private String originalName;

  public FileDescriptor() {}

  public FileDescriptor(File file) {
    this.mimeType = guessMimeType(file);
    this.path = file.getPath();
    this.created = LocalDateTime.now();
  }

  public FileDescriptor(File file, Partner partner) {
    this(file);
    this.partner = partner;
  }

  public MimeType getMimeType() {
    return mimeType;
  }

  public void setMimeType(MimeType mimeType) {
    this.mimeType = mimeType;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public LocalDateTime getCreated() {
    return created;
  }

  public void setCreated(LocalDateTime created) {
    this.created = created;
  }

  public String getOriginalName() {
        return originalName;
  }

  public void setOriginalName(String originalName) {
        this.originalName = originalName;
  }

  private @NotNull MimeType guessMimeType(File file) {
    switch (getFileExtension(file)) {
      case "pdf":
        return MimeType.PDF;
      default:
        throw new ConflictingException("Not recognized file Mime type.");
    }
  }

  @Transient
  private String getFileExtension(File file) {
    String extension = "";
    try {
      if (file != null && file.exists()) {
        String name = file.getName();
        extension = name.substring(name.lastIndexOf(".") + 1);
      }
    } catch (Exception e) {
      extension = "";
    }
    return extension;
  }

  public Partner getPartner() {
    return partner;
  }

  public void setPartner(Partner partner) {
    this.partner = partner;
  }

  @Transient
  public String getDownloadUrl() {
    return System.getProperty("base.url") + "/files/" + getFileName();
  }

  @Transient
  public String getFileName() {
    var startIndex = path.lastIndexOf("/");
    return path.substring(startIndex != -1 ? startIndex + 1 : path.lastIndexOf("\\") + 1,
        path.length());
  }

}
