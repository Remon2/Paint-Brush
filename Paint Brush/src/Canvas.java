import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Stack;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.awt.BasicStroke;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Canvas extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static ArrayList<Shape> shapes;
	private static Stack<Shape> stack;
	private Shape tmp;
	private Line line;
	private boolean cleared;
	private static boolean selected, shaded, moved, resized, deleted;
	private static int mouseX;
	private static int mouseY;
	private static int widthToMove, widthTri;
	private static int heightToMove, heightTri;
	private boolean flag_Undo;
	private boolean new_Paint;
	private JFileChooser fileDialog;
	private File editFile;
	private static int itemSelected = -1;
	private static Graphics2D g;
	private Rectangle2D rect, rect1, rect2, rect3, rect4;
	private int x1Orig, y1Orig, x2Orig, y2Orig, px1, py1, px2, py2, px3, py3;
	private boolean rect1Chosed, rect2Chosed, rect3Chosed, rect4Chosed;

	public Canvas() {
		shapes = new ArrayList<Shape>();
		stack = new Stack<>();
		tmp = new Rect(0, 0, 0, 0, ControlPanel.getColor(), false);
		cleared = false;
		selected = false;
		moved = false;
		deleted = false;
		// for undo and redo Buttons
		Event e = new Event();
		ControlPanel.getUndoButton().addActionListener(e);
		ControlPanel.getRedoButton().addActionListener(e);
		ControlPanel.getSelectButton().addActionListener(e);
		ControlPanel.getMoveButton().addActionListener(e);
		ControlPanel.getResizeButton().addActionListener(e);
		ControlPanel.getDeleteButton().addActionListener(e);
		flag_Undo = false;
		new_Paint = false;
	}

	@Override
	public void paintComponent(Graphics graphics) {
		g = (Graphics2D) graphics;
		super.paintComponent(g);
		if (cleared) {
			cleared = false;
			return;
		}
		if (deleted == true) {
			tmp = new Rect(0, 0, 0, 0, ControlPanel.getColor(), false);
		}
		Shape r = null;
		for (int i = 0; i < shapes.size(); i++) {
			r = shapes.get(i);
			g.setColor(r.getColor());
			if (r instanceof Ellipse) {
				((Ellipse) r).draw(g);
			} else if (r instanceof Rect) {
				((Rect) r).draw(g);
			} else if (r instanceof Square) {
				((Square) r).draw(g);
			} else if (r instanceof Triangle) {
				int x[] = new int[3];
				int y[] = new int[3];
				x[0] = r.getX();
				y[0] = r.getY();
				x[1] = r.getX() + r.getWidth();
				y[1] = r.getY() + r.getHeight();
				if (Frame.isTriangle() && i == shapes.size() - 1
						&& !Frame.isDrag()) {
					y[2] = mouseY;
					x[2] = mouseX;
				} else {
					y[2] = r.getY3();
					x[2] = r.getX3();
				}
				if (r.isFilled()) {
					g.fillPolygon(x, y, 3);
				} else {
					g.drawPolygon(x, y, 3);
				}
			} else if (r instanceof Circle) {
				((Circle) r).draw(g);
			} else if (r instanceof Line) {
				((Line) r).draw(g);
			}
		}

		g.setColor(Color.BLACK);
		if (isSelected() && shaded == true && itemSelected != -1) {
			rect = new Rectangle2D.Float(minX - 2, minY - 2, maxX - minX + 4,
					maxY - minY + 4);
			float[] dash = { 5F, 5F };
			Stroke dashedStroke = new BasicStroke(2F, BasicStroke.CAP_SQUARE,
					BasicStroke.JOIN_MITER, 3F, dash, 0F);
			g.fill(dashedStroke.createStrokedShape(rect));
		}

		if (isResized() && itemSelected != -1
				&& shapes.get(itemSelected) instanceof Triangle) {
			px1 = shapes.get(itemSelected).getX();
			py1 = shapes.get(itemSelected).getY();
			px2 = shapes.get(itemSelected).getX()
					+ shapes.get(itemSelected).getWidth();
			py2 = shapes.get(itemSelected).getY()
					+ shapes.get(itemSelected).getHeight();
			px3 = shapes.get(itemSelected).getX3();
			py3 = shapes.get(itemSelected).getY3();
			rect1 = new Rectangle2D.Float(px1 - 5, py1 - 5, 11, 11);
			float[] dash = { 3F, 3F };
			Stroke dashedStroke = new BasicStroke(1F, BasicStroke.CAP_SQUARE,
					BasicStroke.JOIN_MITER, 3F, dash, 0F);
			g.fill(dashedStroke.createStrokedShape(rect1));
			rect2 = new Rectangle2D.Float(px2 - 5, py2 - 5, 11, 11);
			g.fill(dashedStroke.createStrokedShape(rect2));
			rect3 = new Rectangle2D.Float(px3 - 5, py3 - 5, 11, 11);
			g.fill(dashedStroke.createStrokedShape(rect3));
		} else if (isResized() && itemSelected != -1
				&& shapes.get(itemSelected) instanceof Line) {
			px1 = ((Line) shapes.get(itemSelected)).getX1();
			py1 = ((Line) shapes.get(itemSelected)).getY1();
			px2 = ((Line) shapes.get(itemSelected)).getX2();
			py2 = ((Line) shapes.get(itemSelected)).getY2();
			rect1 = new Rectangle2D.Float(px1 - 5, py1 - 5, 11, 11);
			float[] dash = { 3F, 3F };
			Stroke dashedStroke = new BasicStroke(1F, BasicStroke.CAP_SQUARE,
					BasicStroke.JOIN_MITER, 3F, dash, 0F);
			g.fill(dashedStroke.createStrokedShape(rect1));
			rect2 = new Rectangle2D.Float(px2 - 5, py2 - 5, 11, 11);
			g.fill(dashedStroke.createStrokedShape(rect2));
		} else if (isResized() && itemSelected != -1) {
			rect1 = new Rectangle2D.Float(minX - 5, minY - 5, 11, 11);
			float[] dash = { 3F, 3F };
			Stroke dashedStroke = new BasicStroke(1F, BasicStroke.CAP_SQUARE,
					BasicStroke.JOIN_MITER, 3F, dash, 0F);
			g.fill(dashedStroke.createStrokedShape(rect1));
			rect2 = new Rectangle2D.Float(minX - 5, maxY - 5, 11, 11);
			g.fill(dashedStroke.createStrokedShape(rect2));
			rect3 = new Rectangle2D.Float(maxX - 5, minY - 5, 11, 11);
			g.fill(dashedStroke.createStrokedShape(rect3));
			rect4 = new Rectangle2D.Float(maxX - 5, maxY - 5, 11, 11);
			g.fill(dashedStroke.createStrokedShape(rect4));
		}
		g.drawString("(" + mouseX + "," + mouseY + ")", mouseX, mouseY);

		if (flag_Undo) {
			return;
		}
		if (new_Paint) {
			return;
		}

		// this part for Mouse Dragging
		if (!isSelected() && !isMoved() && !isResized() && !isDeleted()) {
			r = tmp;
			g.setColor(r.getColor());
			if (r instanceof Ellipse) {
				((Ellipse) r).draw(g);
			} else if (r instanceof Rect) {
				((Rect) r).draw(g);
			} else if (r instanceof Square) {
				((Square) r).draw(g);
			} else if (r instanceof Triangle && Frame.isDrag()) {
				int x[] = new int[3];
				int y[] = new int[3];
				x[0] = r.getX();
				x[1] = mouseX;
				y[0] = r.getY();
				y[1] = mouseY;
				y[2] = mouseY;
				x[2] = mouseX;
				if (r.isFilled()) {
					g.fillPolygon(x, y, 3);
				} else {
					g.drawPolygon(x, y, 3);
				}
			} else if (r instanceof Circle) {
				((Circle) r).draw(g);
			} else if (r instanceof Line) {
				((Line) r).draw(g);
			}
		}
	}

	class Event implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			moved = false;
			selected = false;
			resized = false;
			deleted = false;
			itemSelected = -1;
			if (e.getSource() == ControlPanel.getUndoButton()) {
				if (shapes.size() >= 1) {
					int lastShape = shapes.size() - 1;
					stack.push(shapes.get(lastShape));
					shapes.remove(lastShape);
					flag_Undo = true;
					shaded = false;
					repaint();
					if (lastShape == 0)
						ControlPanel.setUndoDisabled();
					ControlPanel.setRedoEnabled();
				}
			} else if (e.getSource() == ControlPanel.getRedoButton()) {
				if (!stack.isEmpty()) {
					shapes.add(stack.pop());
					if (stack.isEmpty())
						ControlPanel.setRedoDisabled();
					ControlPanel.setUndoEnabled();
					shaded = false;
					repaint();
				}
			} else if (e.getSource() == ControlPanel.getSelectButton()
					&& shapes.size() >= 1) {
				selected = true;
			} else if (e.getSource() == ControlPanel.getMoveButton()) {
				moved = true;
			} else if (e.getSource() == ControlPanel.getResizeButton()) {
				resized = true;
			} else if (e.getSource() == ControlPanel.getDeleteButton()) {
				deleted = true;
			}
		}
	}

	public void drawTmp(Shape t) {
		tmp = t;
		if (t instanceof Triangle && !Frame.isTriangle())
			shapes.get(shapes.size() - 1).setX3Y3(tmp.getX3(), tmp.getY3());
		tmp.setColor(ControlPanel.getColor());
		// System.out.println(tmp.getColor());
		flag_Undo = false;
		new_Paint = false;
		stack = new Stack<>();
		repaint();
	}

	public void drawLine(Point start, Point point, Color color) {
		line = new Line(start, point, color);
		tmp = line;
		flag_Undo = false;
		new_Paint = false;
		stack = new Stack<>();
		repaint();
	}

	public void addCircle() {
		shapes.add(new Circle(tmp));
		flag_Undo = false;
		new_Paint = false;
		stack = new Stack<>();
	}

	public void addRect() {
		shapes.add(new Rect(tmp));
		flag_Undo = false;
		new_Paint = false;
		stack = new Stack<>();
	}

	public void addSquare() {
		shapes.add(new Square(tmp));
		flag_Undo = false;
		new_Paint = false;
		stack = new Stack<>();
	}

	public void addTriangle() {
		shapes.add(new Triangle(tmp));
		flag_Undo = false;
		new_Paint = false;
		stack = new Stack<>();
	}

	public void addLine(Point start, Point end, Color color) {
		Line line = new Line(start, end, color);
		line.setColor(color);
		shapes.add(line);
		flag_Undo = false;
		new_Paint = false;
		stack = new Stack<>();
	}

	public void addEllipse() {
		shapes.add(new Ellipse(tmp));
		flag_Undo = false;
		new_Paint = false;
		stack = new Stack<>();
	}

	public void Clear() {
		shapes = new ArrayList<Shape>();
		tmp.setHeight(0);
		tmp.setWidth(0);
		cleared = true;
		new_Paint = true;
		ControlPanel.setUndoDisabled();
		ControlPanel.setRedoDisabled();
		ControlPanel.setSelectDisabled();
		repaint();
	}

	public static Stack<Shape> getStack() {
		return stack;
	}

	public static void setStack(Stack<Shape> stack) {
		Canvas.stack = stack;
	}

	public void newPaint() {
		shapes = new ArrayList<>();
		new_Paint = true;
		repaint();
	}

	private static int maxX, maxY, minX, minY;

	public static void selectShape(int mouse_X, int mouse_Y) {
		itemSelected = -1;
		for (int i = shapes.size() - 1; i >= 0; i--) {
			if (shapes.get(i) instanceof Triangle) {
				maxX = (int) Math.max(
						Math.max(shapes.get(i).getX3(), shapes.get(i).getX()),
						shapes.get(i).getX() + shapes.get(i).getWidth());
				minX = (int) Math.min(Math.min(shapes.get(i).getX3(), shapes
						.get(i).getX() + shapes.get(i).getWidth()),
						shapes.get(i).getX());
				maxY = (int) Math.max(
						Math.max(shapes.get(i).getY3(), shapes.get(i).getY()),
						shapes.get(i).getY() + shapes.get(i).getHeight());
				minY = (int) Math.min(Math.min(shapes.get(i).getY3(), shapes
						.get(i).getY() + shapes.get(i).getHeight()), shapes
						.get(i).getY());
			} else if (shapes.get(i) instanceof Line) {
				int x1 = ((Line) shapes.get(i)).getX1();
				int x2 = ((Line) shapes.get(i)).getX2();
				int y1 = ((Line) shapes.get(i)).getY1();
				int y2 = ((Line) shapes.get(i)).getY2();
				minX = Math.min(x1, x2);
				minY = Math.min(y1, y2);
				maxX = Math.max(x1, x2);
				maxY = Math.max(y1, y2);
			} else {

				maxX = shapes.get(i).getX() + shapes.get(i).getWidth();
				minX = shapes.get(i).getX();
				maxY = shapes.get(i).getY() + shapes.get(i).getHeight();
				minY = shapes.get(i).getY();
			}
			if (mouse_X >= minX && mouse_X <= maxX && mouse_Y <= maxY
					&& mouse_Y >= minY) {
				itemSelected = i;
				shaded = true;
				break;
			}
		}
		if (isMoved() && itemSelected != -1) {
			if (shapes.get(itemSelected) instanceof Triangle) {
				// if(shapes.get(itemSelected).getX3() < mouse_X)
				// widthTri = mouse_X - shapes.get(itemSelected).getX3();
				// else
				// widthTri = mouse_X + shapes.get(itemSelected).getX3();
				// if(shapes.get(itemSelected).getY3() < mouse_Y)
				// heightTri = mouse_Y - shapes.get(itemSelected).getY3();
				// else
				// heightTri = mouse_Y + shapes.get(itemSelected).getY3();
				widthTri = mouse_X - shapes.get(itemSelected).getX3();
				heightTri = mouse_Y - shapes.get(itemSelected).getY3();
				widthToMove = mouse_X - shapes.get(itemSelected).getX();
				heightToMove = mouse_Y - shapes.get(itemSelected).getY();
			} else if (shapes.get(itemSelected) instanceof Line) {
				widthToMove = mouse_X
						- ((Line) shapes.get(itemSelected)).getX1();
				heightToMove = mouse_Y
						- ((Line) shapes.get(itemSelected)).getY1();
				widthTri = ((Line) shapes.get(itemSelected)).getX2() - mouse_X;
				heightTri = ((Line) shapes.get(itemSelected)).getY2() - mouse_Y;
			} else {
				widthToMove = mouse_X - minX;
				heightToMove = mouse_Y - minY;
			}
		}
		if (itemSelected == -1 || !isSelected()) {
			shaded = false;
		}
		if (isDeleted() && itemSelected != -1) {
			shapes.remove(itemSelected);
			itemSelected = -1;
		}
	}

	public void drawMoved(int mouse_X, int mouse_Y) {
		tmp = new Rect(0, 0, 0, 0, ControlPanel.getColor(), false);
		if (shapes.get(itemSelected) instanceof Triangle) {
			shapes.get(itemSelected).setX3(mouse_X - widthTri);
			shapes.get(itemSelected).setY3(mouse_Y - heightTri);
			shapes.get(itemSelected).setX(mouse_X - widthToMove);
			shapes.get(itemSelected).setY(mouse_Y - heightToMove);
		} else if (shapes.get(itemSelected) instanceof Line) {
			((Line) shapes.get(itemSelected)).setX1(mouse_X - widthToMove);
			((Line) shapes.get(itemSelected)).setY1(mouse_Y - heightToMove);
			((Line) shapes.get(itemSelected)).setX2(mouse_X + widthTri);
			((Line) shapes.get(itemSelected)).setY2(mouse_Y + heightTri);
		} else {
			shapes.get(itemSelected).setX(mouse_X - widthToMove);
			shapes.get(itemSelected).setY(mouse_Y - heightToMove);
		}
		repaint();
	}

	public void drawResized(int mouse_X, int mouse_Y) {
		tmp = new Rect(0, 0, 0, 0, ControlPanel.getColor(), false);
		if (rect1Chosed == false && rect2Chosed == false
				&& rect3Chosed == false && rect4Chosed == false) {
			selectShape(mouse_X, mouse_Y);
			return;
		}
		if (itemSelected != -1 && shapes.get(itemSelected) instanceof Triangle) {
			if (rect3Chosed == true) {
				shapes.get(itemSelected).setX3(mouse_X);
				shapes.get(itemSelected).setY3(mouse_Y);
			} else if (rect1Chosed == true) {
				shapes.get(itemSelected).setX(x1Orig);
				shapes.get(itemSelected).setY(y1Orig);
				shapes.get(itemSelected).setWidth(px2 - x1Orig);
				shapes.get(itemSelected).setHeight(py2 - y1Orig);
				shapes.get(itemSelected).setX3(mouse_X);
				shapes.get(itemSelected).setY3(mouse_Y);
			} else if (rect2Chosed == true) {
				shapes.get(itemSelected).setWidth(x1Orig - px1);
				shapes.get(itemSelected).setHeight(y1Orig - py1);
				shapes.get(itemSelected).setX3(mouse_X);
				shapes.get(itemSelected).setY3(mouse_Y);
			}
		} else if (itemSelected != -1
				&& shapes.get(itemSelected) instanceof Line) {
			if (rect1Chosed == true) {
				((Line) shapes.get(itemSelected)).setX1(mouse_X);
				((Line) shapes.get(itemSelected)).setY1(mouse_Y);
			} else if (rect2Chosed == true) {
				((Line) shapes.get(itemSelected)).setX2(mouse_X);
				((Line) shapes.get(itemSelected)).setY2(mouse_Y);
			}
		} else if (itemSelected != -1) {
			int temp;
			if (rect1Chosed == true) {
				if (mouse_X >= x2Orig && mouse_Y >= y2Orig) {
					shapes.get(itemSelected).setX(x2Orig);
					shapes.get(itemSelected).setY(y2Orig);
					if (shapes.get(itemSelected) instanceof Square
							|| shapes.get(itemSelected) instanceof Circle) {
						temp = Math.min(mouse_X - x2Orig, mouse_Y - y2Orig);
						shapes.get(itemSelected).setWidth(temp);
						shapes.get(itemSelected).setHeight(temp);
					} else {
						shapes.get(itemSelected).setWidth(mouse_X - x2Orig);
						shapes.get(itemSelected).setHeight(mouse_Y - y2Orig);
					}
				} else if (mouse_X >= x2Orig) {
					shapes.get(itemSelected).setX(x2Orig);
					if (shapes.get(itemSelected) instanceof Square
							|| shapes.get(itemSelected) instanceof Circle) {
						temp = Math.min(mouse_X - x2Orig, y2Orig - mouse_Y);
						shapes.get(itemSelected).setY(y2Orig - temp);
						shapes.get(itemSelected).setWidth(temp);
						shapes.get(itemSelected).setHeight(temp);
					} else {
						shapes.get(itemSelected).setY(mouse_Y);
						shapes.get(itemSelected).setWidth(mouse_X - x2Orig);
						shapes.get(itemSelected).setHeight(y2Orig - mouse_Y);
					}
					// shapes.get(itemSelected).setWidth(mouse_X-);
				} else if (mouse_Y >= y2Orig) {
					shapes.get(itemSelected).setY(y2Orig);
					if (shapes.get(itemSelected) instanceof Square
							|| shapes.get(itemSelected) instanceof Circle) {
						temp = Math.min(x2Orig - mouse_X, mouse_Y - y2Orig);
						shapes.get(itemSelected).setX(x2Orig - temp);
						shapes.get(itemSelected).setWidth(temp);
						shapes.get(itemSelected).setHeight(temp);
					} else {
						shapes.get(itemSelected).setX(mouse_X);
						shapes.get(itemSelected).setWidth(x2Orig - mouse_X);
						shapes.get(itemSelected).setHeight(mouse_Y - y2Orig);
					}
				} else if (mouse_X < x2Orig && mouse_Y < y2Orig) {
					if (shapes.get(itemSelected) instanceof Square
							|| shapes.get(itemSelected) instanceof Circle) {
						temp = Math.min(x2Orig - mouse_X, y2Orig - mouse_Y);
						shapes.get(itemSelected).setXY(x2Orig - temp,
								y2Orig - temp);
						shapes.get(itemSelected).setWidth(temp);
						shapes.get(itemSelected).setHeight(temp);
					} else {
						shapes.get(itemSelected).setXY(mouse_X, mouse_Y);
						shapes.get(itemSelected).setWidth(x2Orig - mouse_X);
						shapes.get(itemSelected).setHeight(y2Orig - mouse_Y);
					}
				}

			} else if (rect2Chosed == true) {
				if (mouse_X >= x2Orig && mouse_Y <= y1Orig) {
					shapes.get(itemSelected).setX(x2Orig);
					if (shapes.get(itemSelected) instanceof Square
							|| shapes.get(itemSelected) instanceof Circle) {
						temp = Math.min(mouse_X - x2Orig, y1Orig - mouse_Y);
						shapes.get(itemSelected).setY(y1Orig - temp);
						shapes.get(itemSelected).setWidth(temp);
						shapes.get(itemSelected).setHeight(temp);
					} else {
						shapes.get(itemSelected).setY(mouse_Y);
						shapes.get(itemSelected).setWidth(mouse_X - x2Orig);
						shapes.get(itemSelected).setHeight(y1Orig - mouse_Y);
					}
				} else if (mouse_X >= x2Orig) {
					shapes.get(itemSelected).setX(x2Orig);
					shapes.get(itemSelected).setY(y1Orig);
					if (shapes.get(itemSelected) instanceof Square
							|| shapes.get(itemSelected) instanceof Circle) {
						temp = Math.min(mouse_X - x2Orig, mouse_Y - y1Orig);
						shapes.get(itemSelected).setWidth(temp);
						shapes.get(itemSelected).setHeight(temp);
					} else {
						shapes.get(itemSelected).setWidth(mouse_X - x2Orig);
						shapes.get(itemSelected).setHeight(mouse_Y - y1Orig);
					}
				} else if (mouse_Y <= y1Orig) {
					if (shapes.get(itemSelected) instanceof Square
							|| shapes.get(itemSelected) instanceof Circle) {
						temp = Math.min(x2Orig - mouse_X, y1Orig - mouse_Y);
						shapes.get(itemSelected).setX(x2Orig - temp);
						shapes.get(itemSelected).setY(y1Orig - temp);
						shapes.get(itemSelected).setWidth(temp);
						shapes.get(itemSelected).setHeight(temp);
					} else {
						shapes.get(itemSelected).setX(mouse_X);
						shapes.get(itemSelected).setY(mouse_Y);
						shapes.get(itemSelected).setWidth(x2Orig - mouse_X);
						shapes.get(itemSelected).setHeight(y1Orig - mouse_Y);
					}
				} else if (mouse_X < x2Orig && mouse_Y > y1Orig) {
					if (shapes.get(itemSelected) instanceof Square
							|| shapes.get(itemSelected) instanceof Circle) {
						temp = Math.min(x2Orig - mouse_X, mouse_Y - y1Orig);
						shapes.get(itemSelected).setXY(x2Orig - temp, y1Orig);
						shapes.get(itemSelected).setWidth(temp);
						shapes.get(itemSelected).setHeight(temp);
					} else {
						shapes.get(itemSelected).setXY(mouse_X, y1Orig);
						shapes.get(itemSelected).setWidth(x2Orig - mouse_X);
						shapes.get(itemSelected).setHeight(mouse_Y - y1Orig);
					}
				}
			} else if (rect3Chosed == true) {
				if (mouse_X <= x1Orig && mouse_Y >= y2Orig) {
					shapes.get(itemSelected).setY(y2Orig);
					if (shapes.get(itemSelected) instanceof Square
							|| shapes.get(itemSelected) instanceof Circle) {
						temp = Math.min(x1Orig - mouse_X, mouse_Y - y2Orig);
						shapes.get(itemSelected).setX(x1Orig - temp);
						shapes.get(itemSelected).setWidth(temp);
						shapes.get(itemSelected).setHeight(temp);
					} else {
						shapes.get(itemSelected).setX(mouse_X);
						shapes.get(itemSelected).setWidth(x1Orig - mouse_X);
						shapes.get(itemSelected).setHeight(mouse_Y - y2Orig);
					}
				} else if (mouse_X <= x1Orig) {
					// x2 = shapes.get(itemSelected).getX();
					if (shapes.get(itemSelected) instanceof Square
							|| shapes.get(itemSelected) instanceof Circle) {
						temp = Math.min(x1Orig - mouse_X, y2Orig - mouse_Y);
						shapes.get(itemSelected).setX(x1Orig - temp);
						shapes.get(itemSelected).setY(y2Orig - temp);
						shapes.get(itemSelected).setWidth(temp);
						shapes.get(itemSelected).setHeight(temp);
					} else {
						shapes.get(itemSelected).setX(mouse_X);
						shapes.get(itemSelected).setY(mouse_Y);
						shapes.get(itemSelected).setWidth(x1Orig - mouse_X);
						shapes.get(itemSelected).setHeight(y2Orig - mouse_Y);
					}
					// shapes.get(itemSelected).setWidth(mouse_X-);
				} else if (mouse_Y >= y2Orig) {
					shapes.get(itemSelected).setX(x1Orig);
					shapes.get(itemSelected).setY(y2Orig);
					if (shapes.get(itemSelected) instanceof Square
							|| shapes.get(itemSelected) instanceof Circle) {
						temp = Math.min(mouse_X - x1Orig, mouse_Y - y2Orig);
						shapes.get(itemSelected).setWidth(temp);
						shapes.get(itemSelected).setHeight(temp);
					} else {
						shapes.get(itemSelected).setWidth(mouse_X - x1Orig);
						shapes.get(itemSelected).setHeight(mouse_Y - y2Orig);
					}
				} else if (mouse_X > x1Orig && mouse_Y < y2Orig) {
					if (shapes.get(itemSelected) instanceof Square
							|| shapes.get(itemSelected) instanceof Circle) {
						temp = Math.min(mouse_X - x1Orig, y2Orig - mouse_Y);
						shapes.get(itemSelected).setXY(x1Orig, y2Orig - temp);
						shapes.get(itemSelected).setWidth(temp);
						shapes.get(itemSelected).setHeight(temp);
					} else {
						shapes.get(itemSelected).setXY(x1Orig, mouse_Y);
						shapes.get(itemSelected).setWidth(mouse_X - x1Orig);
						shapes.get(itemSelected).setHeight(y2Orig - mouse_Y);
					}
				}
			} else if (rect4Chosed == true) {
				if (mouse_X <= x1Orig && mouse_Y <= y1Orig) {
					if (shapes.get(itemSelected) instanceof Square
							|| shapes.get(itemSelected) instanceof Circle) {
						temp = Math.min(x1Orig - mouse_X, y1Orig - mouse_Y);
						shapes.get(itemSelected).setX(x1Orig - temp);
						shapes.get(itemSelected).setY(y1Orig - temp);
						shapes.get(itemSelected).setWidth(temp);
						shapes.get(itemSelected).setHeight(temp);
					} else {
						shapes.get(itemSelected).setX(mouse_X);
						shapes.get(itemSelected).setY(mouse_Y);
						shapes.get(itemSelected).setWidth(x1Orig - mouse_X);
						shapes.get(itemSelected).setHeight(y1Orig - mouse_Y);
					}
				} else if (mouse_X <= x1Orig) {
					shapes.get(itemSelected).setY(y1Orig);
					if (shapes.get(itemSelected) instanceof Square
							|| shapes.get(itemSelected) instanceof Circle) {
						temp = Math.min(x1Orig - mouse_X, mouse_Y - y1Orig);
						shapes.get(itemSelected).setX(x1Orig - temp);
						shapes.get(itemSelected).setWidth(temp);
						shapes.get(itemSelected).setHeight(temp);
					} else {
						shapes.get(itemSelected).setX(x1Orig - mouse_X);
						shapes.get(itemSelected).setWidth(x1Orig - mouse_X);
						shapes.get(itemSelected).setHeight(mouse_Y - y1Orig);
					}
				} else if (mouse_Y <= y1Orig) {
					shapes.get(itemSelected).setX(x1Orig);
					if (shapes.get(itemSelected) instanceof Square
							|| shapes.get(itemSelected) instanceof Circle) {
						temp = Math.min(mouse_X - x1Orig, y1Orig - mouse_Y);
						shapes.get(itemSelected).setY(y1Orig - temp);
						shapes.get(itemSelected).setWidth(temp);
						shapes.get(itemSelected).setHeight(temp);
					} else {
						shapes.get(itemSelected).setY(mouse_Y);
						shapes.get(itemSelected).setWidth(mouse_X - x1Orig);
						shapes.get(itemSelected).setHeight(y1Orig - mouse_Y);
					}
				} else if (mouse_X > x1Orig && mouse_Y > y1Orig) {
					shapes.get(itemSelected).setXY(x1Orig, y1Orig);
					if (shapes.get(itemSelected) instanceof Square
							|| shapes.get(itemSelected) instanceof Circle) {
						temp = Math.min(mouse_X - x1Orig, mouse_Y - y1Orig);
						shapes.get(itemSelected).setWidth(temp);
						shapes.get(itemSelected).setHeight(temp);
					} else {
						shapes.get(itemSelected).setWidth(mouse_X - x1Orig);
						shapes.get(itemSelected).setHeight(mouse_Y - y1Orig);
					}
				}
			}
			maxX = shapes.get(itemSelected).getX()
					+ shapes.get(itemSelected).getWidth();
			minX = shapes.get(itemSelected).getX();
			maxY = shapes.get(itemSelected).getY()
					+ shapes.get(itemSelected).getHeight();
			minY = shapes.get(itemSelected).getY();
		}
		repaint();
	}

	public void check(int mouse_X, int mouse_Y) {
		rect1Chosed = false;
		rect2Chosed = false;
		rect3Chosed = false;
		rect4Chosed = false;
		if (rect1.contains(mouse_X, mouse_Y))
			rect1Chosed = true;
		else if (rect2.contains(mouse_X, mouse_Y))
			rect2Chosed = true;
		else if (!(shapes.get(itemSelected) instanceof Line)
				&& rect3.contains(mouse_X, mouse_Y))
			rect3Chosed = true;
		else if (!(shapes.get(itemSelected) instanceof Triangle)
				&& rect4.contains(mouse_X, mouse_Y))
			rect4Chosed = true;
		if (shapes.get(itemSelected) instanceof Triangle) {
			x1Orig = shapes.get(itemSelected).getX3();
			y1Orig = shapes.get(itemSelected).getY3();
		} else {
			x1Orig = shapes.get(itemSelected).getX();
			y1Orig = shapes.get(itemSelected).getY();
			x2Orig = shapes.get(itemSelected).getX()
					+ shapes.get(itemSelected).getWidth();
			y2Orig = shapes.get(itemSelected).getY()
					+ shapes.get(itemSelected).getHeight();
		}
	}

	public void setShapeColor(Color c) {
		tmp = shapes.get(itemSelected);
		if (tmp instanceof Line)
			((Line) tmp).setLine_Color(c);
		else
			tmp.setColor(c);
		shapes.get(itemSelected).setColor(c);
		repaint();
	}

	public static int getMouseX() {
		return mouseX;
	}

	public static void setMouseX(int mouse_X) {
		mouseX = mouse_X;
	}

	public static int getMouseY() {
		return mouseY;
	}

	public static boolean isSelected() {
		return selected;
	}

	public void setSelected() {
		selected = false;
	}

	public static boolean isMoved() {
		return moved;
	}

	public void setMoved() {
		moved = false;
	}

	public static boolean isResized() {
		return resized;
	}

	public void setResized() {
		resized = false;
	}

	public static boolean isDeleted() {
		return deleted;
	}

	public void setDeleted() {
		deleted = false;
	}

	public static int getItemSelected() {
		return itemSelected;
	}

	public void setItemSelected() {
		itemSelected = -1;
	}

	public static void setMouseY(int mouse_Y) {
		mouseY = mouse_Y;
	}

	public void doSaveAsJson() {
		if (fileDialog == null)
			fileDialog = new JFileChooser();
		File selectedFile; // Initially selected file name in the dialog.

		selectedFile = new File("shapes.json");
		
		fileDialog.setSelectedFile(selectedFile);
		fileDialog.setDialogTitle("Select File to be Saved");
		int option = fileDialog.showSaveDialog(this);
		if (option != JFileChooser.APPROVE_OPTION)
			return; // User canceled or clicked the dialog's close box.
		selectedFile = fileDialog.getSelectedFile();
		if (selectedFile.exists()) { // Ask the user whether to replace the
										// file.
			int response = JOptionPane.showConfirmDialog(this, "The file \""
					+ selectedFile.getName()
					+ "\" already exists.\nDo you want to replace it?",
					"Confirm Save", JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE);
			if (response != JOptionPane.YES_OPTION)
				return; // User does not want to replace the file.
		}
		
		System.out.println(selectedFile.getName());
		String name = selectedFile.getName();
		int length = name.length();
		if (name.charAt(length - 1) != 'n' || name.charAt(length - 2) != 'o'
				|| name.charAt(length - 3) != 's'
				|| name.charAt(length - 4) != 'j'
				|| name.charAt(length - 5) != '.') {
			JOptionPane.showMessageDialog(null, "Sorry\nCan't save in this file");
			return;
		}
		
		
		
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter(selectedFile);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"Sorry, but an error occurred while trying to open the file:\n"
							+ e);
			System.out.println(e);
			System.out.println("4");
			e.printStackTrace();
			return;
		}
		try {
			Shape r;
			for (int i = 0; i < shapes.size(); i++) {
				r = shapes.get(i);
				if (r instanceof Line) {
					jsonObject.put("Type", "Line");
					jsonObject.put("x1", ((Line) r).getX1());
					jsonObject.put("y1", ((Line) r).getY1());
					jsonObject.put("x2", ((Line) r).getX2());
					jsonObject.put("y2", ((Line) r).getY2());
					jsonObject
							.put("Color", ((Line) r).getLine_Color().getRGB());
				} else {
					if (r instanceof Rect) {
						jsonObject.put("Type", "Rect");
					} else if (r instanceof Square) {
						jsonObject.put("Type", "Square");
					} else if (r instanceof Ellipse) {
						jsonObject.put("Type", "Ellipse");
					} else if (r instanceof Circle) {
						jsonObject.put("Type", "Circle");
					} else if (r instanceof Triangle) {
						jsonObject.put("Type", "Triangle");
						jsonObject.put("x3", r.getX3());
						jsonObject.put("y3", r.getY3());
					}

					jsonObject.put("x", r.getX());
					jsonObject.put("y", r.getY());
					jsonObject.put("width", r.getWidth());
					jsonObject.put("height", r.getHeight());
					if (r.isFilled()) {
						jsonObject.put("fill", true);
					} else {
						jsonObject.put("fill", false);
					}

					jsonObject.put("Color", r.getColor().getRGB());
				}
				jsonArray.add(jsonObject);
				jsonObject = new JSONObject();
			}
			jsonObject.put("Type", "BackGround");
			jsonObject.put("BackGround", getBackground().getRGB());
			jsonArray.add(jsonObject);

			fileWriter.write(JSONArray.toJSONString(jsonArray));
			fileWriter.flush();
			fileWriter.close();
			editFile = selectedFile;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"Sorry, but an error occurred while trying to write the text:\n"
							+ e);
			System.out.println(e);
			System.out.println("3");
			e.printStackTrace();
		}
	}

	public void doOpenAsJSON() {
		if (fileDialog == null)
			fileDialog = new JFileChooser();
		fileDialog.setDialogTitle("Select File to be Opened");
		fileDialog.setSelectedFile(null); // No file is initially selected.
		int option = fileDialog.showOpenDialog(this);
		if (option != JFileChooser.APPROVE_OPTION)
			return; // User canceled or clicked the dialog's close box.
		File selectedFile = fileDialog.getSelectedFile();
		FileReader fileReader;
		try {
			fileReader = new FileReader(selectedFile);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"Sorry, but an error occurred while trying to open the file:\n"
							+ e);
			System.out.println(e);
			System.out.println("1");
			e.printStackTrace();
			return;
		}
		System.out.println(selectedFile.getName());
		String name = selectedFile.getName();
		int length = name.length();
		if (name.charAt(length - 1) != 'n' || name.charAt(length - 2) != 'o'
				|| name.charAt(length - 3) != 's'
				|| name.charAt(length - 4) != 'j'
				|| name.charAt(length - 5) != '.') {
			JOptionPane.showMessageDialog(null, "Sorry\nCan't open this file");
			return;
		}

		JSONParser parser = new JSONParser();
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		Object object = new Object();
		shapes = new ArrayList<>();
		String type = "";
		try {

			object = parser.parse(fileReader);
			System.out.println(object.toString());
			System.out.println("parse tmam");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"Sorry, but an error occurred while trying to read the data:\n"
							+ e);
			System.out.println(e);
			System.out.println("2");
			e.printStackTrace();
		}
		Clear();
		jsonArray = (JSONArray) object;
		for (int i = 0; i < jsonArray.size(); i++) {
			jsonObject = (JSONObject) jsonArray.get(i);
			type = (String) ((JSONObject) jsonArray.get(i)).get("Type");
			if (type == null) {
				continue;
			}
			if (type.equals("Line")) {
				int x1 = Integer.parseInt(((JSONObject) jsonArray.get(i)).get(
						"x1").toString());
				int y1 = Integer.parseInt(((JSONObject) jsonArray.get(i)).get(
						"y1").toString());
				int x2 = Integer.parseInt(((JSONObject) jsonArray.get(i)).get(
						"x2").toString());
				int y2 = Integer.parseInt(((JSONObject) jsonArray.get(i)).get(
						"y2").toString());
				Color color = new Color(
						Integer.parseInt(((JSONObject) jsonArray.get(i)).get(
								"Color").toString()));
				shapes.add(new Line(new Point(x1, y1), new Point(x2, y2), color));
			} else if (type.equals("Rect") || type.equals("Square")
					|| type.equals("Ellipse") || type.equals("Circle")) {
				int x = Integer.parseInt(((JSONObject) jsonArray.get(i)).get(
						"x").toString());
				int y = Integer.parseInt(((JSONObject) jsonArray.get(i)).get(
						"y").toString());
				int width = Integer.parseInt(((JSONObject) jsonArray.get(i))
						.get("width").toString());
				int height = Integer.parseInt(((JSONObject) jsonArray.get(i))
						.get("height").toString());
				boolean isFill = (boolean) jsonObject.get("fill");
				Color color = new Color(
						Integer.parseInt(((JSONObject) jsonArray.get(i)).get(
								"Color").toString()));
				if (type.equals("Rect")) {
					shapes.add(new Rect(x, y, width, height, color, isFill));
				} else if (type.equals("Square")) {
					shapes.add(new Square(x, y, width, height, color, isFill));
				} else if (type.equals("Ellipse")) {
					shapes.add(new Ellipse(x, y, width, height, color, isFill));
				} else if (type.equals("Circle")) {
					shapes.add(new Circle(x, y, width, height, color, isFill));
				}
			} else if (type.equals("Triangle")) {
				int x = Integer.parseInt(((JSONObject) jsonArray.get(i)).get(
						"x").toString());
				int y = Integer.parseInt(((JSONObject) jsonArray.get(i)).get(
						"y").toString());
				int width = Integer.parseInt(((JSONObject) jsonArray.get(i))
						.get("width").toString());
				int height = Integer.parseInt(((JSONObject) jsonArray.get(i))
						.get("height").toString());
				boolean isFill = (boolean) jsonObject.get("fill");
				Color color = new Color(
						Integer.parseInt(((JSONObject) jsonArray.get(i)).get(
								"Color").toString()));
				Triangle t = new Triangle(x, y, width, height, color, isFill);
				t.setX3(Integer.parseInt(((JSONObject) jsonArray.get(i)).get(
						"x3").toString()));
				t.setY3(Integer.parseInt(((JSONObject) jsonArray.get(i)).get(
						"y3").toString()));
				shapes.add(t);
			}

			else {
				setBackground(new Color(
						Integer.parseInt(((JSONObject) jsonArray.get(i)).get(
								"BackGround").toString())));
			}
		}
		repaint();
		editFile = selectedFile;
	}

	public void doSaveAsXML() {
		if (fileDialog == null)
			fileDialog = new JFileChooser();
		File selectedFile; // Initially selected file name in the dialog.

		selectedFile = new File("Allshapes.xml");
		fileDialog.setSelectedFile(selectedFile);

		fileDialog.setDialogTitle("Select File to be Saved");
		int option = fileDialog.showSaveDialog(this);


		if (option != JFileChooser.APPROVE_OPTION)
			return; // User canceled or clicked the dialog's close box.
		selectedFile = fileDialog.getSelectedFile();

		System.out.println(selectedFile.getName());
		String name = selectedFile.getName();
		int length = name.length();
		if (name.charAt(length - 1) != 'l' || name.charAt(length - 2) != 'm'
				|| name.charAt(length - 3) != 'x'
				|| name.charAt(length - 4) != '.') {
			JOptionPane.showMessageDialog(null, "Sorry\nCan't save in this file");
			return;
		}

		if (selectedFile.exists()) { // Ask the user whether to replace the
										// file.
			int response = JOptionPane.showConfirmDialog(this, "The file \""
					+ selectedFile.getName()
					+ "\" already exists.\nDo you want to replace it?",
					"Confirm Save", JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE);
			if (response != JOptionPane.YES_OPTION)
				return; // User does not want to replace the file.
		}
		XMLEncoder encoder;
		try {
			FileOutputStream stream = new FileOutputStream(selectedFile);
			encoder = new XMLEncoder(stream);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"Sorry, but an error occurred while trying to open the file:\n"
							+ e);
			return;
		}
		try {
			encoder.writeObject(getBackground());
			encoder.writeObject(new Integer(shapes.size()));
			for (Shape c : shapes)
				encoder.writeObject(c);
			encoder.close();
			editFile = selectedFile;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"Sorry, but an error occurred while trying to write the text:\n"
							+ e);
		}
	}

	public void doOpenAsXML() {
		if (fileDialog == null)
			fileDialog = new JFileChooser();
		fileDialog.setDialogTitle("Select File to be Opened");
		fileDialog.setSelectedFile(null); // No file is initially selected.
		int option = fileDialog.showOpenDialog(this);
		if (option != JFileChooser.APPROVE_OPTION)
			return; // User canceled or clicked the dialog's close box.
		File selectedFile = fileDialog.getSelectedFile();
		
		
		
		System.out.println(selectedFile.getName());
		String name = selectedFile.getName();
		int length = name.length();
		if (name.charAt(length - 1) != 'l' || name.charAt(length - 2) != 'm'
				|| name.charAt(length - 3) != 'x'
				|| name.charAt(length - 4) != '.') {
			JOptionPane.showMessageDialog(null, "Sorry\nCan't open this file");
			return;
		}
		
		XMLDecoder decoder;
		try {
			FileInputStream stream = new FileInputStream(selectedFile);
			decoder = new XMLDecoder(stream);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"Sorry, but an error occurred while trying to open the file:\n"
							+ e);
			return;
		}
		Clear();
		try {
			Color bgColor = (Color) decoder.readObject();
			Integer curveCt = (Integer) decoder.readObject();

			for (int i = 0; i < curveCt; i++) {
				Shape c = (Shape) decoder.readObject();
				shapes.add(c);
			}
			decoder.close();
			setBackground(bgColor);
			repaint();
			editFile = selectedFile;
			// setTitle("SimplePaint: " + editFile.getName());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"Sorry, but an error occurred while trying to read the data:\n"
							+ e);
		}
	}
}
