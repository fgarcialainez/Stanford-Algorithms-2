/*
 * Programming Question - Week 1, September 2013.
 * 
 * In this programming problem you'll code up Prim's minimum spanning tree algorithm.
 * Download the text file here. This file describes an undirected graph with integer
 * edge costs. It has the format
 * 
 * [number_of_nodes] [number_of_edges]
 * [one_node_of_edge_1] [other_node_of_edge_1] [edge_1_cost]
 * [one_node_of_edge_2] [other_node_of_edge_2] [edge_2_cost]
 * ...
 * 
 * For example, the third line of the file is "2 3 -8874", indicating that there is
 * an edge connecting vertex #2 and vertex #3 that has cost -8874. You should NOT
 * assume that edge costs are positive, nor should you assume that they are distinct.
 * 
 * Your task is to run Prim's minimum spanning tree algorithm on this graph. You should
 * report the overall cost of a minimum spanning tree --- an integer, which may or may
 * not be negative --- in the box below.
 * 
 * IMPLEMENTATION NOTES: This graph is small enough that the straightforward O(mn) time
 * implementation of Prim's algorithm should work fine. OPTIONAL: For those of you seeking
 * an additional challenge, try implementing a heap-based version. The simpler approach, 
 * which should already give you a healthy speed-up, is to maintain relevant edges in a heap
 * (with keys = edge costs). The superior approach stores the unprocessed vertices in the heap,
 * as described in lecture. Note this requires a heap that supports deletions, and you'll probably
 * need to maintain some kind of mapping between vertices and their positions in the heap.
 */
package com.stanford.algorithms.parttwo.weekone;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Algorithms: Design and Analysis, Part 2
 * Programming Question - Week 1
 * @author Felix Garcia Lainez
 */
public class Question3 
{
    private static int[][] graph = null;
    private static int numberOfNodes = 0;
    private static int[] spanningTree = null;
    private static boolean[] expandedNodes = null;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        //READ THE GRAPH
        readGraphFromFile();
        
        System.out.println("*** Overall Cost => " + primMSTAlgorithm() + " ***");
    }
    
    /**
     * Running time O(mn)
     */
    private static int primMSTAlgorithm()
    {
	int overallCost = 0;
        
        expandedNodes[0] = true;

	int minimumCost = Integer.MAX_VALUE;
		
        int minimumI = 0;
	int minimumJ = 0;
		
        while(!allNodesExpanded())
        {
            for(int i = 0; i < numberOfNodes; i++)
            {
		if(!expandedNodes[i])
                {
                    for(int j = 0; j < numberOfNodes; j++)
                    {
                        if(expandedNodes[j])
                        {
                            if(minimumCost > graph[i][j])
                            {
                                minimumCost = graph[i][j];
                                minimumI = i;
                                minimumJ = j;
                            }					
                        }
                    }
                }
            }
			
            expandedNodes[minimumI] = true;
            spanningTree[minimumJ] = minimumI;
            overallCost += graph[minimumI][minimumJ];
            minimumCost = Integer.MAX_VALUE;
	}

        return overallCost;
    }
    
    private static boolean allNodesExpanded()
    {
        boolean allExpanded = true;
        
	for(boolean expanded : expandedNodes)
        {
            if(!expanded)
            {
                allExpanded = false;
                break;
            }
	}
	
        return allExpanded;
    }
    
    /**
     * Reads the Job list 
     * @return A list of jobs 
     */
    private static void readGraphFromFile()
    {
        FileInputStream fstream = null;
        try {
            fstream = new FileInputStream("edges.txt");
            
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            
            String line = br.readLine();
            StringTokenizer tokens = new StringTokenizer(line);
            numberOfNodes = Integer.parseInt(tokens.nextToken());
             
            //INITIALIZE THE GRAPH
            graph = new int[numberOfNodes][numberOfNodes];
            for (int i = 0; i < numberOfNodes; i++){
		for(int j = 0; j < numberOfNodes; j++){
                    graph[i][j] = Integer.MAX_VALUE;
                }
            }
            
            while ((line = br.readLine()) != null)
            {
                // process the line
                tokens = new StringTokenizer(line);
                
                String firstToken = tokens.nextToken();
                String secondToken = tokens.nextToken();
                String thirdToken = tokens.nextToken();
                
                int i = Integer.parseInt(firstToken) - 1;
		int j = Integer.parseInt(secondToken) - 1;
		graph[i][j] = Integer.parseInt(thirdToken);
		graph[j][i] = Integer.parseInt(thirdToken);  
            }
                     
            spanningTree = new int[numberOfNodes];
            for(int i = 0; i < numberOfNodes; i++){
		spanningTree[i] = -1;
            }
            
            expandedNodes = new boolean[numberOfNodes];	
            for(int i = 0; i < numberOfNodes; i++){
                expandedNodes[i] = false;
            }
            
            br.close();
        }catch (FileNotFoundException ex) {
            Logger.getLogger(Question1.class.getName()).log(Level.SEVERE, null, ex);
        }catch (IOException ex) {
            Logger.getLogger(Question1.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            try {
                fstream.close();
            } catch (IOException ex) {
                Logger.getLogger(Question1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
