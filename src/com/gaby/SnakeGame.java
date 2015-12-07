/*Tasks for the project: fix bugs, improve code, upgrade to Snake II, and document your work.

1. Read the source code to understand what the code is doing, and run and test the game.

You should add some more comments of your own as you work out what the code is doing.


2. Submit bug report(s)

When you identify a bug in the original Snake, you must submit a bug report. Blank bug reports are also in the Class Project section of D2L. Download, fill out, and upload to the project dropbox. I am sure you'll find some bugs. (Hint: there's a timing bug, and the game over stage has never been tested). If you can find any bugs, then bug reports can be on bugs in your own code. (p.s. none of the FindBugs issues count, they have to be errors in the program).


3. Upgrade Snake I to Snake II. This includes:

Improve the appearance of the game (but make sure you spend 
the majority of your time on the program code and logic)
Add warp walls (when the snake goes off one side of the screen, it reappears on the other)
Add mazes (blocks or walls in the grid that the snake can’t go through)
Let the user change the speed, size of the screen, whether 
mazes are on or not, whether warp walls are on or not. You 
should also tell the user how to do this.
Extra features of your choice. What else would you add? 
Perhaps different kinds of snake food, with different points? More points for eating food when the game is faster? 
Refactoring the code to get rid of the "magic numbers" 
( HYPERLINK "http://en.wikipedia.org/wiki/Magic_number_
%28programming%29" \l "Unnamed_numerical_constants
"http://en.wikipedia.org/wiki/Magic_number_%28programming%29#Unnamed_numerical_constants)
Commenting ALL of your code
Testing your code. Before you turn in your final project, 
you should test it thoroughly. Submit bug reports on any 
bugs in the features you add; as are present in the code 
you submit. Get someone else to play the game.


4. Write a list of the features you added. This should 
be something that would be useful when another developer has to modify your code.


5. Optional extra credit addition – recommended! 
(up to +10 points) download and run FindBugs on Snake. 
Fix the problems identified. Helpfully, Snake comes 
with various issues that FindBugs will identify. 
HYPERLINK "http://findbugs.sourceforge.net/index.html" 
http://findbugs.sourceforge.net/index.html . 
To earn the extra credit, add a comment with //FINDBUGS next to each issue that you fixed.

Any questions? Please ask me, or email me.



To Submit:

Snake 2 code. Zip your project, rename the file 
so it has your name in the filename, and upload 
to the Class Project 2 dropbox by the due date posted.
Also, push your code to your GitHub repository. 
Unless you totally re-wrote the code, make sure 
you acknowledge all of the authors. Make sure you 
annotate any code adapted from any other sources too. 
Add a note with the GitHub URL in your dropbox submission.
At least 2 bug reports on the original code or on 
features you've added to your final project. 
Hopefully you fixed the issues if you found them in 
the original version, but if it is something complex 
you may not be able to, so that’s ok. You should 
explain the issue and what the fix might be if you 
can’t actually fix it. The bug reports for your code 
should be for unresolved bugs in the code you are 
going to turn in. FindBugs issues don't count – 
you need to find logic errors in the game.
A short report describing the extra features you 
added to the game. Describe in programmer terms, 
so that another developer could use it to continue work on the project.
For extra credit: A screenshot of a clean 
(or nearly clean) FindBugs report on your code and a 
//FINDBUGS comment next to each fix for errors identified by FindBugs.


Grading:

Bug Reports, at least one, hopefully two, perhaps more, 10%. 
Your reports should be clear, concise, and acceptable for 
the lead programmer on your team to read. You should either 
have fixed the bug (for original snake) and explained the fix, 
or explained the bug and what you would do to fix it 
(for original snake or in your submitted version of the code). 
You should submit at least 2 bug reports.

Report on Features 10%. Make this clear and concise. 
It should be something that another programmer would 
find useful if they took over this project.

Code 80%. Your code should be logical, have minimal errors, 
and follow object-orientated principles, e.g. modular, 
use abstraction, encapsulation, inheritance etc. 
You MUST comment your code. Focus on the quality of your code 
– it is preferable to have fewer well-designed features 
implemented correctly than many poorly implemented features.

Extra credit: up to an extra 10 points for FindBugs tasks.*/


package com.gaby;

import java.util.Timer;

import javax.swing.*;


public class SnakeGame {

	public final static int xPixelMaxDimension = 501;  //Pixels in window. 501 to have 50-pixel squares plus 1 to draw a border on last square
	public final static int yPixelMaxDimension = 501;

	public static int xSquares ;
	public static int ySquares ;

	public final static int squareSize = 50;

	protected static Snake snake ;

	protected static Kibble kibble;

	protected static Score score;

	static final int BEFORE_GAME = 1;
	static final int DURING_GAME = 2;
	static final int GAME_OVER = 3;
	static final int GAME_WON = 4;   //The values are not important. The important thing is to use the constants 
	//instead of the values so you are clear what you are setting. Easy to forget what number is Game over vs. game won
	//Using constant names instead makes it easier to keep it straight. Refer to these variables 
	//using statements such as SnakeGame.GAME_OVER 

	private static int gameStage = BEFORE_GAME;  //use this to figure out what should be happening. 
	//Other classes like Snake and DrawSnakeGamePanel will need to query this, and change its value

	protected static long clockInterval = 500; //controls game speed
	//Every time the clock ticks, the snake moves
	//This is the time between clock ticks, in milliseconds
	//1000 milliseconds = 1 second.

	static JFrame snakeFrame;
	static DrawSnakeGamePanel snakePanel;
	//Framework for this class adapted from the Java Swing Tutorial, FrameDemo and Custom Painting Demo. You should find them useful too.
	//http://docs.oracle.com/javase/tutorial/displayCode.html?code=http://docs.oracle.com/javase/tutorial/uiswing/examples/components/FrameDemoProject/src/components/FrameDemo.java
	//http://docs.oracle.com/javase/tutorial/uiswing/painting/step2.html

	private static void createAndShowGUI() {
		//Create and set up the window.
		snakeFrame = new JFrame();
		snakeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		snakeFrame.setSize(xPixelMaxDimension, yPixelMaxDimension);
		snakeFrame.setUndecorated(true); //hide title bar
		snakeFrame.setVisible(true);
		snakeFrame.setResizable(false);

		snakePanel = new DrawSnakeGamePanel(snake, kibble, score);
		snakePanel.setFocusable(true);
		snakePanel.requestFocusInWindow(); //required to give this component the focus so it can generate KeyEvents

		snakeFrame.add(snakePanel);
		snakePanel.addKeyListener(new GameControls(snake));

		setGameStage(BEFORE_GAME);

		snakeFrame.setVisible(true);
	}

	private static void initializeGame() {
		//set up score, snake and first kibble
		xSquares = xPixelMaxDimension / squareSize;
		ySquares = yPixelMaxDimension / squareSize;

		snake = new Snake(xSquares, ySquares, squareSize);
		kibble = new Kibble(snake);
		score = new Score();

		gameStage = BEFORE_GAME;
	}

	protected static void newGame() {
		Timer timer = new Timer();
		GameClock clockTick = new GameClock(snake, kibble, score, snakePanel);
		timer.scheduleAtFixedRate(clockTick, 0 , clockInterval);
	}

	public static void main(String[] args) {
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				initializeGame();
				createAndShowGUI();
			}
		});
	}



	public static int getGameStage() {
		return gameStage;
	}

	public static boolean gameEnded() {
		if (gameStage == GAME_OVER || gameStage == GAME_WON){
			return true;
		}
		return false;
	}

	public static void setGameStage(int gameStage) {
		SnakeGame.gameStage = gameStage;
	}
}
