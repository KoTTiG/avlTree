import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static java.lang.Math.*;
import static org.junit.jupiter.api.Assertions.*;

public class AvlTreeTest {
    @Test
    public void addTest(){
        AvlTree<Integer> testTree = new AvlTree<Integer>();
        Set<Integer> set = new HashSet<>();
        int element = -1;
        while (set.size() < 100){
            set.add((int) (random() * 5000));
        }
        for (int i: set){
            testTree.add(i);
            if (element == -1) element = i;
        }
        assertTrue(testTree.height() <= 8, set.toString()); //Длина сбалансированного дерева лежит в интервале [log2(n);log2(n)+1]
        assertFalse(testTree.add(element));
    }

    @Test
    public void removeTest(){
        AvlTree<Integer> testTree = new AvlTree<Integer>();
        for (int i =0; i<100; i++){
            testTree.add(i);
        }
        int toRemove = (int) (random() * 100);
        int startHeight = testTree.height();
        assertTrue(testTree.remove(toRemove), ""+toRemove);
        assertFalse(testTree.remove(toRemove));
        assertTrue(testTree.height() <= startHeight);
    }
}
