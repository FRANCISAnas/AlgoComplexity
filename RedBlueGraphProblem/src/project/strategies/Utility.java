package project.strategies;

import project.core.ColorBR;
import project.core.Vertex;
import project.graph.Graph;

import java.util.ArrayList;
import java.util.List;

public class Utility {

    public static Vertex getFirstRedVertex(Graph graph){
        for (Vertex v : graph.getVertices()){
            if(v.getColor() ==  ColorBR.RED){
                v.nbArcBleuSortantVersRouge();
                return v;
            }
        }
        return null;
    }

    public static List<Vertex> getRedVertex(Graph graph){
        List<Vertex> redVertex = new ArrayList<>();
        for (Vertex v : graph.getVertices()){
            if(v.getColor() ==  ColorBR.RED){
                redVertex.add(v);
            }
        }
        return redVertex;
    }
}
