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
	
	public void initiateParVector()
	{
//			FEATURE Weights:
//				no.1: terminal state
		//parVector.add(0.5);
//				no.2: legal squares for black king		
		parVector.add(0.5);	
//				no.3: dist black king to edge		
		parVector.add(0.5);	
//				no.4 rook checks black king
		parVector.add(0.5);
				
	}
	
	public void learnOnData()
	{
		System.out.println("finished");
			// updating the parameters	

		double[] sumGradJ = sumGradJ();

		updateParameters(sumGradJ);
		
		normaliseWeights();
	}

	private double[] sumGradJ()
	{
		double[] sumGradients = new double[parVector.size()];
	
		for(int j=0; j<parVector.size();j++)
		{
			for(int i=0; i<pastStates.size()-1;i++)
			{
				sumGradients[j] += calcGradJ(pastStates.get(i), i, j);
			}
			System.out.print("D");
		}
		return sumGradients;
	}
	
	private double calcGradJ(BoardPosition pos, int t, int feature)
	{
		double gradJ = 0;
		
		Chessboard thisBoard = new Chessboard(pos);
		FeatureCalculation thisFC = new FeatureCalculation(thisBoard);

		double correctiondt = calcCorrectiondt(pos, t);
		
		// Feature no.1
		if(feature == 0){gradJ += thisFC.noOfPosSquaresk() * correctiondt;}
		if(feature == 1){gradJ += thisFC.distanceToEdgeBlackKing() * correctiondt;}
		if(feature == 2){gradJ += thisFC.rookChecksBlackKing() * correctiondt;}
		 
		return gradJ;
	}
	
	private double calcCorrectiondt(BoardPosition pos, int t)
	{
		double correctiondt = 0;
		
		for(int j=t;j<pastStates.size()-1;j++)
		{
			correctiondt += Math.pow(lambda, j-t) * tempDif(j); 
		}
		
		return correctiondt;
	}
	
	
	// TODO: can be optemized, double code, double code everywhere...
	private double tempDif(int j)
	{
		return rewardFunction.featureValues.get(j+1) - rewardFunction.featureValues.get(j);
	}
	
	// TODO: make this vector multiplication
	private void updateParameters(double[] sumGradJ)
	{
		double feature;
		for(int i=0; i<parVector.size();i++)
		{
			feature = parVector.get(i);
			feature += learningRate*sumGradJ[i];
			parVector.set(i, feature);
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







