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
    
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  // E7 a)
  // add loginID into constructor
  public ChatClient(String loginID, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    openConnection();
    // login for client to connect to server
    this.sendToServer("#login: " + loginID);

    // mandatory loginID, otherwise clinet should quit if not provided
    //if(loginID == null) {
      //clientUI.display("LoginID mandatory! Client is quitting.");
      //quit();
    //}
  }

  
  //Instance methods ************************************************
  
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
    /*try
    {
      sendToServer(message);
    }
    catch(IOException e)
    {
      clientUI.display("Could not send message to server.  Terminating client.");
      quit();
    } */

    // E6 a)
    // Add #quit, #login, #logoff, #sethost<host>, #setport<port>
    // #gethost, #getport

    
    // split the message to choose every character
    char[] messageCharSplit = message.toCharArray();
    // each command should start with symbol #
    // so we check if it's a # command or not 
    if(messageCharSplit[0] == '#') {
      // split the message to choose every word
      String[] messageStringSplit = message.split("\\s+");

      // use switch-case statement
      switch(messageStringSplit[0]) {
        
        // #quit case
        case "#quit":
          System.out.println("The server is quitting!");
          quit();
          break;
        
        // #logoff case
        case "#logoff":
          // just in case there is a connection exception
          // use closeConnection method
          try {
            System.out.println("Connection is closing!");
            closeConnection();
          }
          // catch the exception
          catch(IOException exception) {
            System.out.println("Exception error while logging off!");
            break;
          }

        // #login case
        case "#login":
          try {
            openConnection();
            System.out.println("The connection is open!");
          }

          catch(IOException exception) {
            System.out.println("Error: unable to connect!");
          }
          break;
        
        // #sethost <host> case
        case "#sethost":
          // check if client is connected 
          if(isConnected() == false) {
            // if not, then use the host argument as sethost
            this.setHost(messageStringSplit[1]);
            System.out.println("New host is set!");
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
            System.out.println("New port is set!");
          }
          else {
            // unable to set port while connected
            System.out.println("Unable to set port while connected!");
          }
          break;

        // #gethost case
        case "#gethost":
          // use getHost() method from AbstractClient class
          System.out.println("Host: " + getHost());
          break;

        // #getport case
        case"#getport":
          // use getPort() method from AbstractClient class
          System.out.println("Port: " + getPort());
          break;

        default:
          System.out.println("The command is invalid.");
          break;
      }
    }

    else {
      try {
        sendToServer(message);
      }
      
      catch(IOException e) {
        clientUI.display("Could not send message to server.  Terminating client.");
        quit();
      }
    }
  }
  
  // E5 a)
  // methods from Abstract Client 
  public void connectionClosed() {
    clientUI.display("The connection of the server is closed!");
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
