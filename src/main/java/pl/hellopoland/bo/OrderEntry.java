package pl.hellopoland.bo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import pl.hellopoland.dto.booking.TicketDTO;

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
  private Long poolId;
  @ElementCollection
  private List<String> numbers;
  private String partnerAffiliateCode;

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

  public Long getPoolId() {
    return poolId;
  }

  public void setPoolId(Long poolId) {
    this.poolId = poolId;
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

  public String getPartnerAffiliateCode() {
    return partnerAffiliateCode;
  }

  public void setPartnerAffiliateCode(String partnerAffiliateCode) {
    this.partnerAffiliateCode = partnerAffiliateCode;
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

  public boolean matches(TicketDTO dto) {
    if (!this.externalDefinitionId.equals(dto.ticketDefinitionId)) {
      // System.out.println("this.externalDefinitionId != dto.ticketDefinitionId");
      // System.out.println("this.externalDefinitionId= " + this.externalDefinitionId);
      // System.out.println("dto.ticketDefinitionId= " + dto.ticketDefinitionId);
      return false;
    }
    if (!this.poolId.equals(dto.tickerPoolDefinitionId)) {
      // System.out.println("this.poolId != dto.tickerPoolDefinitionId");
      // System.out.println("this.poolId= " + this.poolId);
      // System.out.println("dto.tickerPoolDefinitionId= " + dto.tickerPoolDefinitionId);
      return false;
    }
    if (this.dateEntry.getDate().compareTo(dto.date) != 0) {
      Date dayOnly = this.dateEntry.getDate();
      dayOnly.setHours(0);
      dayOnly.setMinutes(0);
      dayOnly.setSeconds(0);

      if (!dto.wholeDay || dayOnly.compareTo(dto.date) != 0) {
        // System.out.println("this.getDateEntry.getDate() != dto.date");
        // System.out.println("this.getDateEntry.getDate= " + this.dateEntry.getDate());
        // System.out.println("dto.date= " + dto.date);
        return false;
      }
    }
    if (!this.unitPrice.equals(dto.price)) {
      // System.out.println("this.unitPrice != dto.price");
      // System.out.println("this.unitPrice= " + this.unitPrice);
      // System.out.println("dto.price= " + dto.price);
      return false;
    }
    if (!this.name.equals(dto.name)) {
      // System.out.println("this.name != dto.name");
      // System.out.println("this.name= " + this.name);
      // System.out.println("dto.name= " + dto.name);
      return false;
    }
    return true;
  }
}
