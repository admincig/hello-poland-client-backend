package pl.hellopoland.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;
import org.junit.Test;
import pl.hellopoland.bo.Tag;

public class TagServiceTest {

  @Test
  public void shouldMarkOnlyTagsUsedByActiveCampaignsAsPromotional() {
    Tag markerTag = tag(64L);
    Tag regularTag = tag(65L);
    regularTag.setPromotional(true);

    new TagService().applyPromotionalFlags(
        List.of(markerTag, regularTag), Set.of(markerTag.getId()));

    assertTrue(markerTag.isPromotional());
    assertFalse(regularTag.isPromotional());
  }

  private Tag tag(Long id) {
    Tag tag = new Tag();
    tag.setId(id);
    return tag;
  }
}
