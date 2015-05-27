/*
 * File: LearnOnData.java
 * -------------------
 * Name: Daniel Staal
 * 
 * This file will eventually implement the game of Chess.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;


/////////////////////////////////////////////////////////////////////////
// Data Learning
/////////////////////////////////////////////////////////////////////////

public class UpdatingParameters extends ConsoleProgram
{		
	/* learning parameters */
	private static final double lambda = 0.7;
	private static final double learningRate = 0.7;
	
	private ArrayList<Double> parVector;
	private ArrayList<BoardPosition> pastStates;
	private RewardFunction rewardFunction;
	
	public UpdatingParameters(ArrayList<Double> pV, ArrayList<BoardPosition> pS, RewardFunction rF)
	{
		parVector = pV;
		pastStates = pS;
		rewardFunction = rF;
	}
	
	public void initiateParVector(int numOfFeatures)
	{
		for(int i=0;i<numOfFeatures;i++)
		{	
			parVector.add(-0.1);
			// to keep track of all calculated feature values	
			rewardFunction.featureValues.add(new ArrayList<Double>());
		}		
	}
	
	public void learnOnData()
	{
		double[] sumGradJ = sumGradJ();

		updateParameters(sumGradJ);
		
		//normaliseWeights();
		
//		for(int i=0;i<rewardFunction.featureValues.get(0).size();i++)
//		{
//			System.out.println(rewardFunction.featureValues.get(0).get(i));
//		}
	}

	private double[] sumGradJ()
	{
		double[] sumGradients = new double[parVector.size()];
	
		// update every weight
		for(int weight=0; weight<parVector.size();weight++)
		{
			// For each past state calc the gradient * correctiondt
			for(int t=0; t<pastStates.size();t++)
			{
				sumGradients[weight] += calcGradJ(t, weight);
				
				//System.out.print(t);System.out.print(" gradient value:");
				//System.out.println(sumGradients[weight]);
			}
		}
		return sumGradients;
	}
	
	private double calcGradJ(int t, int indexFeature)
	{
		double gradJ = 0;

		double correctiondt = calcCorrectiondt(t);

		gradJ = rewardFunction.featureValues.get(indexFeature).get(t) * 			correctiondt;
		 
		return gradJ;
	}
	
	private double calcCorrectiondt(int t)
	{
		double correctiondt = 0;
		
		
		for(int j=t;j<pastStates.size();j++)
		{
			correctiondt += Math.pow(lambda, j-t) * tempDif(j); 
		}
		
		return correctiondt;
	}
	
	
	// TODO: can be optemized, double code, double code everywhere...
	private double tempDif(int j)
	{	
		return rewardFunction.stateValues.get(j+1) - 				 				rewardFunction.stateValues.get(j);
	}
	
	private void updateParameters(double[] sumGradJ)
	{

		for(int i=0; i<parVector.size();i++)
		{
			double weightValue = parVector.get(i);

			//System.out.print("weightValue:");System.out.println(parVector.get(i));
			parVector.set(i, weightValue + learningRate*sumGradJ[i]);
		}
	}
	
	private void normaliseWeights()
	{		
		double cumulativeSquares = 0;
		
		for(int i=0;i<parVector.size();i++)
		{
			cumulativeSquares += Math.pow(parVector.get(i), 2);
		}
		
		double vectorMagnitude = Math.sqrt(cumulativeSquares);
		
		for(int i=0;i<parVector.size();i++)
		{
			parVector.set(i, parVector.get(i)/vectorMagnitude);
		}
	}
}







