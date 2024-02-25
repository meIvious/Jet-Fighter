
public class Fire {
	int width;
	int height;
	int fire_x;
	int fire_y;
	public Fire(int x, int y, int h, int w) { // 47x12
		this.fire_x = x;
		this.fire_y = y;
		this.height = h;
		this.width = w;
	}
	public int getFireY() {
		return fire_y;
	}
	public void setFireY(int k) {
		this.fire_y = k;
	}
	public boolean intersects(int x, int y, int h, int w) {
		int thisLeft = this.fire_x;
		int thisRight = this.fire_x + this.width;
		int thisTop = this.fire_y;
		int thisBottom = this.fire_y + this.height;
		
		int otherLeft = x;
		int otherRight = x + w;
		int otherTop = y;
		int otherBottom = y + h;
		
		boolean intersectsX = thisLeft < otherRight && thisRight > otherLeft;
		boolean intersectsY = thisTop < otherBottom && thisBottom > otherTop;
		
		return intersectsX && intersectsY;
	}
}
