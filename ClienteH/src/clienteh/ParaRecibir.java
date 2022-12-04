
package clienteh;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class ParaRecibir implements Runnable{
      final DataInputStream entrada;
    private String nombre;
    private ParaEnviar paraEnviar;
    public ParaRecibir(Socket socket) throws IOException {
        entrada = new DataInputStream(socket.getInputStream());
    }
    public void setParaEnviar(ParaEnviar paraEnviar){
        this.paraEnviar = paraEnviar;
    }

    @Override
    public void run() {
        String mensaje;
        while (true) {
            try {
                mensaje = entrada.readUTF();
                if (mensaje.equals("--nombre igual--")) {
                    System.exit(1);
                } else if (mensaje.contains("BLOQUEAR")) {
                    bloquear(mensaje);
                } else if(mensaje.contains("DESBLOQUEO")){
                    desbloquear(mensaje);
                }else if(mensaje.contains("DIRECTORIO")){
                    directorio(mensaje);
                }else if(mensaje.contains("SOLICITUD")){
                    recibirSolicitud(mensaje);
                }else if(mensaje.contains("ACEPTADA-")){
                    solicitudAceptada(mensaje);
                }else{
                    filtroBloqueo(mensaje);
                }

            } catch (IOException ex) {

            }
        }
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    private void bloquear(String mensaje) {
        String mensajeRecortado[] = mensaje.split(" ");
        switch (mensajeRecortado[1]) {
            case "MismoUsuario":
                System.out.println("No te puedes bloquear a ti mismo");
                break;
            case "NoExiste":
                System.out.println("No exite ese usuario");
                break;
            default:
                boolean bloqueado = false;
                for (String n : ClienteH.bloqueados) {
                    if (n.equalsIgnoreCase(mensajeRecortado[1])) {
                        bloqueado = true;
                        break;
                    }
                }
                if (bloqueado != true) {
                    ClienteH.bloqueados.add(mensajeRecortado[1]);
                    System.out.println("Se ha agregado a bloqueados");
                } else {
                    System.out.println("Ya se encuentra bloqueado");
                }

                break;
        }
    }

    private void filtroBloqueo(String mensaje) {
        boolean bloqueado = false;
        String mensajes[] = mensaje.split(" ");
        for (String n : ClienteH.bloqueados) {
            if (mensajes[0].equalsIgnoreCase(n+":")) {
                bloqueado = true;
                break;
            }
        }
        if (bloqueado != true) {
            System.out.println(mensaje);
        } 
    }

    private void desbloquear(String mensaje) {
          String mensajeRecortado[] = mensaje.split(" ");
        switch (mensajeRecortado[1]) {
            case "MismoUsuario":
                System.out.println("No te puedes desbloquear si no estas bloqueado");
                break;
            case "NoExiste":
                System.out.println("No exite ese usuario");
                break;
            default:
                boolean bloqueado = false;
                for (String n : ClienteH.bloqueados) {
                    if (n.equalsIgnoreCase(mensajeRecortado[1])) {
                        bloqueado = true;
                        break;
                    }
                }
                if (bloqueado == true) {
                    ClienteH.bloqueados.remove(mensajeRecortado[1]);
                    System.out.println("Se ha desbloqueado a la persona");
                } else {
                    System.out.println("Ese usuario no se encuentra bloqueado");
                }

                break;
        }
    }

    private void directorio(String mensaje) {
        String archivos = "";
        File file = new File(".");
        File[] files = file.listFiles();
        for(File ff : files){
            if(ff.isFile()){
                archivos += ff.getName() + "\n";
            }
        }
        if(!archivos.equals("")){
            
            String[] mensajes =mensaje.split(":");
            paraEnviar.enviar(archivos+ " @" +mensajes[0]);
        }else{
            System.out.println("No se encontraron archivos");
        }
    }

    private void recibirSolicitud(String mensaje) {
        String[] mensajeDividido= mensaje.split("-");
        String nombre = mensajeDividido[1];
        System.out.println("Has recibido una solicitud de " + nombre);
        ClienteH.solicitudes.add(nombre);
    }
    
    private void solicitudAceptada(String mensaje){
        paraEnviar.enviar(mensaje);
    }

    
    
}
