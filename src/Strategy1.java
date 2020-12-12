import java.util.ArrayList;
import java.util.List;

public class Strategy1 implements Strategy {

    @Override
    public void init(Graph graph) {}

    @Override
    public Vertex getNextVertexToDelete(Graph graph) {
        List<Vertex> sommets = graph.getVertices();
        Vertex sommetTODelete = Utility.getFirstRedVertex(graph);
        if(sommetTODelete==null)return null;
        List<Vertex> equals = new ArrayList<>();
        for (Vertex cuSomm : sommets) {
            if (cuSomm.getColor() == ColorG.COLOR.RED) {
                cuSomm.nbArcBleuSortantVersRouge();
                if (cuSomm.nbArcBleuSortantVersRouge < sommetTODelete.nbArcBleuSortantVersRouge) {
                    sommetTODelete = cuSomm;
                    equals.clear();
                } else if (cuSomm.nbArcBleuSortantVersRouge == sommetTODelete.nbArcBleuSortantVersRouge) {
                    equals.add(cuSomm);
                    if(!equals.contains(sommetTODelete))equals.add(sommetTODelete);
                }
            }
        }
        if(equals.isEmpty())return sommetTODelete;
        sommetTODelete = equals.get(0);
        sommetTODelete.nbArcBleuSortantVersBleu();
        for(Vertex vertex : equals){
            vertex.nbArcBleuSortantVersBleu();
            if(vertex.nbArcBleuSortantVersBleu>sommetTODelete.nbArcBleuSortantVersBleu){
                sommetTODelete = vertex;
            }
        }
        return sommetTODelete;
    }
    
}
