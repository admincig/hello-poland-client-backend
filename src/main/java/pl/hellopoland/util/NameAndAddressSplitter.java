package pl.hellopoland.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import pl.hellopoland.exception.conflict.ConflictingException;

public class NameAndAddressSplitter {

  public static String getFirstName(String firstAndLastName) {

    if (isNullOrEmpty(firstAndLastName)) {
      return null;
    }
    if (hasForbiddenCharacter(firstAndLastName)) {
      throw new ConflictingException("You are using forbidden character");
    }
    String correctLastName = deleteSpacesNearDashes(firstAndLastName);
    return correctLastName.replaceAll(" " + getLastName(firstAndLastName), "");
  }

  public static String getLastName(String firstAndLastName) {

    if (isNullOrEmpty(firstAndLastName)) {
      return null;
    }
    if (hasForbiddenCharacter(firstAndLastName)) {
      throw new ConflictingException("You are using forbidden character");
    }
    String correctLastName = deleteSpacesNearDashes(firstAndLastName);
    String[] split = correctLastName.split(" ");

    if (split.length == 1) {
      return null;
    }
    return split[split.length - 1];
  }

  public static String getCity(String cityAndCountry) {
    if (isNullOrEmpty(cityAndCountry)) {
      return null;
    }
    return cityAndCountry.replaceAll(", " + getCountry(cityAndCountry), "");
  }

  public static String getCountry(String cityAndCountry) {
    if (isNullOrEmpty(cityAndCountry)) {
      return null;
    }
    String find = ", ";
    String[] split = cityAndCountry.split(find);
    return split[split.length - 1];
  }

  private static String deleteSpacesNearDashes(String stringWithSpaces) {
    String firstTry = stringWithSpaces.replace(" -", "-");
    String secondTry = firstTry.replace("- ", "-");
    return secondTry;
  }

  private static boolean isNullOrEmpty(String testString) {
    if (testString == null || testString.isEmpty()) {
      return true;
    }
    return false;
  }

  private static boolean hasForbiddenCharacter(String testString) {
    Pattern special = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~]");
    Matcher hasSpecial = special.matcher(testString);
    return hasSpecial.find();
  }

}
