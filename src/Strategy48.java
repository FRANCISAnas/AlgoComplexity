import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Strategy48 implements Strategy {
/** Strategie choisit un sommet rouge aléatoire**/

    @Override
    public void init(Graph graph) {

    }

    @Override
    public Vertex getNextVertexToDelete(Graph graph) {
            List<Vertex> red = Utility.getRedVertex(graph);
            if(red.isEmpty()) return null;
            return red.get(ThreadLocalRandom.current().nextInt(0, red.size()));
    }
}
