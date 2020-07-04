import static java.lang.Math.random;

class Ambiente {

    Ambiente(
            int numeroNodos,
            int probabilidadConexion
    ) {
        crearGrafo(numeroNodos, probabilidadConexion);
    }
    private Agente[] nodos;

    void crearGrafo(int n, double p) {
        nodos = new Agente[n];
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (random() < p) {
                    nodos[i].nuevoAmigo(nodos[j]);
                    nodos[j].nuevoAmigo(nodos[i]);
                }
            }
        }
    }



}