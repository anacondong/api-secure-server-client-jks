Generate a KeyStore (server.jks):
keytool -genkeypair -alias server -keyalg RSA -keysize 2048 -storetype JKS -keystore server.jks -validity 3650

Export the public certificate from the KeyStore (.cer)
keytool -export -alias server -file server.cer -keystore server.jks

Generate a TrustStore (client-truststore.jks) and import the server certificate:
keytool -import -alias server -file server.cer -keystore client-truststore.jks


run both with : mvn spring-boot:run

testing: curl http://localhost:8080/api/client-call