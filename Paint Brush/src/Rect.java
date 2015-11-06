import java.awt.Color;
import java.awt.Graphics;

public class Rect extends Shape {

	public Rect() {
	}

	public Rect(Shape s) {
		super(s);
	}

	public Rect(int x, int y, int width, int height, Color c, boolean b) {
		super(x, y, width, height, c, b);
	}

	public void draw(Graphics g){
		if(isFilled()){
			g.fillRect(getX(), getY(), getWidth(), getHeight());
		}else{
			g.drawRect(getX(), getY(), getWidth(), getHeight());
		}
	}
}
