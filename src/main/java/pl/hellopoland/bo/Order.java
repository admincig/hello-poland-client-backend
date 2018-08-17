package pl.hellopoland.bo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "orders")
public class Order extends ModelSuperclass {

  public enum Status {
    NEW, CONFIRMED, PROBLEM, CANCELLED;
  }

  private static final long serialVersionUID = 3824722747352862154L;

  @OneToMany(mappedBy = "order", cascade = CascadeType.REFRESH)
  private Collection<OrderSightEntry> entries;
  @NotNull
  private String hash;
  @Embedded
  private OrderDetails details;
  @ManyToOne
  private User user;
  @NotNull
  @Enumerated(EnumType.STRING)
  private Status status = Status.NEW;
  @NotNull
  private Date date = new Date();
  @Transient
  private List<P24PassageCartEntry> p24PassageCartEntries;

  public List<P24PassageCartEntry> getP24PassageCartEntries() {
    return p24PassageCartEntries;
  }

  public void setP24PassageCartEntries(List<P24PassageCartEntry> p24PassageCartEntries) {
    this.p24PassageCartEntries = p24PassageCartEntries;
  }

  public void addP24PassageCartEntry(P24PassageCartEntry entry) {
    if (this.getP24PassageCartEntries() == null) {
      this.setP24PassageCartEntries(new ArrayList<>());
    }
    this.getP24PassageCartEntries().add(entry);
  }

  public Collection<OrderSightEntry> getEntries() {
    return entries;
  }

  public void setEntries(Collection<OrderSightEntry> entries) {
    this.entries = entries;
  }

  public String getHash() {
    return hash;
  }

  public void setHash(String hash) {
    this.hash = hash;
  }

  public OrderDetails getDetails() {
    return details;
  }

  public void setDetails(OrderDetails details) {
    this.details = details;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Integer getSum() {
    if (entries == null) {
      return 0;
    }
    return entries.stream().collect(Collectors.summingInt(OrderSightEntry::getSum));
  }

  public void generateHash() {
    this.hash = UUID.randomUUID().toString();
  }

  public void addEntry(OrderSightEntry entry) {
    if (this.getEntries() == null) {
      this.setEntries(new ArrayList<>());
    }
    this.getEntries().add(entry);
  }
}
