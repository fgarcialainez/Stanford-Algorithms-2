/*
 * Programming Question - Week 2, September 2013.
 * 
 * In this question your task is again to run the clustering algorithm from lecture,
 * but on a MUCH bigger graph. So big, in fact, that the distances (i.e., edge costs)
 * are only defined implicitly, rather than being provided as an explicit list.
 * 
 * The data set is here. The format is:
 * 
 * [# of nodes] [# of bits for each node's label]
 * [first bit of node 1] ... [last bit of node 1]
 * [first bit of node 2] ... [last bit of node 2]
 * ...
 * 
 * For example, the third line of the file "0 1 1 0 0 1 1 0 0 1 0 1 1 1 1 1 1 0 1 0 1 1 0 1"
 * denotes the 24 bits associated with node #2.
 * 
 * The distance between two nodes u and v in this problem is defined as the Hamming 
 * distance--- the number of differing bits --- between the two nodes' labels. For example,
 * the Hamming distance between the 24-bit label of node #2 above and the label 
 * "0 1 0 0 0 1 0 0 0 1 0 1 1 1 1 1 1 0 1 0 0 1 0 1" is 3 (since they differ in the 3rd, 7th,
 * and 21st bits).
 * 
 * The question is: what is the largest value of k such that there is a k-clustering with 
 * spacing at least 3? That is, how many clusters are needed to ensure that no pair of nodes 
 * with all but 2 bits in common get split into different clusters?
 * 
 * NOTE: The graph implicitly defined by the data file is so big that you probably can't 
 * write it out explicitly, let alone sort the edges by cost. So you will have to be a little
 * creative to complete this part of the question. For example, is there some way you can 
 * identify the smallest distances without explicitly looking at every pair of nodes?
 */
package com.stanford.algorithms.parttwo.weektwo;

/**
 * Algorithms: Design and Analysis, Part 2
 * Programming Question - Week 2
 * @author Felix Garcia Lainez
 */
public class Question2
{
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        
    }
}