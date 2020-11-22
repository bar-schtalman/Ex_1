package ex_1;

import java.io.*;
import java.util.*;

/**
 * This interface represents an Undirected (positive) Weighted Graph Theory algorithms including:
 * 0. clone(); (copy)
 * 1. init(graph);
 * 2. isConnected();
 * 3. double shortestPathDist(int src, int dest);
 * 4. List<node_data> shortestPath(int src, int dest);
 * 5. Save(file);
 * 6. Load(file);
 *
 * @author boaz.benmoshe
 */
public class WGraph_Algo implements weighted_graph_algorithms, Serializable {
    private WGraph_DS graph;

    public WGraph_Algo() {
        this.graph = new WGraph_DS();
    }

    @Override
    /**
     * Init the graph on which this set of algorithms operates on.
     * @param g
     */
    public void init(weighted_graph g) {
        this.graph = (WGraph_DS) g;
    }

    /**
     * Return the underlying graph of which this class works.
     *
     * @return
     */
    @Override
    public weighted_graph getGraph() {
        return this.graph;
    }

    /**
     * Compute a deep copy of this weighted graph.
     *
     * @return
     */
    @Override
    public weighted_graph copy() {
        WGraph_DS WGDS = new WGraph_DS(this.graph);
        return WGDS;
    }

    /**
     * Returns true if and only if (iff) there is a valid path from EVREY node to each
     * other node. NOTE: assume ubdirectional graph.
     *
     * @return
     */
    @Override
    public boolean isConnected() {
        if (this.graph.nodeSize() <= 1) return true;//case:no vertex in the graph
        if (this.graph.edgeSize() == 0 && this.graph.nodeSize() > 1)
            return false;//case:no edges and more then one vertex
        WGraph_DS g = (WGraph_DS) this.copy();//makes a deep copy of the graph for BFS
        WGraph_DS.Node a = (WGraph_DS.Node) g.getV().iterator().next();
        Dijkstra(a);//applying Dijkstra on vertex s

        for (node_info node : g.getV()) {
            if (node.getInfo().equals("white")) {//if one of the nodes is white,graph not connected.
                return false;
            }
        }

        return true;
    }

    /**
     * returns the length of the shortest path between src to dest
     * Note: if no such path --> returns -1
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        WGraph_DS g = new WGraph_DS(graph);//making a deep copy of the graph for BFS
        WGraph_DS.Node a = (WGraph_DS.Node) g.getNode(src);
        WGraph_DS.Node b = (WGraph_DS.Node) g.getNode(dest);
        if (a == null || b == null) {
            return -1;
        }
        //check if the graph contains those nodes

        Dijkstra((WGraph_DS.Node) g.getNode(src));

        return g.getNode(dest).getTag();//return the distance from src to dest(both in the graph)
    }

    /**
     * returns the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * see: https://en.wikipedia.org/wiki/Shortest_path_problem
     * Note if no such path --> returns null;
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public List<node_info> shortestPath(int src, int dest) {
        WGraph_DS g = new WGraph_DS(graph);//make a deep copy of the graph for BFS
        LinkedList<node_info> list = new LinkedList<node_info>();//list of the vertex path
        WGraph_DS.Node s = (WGraph_DS.Node) g.getNode(src);//adding vertex for the source
        WGraph_DS.Node d = (WGraph_DS.Node) g.getNode(dest);//adding vertex for destination
        if (s != null && d != null) {//check if the graph contain src and dest
            Dijkstra(s);//applying BFS on vertex s
            if (d.getTag() == -1) return null;//case:theres no path between vertex src and vertex dest
            while (d.getParent() != null) {//while loop that adding vertex by parent order
                list.addFirst(d);
                d = d.getParent();
            }
            list.addFirst(s);
        }
        return list;

    }

    /**
     * Saves this weighted (undirected) graph to the given
     * file name
     *
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
    @Override
    public boolean save(String file) {
        try {
            FileOutputStream filename = new FileOutputStream((file));
            ObjectOutputStream object = new ObjectOutputStream(filename);
            object.writeObject(this.graph);
            object.close();
            filename.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     *
     * @param file - file name
     * @return true - iff the graph was successfully loaded.
     */
    @Override
    public boolean load(String file) {
        try {
            FileInputStream filename = new FileInputStream(file);
            ObjectInputStream object = new ObjectInputStream(filename);
            this.graph = (WGraph_DS) object.readObject();
            object.close();
            filename.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WGraph_Algo g = (WGraph_Algo) o;
        for (node_info node : this.graph.getV()) {
            if (!g.graph.getV().contains(node.getKey())) {
                return false;
            }
        }
        return true;
    }

    public void Dijkstra(WGraph_DS.Node n) {
        PriorityQueue<WGraph_DS.Node> queue = new PriorityQueue<WGraph_DS.Node>();
        n.setTag(0);
        queue.add(n);
        while (!queue.isEmpty()) {
            WGraph_DS.Node prev = queue.remove();
            for (node_info next : this.graph.getV(prev.getKey())) {
                WGraph_DS.Node node = (WGraph_DS.Node) next;
                if (node.getTag() > prev.getTag() + this.graph.getEdge(prev.getKey(), node.getKey())) {
                    node.setTag(prev.getTag() + this.graph.getEdge(prev.getKey(), node.getKey()));
                    node.setParent(prev);

                }
                if (node.getInfo().equals("white")) {
                    queue.add(node);
                }
            }
            prev.setInfo("black");
        }

    }
}

