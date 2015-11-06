

import java.awt.Color;
import java.awt.Graphics;

public class Ellipse extends Shape {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Ellipse(int x, int y, int width, int height, Color c, boolean b) {
		super(x, y, width, height, c, b);
	}

	public Ellipse(Shape s) {
		super(s);
	}

	public Ellipse() {
	}
	
	public void draw(Graphics g){
		if(isFilled()){
			g.fillOval(getX(), getY(), getWidth(), getHeight());
		}else{
			g.drawOval(getX(), getY(), getWidth(), getHeight());
		}
	}
}
