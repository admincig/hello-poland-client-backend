package pl.hellopoland.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

import java.util.Optional;

@ApplicationScoped
public class PostalCodeDictionaryLookup {

    @PersistenceContext
    EntityManager em;

    public record AdminDiv(String commune, String county, String voivodeship) {}

    public Optional<AdminDiv> findByZipAndCity(String zip, String city) {
        String z = normalizeZip(zip);
        if (z == null) return Optional.empty();

        try {
            Object[] row = (Object[]) em.createNativeQuery("""
          SELECT commune, county, voivodeship
          FROM postal_code_dictionary
          WHERE zipcode = :zip
            AND (:city IS NULL OR city = :city OR city_base = :city)
          LIMIT 1
          """)
                    .setParameter("zip", z)
                    .setParameter("city", normalizeCity(city))
                    .getSingleResult();

            return Optional.of(new AdminDiv(
                    (String) row[0],
                    (String) row[1],
                    (String) row[2]
            ));
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    private static String normalizeZip(String zip) {
        if (zip == null) return null;
        String s = zip.trim();
        if (s.isEmpty()) return null;

        // akceptuj "12345" -> "12-345"
        String digits = s.replaceAll("[^0-9]", "");
        if (digits.length() == 5) {
            return digits.substring(0, 2) + "-" + digits.substring(2);
        }
        // jesli ktos podal "12-345" zostaw
        if (s.matches("\\d{2}-\\d{3}")) return s;
        return null;
    }

    private static String normalizeCity(String city) {
        if (city == null) return null;
        String s = city.trim();
        return s.isEmpty() ? null : s;
    }
}
