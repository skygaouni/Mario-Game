package mario;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

public class Game implements Runnable {
	public BooleanValueHolder holder;
	public static int scores = 0;
	public static int coins = 0;
	public static int lives = 3;
    @Override
    public void run() {
     // NOTE : recall that the 'final' keyword notes inmutability
        // even for local variables.

        // Top-level frame in which game components live
        // Be sure to change "TOP LEVEL FRAME" to the name of your game
        final JFrame frame = new JFrame("Super Mario");
        frame.setLocation(300, 200);
        //frame.setUndecorated(true);
        

        // Status panel contains the Score/Coins/Lives
        /*final JPanel status_panel = new JPanel();
        status_panel.setLayout(new GridLayout());
        frame.add(status_panel, BorderLayout.NORTH);
        
        final JLabel score_label = new JLabel("Score: 0", SwingConstants.CENTER);
        status_panel.add(score_label);
        
        final JLabel coins_label = new JLabel("Coins: 0", SwingConstants.CENTER);
        status_panel.add(coins_label);
        
        final JLabel lives_label = new JLabel("Lives: 3", SwingConstants.CENTER);
        status_panel.add(lives_label);*/

        // Control Panel with Instructions & High Score panel buttons
        /*final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.SOUTH);*/
        
        // Card Layout for the Welcome Screen, Game, High Scores & Instructions
        final CardLayout cl = new CardLayout();
        final JPanel contentPanel = new JPanel(cl);/*在這段程式碼中，cl 是一個 CardLayout 對象。CardLayout 是一種佈局管理器，它允許你在同一個容器中放置多個面板，並通過切換顯示不同的面板。這在實現分步向導、選項卡界面或多屏應用程序時非常有用。*/
        frame.add(contentPanel, BorderLayout.CENTER);
        
        
        
        // Welcome Screen
        final usernamePanel welcomeScreen = new usernamePanel();
        welcomeScreen.setLayout(null);
        welcomeScreen.setBackground(new Color(107, 140, 255));
        
        // Welcome Screen: Title Image
        final ImageIcon titleImage = new ImageIcon("TitleScreen.png");
        final JLabel title = new JLabel(titleImage);
        title.setBounds(170, 25, 300, 150);
        welcomeScreen.add(title);
        
        final ImageIcon cloud11 = new ImageIcon("cloud1.png");
        final JLabel cloud_11 = new JLabel(cloud11);
        cloud_11.setBounds(75, 100, 50, 50);
        welcomeScreen.add(cloud_11);
        
        final ImageIcon cloud21 = new ImageIcon("cloud2.png");
        final JLabel cloud_21 = new JLabel(cloud21);
        cloud_21.setBounds(550, 100, 75, 50);
        welcomeScreen.add(cloud_21);
        
        final ImageIcon cloud31 = new ImageIcon("cloud3.png");
        final JLabel cloud_31 = new JLabel(cloud31);
        cloud_31.setBounds(25, 25, 100, 50);
        welcomeScreen.add(cloud_31);
        
        final ImageIcon [] floor = new ImageIcon[20];
        final JLabel[] floors = new JLabel[20];
        for (int i = 0; i < floor.length; i++) {
            floor[i] = new ImageIcon("Ground.gif");
            floors[i] = new JLabel(floor[i]);
            floors[i].setBounds(i*32, 368, 32, 32);
            welcomeScreen.add(floors[i]);
        }
        final ImageIcon hillimage = new ImageIcon("Hill2.png");
        final JLabel hill = new JLabel(hillimage);
        hill.setBounds(0, 308, 137, 60);
        welcomeScreen.add(hill);

        final ImageIcon small_hillimage = new ImageIcon("Hill1.png");
        final JLabel small_hill = new JLabel(small_hillimage);
        small_hill.setBounds(640-76, 338, 76, 30);
        welcomeScreen.add(small_hill);
        
        final ImageIcon bushimage = new ImageIcon("Bush3.png");
        final JLabel bush = new JLabel(bushimage);
        bush.setBounds(450, 336, 64, 32);
        welcomeScreen.add(bush);
        
        final ImageIcon marioimage = new ImageIcon("Mario_Jump.gif");
        final JLabel mario = new JLabel(marioimage);
        mario.setBounds(150, 320, 34, 32);
        welcomeScreen.add(mario);
        
        // Welcome Screen: Username text box & action listener
        final JTextField usernameBox = new JTextField(10);
        usernameBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameBox.setBounds(5000, 200, 114, 20);
        usernameBox.setBorder(BorderFactory.createEmptyBorder());
        usernameBox.setForeground(Color.black); // 設置前景色為白色
        usernameBox.setBackground(new Color(107, 140, 255));
        usernameBox.setText("unnamed");
        usernameBox.setMaximumSize(usernameBox.getPreferredSize());
        usernameBox. setOpaque(false);
        
        ActionListener submitUsername = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String userText = usernameBox.getText();
                userText.replaceAll("\\s", "");
                if (userText.length() == 0) GameCourt1.username = "unnamed";
                else if (userText.length() > 50) GameCourt1.username = userText.substring(0, 50);
                else GameCourt1.username = userText;
                cl.show(contentPanel, "Instructions Screen");
            }
        };
        usernameBox.addActionListener(submitUsername);
        
        
        usernameBox.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
            	JTextField textField = (JTextField) e.getSource();
                String text = textField.getText();
                welcomeScreen.getText(text);
            }

            public void keyTyped(KeyEvent e) {
            	
            }

            public void keyPressed(KeyEvent e) {
            }
        });
        
        welcomeScreen.add(usernameBox);

        // Let's Play button
        //final JButton letsPlay = new JButton("Let's Play!");
        //letsPlay.addActionListener(submitUsername);
        //letsPlay.setBounds(263, 275, 114, 20);
        //welcomeScreen.add(letsPlay);
        // Let's Play button
        final JButton letsPlay = new JButton();
        letsPlay.setBounds(209, 225, 223, 37);
        letsPlay.addActionListener(submitUsername);
        letsPlay.setIcon(new ImageIcon("letsplay.png"));
        letsPlay.setOpaque(false);
        letsPlay.setContentAreaFilled(false);
        letsPlay.setBorderPainted(false);
        welcomeScreen.add(letsPlay);
        
        contentPanel.add(welcomeScreen, "Welcome Screen"); // add Welcome Screen to contentPanel
        
        
        // Instructions Screen
        final JPanel instructionsScreen = new InstructionPanel();
        instructionsScreen.setLayout(null);
        instructionsScreen.setBackground(new Color(107, 140, 255));
        contentPanel.add(instructionsScreen, "Instructions Screen");

        final ImageIcon cloud12 = new ImageIcon("cloud1.png");
        final JLabel cloud_12 = new JLabel(cloud12);
        cloud_12.setBounds(75, 100, 50, 50);
        instructionsScreen.add(cloud_12);
        
        final ImageIcon cloud22 = new ImageIcon("cloud2.png");
        final JLabel cloud_22 = new JLabel(cloud22);
        cloud_22.setBounds(550, 100, 75, 50);
        instructionsScreen.add(cloud_22);
        
        final ImageIcon cloud32 = new ImageIcon("cloud3.png");
        final JLabel cloud_32 = new JLabel(cloud32);
        cloud_32.setBounds(25, 25, 100, 50);
        instructionsScreen.add(cloud_32);
        
        final ImageIcon [] floor2 = new ImageIcon[20];
        final JLabel[] floors2 = new JLabel[20];
        for (int i = 0; i < floor.length; i++) {
            floor2[i] = new ImageIcon("Ground.gif");
            floors2[i] = new JLabel(floor2[i]);
            floors2[i].setBounds(i*32, 368, 32, 32);
            instructionsScreen.add(floors2[i]);
        }
        final ImageIcon hillimage2 = new ImageIcon("Hill2.png");
        final JLabel hill2 = new JLabel(hillimage2);
        hill2.setBounds(0, 308, 137, 60);
        instructionsScreen.add(hill2);

        final ImageIcon small_hillimage2 = new ImageIcon("Hill1.png");
        final JLabel small_hill2 = new JLabel(small_hillimage2);
        small_hill2.setBounds(640-76, 338, 76, 30);
        instructionsScreen.add(small_hill2);
        
        final ImageIcon bushimage2 = new ImageIcon("Bush3.png");
        final JLabel bush2 = new JLabel(bushimage2);
        bush2.setBounds(450, 336, 64, 32);
        instructionsScreen.add(bush2);
        
        final ImageIcon marioimage2 = new ImageIcon("Mario_Jump.gif");
        final JLabel mario2 = new JLabel(marioimage2);
        mario2.setBounds(150, 320, 34, 32);
        instructionsScreen.add(mario2);
        /*final JPanel instructionsScreen = new JPanel();
        instructionsScreen.setLayout(new BoxLayout(instructionsScreen, BoxLayout.Y_AXIS));
        instructionsScreen.setBackground(Color.BLACK);
        contentPanel.add(instructionsScreen, "Instructions Screen");
        final JLabel instructionHelp = new JLabel("<html>" + "Welcome to Super Mario!<br>" +
        "You will be controlling Mario using the left and right arrow keys to move him<br>" +
        "left and right (respectively) and the up arrow key to make him jump. Your goal is to<br>" +
        "make it to the end of the game. Pick up coins along the way to add to your score.<br>" +
        "Jump on enemies to kill them and add to your score but don't touch them directly or <br>" +
        "they will kill you. Don't fret if you die though, you'll have 3 chances to get to<br>" +
        "the end of the level. Check out the high scores too and see if you can beat them.<br>" +
        "Good luck!</html>");
        instructionHelp.setForeground(Color.WHITE);
        instructionsScreen.add(instructionHelp);*/
        

        holder = new BooleanValueHolder();
        
        // Main playing area
        /*final GameCourt court3 = new GameCourt(score_label, coins_label, lives_label,holder);
        court3.setBackground(new Color(107, 140, 255));
        contentPanel.add(court3, "Game3");*/
        //if(court3.gameWon) cl.show(contentPanel, "Instructions Screen");
        final GameCourt1 court1 = new GameCourt1(/*score_label, coins_label, lives_label, */holder);
        court1.setBackground(new Color(107, 140, 255));
        contentPanel.add(court1, "Game1");
        //court1.requestFocusInWindow();
        //cl.show(contentPanel, "Game1");
        
        final GameCourt2 court2 = new GameCourt2(/*score_label, coins_label, lives_label, */holder);
        court2.setBackground(new Color(0, 0, 0));
        contentPanel.add(court2, "Game2");
        
        final GameCourt3 court3 = new GameCourt3(/*score_label, coins_label, lives_label, */holder);
        court3.setBackground(new Color(107, 140, 255));
        contentPanel.add(court3, "Game3");
        
        


        
        // High Score Screen
        final MyPanel highScoreScreen = new MyPanel();
        highScoreScreen.setLayout(null);
        highScoreScreen.setBackground(new Color(107, 140, 255));
        final HighScores hs = new HighScores();
        contentPanel.add(highScoreScreen, "High Score Screen");
        
        // Go to game buttons for insturcion & high score screens
        final JButton instructionsToGame = new JButton();
        instructionsToGame.setIcon(new ImageIcon("start.png"));
        instructionsToGame.setBounds(250, 300, 112, 32);
        instructionsToGame.setOpaque(false);
        instructionsToGame.setContentAreaFilled(false);
        instructionsToGame.setBorderPainted(false);
        final JButton highScoresToHome = new JButton();
        highScoresToHome.setIcon(new ImageIcon("Home.png"));
        highScoresToHome.setOpaque(false);
        highScoresToHome.setContentAreaFilled(false);
        highScoresToHome.setBorderPainted(false);
        highScoresToHome.setBounds(250, 330, 92, 30);
        
        ActionListener toGame = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!(GameCourt1.username.equals(""))) {
                	//System.out.println(KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner());
                    court1.reset();
                	cl.show(contentPanel, "Game1");
                    GameCourt1.playing = true;
                    court1.requestFocus();
                } else {
                    cl.show(contentPanel, "Welcome Screen");
                }
            }
        };
        ActionListener home = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                	cl.show(contentPanel, "Welcome Screen");
            }
        };
        highScoresToHome.setAlignmentX(Component.CENTER_ALIGNMENT);
        instructionsToGame.addActionListener(toGame);
        highScoresToHome.addActionListener(home);
        instructionsScreen.add(instructionsToGame);
        
        // High Scores button & action listener
        final JButton highScores = new JButton();
        highScores.setBounds(259, 275, 123, 27);
        highScores.setOpaque(false);
        highScores.setContentAreaFilled(false);
        highScores.setBorderPainted(false);
        highScores.setIcon(new ImageIcon("top10.png"));
        highScores.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GameCourt1.playing = false;
                highScoreScreen.removeAll();
                JLabel highScoreIntro = new JLabel("High Scores:");
                highScoreIntro.setForeground(Color.WHITE);
                highScoreIntro.setAlignmentX(Component.CENTER_ALIGNMENT);
                //highScoreScreen.add(highScoreIntro);
                List<HighScore> highScoreList = hs.getTopTenHighScores();
                if (highScoreList.size() == 0) {
                    JLabel noHS = new JLabel("None yet.");
                    noHS.setForeground(Color.WHITE);
                    noHS.setAlignmentX(Component.CENTER_ALIGNMENT);
                    highScoreScreen.add(noHS);
                } else {
                    int i = 1;
                    for (HighScore h : highScoreList) {
                    	highScoreScreen.setHighScoreList(hs);
                    	//highScoreScreen.drawFont(20, i*50, i + ". " + h.getUsername() + " " + h.getScore());
                        JLabel nextHS = new JLabel(i + ". " + h.getUsername() + " " + h.getScore());
                        nextHS.setForeground(Color.WHITE);
                        nextHS.setAlignmentX(Component.CENTER_ALIGNMENT);
                        //highScoreScreen.add(nextHS);
                        i++;
                    }
                }
                
                highScoreScreen.add(highScoresToHome);
                cl.show(contentPanel, "High Score Screen");
                //highScoreScreen.requestFocus();
            }
        });
        welcomeScreen.add(highScores);
        
        final JButton exit = new JButton();
        exit.setBounds(275, 320, 91, 30);
        exit.setOpaque(false);
        exit.setContentAreaFilled(false);
        exit.setBorderPainted(false);
        exit.setIcon(new ImageIcon("Exit.png"));
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	System.exit(0);
            }
        });
        welcomeScreen.add(exit);

        
        // Instructions button
        /*final JButton instructions = new JButton("Instructions");
        instructions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GameCourt.playing = false;
                cl.show(contentPanel, "Instructions Screen");
            }
        });*/
        /*control_panel.add(instructions);*/

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
        // 添加監聽器
        holder.addPropertyChangeListener(evt -> {
        	System.out.println("Boolean value changed from " + evt.getOldValue() + " to " + evt.getNewValue());
        	if(holder.getValue() ==true && holder.getScene() == 1)
        	{
        		//System.out.println(KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner());
        		System.out.println("Change to Game court1");
        		court1.reset();
        		cl.show(contentPanel, "Game1");
        		contentPanel.getComponent(2).requestFocusInWindow();
        		//court1.requestFocus();
        		//System.out.println(KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner());
        		holder.setValue(false);
        	}
        	else if(holder.getValue() ==true && holder.getScene() == 2)
        	{
        		//System.out.println("*"+KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner());
        		System.out.println("Change to Game court2");
        		court2.reset();
        		cl.show(contentPanel, "Game2");
        		contentPanel.getComponent(3).requestFocusInWindow();
        		//court2.requestFocusInWindow();
        		//System.out.println(KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner());
        		holder.setValue(false);
        	}
        	else if(holder.getValue() ==true && holder.getScene() == 3)
        	{
        		//System.out.println(KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner());
        		System.out.println("Change to Game court3");
        		court3.reset();
        		cl.show(contentPanel, "Game3");
        		contentPanel.getComponent(4).requestFocusInWindow();
        		//court3.requestFocus();
        		//System.out.println(KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner());
        		holder.setValue(false);
        	}    
        	else if(holder.getValue() ==true && holder.getScene() == 4)
        	{
        		System.out.println(KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner());
        		System.out.println("Change to High Score Screen");
        		/*cl.show(contentPanel, "High Score Screen");
        		holder.setValue(false);*/
        		GameCourt1.playing = false;
                highScoreScreen.removeAll();
                JLabel highScoreIntro = new JLabel("High Scores:");
                highScoreIntro.setForeground(Color.WHITE);
                highScoreIntro.setAlignmentX(Component.CENTER_ALIGNMENT);
                //highScoreScreen.add(highScoreIntro);
                List<HighScore> highScoreList = hs.getTopTenHighScores();
                if (highScoreList.size() == 0) {
                    JLabel noHS = new JLabel("None yet.");
                    noHS.setForeground(Color.WHITE);
                    noHS.setAlignmentX(Component.CENTER_ALIGNMENT);
                    highScoreScreen.add(noHS);
                } else {
                    int i = 1;
                    for (HighScore h : highScoreList) {
                    	highScoreScreen.setHighScoreList(hs);
                    	//highScoreScreen.drawFont(20, i*50, i + ". " + h.getUsername() + " " + h.getScore());
                        JLabel nextHS = new JLabel(i + ". " + h.getUsername() + " " + h.getScore());
                        nextHS.setForeground(Color.WHITE);
                        nextHS.setAlignmentX(Component.CENTER_ALIGNMENT);
                        //highScoreScreen.add(nextHS);
                        i++;
                    }
                }
                
                highScoreScreen.add(highScoresToHome);
                cl.show(contentPanel, "High Score Screen");
                //highScoreScreen.requestFocus();
                holder.setValue(false);
        	}   
        });
        // Start game
        System.out.println("Frame size: " + frame.getSize());
    }

    /*
     * Main method run to start and run the game Initializes the GUI elements
     * specified in Game and runs it IMPORTANT: Do NOT delete! You MUST include
     * this in the final submission of your game.
     */
    public static void main(String[] args) {
    	
        SwingUtilities.invokeLater(new Game());
    }
}
