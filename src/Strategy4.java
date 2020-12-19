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
        List<Vertex> redVertex = Utility.getRedVertex(graph);
        // On itère pour chaque sommet rouges de la liste des sommets rouge du graph
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("itération sur toutes les vertexs rouge");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        for (Vertex currentRed : redVertex){
            // On calcul le nombre d'arc bleu sortant vers un sommet rouge
            // du sommet rouge (currentRed) que l'on traite actuellement
            currentRed.nbArcBleuSortantVersRouge();
            // Si le sommet rouge (currentRed) que l'on traite actuellement
            // n'a aucun arc bleu sortant vers un sommet rouge...
            System.out.println("Vertex que l'on traite actuellement : "+currentRed.getId());
            System.out.println("nbArcBleuSortantVersRouge : "+currentRed.nbArcBleuSortantVersRouge);
            System.out.println("######################################################################");
            if(currentRed.nbArcBleuSortantVersRouge == 0) {
                // Alors on supprime ce sommet rouge (currentRed)
                return currentRed;
            }
            // Et l'on recommence
        }
        return null;
    }


    /**
     * @author Anas & Lucas
     * Created : 18/12/2020
     * @param graph
     * Pré-condition : un graph bi vertex (i.e chaque sommet du a au plus 2 arrêtes et au moins 1 arrête)
     * Complexité : polynomiale de l'ordre O(n^3)
     * */
    public List<Vertex> question3Algorithme(Graph graph) {
        List<Vertex> deletions = new ArrayList<>();
        List<Vertex> redVertex = Utility.getRedVertex(graph);
        while (atLeasOnLinkedVertex(redVertex)) { // s'il y a au moins un sommet rouge avec des arc entant/sortant, on continue
            boolean atLeastOneCaseStillValid = false;
            for (Vertex v : redVertex) { // supprimer tous les rouges avec 0 sortants
                if (v.nbArcSortant() == 0){
                    atLeastOneCaseStillValid = true;
                    graph.deleteVertex(v.getId());
                    deletions.add(v);
                }
            }
            for (Vertex v : redVertex) { // supprimer tous les rouge avec 1 arc sortant bleu vers sommet bleu (il a uniquement un arc sortant)
                if (v.nbArcSortant() == 1 && v.nbArcBleuSortantVersBleu == 1) { // conditions nécessaires suffisantes
                    atLeastOneCaseStillValid = true;
                    graph.deleteVertex(v.getId());
                    deletions.add(v);
                }
            }
            for (Vertex v : redVertex) {
                if (v.nbArcRougeSortant > 0) { // supprimer 1 seul rouge avec 1 ou 2 arcs rouge partant vers n'importe quelle vertex
                    atLeastOneCaseStillValid = true;
                    graph.deleteVertex(v.getId());
                    deletions.add(v);
                    break;
                }
            }
            if(!atLeastOneCaseStillValid) break; // nécessaire, sinon boucle infinie !
        }
        //deleteAllRedVertices(graph); // peut-être pas nécessaire à cause la ligne 21 :-/
        return deletions;
    }

    private boolean atLeasOnLinkedVertex(List<Vertex> redVertex) {
        for (Vertex v : redVertex) {
            if (v.getNeighbors().size() > 0) return true;
        }
        return false;
    }


}
