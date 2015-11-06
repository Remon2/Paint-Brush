import java.awt.Color;
import java.awt.Graphics;

public class Circle extends Shape {
	public Circle() {
	}

	public Circle(Shape s) {
		super(s);
	}

	public Circle(int x, int y, int width, int height, Color c, boolean b) {
		super(x, y, width, width, c, b);
	}

	public void draw(Graphics g) {
		int length = Math.max(getWidth(), getWidth());
		if (isFilled()) {
			g.fillOval(getX(), getY(), length, length);
		} else {
			g.drawOval(getX(), getY(), length, length);
		}
	}
}
