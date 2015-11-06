import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Frame extends JFrame implements MouseMotionListener,
		MouseListener, ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static ControlPanel controlPanel;
	private Shape tmp;
	private static Canvas canvas;

	private Point start, end;
	private JButton clear;
	private static boolean isDrag = false;
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem newPaint;
	private JMenuItem openXml;
	private JMenuItem saveInXml;
	private JMenuItem openJson;
	private JMenuItem saveInJson;
	private JMenuItem exit;

	public Frame() {
		setVisible(true);
		setTitle("Paint Application : Developed By Remon Hanna and Abd El Rahman Mostafa.");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		menuBar = new JMenuBar();
		menu = new JMenu("File");
		menuBar.add(menu);
		newPaint = new JMenuItem("New");
		menu.add(newPaint);
		openXml = new JMenuItem("Open Xml");
		menu.add(openXml);
		saveInXml = new JMenuItem("Save in Xml");
		menu.add(saveInXml);
		openJson = new JMenuItem("Open Json");
		menu.add(openJson);
		saveInJson = new JMenuItem("Save in Json");
		menu.add(saveInJson);
		exit = new JMenuItem("Exit");
		menu.add(exit);

		add(menuBar, BorderLayout.NORTH);

		MenuAction menuAction = new MenuAction();
		newPaint.addActionListener(menuAction);
		openXml.addActionListener(menuAction);
		saveInXml.addActionListener(menuAction);
		openJson.addActionListener(menuAction);
		saveInJson.addActionListener(menuAction);
		exit.addActionListener(menuAction);

		controlPanel = new ControlPanel();
		add(controlPanel, BorderLayout.SOUTH);

		tmp = new Rect(0, 0, 0, 0, ControlPanel.getColor(), false);
		start = new Point();
		end = new Point();

		canvas = new Canvas();
		canvas.addMouseMotionListener(this);
		canvas.addMouseListener(this);
		canvas.setBackground(Color.white);
		add(canvas, BorderLayout.CENTER);

		clear = new JButton("Clear");
		controlPanel.add(clear);
		clear.addActionListener(this);
		setLocation(0, 0);
		setSize(1366, 730);
		setResizable(false);
	}

	public void mousePressed(MouseEvent e) {
		ControlPanel.setUndoEnabled();
		ControlPanel.setRedoDisabled();
		if (Canvas.isSelected()) {
			Canvas.selectShape(e.getX(), e.getY());
		} else if (Canvas.isMoved()) {
			Canvas.selectShape(e.getX(), e.getY());
		} else if (Canvas.isResized() && Canvas.getItemSelected() != -1) {
			canvas.check(e.getX(), e.getY());
			return;
		} else if (Canvas.isResized()) {
			Canvas.selectShape(e.getX(), e.getY());
			return;
		} else if (Canvas.isDeleted()) {
			Canvas.selectShape(e.getX(), e.getY());
			return;
		} else if (controlPanel.getShape_Number() == 0) {
			tmp = new Rect();
		} else if (controlPanel.getShape_Number() == 1) {
			tmp = new Ellipse();
		} else if (controlPanel.getShape_Number() == 2) {
			tmp = new Circle();
		} else if (controlPanel.getShape_Number() == 3) {
			tmp = new Line();
		} else if (controlPanel.getShape_Number() == 4) {
			tmp = new Square();
		} else if (controlPanel.getShape_Number() == 5 && isTriangle()) {
			tmp.setX3Y3(e.getX(), e.getY());
			setTriangleDisabled();
			canvas.drawTmp(tmp);
		} else if (controlPanel.getShape_Number() == 5) {

			tmp = new Triangle();
		}
		if (!Canvas.isSelected() && !Canvas.isMoved() && !Canvas.isResized()
				&& !Canvas.isDeleted()) {
			start.x = e.getX();
			start.y = e.getY();
			tmp.setXY(start);
			tmp.setColor(ControlPanel.getColor());
		}
		ControlPanel.setSelectEnabled();
	}

	public void mouseReleased(MouseEvent e) {

		if (controlPanel.getShape_Number() == 5 && !isTriangle()) {
			// setTriangleDisabled();
			isDrag = false;
			return;
		}
		if (isDrag && !Canvas.isSelected() && !Canvas.isMoved()
				&& !Canvas.isResized()) {
			if (tmp instanceof Ellipse)
				canvas.addEllipse();
			else if (tmp instanceof Rect)
				canvas.addRect();
			else if (tmp instanceof Circle)
				canvas.addCircle();
			else if (tmp instanceof Triangle)
				canvas.addTriangle();
			else if (tmp instanceof Square)
				canvas.addSquare();
			else if (tmp instanceof Line)
				canvas.addLine(start, e.getPoint(), ControlPanel.getColor());
		}
		isDrag = false;
	}

	static boolean b = false;

	public static void setTriangleEnabled() {
		b = true;
	}

	public static void setTriangleDisabled() {
		b = false;
	}

	public static boolean isTriangle() {
		return b;
	}

	public void mouseDragged(MouseEvent e) {
		if (!Canvas.isSelected() && !Canvas.isMoved() && !Canvas.isResized()
				&& !Canvas.isDeleted()) {
			isDrag = true;
			if (tmp instanceof Triangle)
				setTriangleEnabled();
			end.x = e.getX();
			end.y = e.getY();
			int x = start.x;
			int y = start.y;
			if (!(tmp instanceof Line)) {
				if (!(tmp instanceof Triangle) && !(tmp instanceof Square)
						&& !(tmp instanceof Circle)) {
					if (end.x - x < 0) {
						end.x = x;
						x = e.getX();
					}
					if (end.y - y < 0) {
						end.y = y;
						y = e.getY();
					}
				}
				if (tmp instanceof Square || tmp instanceof Circle) {
					int min = Math.min(Math.abs(end.x - start.x),
							Math.abs(end.y - start.y));
					if (e.getX() < start.x)
						x = start.x - min;
					if (e.getY() < start.y)
						y = start.y - min;
					tmp.setWH(min, min);
				} else
					tmp.setWH(end.x - x, end.y - y);
				tmp.setXY(x, y);
				tmp.setFill(controlPanel.isFilled());
				tmp.setColor(ControlPanel.getColor());
				// System.out.println(ControlPanel.getColor());
				canvas.drawTmp(tmp);
			} else {
				canvas.drawLine(start, e.getPoint(), ControlPanel.getColor());
			}
			tmp.setColor(ControlPanel.getColor());
			Canvas.setMouseX(e.getX());
			Canvas.setMouseY(e.getY());
		} else if (Canvas.isMoved() && Canvas.getItemSelected() != -1) {
			canvas.drawMoved(e.getX(), e.getY());
		} else if (Canvas.isResized() && Canvas.getItemSelected() != -1) {
			canvas.drawResized(e.getX(), e.getY());
		}
	}

	public static boolean isDrag() {
		return isDrag;
	}

	public void mouseMoved(MouseEvent e) {
		Canvas.setMouseX(e.getX());
		Canvas.setMouseY(e.getY());
		repaint();
	}

	public void actionPerformed(ActionEvent e) {
		canvas.Clear();
	}

	class MenuAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == newPaint) {
				canvas.newPaint();
			} else if (e.getSource() == openXml) {
				canvas.doOpenAsXML();
			} else if (e.getSource() == saveInXml) {
				canvas.doSaveAsXML();
			} else if (e.getSource() == openJson) {
				canvas.doOpenAsJSON();
			} else if (e.getSource() == saveInJson) {
				canvas.doSaveAsJson();
			} else if (e.getSource() == exit) {
				System.exit(0);
			}
		}
	}

	public static Canvas getCanvas() {
		return canvas;
	}

	public void setCanvas(Canvas can) {
		canvas = can;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	public static void main(String[] args) {
		new Frame();
	}
}
