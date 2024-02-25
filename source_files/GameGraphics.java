import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import java.util.Iterator;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GameGraphics extends JPanel implements Runnable ,MouseListener, MouseMotionListener{

	private static final long serialVersionUID = 1L;
	Jet jet;
	BasicEnemy basicEnemy;
	private boolean running;
	Boolean mouseUp, mouseDown, mouseRight, mouseLeft;
	Boolean mousePressed, isDragging;
	String filePath = "C:/Users/Asus/eclipse-workspace/212term/src/scoreboard.txt";
	
	
	private List<Fire> bullets;
	private static List<EnemyJet> enemies;
	private List<Fire> enemyBullets; 
	
	int screen_X = 1200;
	int screen_Y = 800;
	int Level;
	int Score;
	Clip shootSound;
	Clip alienDeath;
	private JLabel scoreboard;
	private JLabel health;
	private JLabel level;
	private JLabel gameOver;
	public int boot;
	public boolean gameRunning;
	
	
	
	public GameGraphics() {
		jet = new Jet(250,500,10);
		running = true;
		setPreferredSize(new Dimension(screen_X, screen_Y));
		setFocusable(true);

		addMouseListener(this);
		addMouseMotionListener(this);

		setLayout(null);
		
		try {
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(GameGraphics.class.getResourceAsStream("fire.wav"));
	        shootSound = AudioSystem.getClip();
	        shootSound.open(audioInputStream);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		
		try {
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(GameGraphics.class.getResourceAsStream("explosion.wav"));
	        alienDeath = AudioSystem.getClip();
	        alienDeath.open(audioInputStream);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		bullets = new ArrayList<>();
		enemies = new ArrayList<>();
		enemyBullets = new ArrayList<>();
		Score = 0;
		gameRunning = false;
		scoreboard = new JLabel("SCORE: " + Score);
		scoreboard.setBounds(10, 10, 100, 20);
		scoreboard.setForeground(Color.RED);
		
		health = new JLabel("Health: " + jet.health);
		health.setBounds(110, 10, 100, 20);
		health.setForeground(Color.RED);
		
		level = new JLabel("LEVEL: " + Level);
		level.setBounds(210,10,100,20);
		level.setForeground(Color.RED);
		
		gameOver = new JLabel("GAME OVER");
		gameOver.setBounds(400,300,400,200);
		gameOver.setForeground(Color.ORANGE);
		Font font = gameOver.getFont();
		Font newFont = font.deriveFont(font.getSize() + 50f);
		gameOver.setFont(newFont);
		bindKeys();
		Thread starter = new Thread(this);
		starter.start();
			add(scoreboard);
			add(health);
			add(level);


			setBackground(Color.BLACK);
			

			enemyFire.start();

			timer.schedule(addBasicEnemy, 0, 2000);

			enemySlider.start();
	}
	
	public GameGraphics(Dimension dimension) {
		setSize(dimension);
		setPreferredSize(dimension);
		addMouseListener(this);
		setFocusable(true);
	}
	
	Timer timer = new Timer();

	TimerTask addBasicEnemy = new TimerTask() {
		public void run() {
			int randomX = 10 + (int) (Math.random()*(screen_X-100));
			int randomY = (int) (Math.random()*(-100));
			EnemyJet enemy = new BasicEnemy(randomX,randomY,74,70,3,1);
			if(Level == 1) {
				if(enemies.size() < 5) {	
					enemies.add(enemy);
					if(enemyBullets.size() < 10) {
						enemyShoot(enemy);
					}
				}
			}
			if(Level == 2) {
				timer.schedule(addMediumEnemy, 0, 2000);
				addBasicEnemy.cancel();
			}
		}
	};
	TimerTask addMediumEnemy = new TimerTask() {
		
		public void run() {
			int randomX = 10 + (int) (Math.random()*(screen_X-100));
			int randomY = (int) (Math.random()*(-100));
			EnemyJet enemy = new BasicEnemy(randomX,randomY,74,70,4,3);
			if(enemies.size() < 10) {	
				enemies.add(enemy);
				if(enemyBullets.size() < 10) {
					enemyShoot(enemy);
				}
			}
			if(Level == 3) {
				timer.schedule(addHardEnemy,0 , 2000);
				addMediumEnemy.cancel();
			}
		}
	};
	TimerTask addHardEnemy = new TimerTask() {
		
		public void run() {
			int randomX = 10 + (int) (Math.random()*(screen_X-100));
			int randomY = (int) (Math.random()*(-100));
			EnemyJet enemy = new BasicEnemy(randomX,randomY,74,70,4,5);
			if(enemies.size() < 15) {	
				enemies.add(enemy);
				if(enemyBullets.size() < 10) {
					enemyShoot(enemy);
				}
			}

		}
	};
	
	private void enemyShoot(EnemyJet enemyjet) {
		TimerTask enemyShoot = new TimerTask() {
			public void run() {
				enemyBullets.add(new Fire(enemyjet.getEnemyJetX()-6, enemyjet.getEnemyJetY()+ 10,47,12));
			}
		};
		int delay = 9000 + (int) (Math.random() * 15000);
		timer.schedule(enemyShoot, 0, delay);
		
	}
	
	@Override
	protected void paintComponent(Graphics g) {

		jet.draw(g);
		scoreboard.setText("SCORE: " + Score);
		health.setText("Health:" + jet.health);
		level.setText("LEVEL: " + Level);
		gameOver.setText("GAME OVER");
		g.setColor(Color.BLUE);
		for(Fire bullet : bullets) {
			g.fillRect(bullet.fire_x, bullet.fire_y, bullet.width, bullet.height);
		}
		g.setColor(Color.RED);
		for(Fire enemybullet : enemyBullets) {
			g.fillRect(enemybullet.fire_x, enemybullet.fire_y, enemybullet.width, enemybullet.height);
		}
		g.setColor(Color.ORANGE);
		for(EnemyJet enemy : enemies) {
			enemy.draw(g);
		}

	}
	
	
	private void bindKeys() {
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "moveUpPressed");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true), "moveUpReleased");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), "moveDownPressed");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "moveDownReleased");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "moveRightPressed");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), "moveRightReleased");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "moveLeftPressed");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), "moveLeftReleased");

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false), "shootPressed");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true), "shootReleased");

        actionMap.put("moveUpPressed", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                jet.setMoveUp(true);
            }
        });
        actionMap.put("moveUpReleased", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
            	jet.setMoveUp(false);
            }
        });
        actionMap.put("moveDownPressed", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
            	jet.setMoveDown(true);
            }
        });
        actionMap.put("moveDownReleased", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
            	jet.setMoveDown(false);
            }
        });
        actionMap.put("moveRightPressed", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
            	jet.setMoveRight(true);
            }
        });
        actionMap.put("moveRightReleased", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
            	jet.setMoveRight(false);
            }
        });
        actionMap.put("moveLeftPressed", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
            	jet.setMoveLeft(true);
            }
        });
        actionMap.put("moveLeftReleased", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
            	jet.setMoveLeft(false);
            }
        });
        actionMap.put("shootPressed", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                bullets.add(new Fire(jet.getJet_x()-6, jet.getJet_y()-76, 47, 12));
            }
        });
    }
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		bullets.add(new Fire(jet.getJet_x()-6, jet.getJet_y()-76, 47, 12));

	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		
		int x = e.getX();
		int y = e.getY();
		if(isDragging == true) {
			if(x < jet.getJet_x()) {
				if(jet.getJet_x() >= 50) {
					jet.setJet_x(jet.getJet_x() - jet.geJet_speed());
					repaint();
				}
			}
			if(jet.getJet_x() < (screen_X-46)) {	
				if(x > jet.getJet_x()) {
					jet.setJet_x(jet.getJet_x() + jet.geJet_speed());
					repaint();
				}
			}
			if(jet.getJet_y() >= 40) {
				if(y < jet.getJet_y()) {
					jet.setJet_y(jet.getJet_y() - jet.geJet_speed());
					repaint();
				}
			}
			if(jet.getJet_y() <= (screen_Y-46)) {
				if(y > jet.getJet_y()) {
					jet.setJet_y(jet.getJet_y()+jet.geJet_speed());
					repaint();
				}
			}
		}
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
		
	}


	@Override
	public void mousePressed(MouseEvent e) {
		isDragging = true;
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		jet.setMoveDown(false);
		jet.setMoveLeft(false);
		jet.setMoveRight(false);
		jet.setMoveUp(false);
		isDragging = false;
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
		
	}

	

	public void run() {
		while (running) {
			if(jet.health == 0) {
				add(gameOver);

					try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
						writer.append(" " + Score);
			            writer.newLine();
			        } catch (IOException e) {
			            System.err.println("Error writing to the file: " + e.getMessage());
			        }

				running = false;
			}
			
			if(Score < 100 ) {
				Level = 1;
			}
			if(Score >= 100) {
				Level = 2;
			}
			if(Score >= 300) {
				Level = 3;
			}

			Iterator<Fire> bulletIterator = bullets.iterator();
			
			while(bulletIterator.hasNext()) {
				Fire bullet = bulletIterator.next();
				bullet.fire_y -= 5;
				if(bullet.fire_y < 0) {
					bulletIterator.remove();
					break;
				}
				Iterator<EnemyJet> enemyIterator = enemies.iterator();
				while(enemyIterator.hasNext()) {
					EnemyJet enemmy = enemyIterator.next();

					if(bullet.intersects(enemmy.enemyjet_x, enemmy.enemyjet_y, enemmy.enemyjet_width, enemmy.enemyjet_height)) {
						if(enemmy.enemyjet_speed == 1) {
							Score += 10;
						}
						if(enemmy.enemyjet_speed == 3){
							Score += 20;
						}
						if(enemmy.enemyjet_speed == 5) {
							Score += 30;
						}
						enemmy.getDamaged();
						if (!shootSound.isRunning()) {
						shootSound.setFramePosition(0);
				        shootSound.start();
						}
						bulletIterator.remove();
						
						break;
					}
					else {
						
					}
				}
			}
			
			repaint();
			try {				
					if(jet.getMoveDown() == true) {
						if(jet.getJet_y() <=(screen_Y-46)) {  
							jet.setJet_y(jet.getJet_y()+jet.geJet_speed());
						}
					}
					if(jet.getMoveUp() == true) {
						if(jet.getJet_y() >= 40) {
							jet.setJet_y(jet.getJet_y() - jet.geJet_speed());
						}
					}
					if(jet.getMoveLeft() == true) {
						if(jet.getJet_x() >= 50) {
							jet.setJet_x(jet.getJet_x() - jet.geJet_speed());
						}
					}
					if(jet.getMoveRight() == true) {
						if(jet.getJet_x() < (screen_X-46)) { 
							jet.setJet_x(jet.getJet_x() + jet.geJet_speed());
						}
					}
					
				Thread.sleep(10);
			}
			
				catch(Exception e){
					
				}
		}
	}
	Thread enemyFire = new Thread(() -> {
		while(running) {
			Iterator<Fire> enemyFire = enemyBullets.iterator();
			
			while(enemyFire.hasNext()) {
				Fire fire = enemyFire.next();
				fire.fire_y += 5;
				if(fire.fire_y > 800) {
					enemyFire.remove();
					break;
				}

				
					if(fire.intersects(jet.getJet_x()-50, jet.getJet_y() + 20, jet.geJet_width(), jet.getJet_height())) {
						jet.health -=1;
						enemyFire.remove();

						break;
					
				}
			}

			try {
				Thread.sleep(50);
			}
			catch(Exception e){
				
			}
		}
	});
	Thread enemySlider = new Thread(() -> {
		while(running) {
			Iterator<EnemyJet> kill = enemies.iterator();
			while(kill.hasNext()) {
				EnemyJet enemy = kill.next();
				enemy.move(screen_X, screen_Y);
				if(enemy.getEnemyJetY()>800) {
					kill.remove();
				}
				if(enemy.health <= 0) {
					
					Score += enemy.getScore();
					if (!alienDeath.isRunning()) {
						alienDeath.setFramePosition(0);
				        alienDeath.start();
						}
					kill.remove();
				}

			}
			try {
				Thread.sleep(20);
			}
			catch(Exception e){
				
			}
		}
	});

	
}

