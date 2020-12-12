import java.util.ArrayList;
import java.util.List;

public class Graph {

    private List<Vertex> vertices;
    private Edge[][] edges;

    Graph(List<Vertex> vertices, Edge[][] edges){
        this.vertices = vertices;
        this.edges = edges;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public Edge[][] getEdges() {
        return edges;
    }

    boolean deleteVertex(int nb){
        Vertex vertex = getVertexByNb(nb, vertices);
        if(vertex==null){
            System.out.println("Ce sommet n'existe pas");
            return false;
        }
        ColorG.COLOR color = vertex.getColor();
        if(color.equals(ColorG.COLOR.BLUE)){
            System.out.println("Impossible de supprimer un sommet bleu");
            return false;
        }
        vertices.remove(vertex);
        for(Vertex vertex1 : vertex.getNeighbors()){
            ColorG.COLOR color1 = edges[vertex.getId()][vertex1.getId()].getColor();
            if(color1.equals(ColorG.COLOR.BLUE)){
                vertex1.setColor(ColorG.COLOR.BLUE);
            }
            else if(color1.equals(ColorG.COLOR.RED)){
                vertex1.setColor(ColorG.COLOR.RED);
            }
        }
        for(Vertex vertex1 : vertices){
            vertex1.getNeighbors().remove(vertex);
            vertex1.trafficNeighbors.remove(vertex);
        }
        return true;
    }


    public Vertex getVertexByNb(int i, List<Vertex> vertices){
        for(Vertex vertex :vertices){
            if(vertex.getId() == i){
                return vertex;
            }
        }
        return null;
    }

    public Graph getCopy(){
        List<Vertex> vertices = new ArrayList<>();
        Edge[][] edges = new Edge[this.vertices.size()][this.vertices.size()];
        for(Vertex vertex : this.vertices){
            vertices.add(new Vertex(vertex.getColor(),vertex.getId()));
        }
        for(int i = 0;i<this.vertices.size();i++) {
            for (int j = 0; j < this.vertices.size(); j++) {
                edges[i][j] = new Edge(this.edges[i][j].getColor());
                if(edges[i][j].getColor()!= ColorG.COLOR.NONE){
                    getVertexByNb(i,vertices).addNeighbor(getVertexByNb(j,vertices));
                }
            }
        }
        return new Graph(vertices,edges);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(Vertex vertex : vertices){
            stringBuilder.append("Sommet : ").append(vertex);
            stringBuilder.append(", Voisins :[");
            for(Vertex vertex1 : vertex.getNeighbors()){
                ColorG.COLOR color = edges[vertex.getId()][vertex1.getId()].getColor();
                if(color.equals(ColorG.COLOR.BLUE)){
                    stringBuilder.append("-B> ");
                }
                else if(color.equals(ColorG.COLOR.RED)) {
                    stringBuilder.append("-R> ");
                }
                stringBuilder.append(vertex1);
                stringBuilder.append(", ");
            }
            stringBuilder.append("]\n");
        }
        return stringBuilder.toString();
    }
}
