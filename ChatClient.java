
package chatproject;

import java.net.*;
import java.io.*;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ChatClient {
    private Socket ircSocket;
    private String username, server;
    private PrintWriter out;
    private Scanner in;
    private TelaPrincipal tela;
    
    public ChatClient(TelaPrincipal tela){
        this.tela = tela; 
        
    }
    
    public void serverConnect(String username, String channel, String server) {
        try {
            ircSocket = new Socket(server, 6667);
            
            
            out = new PrintWriter(ircSocket.getOutputStream(), true);
            in = new Scanner(ircSocket.getInputStream());
            
            //out.print("PASS " + "1324" + "\r\n");
            out.print("USER " + username + " 0 * :"+ username + "\r\n");
            out.print("NICK " + username + "\r\n");
            out.print("JOIN " + channel + "\r\n");
            
            System.out.println(">>>> " + "NICK " + username);
            System.out.println(">>>> " + "USER " + username + " 0 * :"+ "Nicolas M.");
            System.out.println(">>>> " + "JOIN " + channel);
            
            if (ircSocket.isConnected()){
                tela.writeInfo("Connected to the server.\n");
            }
            
            while(in.hasNext()){
                String message = in.nextLine();
                System.out.println("SERVER | " + message);
                tela.addMessageToChat(message);
                if(message.startsWith("PING")){
                    String ping = message.split(" ", 2)[1];
                    out.print("PONG " + ping + "\r\n");
                    System.out.println("PONG " + ping);
                    tela.addMessageToChat(message);
                }
                
            }
            
        } catch (UnknownHostException e) {
            e.printStackTrace();
            tela.writeInfo("Failed to connect to the server.\n");
        } catch (IOException e) {
            e.printStackTrace();
            tela.writeInfo("Failed to connect to the server.\n");
        }
        
        
    }
    
    public void sendMessage(){
        String message = tela.getMessage();
        out.print(message + "\r\n");
        tela.addMessageToChat(message);
    }
    
    public void disconnect(){
        try{
            
            out.close();
            in.close();
            ircSocket.close();
            tela.writeInfo("Disconnected from the server.\n");
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public Socket getIrcSocket() {
        return ircSocket;
    }
    
    
}

