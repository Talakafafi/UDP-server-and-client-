import java.io.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class UDPServer {
      public static final int SERVICE_PORT = 7500;
    // Max size of packet, large enough for almost any client
    public static final int BUFSIZE = 100;
    // Socket used for reading and writing UDP packets
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd-HH:mm:ss");
            LocalDateTime now;
            String Time;
    private DatagramSocket socket;
    public UDPServer(){
        try{
            // Bind to the specified UDP port, to listen for incoming data packets
            socket = new DatagramSocket( SERVICE_PORT );
            System.out.println ("Server active on port " + socket.getLocalPort() );
        }
        catch (Exception e){
            System.err.println ("Unable to bind port");
        }
    }
    public void serviceClients(){
        // Create a buffer large enough for incoming packets
        byte[] buffer = new byte[BUFSIZE];
        for (;;){
            try{
                // Create a DatagramPacket for reading UDP packets
                DatagramPacket packet = new DatagramPacket( buffer, BUFSIZE );
                // Receive incoming packets
                socket.receive(packet);
                Time= LocalDateTime.now().format(dtf);
                
                
                
                ByteArrayInputStream bin = new ByteArrayInputStream (packet.getData());
                InputStreamReader reader = new InputStreamReader(bin);
                BufferedReader bread = new BufferedReader(reader);
                double  op =Double.parseDouble(bread.readLine());
               
                double values[]=new double[packet.getData().length];
              
                int size=0;
                for (int i=0 ; i<packet.getData().length ; i++){            
                values[i]=Double.parseDouble(bread.readLine());
                
                if(values[i]==-1.0)break;
                size = i+1;
                }
                
                 double result=0; 
                 double sum =0;
                 double send[] = new double[2];
                 
                 
                 
                if (op ==1.0){
                for (int i=0 ; i<size ; i++)  sum =sum+values[i]; 
                result = sum /(size)  ;
                send[0]=1;
                        
                }
                else{ 
                 for (int i=0 ; i<size ; i++){
                     if(values[i]>values[i+1])
                         result=values[i];
                     else {
                         result=values[i+1];}
                                     
                     send[0]=2;
                     
                     
                 }
            }
              
             send[1]=result; 
             
             ByteArrayOutputStream out = new ByteArrayOutputStream();
             PrintStream list = new PrintStream(out);
             
                for (int offset = 0; offset <2 ; offset++){
                 list.println(send[offset]);
                }
                 byte[] byteArray = out.toByteArray();

               DatagramPacket sendPacket = new DatagramPacket(byteArray, byteArray.length, packet.getAddress(), packet.getPort());
                socket.send(sendPacket);
                
                System.out.print (Time +"  " + packet.getAddress() + ":" + packet.getPort()+" "  );
                for(int i=0 ; i<size ;i++) System.out.print(values[i]+" ");
                if (op==1)
                System.out.println("- Average ="+result);
                else System.out.println("- max  ="+result);
               
            }
            catch (IOException ioe){
                System.err.println ("Error : " + ioe);
            }
        }
    }
    public static void main(String args[]){
        UDPServer server = new UDPServer();
        server.serviceClients();
    }
}