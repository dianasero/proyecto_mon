import java.util.concurrent.Semaphore;

public class Grupo {

    private Persona[] integrantes;
    private Mostrador mostrador;
    private Caja caja;
    private int nombre;
    // Definir semaforo de caja y mostrador
    private static Semaphore mostradorPersona = new Semaphore(1);
    // Semaforo de caja
    private static Semaphore cajaPersona = new Semaphore(1);

    public Grupo(int numero, int nombre, Caja caja, Mostrador mostrador) {
        this.mostrador = mostrador;
        this.caja = caja;
        this.integrantes = new Persona[numero];
        this.nombre = nombre;
        crearPersonas();
    }

    private void crearPersonas() {
        for (int i = 0; i < integrantes.length; i++)
            integrantes[i] = new Persona(i, mostrador);
    }

    public void ejecutarPersonas() {
        mostrador.pedirGrupo(nombre);
        for (int i = 0; i < integrantes.length; i++)
            integrantes[i].run();
    }

    synchronized public void pedirHelado(int nombre, String sabor) {
        if (!isGrupoPedido()) {
            while (integrantes[nombre].isPedido()) {
                try {
                    integrantes[nombre].regresarSemGet();
                    wait();
                } catch (InterruptedException e) {
                    System.err.println(e.getMessage());
                }
            }
            while (!integrantes[nombre].isPedido()) {
                try {
                    mostradorPersona.acquire();
                    mostrador.pedirHelado(nombre, sabor, this.nombre);
                    integrantes[nombre].setPedido(true);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mostradorPersona.release();
                integrantes[nombre].regresarSemGet();
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxx");
            }
        } else {
            notifyAll();// Puede ser una u otra;
            // while (isGrupoPedido())
            // notify();
            System.out.println("El grupo " + this.nombre + " terminó de pedir su helado");
        }
    }

    synchronized public void pagarHelado(int nombre) {
        //while (integrantes[nombre].isPedido() && !isGrupoPedido()) {
        //     try {
        //         wait();
        //     } catch (InterruptedException e) {
        //         // TODO Auto-generated catch block
        //         e.printStackTrace();
        //     }
        // }
        // if(isGrupoPedido()){
        //     notifyAll();
        // }
        
        if(isGrupoPedido()&&!isGrupoTerminado()){
                while(integrantes[nombre].isPagado()){
                    try{
                        integrantes[nombre].regresarSemPay();
                        wait();
                    }catch (InterruptedException e) {System.err.println(e.getMessage());}
                }
                while(!integrantes[nombre].isPagado()){
                    try{
                        cajaPersona.acquire();
                        caja.pagarHelado(nombre, this.nombre);
                        integrantes[nombre].setPagado(true);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                    cajaPersona.release();
                    integrantes[nombre].regresarSemPay();
                    System.out.println("xxxxxxxxxxxxxxxxxxxxxxxx");
                } 
        }
        if(isGrupoPedido()&& isGrupoTerminado()){
            notifyAll();//Puede ser una u otra;
            // while (isGrupoPedido())
            //     notify();
            System.out.println("El grupo "+ this.nombre+ " terminó de pagar su helado y de ser atendido ---- fin");
            
        }
    }    
    public void setGrupo(Grupo grupo){
        for(int i =0; i< integrantes.length;i++)
            integrantes[i].setGrupo(grupo);
    }
    public boolean isGrupoTerminado(){
        boolean terminado = true;
        for(int i =0; i< integrantes.length;i++)
            terminado &= integrantes[i].isPagado();
        return terminado;
    }

    public boolean isGrupoPedido(){
        boolean terminado = true;
        for(int i =0; i<integrantes.length;i++)
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
