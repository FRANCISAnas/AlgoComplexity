import java.util.List;

public class Strategy0 implements Strategy {

    @Override
    public void init(Graph graph) {}

    @Override
    public Vertex getNextVertexToDelete(Graph graph) {
        List<Vertex> sommets = graph.getVertices();
        Vertex sommetTODelete = Utility.getFirstRedVertex(graph);
        if(sommetTODelete==null)return null;
        for (Vertex cuSomm : sommets) {
            if (cuSomm.getColor() == ColorG.COLOR.RED) {
                cuSomm.nbArcBleuSortantVersRouge();
                if (cuSomm.nbArcBleuSortantVersRouge < sommetTODelete.nbArcBleuSortantVersRouge) {
                    sommetTODelete = cuSomm;
                }
            }
        }
        return sommetTODelete;
    }
    
}
