/*
 * Programming Question - Week 3, September 2013.
 * 
 * In this programming problem and the next you'll code up the knapsack algorithm
 * from lecture. Let's start with a warm-up. Download the text file here. This file
 * describes a knapsack instance, and it has the following format:
 * 
 * [knapsack_size][number_of_items]
 * [value_1] [weight_1]
 * [value_2] [weight_2]
 * ...
 * 
 * For example, the third line of the file is "50074 659", indicating that the second
 * item has value 50074 and size 659, respectively.
 * You can assume that all numbers are positive. You should assume that item weights 
 * and the knapsack capacity are integers.
 * 
 * In the box below, type in the value of the optimal solution.
 * 
 * ADVICE: If you're not getting the correct answer, try debugging your algorithm using
 * some small test cases. And then post them to the discussion forum!
 */
package com.stanford.algorithms.parttwo.weekthree;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Algorithms: Design and Analysis, Part 2
 * Programming Question - Week 3
 * @author Felix Garcia Lainez
 */
public class Question1
{
    private static int n = 0; //number of items
    private static int w = 0; //knapsack size
    private static ArrayList<KnapsackItem> itemsArray = new ArrayList<KnapsackItem>();
    
    /**
     * @param args
     */
    public static void main(String[] args) 
    {
        //LOAD DATA FROM FILE
        loadDataFromFile();
        
        //INITIALIZATION
        int[][] A = new int[n + 1][w + 1];
        
        for(int j = 0; j <= w; j++){
            A[0][j] = 0;
        }
        
        //MAIN LOOP
        for(int i = 1; i <= n; i++)
        {
            KnapsackItem currentItem = itemsArray.get(i - 1);
            
            for(int x = 0; x <= w; x++)
            {
                int firstItem = A[i - 1][x];
                
                int secondItem = 0;
    
                if(x - currentItem.getWeight() >= 0 && x - currentItem.getWeight() <= w){
                    secondItem = A[i - 1][x - currentItem.getWeight()] + currentItem.getValue();
                }
                
                A[i][x] = Math.max(firstItem, secondItem);
            }
        }
        
        System.out.println("*** Optimal Solution => " + A[n][w] + " ***");
    }
    
    /**
     * Load the data of the problem from a given file
     */
    private static void loadDataFromFile()
    {
        try
        {
            FileInputStream f = new FileInputStream("knapsack1.txt");
            DataInputStream d = new DataInputStream(f);
            BufferedReader b =  new BufferedReader(new InputStreamReader(d));
            
            StringTokenizer tokenizer = new StringTokenizer(b.readLine());
 
            w = Integer.parseInt(tokenizer.nextToken());
            n = Integer.parseInt(tokenizer.nextToken());
            
            int value;
            int weight;
            String str;
            
            while((str=b.readLine())!=null)
            {
                tokenizer = new StringTokenizer(str);
                
		value = Integer.parseInt(tokenizer.nextToken());
		weight = Integer.parseInt(tokenizer.nextToken());

                itemsArray.add(new KnapsackItem(value, weight));
            }
        }
        catch(Exception ex){
        } 
    }
}