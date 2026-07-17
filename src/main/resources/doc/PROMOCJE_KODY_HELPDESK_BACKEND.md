# Promocje i kody rabatowe - dokumentacja techniczna backendu Helpdesk

Dokument opisuje backendowe usługi Helpdesku do zarządzania promocjami i kodami rabatowymi.

Zakres dotyczy przede wszystkim promocji typu `TICKET`, czyli promocji, w której poprawny kod dodaje do koszyka bilet specjalny z osobnej puli promocyjnej.

## Założenia

- Panel Helpdesku zarządza promocjami przez endpointy `/helpdesk/promotions`.
- Dostęp do endpointów Helpdesku mają role `root` i `admin`.
- Kod promocyjny jest tworzony razem z promocją albo dogrywany później.
- Formularz tworzenia promocji nie wysyła pola `mode` dla kodów.
- Backend sam rozpoznaje tryb kodów:
  - `codes` -> import listy kodów,
  - `fixedCode` -> jeden kod stały,
  - brak `codes` i `fixedCode` + `generateCount` -> generator losowych kodów.
- Konfiguracja formularza kodów nie jest zapisywana w bazie.
- W bazie zostają tylko kampania, batch kodów i konkretne kody.
- Dla promocji `TICKET` backend automatycznie tworzy w HT bilet typu `SPECJALNY` i pulę `PROMOTIONAL`.
- Helpdesk nie powinien ręcznie wpisywać `hptAtnaId`, `hptTicketDefinitionId` ani `hptTicketPoolDefinitionId`.

## Główne encje

### `promotion_campaign`

Kampania promocyjna.

Przechowuje:

- nazwę promocji,
- typ promocji: `TICKET`, `PERCENT`, `AMOUNT`,
- status: `DRAFT`, `ACTIVE`, `DISABLED`, `ARCHIVED`,
- zakres: `MANUAL`, `TAG`, `GLOBAL`,
- daty obowiązywania,
- limity użycia,
- liczniki rezerwacji i użyć,
- konfigurację rabatu dla `PERCENT` albo `AMOUNT`,
- konfigurację liczby biletów wymaganych i przyznawanych dla `TICKET`.

### `promotion_code_batch`

Paczka kodów.

Przechowuje:

- kampanię,
- źródło: `IMPORT` albo `GENERATED`,
- nazwę pliku lub opis źródła,
- liczbę kodów w batchu.

### `promotion_code`

Konkretny kod promocyjny.

Przechowuje:

- wartość kodu,
- typ kodu: `ONE_TIME` albo `FIXED`,
- status: `ACTIVE`, `RESERVED`, `USED`, `DISABLED`,
- limity użyć,
- liczniki rezerwacji i użyć.

### `promotion_campaign_sightevent`

Powiązanie kampanii z ofertą.

Dla promocji `TICKET` przechowuje też wynik automatycznego utworzenia puli i biletu w HT:

- `hptAtnaId`,
- `hptTicketDefinitionId`,
- `hptTicketPoolDefinitionId`,
- `ticketPoolStatus`: `NOT_REQUIRED`, `NOT_CREATED`, `CONFIG_REQUIRED`, `CREATED`, `ERROR`.

### `promotion_code_redemption`

Historia rezerwacji i użycia kodu.

Przechowuje:

- kod,
- kampanię,
- token rezerwacji,
- zamówienie,
- pozycję zamówienia,
- status: `RESERVED`, `USED`, `RELEASED`, `CANCELLED`, `EXPIRED`,
- snapshot danych promocji i kodu.

## Endpointy Helpdesku

Base path:

```http
/helpdesk/promotions
```

Wszystkie endpointy wymagają autoryzacji Helpdesku.

## Lista promocji

```http
GET /helpdesk/promotions
```

Zwraca listę kampanii.

Przykładowe zastosowanie:

- tabela promocji w panelu,
- podgląd statusu,
- podgląd liczników rezerwacji i użyć.

## Szczegóły promocji

```http
GET /helpdesk/promotions/{id}
```

Zwraca pełne dane kampanii, w tym:

- konfigurację kampanii,
- tagi,
- oferty w promocji,
- statusy pul promocyjnych,
- liczniki.

## Tworzenie promocji

```http
POST /helpdesk/promotions
```

Tworzy kampanię. Dla promocji `TICKET` może od razu utworzyć kody oraz pule promocyjne w HT.

### Przykład: promocja TICKET z generatorem kodów i automatycznym tworzeniem pul

```json
{
  "name": "Promocja VISA",
  "promotionType": "TICKET",
  "scopeType": "MANUAL",
  "validFrom": "2026-08-01T00:00:00",
  "validTo": "2026-08-31T23:59:59",
  "globalLimit": 2000,
  "codeLimit": 1,
  "requiredTicketQuantity": 1,
  "grantedTicketQuantity": 1,
  "codeSetup": {
    "codeType": "ONE_TIME",
    "generateCount": 2000,
    "generatedCodeLength": 12,
    "generatedCodePrefix": "VISA",
    "generatedCodeSeparator": "-"
  },
  "targetSetup": {
    "tagIds": [12],
    "partnerIds": [222],
    "sightIds": [55],
    "sightEventIds": [411]
  },
  "ticketPoolSetup": {
    "ticketPrice": 100,
    "availableTicketsNumber": 10,
    "wholeDay": true,
    "isCyclic": true
  }
}
```

Backend wykona:

1. Utworzenie `promotion_campaign`.
2. Wygenerowanie kodów.
3. Utworzenie `promotion_code_batch`.
4. Utworzenie rekordów `promotion_code`.
5. Zebranie ofert z `targetSetup`.
6. Utworzenie w HT biletu typu `SPECJALNY` dla każdej oferty.
7. Utworzenie w HT puli `PROMOTIONAL` dla każdej oferty.
8. Zapisanie w HP identyfikatorów HT w `promotion_campaign_sightevent`.

### Przykład: promocja TICKET z importem kodów

```json
{
  "name": "Promocja VISA import",
  "promotionType": "TICKET",
  "scopeType": "MANUAL",
  "validFrom": "2026-08-01T00:00:00",
  "validTo": "2026-08-31T23:59:59",
  "globalLimit": 2000,
  "codeLimit": 1,
  "requiredTicketQuantity": 1,
  "grantedTicketQuantity": 1,
  "codeSetup": {
    "codeType": "ONE_TIME",
    "fileName": "visa.csv",
    "codes": ["VISA-001", "VISA-002"]
  },
  "targetSetup": {
    "sightEventIds": [411]
  },
  "ticketPoolSetup": {
    "ticketPrice": 100,
    "availableTicketsNumber": 10,
    "wholeDay": true,
    "isCyclic": true
  }
}
```

### Przykład: promocja ze stałym kodem

```json
{
  "name": "Wakajki 2026",
  "promotionType": "PERCENT",
  "scopeType": "GLOBAL",
  "validFrom": "2026-07-01T00:00:00",
  "validTo": "2026-08-31T23:59:59",
  "discountPercent": 20,
  "codeSetup": {
    "fixedCode": "PROMO20",
    "maxRedemptions": 1000
  }
}
```

## Pola kampanii

### `promotionType`

Dozwolone wartości:

- `TICKET` - kod dodaje do koszyka bilet specjalny,
- `PERCENT` - kod nalicza rabat procentowy,
- `AMOUNT` - kod nalicza rabat kwotowy.

Na ten moment główny obsługiwany produkcyjnie wariant to `TICKET`.

### `scopeType`

Dozwolone wartości:

- `MANUAL` - promocja ma ręcznie wskazane oferty,
- `TAG` - promocja działa na oferty z tagiem,
- `GLOBAL` - promocja działa globalnie.

Promocja `TICKET` nie może być globalna, bo wymaga rzeczywistych pul i biletów dla konkretnych ofert.

### `requiredTicketQuantity`

Liczba zwykłych biletów, które muszą być w koszyku, żeby przyznać bilet promocyjny.

Domyślnie backend walidacji traktuje brak wartości jako `1`.

### `grantedTicketQuantity`

Liczba biletów promocyjnych dodawanych po spełnieniu warunku.

Domyślnie backend walidacji traktuje brak wartości jako `1`.

## Sekcja `codeSetup`

Sekcja jest używana tylko przy tworzeniu promocji. Nie jest zapisywana jako konfiguracja w bazie.

### Import kodów

Jeśli `codeSetup.codes` zawiera kody, backend tworzy batch `IMPORT`.

```json
{
  "codeType": "ONE_TIME",
  "fileName": "visa.csv",
  "codes": ["VISA-001", "VISA-002"]
}
```

Walidacje:

- lista nie może być pusta,
- kody nie mogą być puste po `trim`,
- lista nie może zawierać duplikatów,
- kod musi być unikalny globalnie w `promotion_code.code`.

### Kod stały

Jeśli `codeSetup.fixedCode` jest podany, backend tworzy jeden kod typu `FIXED`.

```json
{
  "fixedCode": "PROMO20",
  "maxRedemptions": 1000,
  "maxRedemptionsPerCustomer": 1
}
```

Kod stały nie przechodzi w status `USED` po pierwszym użyciu. Dostępność jest kontrolowana limitami i historią użyć.

### Generator losowych kodów

Jeśli nie ma `codes` ani `fixedCode`, backend oczekuje `generateCount` i generuje kody.

```json
{
  "codeType": "ONE_TIME",
  "generateCount": 2000,
  "generatedCodeLength": 12,
  "generatedCodePrefix": "VISA",
  "generatedCodeSeparator": "-"
}
```

Zasady:

- `generateCount` musi być większe od `0`,
- maksymalnie można wygenerować `100000` kodów w jednym requestcie,
- `generatedCodeLength` domyślnie wynosi `12`,
- prefix jest opcjonalny,
- separator jest używany tylko, gdy prefix nie jest pusty,
- generator używa alfabetu bez mylących znaków: bez `0`, `O`, `1`, `I`.

Przykład bez prefixu:

```text
X7K9Q2LM8P4A
MN6Q3RVDZ9TQ
```

Przykład z prefixem:

```text
VISA-X7K9Q2LM8P4A
VISA-MN6Q3RVDZ9TQ
```

### Konflikty w `codeSetup`

Backend odrzuca request, jeśli podano więcej niż jeden wariant kodów, np. jednocześnie:

- `codes` i `fixedCode`,
- `codes` i `generateCount`,
- `fixedCode` i `generateCount`.

## Sekcja `targetSetup`

Sekcja określa, dla jakich ofert backend ma przygotować promocję.

```json
{
  "tagIds": [12],
  "partnerIds": [222],
  "sightIds": [55],
  "sightEventIds": [411]
}
```

Backend zbiera oferty z każdego podanego źródła i usuwa duplikaty.

Znaczenie pól:

- `tagIds` - oferty z wybranymi tagami,
- `partnerIds` - wszystkie oferty wskazanych partnerów,
- `sightIds` - wszystkie oferty wskazanych obiektów,
- `sightEventIds` - konkretne oferty.

Jeśli kampania ma `scopeType = TAG`, backend dodatkowo uwzględnia aktywne tagi kampanii.

## Sekcja `ticketPoolSetup`

Sekcja jest wymagana, jeśli przy tworzeniu promocji `TICKET` chcemy od razu wygenerować pule promocyjne.

```json
{
  "poolName": "Promocja VISA",
  "ticketName": "Bilet promocyjny VISA",
  "ticketPrice": 100,
  "availableTicketsNumber": 10,
  "startDate": "2026-08-01T00:00:00",
  "endDate": "2026-08-31T23:59:59",
  "wholeDay": true,
  "isCyclic": true
}
```

Wymagane:

- `ticketPrice`,
- `availableTicketsNumber`.

Domyślne zachowania:

- jeśli nie podano `startDate`, backend użyje `promotion_campaign.validFrom`,
- jeśli nie podano `endDate`, backend użyje `promotion_campaign.validTo`,
- jeśli nie podano `entryStartDate`, backend użyje daty startu puli,
- jeśli nie podano `entryEndDate`, backend użyje daty końca puli,
- jeśli nie podano `wholeDay`, backend ustawi `true`,
- jeśli nie podano `isCyclic`, backend ustawi `true`.

Backend tworzy w HT:

- ticket definition typu `SPECJALNY`,
- ticket pool definition typu `PROMOTIONAL`,
- `visibleForPartner = false`,
- `visibleOnPortal = false`.

Po utworzeniu zapisuje w HP:

- `hptAtnaId`,
- `hptTicketDefinitionId`,
- `hptTicketPoolDefinitionId`,
- `ticketPoolStatus = CREATED`.

Jeśli tworzenie nie powiedzie się, relacja dostaje:

```text
ticketPoolStatus = ERROR
```

## Aktualizacja promocji

```http
PUT /helpdesk/promotions/{id}
```

Aktualizuje konfigurację kampanii.

Uwaga: `codeSetup`, `targetSetup` i `ticketPoolSetup` są wejściowe dla tworzenia/generowania i nie powinny być traktowane jako trwale edytowalna konfiguracja kampanii.

## Zmiana statusu promocji

```http
PATCH /helpdesk/promotions/{id}/status
```

Przykład:

```json
{
  "status": "ACTIVE"
}
```

Statusy:

- `DRAFT`,
- `ACTIVE`,
- `DISABLED`,
- `ARCHIVED`.

Tylko kampania `ACTIVE` może przejść pozytywnie walidację kodu po stronie Portalu.

## Dodanie oferty do promocji

```http
POST /helpdesk/promotions/{id}/sight-events
```

Dodaje ofertę do kampanii, ale nie pozwala ręcznie wpisywać HT ID.

Przykład:

```json
{
  "sightEventId": 411
}
```

Dla promocji `TICKET` taka oferta dostanie status:

```text
ticketPoolStatus = NOT_CREATED
```

Właściwe HT ID zostaną zapisane dopiero po uruchomieniu generatora pul.

## Wyłączenie oferty z promocji

```http
DELETE /helpdesk/promotions/{id}/sight-events/{relationId}
```

Nie usuwa rekordu fizycznie. Ustawia powiązanie jako nieaktywne.

## Generowanie pul promocyjnych dla istniejącej promocji

```http
POST /helpdesk/promotions/{id}/ticket-pools/generate
```

Służy do wygenerowania pul po utworzeniu promocji albo po późniejszym dodaniu ofert.

Request:

```json
{
  "targetSetup": {
    "sightEventIds": [411]
  },
  "ticketPoolSetup": {
    "ticketPrice": 100,
    "availableTicketsNumber": 10,
    "wholeDay": true,
    "isCyclic": true
  }
}
```

Backend pominie oferty, które mają już:

```text
ticketPoolStatus = CREATED
```

oraz zapisany `hptAtnaId`.

## Lista kodów kampanii

```http
GET /helpdesk/promotions/{id}/codes
```

Zwraca kody z kampanii.

Najważniejsze pola:

- `code`,
- `codeType`,
- `status`,
- `maxRedemptions`,
- `reservedRedemptionsCount`,
- `usedRedemptionsCount`,
- `reservedUntil`.

## Dogranie kodów do istniejącej kampanii

```http
POST /helpdesk/promotions/{id}/codes/import
```

Służy do późniejszego dogrania kodów.

Request:

```json
{
  "fileName": "visa-extra.csv",
  "codeType": "ONE_TIME",
  "codes": ["VISA-EXTRA-001", "VISA-EXTRA-002"]
}
```

Backend utworzy nowy batch `IMPORT` i nowe rekordy `promotion_code`.

## Aktualizacja kodu

```http
PUT /helpdesk/promotions/{id}/codes/{codeId}
```

Najczęstsze użycie: wyłączenie kodu.

```json
{
  "status": "DISABLED"
}
```

Dla kodu typu `FIXED` można aktualizować limity:

```json
{
  "maxRedemptions": 1000,
  "maxRedemptionsPerCustomer": 1,
  "maxRedemptionsPerDay": 100
}
```

## Lista batchy kodów

```http
GET /helpdesk/promotions/{id}/code-batches
```

Zwraca paczki kodów.

Najważniejsze pola:

- `source`: `IMPORT` albo `GENERATED`,
- `fileName`,
- `codesCount`,
- `createdAt`.

## Historia użyć kodów

```http
GET /helpdesk/promotions/{id}/redemptions
```

Zwraca rezerwacje i wykorzystania kodów.

Statusy:

- `RESERVED` - kod zarezerwowany po walidacji,
- `USED` - kod wykorzystany po potwierdzeniu zamówienia,
- `RELEASED` - rezerwacja zwolniona,
- `CANCELLED` - rezerwacja anulowana razem z zamówieniem,
- `EXPIRED` - rezerwacja wygasła i została posprzątana schedulerem.

## Proces użycia kodu przez Portal

Ten proces jest wykonywany poza Helpdeskiem, ale Helpdesk widzi jego efekty w licznikach i historii użyć.

1. Portal wysyła kod i koszyk:

```http
POST /market/promotions/validate-code
```

2. Backend waliduje kod i tworzy rezerwację.
3. Backend zwraca `reservationToken` i efekt, np. `ADD_TICKET`.
4. Portal dodaje bilet promocyjny do koszyka.
5. Portal składa zamówienie przez:

```http
POST /market/orders
```

6. Portal wysyła w zamówieniu:

```json
{
  "promotionReservationToken": "..."
}
```

7. Kod przechodzi na `USED` dopiero po potwierdzeniu zamówienia.

## Scheduler rezerwacji

Porzucone rezerwacje są czyszczone schedulerem co 10 minut.

Rezerwacja wygasa po czasie skonfigurowanym w backendzie promocji. Aktualnie kod używa 30 minut.

Scheduler:

- znajduje rezerwacje `RESERVED`,
- bez zamówienia,
- z przekroczonym `reservedUntil`,
- oznacza je jako `EXPIRED`,
- zwalnia licznik rezerwacji kodu i kampanii.

## Regresja zwykłego działania systemu

Zmiany nie powinny być widoczne w zwykłym zakupie, jeśli nie używamy promocji.

Oczekiwane zachowanie:

- standardowe pule nadal działają jak wcześniej,
- standardowe bilety nadal działają jak wcześniej,
- Partner nie widzi pul `PROMOTIONAL`,
- Portal nie pokazuje pul `PROMOTIONAL` jako zwykłych biletów,
- pole `promotionReservationToken` w zamówieniu jest opcjonalne,
- zakup bez kodu działa bez zmian.

## Minimalny test manualny

Scenariusz testowy znajduje się w:

```text
hp-client-backend/src/test/resources/promotion-ticket-flow.http
```

Minimalny flow:

1. Utworzyć promocję `TICKET` przez `POST /helpdesk/promotions`.
2. Sprawdzić, czy powstały kody.
3. Sprawdzić, czy dla ofert powstały pule `CREATED`.
4. Wywołać `POST /market/promotions/validate-code`.
5. Sprawdzić, czy odpowiedź zawiera `reservationToken` i efekt `ADD_TICKET`.
6. Utworzyć zamówienie z normalnym i promocyjnym biletem.
7. Potwierdzić zamówienie.
8. Sprawdzić, czy redemption ma status `USED`.
9. Spróbować użyć kodu ponownie i oczekiwać błędu.

## Sekcja techniczna

### Główne klasy backendu HP

REST Helpdesk:

```text
hp-client-backend/src/main/java/pl/hellopoland/rest/helpdesk/HelpdeskPromotionRestService.java
```

Warstwa API Helpdesk z rolami `root/admin`:

```text
hp-client-backend/src/main/java/pl/hellopoland/service/api/helpdesk/PromotionManagementServiceHelpdeskAPI.java
```

Logika zarządzania promocjami, kodami i generowaniem pul:

```text
hp-client-backend/src/main/java/pl/hellopoland/service/PromotionManagementService.java
```

Walidacja kodu po stronie Portalu, rezerwacja kodu, zwalnianie i oznaczanie jako `USED`:

```text
hp-client-backend/src/main/java/pl/hellopoland/service/PromotionCodeService.java
```

Podpięcie rezerwacji kodu pod zamówienie:

```text
hp-client-backend/src/main/java/pl/hellopoland/service/OrderService.java
```

Scheduler czyszczenia porzuconych rezerwacji:

```text
hp-client-backend/src/main/java/pl/hellopoland/service/timer/PromotionReservationReleaseScheduler.java
```

### DTO wejściowe i wyjściowe

Tworzenie/edycja kampanii:

```text
PromotionCampaignHelpdeskDTO
```

Jednorazowa konfiguracja kodów przy tworzeniu kampanii:

```text
PromotionCodeSetupIRO
```

Zakres promocji, z którego backend zbiera oferty:

```text
PromotionTargetSetupIRO
```

Parametry generowanej puli i biletu promocyjnego:

```text
PromotionTicketPoolSetupIRO
```

Ręczne uruchomienie generatora pul dla istniejącej kampanii:

```text
PromotionTicketPoolGenerationIRO
```

Odpowiedź walidacji kodu dla Portalu:

```text
PromotionCodeValidationORO
```

Request walidacji kodu z Portalu:

```text
PromotionCodeValidationIRO
```

### Główne enumy

Typ promocji:

```text
PromotionType: TICKET, PERCENT, AMOUNT
```

Status kampanii:

```text
PromotionStatus: DRAFT, ACTIVE, DISABLED, ARCHIVED
```

Zakres kampanii:

```text
PromotionScopeType: MANUAL, TAG, GLOBAL
```

Typ kodu:

```text
PromotionCodeType: ONE_TIME, FIXED
```

Status kodu:

```text
PromotionCodeStatus: ACTIVE, RESERVED, USED, DISABLED
```

Status użycia/rezerwacji:

```text
PromotionCodeRedemptionStatus: RESERVED, USED, RELEASED, CANCELLED, EXPIRED
```

Status puli promocyjnej przy ofercie:

```text
PromotionTicketPoolStatus: NOT_REQUIRED, NOT_CREATED, CONFIG_REQUIRED, CREATED, ERROR
```

### Schemat tworzenia kampanii TICKET

Metoda wejściowa:

```text
PromotionManagementService.createCampaign(...)
```

Kolejność operacji:

1. Walidacja konfiguracji kampanii.
2. Zapis `promotion_campaign`.
3. Podpięcie tagów kampanii, jeśli są podane.
4. Utworzenie kodów na podstawie `codeSetup`.
5. Jeśli `promotionType = TICKET` i podano `ticketPoolSetup`, backend uruchamia generowanie pul.
6. Backend rozwiązuje `targetSetup` do listy ofert HP.
7. Dla każdej oferty tworzy lub aktualizuje `promotion_campaign_sightevent`.
8. Dla każdej oferty tworzy w HT:
   - `TicketDefinition` typu `SPECJALNY`,
   - `TicketPoolDefinition` typu `PROMOTIONAL`.
9. Backend zapisuje zwrócone identyfikatory HT w `promotion_campaign_sightevent`.

Ważne: jeśli integracja z HT rzuci wyjątek, transakcja HP powinna się wycofać, a relacja może dostać status `ERROR` przed propagacją wyjątku.

### Integracja HP -> HT

Integracja idzie przez klienta:

```text
hp-client-backend/src/main/java/pl/hellopoland/util/HelloTicket.java
```

Używane metody:

```text
getTicketTypes(...)
addTicketDefinition(...)
addTicketPoolDefinition(...)
getTicketDefinitions(...)
```

Backend HP używa tokena HT partnera właściciela oferty:

```text
sightEvent.getPartner().getHptToken()
```

Tworzony bilet:

```text
TicketDefinitionDTO
ticketType.code = SPECJALNY
price = ticketPoolSetup.ticketPrice
```

Tworzona pula:

```text
TicketPoolDefinitionDTO
poolType = PROMOTIONAL
visibleForPartner = false
visibleOnPortal = false
ticketDefinitions = [utworzony bilet SPECJALNY]
```

Po utworzeniu puli backend odczytuje `atnaId` biletu promocyjnego i zapisuje go jako:

```text
promotion_campaign_sightevent.hpt_atna_id
```

### Flow walidacji kodu

Endpoint:

```http
POST /market/promotions/validate-code
```

REST:

```text
MarketPromotionCodeRestService.validate(...)
```

Logika:

```text
PromotionCodeService.validate(...)
```

Kroki:

1. Pobranie kodu po `code`.
2. Lock pesymistyczny na rekordzie `promotion_code`.
3. Walidacja statusu kodu.
4. Walidacja kampanii: status i daty.
5. Walidacja limitów kampanii i kodu.
6. Pobranie danych koszyka po ATNA ID z HT.
7. Mapowanie HT `sightEventId` na HP `SightEvent`.
8. Walidacja zakresu promocji.
9. Zbudowanie efektu, np. `ADD_TICKET`.
10. Utworzenie `promotion_code_redemption` ze statusem `RESERVED`.
11. Zwiększenie liczników `reservedRedemptionsCount`.
12. Dla `ONE_TIME` ustawienie kodu na `RESERVED`.
13. Zwrot `reservationToken`.

### Flow zamówienia

Portal składa zamówienie przez istniejący endpoint:

```http
POST /market/orders
```

Pole opcjonalne:

```json
{
  "promotionReservationToken": "..."
}
```

Podpięcie rezerwacji:

```text
OrderService.create(...)
PromotionCodeService.attachReservationToOrder(...)
```

Moment podpięcia:

- po utworzeniu lokalnych pozycji zamówienia,
- przed bookingiem w HT.

Backend sprawdza, czy w zamówieniu faktycznie istnieje bilet promocyjny wymagany przez rezerwację.

### Finalizacja użycia kodu

Potwierdzenie zamówienia:

```text
OrderService.confirm(...)
PromotionCodeService.markOrderPromotionsAsUsed(...)
```

Efekt:

- `promotion_code_redemption.status = USED`,
- `promotion_code.usedRedemptionsCount + 1`,
- `promotion_campaign.usedRedemptionsCount + 1`,
- `reservedRedemptionsCount - 1`,
- dla `ONE_TIME`: `promotion_code.status = USED`.

Anulowanie zamówienia:

```text
OrderService.cancel(...)
PromotionCodeService.cancelOrderPromotions(...)
```

Problem płatności:

```text
OrderService.problem(...)
PromotionCodeService.releaseOrderPromotions(...)
```

Efekt:

- rezerwacja zostaje zwolniona,
- liczniki rezerwacji są zmniejszane,
- dla `ONE_TIME` kod wraca do `ACTIVE`, jeśli był tylko zarezerwowany.

### Scheduler rezerwacji

Klasa:

```text
PromotionReservationReleaseScheduler
```

Harmonogram:

```text
co 10 minut
```

Wywoływana metoda:

```text
PromotionCodeService.releaseExpiredReservations()
```

Scheduler zwalnia tylko rezerwacje:

- `status = RESERVED`,
- bez zamówienia,
- z `reservedUntil < now`.

### Statusy techniczne kodu

Kod jednorazowy:

```text
ACTIVE -> RESERVED -> USED
ACTIVE -> RESERVED -> ACTIVE      // zwolnienie/anulowanie/problem
ACTIVE -> RESERVED -> EXPIRED     // wpis redemption, kod wraca do ACTIVE
```

Kod stały:

```text
ACTIVE
DISABLED
```

Kod stały nie przechodzi na `USED`; zwiększa się tylko licznik użyć.

### Ważne ograniczenia

- `TICKET` nie może mieć `scopeType = GLOBAL`.
- Dla `TICKET` wymagane są konkretne oferty, bo trzeba utworzyć realne pule w HT.
- `hptAtnaId`, `hptTicketDefinitionId` i `hptTicketPoolDefinitionId` nadaje wyłącznie generator.
- Endpoint ręcznego dodania oferty do promocji nie służy do ręcznego wpisywania HT ID.
- Promocyjna pula w HT ma być niewidoczna dla Partnera i Portalu.
- Portal może dostać promocyjne pule w danych technicznych, ale nie powinien ich pokazywać jako zwykłej sprzedaży.

### Minimalne punkty regresji technicznej

Po wdrożeniu należy sprawdzić:

1. `GET /market/sight-events/{id}/available-tickets` nadal zwraca zwykłe bilety.
2. Promocyjne pule mają `poolType = PROMOTIONAL`.
3. Promocyjne pule mają `visibleOnPortal = false`.
4. Promocyjne pule mają `visibleForPartner = false`.
5. Partner nie widzi pul promocyjnych w panelu.
6. Standardowy zakup bez kodu działa bez `promotionReservationToken`.
7. Walidacja kodu zwraca `reservationToken`.
8. Zamówienie z tokenem i biletem promocyjnym przechodzi.
9. Potwierdzenie zamówienia ustawia redemption na `USED`.
10. Ponowne użycie kodu jednorazowego zwraca błąd.
