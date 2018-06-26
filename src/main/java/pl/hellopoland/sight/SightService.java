package pl.hellopoland.sight;

import static java.util.stream.Collectors.toList;

import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.ServiceSuperclass;
import pl.hellopoland.image.Image;
import pl.hellopoland.image.ImageService;
import pl.hellopoland.partner.Partner;
import pl.hellopoland.partner.PartnerService;
import pl.hellopoland.security.dto.CurrentUser;

@LocalBean
@Stateless
public class SightService extends ServiceSuperclass {

  @Inject
  private ImageService imageService;

  @Inject
  private PartnerService partnerService;

  public pl.hellopoland.dto.Sight add(pl.hellopoland.dto.Sight sightDTO, CurrentUser currentUser) {
    fillInSightWithDTOData(new Sight(), sightDTO, currentUser);

    return sightDTO;
  }

  public pl.hellopoland.dto.Sight get(Long sightId) {
    Sight sight = em.createQuery("from Sight sight where sight.id=:sightId", Sight.class)
        .setParameter("sightId", sightId)
        .getSingleResult();

    return createSightDTO(sight);
  }

  public List<pl.hellopoland.dto.Sight> get() {
    return em.createQuery("from Sight sight where sight.active=True", Sight.class)
        .getResultStream()
        .map(this::createSightDTO)
        .collect(toList());
  }

  public List<pl.hellopoland.dto.Sight> getAllForPartner(CurrentUser currentUser) {
    Partner partner = partnerService.findByUserEmail(currentUser.getEmail());

    return em
        .createQuery("from Sight sight where sight.active=True and sight.partner.id=:partnerId",
            Sight.class)
        .setParameter("partnerId", partner.getId())
        .getResultStream()
        .map(this::createSightDTO)
        .collect(toList());
  }

  public pl.hellopoland.dto.Sight update(Long sightId, pl.hellopoland.dto.Sight sightDTO,
      CurrentUser currentUser) {
    Sight sight = em.createQuery("from Sight sight where sight.id=:sightId", Sight.class)
        .setParameter("sightId", sightId)
        .getSingleResult();

    fillInSightWithDTOData(sight, sightDTO, currentUser);

    return sightDTO;
  }

  public void delete(Long sightId) {
    Sight sight = em.createQuery("from Sight sight where sight.id=:sightId", Sight.class)
        .setParameter("sightId", sightId)
        .getSingleResult();

    sight.setActive(false);
  }

  private void fillInSightWithDTOData(Sight sight, pl.hellopoland.dto.Sight sightDTO,
      CurrentUser currentUser) {
    Partner partner = partnerService.findByUserEmail(currentUser.getEmail());

    sight.setName(sightDTO.name);
    sight.setLead(sightDTO.lead);
    sight.setDescription(sightDTO.description);
    sight.setMainImage(imageService
        .downloadImage(sightDTO.mainImage == null ? null : sightDTO.mainImage.ImageURL));
    sight.setEmail(sightDTO.email);
    sight.setPhone(sightDTO.phone);
    sight.setPartner(partner);

    fillInSightLocationWithDTOData(sight, sightDTO);

    em.persist(sight);

    sightDTO.id = sight.getId();

    if (sight.getMainImage() != null) {
      sightDTO.mainImage.path = sight.getMainImage().getPath();
      sightDTO.mainImage.hash = sight.getMainImage().getHash();
      sightDTO.mainImage.extension = sight.getMainImage().getExtension();
      sightDTO.mainImage.ImageURL = sight.getMainImage().getImageURL();
    }
  }

  private void fillInSightLocationWithDTOData(Sight sight,
      pl.hellopoland.dto.Sight sightDTO) {
    SightLocation sightLocation = sight.getSightLocation();

    if (sightDTO.sightLocation != null) {
      if (sightLocation == null) {
        sightLocation = new SightLocation();
      }

      sightLocation.setLatitude(sightDTO.sightLocation.latitude);
      sightLocation.setLongitude(sightDTO.sightLocation.longitude);
      sightLocation.setStreet(sightDTO.sightLocation.street);
      sightLocation.setZipCode(sightDTO.sightLocation.zipCode);
      sightLocation.setCity(sightDTO.sightLocation.city);
      sightLocation.setCountry(sightDTO.sightLocation.country);
    } else {
      sight.setSightLocation(null);
    }

    sight.setSightLocation(sightLocation);
  }

  private pl.hellopoland.dto.Sight createSightDTO(Sight sight) {
    pl.hellopoland.dto.Sight sightDTO = new pl.hellopoland.dto.Sight();

    sightDTO.id = sight.getId();
    sightDTO.name = sight.getName();
    sightDTO.lead = sight.getLead();
    sightDTO.description = sight.getDescription();
    sightDTO.mainImage = sight.getMainImage() != null ? createImageDTO(sight.getMainImage()) : null;

    return sightDTO;
  }

  private pl.hellopoland.dto.Image createImageDTO(Image image) {
    pl.hellopoland.dto.Image imageDTO = new pl.hellopoland.dto.Image();

    imageDTO.path = image.getPath();
    imageDTO.hash = image.getHash();
    imageDTO.extension = image.getExtension();
    imageDTO.ImageURL = image.getImageURL();

    return imageDTO;
  }
}
