package test;

import ex_1.WGraph_DS;
import ex_1.node_info;
import ex_1.weighted_graph;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WGraph_DSTest {
    private static Random _rnd = null;

    @Test
    void hasEdge() {
        WGraph_DS g=new WGraph_DS();
        g.addNode(0);
        g.addNode(1);
        g.addNode(2);
        g.addNode(3);
        g.addNode(4);
        g.addNode(5);
        g.connect(0,1,4);
        g.connect(2,3,1);
        g.connect(4,5,3);
        g.connect(0,5,1);
        g.connect(5,3,88);
        g.connect(1,4,12);
        assertEquals(true,g.hasEdge(0,1));
        assertEquals(false,g.hasEdge(0,3));
    }

    @Test
    void getEdge() {
        WGraph_DS g=new WGraph_DS();
        g.addNode(0);
        g.addNode(1);
        g.addNode(2);
        g.addNode(3);
        g.addNode(4);
        g.addNode(5);
        g.connect(0,1,4);
        g.connect(2,3,1);
        g.connect(4,5,3);
        assertEquals(-1,g.getEdge(1,5));
        assertEquals(1,g.getEdge(2,3));
    }

    @Test
    void connect() {
        WGraph_DS g=new WGraph_DS();
        g.addNode(0);
        g.addNode(1);
        g.addNode(2);
        g.connect(0,1,2);
        g.connect(0,2,3);
        g.connect(1,2,4);
        assertEquals(3,g.edgeSize());
        g.connect(1,2,3);
        assertEquals(3,g.getEdge(1,2));
        g.connect(1,2,6);
        assertEquals(6,g.getEdge(1,2));
    }

    @Test
    void getV() {
        WGraph_DS g=new WGraph_DS();
        g.addNode(0);
        g.addNode(1);
        g.addNode(2);
        g.addNode(3);
        g.addNode(4);
        g.connect(0,1,4);
        g.connect(0,4,1);
        g.connect(2,4,2);
        Collection<node_info> v = g.getV();
        Iterator<node_info> iter = v.iterator();
        while (iter.hasNext()) {
            node_info n = iter.next();
            assertNotNull(n);
        }

    }


    @Test
    void removeNode() {
        WGraph_DS g = new WGraph_DS();
        g.addNode(0);
        g.addNode(1);
        g.addNode(2);
        assertEquals(3,g.nodeSize());
        g.addNode(3);
        g.addNode(4);
        g.addNode(5);
        g.connect(0,1,4);
        g.connect(2,3,1);
        g.connect(4,5,3);
        g.connect(0,5,1);
        g.connect(5,3,88);
        g.connect(1,4,12);
        assertTrue(g.hasEdge(3,5));
        assertTrue(g.hasEdge(3,2));
        g.removeNode(3);
        assertEquals(5,g.nodeSize());
        assertEquals(4,g.edgeSize());
        assertFalse(g.hasEdge(3,5));
    }


    @Test
    void nodeSize() {
        WGraph_DS g = new WGraph_DS();
        g.addNode(0);
        g.addNode(1);
        g.addNode(2);
        g.addNode(3);
        assertEquals(4,g.nodeSize());
        g.removeNode(5);
        assertEquals(4,g.nodeSize());
        g.removeNode(1);
        assertEquals(3,g.nodeSize());
        g.removeNode(0);
        g.removeNode(2);
        g.removeNode(3);
        assertEquals(0,g.nodeSize());
    }

    @Test
    void edgeSize() {
        weighted_graph g = new WGraph_DS();
        g.addNode(0);
        g.addNode(1);
        g.addNode(2);
        g.addNode(3);
        g.addNode(4);
        g.addNode(5);
        g.connect(0,1,4);
        g.connect(2,3,1);
        g.connect(4,5,3);
        g.connect(0,5,1);
        g.connect(5,3,88);
        g.connect(1,4,12);
        assertEquals(6,g.edgeSize());
        g.removeEdge(1,3);
        assertEquals(6,g.edgeSize());
        g.removeEdge(0,1);
        g.removeEdge(1,4);
        assertEquals(4,g.edgeSize());
        assertEquals(3,g.getEdge(4,5));
    }

    ///////////////////////////////////
    /**
     * Generate a random graph with v_size nodes and e_size edges
     * @param v_size
     * @param e_size
     * @param seed
     * @return
     */
    public static weighted_graph graph_creator(int v_size, int e_size, int seed) {
        weighted_graph g = new WGraph_DS();
        _rnd = new Random(seed);
        for(int i=0;i<v_size;i++) {
            g.addNode(i);
        }
        // Iterator<node_data> itr = V.iterator(); // Iterator is a more elegant and generic way, but KIS is more important
        int[] nodes = nodes(g);
        while(g.edgeSize() < e_size) {
            int a = nextRnd(0,v_size);
            int b = nextRnd(0,v_size);
            int i = nodes[a];
            int j = nodes[b];
            double w = _rnd.nextDouble();
            g.connect(i,j, w);
        }
        return g;
    }
    private static int nextRnd(int min, int max) {
        double v = nextRnd(0.0+min, (double)max);
        int ans = (int)v;
        return ans;
    }
    private static double nextRnd(double min, double max) {
        double d = _rnd.nextDouble();
        double dx = max-min;
        double ans = d*dx+min;
        return ans;
    }
    /**
     * Simple method for returning an array with all the node_data of the graph,
     * Note: this should be using an Iterator<node_edge> to be fixed in Ex1
     * @param g
     * @return
     */
    private static int[] nodes(weighted_graph g) {
        int size = g.nodeSize();
        Collection<node_info> V = g.getV();
        node_info[] nodes = new node_info[size];
        V.toArray(nodes); // O(n) operation
        int[] ans = new int[size];
        for(int i=0;i<size;i++) {ans[i] = nodes[i].getKey();}
        Arrays.sort(ans);
        return ans;
    }


}