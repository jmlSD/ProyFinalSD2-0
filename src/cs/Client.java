package cs;

import java.io.IOException;
import static java.lang.Thread.sleep;
import java.util.Scanner;
import java.util.Vector;
import org.apache.xmlrpc.*;

public class Client {
    private static final int PuertoServidor = 8080; //puerto utilizado por el server Principal
    
    public static void main(String[] args) {
        // Declaracion de variables: c1 es la cadena a procesar, params son los parametros para el server principal, 
        //opc es opcion de los diferentes menus, procMenu es el menu obtenido del proceso, sc es el scanner para la lectura de datos
        String c1 = "";
        Vector<Object> params = new Vector<>(), parmenu = new Vector<>();
        String opc = "0"; 
        Object procMenu = new Object();
        Scanner sc = new Scanner(System.in);
        
        try{
            //Se realiza la conexion al server principal y obtiene el primer menu
            XmlRpcClient rpcClient = new XmlRpcClient("http://localhost:" + PuertoServidor);
            System.out.println("Conexion exitosa al servidor");
            Object menu = rpcClient.execute("myServer.desplegaMenu", params);//Menu Sockets, RPC, RMI
            
            do{     //Se repite hasta seleccionar una opcion valida, numero entre 1 y 3 
                System.out.println(menu);
                do{
                opc = sc.next();
                if(!esNumero(opc))
                        System.out.println("Opcion invalida");
                }while(!esNumero(opc));
                
            }while(Integer.parseInt(opc) < 0 || Integer.parseInt(opc) > 3);
            
            //Se almacena la opcion del menu 1 
            params.clear();
            int opc2 = Integer.parseInt(opc);
            parmenu.add(opc2);
            
            
            do{     //Se hace la solicitud el menu al servidor secundario
                procMenu = rpcClient.execute("myServer.solicitudMenu", parmenu);
                System.out.println(procMenu);
                
                do{     //Solicita una opcion valida, numero entre 1 y 3
                    opc = sc.next();
                    if(!esNumero(opc))
                            System.out.println("Opcion invalida");
                }while(!esNumero(opc));
                
                if(Integer.parseInt(opc) > 0 && Integer.parseInt(opc) < 4){
                    if(Integer.parseInt(opc) < 2) {
                        
                        do{     //caso de buscar letra
                            System.out.println("Que letra");
                            c1 = sc.next();
                        }while(c1.length() != 1);
                    
                    }else if(Integer.parseInt(opc) < 3){    //caso de buscar una palabra
                        System.out.println("Que palabra");
                        c1 = sc.next();  
                    }
                    
                    // Se agregan los parametros para procesar la info
                    params.clear();
                    params.add(Integer.parseInt(opc));
                    params.add(c1);
                    Object procOper = null;
                    
                    
                    // Se llama el procedimiento correpondiente para cada servidor
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
                }else
                    System.out.println("Opcion invalida");
        }while(Integer.parseInt(opc) != 3); 
            
            sleep(1000);
        }catch(XmlRpcException | IOException | InterruptedException ex){
            System.err.println("Client: " + ex);
        }        
    }    
    
    public static boolean esNumero(String cad) {  //Funcion para determinar si una cadena es un numero
        boolean res;

        try {
            Integer.parseInt(cad);
            res = true;
        } catch (NumberFormatException ex) {
            res = false;
        }
        return res;
    }
}
