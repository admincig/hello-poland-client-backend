package pl.hellopoland.sight;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.ServiceSuperclass;
import pl.hellopoland.image.ImageService;

@LocalBean
@Stateless
public class SightService extends ServiceSuperclass {

  @Inject
  private ImageService imageService;

  public pl.hellopoland.dto.Sight addSight(pl.hellopoland.dto.Sight sightDTO) {
    SightLocation sightLocation = null;
    Sight sight = new Sight();

    sight.setName(sightDTO.name);
    sight.setLead(sightDTO.lead);
    sight.setDescription(sightDTO.description);
    sight.setMainImage(imageService
        .downloadImage(sightDTO.mainImage == null ? null : sightDTO.mainImage.ImageURL));
    sight.setEmail(sightDTO.email);
    sight.setPhone(sightDTO.phone);

    if (sightDTO.sightLocation != null) {
      sightLocation = new SightLocation();

      sightLocation.setLatitude(sightDTO.sightLocation.latitude);
      sightLocation.setLongitude(sightDTO.sightLocation.longitude);
      sightLocation.setStreet(sightDTO.sightLocation.street);
      sightLocation.setZipCode(sightDTO.sightLocation.zipCode);
      sightLocation.setCity(sightDTO.sightLocation.city);
      sightLocation.setCountry(sightDTO.sightLocation.country);
    }

    sight.setSightLocation(sightLocation);

    em.persist(sight);

    sightDTO.id = sight.getId();

    if (sight.getMainImage() != null) {
      sightDTO.mainImage.path = sight.getMainImage().getPath();
      sightDTO.mainImage.hash = sight.getMainImage().getHash();
      sightDTO.mainImage.extension = sight.getMainImage().getExtension();
      sightDTO.mainImage.ImageURL = sight.getMainImage().getImageURL();
    }

    return sightDTO;
  }

}
