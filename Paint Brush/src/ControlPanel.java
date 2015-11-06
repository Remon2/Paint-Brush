import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class ControlPanel extends JPanel implements ActionListener,
		ItemListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int Shape_Number;
	private static Color color;

	private boolean isFilled;

//	private JLabel draw;
	private JRadioButton yBut, nBut;
	private ButtonGroup fillGroup;

//	private JComboBox combo;
//	private Color[] colors = { color.black, color.blue, color.green,
//			color.yellow, color.cyan, color.DARK_GRAY, color.gray,
//			color.LIGHT_GRAY, color.magenta, color.orange, color.pink,
	//		color.red };

	private JLabel fill;
	private JButton rectButton;
	private JButton ellipseButton;
	private JButton circleButton;
	private JButton lineButton;
	private JButton squareButton;
	private JButton triangleButton;
	private JButton chooseColor;
	private JButton backGround_Color;
	private static JButton undoButton;
	private static JButton redoButton;
	private static JButton selectButton;
	private static JButton moveButton;
	private static JButton resizeButton;
	private static JButton deleteButton;
	
	public ControlPanel() {
		fill = new JLabel("Fill :");
		yBut = new JRadioButton("Yes");
		nBut = new JRadioButton("No", true);
		yBut.addItemListener(this);
		nBut.addItemListener(this);
		fillGroup = new ButtonGroup();
		fillGroup.add(yBut);
		fillGroup.add(nBut);
		add(fill);
		add(yBut);
		add(nBut);
		
		selectButton = new JButton("Select");
		selectButton.setEnabled(false);
		add(selectButton);
		
		moveButton = new JButton("Move");
	//	selectButton.setEnabled(false);
		add(moveButton);
		
		resizeButton = new JButton("Resize");
		//	selectButton.setEnabled(false);
		add(resizeButton);
		
		deleteButton = new JButton("Delete");
		//	selectButton.setEnabled(false);
		add(deleteButton);
	//	String s[] = { "Black", "Blue", "Green", "Yellow", "Cyan", "DARK_GRAY",
	//			"Gray", "LIGHT_GRAY", "Magenta", "Orange", "Pink", "Red" };

	//	ComboColor com = new ComboColor();
	//	combo = new JComboBox<>(s);
	//	combo.addActionListener(com);
	//	add(combo);

		undoButton = new JButton("Undo");
		undoButton.setEnabled(false);
		add(undoButton);

		redoButton = new JButton("Redo");
		redoButton.setEnabled(false);
		add(redoButton);

		chooseColor = new JButton("Shape Color");
		chooseColor.addActionListener(this);
		add(chooseColor);

		backGround_Color = new JButton("BackGround Color");
		backGround_Color.addActionListener(this);
		add(backGround_Color);

		lineButton = new JButton("Line");
		lineButton.addActionListener(this);
		add(lineButton);

		rectButton = new JButton("Rectangle");
		rectButton.addActionListener(this);
		add(rectButton);

		squareButton = new JButton("Square");
		squareButton.addActionListener(this);
		add(squareButton);

		ellipseButton = new JButton("Ellipse");
		ellipseButton.addActionListener(this);
		add(ellipseButton);

		circleButton = new JButton("Circle");
		circleButton.addActionListener(this);
		add(circleButton);

		triangleButton = new JButton("Triangle");
		triangleButton.addActionListener(this);
		add(triangleButton);
		
		color = Color.BLACK;
		Shape_Number = 3;
	}
	
	public static JButton getSelectButton(){
		return selectButton;
	}
	
	public static JButton getMoveButton(){
		return moveButton;
	}
	
	public static JButton getResizeButton(){
		return resizeButton;
	}
	
	public static JButton getDeleteButton(){
		return deleteButton;
	}
	
	public static JButton getUndoButton() {
		return undoButton;
	}

	public static void setUndoButton(JButton undo_Button) {
		undoButton = undo_Button;
	}

	public static JButton getRedoButton() {
		return redoButton;
	}

	public static void setRedoButton(JButton redo_Button) {
		redoButton = redo_Button;
	}
	
	public static void setUndoDisabled(){
		undoButton.setEnabled(false);
	}
	
	public static void setUndoEnabled(){
		undoButton.setEnabled(true);
	}
	
	public static void setRedoDisabled(){
		redoButton.setEnabled(false);
	}
	
	public static void setRedoEnabled(){
		redoButton.setEnabled(true);
	}
	
	public static void setSelectEnabled(){
		selectButton.setEnabled(true);
	}
	
	public static void setSelectDisabled(){
		selectButton.setEnabled(false);
	}
	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == yBut) {
			setFilled(true);
		}
		if (e.getSource() == nBut) {
			setFilled(false);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		boolean b=true;
		Frame.getCanvas().setMoved();
		Frame.getCanvas().setResized();
		Frame.getCanvas().setDeleted();
		if (e.getSource() == rectButton) {
			Shape_Number = 0;
		} else if (e.getSource() == ellipseButton) {
			Shape_Number = 1;
		} else if (e.getSource() == circleButton) {
			Shape_Number = 2;
		} else if (e.getSource() == lineButton) {
			Shape_Number = 3;
		} else if (e.getSource() == squareButton) {
			Shape_Number = 4;
		} else if (e.getSource() == triangleButton) {
			Shape_Number = 5;
		} else if (e.getSource() == chooseColor && Canvas.isSelected() && Canvas.getItemSelected()!=-1) {
			Color color1 = JColorChooser.showDialog(null, "Choose a color",
					Color.BLACK);
			Frame.getCanvas().setShapeColor(color1);
			b = false;
		} else if (e.getSource() == chooseColor) {
			color = JColorChooser.showDialog(null, "Choose a color",
					Color.BLACK);
		//	Frame.getCanvas().setShapeColor(color);
		} else if (e.getSource() == backGround_Color) {
			Frame.getCanvas().setBackground(
					JColorChooser.showDialog(null, "Choose a color",
							Color.white));
		}
		if(b==true && Frame.getCanvas().isSelected()){
			Frame.getCanvas().setSelected();
			Frame.getCanvas().setItemSelected();
			color = Color.BLACK;
		}
		//	else if (e.getSource() == selectButton){
	//		selected = true;
	//	}
	}

	public int getShape_Number() {
		return Shape_Number;
	}

	public boolean isFilled() {
		return isFilled;
	}

	public void setFilled(boolean isFilled) {
		this.isFilled = isFilled;
	}

	public static  Color getColor() {
		return color;
	}

	public void setColor(Color c) {
		color = c;
	}

	class ComboColor implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
	//		color = colors[combo.getSelectedIndex()];
		}

	}

}
