import java.awt.Image;
import java.net.URL;
import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;

public class Jet {
	private static boolean png = true;
	private Image icon;
	private int height, width;
	public int health;
	private int jet_x;
	private int jet_y;
	private int jet_speed;
	private Boolean moveRight;
	private Boolean moveLeft;
	private Boolean moveUp;
	private Boolean moveDown; 
	
	public Jet(int jet_x, int jet_y, int jet_speed) {
		Timer timer = new Timer();
		timer.schedule(switchIcon, 0, 200);
		this.jet_x = jet_x;
		this.jet_y = jet_y;
		this.jet_speed = jet_speed;
		this.moveRight = false;
		this.moveLeft = false;
		this.moveUp = false;
		this.moveDown = false;
		this.health = 3;
	}
	public void setJet_x(int x) {
		this.jet_x = x;
	}
	public int getJet_x() {
		return jet_x;
	}
	public void setJet_y(int y) {
		this.jet_y = y;
	}
	public int getJet_y() {
		return jet_y;
	}
	public int geJet_speed() {
		return jet_speed;
	}
	public int geJet_width() {
		return width;
	}
	public int getJet_height() {
		return height;
	}
	public void setMoveUp(Boolean k) {
		this.moveUp = k;
	}
	public Boolean getMoveUp() {
		return moveUp;
	}
	public void setMoveDown(Boolean k) {
		this.moveDown = k;
	}
	public Boolean getMoveDown() {
		return moveDown;
	}
	public void setMoveRight(Boolean k) {
		this.moveRight = k;
	}
	public Boolean getMoveRight() {
		return moveRight;
	}
	public void setMoveLeft(Boolean k) {
		this.moveLeft = k;
	}
	public Boolean getMoveLeft() {
		return moveLeft;
	}
	
	public void draw(Graphics window) {
		int playerX = getJet_x() - 43; 
		int playerY = getJet_y() - 28; 
		window.drawImage(icon, playerX, playerY,height,width,null);
	}
	Timer timer = new Timer();
	TimerTask switchIcon = new TimerTask() {
		public void run() {
			png = !png;
			if(png) {
				try {
					URL path = getClass().getResource("jet.png");
					icon = ImageIO.read(path);
					height = 86;
					width = 71;
				}
				catch(Exception e) {
					
				}
				
			}
			else {
				try {
					URL path = getClass().getResource("jet2.png");
					icon = ImageIO.read(path);
					height = 86;
					width = 71;
				}
				catch(Exception e) {
					
				}
			}
		}
	};
}
