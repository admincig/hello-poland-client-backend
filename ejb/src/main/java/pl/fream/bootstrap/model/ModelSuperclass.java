package pl.fream.bootstrap.model;

import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class ModelSuperclass implements Serializable {
  private static final long serialVersionUID = 5743489415102329432L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected Long id;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return new StringBuilder().append(getClass().getSimpleName()).append("[id=").append(id)
        .append("]").toString();
  }

}
