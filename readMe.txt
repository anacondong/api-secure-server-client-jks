Generate a KeyStore (server.jks):
keytool -genkeypair -alias server -keyalg RSA -keysize 2048 -keystore server.jks -validity 365 -ext "SAN=dns:localhost,ip:127.0.0.1"

Export the public certificate from the KeyStore (.cer)
keytool -export -alias server -file server.cer -keystore server.jks

Generate a TrustStore (client-truststore.jks) and import the server certificate:
keytool -import -alias server -file server.cer -keystore client-truststore.jks


run Server with : mvn spring-boot:run

run Client with : vm option

-Djavax.net.ssl.trustStore="D:/Coding/GitHub/api-secure-server-client-jks/client/src/main/resources/client-truststore.jks" 
-Djavax.net.ssl.trustStorePassword="yourpassword"

testing: curl http://localhost:8080/api/client-call