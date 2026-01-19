# Hello! Poland – Backend (Helpdesk API)

Backend projektu **Hello! Poland** to aplikacja oparta na **Java EE**, udostępniająca interfejs **REST API** wykorzystywany przez frontendy:
* **Helpdesk Web**
* **Partner Web**

Aplikacja jest pakowana jako artefakt `WAR` i uruchamiana na serwerze **WildFly 34** (tryb standalone), najczęściej wewnątrz kontenera **Docker**.

> [!IMPORTANT]
> Konfiguracja środowiskowa (bazy danych, DMS, URL-e) **nie jest częścią artefaktu** i zawsze musi być dostarczona z zewnątrz.

---

## 🛠 Stack Techniczny

| Komponent | Wersja / Technologia |
| :--- | :--- |
| **Java (Runtime)** | Java 17 |
| **Java (Build-time)** | Java 21 |
| **Serwer aplikacji** | WildFly 34 |
| **Baza danych** | PostgreSQL |
| **Komunikacja** | REST (JAX-RS) |
| **ORM / JPA** | Hibernate |
| **Build Tool** | Maven 3.8+ |
| **Konteneryzacja** | Docker |

---

## 📦 Repozytorium i artefakt

* **Repozytorium:** [hp-client-backend](https://github.com/cig-hellopoland/hp-client-backend)
* **Artefakt:** `hellopoland.war`
* **Packaging:** `WAR`
* **Wersja:** Zgodnie z definicją w `pom.xml`

---

## 🏗 Wymagania do builda

Aby zbudować projekt, Twoje środowisko musi spełniać poniższe wymagania:
* **JDK 21**
* **Maven 3.8+**
* **Git**

> [!CAUTION]
> Chociaż runtime WildFly może działać na Javie 17, **proces budowania pliku WAR (kompilacja) bezwzględnie wymaga JDK 21**.

---

## 🚀 Profile Maven

Dostępne profile:
1.  `tst` – ustawiony jako domyślny (`activeByDefault=true`).
2.  `prod` – profil produkcyjny.

**Zalecenie:** Zawsze jawnie podawaj profil w procesach CI/CD.

### Budowa pliku WAR:
```bash
# Budowa dla profilu testowego
mvn -P tst clean package

# Budowa dla profilu produkcyjnego
mvn -P prod clean package
```
**Wynik:** `target/hellopoland.war`

---

## ⚙️ Build-time vs Runtime (Kluczowe rozróżnienie)

### 1. Build-time (Zasoby Maven)
Wykorzystywane do resource filtering i konfiguracji zasobów (np. `web.xml`).
* **Pliki:** `maven.build.tst.properties`, `maven.build.prod.properties`
* **Uwaga:** Każda zmiana w tych plikach **wymaga rebuilda pliku WAR**.

Można również wskazać własny plik właściwości:
```bash
mvn clean package -Dlocal.maven.build.properties=/path/to/maven.build.properties
```

### 2. Runtime (Konfiguracja serwera)
* **Plik:** `local.runtime.properties`
* **Cechy:** Nie jest częścią WAR, dostarczany z zewnątrz (parametr JVM lub wolumen). Nadpisuje konfigurację w czasie działania.
* **Uwaga:** Zmiana wymaga restartu, **bez rebuilda WAR**.

**Przykład (WildFly):**
```bash
standalone.sh -Dlocal.runtime.properties=/path/to/local.runtime.properties
```

---

## 🐳 Docker

Obraz Dockera zawiera serwer WildFly, Javę oraz mechanizm deployu. **Nie zawiera konfiguracji, danych ani zasobów DMS.**

### Przykład uruchomienia:
```bash
docker run -d \
  -p 8180:8080 \
  -v hellopoland.war:/opt/jboss/wildfly/standalone/deployments/hellopoland.war:ro \
  -v local.runtime.properties:/opt/jboss/wildfly/standalone/deployments/local.runtime.properties:ro \
  -v HELLO_DMS:/DMS \
  hpl-backend:<tag>
```

---

## 📂 DMS (Document Management System)

Backend wykorzystuje katalog DMS do przechowywania plików binarnych (obrazy, załączniki, multimedia).

* **Domyślna ścieżka:** `/DMS`
* **Wymagania:** W Dockerze musi być podmontowany **named volume** (np. `HELLO_DMS`) lub **bind-mount**.

**Skutki braku DMS:**
* Brak obrazów w odpowiedziach API.
* Błędy uploadu plików.
* Błędy 500 w niektórych endpointach REST.

---

## 🛣 Endpointy API

API dostępne jest pod kontekstem: `/hellopoland/v1`

**Przykładowe ścieżki:**
* `/hellopoland/v1/market/...`
* `/hellopoland/v1/helpdesk/...`
* `/hellopoland/v1/partner/...`

### Informacja o wersji
Podczas builda generowany jest plik `target/classes/git.properties`, który zawiera:
* Hash commitu
* Nazwę brancha
* Timestamp buildu

---

## ⚠️ Najczęstsze problemy

* ❌ **Błąd kompilacji:** Użycie Javy starszej niż 21.
* ❌ **Zły profil:** Pomylenie profilu `tst` z `prod` podczas pakowania.
* ❌ **Brak konfiguracji:** Brak pliku `local.runtime.properties` przy starcie WildFly.
* ❌ **Brak mediów:** Zapomnienie o zamontowaniu wolumenu pod `/DMS`.

---

## 📜 Zasady projektu

1.  **Zero hardcode'u:** Konfiguracja środowiskowa nigdy nie jest częścią obrazu Dockera.
2.  **Uniwersalność:** Ten sam obraz Docker obsługuje TST i PROD.
3.  **Separacja:** Różnice środowiskowe wynikają wyłącznie z zewnętrznych plików properties.

---

## 📚 Dokumentacja uzupełniająca

* **Wiki projektu:** [Hello! Poland: API](https://wiki.coigdzie.pl/wiki/Hello!_Poland:_API) (Wdrożenia, Środowiska, DMS)
* **Repozytorium Backend:** [hp-client-backend](https://github.com/cig-hellopoland/hp-client-backend)
