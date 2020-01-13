package pl.hellopoland.rest.market;

import java.time.LocalDate;
import java.util.Date;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import pl.hellopoland.annotation.DateFormat;
import pl.hellopoland.config.SightEventPagedCollectionConfig;
import pl.hellopoland.dto.FiltersContainerDTO;
import pl.hellopoland.dto.SightEventDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.rest.RestService;
import pl.hellopoland.rest.dto.AvailableDatesORO;
import pl.hellopoland.rest.dto.AvailableTicketNumberAssociationORO;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.api.market.FilterMarketAPI;
import pl.hellopoland.service.api.market.SightEventServiceMarketAPI;

@Path("/market/sight-events")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MarketSightEventRestService {

  @Inject
  SightEventServiceMarketAPI service;

  @Inject
  private FilterMarketAPI filterService;

  @GET
  public PagedCollection getList(@QueryParam("city") String city,
      @QueryParam("fromDate") @DateFormat Date fromDate,
      @QueryParam("toDate") @DateFormat Date toDate,
      @HeaderParam("Accept-Language") String acceptLanguage,
      @HeaderParam("Content-Language") String contentLanguage) {
    var config = new SightEventPagedCollectionConfig();
    config.setCity(city);
    return service.getList(config, fromDate, toDate,
        contentLanguage != null ? contentLanguage : acceptLanguage);
  }

  @GET
  @Path("/promoted")
  public PagedCollection getPromotedSightEvents(
      @HeaderParam("Content-Language") String contentLanguage) {
    SightEventPagedCollectionConfig config = new SightEventPagedCollectionConfig();
    config.setPromotion(1, 2, 3);
    config.setOrderColumn("e.promotion, id");
    return service.getPromoted(config, RestService.parseLang(contentLanguage));
  }

  @POST
  @Path("/search")
  public PagedCollection search(SightEventPagedCollectionConfig config,
      @QueryParam("fromDate") @DateFormat Date fromDate,
      @QueryParam("toDate") @DateFormat Date toDate, @QueryParam("city") String city,
      @HeaderParam("Accept-Language") String acceptLanguage,
      @HeaderParam("Content-Language") String contentLanguage) {
    config.setCity(city);
    return service.getList(config, fromDate, toDate,
        contentLanguage != null ? contentLanguage : acceptLanguage);
  }

  @GET
  @Path("/{id}")
  public SightEventDTO get(@PathParam("id") Long id,
      @HeaderParam("Accept-Language") String acceptLanguage,
      @HeaderParam("Content-Language") String contentLanguage) {
    return service.get(id, contentLanguage != null ? contentLanguage : acceptLanguage);
  }

  @GET
  @Path("/filters")
  public FiltersContainerDTO getFilters() {
    return filterService.getForSightEvents();
  }

  @GET
  @Path("/{id}/available-tickets")
  public AvailableTicketNumberAssociationORO checkAvailability(@PathParam("id") Long id,
      @QueryParam("date") @DateFormat final Date date) {
    return service.checkAvailability(id, date, null);
  }

  @GET
  @Path("/{id}/available-dates")
  public AvailableDatesORO checkAvailableDates(@PathParam("id") Long id,
      @QueryParam("date") String dateString) {
    LocalDate date = null;
    if (dateString != null) {
      date = LocalDate.parse(dateString);
    }
    return service.checkAvailableDates(id, date);
  }

  @GET
  @Path("/recommended")
  public PagedCollection recommended(
      @HeaderParam("Content-Language") String contentLanguage,
      @QueryParam("count") @DefaultValue("6") Integer count) {
    LanguageVersion lang = RestService.parseLang(contentLanguage);
    return service.getRecommended(count, lang);
  }

  @POST
  @Path("/personalized")
  public PagedCollection personalized(
      SightEventPagedCollectionConfig config,
      @HeaderParam("Content-Language") String contentLanguage,
      @QueryParam("count") @DefaultValue("6") Integer count) {
    LanguageVersion lang = RestService.parseLang(contentLanguage);
    return service.getPersonalized(config, count, lang);
  }

  @PATCH
  @Path("/{id}/favourite")
  public SightEventDTO addToFavourite(@PathParam("id") Long id) {
    return service.addFavourite(id);
  }

  @DELETE
  @Path("/{id}/favourite")
  public Response removeFavourite(@PathParam("id") Long id) {
    service.removeFavourite(id);
    return Response.ok().build();
  }

  @GET
  @Path("/favourites")
  public PagedCollection favourites(@HeaderParam("Accept-Language") String acceptLanguage,
      @QueryParam("fromDate") @DateFormat Date fromDate,
      @QueryParam("toDate") @DateFormat Date toDate,
      @HeaderParam("Content-Language") String contentLanguage) {
    var config = new SightEventPagedCollectionConfig();
    config.onlyActive();
    config.onlyPublished();
    return service.favourites(config, fromDate, toDate,
        contentLanguage != null ? contentLanguage : acceptLanguage);
  }

}
