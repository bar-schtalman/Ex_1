package test;

import ex_1.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.*;

class WGraph_AlgoTest {

    @Test
    void isConnected() {
        WGraph_DS g0 = (WGraph_DS) WGraph_DSTest.graph_creator(4, 0, 1);
        WGraph_Algo ag0 = new WGraph_Algo();
        ag0.init(g0);
        assertFalse(ag0.isConnected());
        g0 = (WGraph_DS) WGraph_DSTest.graph_creator(12, 21, 1);
        WGraph_Algo ag1 = new WGraph_Algo();
        ag1.init(g0);
        assertTrue(ag1.isConnected());
        g0 = (WGraph_DS) WGraph_DSTest.graph_creator(40, 10, 1);
        WGraph_Algo ag2 = new WGraph_Algo();
        ag2.init(g0);
        assertFalse(ag2.isConnected());
        WGraph_DS g3 = (WGraph_DS) WGraph_DSTest.graph_creator(8, 17, 1);
        WGraph_Algo ag3 = new WGraph_Algo();
        ag3.init(g3);
        assertTrue(ag3.isConnected());
        g3.removeNode(2);
        g3.removeNode(7);
        g3.removeNode(5);
        g3.removeNode(6);
        g3.removeNode(1);
        assertTrue(ag3.isConnected());
        g3.removeNode(0);
        assertFalse(ag3.isConnected());


    }

    @Test
    void shortestPathDist() {
        WGraph_DS g0 = (WGraph_DS) WGraph_DSTest.graph_creator(3, 0, 1);
        g0.connect(1, 2, 4);
        g0.connect(0, 1, 5);
        WGraph_Algo ag0 = new WGraph_Algo();
        ag0.init(g0);
        assertTrue(ag0.isConnected());
        double d = ag0.shortestPathDist(0, 2);
        assertEquals(9, d);
    }



    @Test
    void shortestPath() {
        WGraph_DS g0 = (WGraph_DS) WGraph_DSTest.graph_creator(5, 0, 1);
        g0.connect(1, 2, 4);
        g0.connect(0, 1, 5);
        g0.connect(2, 3, 2);
        g0.connect(3, 4, 6);
        g0.connect(0, 4, 2);
        WGraph_Algo ag0 = new WGraph_Algo();
        ag0.init(g0);
        List<node_info> sp = ag0.shortestPath(1, 4);
        int[] checkKey = {1, 0, 4};
        int i = 0;
        for (node_info n : sp) {
            //assertEquals(n.getTag(), checkTag[i]);
            assertEquals(n.getKey(), checkKey[i]);
            i++;
        }

    }


    @Test
    void save_load() {
        weighted_graph g0 = WGraph_DSTest.graph_creator(10,30,1);
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g0);
        String str = "g0.obj";
        ag0.save(str);
        weighted_graph g1 = WGraph_DSTest.graph_creator(10,30,1);
        ag0.load(str);
        assertEquals(g0,g1);
        g0.removeNode(0);
        assertNotEquals(g0,g1);
    }

}