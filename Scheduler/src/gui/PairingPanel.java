package gui;

import middleend.*;
import backbone.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This is the panel that displays a pairing attributes and allows
 * the user to edit them.
 */
public class PairingPanel extends JPanel implements GUIConstants {

	private MiddleEnd _middleEnd;
	private Round _round;
	private Pairing _pairing;
	
	/**
	 * Constructor.
	 * @param me
	 * @param r
	 * @param p
	 */
	public PairingPanel(MiddleEnd me, Round r, Pairing p) {
		_middleEnd = me;
		_round = r;
		_pairing = p;
		resetPanel();
	}
	
	public Color conflictColor(float conflictScore){
		float red = 1;
		float green = 1;
		if(conflictScore < 0) green = (1 + conflictScore) * green;
		else if(conflictScore > 0) red = (1 - conflictScore) * red;
		return new Color(red, green, 0);
		
	}

	/**
	 * Resets this panel.
	 */
	public void resetPanel() {
		this.removeAll();
		if (COLORSON) {
			this.setBackground(BACKGROUND_COLOR);
			this.setForeground(FOREGROUND_COLOR);
		}
		float conflictFloat = (float)_pairing.getConflictScore();
		Color conflictColor = conflictColor(conflictFloat);
		this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
		this.setToolTipText(_pairing.getConflictMessage());
		JPanel almostthis = new JPanel();
		almostthis.setBorder(BorderFactory.createMatteBorder(6, 6, 4, 4, conflictColor));
		JPanel reallythis = new JPanel();
		reallythis.setLayout(new BoxLayout(reallythis, BoxLayout.X_AXIS));
		reallythis.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
//		this.add(Box.createHorizontalGlue());
		JPanel deletepanel = new JPanel();
		if (COLORSON) {
			deletepanel.setBackground(BACKGROUND_COLOR);
			deletepanel.setForeground(FOREGROUND_COLOR);
		}
		deletepanel.setLayout(new BoxLayout(deletepanel, BoxLayout.Y_AXIS));
		// This button allows you to delete pairings.
		final JButton deletebutton = new JButton("Delete this pairing");
		if (IMAGESON)
			deletebutton.setIcon(DELETEBUTTONIMAGE);
		if (COLORSON) {
			deletebutton.setBackground(BACKGROUND_COLOR);
			deletebutton.setForeground(FOREGROUND_COLOR);
		}
		// The button needs to be clicked twice to actually delete,
		// helps prevent accidental deletions
		deletebutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (deletebutton.getText().equals("Delete this pairing"))
					deletebutton.setText("Are you sure?");
				else {
					_round.removePairing(_pairing);
					_middleEnd.repaintAll();
				}
					
			}
		});
		deletepanel.add(deletebutton);
		reallythis.add(deletepanel);
		reallythis.add(Box.createRigidArea(SMALLSPACING_SIZE));
		int attNum = 0;
		JPanel bigWrapper = new JPanel();
		if (COLORSON) {
			bigWrapper.setBackground(BACKGROUND_COLOR);
			bigWrapper.setForeground(FOREGROUND_COLOR);
		}
		bigWrapper.setLayout(new BoxLayout(bigWrapper, BoxLayout.Y_AXIS));
		JPanel toAddTo = new JPanel();
		for (Attribute attribute : _pairing.getAttributes()) {
			// Wrap attributes so that only 4 are in a row
			if(attNum % 4 == 0){
				if(attNum != 0){
					bigWrapper.add(Utility.wrapLeft(toAddTo));
					bigWrapper.add(Box.createRigidArea(SMALLSPACING_SIZE));
				}
				toAddTo = new JPanel();
				if (COLORSON) {
					toAddTo.setBackground(BACKGROUND_COLOR);
					toAddTo.setForeground(FOREGROUND_COLOR);
				}
				toAddTo.setLayout(new BoxLayout(toAddTo, BoxLayout.X_AXIS));
			}
			attNum++;
			// Display all unit attributes and their info
			if (attribute.getType() == Attribute.Type.UNIT) {
				JPanel attrpanel = new JPanel();
				if (COLORSON) {
					attrpanel.setBackground(BACKGROUND_COLOR);
					attrpanel.setForeground(FOREGROUND_COLOR);
				}
				attrpanel.setLayout(new BoxLayout(attrpanel, BoxLayout.Y_AXIS));
				attrpanel.add(Utility.wrapLeft(Utility.getTitleLabel(attribute)));
				attrpanel.add(Utility.wrapLeft(new UnitAttributeComboBox((UnitAttribute<?>) attribute, _pairing, this)));//Not header, needs to be editable
				if (((UnitAttribute<?>) attribute).getAttribute() != null) {
					// Add labels with all the values of the unit's attributes
					for (Attribute attr : ((UnitAttribute<?>) attribute).getAttribute().getAttributes()) {
						toAddTo.add(Box.createRigidArea(SMALLSPACING_SIZE));
						if (attr.getType() == Attribute.Type.GROUPING) {
							JLabel label = Utility.getTitleLabel(attr);							
							label.setToolTipText("Go to the input panel to edit/view this attribute of this unit.");
							attrpanel.add(Utility.wrapLeft(label));
						}
						else {
							JLabel title = Utility.getTitleLabel(attr);
							JLabel value = Utility.getValueLabel(attr);
							JLabel label = new JLabel(title.getText() + ": " + value.getText());
							label.setToolTipText("Go to the input panel to edit/view this attribute of this unit.");
							attrpanel.add(Utility.wrapLeft(label));
						}
					}
				}
				else {
					toAddTo.add(Box.createRigidArea(SMALLSPACING_SIZE));
					JLabel title = Utility.getTitleLabel(attribute);
					title.setText(title.getText() + ": N/A");
					attrpanel.add(Utility.wrapLeft(title));
				}
				toAddTo.add(Utility.wrapUp(attrpanel));
			}
//			else if (attribute.getType() == Attribute.Type.GROUPING) {
//				JLabel label = Utility.getTitleLabel(attribute);
//				label.setToolTipText("Go to the input panel to edit/view this attribute of this unit.");
//				toAddTo.add(label);
//			}
			else if(attribute.getType() == Attribute.Type.DOUBLE){
				JPanel attrpanel = new DoubleAttributePanel((DoubleAttribute)attribute, _pairing, this);
				toAddTo.add(attrpanel);
			}
			else {
				JPanel attrpanel = new JPanel();
				if (COLORSON) {
					attrpanel.setBackground(BACKGROUND_COLOR);
					attrpanel.setForeground(FOREGROUND_COLOR);
				}
				attrpanel.setLayout(new BoxLayout(attrpanel, BoxLayout.Y_AXIS));
				attrpanel.add(Utility.wrapLeft(Utility.getTitleLabel(attribute)));
				JComponent wrap = Utility.getField(attribute);
				//wrap.setPreferredSize(new Dimension(TEXTFIELD_SIZE.width, TEXTFIELD_SIZE.height + 40));
				//wrap.setPreferredSize(new Dimension(TEXTFIELD_SIZE.width, TEXTFIELD_SIZE.height + 40));
				attrpanel.add(Utility.wrapLeft(wrap));
				//attrpanel.setPreferredSize(new Dimension(TEXTFIELD_SIZE.width, TEXTFIELD_SIZE.height*3));
				toAddTo.add(attrpanel);
			}
			toAddTo.add(Box.createRigidArea(SMALLSPACING_SIZE));
		}
		bigWrapper.add(Utility.wrapLeft(toAddTo));
//		bigWrapper.add(Box.createVerticalGlue());
		//bigWrapper.setPreferredSize(new Dimension(PAIRINGPANEL_SIZE.width, PAIRINGPANEL_SIZE.height * ((attNum / 4)+1) ));
		//this.setSize(new Dimension(PAIRINGPANEL_SIZE.width, PAIRINGPANEL_SIZE.height * ((attNum / 4)+1) ));
		reallythis.add(bigWrapper);
		reallythis.add(Box.createHorizontalGlue());
		almostthis.add(reallythis);
		this.add(almostthis);
	}
	
	/**
	 * Repaints this panel.
	 */
	public void repaintAll() {
		this.resetPanel();
		this.repaint();
	}
	
	private class DoubleAttributePanel extends JPanel {
		private DoubleAttribute _doubleattribute;
		private PairingPanel _pairingpanel;
		private Pairing _pairing;
		
		public DoubleAttributePanel(DoubleAttribute da, Pairing p, PairingPanel pp){
			super();
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			_doubleattribute = da;
			_pairingpanel = pp;
			_pairing = p;
			DecimalFormat df = new DecimalFormat();
			df.setGroupingUsed(false);
			JFormattedTextField tf = new JFormattedTextField(df);
			
			tf.setPreferredSize(TEXTFIELD_SIZE);
			tf.setMaximumSize(TEXTFIELD_SIZE);
			tf.setValue(da.getAttribute());
			
			tf.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JFormattedTextField db = (JFormattedTextField) e.getSource();
					double value = Double.parseDouble(db.getText());
					System.out.println(value);
					_pairing.setAttribute(new DoubleAttribute(_doubleattribute.getTitle(), value));
					_pairingpanel.repaintAll();
				}
			});
//			tf.addFocusListener(new FocusListener() {
//
//				@Override
//				public void focusGained(FocusEvent e) {
//				}
//				@Override
//				public void focusLost(FocusEvent e) {
//					JFormattedTextField db = (JFormattedTextField) e.getSource();
//					double value = Double.parseDouble(db.getText());
//					System.out.println(value);
//					_pairing.setAttribute(new DoubleAttribute(_doubleattribute.getTitle(), value));
//					_pairingpanel.repaintAll();
//				}
//				
//			});
			this.add(Utility.wrapLeft(Utility.getTitleLabel(_doubleattribute)));
			this.add(Utility.wrapLeft(tf));
		}
	}

	/**
	 * This class represents a UnitAttribute in a Pairing.
	 */
	private class UnitAttributeComboBox extends JComboBox {
		
		private UnitAttribute _unitattribute;
		private PairingPanel _pairingpanel;
		private Pairing _pairing;
		
		/**
		 * Constructor.
		 * @param ua
		 * @param p
		 * @param pp
		 */
		public UnitAttributeComboBox(UnitAttribute ua, Pairing p, PairingPanel pp) {
			_unitattribute = ua;
			_pairingpanel = pp;
			_pairing = p;
			
			this.setPreferredSize(JCOMBOBOX_SIZE);
			this.setMaximumSize(JCOMBOBOX_SIZE);
			
			// Ensure that the combo box displays the actual names of the units
			final ArrayList<Unit> units = new ArrayList<Unit>();
			units.add(null);
			units.addAll(_unitattribute.getListOfUnits());
			ArrayList<String> unitnames = new ArrayList<String>();
			unitnames.add("");
			int toSelect = 0;
			for (int i = 1; i < units.size(); i++) {
				unitnames.add(units.get(i).getName());
				if (units.get(i) == _unitattribute.getAttribute())
					toSelect = i;
			}
			this.setModel(new DefaultComboBoxModel(unitnames.toArray(new String[0])));
			this.setSelectedIndex(toSelect);
			// setAttribute() of the pairing when a selection is made
			this.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					UnitAttributeComboBox cb = (UnitAttributeComboBox) e.getSource();
					if (cb.getSelectedIndex() <= 0) {
						_pairing.setAttribute(new UnitAttribute(_unitattribute.getTitle(), _unitattribute.getMemberOf()));
						_pairingpanel.repaintAll();
					}
					else {
						_pairing.setAttribute(new UnitAttribute(_unitattribute.getTitle(), units.get(cb.getSelectedIndex()), _unitattribute.getMemberOf()));
						_pairingpanel.repaintAll();
					}
				}
			});
		}
	}
	
}
