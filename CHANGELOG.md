# Changelog — Hello Poland Backend

Każda zmiana numeru `<version>` w `pom.xml` wymaga dodania odpowiadającej jej sekcji
w tym pliku. Zmiany przygotowywane do następnego wydania zapisujemy w sekcji
`Unreleased`, a podczas podnoszenia wersji przenosimy je pod numer zgodny z POM-em.

## Unreleased

## 2.0.20.15 — 2026-08-30

### Dodano

- Synchronizację nazwy i adresu e-mail partnera z Hello Poland do Hello Ticket.
- Synchronizację loginu głównego konta partnera w HP i HT podczas edycji danych partnera.
- Obsługę daty ważności biletów wydawanych w kampanii promocyjnej (`ticketValidTo`).
- Rozpoznawanie zamówień pochodzących z widgetu i właściwy adres powrotu z płatności TPay.

### Zmieniono

- Walidację kodów promocyjnych z uwzględnieniem dat biletów w koszyku oraz czytelniejszymi
  komunikatami o niespełnionych warunkach promocji.
- Walidację dat i konfiguracji cyklicznych pul biletowych oraz komunikaty zwracane do HelpDesku.
- Komunikaty błędów rezerwacji biletów zwracane użytkownikowi.

### Naprawiono

- Rozjazd adresu powiadomień partnera pomiędzy bazami HP i HT po zmianie loginu partnera.
- Wybór głównego konta partnera, gdy partner posiada więcej niż jednego użytkownika z rolą `PARTNER`.
- Jednoczesne filtrowanie wydarzeń po kategoriach i tagach dzięki rozdzieleniu nazw parametrów zapytania.
