import java.awt.Color;
import java.awt.Point;

public abstract class Shape {
	private int x,x3;
	private int y,y3;
	private int width;
	private int height;
	private Color color;
	private boolean isFilled;

	public Shape() {
	}

	public Shape(Shape s) {
		x = s.x;
		y = s.y;
		width = s.width;
		height = s.height;
		color = s.color;
		isFilled = s.isFilled;
	}

	public Shape(int x1, int y1, int x2, int y2, Color c) {
		x = x1;
		y = y1;
		width = x2 - x1;
		height = y2 - y1;
		color = c;
	}

	public Shape(int x, int y, int width, int height, Color c, boolean b) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		color = c;
		isFilled = b;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isFilled() {
		return isFilled;
	}

	public void setFilled(boolean isFilled) {
		this.isFilled = isFilled;
	}

	public Color getColor() {
		return color;
	}

	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setXY(Point p) {
		this.x = p.x;
		this.y = p.y;
	}

	public void setWH(int w, int h) {
		width = w;
		height = h;
	}

	public void setColor(Color c) {
		color = c;
	}

	public void setFill(boolean b) {
		isFilled = b;
	}
	
	public void setX3Y3(int x,int y){
		x3 = x ; 
		y3 = y ;
	}
	
	public void setX3(int x){
		x3 = x;
	}
	
	public void setY3(int y){
		y3 = y;
	}
	
	public int getX3(){
		return x3;
	}
	
	public int getY3(){
		return y3;
	}
	
}
