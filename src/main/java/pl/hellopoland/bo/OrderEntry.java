package pl.hellopoland.bo;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class OrderEntry extends ModelSuperclass {

  private static final long serialVersionUID = -1590949806641422316L;

  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  private OrderDateEntry dateEntry;
  @NotNull
  private Integer quantity;
  @NotNull
  private Integer unitPrice;
  @NotNull
  private String name;
  private Long externalId;
  private Long externalDefinitionId;
  @ElementCollection
  private List<String> numbers;


  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public Integer getUnitPrice() {
    return unitPrice;
  }

  public void setUnitPrice(Integer unitPrice) {
    this.unitPrice = unitPrice;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<String> getNumbers() {
    return numbers;
  }

  public void setNumbers(List<String> numbers) {
    this.numbers = numbers;
  }

  public Long getExternalId() {
    return externalId;
  }

  public void setExternalId(Long externalId) {
    this.externalId = externalId;
  }

  public OrderDateEntry getDateEntry() {
    return dateEntry;
  }

  public void setDateEntry(OrderDateEntry dateEntry) {
    this.dateEntry = dateEntry;
  }

  public Long getExternalDefinitionId() {
    return externalDefinitionId;
  }

  public void setExternalDefinitionId(Long externalDefinitionId) {
    this.externalDefinitionId = externalDefinitionId;
  }

  public Integer getSum() {
    return unitPrice * quantity;
  }

  public void addNumber(String number) {
    if (this.numbers == null) {
      this.numbers = new ArrayList<>();
    }
    this.numbers.add(number);
  }

}
