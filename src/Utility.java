import java.util.ArrayList;
import java.util.List;

public class Utility {

    public static Vertex getFirstRedVertex(Graph graph){
        for (Vertex cuSomm : graph.getVertices()){
            if(cuSomm.getColor() ==  ColorG.COLOR.RED){
                cuSomm.nbArcBleuSortantVersRouge();
                return cuSomm;
            }
        }
        return null;
    }

    public static List<Vertex> getVertex(List<TrafficNeighbor> trafficNeighbors){
        List<Vertex> toRet = new ArrayList<>();
        for(TrafficNeighbor t: trafficNeighbors){
            toRet.add(t.getVertex());
        }
        return toRet;
    }



    public static List<Vertex> getRedVertex(Graph graph){
        List<Vertex> redVertex = new ArrayList<>();
        for (Vertex cuSomm : graph.getVertices()){
            if(cuSomm.getColor() ==  ColorG.COLOR.RED){
                redVertex.add(cuSomm);
            }
        }
        return redVertex;
    }
}
