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
//				no.1: legal squares for black king		
		parVector.add(0.5);
//				no.2: 	
	}
	
	public void learnOnData()
	{
		System.out.println("finished");
			// updating the parameters	

		double sumGradJ = sumGradJ();
				System.out.print("C");
		updateParameters(learningRate, sumGradJ);
	}

	private double sumGradJ()
	{
		double sumGradients = 0;
	
		for(int i=0; i<pastStates.size();i++)
		{
			sumGradients += calcGradJ(pastStates.get(i), i);
									System.out.print("D");
		}
		return sumGradients;
	}
	
	private double calcGradJ(BoardPosition pos, int t)
	{
		double gradJ = 0;
		
		Chessboard thisBoard = new Chessboard(pos);
		Moves thisMoves = new Moves(thisBoard);
		FeatureCalculation thisFC = new FeatureCalculation(thisBoard, thisMoves);

		double correctiondt = calcCorrectiondt(pos, t);
		
		// Feature no.1
		gradJ += thisFC.noOfPosSquaresk() * correctiondt;
		
		return gradJ;
	}
	
	private double calcCorrectiondt(BoardPosition pos, int t)
	{
		double correctiondt = 0;
		
		for(int j=t;j<pastStates.size();j++)
		{
			correctiondt += Math.pow(lambda, j-t) * tempDif(pos); 
		}
		
		return correctiondt;
	}
	
	
	// TODO: can be optemize, double code, double code everywhere...
	private double tempDif(BoardPosition pos)
	{
		double tempDif = 0;
		
		ArrayList<BoardPosition> allPosNextMoves;
		Chessboard tempChessBoard = new Chessboard(pos);
		Moves tempMoves = new Moves(tempChessBoard);
		AllNextStates tempAllNext = new AllNextStates(tempChessBoard, tempMoves);
		
		allPosNextMoves = tempAllNext.findAllPosNextStates();
		
		BoardPosition bestMove = rewardFunction.findHighestReward(allPosNextMoves);

		tempDif = rewardFunction.calcReward(bestMove) - rewardFunction.calcReward(pos);	
			
		return tempDif;
	}
	
	// TODO: make this vector multiplication
	private void updateParameters(double learningRate, double sumGradJ)
	{
		double feature = parVector.get(0);
		feature += learningRate*sumGradJ;
		parVector.set(0, feature);
		System.out.print("B");
	}
}







