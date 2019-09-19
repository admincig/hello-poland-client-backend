# Modyfikacja propertiesów w różnych środowiskach:
Propertiesy wykorzystywane podczas budowania aplikacji (np. `hibernate.hbm2ddl.auto=create`):
- do komendy mavenowej dorzucamy parametr `-Dlocal.maven.build.properties=<ścieżka_do_pliku>`
- przykład: `mvn clean compile -Dlocal.maven.build.properties=C:\Users\kret\workspaces\HelloPoland\local.maven.build.properties`



Propertiesy wykorzystywane w runtime aplikacji (np. `db.filler.run=true`):
- do komendy uruchomieniowej serwera aplikacji dorzucamy parametr `-Dlocal.runtime.properties=<ścieżka_do_pliku>`
- przykład: `$WILDFLY_HOME/bin/standalone.sh -Dlocal.runtime.properties=C:\Users\kret\workspaces\HelloPoland\local.runtime.properties`