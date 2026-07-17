package pl.hellopoland.rest.dto;

import java.util.Date;

public class PromotionTicketPoolSetupIRO {

  public String poolName;
  public String ticketName;
  public Integer ticketPrice;
  public Integer availableTicketsNumber;
  public Date startDate;
  public Date endDate;
  public Date entryStartDate;
  public Date entryEndDate;
  public Boolean wholeDay;
  public Boolean isCyclic;
}
