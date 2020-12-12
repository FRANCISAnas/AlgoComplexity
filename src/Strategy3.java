import javax.crypto.spec.PSource;
import java.util.List;

public class Strategy3 implements Strategy {
    private Graph copy;

    @Override
    public void init(Graph graph) {
        this.copy = graph.getCopy();
    }

    @Override
    public Vertex getNextVertexToDelete(Graph graph) {
        List<Vertex> redVertex = Utility.getRedVertex(graph);
        Vertex sommetTODelete = Utility.getFirstRedVertex(graph);
        if(sommetTODelete==null)return null;
        sommetTODelete.calculateRatioV2();
        for (Vertex cuSomm : redVertex) {
            cuSomm.calculateRatioV2();
            if(sommetTODelete.ratio < cuSomm.ratio){
                sommetTODelete = cuSomm;
            }
        }
        return sommetTODelete;
    }
}
