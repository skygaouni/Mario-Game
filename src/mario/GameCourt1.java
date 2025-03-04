package mario;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.LinkedList;


@SuppressWarnings("serial")
public class GameCourt1 extends JPanel {
    
    SceneGenerator1 lg;
    
    private Mario1 mario; // the Mario character, keyboard control
    private GroundTile[] ground; // array of ground tiles
    //private EndCastle castle; // pipe at the end of the level
    private Pipe endpipe;
    private LinkedList<Enemy> enemies; // list of enemies contained in the game
    private LinkedList<Enemy> dead_turtels;
    private LinkedList<Coin> coins; // list containing all coins in the game
    private LinkedList<Cloud> clouds;
    private LinkedList<Brick> bricks;
    private LinkedList<BushAndHill> bushAndHill;
    private LinkedList<Integer> marioMaxY; // record the MaxY of mario when calling standOnObj
    private BooleanValueHolder holder;
    private Timer timer;
    Font font;
    
    public HighScores hs = new HighScores(); // High Scores object to write high scores
    public static String username = ""; // User set username
    
    public static boolean playing = false; // whether the game is running
    public boolean gameOver = false; // whether the user has lost
    public boolean gameWon = false; // whether the user has won
    public static boolean endTile = false; // whether the user has reached the end
    public static boolean mario_right_move = false;
    
    /*private JLabel coins_label;
    private JLabel score_label;
    private JLabel lives_label;*/
    private JLabel doneLabel; // label that shows if the user wins or loses
    
    private static int finalScore; // the user's final score once they've won


    // Game constants
    public static final int COURT_WIDTH = 640;
    public static final int COURT_HEIGHT = 400;
    public static final int MARIO_X_VELOCITY = 6;
    public static final int MARIO_Y_VELOCITY = 10;
    public static final int GROUND_X_VELOCITY = 6;
    public static final int CLOUD_X_VELOCITY = 6;   
    public static final int BUSHANDHILL_X_VELOCITY = 6;   
    public static final int ENEMY_X_VELOCITY = 5;
    public static final int MAX_MARIO_X = 350;
    
    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 35;
    
    public static int distanceTravelled;
    
    public GameCourt1(/*JLabel score_label, JLabel coins_label, JLabel lives_label,*/BooleanValueHolder holder) {

        // The timer is an object which triggers an action periodically
        // with the given INTERVAL. One registers an ActionListener with
        // this timer, whose actionPerformed() method will be called
        // each time the timer triggers. We define a helper method
        // called tick() that actually does everything that should
        // be done in a single timestep.
        timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        

        // Enable keyboard focus on the court area.
        // When this component has the keyboard focus, key
        // events will be handled by its key listener.
        //setFocusable(true);

        // This key listener allows the characters on the screen to move as
        // long as an arrow key is pressed, by changing the objects' velocity
        // accordingly. (The tick method below actually moves the objects.)
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                	System.out.println("Press court1 left");
                    mario.v_x = -MARIO_X_VELOCITY;
                    mario_right_move = false;
                    GroundTile.vel_x = GROUND_X_VELOCITY;
                    Cloud.vel_x = CLOUD_X_VELOCITY;
                    BushAndHill.vel_x = BUSHANDHILL_X_VELOCITY;
                    Coin.vel_x = GROUND_X_VELOCITY;
                    Brick.vel_x = GROUND_X_VELOCITY;
                    endpipe.vel_x = GROUND_X_VELOCITY;
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                	System.out.println("Press court1 right");
                    mario.v_x = MARIO_X_VELOCITY;
                    mario_right_move = true;
                    GroundTile.vel_x = -GROUND_X_VELOCITY;
                    Cloud.vel_x = -CLOUD_X_VELOCITY;
                    BushAndHill.vel_x = -BUSHANDHILL_X_VELOCITY;
                    Coin.vel_x = -GROUND_X_VELOCITY;
                    Brick.vel_x = -GROUND_X_VELOCITY;
                    endpipe.vel_x = -GROUND_X_VELOCITY;
                    
                }
                
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                	System.out.println("Press court1 up");
                    if (!mario.gravityOn) mario.v_y = -MARIO_Y_VELOCITY;
                    mario.gravityOn = true;
                    mario.onGround = false;
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    if(endpipe.intersectsTop(mario)) {
                    	timer.stop();
                    	//gameWon = true;
                    	finalScore = Game.scores;
                    	hs.addHighScore(new HighScore(username, finalScore));
                    	holder.setScene(2);
                    	holder.setValue(true);
                    	return;
                    }
                }
                
                if (e.getKeyCode() == KeyEvent.VK_R) {
                	//System.out.println("Press s");
                    if (gameWon || gameOver) {
                        Game.lives = 3;
                        Game.coins = 0;
                    	Game.scores = 0;
                        gameWon = false;
                        gameOver = false;
                        holder.setScene(1);
                        holder.setValue(true);
                    }
                }
            }

            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    mario.v_x = 0;
                    mario_right_move = false;
                    GroundTile.vel_x = 0;
                    Cloud.vel_x = 0;
                    BushAndHill.vel_x = 0;
                    Coin.vel_x = 0;
                    Brick.vel_x = 0;
                    endpipe.vel_x = 0;
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    mario.v_x = 0;
                    GroundTile.vel_x = 0;
                    Cloud.vel_x = 0;
                    BushAndHill.vel_x = 0;
                    Coin.vel_x = 0;
                    Brick.vel_x = 0;
                    endpipe.vel_x = 0;
                }
                else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    if (!mario.gravityOn) {
                        mario.v_y = 0;
                    }
                }         
            }
        });
        
        // Initialize the game state labels
        /*this.score_label = score_label;
        this.coins_label = coins_label;
        this.lives_label = lives_label;*/
        this.holder = holder;
        
        doneLabel = new JLabel("");
    }
    
    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
    	
    	timer.start(); // MAKE SURE TO START THE TIMER!
        doneLabel.setText("");
        distanceTravelled = 0;
        endTile = false; //是否到最後面
        removeAll();
        
        // Reset all of the game objects
        mario = new Mario1(COURT_WIDTH, COURT_HEIGHT);
        lg = new SceneGenerator1(51, COURT_WIDTH, COURT_HEIGHT);
        coins = lg.getCoins();
        enemies = lg.getEnemies();
        ground = lg.getGroundTiles();
        clouds = lg.getClouds();
        bushAndHill = lg.getBushAndHill();
        bricks = lg.getBricks();
        endpipe = new Pipe(COURT_WIDTH, 
        					COURT_HEIGHT, 
        					(ground.length - 2) * GroundTile.SIZE - Pipe.SIZE_X[2]);
        dead_turtels = new LinkedList<Enemy>();
        marioMaxY = new LinkedList<>();
        
        // Start playing the game
        playing = true;
        
        // Appropriately set all of the game labels
        add(doneLabel);
        /*score_label.setText("Score: " + Game.scores);
        lives_label.setText("Lives: " + Game.lives);
        coins_label.setText("Coins: " + Game.coins);*/
    }
    
    /**
     * This method is called every time the timer defined in the constructor
     * triggers.
     */
    void tick() {
    	//System.out.println("1"+KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner());
    	//System.out.println(distanceTravelled);
        /*System.out.println(endpipe.img);
        System.out.println("pos_x:"+endpipe.pos_x);
        System.out.println("pos_y:"+endpipe.pos_y);
        System.out.println("width:"+endpipe.width);
        System.out.println("height:"+endpipe.height);*/
        /*if (gameWon && playing) 
        {
            // Handle the user winning the game
            doneLabel.setText("<html>Congratulations! You've won! <br> Press 'S' to play again!" +
                    "<br> Your score: " + finalScore + "<html>");
            playing = false;
            
        } 
        else */if (gameOver && playing) 
        {
        	
            // Handle the user losing the game
            /*doneLabel.setText("Sorry! You've lost!\nPress 'S' to play again!");*/
            playing = false;
        } 
        else if (playing && mario.pos_y <= COURT_HEIGHT) 
        {
            if (!(ground[ground.length-2].pos_x + GroundTile.SIZE > COURT_WIDTH)) {   
            	//System.out.println("end"+ground[ground.length-2].pos_x);
                endTile = true;  //終點出現了
            }
            //	System.out.println("not to end"+ground[ground.length-2].pos_x);
            // Advance Mario in his current direction        
            mario.move();
            
            // Advance the ground & coins in their current direction
            // 到達max_mario_x後方塊會往左移動
            if (mario.pos_x + mario.width >= MAX_MARIO_X && !mario.dead &&
                    ground[ground.length-2].pos_x + GroundTile.SIZE > COURT_WIDTH) {
                for (int i = 0; i < ground.length; i++) {
                    
                	ground[i].move();

                }
                for (int i = 0; i < coins.size(); i++) {
                    coins.get(i).move();
                }
                for (int i = 0; i< clouds.size(); i++)
                {
                	clouds.get(i).move();
                }
                for (int i = 0; i< bushAndHill.size(); i++)
                {
                	bushAndHill.get(i).move();
                }
                for (int i = 0; i< bricks.size(); i++)
                {
                	bricks.get(i).move();
                    //System.out.println("pos_x:"+bricks.get(i).pos_x);
                }
                endpipe.move();
            }
            
            // Spin the coins & remove them if Mario collects them
            Coin[] cs = new Coin[coins.size()];
            coins.toArray(cs);
            for (Coin coin : cs) {
                coin.spinCoin();
                if (coin.intersects(mario)) {
                	Game.coins++;
                	Game.scores += 100;
                    coins.remove(coin);
                }
            }
            
         // Move the enemies & handle their death & Mario's death
            Enemy[] es = new Enemy[enemies.size()];
            Enemy[] dead_es = new Enemy[dead_turtels.size()];
            
            enemies.toArray(es);
            for (Enemy enemy : es) {
                if (enemy.dead) {
                	if(mario.v_x > 0 && mario.pos_x + mario.width >=  MAX_MARIO_X)
                		enemy.v_x = -GROUND_X_VELOCITY;
                	else 
                		enemy.v_x = 0;

                	if(enemy.sec_dead == 20) {
                		if(enemy.label == "Goomba") enemies.remove(enemy);
                	}
                		
                	else 
                		enemy.sec_dead += 1;
                	
                	if(enemy.label == "GreenKoopaTroop" && enemy.sec_dead == 20) {
                		             		
                		if(enemy.intersectsRight(mario)) {
                			enemy.intersectsRight = true;
                			enemy.intersectsLeft = false;
                			//double tmp = enemy.v_x;
                			//enemy.v_x = 1.5 * ENEMY_X_VELOCITY;
                			dead_turtels.add(enemy);
                			//enemy.v_x = tmp;
                		}
                		else if(enemy.intersectsLeft(mario)) {
                			enemy.intersectsRight = false;
                			enemy.intersectsLeft = true;
                			//enemy.v_x = - 2 * ENEMY_X_VELOCITY;
                			dead_turtels.add(enemy);
                		}	
                		if(enemy.intersectsRight) {
                			enemy.pos_x -= ENEMY_X_VELOCITY;
                		}
                		if(enemy.intersectsLeft) {
                			enemy.pos_x += ENEMY_X_VELOCITY;
                		}
                	}
                		
                }
                else {
	                if (enemy.startDistance <= distanceTravelled + 50 && !mario.dead) {
	                    enemy.onScreen = true;
	                    if(mario.pos_x + mario.width >= MAX_MARIO_X && mario_right_move) {
	                    	enemy.v_x = -ENEMY_X_VELOCITY/2 - MARIO_X_VELOCITY;
	                    } else {
	                    	enemy.v_x = -ENEMY_X_VELOCITY;
	                    }
	                }
	                if (enemy.offScreenLeft()) enemies.remove(enemy);
	                if (!mario.dead && !enemy.dead && enemy.intersectsTop(mario) && mario.reachedMaxHeight) {
	                	Game.scores = enemy.incrementScore(Game.scores);
	                    enemy.dead = true;
	                } else if (!mario.dead && !enemy.dead && (enemy.intersectsLeft(mario)
	                        || enemy.intersectsRight(mario))) {
	                	Game.lives--;
	                    mario.dead = true;
	                }
                }
                
                for(Enemy deadturtel : dead_turtels) {
                	if(deadturtel.intersects(enemy)) {
                		if(!enemy.dead)
                			Game.scores = enemy.incrementScore(Game.scores);
                		enemy.dead = true;
                		
                	}
                }
                	
                enemy.move();   
            }
            
            int[] serial = {1}; //store the number of top stair
            Pipe[] pi = new Pipe[1];
            pi[0] = endpipe;
            mario.standOnObj(pi, serial, mario);
            marioMaxY.add(mario.max_y);
            // win the game if Mario intersects the right half of the castle
            /*if (endpipe.intersectsEndPipe(mario)) {
            	timer.stop();
            	//gameWon = true;
            	finalScore = Game.scores;
            	hs.addHighScore(new HighScore(username, finalScore));
            	holder.setScene(2);
            	holder.setValue(true);
            	return;
            }*/
            
            for (int j = 0; j < bricks.size(); j ++) {
            		if(bricks.get(j).intersectsRight(mario)) {
                		mario.pos_x = bricks.get(j).pos_x + bricks.get(j).width;
                	}
                	else if(bricks.get(j).intersectsLeft(mario)) {
                		mario.pos_x = bricks.get(j).pos_x - mario.width;
                	}	
            }
            
            // Stop everything from moving if mario is dead;
            if (mario.dead) {
            	
                GroundTile.vel_x = 0;
                Coin.vel_x = 0;
                Game.coins = 0;
            	Game.scores = 0;
                for (Enemy enemy : enemies) enemy.v_x = 0;

                if (Game.lives > 0) 
                {
                	timer.stop();
                	holder.setScene(1);
                    holder.setValue(true);
                }
                else gameOver = true;
            }
            
            /*score_label.setText("Score: " + Game.scores);
            lives_label.setText("Lives: " + Game.lives);
            coins_label.setText("Coins: " + Game.coins);*/
            
            mario.max_y = findMin(marioMaxY);
            marioMaxY.clear();
            // update the display
            repaint();
        } 
        else 
        {
            // If Mario is dead, check to see if the game should be over
            /*lives_label.setText("Lives: " + Game.lives);*/
            
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the castle first since it should be in the background
        
        

        for(BushAndHill bushandhill : bushAndHill)
        {
            if (bushandhill.pos_x <= COURT_WIDTH && bushandhill.pos_x + bushandhill.width >=0) {
            	//System.out.println("*");
            	bushandhill.draw(g);
            }
        }

        for (Brick brick : bricks) {
            if (brick.pos_x <= COURT_WIDTH && brick.pos_x + brick.width >=0) {
            	brick.draw(g);
            }
        }
        // Draw the ground if they're on screen
        for (int i = 0; i < ground.length; i++) {
            if ((ground[i].pos_x <= COURT_WIDTH && ground[i].pos_x >= 0)
                    || (ground[i].pos_x + ground[i].width <= COURT_WIDTH 
                        && ground[i].pos_x + ground[i].width >= 0)) 
                ground[i].draw(g);
        }
        endpipe.draw(g);
        // Draw the enemies if they're on screen
        for (Enemy enemy : enemies) {
            if (enemy.pos_x <= COURT_WIDTH && enemy.pos_x + enemy.width >=0 && enemy.onScreen) {
                //System.out.print("*");
            	enemy.draw(g);
            }
        }
        
        // Draw the coins if they're on screen
        for (Coin coin : coins) {
            coin.draw(g);
            if (coin.pos_x <= COURT_WIDTH && coin.pos_x + coin.width >= 0) {
                coin.draw(g);
            }
        }
        for (Cloud cloud : clouds) {
            if (cloud.pos_x <= COURT_WIDTH && cloud.pos_x + cloud.width >=0) {
            	cloud.draw(g);
            }
        }
        
        for(Enemy dead_turtel: dead_turtels) {
        	dead_turtel.draw(g);
        }
        
        //endpipe.draw(g);
        // Draw Mario
        mario.draw(g);
        new Font(50,20,"Score:"+Integer.toString(Game.scores)).draw(g);
        new Font(300,20,"@:"+Integer.toString(Game.coins)).draw(g);
        new Font(510,20,"Mario").drawMario(g);
        new Font(530,20,":"+Integer.toString(Game.lives)).draw(g);
        if(gameOver) {
        	System.out.println("*");
        	new Font(50,50,"Gameover! Press 'R' to play again!").draw(g);
        }
        	
        	
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
    
    public int findMin(LinkedList<Integer> link) {
        int min = 1000;
        for (int serial : link) {
        	if(serial < min)
        		min = serial;
        }
        return min;
    }
}
