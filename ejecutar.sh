# Script que ejecuta automaticamente la aplicación tras compilarla

# Re compila el codigo
mvn compile

# Ejecutar la aplicación
mvn clean javafx:run -Drun.jvmArguments="-Xmx3g"