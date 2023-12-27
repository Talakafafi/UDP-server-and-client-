import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime; 
import java.util.logging.Level;
import java.util.logging.Logger;

public class UDPClient {
    public static void main(String args[]){
        String hostname = "127.0.0.1";
        InetAddress addr = null;
         try{
            // Resolve the hostname to an InetAddr
            addr = InetAddress.getByName(hostname);
        }
        catch (UnknownHostException uhe){
            System.err.println ("Unable to resolve host");
            return;
        }
        try {
            ArrayList<Double> values = new ArrayList<>();
         
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println ("Which operation you want to be  performed ?");
            System.out.println ("1... avg");
            System.out.println ("2... max");
            String response = reader.readLine();
            double value = Double.parseDouble(response);
            values.add(value);
            
                    
            while (value != -1 ){
            System.out.println ("please enter a value (enter -1 if you finished)");
            String valu = reader.readLine();
            value = Double.parseDouble(valu);
            values.add(value);
            }
      
             ByteArrayOutputStream out = new ByteArrayOutputStream();
             PrintStream list = new PrintStream(out);
             
                for (int offset = 0; offset <values.size() ; offset++){
                 list.println(values.get(offset));
                }
                
                DatagramSocket socket = new DatagramSocket();
                  // Set a timeout value of two seconds
                socket.setSoTimeout (2 * 1000);
                
                byte[] byteArray = out.toByteArray();
                
                DatagramPacket sendPacket = new DatagramPacket(byteArray, byteArray.length, addr, 7500);
                 System.out.println ("Sending packet to " +hostname);
                // Send the packet
                socket.send (sendPacket);
                System.out.print ("Waiting for packet.... ");
                
                byte[] recbuf = new byte[100];
                DatagramPacket receivePacket = new DatagramPacket(recbuf, 100);
                // Declare a timeout flag
                boolean timeout = false;
                 try{
                    socket.receive (receivePacket);
                }
                catch (InterruptedIOException ioe){
                    timeout = true;
                }
            if (!timeout){
                    System.out.print ("packet received! :  ");
                    System.out.println ("Details : " +receivePacket.getAddress());
                  
                    
                ByteArrayInputStream bin = new ByteArrayInputStream (receivePacket.getData(), 0, receivePacket.getLength());
                InputStreamReader replay = new InputStreamReader(bin);
                BufferedReader bread = new BufferedReader(replay);
                double  op =Double.parseDouble(bread.readLine());
                double result=Double.parseDouble(bread.readLine());
                    
                        if(op == 1){
                        System.out.print("Average of your numbers is : ");
                        System.out.println(result);
                        }
                        if(op==2){
                        System.out.print("Maximum of your numbers is : ");
                        System.out.println(result);
                        }      
                }
                else{
                    System.out.println ("packet lost!");
                }
                // Sleep for a second, to allow user to see packet 
                try{
                    Thread.sleep(1000);
                } 
                catch (InterruptedException ie) { }
            
            
            
        } catch (IOException ex) {
           System.err.println ("I/O error");
        }
    }
    
}