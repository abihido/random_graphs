import java.util.ArrayList;
import java.util.List;

class Agente {

    Agente() {
        amigos = new ArrayList<Agente>();
        antivirus = 2;
    }
    private List<Agente> amigos;
    private int antivirus;
    public void nuevoAmigo(Agente conocido) {
        amigos.add(conocido);
    }

    int numeroAmigos() {
        return amigos.size();
    }
}