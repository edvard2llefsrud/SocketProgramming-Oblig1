/**
 * Socket programming example: TCP Server
 * DATA410 Networking and Cloud Computing, Spring 2020
 * Raju Shrestha, OsloMet
 **/
import java.net.*;
import java.io.*;

public class EchoUcaseServerTCP
{
    public static void main(String[] args) throws IOException
    {
        int portNumber = 5555; // Default port to use

        if (args.length > 0)
        {
            if (args.length == 1)
                portNumber = Integer.parseInt(args[0]);
            else
            {
                System.err.println("Usage: java EchoUcaseServerTCP [<port number>]");
                System.exit(1);
            }
        }

        System.out.println("Hi, I am EchoUCase TCP server");

        // try() with resource makes sure that all the resources are automatically
        // closed whether there is any exception or not!!!
        try (
                // Create server socket with the given port number
                ServerSocket serverSocket =
                        new ServerSocket(portNumber);
                // create connection socket, server begins listening
                // for incoming TCP requests
                Socket connectSocket = serverSocket.accept();

                // Stream writer to the connection socket
                PrintWriter out =
                        new PrintWriter(connectSocket.getOutputStream(), true);
                // Stream reader from the connection socket
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connectSocket.getInputStream()));
        )
        {
            InetAddress clientAddr = connectSocket.getInetAddress();
            int clientPort = connectSocket.getPort();
            String receivedText;
            // read from the connection socket
            while ((receivedText = in.readLine())!=null)
            {
                System.out.println("Client [" + clientAddr.getHostAddress() +  ":" + clientPort +"] > " + receivedText);

                if(receivedText.endsWith("url")){
                    String link = receivedText.substring(0, receivedText.length() -4);
                    URL url = new URL(link);
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));

                    String textLine;
                    while ((textLine = bufferedReader.readLine()) != null){
                        if(textLine.contains("@")){
                            String[] mailadress = textLine.split("@");
                            if(mailadress[1].contains(".")){
                                String[] domainName = mailadress[1].split(".");
                                if(domainName[1].length() <= 3){
                                    out.println(textLine);
                                }
                            }
                        }
                    }
                }

                String outText = receivedText.toUpperCase();
                // Write the converted uppercase string to the connection socket
                out.println(outText);
                System.out.println("I (Server) [" + connectSocket.getLocalAddress().getHostAddress() + ":" + portNumber + "] > " + outText);
            }

            System.out.println("I am done, Bye!");
        } catch (IOException e)
        {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }

    }
}
