package pl.hellopoland.bo;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.io.Serializable;

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
