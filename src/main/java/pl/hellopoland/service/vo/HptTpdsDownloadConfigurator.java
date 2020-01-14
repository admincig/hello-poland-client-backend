package pl.hellopoland.service.vo;

import java.util.List;
import pl.hellopoland.bo.HptSubject;

public class HptTpdsDownloadConfigurator {

  public List<Long> sightEventIds;
  public HptSubject subject;
  public boolean showDeletedAndOverdued;
  public boolean replaceTdIdsWithAtnaIds;

}
