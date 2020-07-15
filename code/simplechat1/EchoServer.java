// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import ocsf.server.*;

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
  public void handleMessageFromClient(Object msg, ConnectionToClient client)
  {
    System.out.println("Message received: " + msg + " from " + client);
    this.sendToAllClients(msg);
  }
    
  // E6 c)
  // Add a new method to use # commands with special functions
  // Add #quit, #stop, #close, #setport<port>, #gethost, #getport

  public void handleMessageFromServerUI(String message) {
    try {
      this.listen();
      System.out.println(message);
    }
    
    catch(IOException e) {
      System.out.println("Could not send message to server.  Terminating server.");
      System.exit(1);
    }
    
    // Add #quit, #stop, #close, #setport<port>, #start, #getport

    // split the message to choose every word
    String[] messageSplit = message.split(" ");
    // each command should start with symbol #
    // so we check if it's a # command or not 
      if(messageSplit[0].equals("#")) {
        // use switch-case statement
        switch(messageSplit[0]) {
          
          // #quit case
          // use close from AbstractServer class
          case "#quit":
            try {
              this.close();
            }
            catch(Exception ex) {
              System.out.println("Quitting");
              break;
            }
            break;
          
          // #stop case
          case "#stop":
            // use stopListening() method from AbstractServer
            this.stopListening();
            break;
          
          // #close case
          case "#close":
            // use close() method from AbstractServer
            try {
              this.close();
            }
            catch(Exception ex) {
              System.out.println("Closing");
              break;
            }
            break;

          // #setport <port> case
          case "#setport":
            // check if server is connected 
            // use isListening from AbstractServer
            if(this.isListening() == false) {
              // if not, then use the port argument as setport
              this.setPort(Integer.parseInt(messageSplit[1]));
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
              }
              catch(Exception ex) {
                System.out.println("Exception error while quitting");
                break;
              }
            break;
            }

            else {
              System.out.println("The server has already started");
            }
            break;

          // #getport case
          case"#getport":
            // use getPort() method from AbstractServer 
            System.out.println("Port: " + this.getPort());
            break;

          default:
            System.out.println("The command is invalid");
            break;
        }
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
    System.out.println("Client is connected!");
  }


  public void clientDisconnected(ConnectionToClient client) {
    System.out.println("Client has disconnected!");
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
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of EchoServer class
