package pl.hellopoland.bo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

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
  private String tPayPaymentId;
  private String tPayPaymentUrl;
  private String tPayPaymentStatement;

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

  public String getTPayPaymentId() {
    return tPayPaymentId;
  }

  public void setTPayPaymentId(String tPayPaymentId) {
    this.tPayPaymentId = tPayPaymentId;
  }

  public String getTPayPaymentUrl() {
    return tPayPaymentUrl;
  }

  public void setTPayPaymentUrl(String tPayPaymentUrl) {
    this.tPayPaymentUrl = tPayPaymentUrl;
  }

  public String getTPayPaymentStatement() {
    return tPayPaymentStatement;
  }

  public void setTPayPaymentStatement(String tPayPaymentStatement) {
    this.tPayPaymentStatement = tPayPaymentStatement;
  }

  public void generateHash() {
    this.hash = UUID.randomUUID().toString();
  }

  public boolean sumIsZero() {
    return this.entries.stream()
        .mapToInt(OrderSightEntry::getSum)
        .noneMatch(sum -> sum > 0);
  }

}
