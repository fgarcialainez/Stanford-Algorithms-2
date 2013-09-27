/*
 * Programming Question - Week 4, September 2013.
 * 
 * In this assignment you will implement one or more algorithms for the all-pairs
 * shortest-path problem. Here are data files describing three graphs: graph #1;
 * graph #2; graph #3.
 * 
 * The first line indicates the number of vertices and edges, respectively. Each 
 * subsequent line describes an edge (the first two numbers are its tail and head,
 * respectively) and its length (the third number). NOTE: some of the edge lengths 
 * are negative. NOTE: These graphs may or may not have negative-cost cycles.
 * 
 * Your task is to compute the "shortest shortest path". Precisely, you must first
 * identify which, if any, of the three graphs have no negative cycles. For each 
 * such graph, you should compute all-pairs shortest paths and remember the smallest
 * one (i.e., compute minu,v∈Vd(u,v), where d(u,v) denotes the shortest-path distance
 * from u to v).
 * 
 * If each of the three graphs has a negative-cost cycle, then enter "NULL" in the
 * box below. If exactly one graph has no negative-cost cycles, then enter the length
 * of its shortest shortest path in the box below. If two or more of the graphs have
 * no negative-cost cycles, then enter the smallest of the lengths of their shortest
 * shortest paths in the box below.
 * 
 * OPTIONAL: You can use whatever algorithm you like to solve this question. If you
 * have extra time, try comparing the performance of different all-pairs shortest-path
 * algorithms!
 * 
 * OPTIONAL: If you want a bigger data set to play with, try computing the shortest
 * shortest path for this graph.
 */
package com.stanford.algorithms.parttwo.weekfour;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Algorithms: Design and Analysis, Part 2
 * Programming Question - Week 4
 * @author Felix Garcia Lainez
 */
public class Question1
{
    private static final int MAX_INT_VALUE = 999999;
    
    /**
     * @param args
     */
    public static void main(String[] args) 
    {
        //First Graph Computation
        HashMap<String, Edge> firstGraph = new HashMap<String, Edge>();
        int numberOfNodesG1 = loadGraphFromFile("g1.txt", firstGraph);
        
        int firstGraphComputation = computeFloydWarshallAlgorithm(firstGraph, numberOfNodesG1);
        
        System.out.println("*** Minimum shortest path of the graph1 => " + firstGraphComputation + " ***");
        
        
        //Second Graph Computation
        HashMap<String, Edge> secondGraph = new HashMap<String, Edge>();
        int numberOfNodesG2 = loadGraphFromFile("g2.txt", secondGraph);
        
        int secondGraphComputation = computeFloydWarshallAlgorithm(secondGraph, numberOfNodesG2);
        
        System.out.println("*** Minimum shortest path of the graph2 => " + secondGraphComputation + " ***");
        
        
        //Third Graph Computation
        HashMap<String, Edge> thirdGraph = new HashMap<String, Edge>();
        int numberOfNodesG3 = loadGraphFromFile("g3.txt", thirdGraph);
        
        int thirdGraphComputation = computeFloydWarshallAlgorithm(thirdGraph, numberOfNodesG3);
        
        System.out.println("*** Minimum shortest path of the graph3 => " + thirdGraphComputation + " ***");
    }
    
    /**
     * Computes the shortest path of the grah
     * @param graph The graph
     * @param numberOfNodes The number of nodes
     * @return The shortest path if no negative cycles found, else -1
     */
    private static int computeFloydWarshallAlgorithm(HashMap<String, Edge> graph, int numberOfNodes)
    {
        int shortestPathLength = 0;
        
        //Create the 3D array
        int[][][] A = new int[numberOfNodes][numberOfNodes][numberOfNodes];
        
        //Initialize the matrix as specified in the algorithm
        for(int i = 0; i < numberOfNodes; i++)
        {
            for(int j = 0; j < numberOfNodes; j++)
            {
                for(int k = 0; k < numberOfNodes; k++)
                {
                    if(k == 0)
                    {
                        if(i == j){
                            A[i][j][0] = 0;
                        }
                        else
                        {
                            String key = i + "|" + j;
                            Edge edge = graph.get(key);
                    
                            if(edge != null){
                                A[i][j][0] = edge.getLength();
                            }
                            else{
                                A[i][j][0] = MAX_INT_VALUE;
                            }
                        }
                    }
                    else
                    {
                        if(i == j){
                            A[i][j][k] = 0;
                        }
                        else{
                            A[i][j][k] = MAX_INT_VALUE;
                        }
                    }
                }
            }
        }
        
        //Perform the computation
        for(int k = 1; k < numberOfNodes; k++)
        {  
            for(int i = 0; i < numberOfNodes; i++)  
            {
                for(int j = 0; j < numberOfNodes; j++)
                {
                    int firstItem = A[i][j][k - 1];
                    int secondItem = A[i][k][k - 1] + A[k][j][k - 1];
                    
                    A[i][j][k] = Math.min(firstItem, secondItem);
                }
            }
        }
        
        //Detect negative cycles
        boolean negativeCycles = false;
        
        for(int i = 0; i < numberOfNodes; i++)
        {  
            if(A[i][i][numberOfNodes - 1] < 0)
            {  
                negativeCycles = true;
                break;  
            }  
        } 
        
        //If no negative cycles then compute the shortest path length
        if(!negativeCycles)
        {
            for(int i = 0; i < numberOfNodes; i++)  
            {
                for(int j = 0; j < numberOfNodes; j++)
                {
                    shortestPathLength = Math.min(shortestPathLength, A[i][j][numberOfNodes - 1]);
                }
            }
        }
        else{
            //Return -1 if a negative cycles has been found
            shortestPathLength = -1;
        }
        
        return shortestPathLength;
    }
    
    /**
     * Load a graph from a given file
     * @param path The path of the graph file
     * @return A Matrix with the costs 
     */
    private static int loadGraphFromFile(String path, HashMap<String, Edge> graph)
    {
        int numberOfNodes = 0;
        
        try
        {
            FileInputStream f = new FileInputStream(path);
            DataInputStream d = new DataInputStream(f);
            BufferedReader b =  new BufferedReader(new InputStreamReader(d));
            
            StringTokenizer tokenizer = new StringTokenizer(b.readLine());
 
            numberOfNodes = Integer.parseInt(tokenizer.nextToken());
            int numberOfVertex = Integer.parseInt(tokenizer.nextToken());
            
            int tailNode;
            int headNode;
            int length;
            String str;
            String key;
            Edge edge;
            
            while((str=b.readLine())!=null)
            {
                tokenizer = new StringTokenizer(str);
                
		tailNode = Integer.parseInt(tokenizer.nextToken());
		headNode = Integer.parseInt(tokenizer.nextToken());
                length = Integer.parseInt(tokenizer.nextToken());
                
                key = headNode + "|" + tailNode;
                edge = new Edge(headNode, tailNode, length);
               
                graph.put(key, edge);
            }
        }
        catch(Exception ex){
        } 
        
        return numberOfNodes;
    }
}

/**
 * This class represents an edge of the Graph 
 */
class Edge
{
    private int headNode;
    private int tailNode;
    private int length;
    
    public Edge(int headNode, int tailNode, int length)
    {
        this.headNode = headNode;
        this.tailNode = tailNode;
        this.length = length;
    }

    public int getHeadNode() {
        return headNode;
    }

    public void setHeadNode(int headNode) {
        this.headNode = headNode;
    }

    public int getTailNode() {
        return tailNode;
    }

    public void setTailNode(int tailNode) {
        this.tailNode = tailNode;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}