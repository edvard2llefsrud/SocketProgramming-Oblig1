import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EchoUcaseServerTCP
{
    public static void main(String[] args) throws IOException
    {
        int portNumber = 5556; // Default port to use

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
        )
        {


            while(true){
                clientStarter aClient = new clientStarter(serverSocket.accept());
                aClient.start();
            }



        } catch (IOException e)
        {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }

    }

    static class clientStarter extends Thread{

        Socket connectSocket;
        InetAddress aClientAdr;
        int serverPort;
        int clientPort;

        public clientStarter(Socket connectSocket){
            this.connectSocket = connectSocket;
            aClientAdr = connectSocket.getInetAddress();
            clientPort = connectSocket.getPort();
            serverPort = connectSocket.getLocalPort();
        }

        public void run(){
            try(
                    PrintWriter output = new PrintWriter(connectSocket.getOutputStream(),true);
                    BufferedReader input = new BufferedReader(new InputStreamReader(connectSocket.getInputStream()));
            ){

                System.out.println("helllo");
                //output.println("Hello");
                String URL;
                URL url;
                BufferedReader inStream;
                ArrayList<String> mails = new ArrayList<>();

                while (((URL = input.readLine()) != null))
                {
                    //output.print("ok");
                    if(URL.contains("http")){
                        url = new URL(URL);
                    } else {
                        url = new URL("https://" + URL);
                    }
                    try{
                        inStream = new BufferedReader(new InputStreamReader(url.openStream()));
                        String inputLine;
                        StringBuilder HTML = new StringBuilder();
                        while ((inputLine = inStream.readLine())!=null) {
                            HTML.append(inputLine);
                        }

                        Pattern pattern = Pattern.compile("([a-z0-9.-]+)@([a-z0-9.-]+[a-z])");
                        Matcher matcher = pattern.matcher(HTML.toString());
                        int counter = 0;
                        String mail1s = "";

                        while(matcher.find()){
                            System.out.println(matcher.group());
                            mail1s += matcher.group() + "\n";
                            counter++;
                        }
                        if(counter != 0){
                            output.println("0\n"+mail1s);
                        }else{
                            output.println("1");
                        }
                    }catch (UnknownHostException ue){
                        output.println("2");

                    }




                    //System.out.println(mails.size());



                }





                System.out.println("I (Server) [" + connectSocket.getLocalAddress().getHostAddress() + ":" + serverPort +"] > "  /*outText*/);


                //output.println("lol");
            }catch (UnknownHostException ue){
                ue.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }


            // close the connection socket
            try {
                connectSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}


