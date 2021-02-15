package pl.hellopoland.service.vo;

import pl.hellopoland.bo.HptSubject;

import java.util.List;

public class HptTpdsDownloadConfigurator {

  public List<Long> sightEventIds;
  public HptSubject subject;
  public boolean showDeletedAndOverdued;
  public boolean replaceTdIdsWithAtnaIds;

}
