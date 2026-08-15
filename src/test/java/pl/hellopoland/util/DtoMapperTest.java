package pl.hellopoland.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.bo.Sight;
import pl.hellopoland.dto.SightDTO;
import pl.hellopoland.bo.Tag;
import pl.hellopoland.dto.TagDTO;
import pl.hellopoland.enums.LanguageVersion;

public class DtoMapperTest {

  @Test
  public void shouldMapGooglePlaceIdBothWays() {
    SightDTO input = new SightDTO();
    input.googlePlaceId = "ChIJN1t_tDeuEmsRUsoyG83frY4";

    Sight sight = new Sight();
    sight.setDefaultLanguage(LanguageVersion.PL_PL);
    Partner partner = new Partner();
    partner.setId(1L);
    partner.setName("Partner");
    sight.setPartner(partner);

    DtoMapper.copy(input, sight);
    SightDTO output = DtoMapper.getDTO(sight);

    assertEquals(input.googlePlaceId, sight.getGooglePlaceId());
    assertEquals(input.googlePlaceId, output.googlePlaceId);
  }

  @Test
  public void shouldExposePromotionalTagMarker() {
    Tag tag = new Tag();
    tag.setId(64L);
    tag.setDefaultLanguage(LanguageVersion.PL_PL);
    tag.setLabel("VISA Benefit");
    tag.setPromotional(true);

    TagDTO output = DtoMapper.getDTO(tag);

    assertEquals(Boolean.TRUE, output.promotional);
    assertEquals("VISA Benefit", output.label);
  }
}
