import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Line extends Shape {
	private Point start;
	private Point end;
	private int x1;
	private int y1;
	private int x2;
	private int y2;
	private Color line_Color;

	public Line() {
	}

	public Line(Point p1, Point p2, Color c) {
		start = new Point(p1);
		end = new Point(p2);
		x1 = p1.x;
		y1 = p1.y;
		x2 = p2.x;
		y2 = p2.y;
		line_Color = c;
	}

	public Point getStart() {
		return start;
	}

	public void setStart(Point start) {
		this.start = start;
	}

	public Point getEnd() {
		return end;
	}

	public void setEnd(Point end) {
		this.end = end;
	}

	public int getX1() {
		return x1;
	}

	public void setX1(int x1) {
		this.x1 = x1;
	}

	public int getY1() {
		return y1;
	}

	public void setY1(int y1) {
		this.y1 = y1;
	}

	public int getX2() {
		return x2;
	}

	public void setX2(int x2) {
		this.x2 = x2;
	}

	public int getY2() {
		return y2;
	}

	public void setY2(int y2) {
		this.y2 = y2;
	}

	public Color getLine_Color() {
		return line_Color;
	}

	public void setLine_Color(Color line_Color) {
		this.line_Color = line_Color;
	}
	
	public void draw(Graphics g) {
		g.setColor(line_Color);
		g.drawLine(x1, y1, x2, y2);
	}

}
