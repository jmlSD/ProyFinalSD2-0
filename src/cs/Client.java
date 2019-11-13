package cs;

import java.io.IOException;
import static java.lang.Thread.sleep;
import java.util.Scanner;
import java.util.Vector;
import org.apache.xmlrpc.*;

public class Client {
    private static final int PuertoServidor = 8080;
    
    public static void main(String[] args) {
        String c1 = "";
        try{
            XmlRpcClient rpcClient = new XmlRpcClient("http://localhost:" + PuertoServidor);
            Vector<Object> params = new Vector<>(), parmenu = new Vector<>();
            int opc = 0; 
            Object procMenu = new Object();
            Scanner sc = new Scanner(System.in);
            System.out.println("Conexion exitosa al servidor");
            
            Object menu = rpcClient.execute("myServer.desplegaMenu", params);//Menu Sockets, RPC, RMI
            do{ 
                
                if(opc < 0 || opc > 3)
                    System.out.println("Opcion no valida");
                
                System.out.println(menu);
                opc = sc.nextInt();
                
            }while(opc < 0 || opc > 3);
            
            params.clear();
            parmenu.add(opc);
            int opc2 = opc;
            
            do{
                procMenu = rpcClient.execute("myServer.solicitudMenu", parmenu);
                System.out.println(procMenu);
                opc = sc.nextInt();
                
                if(opc > 0 && opc < 4){
                    System.out.println("Que letra o palabra");
                    c1 = sc.next();
                    params.clear();
                    params.add(opc);
                    params.add(c1);
                    Object procOper = null;
                    //xvqwuycveqiucvwqiucvikjhgf

                    if(opc2 == 1){
                        procOper = rpcClient.execute("myServer.procesamientoSCK", params);
                        
                    }else if(opc2 == 2){
                        /*Object procOper = rpcClient.execute("myServer.pideLetraoPal", params);
                        System.out.println(procOper);//Pide letra o palabra
                        String pal = sc.nextLine();*/              
                        procOper = rpcClient.execute("myServer.procesamientoRPC", params);
                        
                    }else if(opc2 == 3){
                        /*Object procOper = rpcClient.execute("myServer.pideLetraoPal", params);
                        System.out.println(procOper);//Pide letra o palabra
                        String pal = sc.nextLine();//Lee la palabra o letra a buscar*/
                        procOper = rpcClient.execute("myServer.procesamientoRMI", params);
                    }
                    System.out.println(procOper);
                }       
        }while(opc != 3); 
            
            sleep(1000);
        }catch(XmlRpcException | IOException | InterruptedException ex){
            System.err.println("Client: " + ex);
        }        
    }    
}
