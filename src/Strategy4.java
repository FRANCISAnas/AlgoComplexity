import java.util.ArrayList;
import java.util.List;

public class Strategy4 implements Strategy {

    private List<Vertex> deletions = null;
    private Graph g = null;
    @Override
    public void init(Graph graph) {
    }

    @Override
    public Vertex getNextVertexToDelete(Graph graph) {
        return null;
        /*if(deletions==null){
            g = graph.getCopy();
            deletions = question3Algorithme(g);
        }
        if(deletions.isEmpty())return null;
        return deletions.remove(0);*/
    }


    /**
     * @author Anas & Lucas
     * Created : 18/12/2020
     * @param graph
     * Pré-condition : un graph bi vertex (i.e chaque sommet du graph a au plus 2 arrêtes et au moins 1 arrête)
     * Complexité : polynomiale de l'ordre O(n^3)
     * @param initAction  */
    public static void question3Algorithme(Graph graph, GraphPanel.InitAction initAction) {
        List<Vertex> deletions = new ArrayList<>();
        List<Vertex> redVertex = Utility.getRedVertex(graph);
        while (atLeasOnLinkedVertex(redVertex)) { // s'il y a au moins un sommet rouge avec des arc entant/sortant, on continue
            boolean atLeastOneCaseStillValid = false;
            for (Vertex v : redVertex) { // supprimer tous les rouges avec 0 sortants
                if(deletions.contains(v))break;
                if (v.nbArcSortant() == 0){
                    System.out.println("v.toString() = "+v.toString());
                    atLeastOneCaseStillValid = true;
                    if(!graph.deleteVertex(v.getId())){
                        System.out.println("couldn't perform deletion in first case !");
                        return;
                    }
                    initAction.updateGraph(graph);
                    deletions.add(v);
                }
            }
            redVertex.removeIf(deletions::contains);
            deletions = new ArrayList<>();

            for (Vertex v : redVertex) { // supprimer tous les rouge avec 1 arc sortant bleu vers sommet bleu (il a uniquement un arc sortant)
                if(deletions.contains(v))break;
                if (v.nbArcSortant() == 1 && v.nbArcBleuSortantVersBleu == 1) { // conditions nécessaires suffisantes
                    System.out.println("v.toString() = "+v.toString());
                    atLeastOneCaseStillValid = true;
                    System.out.println(v.toString());
                    if(!graph.deleteVertex(v.getId())) {
                        System.out.println("couldn't perform deletion in 2nd case !");
                        return;
                    }
                    initAction.updateGraph(graph);
                    deletions.add(v);
                }
            }
            redVertex.removeIf(deletions::contains);
            deletions = new ArrayList<>();
            for (Vertex v : redVertex) {
                if (v.nbArcRougeSortant > 0) { // supprimer 1 seul rouge avec 1 ou 2 arcs rouge partant vers n'importe quelle vertex
                    System.out.println("v.toString() = "+v.toString());
                    atLeastOneCaseStillValid = true;
                    if(!graph.deleteVertex(v.getId())){
                        System.out.println("couldn't perform deletion in 3rd case !");
                        return;
                    }
                    initAction.updateGraph(graph);
                    deletions.add(v);
                    break;
                }
            }
            redVertex.removeIf(deletions::contains);
            deletions = new ArrayList<>();
            if(!atLeastOneCaseStillValid) break; // nécessaire, sinon boucle infinie !
        }
        Strategy.deleteAllRedVertices(graph); // peut-être pas nécessaire à cause la ligne 21 :-/
        //return deletions;
    }

    private static boolean atLeasOnLinkedVertex(List<Vertex> redVertex) {
        for (Vertex v : redVertex) {
            if (v.getNeighbors().size() > 0) return true;
        }
        return false;
    }


}
