/*
 * Programming Question - Week 5, September 2013.
 * 
 * In this assignment you will implement one or more algorithms for the traveling
 * salesman problem, such as the dynamic programming algorithm covered in the video
 * lectures. Here is a data file describing a TSP instance. The first line indicates
 * the number of cities. Each city is a point in the plane, and each subsequent line
 * indicates the x- and y-coordinates of a single city.
 * 
 * The distance between two cities is defined as the Euclidean distance --- that is,
 * two cities at locations (x,y) and (z,w) have distance (x−z)2+(y−w)2‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾√ 
 * between them.
 * 
 * In the box below, type in the minimum cost of a traveling salesman tour for this 
 * instance, rounded down to the nearest integer.
 * 
 * OPTIONAL: If you want bigger data sets to play with, check out the TSP instances 
 * from around the world here. The smallest data set (Western Sahara) has 29 cities,
 * and most of the data sets are much bigger than that. What's the largest of these 
 * data sets that you're able to solve --- using dynamic programming or, if you like, 
 * a completely different method?
 * 
 * HINT: You might experiment with ways to reduce the data set size. For example, 
 * trying plotting the points. Can you infer any structure of the optimal solution? 
 * Can you use that structure to speed up your algorithm?
 */
package com.stanford.algorithms.parttwo.weekfive;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Algorithms: Design and Analysis, Part 2
 * Programming Question - Week 5
 * @author Felix Garcia Lainez
 * 
 * @see http://en.wikipedia.org/wiki/User:LIU_CS_MUN/draft_of_the_page_for_Held-Karp_algorithm
 */
public class Question1
{
    // number of cities
    static int nCities;
    
    // cost matrix
    static double[][] costMatrix;
  
    static double[][] costWithPi;
    static Node bestNode = new Node();
  
    /**
     * Main Method
     */
    public static void main(String[] args)
    {
        //Load cities from input file
        List<City> citiesArray = loadCitiesArrayFromFile();
        
        //Calculate adjacencry cost matrix
        createDistancesMatrixArrayFromCities(citiesArray);
        
        System.out.println("Processing using Held–Karp algorithm [O(n^2 * 2^n)]...");
        System.out.println("*** The minimum cost of a traveling salesman tour for this instance is => " + processTSP() + " ***");
    }
    
    /**
     * Creates the distances matrix from the initial cities array 
     */
    private static void createDistancesMatrixArrayFromCities(List<City> citiesArray)
    {
        costMatrix = new double[nCities][nCities];
        
        for(int i = 0; i < nCities; i++)
        {
            City city = citiesArray.get(i);
            
            for(int j = 0; j < nCities; j++)
            {
                City otherCity = citiesArray.get(j);
                
                costMatrix[i][j] = city.calculateDistanceToCity(otherCity);
            }
        }
    }
    
    /**
     * Loads the City array from an input file
     */
    private static List<City> loadCitiesArrayFromFile()
    {
        ArrayList<City> citiesArray = new ArrayList<City>();
        
        try
        {
            FileInputStream f = new FileInputStream("tsp.txt");
            DataInputStream d = new DataInputStream(f);
            BufferedReader br =  new BufferedReader(new InputStreamReader(d));
        
            nCities = Integer.parseInt(br.readLine());
            
            for(int i = 0; i < nCities; i++)
            {
                String line = br.readLine();
                
                double xCoordinate = Double.valueOf(line.split(" ")[0]);
                double yCoordinate = Double.valueOf(line.split(" ")[1]);
                
                citiesArray.add(new City(xCoordinate, yCoordinate));
            }
        }
        catch(Exception e){
        }
        
        return citiesArray;
    }
    
    /**
     * Process the travel salesman problem 
     * @return The shortest path length
     */
    private static double processTSP() 
    {
        bestNode.lowerBound = Double.MAX_VALUE;
        Node currentNode = new Node();
        currentNode.excluded = new boolean[nCities][nCities];
        costWithPi = new double[nCities][nCities];
        currentNode.computeHeldKarp();
        
        PriorityQueue<Node> pq = new PriorityQueue<Node>(nCities, new NodeComparator());
        
        do{
            do{
                int i = -1;
                for(int j = 0; j < nCities; j++) 
                {
                    if(currentNode.degree[j] > 2 && (i < 0 || currentNode.degree[j] < currentNode.degree[i])){
                        i = j;
                    }
                }
                
                if(i < 0){
                    if(currentNode.lowerBound < bestNode.lowerBound) {
                        bestNode = currentNode;
                    }
                    break;
                }
                
                PriorityQueue<Node> children = new PriorityQueue<Node>(nCities, new NodeComparator());
                children.add(currentNode.exclude(i, currentNode.parent[i]));
                
                for(int j = 0; j < nCities; j++) 
                {
                    if(currentNode.parent[j] == i) {
                        children.add(currentNode.exclude(i, j));
                    }
                }
                
                currentNode = children.poll();
                pq.addAll(children);
            } while(currentNode.lowerBound < bestNode.lowerBound);
            
            currentNode = pq.poll();
        } while (currentNode != null && currentNode.lowerBound < bestNode.lowerBound);
        
        return bestNode.lowerBound;
    }
    
    /**
     * Represents a single Node
     */
    static class Node 
    {
        boolean[][] excluded;
        
        // Held--Karp solution
        double[] pi;
        double lowerBound;
        int[] degree;
        int[] parent;
             
        public void computeHeldKarp() 
        {
            this.pi = new double[nCities];
            this.lowerBound = Double.MIN_VALUE;
            this.degree = new int[nCities];
            this.parent = new int[nCities];
            
            double lambda = 0.1;
            while(lambda > 1e-06) 
            {
                double previousLowerBound = this.lowerBound;
                computeOneTree();
                
                if(!(this.lowerBound < bestNode.lowerBound)) {
                    return;
                }
                
                if(!(this.lowerBound < previousLowerBound)) {
                    lambda *= 0.9;
                }
                
                int denom = 0;
                for(int i = 1; i < nCities; i++) 
                {
                    int d = this.degree[i] - 2;
                    denom += d * d;
                }
                
                if(denom == 0){
                    return;
                }
                
                double t = lambda * this.lowerBound / denom;
                for(int i = 1; i < nCities; i++){
                    this.pi[i] += t * (this.degree[i] - 2);
                }
            }
        }
           
        /////////////////////////////////////////////////////////////////////
        //////////////////////////  AUXILIAR METHODS  //////(////////////////
        /////////////////////////////////////////////////////////////////////
        private Node exclude(int i, int j)
        {
            Node child = new Node();
            child.excluded = this.excluded.clone();
            child.excluded[i] = this.excluded[i].clone();
            child.excluded[j] = this.excluded[j].clone();
            child.excluded[i][j] = true;
            child.excluded[j][i] = true;

            child.computeHeldKarp();

            return child;
        }
        
        private void addEdge(int i, int j)
        {
            double q = this.lowerBound;
            this.lowerBound += costWithPi[i][j];
            this.degree[i]++;
            this.degree[j]++;
        }
        
        private void computeOneTree() 
        {
            // compute adjusted costs
            this.lowerBound = 0.0;
            Arrays.fill(this.degree, 0);
            for (int i = 0; i < nCities; i++) {
                for (int j = 0; j < nCities; j++) {
                    costWithPi[i][j] = this.excluded[i][j] ? Double.MAX_VALUE : costMatrix[i][j] + this.pi[i] + this.pi[j];
                }
            }

            int firstNeighbor;
            int secondNeighbor;

            // find the two cheapest edges from 0
            if (costWithPi[0][2] < costWithPi[0][1]) {
                firstNeighbor = 2;
                secondNeighbor = 1;
            } else {
                firstNeighbor = 1;
                secondNeighbor = 2;
            }

            for (int j = 3; j < nCities; j++) {
                if (costWithPi[0][j] < costWithPi[0][secondNeighbor]) {
                    if (costWithPi[0][j] < costWithPi[0][firstNeighbor]) {
                        secondNeighbor = firstNeighbor;
                        firstNeighbor = j;
                    } else {
                        secondNeighbor = j;
                    }
                }
            }

            addEdge(0, firstNeighbor);
            Arrays.fill(this.parent, firstNeighbor);
            this.parent[firstNeighbor] = 0;

            //compute the minimum spanning tree on nodes 1..n-1
            double[] minCost = costWithPi[firstNeighbor].clone();

            for (int k = 2; k < nCities; k++) {
                int i;
                for (i = 1; i < nCities; i++) {
                    if (this.degree[i] == 0) {
                        break;
                    }
                }

                for (int j = i + 1; j < nCities; j++) {
                    if (this.degree[j] == 0 && minCost[j] < minCost[i]) {
                        i = j;
                    }
                }

                addEdge(this.parent[i], i);
                for (int j = 1; j < nCities; j++) {
                    if (this.degree[j] == 0 && costWithPi[i][j] < minCost[j]) {
                        minCost[j] = costWithPi[i][j];
                        this.parent[j] = i;
                    }
                }
            }

            addEdge(0, secondNeighbor);
            this.parent[0] = secondNeighbor;
        }
    }

    /**
     * Node comparator class to be used in PriorityQueue 
     */
    static class NodeComparator implements Comparator<Node>
    {
        @Override
        public int compare(Node a, Node b) {
            return Double.compare(a.lowerBound, b.lowerBound);
        }
    }
    
    /**
     * This class represents de coordinates of a C
     */
    static class City
    {
        double xCoordinate;
        double yCoordinate;
        
        public City(double xCoordinate, double yCoordinate){
            this.xCoordinate = xCoordinate;
            this.yCoordinate = yCoordinate;
        }
        
        /**
         * Calculates the distance to other city 
         */
        public double calculateDistanceToCity(City other){
            return Math.sqrt(Math.pow(xCoordinate - other.xCoordinate, 2) + Math.pow(yCoordinate - other.yCoordinate, 2));
        }
    }
}