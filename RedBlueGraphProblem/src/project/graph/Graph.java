package project.graph;

import project.core.ColorBR;
import project.core.Edge;
import project.core.TrafficNeighbor;
import project.core.Vertex;

import java.util.List;

public class Graph {

    private final List<Vertex> vertices;
    private final Edge[][] edges;

    public Graph(List<Vertex> vertices, Edge[][] edges){
        this.vertices = vertices;
        this.edges = edges;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public Edge[][] getEdges() {
        return edges;
    }

    public void deleteVertex(int nb){
        Vertex vertex = getVertexByNb(nb, vertices);
        if(vertex==null){
            System.out.println("Ce sommet n'existe pas");
            return;
        }
        ColorBR color = vertex.getColor();
        if(color.equals(ColorBR.BLUE)){
            System.out.println("Impossible de supprimer un sommet bleu");
            return;
        }
        vertices.remove(vertex);
        for(TrafficNeighbor t : vertex.getTrafficNeighbors()){
            ColorBR color1 = edges[vertex.getId()][t.getVertex().getId()].getColor();
            if(color1.equals(ColorBR.BLUE)){
                t.getVertex().setColor(ColorBR.BLUE);
            }
            else if(color1.equals(ColorBR.RED)){
                t.getVertex().setColor(ColorBR.RED);
            }
        }

        for(Vertex vertex1 : vertices) {
            vertex1.removeTrafficNeighborByVertex(vertex);
        }
    }


    public Vertex getVertexByNb(int i, List<Vertex> vertices){
        for(Vertex vertex :vertices){
            if(vertex.getId() == i){
                return vertex;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(Vertex vertex : vertices){
            stringBuilder.append("Sommet : ").append(vertex);
            stringBuilder.append(", Voisins :[");
            for(TrafficNeighbor t: vertex.getTrafficNeighbors()) {
                ColorBR color = edges[vertex.getId()][t.getVertex().getId()].getColor();
                if(color.equals(ColorBR.BLUE)){
                    stringBuilder.append("-B> ");
                }
                else if(color.equals(ColorBR.RED)) {
                    stringBuilder.append("-R> ");
                }
                stringBuilder.append(t.getVertex());
                stringBuilder.append(", ");
            }
            stringBuilder.append("]\n");
        }
        return stringBuilder.toString();
    }
}
