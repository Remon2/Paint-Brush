import java.awt.Color;
import java.awt.Graphics;

public class Square extends Shape {

	public Square() {
	}

	public Square(Shape s) {
		super(s);
	}

	public Square(int x, int y, int width, int height, Color c, boolean b) {
		super(x, y, width, height, c, b);
	}

	public void draw(Graphics g) {
		int length = Math.max(getWidth(), getWidth());
		if (isFilled()) {
			g.fillRect(getX(), getY(), length, length);
		} else {
			g.drawRect(getX(), getY(), length, length);
		}
	}
}
