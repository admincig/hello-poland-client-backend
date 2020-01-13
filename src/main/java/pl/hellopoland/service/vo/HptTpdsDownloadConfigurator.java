package pl.hellopoland.service.vo;

import pl.hellopoland.bo.HptSubject;

public class HptTpdsDownloadConfigurator {

  public HptTpdsDownloadConfigurator(HptSubject subject, boolean showDeletedAndOverdued,
      boolean replaceTdIdsWithAtnaIds) {
    this.subject = subject;
    this.showDeletedAndOverdued = showDeletedAndOverdued;
    this.replaceTdIdsWithAtnaIds = replaceTdIdsWithAtnaIds;
  }

  public final HptSubject subject;
  public final boolean showDeletedAndOverdued;
  public final boolean replaceTdIdsWithAtnaIds;

}
