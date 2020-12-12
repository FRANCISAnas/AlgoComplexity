import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Vertex {


    private ColorG.COLOR color;
    private List<Vertex> neighbors;
    private int id;
    public HashMap<Vertex, Edge>  trafficNeighbors;
    public int nbArcBleuSortantVersRouge;
    public int nbArcBleuSortantVersBleu;
    public int nbArcRougeSortantVersBleu;
    public int ratio;
    public int nbArcRougeSortant;

    public Vertex(ColorG.COLOR color, List<Vertex>neighbors, int id ){
        this.color = color;
        this.neighbors = neighbors;
        this.id = id;

    }

    public Vertex(ColorG.COLOR color, int id ){
        this.color = color;
        this.id = id;
        this.neighbors = new ArrayList<>();
        this.trafficNeighbors = new HashMap<>();
    }

    public void calculateRatio(){
        this.nbArcBleuSortantVersRouge();
        this.nbArcRougeSortantVersBleu();
        ratio = this.nbArcRougeSortantVersBleu - this.nbArcBleuSortantVersRouge;
    }

    public void nbArcBleuSortantVersRouge(){
        nbArcBleuSortantVersRouge = 0;
        for(Vertex vertex : trafficNeighbors.keySet()){
            if(trafficNeighbors.get(vertex).getColor() == ColorG.COLOR.BLUE){
                if(vertex.getColor() == ColorG.COLOR.RED){
                    nbArcBleuSortantVersRouge++;
                }
            }
        }
    }

    public void nbArcRougeSortantVersBleu(){
        nbArcRougeSortantVersBleu = 0;
        for(Vertex vertex : trafficNeighbors.keySet()){
            if(trafficNeighbors.get(vertex).getColor() == ColorG.COLOR.RED){
                if(vertex.getColor() == ColorG.COLOR.BLUE){
                    nbArcRougeSortantVersBleu++;
                }
            }
        }
    }

    public void nbArcBleuSortantVersBleu(){
        nbArcBleuSortantVersBleu = 0;
        for(Vertex vertex : trafficNeighbors.keySet()){
            if(trafficNeighbors.get(vertex).getColor() == ColorG.COLOR.BLUE){
                if(vertex.getColor() == ColorG.COLOR.BLUE){
                    nbArcBleuSortantVersBleu++;
                }
            }
        }
    }

    public void nbArcRougeSortant(){
        nbArcRougeSortant = 0;
        for(Vertex vertex : trafficNeighbors.keySet()){
            if(trafficNeighbors.get(vertex).getColor() == ColorG.COLOR.RED){
                nbArcRougeSortant++;
            }
        }
    }

    public void setNeighbors(List<Vertex> neighbors) {
        this.neighbors = neighbors;
    }

    public void addNeighbor(Vertex vertex){
        this.neighbors.add(vertex);
    }

    public void setColor(ColorG.COLOR color) {
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public ColorG.COLOR getColor() {
        return color;
    }

    public List<Vertex> getNeighbors() {
        return neighbors;
    }

    @Override
    public String toString() {
        return id+color.toString();
    }
}
