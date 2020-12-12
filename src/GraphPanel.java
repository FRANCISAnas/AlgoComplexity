import java.awt.*;
import java.awt.Color;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.*;

/**
 * @author John B. Matthews; distribution per GPL.
 * @author lucas Dominguez; reprise du code et adaptation au sujet
 */
public class GraphPanel extends JComponent {

    private static final int WIDE = 900;
    private static final int HIGH = 700;
    private static final int RADIUS = 35;
    public static int delayAnimation = 2000;
    private static final Random rnd = new Random();
    private ControlPanel control = new ControlPanel();
    private int radius = RADIUS;
    private Kind kind = Kind.Circular;
    private List<NodeDraw> nodeDraws = new ArrayList<NodeDraw>();
    private List<NodeDraw> positionSave = new ArrayList<>();
    private List<NodeDraw> selected = new ArrayList<NodeDraw>();
    private List<EdgeDraw> edgeDraws = new ArrayList<EdgeDraw>();
    private Point mousePt = new Point(WIDE / 2, HIGH / 2);
    private Rectangle mouseRect = new Rectangle();
    private boolean selecting = false;
    private static Graph graph;
    private static Strategy strategy;
    private boolean random = false;
    private static int nbtests=100;


    public GraphPanel() {
        this.setOpaque(true);
        this.addMouseListener(new MouseHandler());
        this.addMouseMotionListener(new MouseMotionHandler());
    }

    public void init(JFrame f, Graph g, Strategy s){
        f.add(control, BorderLayout.NORTH);
        f.add(new JScrollPane(this), BorderLayout.CENTER);
        f.getRootPane().setDefaultButton(control.defaultButton);
        f.pack();
        f.setLocationByPlatform(true);
        graph = g;
        strategy = s;
        f.setVisible(true);
    }
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDE, HIGH);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        for (EdgeDraw e : edgeDraws) {
            e.draw(g);
        }
        for (NodeDraw n : nodeDraws) {
            n.draw(g);
        }
        if (selecting) {
            g.setColor(Color.darkGray);
            g.drawRect(mouseRect.x, mouseRect.y,
                mouseRect.width, mouseRect.height);
        }
    }

    private class MouseHandler extends MouseAdapter {

        @Override
        public void mouseReleased(MouseEvent e) {
            selecting = false;
            mouseRect.setBounds(0, 0, 0, 0);
            if (e.isPopupTrigger()) {
                showPopup(e);
            }
            e.getComponent().repaint();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            mousePt = e.getPoint();
            if (e.isShiftDown()) {
                NodeDraw.selectToggle(nodeDraws, mousePt);
            } else if (e.isPopupTrigger()) {
                NodeDraw.selectOne(nodeDraws, mousePt);
                showPopup(e);
            } else if (NodeDraw.selectOne(nodeDraws, mousePt)) {
                selecting = false;
            } else {
                NodeDraw.selectNone(nodeDraws);
                selecting = true;
            }
            e.getComponent().repaint();
        }

        private void showPopup(MouseEvent e) {
            control.popup.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    private class MouseMotionHandler extends MouseMotionAdapter {

        Point delta = new Point();

        @Override
        public void mouseDragged(MouseEvent e) {
            if (selecting) {
                mouseRect.setBounds(
                    Math.min(mousePt.x, e.getX()),
                    Math.min(mousePt.y, e.getY()),
                    Math.abs(mousePt.x - e.getX()),
                    Math.abs(mousePt.y - e.getY()));
                NodeDraw.selectRect(nodeDraws, mouseRect);
            } else {
                delta.setLocation(
                    e.getX() - mousePt.x,
                    e.getY() - mousePt.y);
                NodeDraw.updatePosition(nodeDraws, delta);
                mousePt = e.getPoint();
            }
            e.getComponent().repaint();
        }
    }

    private class ControlPanel extends JToolBar {
        private Action newNode = new NewNodeAction("New" );
        private Action clearAll = new ClearAction("Clear",this);
        private Action init = new InitAction("Init prebuiltGraph", this);
        //private Action kind = new KindComboAction("Change the shape");
        private Action changeStrat = new ChangeStratAction("Change the strategy");
        //private Action color = new ColorAction("Color");
        private Action deleteAll = new DeleteAllAction("DeleteAll",this);
        private Action delete = new DeleteAction("Delete Red",this);
        private Action random = new RandomAction("Create a random Graph", this);
        private Action tests = new TestsAction("Run Tests", this);
        private JButton defaultButton = new JButton(newNode);
        //private JComboBox kindCombo = new JComboBox();
        private JComboBox stratCombo = new JComboBox();
        private ColorIcon hueIcon = new ColorIcon(Color.blue);
        private JPopupMenu popup = new JPopupMenu();
        private JLabel labelTest;


        ControlPanel() {
            this.setLayout(new FlowLayout(FlowLayout.LEFT));
            this.setBackground(Color.lightGray);

            //this.add(defaultButton);
            this.add(new JButton(init));
            this.add(new JButton(clearAll));
            //this.add(kindCombo);
            this.add(stratCombo);
            this.add(new JButton(deleteAll));
            this.add(new JLabel("Delay:"));
            JSpinner js5 = new JSpinner();
            js5.setModel(new SpinnerNumberModel(GraphPanel.delayAnimation, 0, 10000, 100));
            js5.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    JSpinner s = (JSpinner) e.getSource();
                    GraphPanel.delayAnimation = (Integer) s.getValue();
                    GraphPanel.this.repaint();
                }
            });
            this.add(js5);
            //this.add(new JLabel(hueIcon));
            JSpinner js = new JSpinner();
            js.setModel(new SpinnerNumberModel(Math.abs(Main.pEdge*100), 0, 100, 5));
            js.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    JSpinner s = (JSpinner) e.getSource();
                    Main.pEdge = (Double) s.getValue()/100.;
                    GraphPanel.this.repaint();
                }
            });
            this.add(new JLabel("Number of vertices : "));
            JSpinner js0 = new JSpinner();
            js0.setModel(new SpinnerNumberModel(Main.nbVertices, 0, 100, 5));
            js0.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    JSpinner s = (JSpinner) e.getSource();
                    Main.nbVertices = (Integer) s.getValue();
                    GraphPanel.this.repaint();
                }
            });
            this.add(js0);
            this.add(new JLabel("% of an edge :"));
            this.add(js);
            this.add(new JLabel("% vertex being red : "));
            JSpinner js2 = new JSpinner();
            js2.setModel(new SpinnerNumberModel(Math.abs(Main.pCVertex*100), 0, 100, 5));
            js2.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    JSpinner s = (JSpinner) e.getSource();
                    Main.pCVertex = (Double) s.getValue()/100.;
                    GraphPanel.this.repaint();
                }
            });
            this.add(js2);
            this.add(new JLabel("% edge being blue : "));
            JSpinner js3 = new JSpinner();
            js3.setModel(new SpinnerNumberModel(Math.abs(Main.pCEdge*100), 0, 100, 5));
            js3.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    JSpinner s = (JSpinner) e.getSource();
                    Main.pCEdge = (Double) s.getValue()/100.;
                    GraphPanel.this.repaint();
                }
            });
            this.add(js3);
            this.add(new JButton(random));
            //this.add(new JLabel("Nb de tests:"));
            JSpinner js6 = new JSpinner();
            js6.setModel(new SpinnerNumberModel(GraphPanel.nbtests, 32, 10000, GraphPanel.nbtests));
            js6.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    JSpinner s = (JSpinner) e.getSource();
                    GraphPanel.nbtests = (Integer) s.getValue();
                }
            });
            //this.add(js6);
            this.add(tests);
            labelTest = new JLabel("%Restant");
            //labelTest.setVisible(false);
            //this.add(labelTest);
            //popup.add(new JMenuItem(newNode));
            //popup.add(new JMenuItem(color));
            popup.add(new JMenuItem(delete));
            JMenu subMenu = new JMenu("Kind");
            /*for (Kind k : Kind.values()) {
                kindCombo.addItem(k);
                subMenu.add(new JMenuItem(new KindItemAction(k)));
            }*/
            Strategy[] s =Main.getAllStrats();
            for (int i=0;i<s.length;i++) {
                if(s[i]!=null) {
                    stratCombo.addItem(i);
                }
            }
            //popup.add(subMenu);
            stratCombo.addActionListener(changeStrat);
        }


        class KindItemAction extends AbstractAction {

            private Kind k;

            public KindItemAction(Kind k) {
                super(k.toString());
                this.k = k;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                //kindCombo.setSelectedItem(k);
            }
        }
    }
    private class ClearAction extends AbstractAction {
        ControlPanel controlPanel;
        public ClearAction(String name, ControlPanel controlPanel) {
            super(name);
            this.controlPanel =controlPanel;
        }

        public void actionPerformed(ActionEvent e) {
            nodeDraws.clear();
            edgeDraws.clear();
            Timer t = ((DeleteAllAction)this.controlPanel.deleteAll).timer;
            if(t!=null)t.stop();
            repaint();
        }
    }

    private class ColorAction extends AbstractAction {

        public ColorAction(String name) {
            super(name);
        }

        public void actionPerformed(ActionEvent e) {
            Color color = control.hueIcon.getColor();
            color = JColorChooser.showDialog(
                GraphPanel.this, "Choose a color", color);
            if (color != null) {
                NodeDraw.updateColor(nodeDraws, color);
                control.hueIcon.setColor(color);
                control.repaint();
                repaint();
            }
        }
    }

    public class DeleteAllAction extends AbstractAction {

        ControlPanel controlPanel;
        public Timer timer;
        public DeleteAllAction(String name, ControlPanel controlPanel) {
            super(name);
            this.controlPanel = controlPanel;
        }

        public void actionPerformed(ActionEvent e) {
            if(!random) {
                positionSave.clear();
                positionSave.addAll(nodeDraws);
            }
            timer = new Timer(0,new TimerListener((InitAction)controlPanel.init));
            timer.setRepeats(true);
            timer.setDelay(GraphPanel.delayAnimation);
            timer.start();
        }

        private class TimerListener implements ActionListener{
            private InitAction initAction;
            public TimerListener(InitAction initAction){
                this.initAction = initAction;
            }
            @Override
            public void actionPerformed(ActionEvent e) {
                Vertex toDelete = strategy.getNextVertexToDelete(graph);//Main.getSommetToDelete(graph);
                if(toDelete ==null){
                    if(random)return;
                    try {
                        initAction.resetGraph();
                        repaint();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                else {
                    graph.deleteVertex(toDelete.getId());
                    initAction.updateGraph(graph);
                    repaint();
                }
            }
        }
    }


    private class DeleteAction extends AbstractAction {


        private ControlPanel controlPanel;
        public DeleteAction(String name, ControlPanel controlPanel) {
            super(name);
            this.controlPanel = controlPanel;
        }

        public void actionPerformed(ActionEvent e) {
            if(!random) {
                positionSave.clear();
                positionSave.addAll(nodeDraws);
            }
            ListIterator<NodeDraw> iter = nodeDraws.listIterator();
            while (iter.hasNext()) {
                NodeDraw n = iter.next();
                if (n.isSelected()) {
                    graph.deleteVertex(n.id);
                }
            }
            ((InitAction)this.controlPanel.init).updateGraph(graph);
            repaint();
        }

    }

    private class KindComboAction extends AbstractAction {

        public KindComboAction(String name) {
            super(name);
        }

        public void actionPerformed(ActionEvent e) {
            JComboBox combo = (JComboBox) e.getSource();
            kind = (Kind) combo.getSelectedItem();
            NodeDraw.updateKind(nodeDraws, kind);
            repaint();
        }
    }

    private class ChangeStratAction extends AbstractAction {

        public ChangeStratAction(String name) {
            super(name);
        }

        public void actionPerformed(ActionEvent e) {
            JComboBox combo = (JComboBox) e.getSource();
            int i  = (Integer) combo.getSelectedItem();
            try {
                strategy = Main.getAllStrats()[i];
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            repaint();
        }
    }
    private class NewNodeAction extends AbstractAction {

        public NewNodeAction(String name) {
            super(name);
        }

        public void actionPerformed(ActionEvent e) {
            NodeDraw.selectNone(nodeDraws);
            Point p = mousePt.getLocation();
            Color color = control.hueIcon.getColor();
            NodeDraw n = new NodeDraw(p, radius, color, kind,rnd.nextInt());
            n.setSelected(true);
            nodeDraws.add(n);
            repaint();
        }

    }

    private class RandomAction extends AbstractAction {

        private ControlPanel controlPanel;
        public RandomAction(String name, ControlPanel panel) {
            super(name);
            this.controlPanel = panel;
        }

        public void actionPerformed(ActionEvent e) {
            this.controlPanel.clearAll.actionPerformed(null);
            graph = Main.createRandomGraph(Main.pCVertex,Main.pCEdge,Main.pEdge,Main.nbVertices,-1);
            //graph = Main.createBiArete(Main.pCVertex,Main.pCEdge,Main.nbVertices);
            this.controlPanel.init.actionPerformed(null);
        }
    }

    private class TestsAction extends AbstractAction {

        private ControlPanel controlPanel;
        public TestsAction(String name, ControlPanel panel) {
            super(name);
            this.controlPanel = panel;
        }

        public void actionPerformed(ActionEvent e) {
            double[][] n = Tests.testProf(strategy);
            System.out.println("\n");
            for(int i = 0;i<=10;i++){
                System.out.println("p= "+i/10.+" : " + Arrays.toString(n[i]));
            }
            //this.controlPanel.labelTest.setVisible(true);
            //this.controlPanel.labelTest.setText(String.valueOf(Math.round(n*100)/100.));
        }
    }

    private class InitAction extends AbstractAction {

        Edge[][] edges;
        List<Vertex> vertices;
        private ControlPanel controlPanel;

        public InitAction(String name, ControlPanel controlPanel) {
            super(name);
            this.controlPanel = controlPanel;
        }

        public void initGraphAction(Graph graph){
            this.edges = graph.getEdges();
            this.vertices = graph.getVertices();
            GraphPanel.graph = graph;
        }

        public void updateGraph(Graph graph){
            initGraphAction(graph);
            List<NodeDraw> nodeDrawsSave = new ArrayList<>(nodeDraws);
            nodeDraws.clear();
            edgeDraws.clear();
            for (int i = 0; i < vertices.size(); i++) {
                if(vertices.get(i).getColor().equals(ColorG.COLOR.BLUE))
                    nodeDraws.add(new NodeDraw(getNodeById(vertices.get(i).getId(),nodeDrawsSave).getLocation(), radius,new Color(173,216,230) , kind,vertices.get(i).getId()));
                else if(vertices.get(i).getColor().equals(ColorG.COLOR.RED))
                    nodeDraws.add(new NodeDraw(getNodeById(vertices.get(i).getId(),nodeDrawsSave).getLocation(), radius,Color.RED , kind,vertices.get(i).getId()));
            }
            for(int i = 0;i<nodeDraws.size();i++){
                for(int j = 0;j<nodeDraws.size();j++){
                    ColorG.COLOR color = edges[nodeDraws.get(i).id][nodeDraws.get(j).id].getColor();
                    if(color== ColorG.COLOR.BLUE)
                        edgeDraws.add(new EdgeDraw(nodeDraws.get(i),nodeDraws.get(j),Color.BLUE));
                    else if(color== ColorG.COLOR.RED)
                        edgeDraws.add(new EdgeDraw(nodeDraws.get(i),nodeDraws.get(j),Color.RED));
                }
            }
        }
        public void resetGraph() throws IOException {
            initGraphAction(Main.initGraph(Main.fileName));
            List<NodeDraw> nodeDrawsSave = new ArrayList<>(positionSave);
            nodeDraws.clear();
            edgeDraws.clear();
            for (int i = 0; i < vertices.size(); i++) {
                if(vertices.get(i).getColor().equals(ColorG.COLOR.BLUE))
                    nodeDraws.add(new NodeDraw(getNodeById(vertices.get(i).getId(),nodeDrawsSave).getLocation(), radius,new Color(173,216,230) , kind,vertices.get(i).getId()));
                else if(vertices.get(i).getColor().equals(ColorG.COLOR.RED))
                    nodeDraws.add(new NodeDraw(getNodeById(vertices.get(i).getId(),nodeDrawsSave).getLocation(), radius,Color.RED , kind,vertices.get(i).getId()));
            }
            for(int i = 0;i<nodeDraws.size();i++){
                for(int j = 0;j<nodeDraws.size();j++){
                    ColorG.COLOR color = edges[nodeDraws.get(i).id][nodeDraws.get(j).id].getColor();
                    if(color== ColorG.COLOR.BLUE)
                        edgeDraws.add(new EdgeDraw(nodeDraws.get(i),nodeDraws.get(j),Color.BLUE));
                    else if(color== ColorG.COLOR.RED)
                        edgeDraws.add(new EdgeDraw(nodeDraws.get(i),nodeDraws.get(j),Color.RED));
                }
            }
        }
        public void actionPerformed(ActionEvent e) {

            this.controlPanel.clearAll.actionPerformed(null);
            Graph graph = null;
            if(e !=null) {
                random = false;
                try {
                    graph = Objects.requireNonNull(Main.initGraph(Main.fileName));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            else {
                graph = GraphPanel.graph;
                random = true;
            }
            if(positionSave.size()!=0 && e !=null){
                try {
                    resetGraph();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                repaint();
                return;
            }
            initGraphAction(graph);

            for (int i = 0; i < vertices.size(); i++) {
                Point p = new Point(rnd.nextInt(getWidth()), rnd.nextInt(getHeight()));
                if(vertices.get(i).getColor().equals(ColorG.COLOR.BLUE))
                    nodeDraws.add(new NodeDraw(p, radius,new Color(173,216,230) , kind,vertices.get(i).getId()));
                else if(vertices.get(i).getColor().equals(ColorG.COLOR.RED))
                    nodeDraws.add(new NodeDraw(p, radius,Color.RED , kind,vertices.get(i).getId()));
            }
            for(int i = 0;i<edges.length;i++){
                for(int j = 0;j<edges.length;j++){
                    ColorG.COLOR color = edges[i][j].getColor();
                    if(color== ColorG.COLOR.BLUE)
                        edgeDraws.add(new EdgeDraw(nodeDraws.get(i),nodeDraws.get(j),Color.BLUE));
                    else if(color== ColorG.COLOR.RED)
                        edgeDraws.add(new EdgeDraw(nodeDraws.get(i),nodeDraws.get(j),Color.RED));
                }
            }
            repaint();
        }

    }

    /**
     * The kinds of node in a graph.
     */
    private enum Kind {

        Circular, Rounded, Square;
    }

    /**
     * An Edge is a pair of Nodes.
     */
    private static class EdgeDraw {

        private NodeDraw n1;
        private NodeDraw n2;
        private Color color;

        public EdgeDraw(NodeDraw n1, NodeDraw n2, Color color) {
            this.n1 = n1;
            this.n2 = n2;
            this.color = color;
        }

        public void draw(Graphics g) {
            Point p1 = n1.getLocation();
            Point p2 = n2.getLocation();
            g.setColor(color);
            g.drawLine(p1.x+plusMinus1(), p1.y+plusMinus1(), p2.x+plusMinus1(), p2.y+plusMinus1());
            g.setColor(Color.black);
            String s = ((n1.p.getX()<n2.p.getX()))?">":"<";
            g.drawString(s,(int)(n1.p.getX()+n2.p.getX())/2,(int)(n1.p.getY()+n2.p.getY())/2);
        }
    }

    /**
     * A Node represents a node in a graph.
     */
    private static class NodeDraw {

        private Point p;
        private int r;
        private Color color;
        private Kind kind;
        private boolean selected = false;
        private Rectangle b = new Rectangle();
        private int id;
        /**
         * Construct a new node.
         */
        public NodeDraw(Point p, int r, Color color, Kind kind, int id) {
            this.p = p;
            this.r = r;
            this.color = color;
            this.kind = kind;
            this.id = id;
            setBoundary(b);
        }

        /**
         * Calculate this node's rectangular boundary.
         */
        private void setBoundary(Rectangle b) {
            b.setBounds(p.x - r, p.y - r, 2 * r, 2 * r);
        }

        /**
         * Draw this node.
         */
        public void draw(Graphics g) {
            g.setColor(this.color);
            if (this.kind == Kind.Circular) {
                g.fillOval(b.x, b.y, b.width, b.height);
            } else if (this.kind == Kind.Rounded) {
                g.fillRoundRect(b.x, b.y, b.width, b.height, r, r);
            } else if (this.kind == Kind.Square) {
                g.fillRect(b.x, b.y, b.width, b.height);
            }
            if (selected) {
                g.setColor(Color.darkGray);
                g.drawRect(b.x, b.y, b.width, b.height);
            }
            g.setColor(Color.BLACK);
            g.drawString(this.id+"",(int)p.getX(),(int)p.getY());
        }

        /**
         * Return this node's location.
         */
        public Point getLocation() {
            return p;
        }

        /**
         * Return true if this node contains p.
         */
        public boolean contains(Point p) {
            return b.contains(p);
        }

        /**
         * Return true if this node is selected.
         */
        public boolean isSelected() {
            return selected;
        }

        /**
         * Mark this node as selected.
         */
        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        /**
         * Collected all the selected nodes in list.
         */
        public static void getSelected(List<NodeDraw> list, List<NodeDraw> selected) {
            selected.clear();
            for (NodeDraw n : list) {
                if (n.isSelected()) {
                    selected.add(n);
                }
            }
        }

        /**
         * Select no nodes.
         */
        public static void selectNone(List<NodeDraw> list) {
            for (NodeDraw n : list) {
                n.setSelected(false);
            }
        }

        /**
         * Select a single node; return true if not already selected.
         */
        public static boolean selectOne(List<NodeDraw> list, Point p) {
            for (NodeDraw n : list) {
                if (n.contains(p)) {
                    if (!n.isSelected()) {
                        NodeDraw.selectNone(list);
                        n.setSelected(true);
                    }
                    return true;
                }
            }
            return false;
        }

        /**
         * Select each node in r.
         */
        public static void selectRect(List<NodeDraw> list, Rectangle r) {
            for (NodeDraw n : list) {
                n.setSelected(r.contains(n.p));
            }
        }

        /**
         * Toggle selected state of each node containing p.
         */
        public static void selectToggle(List<NodeDraw> list, Point p) {
            for (NodeDraw n : list) {
                if (n.contains(p)) {
                    n.setSelected(!n.isSelected());
                }
            }
        }

        /**
         * Update each node's position by d (delta).
         */
        public static void updatePosition(List<NodeDraw> list, Point d) {
            for (NodeDraw n : list) {
                if (n.isSelected()) {
                    n.p.x += d.x;
                    n.p.y += d.y;
                    n.setBoundary(n.b);
                }
            }
        }

        /**
         * Update each node's radius r.
         */
        public static void updateRadius(List<NodeDraw> list, int r) {
            for (NodeDraw n : list) {
                if (n.isSelected()) {
                    n.r = r;
                    n.setBoundary(n.b);
                }
            }
        }

        /**
         * Update each node's color.
         */
        public static void updateColor(List<NodeDraw> list, Color color) {
            for (NodeDraw n : list) {
                if (n.isSelected()) {
                    n.color = color;
                }
            }
        }

        /**
         * Update each node's kind.
         */
        public static void updateKind(List<NodeDraw> list, Kind kind) {
            for (NodeDraw n : list) {
                if (n.isSelected()) {
                    n.kind = kind;
                }
            }
        }
    }

    private static class ColorIcon implements Icon {

        private static final int WIDE = 20;
        private static final int HIGH = 20;
        private Color color;

        public ColorIcon(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return color;
        }

        public void setColor(Color color) {
            this.color = color;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(color);
            g.fillRect(x, y, WIDE, HIGH);
        }

        public int getIconWidth() {
            return WIDE;
        }

        public int getIconHeight() {
            return HIGH;
        }
    }

    public NodeDraw getNodeById(int id, List<NodeDraw> nodeDraws){
        for(NodeDraw nodeDraw :nodeDraws){
            if(nodeDraw.id==id)
                return nodeDraw;
        }
        return null;
    }
    public static int plusMinus1(){
        return (Math.random()>0.5)?1:-1;
    }



}
