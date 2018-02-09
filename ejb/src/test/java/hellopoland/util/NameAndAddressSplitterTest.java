package hellopoland.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import pl.hellopoland.ConflictingException;
import pl.hellopoland.util.NameAndAddressSplitter;

public class NameAndAddressSplitterTest {

  @Test
  public void singleFirstAndLastName() {
    String firstAndLastName = "Jan Kowalski";
    String firstName = NameAndAddressSplitter.getFirstName(firstAndLastName);
    String lastName = NameAndAddressSplitter.getLastName(firstAndLastName);

    assertEquals("Jan", firstName);
    assertEquals("Kowalski", lastName);
  }

  @Test
  public void doubleFirstAndLastName() {
    String firstAndLastName = "Jan Maria Kowalski";
    String firstName = NameAndAddressSplitter.getFirstName(firstAndLastName);
    String lastName = NameAndAddressSplitter.getLastName(firstAndLastName);

    assertEquals("Jan Maria", firstName);
    assertEquals("Kowalski", lastName);
  }

  @Test
  public void onlyFirstName() {
    String firstAndLastName = "Jan";
    String firstName = NameAndAddressSplitter.getFirstName(firstAndLastName);
    String lastName = NameAndAddressSplitter.getLastName(firstAndLastName);

    assertEquals("Jan", firstName);
    assertNull(lastName);
  }

  @Test
  public void singleFirstNameAndDashLastname() {
    String firstAndLastName = "Jan Kowalski-Nowak";
    String firstName = NameAndAddressSplitter.getFirstName(firstAndLastName);
    String lastName = NameAndAddressSplitter.getLastName(firstAndLastName);

    assertEquals("Jan", firstName);
    assertEquals("Kowalski-Nowak", lastName);
  }

  @Test
  public void singleFirstNameAndSpaceDashLastname() {
    String firstAndLastName = "Jan Kowalski - Nowak";
    String firstName = NameAndAddressSplitter.getFirstName(firstAndLastName);
    String lastName = NameAndAddressSplitter.getLastName(firstAndLastName);

    assertEquals("Jan", firstName);
    assertEquals("Kowalski-Nowak", lastName);
  }

  @Test
  public void doubleFirstNameAndDashLastname() {
    String firstAndLastName = "Jan Maria Kowalski-Nowak";
    String firstName = NameAndAddressSplitter.getFirstName(firstAndLastName);
    String lastName = NameAndAddressSplitter.getLastName(firstAndLastName);

    assertEquals("Jan Maria", firstName);
    assertEquals("Kowalski-Nowak", lastName);
  }

  @Test
  public void doubleFirstNameAndSpaceDashLastname() {
    String firstAndLastName = "Jan Maria Kowalski - Nowak";
    String firstName = NameAndAddressSplitter.getFirstName(firstAndLastName);
    String lastName = NameAndAddressSplitter.getLastName(firstAndLastName);

    assertEquals("Jan Maria", firstName);
    assertEquals("Kowalski-Nowak", lastName);
  }

  @Test
  public void nullName() {
    String firstAndLastName = null;
    String firstName = NameAndAddressSplitter.getFirstName(firstAndLastName);
    String lastName = NameAndAddressSplitter.getLastName(firstAndLastName);

    assertNull(firstName);
    assertNull(lastName);
  }

  @Test
  public void emptyName() {
    String firstAndLastName = "";
    String firstName = NameAndAddressSplitter.getFirstName(firstAndLastName);
    String lastName = NameAndAddressSplitter.getLastName(firstAndLastName);

    assertNull(firstName);
    assertNull(lastName);
  }

  @Test(expected = ConflictingException.class)
  public void incorrectName() {
    String firstAndLastName = " )()##!@# - xx";
    NameAndAddressSplitter.getFirstName(firstAndLastName);
    NameAndAddressSplitter.getLastName(firstAndLastName);
  }

  @Test
  public void singleCityAndCountry() {
    String cityAndCountry = "Wrocław, Poland";
    String city = NameAndAddressSplitter.getCity(cityAndCountry);
    String country = NameAndAddressSplitter.getCountry(cityAndCountry);

    assertEquals("Wrocław", city);
    assertEquals("Poland", country);
  }

  @Test
  public void doubleCityAndCountry() {
    String cityAndCountry = "Uraz, Wrocław, Poland";
    String city = NameAndAddressSplitter.getCity(cityAndCountry);
    String country = NameAndAddressSplitter.getCountry(cityAndCountry);

    assertEquals("Uraz, Wrocław", city);
    assertEquals("Poland", country);
  }

  @Test
  public void nullCityAndCountry() {
    String cityAndCountry = null;
    String city = NameAndAddressSplitter.getCity(cityAndCountry);
    String country = NameAndAddressSplitter.getCountry(cityAndCountry);

    assertNull(city);
    assertNull(country);
  }

  @Test
  public void emptyCityAndCountry() {
    String cityAndCountry = "";
    String city = NameAndAddressSplitter.getCity(cityAndCountry);
    String country = NameAndAddressSplitter.getCountry(cityAndCountry);

    assertNull(city);
    assertNull(country);
  }
}
