package pl.hellopoland.bo;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "orders")
public class Order extends ModelSuperclass {

  public Order() {
    this.generateHash();
  }

  public enum Status {
    NEW, CONFIRMED, PROBLEM, CANCELLED;
  }


  private static final long serialVersionUID = 3824722747352862154L;

  @OneToMany(mappedBy = "order", cascade = CascadeType.REFRESH)
  private Collection<OrderSightEntry> entries;
  @NotNull
  @Column(unique = true)
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
  private String p24OrderId;
  private String p24Currency;
  private String p24Statement;

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

  public String getP24OrderId() {
    return p24OrderId;
  }

  public void setP24OrderId(String p24OrderId) {
    this.p24OrderId = p24OrderId;
  }

  public String getP24Currency() {
    return p24Currency;
  }

  public void setP24Currency(String p24Currency) {
    this.p24Currency = p24Currency;
  }

  public String getP24Statement() {
    return p24Statement;
  }

  public void setP24Statement(String p24Statement) {
    this.p24Statement = p24Statement;
  }

  public void generateHash() {
    this.hash = UUID.randomUUID().toString();
  }

}
