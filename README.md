# scalableXmahaba
Google services backend implemented using Java.


To run the http-server:
  1. Open IntelliJ
  2. Run NettyHTTPServer.java
  3. Server will be listening at localhost:8083
  4. Use Insomnia to send JSON objects that include the property "service", which will include the name of your app.
  
To run the rabbitMQ code (which will allow you to listen to the request):
  1. Open IntelliJ.
  2. Open CorrelationID/RPCServer.java
  3. Rename RPC_QUEUE_NAME to the value written in "service".
  4. Run RPCServer.java.
  5. A successful insomnia request will return the sentence "What I'll be getting from running my application".
  
To add your project:
  1. Create your own branch.
  2. Navigate to /src/main/java/CommandPattern.
  3. Add one file named after your service that contains all setters and getters.
  4. Add a folder that contains all your commands (follow email example). 
  
To install jdbc on your machine:
  run from vm terminal: yay -S postgresql-jdbc --noconfirm
  
