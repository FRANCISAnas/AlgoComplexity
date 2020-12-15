import java.util.List;

public class Strategy4 implements Strategy {

    @Override
    public void init(Graph graph) {

    }

    @Override
    public Vertex getNextVertexToDelete(Graph graph) {

        return null;
    }



    public void question3Algorithme(Graph graph) {
        List<Vertex> redVertex = Utility.getRedVertex(graph);
        while (atLeasOnLinkedVertex(redVertex)) { // s'il y a au moins un sommet rouge avec des arc entant/sortant, on continue
            for (Vertex v : redVertex) { // supprimer tous les rouges avec 0 sortants
                if (v.nbArcSortant() == 0){
                    graph.deleteVertex(v.getId());
                }
            }
            for (Vertex v : redVertex) { // supprimer tous les rouge avec 1 arc sortant bleu vers sommet bleu (il a uniquement un arc sortant)
                if (v.nbArcSortant() == 1 && v.nbArcBleuSortantVersBleu == 1) { // conditions nécessaires suffisantes
                    graph.deleteVertex(v.getId());
                }
            }
            for (Vertex v : redVertex) {
                if (v.nbArcRougeSortant > 0) { // supprimer 1 seul rouge avec 1 ou 2 arcs rouge partant vers n'importe quelle vertex
                    graph.deleteVertex(v.getId());
                    break;
                }
            }
        }
        deleteAllRedVertices(graph); // peut-être pas nécessaire à cause la ligne 21 :-/
    }




    private boolean atLeasOnLinkedVertex(List<Vertex> redVertex) {
        for (Vertex v : redVertex) {
            if (v.getNeighbors().size() > 0) return true;
        }
        return false;
    }


}
