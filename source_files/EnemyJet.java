import java.awt.Graphics;

public abstract class EnemyJet {
	int health;
	int enemyjet_x;
	int enemyjet_y;
	int enemyjet_width;
	int enemyjet_height;
	int enemyjet_speed;
	boolean isDead;
	public int score;
	
	public EnemyJet(int x, int y, int width, int height, int health, int speed) {
		this.enemyjet_x = x;
		this.enemyjet_y = y;
		this.enemyjet_width = width;
		this.enemyjet_height = height;
		this.health = health;
		this.enemyjet_speed = speed;	
		this.score = (speed*5) + (health*5);
	}
	
	public void setEnemyJetX(int x) {
		this.enemyjet_x = x;
	}
	public void setEnemyJetY(int y) {
		this.enemyjet_y = y;
	}
	public void setEnemyJetWidth(int w) {
		this.enemyjet_width = w;
	}
	public void setEnemyJetHeight(int h) {
		this.enemyjet_height = h;
	}
	public int getEnemyJetX() {
		return enemyjet_x;
	}
	public int getEnemyJetY() {
		return enemyjet_y;
	}
	public int getEnemyJetWidth() {
		return enemyjet_width;
	}
	public int getEnemyJetHeight() {
		return enemyjet_height;
	}
	public int getScore() {
		return score;
	}
	public void getDamaged() {
		health -= 1;
	}
	
	public abstract void draw(Graphics g);
	public void move( int borderX, int borderY){

		if(enemyjet_y <= 100) {
			enemyjet_y += 1;
		}
		if(enemyjet_x >= borderX-100 || enemyjet_x <= 0) {
			enemyjet_speed *= -1;
			enemyjet_y += 40;
		}

		enemyjet_x += enemyjet_speed;
		
		if(enemyjet_y > 600) {
			isDead = true;
		}
	}
}
