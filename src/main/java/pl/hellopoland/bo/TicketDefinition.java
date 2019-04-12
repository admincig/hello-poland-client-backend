package pl.hellopoland.bo;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import pl.hellopoland.enums.DateType;

@Entity
public class TicketDefinition extends ModelSuperclass {

  private static final long serialVersionUID = 574062027966116452L;

  @NotNull
  private String name;

  @NotNull
  private Integer price;

  @ManyToOne(optional = false)
  private SightEvent sightEvent;

  @NotNull
  private boolean predefinedDate;

  private Long poolId;

  private Date date;

  private DateType dateType;

  private Long externalId;

  private Integer availableTicketsNumber;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  public Long getPoolId() {
    return poolId;
  }

  public void setPoolId(Long poolId) {
    this.poolId = poolId;
  }

  public SightEvent getSightEvent() {
    return sightEvent;
  }

  public void setSightEvent(SightEvent sightEvent) {
    this.sightEvent = sightEvent;
  }

  public boolean isPredefinedDate() {
    return predefinedDate;
  }

  public void setPredefinedDate(boolean predefinedDate) {
    this.predefinedDate = predefinedDate;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Long getExternalId() {
    return externalId;
  }

  public void setExternalId(Long externalId) {
    this.externalId = externalId;
  }

  public DateType getDateType() {
    return dateType;
  }

  public void setDateType(DateType dateType) {
    this.dateType = dateType;
  }

  public void setAvailableTicketsNumber(Integer availableTicketsNumber) {
    this.availableTicketsNumber = availableTicketsNumber;
  }

  public Integer getAvailableTicketsNumber() {
    return availableTicketsNumber;
  }
}
