package pl.hellopoland.bo;

import java.time.LocalTime;
import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class OpeningHours extends ModelSuperclass {

  private static final long serialVersionUID = -9192565384148638549L;

  @ManyToOne(optional = true)
  private SightEvent sightEvent;
  @ManyToOne(optional = true)
  private Sight sight;
  @NotNull
  private Integer day;
  @NotNull
  @JsonbDateFormat("HH:mm")
  private LocalTime openTime;
  @NotNull
  @JsonbDateFormat("HH:mm")
  private LocalTime closeTime;


  public SightEvent getSightEvent() {
    return sightEvent;
  }

  public void setSightEvent(SightEvent sightEvent) {
    this.sightEvent = sightEvent;
  }

  public Sight getSight() {
    return sight;
  }

  public void setSight(Sight sight) {
    this.sight = sight;
  }

  public Integer getDay() {
    return day;
  }

  public void setDay(Integer day) {
    this.day = day;
  }

  public LocalTime getOpenTime() {
    return openTime;
  }

  public void setOpenTime(LocalTime openTime) {
    this.openTime = openTime;
  }

  public LocalTime getCloseTime() {
    return closeTime;
  }

  public void setCloseTime(LocalTime closeTime) {
    this.closeTime = closeTime;
  }


}
