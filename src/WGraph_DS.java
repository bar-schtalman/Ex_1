package ex_1;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

/**
 * This interface represents an undirectional weighted graph.
 * It should support a large number of nodes (over 10^6, with average degree of 10).
 * The implementation should be based on an efficient compact representation
 * (should NOT be based on a n*n matrix).
 */

public class WGraph_DS implements weighted_graph, Serializable {
    private int mc;
    private int edgeSize;
    private int nodeSize;
    private HashMap<Integer, node_info> hash;

    public WGraph_DS() {
        this.mc = 0;
        this.edgeSize = 0;
        this.nodeSize = 0;
        this.hash = new HashMap<Integer, node_info>();
    }

    /**
     * *************************---------this is a subClass for WGraph_DS---------**************************
     */
    public class Node implements node_info, Serializable, Comparable<Node> {
        private int key;
        private String info;
        private double tag;
        private Node parent;
        private HashMap<Integer, node_info> hash;
        private HashMap<node_info, Double> edges;


        public Node() {
            this.key = 0;
            this.info = "white";
            this.tag = Integer.MAX_VALUE;
            this.parent = null;
            this.hash = new HashMap<Integer, node_info>();
            this.edges = new HashMap<node_info, Double>();
        }

        public Node(WGraph_DS.Node node) {
            this.key = node.key;
            this.info = node.info;
            this.tag = node.tag;
            this.parent = node.parent;
            this.hash = new HashMap<Integer, node_info>();
            this.edges = new HashMap<node_info, Double>();

        }

        /**
         * Return the key (id) associated with this node.
         * Note: each node_data should have a unique key.
         *
         * @return
         */
        @Override
        public int getKey() {
            return this.key;
        }

        /**
         * return the remark (meta data) associated with this node.
         *
         * @return
         */

        @Override
        public String getInfo() {
            return this.info;
        }

        /**
         * Allows changing the remark (meta data) associated with this node.
         *
         * @param s
         */
        @Override
        public void setInfo(String s) {
            this.info = s;
        }

        /**
         * Temporal data (aka distance, color, or state)
         * which can be used be algorithms
         *
         * @return
         */
        @Override
        public double getTag() {
            return this.tag;
        }

        /**
         * Allow setting the "tag" value for temporal marking an node - common
         * practice for marking by algorithms.
         *
         * @param t - the new value of the tag
         */
        @Override
        public void setTag(double t) {
            this.tag = t;

        }

        /**
         * Temporal parent data
         * which can be used be algorithms
         *
         * @return
         */
        public Node getParent() {
            return parent;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public HashMap<Integer, node_info> getHash() {
            return hash;
        }

        public void setHash(HashMap<Integer, node_info> hash) {
            this.hash = hash;
        }

        public HashMap<node_info, Double> getEdges() {
            return edges;
        }

        public void setEdges(HashMap<node_info, Double> edges) {
            this.edges = edges;
        }

        /**
         * method from ex_0, return true if given Node has neighbor by the key
         *
         * @return
         */
        public boolean hasNi(int key) {
            return this.hash.containsKey(key);
        }

        /**
         * equals method for saving and loading the graph
         *
         * @return
         */
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (o instanceof Node) {
                return key == ((Node) o).getKey();
            }
            return false;
        }

        @Override
        public int compareTo(Node o) {
            if (this.tag > o.getTag())
                return 1;
            if (this.tag < o.getTag()) {
                return -1;
            }
            return 0;
        }
    }
    /**
     ********************************--------------end of Node class--------------********************************
     */

    /**
     * deep copy of this graph
     * which can be used by algorithms
     *
     * @return
     */
    public WGraph_DS(WGraph_DS graph) {
        this.hash = new HashMap<Integer, node_info>();
        for (node_info temp : graph.getV()) {
            this.hash.put(temp.getKey(), temp);
        }
        this.mc = graph.mc;
        this.nodeSize = graph.nodeSize();
        this.edgeSize = graph.edgeSize;
    }

    /**
     * return the node_data by the node_id,
     *
     * @param key - the node_id
     * @return the node_data by the node_id, null if none.
     */
    @Override
    public node_info getNode(int key) {
        if (!this.hash.isEmpty()) return this.hash.get(key);
        return null;
    }

    /**
     * return true iff (if and only if) there is an edge between node1 and node2
     * Note: this method should run in O(1) time.
     *
     * @param node1
     * @param node2
     * @return
     */

    @Override
    public boolean hasEdge(int node1, int node2) {
        if(!this.hash.containsKey(node1)||!this.hash.containsKey(node2)){
            return false;
        }
        if (node1 == node2) return false;
        Node temp1 = (Node) this.getNode(node1);
        return temp1.hasNi(node2);
    }

    /**
     * return the weight if the edge (node1, node1). In case
     * there is no such edge - should return -1
     * Note: this method should run in O(1) time.
     *
     * @param node1
     * @param node2
     * @return
     */
    @Override
    public double getEdge(int node1, int node2) {
        Node temp1 = (Node) this.getNode(node1);
        Node temp2 = (Node) this.getNode(node2);
        if (hasEdge(node1, node2)) {
            return temp1.getEdges().get(temp2);
        }
        return -1;
    }

    /**
     * add a new node to the graph with the given key.
     * Note: this method should run in O(1) time.
     * Note2: if there is already a node with such a key -> no action should be performed.
     *
     * @param key
     */
    @Override
    public void addNode(int key) {
        if (this.hash.containsKey(key)) {
            return;
        }
        Node n = new Node();
        n.setKey(key);
        this.hash.put(n.getKey(), n);
        this.nodeSize++;
        this.mc++;
    }

    /**
     * Connect an edge between node1 and node2, with an edge with weight >=0.
     * Note: this method should run in O(1) time.
     * Note2: if the edge node1-node2 already exists - the method simply updates the weight of the edge.
     */
    @Override
    public void connect(int node1, int node2, double w) {
        if (node1 == node2) return;
        Node temp1 = (Node) this.getNode(node1);
        Node temp2 = (Node) this.getNode(node2);
        if (temp1 != null && temp2 != null) {
            if (hasEdge(node1, node2) && this.getEdge(node2, node2) == w) {
                return;
            }
            if (hasEdge(node1, node2) && this.getEdge(node2, node2) != w) {
                temp1.getEdges().replace(temp2, getEdge(node1, node2), w);
                temp2.getEdges().replace(temp1, getEdge(node2, node1), w);
                this.mc++;
                return;
            }
            temp1.getEdges().put(temp2, w);
            temp1.getHash().put(node2, temp2);
            temp2.getEdges().put(temp1, w);
            temp2.getHash().put(node1, temp1);
            this.edgeSize++;
            this.mc++;

        }
    }

    /**
     * This method return a pointer (shallow copy) for a
     * Collection representing all the nodes in the graph.
     * Note: this method should run in O(1) tim
     *
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_info> getV() {
        return this.hash.values();
    }

    /**
     * This method returns a Collection containing all the
     * nodes connected to node_id
     * Note: this method can run in O(k) time, k - being the degree of node_id.
     *
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_info> getV(int node_id) {
        Node node = (Node) this.hash.get(node_id);
        return node.getHash().values();
    }

    /**
     * Delete the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * This method should run in O(n), |V|=n, as all the edges should be removed.
     *
     * @param key
     * @return the data of the removed node (null if none).
     */
    @Override
    public node_info removeNode(int key) {
        if (this.getNode(key) == null) return null;//case 0:vertex does not exists
        Node node = (Node) this.getNode(key);
        for (node_info temp : node.getHash().values()) {
            Node temp2 = (Node) temp;
            temp2.getHash().remove(key);
            this.edgeSize--;
        }
        this.nodeSize--;
        this.mc++;
        return this.hash.remove(key);
    }

    /**
     * Delete the edge from the graph,
     * Note: this method should run in O(1) time.
     *
     * @param node1
     * @param node2
     */
    @Override
    public void removeEdge(int node1, int node2) {
        if (!hasEdge(node1, node2) || node1 == node2) return;
        Node temp1 = (Node) this.getNode(node1);
        Node temp2 = (Node) this.getNode(node2);
        temp1.getHash().remove(node2);
        temp1.getEdges().remove(temp2);
        temp2.getHash().remove(node1);
        temp2.getEdges().remove(temp1);
        this.mc++;
        this.edgeSize--;

    }

    /**
     * return the number of vertices (nodes) in the graph.
     * Note: this method should run in O(1) time.
     *
     * @return
     */
    @Override
    public int nodeSize() {
        return this.nodeSize;
    }

    /**
     * return the number of edges (undirectional graph).
     * Note: this method should run in O(1) time.
     *
     * @return
     */
    @Override
    public int edgeSize() {
        return this.edgeSize;
    }

    /**
     * return the Mode Count - for testing changes in the graph.
     * Any change in the inner state of the graph should cause an increment in the ModeCount
     *
     * @return
     */
    @Override
    public int getMC() {
        return this.mc;
    }

    /**
     * equals method for saving and loading the graph
     *
     * @return
     */
    public boolean equals(Object o) {
        if (!(o instanceof WGraph_DS)) {
            return false;
        }
        WGraph_DS g = (WGraph_DS) o;
        for (node_info temp : g.getV()) {
            if (!this.hash.containsKey(temp.getKey())) return false;
            WGraph_DS.Node node = (Node) this.getNode(temp.getKey());
            if (!node.equals(temp)) return false;
        }
        return true;
    }


}
