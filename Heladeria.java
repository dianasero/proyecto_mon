import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;

public class Heladeria{
    private int barquillos;
    private int servilletas;
    private Hashtable<String, Integer> sabores;
    private Caja caja;
    private Mostrador mostrador;

    public Heladeria (Mostrador mostrador, Caja caja){
        this.mostrador = mostrador;
        this.caja = caja;
        this.barquillos = 10;
        this.servilletas = 10;
        this.sabores  = new Hashtable<String, Integer>();
    }
    public void abrirHeladeria(){
        iniciarSabores();
        System.out.println("SE ABRE LA HELADERIA");
    }
    public Hashtable<String, Integer> getNumSabores(){
        return sabores;
    }

    private void iniciarSabores(){
        sabores.put("Chocolate", 10);
        sabores.put("Vainilla", 10);
        sabores.put("Fresa", 10);
    }
    
    public void restarHelado(String helado){
        
        sabores.replace(helado, sabores.get(helado), sabores.get(helado) - 1);
        barquillos -=1;
        servilletas -=1;
    }
    
    public void llenarSabor(String helado){
        
        sabores.replace(helado, 0, 10);
    }
    
    public boolean verificarSabor(String helado){
        if(sabores.get(helado) == 0)
            return false;
        else
            return true;
    }
    
    public boolean verificarServilletas(){
        
         if(servilletas == 0)
           return false;
         else{
             servilletas--;
             return true;
         }
    }
    
    public boolean verificarBarquillos(){
        
        if(barquillos == 0)
           return false;
         else{
             barquillos--;
             return true;
         }
    }
    
    public void llenarServilletas(){
        
        this.servilletas = 10;
    }
    
    public void llenarBarquillos(){
        
        this.barquillos = 10;
    }
    
    public String [] getSabores(){
        String [] sab = {"Chocolate", "Vainilla", "Fresa"};
        return sab;
    }
    public static void main(String[] args) {
        Mostrador mostrador = new Mostrador();
        Caja caja  = new Caja();
        Heladeria heladeria  = new Heladeria(mostrador,caja);
        mostrador.setHeladeria(heladeria);
        caja.setHeladeria(heladeria);
        heladeria.abrirHeladeria();

        Queue<Grupo> colaGrupo;
        colaGrupo = new LinkedList();
        colaGrupo.add(new Grupo(3, 0, caja, mostrador));
        // colaGrupo.add(new Grupo(3, 2, caja, mostrador));
        // colaGrupo.add(new Grupo(3, 3, caja, mostrador));

        for(int i = 0; i < colaGrupo.size(); i++){
            colaGrupo.poll().ejecutarPersonas();;
        }
    }
}

