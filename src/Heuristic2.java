import java.util.List;

public class Heuristic2 implements Strategy {

    @Override
    public Vertex getNextVertexToDelete(Graph graph) {
        List<Vertex> redVertex = Utility.getRedVertex(graph);
        Vertex sommetTODelete = Utility.getFirstRedVertex(graph);
        if(sommetTODelete==null)return null;
        for (Vertex cuSomm : redVertex) {
            cuSomm.calculateRatioV2();
            sommetTODelete.calculateRatioV2();
            if(sommetTODelete.ratio < cuSomm.ratio){
                sommetTODelete = cuSomm;
            }
        }
        return sommetTODelete;
    }
}
