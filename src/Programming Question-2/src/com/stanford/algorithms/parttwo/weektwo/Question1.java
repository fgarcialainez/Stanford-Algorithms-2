/*
 * Programming Question - Week 2, September 2013.
 * 
 * In this programming problem and the next you'll code up the clustering algorithm 
 * from lecture for computing a max-spacing k-clustering. Download the text file here.
 * This file describes a distance function (equivalently, a complete graph with edge
 * costs). It has the following format:
 *
 * [number_of_nodes]
 * [edge 1 node 1] [edge 1 node 2] [edge 1 cost]
 * [edge 2 node 1] [edge 2 node 2] [edge 2 cost]
 * ...
 * 
 * There is one edge (i,j) for each choice of 1≤i<j≤n, where n is the number of nodes.
 * For example, the third line of the file is "1 3 5250", indicating that the distance
 * between nodes 1 and 3 (equivalently, the cost of the edge (1,3)) is 5250. You can 
 * assume that distances are positive, but you should NOT assume that they are distinct.
 * 
 * Your task in this problem is to run the clustering algorithm from lecture on this 
 * data set, where the target number k of clusters is set to 4. What is the maximum 
 * spacing of a 4-clustering?
 * 
 * ADVICE: If you're not getting the correct answer, try debugging your algorithm using
 * some small test cases. And then post them to the discussion forum!
 */
package com.stanford.algorithms.parttwo.weektwo;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Algorithms: Design and Analysis, Part 2
 * Programming Question - Week 2
 * @author Felix Garcia Lainez
 */
public class Question1
{
    private static int k = 4;
    private static int numberOfEdges = 0;
    private static int[] parents;
	
    /**
     * @param args
     */
    public static void main(String[] args) 
    {
        List<Edge> edgesArray = readEdgesArrayFromFile();
        
        Collections.sort(edgesArray);
        
        QuickUnionPathCompressionUF unionFind = new QuickUnionPathCompressionUF(numberOfEdges);
        
        for(Edge e : edgesArray)
        {
            unionFind.union(e.getI(), e.getJ());
 				
            if(unionFind.count() == k){
                break;				
            }
        }
        
        int max = Integer.MAX_VALUE;
			
        for(Edge e : edgesArray)
        {
            if(unionFind.find(e.getI()) != unionFind.find(e.getJ())){
                max = Math.min(max, e.getCost());				
            }
	}
	
        System.out.println("Max-Spacing K-Clustering => " + max);
    }
    
    /**
     * Read the data of the Graph to be used in the assignment
     * @return A list of Edge 
     */
    private static ArrayList<Edge> readEdgesArrayFromFile()
    {
        ArrayList<Edge> edgesArray;
        
        try
        {
            FileInputStream f = new FileInputStream("clustering1.txt");
            DataInputStream d = new DataInputStream(f);
            BufferedReader b =  new BufferedReader(new InputStreamReader(d));
            
            numberOfEdges = Integer.parseInt(b.readLine());
            edgesArray = new ArrayList<Edge>(numberOfEdges);
            
            parents = new int[numberOfEdges];			

            for(int i = 0; i < numberOfEdges; i++){
                parents[i] = -1;				
            }
            
            String str; 
            StringTokenizer tokenizer;
            int i, j, v;
			
            while((str=b.readLine())!=null)
            {
                tokenizer = new StringTokenizer(str);
                
		i = Integer.parseInt(tokenizer.nextToken());
		j = Integer.parseInt(tokenizer.nextToken());
		v = Integer.parseInt(tokenizer.nextToken());
		edgesArray.add(new Edge(i - 1, j - 1, v));
            }
        }
        catch(Exception ex){
            edgesArray = new ArrayList<com.stanford.algorithms.parttwo.weektwo.Edge>();
        } 
        
        return edgesArray;
    }
}	

/**
 * Represents an Edge of the Graph
 */
class Edge implements Comparable<Edge>
{
    private int i;
    private int j;
    private int cost;
		
    public Edge(int i, int j, int cost)
    {
	this.i = i;
	this.j = j;
	this.cost = cost;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
    
    @Override
    public int compareTo(Edge edge) 
    {
        int result;
        
        if(this.getCost() >= edge.getCost()){
            result = 1;
        }
        else{
            result = -1;
        }
        
        return result;
    }
}