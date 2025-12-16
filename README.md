# Modyfikacja propertiesów w różnych środowiskach:
Propertiesy wykorzystywane podczas budowania aplikacji (np. `hibernate.hbm2ddl.auto=create`):
- do komendy mavenowej dorzucamy parametr `-Dlocal.maven.build.properties=<ścieżka_do_pliku>`
- przykład: `mvn clean compile -Dlocal.maven.build.properties=C:\Users\kret\workspaces\HelloPoland\local.maven.build.properties`



Propertiesy wykorzystywane w runtime aplikacji (np. `db.filler.run=true`):
- do komendy uruchomieniowej serwera aplikacji dorzucamy parametr `-Dlocal.runtime.properties=<ścieżka_do_pliku>`
- przykład: `$WILDFLY_HOME/bin/standalone.sh -Dlocal.runtime.properties=C:\Users\kret\workspaces\HelloPoland\local.runtime.properties`


# Docker

Obraz dockerowy budujemy po wcześniejszym zbudowaniu paczki (.war).
Komenda uruchomieniowa Wildfly w obrazie wygląda następująco:
+ `CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-Dlocal.runtime.properties=/opt/local.runtime.properties", "-b", "0.0.0.0"]`

oznacza to, że zmienne runtimowe można modyfikować również poprzez plik dostarczany jako volume:
+ `docker run -v /path/to/runtime/properties:/opt/local.runtime.properties`

Można łączyć ze zmiennymi typu file w GitLab CI.

Ustawienie domyślne ścieżki do DMS (`dms.root.path`) jest przygotowane z myślą o kontenerze: /DMS. 
Aby nie utracić danych między releasami zalecane jest utworzenie named volume i podpięcie go na ścieżce `dms.root.path`:

+ `docker run -v HELLO_DMS:/DMS`
Polecenie do podmontowania do katalogu z plikami (DMS):
+ -v /var/lib/docker/volumes/HELLO_DMS/_data:/DMS:ro
Analogicznie wygląda proces dla klonów HPL.

