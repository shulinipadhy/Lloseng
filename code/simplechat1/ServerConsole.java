import java.io.*;
import ocsf.server.*;
import client.*;
import common.*;

// E6 b)
public class ServerConsole implements ChatIF {
	
	// Class variables ***************************************************
	final public static int DEFAULT_PORT = 5555;

	// Instance variables ************************************************
	EchoServer server;
	
	// Constructor *******************************************************
	public ServerConsole(int port) {
    	// IOException is never thrown in body of try-catch statement
    	server = new EchoServer(port);
    	try {
    		// use listen() method from AbstractServer
    		// since it already throws an IOException
    		server.listen();
    	} 
    		
    	catch(IOException exception) {
      		System.out.println("Error: Can't setup connection!"
                	  + " Terminating server.");
      		System.exit(1);
    	}
  	}

    // Instance methods **************************************************
	public void display(String message) {
		System.out.println("SERVER MSG> " + message);
	}

	public void accept() {
		try {
			BufferedReader fromConsole;  
        	fromConsole = new BufferedReader(new InputStreamReader(System.in));
      		String message;

      		while (true) {
      			message = fromConsole.readLine();
       			server.handleMessageFromServerUI(message);
       		}
    	} 
    		
    	catch (Exception ex) {
      		System.out.println("Unexpected error while reading from console!");
    	}
  	}




  	// Class methods *****************************************************
  	// This method is responsible for the creation of the Server UI

  	public static void main(String[] args) {
    	int port = 0;  //The port number

    try {
      // no need to take any arguments like ClientConsole
      // since port is the only argument 
      port = DEFAULT_PORT;
    }
    
    catch(ArrayIndexOutOfBoundsException e) {
    	// in case of any exceptions
    	port = DEFAULT_PORT;
    }
    
    ServerConsole chat= new ServerConsole(DEFAULT_PORT);
    chat.accept();  //Wait for console data
  }
}
//End of ConsoleChat class



	