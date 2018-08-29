package pl.hellopoland.rest;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.bind.adapter.JsonbAdapter;
import pl.hellopoland.bo.OpeningHours;

public class OpeningHoursAdapter implements JsonbAdapter<OpeningHours, JsonObject> {
  @Override
  public JsonObject adaptToJson(OpeningHours oHours) throws Exception {
    JsonObjectBuilder job =
        Json.createObjectBuilder().add("closeTime", oHours.getCloseTime().toString())
            .add("day", oHours.getDay()).add("openTime", oHours.getOpenTime().toString());
    if (oHours.getId() != null) {
      job.add("id", oHours.getId());
    }

    // TODO:!!!!!!!!!!!
    if (oHours.getSight() != null) {
      job.add("sight", JsonbConfig.getInstance().toJson(oHours.getSight()));
    }
    if (oHours.getSightEvent() != null) {
      job.add("sight", JsonbConfig.getInstance().toJson(oHours.getSightEvent()));
    }
    // !!!!!!!


    return job.build();
  }

  @Override
  public OpeningHours adaptFromJson(JsonObject adapted) throws Exception {
    OpeningHours oHours = new OpeningHours();
    if (adapted.containsKey("id")) {
      oHours.setId(Long.valueOf(adapted.getInt("id")));
    }
    oHours.setCloseTime(
        LocalTime.parse(adapted.getString("closeTime"), DateTimeFormatter.ofPattern("HH:mm")));
    oHours.setOpenTime(
        LocalTime.parse(adapted.getString("openTime"), DateTimeFormatter.ofPattern("HH:mm")));
    oHours.setDay(adapted.getInt("day"));
    return oHours;
  }
}
