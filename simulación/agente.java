import java.util.List;

class Agente {

    private List<Agente> amigos;
    void nuevoAmigo(Agente conocido) {
        amigos.add(conocido);
    }
    int numeroAmigos() {
        return amigos.size();
    }
}