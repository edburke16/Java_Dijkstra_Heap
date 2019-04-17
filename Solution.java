import java.io.*;
import java.text.*;
import java.math.*;
import java.util.Scanner;
import java.util.Iterator;

public class Solution {

/* Enter your code here. Read input from STDIN. Print output to STDOUT. The classname 'Solution' is written above in the head. */
/* DO NOT import and/or use any addtional libraries. */

    public static void main(String[] args) throws Exception {
        Solution sol = new Solution(); 
        Scanner input = new Scanner(System.in);
        int cities = input.nextInt(); // Total cities N 
        int flights = input.nextInt(); // Total flights M 

        // Adjacency List Array of ConsList
        ConsList[] network = new ConsList[cities + 1]; 

        // Fill adjacency list with flights 
        for (int i = 0; i < flights; i++) { 
            int start = input.nextInt(); 
            int end = input.nextInt(); 
            int cost = input.nextInt(); 

            sol.addEdge(network, start, end, cost); 
        } 

        int origin = input.nextInt(); 
        int destination = input.nextInt(); 

        Heap answer = new Heap(cities); 
        answer.initHeap(origin); 

        // Dijkstra's Algorithm 
        while (answer.size() != 0) { 
            int ver = answer.extractMin();
            ConsList next = network[ver];
            while (next != null) {
                answer.decreaseKey(ver, next.vertex, next.weight);
                next = next.rest;
            }    
        }

        // Output of the program 
        System.setOut(new PrintStream(System.out));
        if (answer.getKey(destination) == 100000000) {
            System.out.print(-1); // No path found 
        }
        else {
            System.out.print(answer.getKey(destination)); // Print final weight
        }
    }

    void addEdge(ConsList[] adjacencyList, int u, int v, int w) {
        adjacencyList[u] = new ConsList(v, w, adjacencyList[u]);
    }

/* Remember to include the following '}' to complete the definition of 'Solution'. */
}

/* You can also define other classes here. */

class ConsList {
    int vertex, weight;
    ConsList rest;

    public ConsList(int v, int w, ConsList r) {
        vertex = v;
        weight = w;
        rest = r;
    }
}

class Heap {
    int[] heap, key, pos;
    int allocatedSize, heapSize;

    public Heap(int size) {
        // To make life easier we index all the vertices from 1 to N,
        // and the heap also from 1 to N, so we need to allocate one more
        // Verticies themselves 
        heap = new int[size + 1];
        // Value on the item in the heap 
        key = new int[size + 1];
        // Position in the heap tree 
        pos = new int[size + 1];
        allocatedSize = size + 1;
        heapSize = size;
    }

    public int size() {
    return heapSize;
}
    // Gives the cost to get to v 
    public int getKey(int v) {
        return key[v];
    }

    private void swap(int a, int b) {
    // Swapping the a-th element and the b-th element in the heap
        pos[heap[a]] = b;
        pos[heap[b]] = a;
        int tmp = heap[b];
        heap[b] = heap[a];
        heap[a] = tmp;
    }
    
    public void initHeap(int source) {
        for (int i = 1; i <= heapSize; i++) {
            // Put Vertex i as the i-th element in the heap
            heap[i] = i;
            pos[i] = i;
            key[i] = 100000000;
        }
        // Set the key of the source vertex and make it the root of the heap
        key[source] = 0;
        swap(1, source);
    }

    public int extractMin() {
        int res = heap[1];
        swap(1, heapSize);
        heapSize -= 1; // Remember to decrease the heap size before bubble down 
        bubbleDown(1);
        return res;
    }
    
    // u = start node, v = dest node, w = cost 
    public boolean decreaseKey(int u, int v, int w) {
        int newKey = key[u] + w;
        if (key[v] > newKey) {
            key[v] = newKey;
            bubbleUp(pos[v]);
            return true;
        }
        return false;
    }

    public void bubbleUp(int in) {
        while (key[heap[in / 2]] > key[heap[in]]) {
            swap(in, in / 2);
            in = in / 2;
        }
    }

    public void bubbleDown(int in) {
        while (2 * in <= heapSize) {
            int j = 2 * in;
            if (j < heapSize && key[heap[j]] > key[heap[j + 1]]){
                j++;
            }
            if (key[heap[in]] <= key[heap[j]]){
                break;
            }
            swap(in, j);
            in = j;
        }
    }
}
