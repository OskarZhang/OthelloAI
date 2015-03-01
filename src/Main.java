import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;


public class Main {
	static int counter=0;
	public static void main(String args[])
	{
		String[][] board=new String[8][8];//initialize the board
		for(int i=0;i<8;i++)
		{
			for(int j=0;j<8;j++)
				board[i][j]="-";
		}
		int numOfBlack=2;
		int numOfWhite=2;
		board[4][3]="@";board[3][4]="@";board[3][3]="O";board[4][4]="O";
		Scanner scan=new Scanner(System.in);
		System.out.println("".equals("-"));
		String player="@";
		while((numOfBlack+numOfWhite)!=64)
		{
			printBoard(board);
			
			ArrayList<Point> availableMoves=getAvailableMove(board,player);
			Point maxPoint=availableMoves.get(1);
			double max=evaluatePoint(maxPoint, board, player);
			for(int i=1;i<availableMoves.size();i++)
			{
				Point currPoint=availableMoves.get(i);
				double currScore=evaluatePoint(currPoint, board, player);
				if(currScore>=max)
					{max=currScore;maxPoint=currPoint;}
			}
			System.out.println("Computer's Move: "+maxPoint.x+", "+maxPoint.y);
			counter++;
			simulateMove(maxPoint, board, player);
			printBoard(board);
			
			System.out.println("Your turn O! format:(x,y)");
			String opponentMove= scan.nextLine();
			int playerX=Character.getNumericValue(opponentMove.charAt(0));int playerY=Character.getNumericValue(opponentMove.charAt(2));
			counter++;
			simulateMove(new Point(playerX,playerY), board, "O");
			printBoard(board);
//get max score
		}
		if(numOfBlack>numOfWhite)
			System.out.println("Black wins");
		else if(numOfWhite>numOfBlack)
			System.out.println("White wins");
		else
			System.out.println("TIE");
	/**
		//standard input 
		//initialization
		Scanner scan=new Scanner(System.in);
		String[][] contestBoard=new String[8][8];
		String firstLine=scan.nextLine();
		if(firstLine.charAt(0)=='B')
			{System.out.println("A");}
		String player="";
		player+=firstLine.charAt(2);
		while(scan.hasNextLine())
		{
			String input=scan.nextLine();
			if(input.charAt(0)=='T')
			{
				contestBoard=loadBoard(input);
				//75
				int curr=75;
				ArrayList<Point> validMoves=new ArrayList<Point>(0);
				while(curr<input.length())
				{
					int x=Character.getNumericValue(input.charAt(curr));
					int y=Character.getNumericValue(input.charAt(curr+2));
					Point pt= new Point(x,y);
					validMoves.add(pt);
					curr+=4;
				}
				int prev=0;
				Point maxPoint=new Point(0,0);
				double max=0;
				for(int i=0;i<validMoves.size();i++)
				{
					Point currPoint=validMoves.get(i);
					double currScore=evaluatePoint(currPoint, contestBoard, player);
					if(currScore>=prev)
						{max=currScore;maxPoint=currPoint;}
				}
				System.out.println(maxPoint.x+","+maxPoint.y);
				
			}
			else if(input.charAt(0)=='R')
			{
				contestBoard=loadBoard(input);
				int opponentX=Character.getNumericValue(input.charAt(75));
				int opponentY=Character.getNumericValue(input.charAt(77));
				//simulateMove(p, contestBoard, player);
				int curr=79;
				ArrayList<Point> validMoves=new ArrayList<Point>(0);
				while(curr<input.length())
				{
					int x=Character.getNumericValue(input.charAt(curr));
					int y=Character.getNumericValue(input.charAt(curr+2));
					Point pt= new Point(x,y);
					validMoves.add(pt);
					curr+=4;
				}
				Point maxPoint=validMoves.get(1);
				double max=evaluatePoint(maxPoint, contestBoard, player);
				for(int i=1;i<validMoves.size();i++)
				{
					Point currPoint=validMoves.get(i);
					double currScore=evaluatePoint(currPoint, contestBoard, player);
					if(currScore>=max)
						{max=currScore;maxPoint=currPoint;}
				}
				System.out.println(maxPoint.x+","+maxPoint.y);
				
			}
			else if(input.charAt(0)=='S')
			{
				contestBoard=loadBoard(input);
			}
		}**/
		
		
		
	}
	static String[][] loadBoard(String input)
	{
		int x=0;int y=0;
		String[][] output=new String[8][8];
		for(int i=2;i<74;i++)
		{
			if(input.charAt(i)=='|')
			{
				x++;
				y=0;
				continue;
			}
			output[x][y]=""+input.charAt(i);
			y++;
		}
		return output;
	}
	static double evaluatePoint(Point p,String[][] board,String player)
	{
		boolean isBlack=player.equals("@");
		boolean isBeginning=true;
		if(counter<=35)
			;
		else
			isBeginning=false;
		//beginning phase 
		double pElimination=1.00;
		 double pCorner=0.10;
		 double pOnto=-0.5;
		 double pWall=2;
		if(!isBeginning)
		{
			pElimination=0.30;
			pCorner=1.00;
			if(isBlack)
				pOnto=0;
			else
				pOnto=-4;
			pWall=3;
		}
		double elimination=getExpectedLossTile(p.x, p.y, board, player);
		boolean isOn=isOn(p.x, p.y);
		double distanceToClosestCorner=0.0;
		boolean isOnTheWall=isOnTheWall(p);
		switch(shortestDistanceToCorner(p.x, p.y, board))
		{
		case 7:distanceToClosestCorner=distanceToCornerOne(p.x, p.y, board); 
		case 1:distanceToClosestCorner=distanceToCornerTwo(p.x, p.y, board); 
		case 3:distanceToClosestCorner=distanceToCornerThree(p.x, p.y, board); 
		case 5:distanceToClosestCorner=distanceToCornerFour(p.x, p.y, board); 
		default:;
		}
		double score=elimination*pElimination+pCorner*(1/distanceToClosestCorner)+pOnto*(isOn?1:0)+pWall*(isOnTheWall?1:0);
		
		
		
		
		return score;
	}
	static String[][] simulateMove(Point p,String[][] board,String player)
	{
		//flip
		String opponent="";
		if(player.equals("@"))
			opponent="O";
		else
			opponent="@";
		//check horizontal left
		int i=p.x; int j=p.y;
		while(getTileInDir(i, j, 6, board).equals(opponent))
		{
				i--;
		}
		if(getTileInDir(i, j, 6, board).equals(player))
		{
			Point currPoint=increment(i, j, 6);
			while(!currPoint.equals(p))
			{
				i=currPoint.x;j=currPoint.y;
				board[i][j]=player;
				currPoint=increment(i, j, oppositeDir(6));
			}
		}
		//check horizontal right
		i=p.x;
		while(getTileInDir(i, j, 2, board).equals(opponent))
		{
				
				i++;
		}
		if(getTileInDir(i, j, 2, board).equals(player))
		{
			Point currPoint=increment(i, j, 2);
			while(!currPoint.equals(p))
			{
				i=currPoint.x;j=currPoint.y;
				board[i][j]=player;
				currPoint=increment(i, j, oppositeDir(2));
			}
		}
		//check vertical up
		i=p.x;
		while(getTileInDir(i, j, 0, board).equals(opponent))
		{
				j--;
		}
		if(getTileInDir(i, j, 0, board).equals(player))
		{
			Point currPoint=increment(i, j, 0);
			while(!currPoint.equals(p))
			{
				i=currPoint.x;j=currPoint.y;
				board[i][j]=player;
				currPoint=increment(i, j, oppositeDir(0));
			}
		}
		//check vertical down
		j=p.y;
		while(getTileInDir(i, j, 4, board).equals(opponent))
		{
				
				j++;
		}
		if(getTileInDir(i, j, 4, board).equals(player))
		{
			Point currPoint=increment(i, j, 4);
			while(!currPoint.equals(p))
			{
				i=currPoint.x;j=currPoint.y;
				board[i][j]=player;
				currPoint=increment(i, j, oppositeDir(4));
			}
		}
		
		//check diagonal
		//check toward 7,7
		i=p.x;j=p.y;
		while(getTileInDir(i, j, 3, board).equals(opponent))
		{
				
				j++;
				i++;
		}

		if(getTileInDir(i, j, 3, board).equals(player))
		{
			Point currPoint=increment(i, j, 3);
			while(!currPoint.equals(p))
			{
				i=currPoint.x;j=currPoint.y;
				board[i][j]=player;
				currPoint=increment(i, j, oppositeDir(3));
			}
		}
	
		//check toward 0,0
		i=p.x;j=p.y;
		while(getTileInDir(i, j, 7, board).equals(opponent))
		{
				
				j--;i--;
		}
		if(getTileInDir(i, j, 7, board).equals(player))
		{
			Point currPoint=increment(i, j, 7);
			while(!currPoint.equals(p))
			{
				i=currPoint.x;j=currPoint.y;
				board[i][j]=player;
				currPoint=increment(i, j, oppositeDir(7));
			}
		}
		//check toward 7,0
		i=p.x;j=p.y;
		while(getTileInDir(i, j, 1, board).equals(opponent))
		{
				
				j--;i++;
		}
		if(getTileInDir(i, j, 1, board).equals(player))
		{
			Point currPoint=increment(i, j, 1);
			while(!currPoint.equals(p))
			{
				i=currPoint.x;j=currPoint.y;
				board[i][j]=player;
				currPoint=increment(i, j, oppositeDir(1));
			}
		}

		//check toward 0,7
		i=p.x;j=p.y;
		while(getTileInDir(i, j, 5, board).equals(opponent))
		{
				j++;i--;
		}
		if(getTileInDir(i, j, 5, board).equals(player))
		{
			Point currPoint=increment(i, j, 5);
			while(!currPoint.equals(p))
			{
				i=currPoint.x;j=currPoint.y;
				board[i][j]=player;
				currPoint=increment(i, j, oppositeDir(5));
			}
		}
		board[p.x][p.y]=player;
		return board;
	}

	static ArrayList<Point> getAvailableMove(String[][] board,String player)
	{
		ArrayList<Point> output=new ArrayList<Point>(0);
		for(int i=0;i<board.length;i++)
		{
			for(int j=0;j<board[0].length;j++)
			{
				if(!board[i][j].equals("-"))
				{
						if(getTileInDir(i, j, 0, board).equals("-"))
						{
							
							Point temp=new Point(i,j-1);
							if(getExpectedLossTile(temp.x, temp.y, board, player)>0)
							{
							if(output.contains(temp));
								output.add(temp);
							}
						}
						if(getTileInDir(i, j, 1, board).equals("-"))
						{
							Point temp=new Point(i+1,j-1);
							if(getExpectedLossTile(temp.x, temp.y, board, player)>0)
							{
							if(output.contains(temp));
								output.add(temp);
							}
						}
						if(getTileInDir(i, j, 2, board).equals("-"))
						{
							Point temp=new Point(i+1,j);
							if(getExpectedLossTile(temp.x, temp.y, board, player)>0)
							{
							if(output.contains(temp));
								output.add(temp);
							}
						}
						if(getTileInDir(i, j, 3, board).equals("-"))
						{
							Point temp=new Point(i+1,j+1);
							if(getExpectedLossTile(temp.x, temp.y, board, player)>0)
							{
							if(output.contains(temp));
								output.add(temp);
							}
						}
						if(getTileInDir(i, j, 4, board).equals("-"))
						{
							Point temp=new Point(i,j+1);
							if(getExpectedLossTile(temp.x, temp.y, board, player)>0)
							{
							if(output.contains(temp));
								output.add(temp);
							}
						}
						if(getTileInDir(i, j, 5, board).equals("-"))
						{
							Point temp=new Point(i-1,j+1);
							if(getExpectedLossTile(temp.x, temp.y, board, player)>0)
							{
							if(output.contains(temp));
								output.add(temp);
							}
						}
						if(getTileInDir(i, j, 6, board).equals("-"))
						{
							Point temp=new Point(i-1,j);
							if(getExpectedLossTile(temp.x, temp.y, board, player)>0)
							{
							if(output.contains(temp));
								output.add(temp);
							}
						}
						if(getTileInDir(i, j, 7, board).equals("-"))
						{
							Point temp=new Point(i-1,j-1);
							if(getExpectedLossTile(temp.x, temp.y, board, player)>0)
							{
							if(output.contains(temp));
								output.add(temp);
							}
						}

					
				}
			}
		}
		return output;
	}
	static void printBoard(String[][] board)
	{
		System.out.println(" 0 1 2 3 4 5 6 7");
		for(int i=0;i<board.length;i++)
		{
			System.out.print(i);
			for(int j=0;j<board[0].length;j++)
			{
				System.out.print(board[j][i]+" ");
			}
			System.out.println("|");
		}
	}
	static int getExpectedLossTile(int locX,int locY,String[][] board,String player)
	{
		String opponent="";
		if(player.equals("@"))
			opponent="O";
		else
			opponent="@";
		int sum=0;
		//check horizontal left
		int i=locX; int j=locY;
		while(getTileInDir(i, j, 6, board).equals(opponent))
		{
				i--;
		}
		if(getTileInDir(i, j, 6, board).equals(player))
		{
			sum+=locX-i;	
		}
		//check horizontal right
		i=locX;
		while(getTileInDir(i, j, 2, board).equals(opponent))
		{
				
				i++;
		}
		if(getTileInDir(i, j, 2, board).equals(player))
		{
			sum+=i-locX;
		}
		//check vertical up
		i=locX;
		while(getTileInDir(i, j, 0, board).equals(opponent))
		{
				j--;
		}
		if(getTileInDir(i, j, 0, board).equals(player))
		{
			sum+=locY-j;
		}
		//check vertical down
		j=locY;
		while(getTileInDir(i, j, 4, board).equals(opponent))
		{
				
				j++;
		}
		if(getTileInDir(i, j, 4, board).equals(player))
		{
			sum+=j-locY;
		}
		
		//check diagonal
		//check toward 7,7
		i=locX;j=locY;
		while(getTileInDir(i, j, 3, board).equals(opponent))
		{
				
				j++;
				i++;
		}
		if(getTileInDir(i, j, 3, board).equals(player))
		{
			sum+=j-locY;
		}
	
		//check toward 0,0
		i=locX;j=locY;
		while(getTileInDir(i, j, 7, board).equals(opponent))
		{
				
				j--;i--;
		}
		if(getTileInDir(i, j, 7, board).equals(player))
		{
			sum+=locY-j;
		}
		//check toward 7,0
		i=locX;j=locY;
		while(getTileInDir(i, j, 1, board).equals(opponent))
		{
				
				j--;i++;
		}
		if(getTileInDir(i, j, 1, board).equals(player))
		{
			sum+=locY-j;
		}

		//check toward 0,7
		i=locX;j=locY;
		while(getTileInDir(i, j, 5, board).equals(opponent))
		{
				j++;i--;
		}
		if(getTileInDir(i, j, 5, board).equals(player))
		{
			sum+=j-locY;
		}

		return sum;
		
	}
	static String getTileInDir(int x,int y,int dir,String[][] board)
	{
		if(dir==0)
		{	if(y!=0)
				return board[x][y-1];
			else
				return "";
		}
		if(dir==1)
		{
			if(x!=7&&y!=0)
				return board[x+1][y-1];
			else
				return "";
		}
		if(dir==2)
		{
			if(x!=7)
				return board[x+1][y];
			else
				return "";
		}
		if(dir==3)
		{
			if(x!=7&&y!=7)
				return board[x+1][y+1];
			else
				return "";
		}
		if(dir==4)
		{
			if(y!=7)
				return board[x][y+1];
			else
				return "";
		}
		if(dir==5)
		{
			if(x!=0&&y!=7)
				return board[x-1][y+1];
			else
				return "";
		}
		if(dir==6)
		{
			if(x!=0)
				return board[x-1][y];
			else
				return "";
		}
		if(dir==7)
		{
			if(y!=0&&x!=0)
				return board[x-1][y-1];
			else
				return "";
		}
	return "";
	}
	static int oppositeDir(int dir)
	{
		if(dir==0)
			return 4;
		if(dir==1)
			return 5;
		if(dir==2)
			return 6;
		if(dir==3)
			return 7;
		if(dir==4)
			return 0;
		if(dir==5)
			return 1;
		if(dir==6)
			return 2;
		if(dir==7)
			return 3;
		return 0;
	}
	static Point increment(int x, int y,int dir)
	{
		if(dir==0)
			{y--;}
		if(dir==1)
			{y--;x++;}
		if(dir==2)
			{x++;}
		if(dir==3)
			{y++;x++;}
		if(dir==4)
			{y++;}
		if(dir==5)
			{x--;y++;}
		if(dir==6)
			{x--;}
		if(dir==7)
			{x--;y--;}
		return new Point(x,y);
	}
	static int shortestDistanceToCorner (int x, int y, String[][] board)
	{

	double d1 =  distanceToCornerOne(x, y, board);


	double d2 = distanceToCornerTwo(x, y, board);


	double d3 = distanceToCornerThree(x, y, board);


	double d4 = distanceToCornerFour(x, y, board);


	double minX = findMinOfFour(d1, d2, d3, d4);



	if(minX == d1)
		return 7;
	else if(minX == d2)
		return 1;
	else if(minX == d3)
		return 3;
	else
		return 5;



	}

	static double distanceToCornerOne (int x, int y, String[][] board)
	{
		return (Math.pow(x,2)+ Math.pow(y,2));
	}

	static double distanceToCornerTwo (int x, int y, String[][] board)
	{
		y = 8 - y;
		return (Math.pow(x,2)+ Math.pow(y,2));
	}

	static double distanceToCornerThree (int x, int y, String[][] board)
	{
		x = 8 - x;
		y = 8 - y;

	return (Math.pow(x,2)+ Math.pow(y,2));



	}

	static double distanceToCornerFour (int x, int y, String[][] board)
	{
		x = 8 - x;
		return (Math.pow(x,2)+ Math.pow(y,2));
	}

	static double findMinOfFour(double a, double b, double c, double d)
	{

	double output = a;


	if (output > b)



	output = b;

	if (output > c)



	output = c;

	if (output > d)



	output = d;

	return output;



	}
static boolean isOnTheWall(Point p)
{
	if(p.x==0&&(p.y<=7&&p.y>=0))
		return true;
	if(p.x==7&&(p.y<=7&&p.y>=0))
		return true;
	if(p.y==0&&(p.x<=7&&p.y>=0))
		return true;
	if(p.y==7&&(p.x<=7&&p.x>=0))
		return true;
	return false;
}
static boolean isOn (int x,int y)
{
    	if(x == 1 && (y >= 1 && y <= 6))
    		return true; 
    	else if(x == 6 && (y >= 1 && y <= 6))
    		return true; 
    	else if(y == 1 && (x >= 1 && y <= 6))
    		return true; 
    	else if(y == 6 && (x >=1 && y <=6))
    		return true; 
    	else 
    		return false; 
    	
    }

}

