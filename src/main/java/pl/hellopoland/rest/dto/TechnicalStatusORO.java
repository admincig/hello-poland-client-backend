package pl.hellopoland.rest.dto;

import java.util.ArrayList;
import java.util.List;

public class TechnicalStatusORO {

  public String checkedAt;
  public boolean agentConfigured;
  public String agentMessage;
  public TechnicalCheckORO hpDatabase;
  public TechnicalCheckORO wordpressLocal;
  public TechnicalCheckORO wordpressPublic;
  public TechnicalCheckORO wordpressDatabase;
  public List<TechnicalContainerORO> containers = new ArrayList<>();
}
