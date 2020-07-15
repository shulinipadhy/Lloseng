// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  
  // E7 a)
  // add login Id 
  String loginId;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  // add loginId into constructor
  public ChatClient(String loginId, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.loginId = loginId;
    this.clientUI = clientUI;
    openConnection();
    // login for client to connect to server
    handleMessageFromClientUI("#login: " + loginId);

    // mandatory loginId, otherwise clinet should quit if not provided
    if(loginId == null) {
      clientUI.display("LoginId mandatory! Client is quitting.");
      quit();
    }
  }

  
  //Instance methods ************************************************
    
  // method to get login Id
  public String getLoginId() {
    return loginId;
  }

  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  

  public void handleMessageFromClientUI(String message)
  {
    try
    {
      sendToServer(message);
    }
    catch(IOException e)
    {
      clientUI.display("Could not send message to server.  Terminating client.");
      quit();
    }

    // E6 a)
    // Add #quit, #login, #logoff, #sethost<host>, #setport<port>
    // #gethost, #getport

    
    // split the message to choose every word
    String[] messageStringSplit = message.split(" ");
    // split the message to choose every character
    char[] messageCharSplit = message.toCharArray();
    // each command should start with symbol #
    // so we check if it's a # command or not 
      if(messageCharSplit[0] == ('#')) {
        // use switch-case statement
        switch(messageStringSplit[0]) {
          
          // #quit case
          case "#quit":
            quit();
            break;
          
          // #logoff case
          case "#logoff":
            // just in case there is a connection exception
            // use closeConnection method
            try {
              closeConnection();
            }
            // catch the exception
            catch(IOException exception) {
              System.out.println("Exception error while logging off!");
              break;
            }
          
          // #sethost <host> case
          case "#sethost":
            // check if client is connected 
            if(isConnected() == false) {
              // if not, then use the host argument as sethost
              this.setHost(messageStringSplit[1]);
            }
            else {
              // unable to set port while connected
              System.out.println("Unable to set host while connected!");
            }
            break;

          // #setport <port> case
          case "#setport":
            // check if client is connected 
            if(isConnected() == false) {
              // if not, then use the port argument as setport
              this.setPort(Integer.parseInt(messageStringSplit[1]));
            }
            else {
              // unable to set port while connected
              System.out.println("Unable to set port while connected!");
            }
            break;

          // #gethost case
          case "#gethost":
            // use getHost() method from AbstractClient class
            System.out.println("Host: " + this.getHost());
            break;

          // #getport case
          case"#getport":
            // use getPort() method from AbstractClient class
            System.out.println("Port: " + this.getPort());
            break;

          default:
            System.out.println("ERROR - No login ID specified. Connection aborted.");
            break;
        }
      }
  }
  
  // E5 a)
  // methods from Abstract Client 
  public void connectionClosed() {
    clientUI.display("The connection of the server is closed!");
    quit();
  }

  public void connectionException(Exception exception) {
    clientUI.display("Connection interrupted, did not terminate properly.");
    quit();
  }



  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
}
//End of ChatClient class
