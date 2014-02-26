// Sharon Yang
// Pacman.java
// March 12, 2012
// This program creates a game when Pacman is trying to eat all the cheeses and avoid monsters.

// Class variables:
//	x: x-coordinate of the location of Pacman
//	y: y-coordinate of the location of Pacman
//	canvas: calling the class DrawingArea
//	board: contains either cheese or the user in each location
//	monster: contains the location of monsters
//	buttons: calling the button class
//	win: boolean that signifies the winning of game
//	lose: boolean that signifies the losing of game
//	cheesecount: counting the number of cheeses eaten
//	resetcall: signals the resetting of the program
//	movingonly: a boolean used to prevent repainting when the buttons are pushed
//	timer: a timer that repaints the monsters every 400 miliseconds
//	mouth: an int that controls the opening and closing of the mouth
//	firsttime: a boolean that is used to draw out the component when the program is first used
//	counter: an integer that delays the monster action

// Class methods:
//	Pacman: constructor that initializes the starting position of all objects
//	init: set up the connection between the two JPanel with the Applet
//	(class) Buttons: contains the buttons of the program
//	actionPerformed: within Buttons when a certain button is pushed the Pacman moves
//	(class) DrawingArea: draws out the program
//	drawGrid: draws the grid of the program
//	drawComponents: draws the Pacman and the cheeses
//	drawMonsters: draws the monster
//	moveMonsters: moves the monster
//	winner: if Pacman has eaten all cheeses, this signals the victory
//	lose: if Pacman is eaten by the monster, this signals the loss

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Pacman extends JApplet{
	private int x, y;				// x,y location of current square
	private DrawingArea canvas;		// JPanel canvas on which to draw grid
	private char[][] board, monster; 
	private Buttons buttons;
	private boolean win, lose, resetcall;
	private int cheesecount;
	private boolean movingonly;
	
	public Pacman (){
		movingonly = true;
		resetcall = false;
		win = false;
		lose = false;
		cheesecount = 0;
		board = new char[10][10];
		monster = new char[10][10];
		
		for (int row = 0; row < board.length; row ++) // this constructs the arrays to all 'none'
			for (int col = 0; col < board[row].length ; col ++)
				board[row][col] = 'o';
		
		for (int row = 0; row < monster.length; row ++) // this constructs the arrays to all 'none'
			for (int col = 0; col < monster[row].length ; col ++)
				monster[row][col] = 'o';
		
		x= (int)(Math.random()*10);
		y= (int)(Math.random()*10);
		
		board[y][x] = 'x'; 		// 'x' is the location of the user.
		
		int countcheese=0, countmonster =0;
		int cheesex, cheesey;
		int monsterx, monstery;
		
		while (countcheese < 6){ // this puts six cheeses on random location
			cheesex = (int)(Math.random()*10);
			cheesey = (int)(Math.random()*10);
			if (board[cheesey][cheesex]=='o'){
				board[cheesey][cheesex]='c';
				countcheese++;
			}
			else{}		
		}
		
		while (countmonster < 6){ // this puts six monsters on random location
			monsterx = (int)(Math.random()*10);
			monstery = (int)(Math.random()*10);
			if (board[monstery][monsterx]=='o'){
				monster[monstery][monsterx]='m';
				countmonster++;
			}
			else{}	
		}
	}
	
	public void init ( ) { // this constructs the different Panels to this Applet
		canvas = new DrawingArea();				
		getContentPane().add ( canvas, BorderLayout.CENTER );
		canvas.setBackground(Color.blue);
		buttons = new Buttons();
		getContentPane().add ( buttons, BorderLayout.SOUTH );
		buttons.setBackground(Color.black);
	}
	
	class Buttons extends JPanel implements ActionListener{
		public Buttons (){ // this sets up the five buttons on the bottom of the Applet.
			JButton left = new JButton("LEFT");
			left.addActionListener ( this );
       		this.add ( left );
			JButton right = new JButton("RIGHT");
			right.addActionListener ( this );
       		this.add ( right );
			JButton up = new JButton("UP");
			up.addActionListener ( this );
       		this.add ( up );
			JButton down = new JButton("DOWN");
			down.addActionListener ( this );
       		this.add ( down );
			JButton reset = new JButton("RESET");
			reset.addActionListener ( this );
       		this.add ( reset );
		}
		
		public void actionPerformed(ActionEvent e){
			String command = e.getActionCommand();
			int originalx, originaly;
			originalx= x;
			originaly = y;
			if(command.equals("LEFT")&&lose == false&&win == false){ // if certain button is pushed, an action is initialized.
				board[y][x]='o';
				if (x-1 >=0)
					x=x-1;
				else
					x=9;
				movingonly = true;
			}
			
			else if(command.equals("RIGHT")&&lose == false&&win == false){
				board[y][x]='o';
				if (x+1 <=9)
					x=x+1;
				else
					x=0;
				movingonly = true;
			}
			
			else if(command.equals("UP")&&lose == false&&win == false){
				board[y][x]='o';
				if (y-1 >=0)
					y=y-1;
				else
					y=9;
				movingonly = true;
			}
			
			else if(command.equals("DOWN")&&lose == false&&win == false){
				board[y][x]='o';
				if (y+1 <=9)
					y=y+1;
				else
					y=0;
				movingonly = true;
			}
			
			else if (command.equals("RESET")){ // this restarts the program
				resetcall = true;
				movingonly = true;
			}
			
			if (command.equals("RESET") == false){  // if no 'reset' button pressed, check if the user wins/loses
				if (monster[y][x]!='m'&&board[y][x]== 'c')
					cheesecount= cheesecount +1;
				
				if (cheesecount ==6)
					win = true;
				
				if (monster[y][x] == 'm'){
					win = false;
					lose = true;
				}
				
				else
					board[y][x]='x';
			}
			canvas.repaint();
		}
	}
	
	class DrawingArea extends JPanel{
		private Timer timer;
		private int mouth, counter;
		private boolean firsttime;
		
		public DrawingArea (){ // this starts the timer, constructs the Panel, etc.
			counter = 5;
			firsttime = true;
			mouth = 0;
			RepaintAction action = new RepaintAction();
			timer = new Timer(400, action);
			timer.start();
    		
		}
		
		class RepaintAction implements ActionListener{
			public void actionPerformed(ActionEvent e){
				movingonly = false; // this is made so that when buttons pressed = monsters don't get repainted. But when timer is called, monsters repainted
				repaint(); 	
			}
		}
		
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			while (firsttime){ // this calls the program for the first time without reassigning the monster's positions
				drawGrid(g);
				drawComponents(g);
				drawMonsters(g);
				lose(g);
				winner(g);
				firsttime = false;
			}
			playGame(g); // when not first time, this is called.
			
		}
		public void playGame(Graphics g){
			if (resetcall == true){
				restart(g);
				resetcall = false;
			}
			drawGrid(g);
			drawComponents(g);
			if (movingonly == false&&lose==false&&win ==false) // when the buttons are pushed, this repaint method is ignored.
				moveMonsters(g);
			drawMonsters(g);
			lose(g);
			winner(g);
		}
		
		public void restart(Graphics g){
			timer.start();
			win = false;
			lose = false;
			cheesecount = 0;
			board = new char[10][10];
			monster = new char[10][10];
			
			for (int row = 0; row < board.length; row ++) // this resets the board to none.
				for (int col = 0; col < board[row].length ; col ++)
					board[row][col] = 'o';
			
			for (int row = 0; row < monster.length; row ++) // this resets the board to none.
				for (int col = 0; col < monster[row].length ; col ++)
					monster[row][col] = 'o';
			
			x= (int)(Math.random()*10);
			y= (int)(Math.random()*10);
			
			board[y][x] = 'x'; 		// 'x' is the location of the user.
			
			int countcheese=0, countmonster =0;
			int cheesex, cheesey;
			int monsterx, monstery;
			
			while (countcheese < 6){ // this puts six cheeses on random location
				cheesex = (int)(Math.random()*10);
				cheesey = (int)(Math.random()*10);
				if (board[cheesey][cheesex]=='o'){
					board[cheesey][cheesex]='c';
					countcheese++;
				}
				else{}		
			}
			
			while (countmonster < 6){ // this puts six monsters on random location
				monsterx = (int)(Math.random()*10);
				monstery = (int)(Math.random()*10);
				if (monster[monstery][monsterx]=='o'){
					monster[monstery][monsterx]='m';
					countmonster++;
				}
				else{}	
			}
		}
		
		public void drawGrid(Graphics g) {
			g.setColor(Color.lightGray);
			g.fillRect(30,30, 605, 605);
			g.setColor(Color.white); 
			for (int row = 0; row < 10; row ++) // this draws the grid on the applet.
				for (int col = 0; col < 10; col ++)
					g.fillRect(35 + col *60, 35 + row * 60, 55, 55);			
		
			
			
		}
		
		public void drawMonsters(Graphics g){
			
			for (int row = 0; row < monster.length; row ++) // this draws the monsters on the applet.
				for (int col = 0; col < monster[row].length ; col ++){
					if (monster[row][col] == 'm'){
						g.setColor(Color.red);
						g.fillOval(36 + col *60, 36 + row * 60, 52, 52);
						g.setColor(Color.black);
						g.fillRect(45 + col *60, 74 + row * 60, 34, 3);
						g.fillOval(47 + col *60, 50 + row *60, 10, 10);
						g.fillOval(65 + col *60, 50 + row *60, 10, 10);
					}
					
					else {}
				}
		}
				
		public void drawComponents(Graphics g) {
			
			g.setColor(Color.blue); // this is responsible for drawing Pacman and the opening/closing of its mouth.
			switch (mouth){
				case 0:
					g.fillArc(38 + x *60, 38 + y * 60, 50, 50, 20, 320);
					mouth = mouth+1;
					break;
				case 1:
					g.fillArc(38 + x *60, 38 + y * 60, 50, 50, 40, 280);
					mouth = mouth-1;
					break;
			}
			
			Color cheesecolor = new Color (255,189,11); // this draws the cheeses on the board
			g.setColor(cheesecolor);
			int cheesex=0, cheesey=0;
			for (int row = 0; row < board.length; row ++)
				for (int col = 0; col < board[row].length ; col ++){
					if (board[row][col] == 'c'){
						cheesex=col; 
						cheesey=row;
						g.fillRect(38 + cheesex *60, 38 + cheesey * 60, 49, 49);
					}
			}
		}
			
		public void moveMonsters(Graphics g){	
			int monsterx=0, monstery=0;
			int count;
			char[][] repeat = new char [10][10];
			
			for (int row = 0; row < repeat.length; row ++) // this constructs the arrays to all 'none'
				for (int col = 0; col < repeat[row].length ; col ++)
					repeat[row][col] = 'o';
				
			counter = counter-1;
			if (counter == 0){
				for (int row = 0; row < monster.length; row ++) // this is responsible for assigning monsters to random location near the original loaction.
					for (int col = 0; col < monster[row].length ; col ++){
						if (monster[row][col] == 'm'&&repeat[row][col] != 'r'){
							counter = (int)(Math.random()*4);
							switch (counter){
								case 0:
									if (col < 9 &&monster[row][col+1]!='m'){
										monstery = row;
										monsterx = col +1;
									}
									else if (col ==9 &&monster[row][0]!='m'){
										monstery = row;
										monsterx = 0;
									}
									else{
										monstery = row;
										monsterx = col;
									}
									break;
								case 1:
									if (col >0 &&monster[row][col-1]!='m'){
										monstery = row;
										monsterx = col -1;
									}
									else if (col ==0 &&monster[row][9]!='m'&&col-1<0){
										monstery = row;
										monsterx = 9;
									}
									else{
										monstery = row;
										monsterx = col;
									}
									break;
								case 2:
									if (row <9 &&monster[row+1][col]!='m'){
										monstery = row+1;
										monsterx = col;
									}
									else if (row == 9&& monster[0][col]!='m'){
										monstery = 0;
										monsterx = col;
									}
									else{
										monstery = row;
										monsterx = col;
									}
									break;
								case 3:
									if (row >0&&monster[row-1][col]!='m'){
										monstery = row-1;
										monsterx = col;
									}
									else if (row ==0&&monster[9][col]!='m'){
										monstery = 9;
										monsterx = col;
									}
									else{
										monstery = row;
										monsterx = col;
									}
									break;
							
							}

							if (monstery != row || monsterx !=col){ // if the monster moves, the original position is set to 'none' and the new position is set to 'm'
								monster[row][col] = 'o';
								monster[monstery][monsterx] = 'm';
								repeat[row][col] = 'r';
								repeat[monstery][monsterx] = 'r';
							}
							else{
								monster[row][col] = 'm';
								repeat[row][col] = 'r';
							}
							
							
							
							
						}
					}
					counter =5;
				}
		}
		
		public void winner(Graphics g){
			if (lose == false && win == true){ // all six cheeses are collected, this is called.
				g.setColor(Color.black);
				Font font = new Font("Serif", Font.BOLD, 60);
				String str = "YOU WIN!!!";
				g.setFont(font);
				g.drawString(str,180,300);
				timer.stop();
			
			}
		}
		
		public void lose(Graphics g){ // this is run to finished when monster cruchses with the Pacman
			if (monster[y][x] == 'm')
				lose = true;
			if (lose == true){
				g.setColor(Color.black);
				Font font = new Font("Serif", Font.BOLD, 60);
				String str = "YOU LOSE!!!";
				String str2= "TRY AGAIN!";
				g.setFont(font);
				g.drawString(str,160,300);
				g.drawString(str2,160,400);
				timer.stop();
			}
		
		}
	}
	
} // end class