// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import ocsf.server.*;
import common.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  
  // E7 c) 
  // #login command should be recognized by the server 
  public void handleMessageFromClient(Object msg, ConnectionToClient client) {
      // check if the client is using #login command 
      String msgStr = msg.toString();
      // split message to choose every character
      char[] messageCharSplit = msgStr.toCharArray();
      // check if it starts with #
      if(messageCharSplit[0] == '#') {
        // split message to choose every word
        String[] messageStringSplit = msgStr.split(" ");
        if(messageStringSplit[0].equals("#login") && messageStringSplit.length>1) {
          if(client.getInfo("loginID") == null) {
                // use method setInfo; takes 2 arguments 
                // the new set ID, and the message
                client.setInfo(("loginID"), messageStringSplit[1]);
                System.out.println(client.getInfo("loginID") + " has logged on.");
                this.sendToAllClients(client.getInfo("loginID") + " has logged on.");
              }

            else {
                try {
                // sendToClient method from AbstractServer
                client.sendToClient("SERVER MSG> Error: loginID could not be reset");
                }
            
                catch(IOException e) {
                System.out.println("Error occured while logging in! Terminating server.");
                }
            }   
        } 
      }

      else {
        if(client.getInfo("loginID") == null) {
              try {
              // sendToClient method from AbstractServer
              client.sendToClient("SERVER MSG> Error: loginID could not be reset");
              client.close();          
              }

              catch(IOException e) {
              System.out.println("Error occured while logging in! Terminating server.");
              }
        }

        else {
          String idString = client.getInfo("loginID").toString();
          System.out.println("Message received: " + msg + " from " + idString);
          // send out loginID info back to client
          // use sendToAllClients method from AbstractServer
          this.sendToAllClients(idString + ": " + msgStr);
        }
      }
  }

  // E6 c)
  // Add a new method to use # commands with special functions
  // Add #quit, #stop, #close, #setport<port>, #gethost, #getport

  public void handleMessageFromServerUI(String message) {
    /*try {
      this.listen();
      System.out.println(message);
    }
    
    catch(IOException e) {
      System.out.println("Could not send message to server.  Terminating server.");
      System.exit(1);
    } */
    
    // Add #quit, #stop, #close, #setport<port>, #start, #getport

    // split the message to choose every character
    char[] messageCharSplit = message.toCharArray();
    // split the message to choose every word
    String[] messageStringSplit = message.split(" ");
    // each command should start with symbol #
    // so we check if it's a # command or not 
      if(messageCharSplit[0] == ('#')) {
        // use switch-case statement
        switch(messageStringSplit[0]) {
          
          // #quit case
          // use close from AbstractServer class
          case "#quit":
            try {
              this.close();
              System.out.println("Server has quit");
            }
            catch(IOException ex) {
              System.out.println("Error while quitting the server.");
            }
            break;
          
          // #stop case
          case "#stop":
            // use stopListening() method from AbstractServer
            this.stopListening();
            // give a notification to user 
            this.sendToAllClients("Notification: Server has stopped listening for connections!");
            break;
          
          // #close case
          case "#close":
            // stop listening before closing
            this.stopListening();
            // use close() method from AbstractServer
            try {
              this.close();
              System.out.println("The server has closed.");
            }
            catch(IOException ex) {
              System.out.println("Error while closing the server.");
            }
            break;

          // #setport <port> case
          case "#setport":
            // check if server is connected 
            // use isListening from AbstractServer
            if(this.isListening() == false) {
              // if not, then use the port argument as setport
              super.setPort(Integer.parseInt(messageStringSplit[1]));
              System.out.println("New port: " + Integer.parseInt(messageStringSplit[1]));
            }
            else {
              // unable to set port while connected
              System.out.println("Unable to set port while connected!");
            }
            break;

          // #start case
          case "#start":
            // check if server is connected
            // if not, use listen() method from AbstractServer
            if(this.isListening() == false) {
              try {
                this.listen();
                System.out.println("The server is listening.");
              }
              catch(IOException ex) {
                System.out.println("Error occured while starting.");
              }
            }

            else {
              System.out.println("The server has already started.");
            }
            break;

          // #getport case
          case "#getport":
            // use getPort() method from AbstractServer 
            System.out.println("Port: " + this.getPort());
            break;

          default:
            System.out.println("The command is invalid");
            break;
        }
      }

      else {
        this.sendToAllClients("SERVER MSG> " + message);
      }
  }


  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }

  // E5 c)
  // prints out a nice messafef when a client disconnects/connects
  // method from AbstractServer
  public void clientConnected(ConnectionToClient client) {
    // E7 c)
    // login command recognized by server when connected
    if (client.getInfo("loginID") != null) {
      System.out.println(client.getInfo("loginID" + " is connected!"));
    }
    else {
      System.out.println("A client is connected!");
    }
  }


  public void clientDisconnected(ConnectionToClient client) {
    System.out.println(client.getInfo("loginID") + " has disconnected!");
    this.sendToAllClients(client.getInfo("loginID") + " has disconnected!");
  }


  public void clientException(ConnectionToClient client, Throwable exception) {
    System.out.println("Client exception occured: Client has disconnected!");
  }
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    ServerConsole sv = new ServerConsole(port);
    
    try 
    {
      sv.accept(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of EchoServer class
