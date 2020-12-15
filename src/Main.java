import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class Main {

    public static String fileName = null;
    public static Strategy strategy = null;
    public static double pCEdge = 0.5;
    public static double pCVertex = 0.5;
    public static double pEdge = 0.1;
    public static int nbVertices = 10;


    public static void main(String[] args) throws IOException {
        Graph graph;
        if(args.length > 0) {
            fileName = args[0];
            /*if(args.length > 1) {
                try {
                    strategy = selectStrategy(Integer.parseInt(args[1]));
                }catch (Exception e){
                    System.out.println("Mauvais numéro de stratégie");
                    return;
                }
            }*/
        }
        //if(strategy == null)
        strategy = new Strategy0();
        graph = initGraph(fileName);
        if(graph == null){
            System.out.println("Attention il y a eu une erreur");
            return;
        }
        //System.out.println(graph);
        //System.out.println("Nombre de sommets restants en moyenne : "+ Tests.runTests(100,strategy));
        run(graph, strategy);
        //deleteAuto(graph);

        /*int[] deletions = {2,7,4,1,5,6,0};
        deletions(deletions,graph);
        System.out.println("Il reste "+graph.getVertices().size() + " sommets");*/
    }


    public static Graph createRandomCompleteGraph(double pcVertex, double pcEdge, int nbVertices){
        return createRandomGraph(pcVertex,pcEdge,1,nbVertices,-1);
    }


    public static Graph createBiArete(double pCVertex, double pCEdge, int nbVertices){
        List<Vertex> vertices = new ArrayList<>();
        Edge[][] edges = new Edge[nbVertices][nbVertices];
        for(int i = 0;i<nbVertices;i++){
            Arrays.fill(edges[i],new Edge(ColorG.COLOR.NONE));
        }
        for(int i =0;i<nbVertices;i++){
            ColorG.COLOR color = (Math.random()<=pCVertex)? ColorG.COLOR.RED: ColorG.COLOR.BLUE;
            vertices.add(new Vertex(color,i));
        }
        for(int i=0;i< nbVertices-1;i++){
            if(Math.random()<0.5){
                vertices.get(i).addNeighbor(vertices.get(i+1));
                ColorG.COLOR color = (Math.random()<=pCEdge)? ColorG.COLOR.BLUE: ColorG.COLOR.RED;
                edges[vertices.get(i).getId()][vertices.get(i+1).getId()] = new Edge(color);
            }
        }
        for(int i=nbVertices-1;i>0;i--){
            if(edges[vertices.get(i-1).getId()][vertices.get(i).getId()].getColor() == ColorG.COLOR.NONE){
                vertices.get(i).addNeighbor(vertices.get(i-1));
                ColorG.COLOR color = (Math.random()<=pCEdge)? ColorG.COLOR.BLUE: ColorG.COLOR.RED;
                edges[vertices.get(i).getId()][vertices.get(i-1).getId()] = new Edge(color);
            }
        }
        return new Graph(vertices,edges);
    }
    public static Graph createRandomGraph(double pCVertex, double pCEdge, double pEdge,int nbVertices, int nbVerticesMax){
        List<Vertex> vertices = new ArrayList<>();
        if(nbVertices ==-1){
            nbVertices = (new Random().nextInt(nbVerticesMax));
        }
        Edge[][] edges = new Edge[nbVertices][nbVertices];
        for(int i = 0;i<nbVertices;i++){
            Arrays.fill(edges[i],new Edge(ColorG.COLOR.NONE));
        }
        for(int i =0;i<nbVertices;i++){
            ColorG.COLOR color = (Math.random()<=pCVertex)? ColorG.COLOR.RED: ColorG.COLOR.BLUE;
            vertices.add(new Vertex(color,i));
        }
        for(Vertex vertex : vertices){
            for(Vertex vertex1 : vertices){
                if(vertex!=vertex1 && Math.random()<=pEdge){
                    vertex.addNeighbor(vertex1);
                    ColorG.COLOR color = (Math.random()<=pCEdge)? ColorG.COLOR.BLUE: ColorG.COLOR.RED;
                    edges[vertex.getId()][vertex1.getId()] = new Edge(color);
                }
            }
        }
        return new Graph(vertices,edges);
    }


    /**************************** NE PAS TOUCHER APRES *****************/

    static Graph initGraph(String fileName) throws IOException {
        Scanner sc;
        if(fileName != null && !fileName.isEmpty()) {
            String s = new String(Files.readAllBytes(Paths.get("./" + fileName)));
            InputStream stream = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
            sc = new Scanner(stream);
        }
        else {
            sc = new Scanner(System.in);
        }
        int nbVertices = sc.nextInt();
        List<Vertex> vertices = new ArrayList<>();
        Edge[][] edges = new Edge[nbVertices][nbVertices];
        for(int i = 0;i<nbVertices;i++){
            Arrays.fill(edges[i],new Edge(ColorG.COLOR.NONE));
        }
        for(int i =0;i<nbVertices;i++){
            String res = sc.next();
            if(!res.equals(ColorG.COLOR.RED.toString())&&!res.equals(ColorG.COLOR.BLUE.toString())){
                System.out.println("Error was expecting BLUE or RED and got : "+ res);
                return null;
            }
            ColorG.COLOR color = ColorG.COLOR.valueOf(res);
            vertices.add(new Vertex(color,i));
        }
        sc.nextLine();
        for(Vertex vertex : vertices){
            ColorG.COLOR[] colors = new ColorG.COLOR[nbVertices];
            int[] neig = new int[nbVertices];
            Arrays.fill(neig, -1);
            String res = sc.nextLine();
            String[] neighbor = res.split(", ");
            if(neighbor[0].equals("none"))continue;
            try {
                for(int i = 0;i<neighbor.length;i++) {
                    colors[i] = ColorG.COLOR.valueOf(neighbor[i].split(" ")[0]);
                    neig[i] = Integer.parseInt(neighbor[i].split(" ")[1]);
                }
                for(int i =0;i<neig.length;i++){
                    if(neig[i]>=0) {
                        vertex.addNeighbor(vertices.get(neig[i]));
                        edges[vertex.getId()][neig[i]] = new Edge(colors[i]);
                        TrafficNeighbor trafficNeighbor =  new TrafficNeighbor(vertices.get(neig[i]), edges[vertex.getId()][neig[i]]);
                        vertex.trafficNeighbors.add( trafficNeighbor);
                    }
                }
            }
            catch (Exception e){
                System.out.println(res);
                e.printStackTrace();
                return null;
            }

        }
        return new Graph(vertices,edges);
    }


    public static void run(Graph g, Strategy s){
        EventQueue.invokeLater(() -> {
            JFrame f = new JFrame("GraphPanel");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            GraphPanel gp = new GraphPanel();
            gp.init(f, g, s);
        });
    }

    public static Strategy[] getAllStrats() {
        Strategy[] l = new Strategy[100];
        for(int i =0;i<100;i++){
            l[i] = null;
        }
        for(int i =0;i<100;i++){
            try {
                Strategy s=  (Strategy) Class.forName("Strategy"+i).newInstance();
                l[i] = s;
            }
            catch (Exception e){}
        }
        return l;
    }


}
