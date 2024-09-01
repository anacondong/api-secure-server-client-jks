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

=============================================================

Sequence of Events:
Server Preparation:

server.jks (Server KeyStore):

The server.jks file is created on the server. It contains the server's private key and public certificate.
The private key is used to encrypt data that the server sends to the client, ensuring that only the client with the corresponding public key can decrypt it.
Export the Public Certificate (server.cer):

The server's public certificate is exported from server.jks and saved as server.cer.
This certificate contains the public key, which the client will use to verify the server’s identity.
Client Preparation:

client-truststore.jks (Client TrustStore):
The client imports the server.cer into its client-truststore.jks.
This truststore is used by the client to verify the server's certificate during the SSL/TLS handshake.
SSL/TLS Handshake:

Step 1: Client Initiates Connection:

The client sends an HTTPS request to the server. This request is encrypted using SSL/TLS.
Step 2: Server Responds with Certificate:

The server responds by sending its SSL certificate, which is derived from the server.jks and includes the public key.
This certificate is presented to the client to prove the server's identity.
Step 3: Client Verifies Server Certificate:

The client receives the server's certificate and checks it against the certificates stored in client-truststore.jks.
If the certificate matches and is valid (i.e., signed by a trusted authority or matches the server.cer), the client trusts that it is indeed communicating with the intended server.
Step 4: Secure Session Established:

Once the server's certificate is validated, a secure SSL/TLS session is established.
Both the client and server now share a session key, which is used to encrypt and decrypt the data transmitted during the session.
Data Transmission:

After the secure session is established, all data exchanged between the client and server is encrypted, ensuring confidentiality and integrity.
Summary of Roles:
server.jks (Server KeyStore):

Contains the server's private key and public certificate.
Used by the server to establish its identity to clients and decrypt data sent by clients.
server.cer (Server Certificate):

The public certificate exported from server.jks.
Distributed to clients so they can trust the server's identity.
client-truststore.jks (Client TrustStore):

Contains the server's public certificate (server.cer).
Used by the client to verify the server's identity during the SSL/TLS handshake.
This sequence ensures that the client can trust the server it is communicating with and that the communication between them is securely encrypted.


=============================================================

Sequence of Events for Adding More Trusted Sites:
1. Obtain Public Certificates from Additional Servers
For each new server that you want to trust, you must obtain the server's public certificate (server.cer).
This certificate can usually be exported from the server's keystore (similar to how server.cer was exported from the server.jks in your initial setup).
2. Import the Certificates into the Client's TrustStore
Import each server’s public certificate into the client’s truststore (client-truststore.jks).
You can do this using the keytool command for each certificate:
bash
Copy code
keytool -import -alias server2 -file server2.cer -keystore client-truststore.jks -storepass yourpassword
server2 is a unique alias for the new server’s certificate.
server2.cer is the public certificate file for the new server.
Repeat this process for each additional server.
3. Verify TrustStore Configuration
Ensure that the client’s application.properties file correctly points to the client-truststore.jks:
properties
Copy code
server.ssl.trust-store=classpath:client-truststore.jks
server.ssl.trust-store-password=yourpassword
Ensure that the truststore is updated with all necessary server certificates.
4. Client Initiates a Connection to a New Server
The client application can now initiate an HTTPS connection to any of the trusted servers.
The client sends an HTTPS request to the new server (e.g., https://newserver.com/api/endpoint).
5. Server Responds with Its Certificate
The new server responds by sending its SSL certificate during the SSL/TLS handshake.
6. Client Verifies the Server Certificate
The client checks the received server certificate against the certificates stored in client-truststore.jks.
If the certificate matches one of the certificates in the truststore, the client trusts the server, and the connection proceeds.
7. Secure SSL/TLS Session is Established
Upon successful verification, a secure SSL/TLS session is established between the client and the new server.
Data is then securely transmitted between the client and the new server.
8. Repeat the Process for Additional Servers
Repeat the above steps (1 to 7) for each additional server you wish to trust. Each server's certificate must be imported into the client-truststore.jks.
Example Scenario:
Let’s assume you have a client that needs to connect to three different servers (server1, server2, and server3). Here’s what you’d do:

Export Certificates:

Obtain server1.cer, server2.cer, and server3.cer from server1, server2, and server3 respectively.
Import into TrustStore:

Run the following commands to import these certificates into client-truststore.jks:
bash
Copy code
keytool -import -alias server1 -file server1.cer -keystore client-truststore.jks -storepass yourpassword
keytool -import -alias server2 -file server2.cer -keystore client-truststore.jks -storepass yourpassword
keytool -import -alias server3 -file server3.cer -keystore client-truststore.jks -storepass yourpassword
Client Configuration:

Ensure the client’s application.properties is correctly configured to use the client-truststore.jks.
Establish Connections:

The client can now securely connect to any of the three servers (server1, server2, or server3), as their certificates are trusted.
Important Notes:
Unique Aliases: Ensure that each imported certificate uses a unique alias within the truststore.
TrustStore Integrity: Regularly backup your truststore, especially when making changes.
Environment Consistency: If this truststore is used in multiple environments (e.g., development, staging, production), ensure consistency across all environments.
This process allows the client to trust and securely communicate with multiple servers, each identified by its SSL certificate.







