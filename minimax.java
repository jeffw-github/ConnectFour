import java.util.Random;
import java.lang.Math;
import java.util.ArrayList;

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
	private int maxLayer = 2;
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

		while(!terminate)
		{
			// reset max value
			max = Integer.MIN_VALUE;

			// test for each possible move which one is the most optimal
			for(int move: moves)
			{
				System.out.println("move: " + move);
				if(terminate == true)
				{
					maxLayer--;
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
					System.out.println("newMax: " + newMax + " max: " + max);

					// if move is more optimal than prev:
					if (newMax > max)
					{
						// reset max & column to play
						max = newMax;
						choose = hold;
						System.out.println("choose: " + choose);
					}

					// unmake move
					state.unMakeMove();
				}
			}
			System.out.println("-------------");
			maxLayer++;
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
			else if(winner == state.getActivePlayer())
			{
				return Integer.MIN_VALUE; //if min wins negative reinforcement
			}
			else
				return Integer.MAX_VALUE; //if max wins positive reinforcement
		}
		// if max layer
		if( curLayer == maxLayer )
		{
			// return evaluation(state);
			if(mov >= 3)
				return 40;
			else
				return 30;
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
					if(mov >= 3)
						return 20;
					else
						return 10;
					// break;
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
			else if(winner == state.getActivePlayer())
				return Integer.MAX_VALUE;
			else
				return Integer.MIN_VALUE; //if max wins positive reinforcement
		}
		// if max layer
		if( curLayer == maxLayer )
		{
			// return evaluation(state);
			if(mov >= 3)
				return 20;
			else
				return 10;
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
					if(mov >= 3)
						return 20;
					else
						return 10;
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

		for (int i = 0; i < state.getWidth() ; i++)
		{
			if (state.canMakeMove(i))
				moves[i] = i;
			else
				moves[i] = -1;
		}

		return moves;
	}

}
