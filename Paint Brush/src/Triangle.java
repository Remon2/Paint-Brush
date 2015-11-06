import java.awt.Color;

public class Triangle extends Shape {
		
	public Triangle() {
	}

	public Triangle(Shape s) {
		super(s);
	}

	public Triangle(int x, int y, int width, int height, Color c, boolean b) {
		super(x, y, width, height, c, b);
	}
}
