import java.util.Random;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Scanner;

// minimax alogrithm
// decision funciton
//    for every possible move:
// 		call max(move)
// 	  choose optimal move

// max
// 	  if reach terminal state:
//		return utility
//    else:
//  	for every possible successor move:
//			cur = math.max(cur, min(move))
//    return cur

// min
// 	  if reach terminal state:
//		return utility
//    else:
//  	for every possible successor move:
//			cur = math.min(cur, max(move))
//    return cur

public class minimax extends AIModule
{
	private int maxLayer = 6;
	private int p;

	@Override
	public void getNextMove(final GameStateModule state)
	{
		// generate all legal moves for the given state
		int moves[] = legalMoves(state);

		// hold final move to play
		int choose = -1;

		// max eval score (for most optimal move)
		int max = Integer.MIN_VALUE;


		// define current player*
		final int p = state.getActivePlayer();
		// System.out.println("AI called");

		while(!terminate)
		{
			// reset max value
			max = Integer.MIN_VALUE;
			// test for each possible move which one is the most optimal
			for(int move: moves)
			{
				// System.out.println("move: " + move);
				if(terminate == true)
				{
					// maxLayer--;
					return;
				}

				// if move is valid
				if(move > -1)
				{
					// hold original move (column number)
					int hold = move;

					// make move
					state.makeMove(move);

					// generate max value:
					int newMax = minValue(state, 1, move);
					// System.out.println("newMax: " + newMax + " max: " + max);

					// if move is more optimal than prev:
					if (newMax > max)
					{
						// reset max & column to play
						max = newMax;
						choose = hold;
						// System.out.println("choose: " + choose);
					}

					// unmake move
					state.unMakeMove();
				}
			}
			// System.out.println("-------------");
			// maxLayer++; // work as intended without this and maxlayer--
			//error if two ways to win
			chosenMove = choose; //set our move
		}
	}

	// minVal
	private int minValue(final GameStateModule state, int curLayer, int mov)
	{
		// if terminal state
		if(state.isGameOver())
		{
			int winner = state.getWinner();
			if(winner == 0)
				return 0; //if game ended in draw, no reinforcement
			else
				return Integer.MAX_VALUE;
		}
		// if max layer
		if( curLayer == maxLayer )
		{
			// return evaluation(state);
			return Integer.MIN_VALUE + 1;
		}

		// min
		// generate all legal moves for the given state
		int moves[] = legalMoves(state);

		int min =  Integer.MAX_VALUE;

		for(int move : moves)
		{
			if(terminate == true)
	      {
					// return Math.min(min, evaluation(state));
					// return 30;
					// break;
					return Integer.MIN_VALUE + 1;
	      }//if we have run out of time

			if( move > -1)
			{
				state.makeMove(move);
				min =  Math.min(min, maxValue(state, curLayer + 1, mov));
				state.unMakeMove();
			}
		}
		return min;
	}

	// minVal
	private int maxValue(final GameStateModule state, int curLayer, int mov)
	{
		// if terminal state
		if(state.isGameOver())
		{
			int winner = state.getWinner();
			if(winner == 0)
				return 0; //if game ended in draw, no reinforcement
			else
				return Integer.MIN_VALUE + 1;
		}
		// if max layer
		if( curLayer == maxLayer )
		{
			// return evaluation(state);
				return Integer.MAX_VALUE;
		}

		// min
		// generate all legal moves for the given state
		int moves[] = legalMoves(state);

		int max = Integer.MIN_VALUE;

		for(int move:moves)
		{
			if(terminate == true)
        {
					// return Math.max(max, evaluation(state));
					return Integer.MIN_VALUE + 1;
					// break;
        }//if we have run out of time

			if( move > -1)
			{
				state.makeMove(move);
				max =  Math.max(max, minValue(state, curLayer + 1, mov));
				state.unMakeMove();
			}
		}
		return max;
	}

	// generate all possible legal moves within a given state
	private int[] legalMoves(final GameStateModule state)
	{
		int moves[] = new int[state.getWidth()];
		//initialize order to play moves
		if(state.getWidth() == 7){
			int order[] = {3, 2, 4, 1, 5, 0, 6};
			moves = order;
		}
		else{
			int order[] = {3, 4, 2, 5, 1, 6, 0, 7};
			moves = order;
		}


		for (int i = 0; i < state.getWidth(); i++)
		{
			if (!state.canMakeMove(i)){ //if move is not valid set as -1
				for(int j = 0; j < state.getWidth(); j++)
				{
					if(i == moves[j])
					{
						moves[j] = -1;
					}
				}
			}
		}

		return moves;
	}

}
