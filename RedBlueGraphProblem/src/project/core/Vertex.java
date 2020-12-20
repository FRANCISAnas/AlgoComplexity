package project.core;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Vertex {


    private ColorBR color;
    private List<Vertex> neighbors;
    private int id;
    public HashMap<Vertex, Edge> trafficNeighbors;
    public int nbArcBleuSortantVersRouge;
    public int nbArcBleuSortantVersBleu;
    public int nbArcRougeSortantVersBleu;
    public int nbArcRougeSortantVersRouge;
    //public int ratio;
    public int nbArcRougeSortant;

    public Vertex(ColorBR color, List<Vertex> neighbors, int id) {
        this.color = color;
        this.neighbors = neighbors;
        this.id = id;

    }

    public Vertex(ColorBR color, int id) {
        this.color = color;
        this.id = id;
        this.neighbors = new ArrayList<>();
        this.trafficNeighbors = new HashMap<>();
    }

    public int calculateScore() {
        this.nbArcBleuSortantVersRouge();
        this.nbArcRougeSortantVersBleu();
        this.nbArcBleuSortantVersBleu();
        this.nbArcRougeSortantVersRouge();
        return ((this.nbArcRougeSortantVersBleu * neighbors.size()/2) + this.nbArcBleuSortantVersBleu)
                - ((this.nbArcBleuSortantVersRouge * neighbors.size()/2) + this.nbArcRougeSortantVersRouge);
    }


    public void nbArcBleuSortantVersRouge() {
        nbArcBleuSortantVersRouge = 0;
        for (Vertex vertex : trafficNeighbors.keySet()) {
            if (trafficNeighbors.get(vertex).getColor() == ColorBR.BLUE) {
                if (vertex.getColor() == ColorBR.RED) {
                    nbArcBleuSortantVersRouge++;
                }
            }
        }
    }

    public void nbArcRougeSortantVersBleu() {
        nbArcRougeSortantVersBleu = 0;
        for(Vertex vertex : trafficNeighbors.keySet()){
            if(trafficNeighbors.get(vertex).getColor() == ColorBR.RED){
                if(vertex.getColor() == ColorBR.BLUE){
                    nbArcRougeSortantVersBleu++;
                }
            }

        }
    }

    public void nbArcRougeSortantVersRouge() {
        nbArcRougeSortantVersRouge = 0;
        for(Vertex vertex : trafficNeighbors.keySet()){
            if(trafficNeighbors.get(vertex).getColor() == ColorBR.RED){
                if(vertex.getColor() == ColorBR.RED){
                    nbArcRougeSortantVersRouge++;
                }
            }
        }
    }

    public void nbArcBleuSortantVersBleu() {
        nbArcBleuSortantVersBleu = 0;
        for(Vertex vertex : trafficNeighbors.keySet()){
            if(trafficNeighbors.get(vertex).getColor() == ColorBR.BLUE){
                if(vertex.getColor() == ColorBR.BLUE){
                    nbArcBleuSortantVersBleu++;
                }
            }

        }
    }

    public void addNeighbor(Vertex vertex) {
        this.neighbors.add(vertex);
    }

    public void setColor(ColorBR color) {
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public ColorBR getColor() {
        return color;
    }

    public List<Vertex> getNeighbors() {
        return neighbors;
    }

    @Override
    public String toString() {
        return id + color.toString();
    }

}
