package pl.hellopoland.bo;

import pl.hellopoland.enums.PromotionCodeBatchSource;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "promotion_code_batch")
public class PromotionCodeBatch extends ModelSuperclass {

  private static final long serialVersionUID = -7477023526742164633L;

  @NotNull
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "promotion_campaign_id", nullable = false)
  private PromotionCampaign promotionCampaign;
  @NotNull
  @Column(nullable = false, length = 32)
  @Enumerated(EnumType.STRING)
  private PromotionCodeBatchSource source;
  @Column(name = "file_name")
  private String fileName;
  @NotNull
  @Column(name = "codes_count", nullable = false)
  private Integer codesCount = 0;
  @NotNull
  @Column(name = "created_at", nullable = false)
  private Date createdAt = new Date();
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "created_by")
  private User createdBy;

  public PromotionCampaign getPromotionCampaign() {
    return promotionCampaign;
  }

  public void setPromotionCampaign(PromotionCampaign promotionCampaign) {
    this.promotionCampaign = promotionCampaign;
  }

  public PromotionCodeBatchSource getSource() {
    return source;
  }

  public void setSource(PromotionCodeBatchSource source) {
    this.source = source;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public Integer getCodesCount() {
    return codesCount;
  }

  public void setCodesCount(Integer codesCount) {
    this.codesCount = codesCount;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public User getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(User createdBy) {
    this.createdBy = createdBy;
  }
}
