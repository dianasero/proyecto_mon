import java.util.concurrent.Semaphore;

public class Grupo {
    
    private Persona [] integrantes;
    private Mostrador mostrador;
    private Caja caja;
    private int nombre;
     // Definir semaforo de caja y mostrador
     private static Semaphore mostradorPersona = new Semaphore(1);
     // Semaforo de caja
     private static Semaphore cajaPersona = new Semaphore(1);
     
    public Grupo(int numero, int nombre, Caja caja, Mostrador mostrador){
        this.mostrador = mostrador;
        this.caja = caja;
        this.integrantes = new Persona[numero];
        this.nombre = nombre;
        crearPersonas(this);
    }
    private void crearPersonas(Grupo grupo) {
        for(int i = 0; i < integrantes.length; i++)
            integrantes[i] = new Persona(i,mostrador, grupo);
    }
    
    public void ejecutarPersonas(){
        mostrador.pedirGrupo(nombre);
        for(int i =0; i< integrantes.length;i++)
            integrantes[i].run();
    }

    synchronized public void pedirHelado(int nombre, String sabor){
        if(!isGrupoPedido()){
            while (!isGrupoPedido()){//Ya terminaron todos de pedir y de pagar
                if(integrantes[nombre].isPedido()){
                    try{
                        wait();
                    }catch (InterruptedException e) {System.err.println(e.getMessage());}
                } 
                if(!integrantes[nombre].isPedido()){
                    try{
                        mostradorPersona.acquire();
                        mostrador.pedirHelado(nombre, sabor, this.nombre);
                        integrantes[nombre].setPedido(true);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                    mostradorPersona.release();
                }
            }
        }else{
            notifyAll();//Puede ser una u otra;
            // while (isGrupoPedido())
            //     notify();
            System.out.println("El grupo "+ this.nombre+ " terminó de pedir su helado");
        }
    }
    synchronized public void pagarHelado(int nombre){
        if(!isGrupoTerminado()){
            while (!isGrupoTerminado()){//Ya terminaron todos de pedir y de pagar
                if(integrantes[nombre].isPagado()){
                    try{
                        wait();
                    }catch (InterruptedException e) {System.err.println(e.getMessage());}
                } 
                if(!integrantes[nombre].isPagado()){
                    try{
                        cajaPersona.acquire();
                        caja.pagarHelado(nombre, this.nombre);
                        integrantes[nombre].setPagado(true);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                    cajaPersona.release();
                }
            }
        }
        else{
            notifyAll();//Puede ser una u otra;
            // while (isGrupoPedido())
            //     notify();
            System.out.println("El grupo "+ this.nombre+ " terminó de pagar su helado y de ser atendido ---- fin");
            
        }
    }    

    public boolean isGrupoTerminado(){
        boolean terminado = true;
        for(int i =0; i< integrantes.length;i++)
            terminado &= integrantes[i].isPagado();
        return terminado;
    }

    public boolean isGrupoPedido(){
        boolean terminado = true;
        for(int i =0; i< integrantes.length;i++)
            terminado &= integrantes[i].isPedido();
        return terminado;
    }

    public int getNombre(){
        return this.nombre;
    }

    public Persona [] personas(){
        return integrantes;
    }
}
