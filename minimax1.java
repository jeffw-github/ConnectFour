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

public class minimax1 extends AIModule
{
	Scanner scanner = new Scanner(System.in);
	private int maxLayer = 2;
	private int p, width, height;

	@Override
	public void getNextMove(final GameStateModule state)
	{
		width = state.getWidth();
		height = state.getHeight();

		// define private variables
		p = state.getActivePlayer();

		// generate all legal moves for the given state
		int moves[] = legalMoves(state);

		// hold final move to play
		int choose = -1;

		// max eval score (for most optimal move)
		int max = Integer.MIN_VALUE;


		while(!terminate)
		{
			// reset max value
			max = Integer.MIN_VALUE;

			// test for each possible move which one is the most optimal
			for (int move: moves)
			{
				if(terminate == true)
          {
						// maxLayer--;
          	return;
          }

				// if move is valid
				if(move != -1)
				{
					int hold = move;

					state.makeMove(move);

					int newMax = minValue(state, 1);

					// if move is more optimal than prev:
					if (newMax > max)
					{
						max = newMax;
						choose = hold;
					}

					state.unMakeMove();
				}

				// System.out.println("Choose move[" + choose + "] because score is " + max);
			}
			// maxLayer++;
			chosenMove = choose; //set our move
			// System.out.println("Chose column " + chosenMove);
			// System.out.println("Move Score: " + max);
			// System.out.println("---------------------------");
		}

	}

	// minVal
	private int minValue(final GameStateModule state, int curLayer)
	{
		// if terminal state
		if(state.isGameOver())
		{
			int winner = state.getWinner();
			if(winner == 0)
				return 0; //if game ended in draw, no reinforcement
			else if(winner == state.getActivePlayer())
				return Integer.MIN_VALUE; //if min wins negative reinforcement
			else
				return Integer.MAX_VALUE; //if max wins positive reinforcement
		}
		// if max layer
		if( curLayer == maxLayer )
		{
			return evaluation(state);
		}

		// min
		// generate all legal moves for the given state
		int moves[] = legalMoves(state);

		int min =  Integer.MAX_VALUE;

		for(int move:moves)
		{
			if(terminate == true)
        {
					return Math.min(min, evaluation(state));
        }//if we have run out of time

			if( move != -1)
			{
				//System.out.println(" evaluate move");
				state.makeMove(move);
				min =  Math.min(min, maxValue(state, curLayer + 1));
				state.unMakeMove();
			}
		}
		return min;
	}

	// maxVal
	private int maxValue(final GameStateModule state, int curLayer)
	{
		// if terminal state
		if(state.isGameOver())
		{
			int winner = state.getWinner();
			if(winner == 0)
				return 0; //if game ended in draw, no reinforcement
			else if(winner == state.getActivePlayer())
				return Integer.MAX_VALUE; //if min wins negative reinforcement
			else
				return Integer.MIN_VALUE; //if max wins positive reinforcement
		}
		// if max layer
		if( curLayer == maxLayer )
		{
			return evaluation(state);
		}

		// min
		// generate all legal moves for the given state
		int moves[] = legalMoves(state);

		int max =  Integer.MIN_VALUE;

		for(int move:moves)
		{
			if(terminate == true)
        {
					return Math.max(max, evaluation(state));
        }//if we have run out of time

			if( move != -1)
			{
				state.makeMove(move);
				max =  Math.max(max, minValue(state, curLayer + 1));
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
			{
				moves[i] = i;
			}
			else
				moves[i] = -1;
		}

		return moves;
	}

	private int evaluation(final GameStateModule state)
	{
		int ans = 0;

		for(int i = 0; i < height; i++)
		{
			for(int j = 0; j < width; j++)
			{
				if(state.getAt(j,i) == p)
				{
					int temp = growCoin(state, j, i, 1, 0) + growCoin(state, j, i, 0, 1) + growCoin(state, j, i, 1, 1) + growCoin(state, j, i, -1, 1);

					ans += temp;
				}
				else
				{
					if(state.getAt(j,i) != 0)
					{
						int temp = growCoin(state, j, i, 1, 0) + growCoin(state, j, i, 0, 1) + growCoin(state, j, i, 1, 1) + growCoin(state, j, i, -1, 1);

						ans -= temp;
					}
				}

			}
		}

		return ans;
	}

	// coin the number of coins possible with a given (x,y) position in + dx, +dy direction
	private int growCoin(final GameStateModule state, int x, int y, int dX, int dY)
	{
		// from each position of the coin *only grow until 4 in a row
		int sides[] = {1,-1};
		int coins = 0, xTemp = x, yTemp = y;
		int xMax = state.getWidth(), yMax = state.getHeight();


		for(int side: sides)
		{
			// reset the origin value
			x = xTemp;
			y = yTemp;

			// explore the other direction from origin
			dX = dX * side;
			dY = dY * side;

			if (side == -1 && x == xTemp && y == yTemp)
			{
				x += dX;
				y += dY;
			}

			int max = 0;

			while( x >= 0 && y >= 0 && x <  xMax &&  y < yMax && Math.abs(x-xTemp) < 4 && Math.abs(y-yTemp) < 4 )
			{

				coins++;
				max++;

				// increment to the next position
				x += dX;
				y += dY;
			}
		}

		// if there is four coins
		if (coins > 3)
		{
			// if there is more than four coins account for more than possible of winning
			if (coins > 4)
				return Math.min(4, coins-3);
			else
				return 1;
		}
		else
			return 0;
	}


}
