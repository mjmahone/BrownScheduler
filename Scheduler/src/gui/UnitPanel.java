package gui;

import backbone.*;
import middleend.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class UnitPanel extends JPanel implements GUIConstants {

	private MiddleEnd _middleEnd;
	private Utility _util;
	private JPanel _mainPanel, _buttonPanel, _tablePanel;
	private Grouping _grouping;
	
	public UnitPanel(MiddleEnd m, Unit u) {
		_middleEnd = m;
		_util = new Utility();
		_mainPanel = new JPanel();
		_buttonPanel = new JPanel();
		_tablePanel = new JPanel();
		initialize(u, "Save Changes");
	}
	
	public UnitPanel(MiddleEnd m, Unit u, Grouping g) {
		_middleEnd = m;
		_grouping = g;
		_util = new Utility();
		_mainPanel = new JPanel();
		_buttonPanel = new JPanel();
		_tablePanel = new JPanel();
		initialize(u, "Save Changes to New Unit");
	}
	
	public void initialize(final Unit unit, String buttonstring) {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		_mainPanel.setLayout(new BoxLayout(_mainPanel, BoxLayout.X_AXIS));
		_tablePanel.setLayout(new BoxLayout(_tablePanel, BoxLayout.Y_AXIS));
//		_tablePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, INPUTTABLE_HEIGHT));
		final HashMap<Attribute, JComponent> components = new HashMap<Attribute, JComponent>();
		for (final Attribute attr : unit.getAttributes()) {
			JLabel titLabel = Utility.getTitleLabel(attr);
			if (attr instanceof GroupingAttribute) {
				GroupingAttribute<Unit> g = (GroupingAttribute<Unit>) attr;
				components.put(attr, new InputTablePane(_middleEnd, g.getBlankUnit().getAttributes(), g));
			}
			JComponent comp = _util.getField(attr);
			if (comp instanceof JButton) {
				((JButton) comp).addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						boolean hasPanel = false;
						for (int i = 0; i < _tablePanel.getComponentCount(); i++) {
							if (_tablePanel.getComponent(i) == components.get(attr))
								hasPanel = true;
						}
						if (_tablePanel.getComponentCount() == 0) {
							_tablePanel.removeAll();
							_tablePanel.add(((InputTablePane) components.get(attr)).getTable().getTableHeader(), BorderLayout.PAGE_START);
							_tablePanel.add(components.get(attr), BorderLayout.CENTER);
						}
						else if (hasPanel) {
							_tablePanel.removeAll();
						}
						else {
							_tablePanel.removeAll();
							_tablePanel.add(((InputTablePane) components.get(attr)).getTable().getTableHeader(), BorderLayout.PAGE_START);
							_tablePanel.add(components.get(attr), BorderLayout.CENTER);
						}
						_tablePanel.repaint();
					}
				});
			}
			if (!components.containsKey(attr))
				components.put(attr, comp);
			JPanel toAdd = new JPanel();
			toAdd.setLayout(new BoxLayout(toAdd, BoxLayout.Y_AXIS));
			toAdd.add(titLabel);
			toAdd.add(comp);
			_mainPanel.add(toAdd);
		}
		this.add(_mainPanel);
		this.add(Box.createRigidArea(new Dimension(10, 10)));
		JButton savebutton = new JButton(buttonstring);
		savebutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Collection<Attribute> attributes = components.keySet();
				for (Attribute attr : attributes) {
					if (attr.getType() == Attribute.Type.BOOLEAN) {
						boolean value = ((JCheckBox) components.get(attr)).isSelected();
						unit.setAttribute(new BooleanAttribute(attr.getTitle(), value));
					}
					else if (attr.getType() == Attribute.Type.DOUBLE) {
						double value = Double.parseDouble(((JTextField) components.get(attr)).getText());
						unit.setAttribute(new DoubleAttribute(attr.getTitle(), value));
					}
					else if (attr.getType() == Attribute.Type.GROUPING) {
						InputTablePane table = (InputTablePane) components.get(attr);
						GroupingAttribute groupattr = (GroupingAttribute) attr;
						for (int i = 0; i < table.getTable().getRowCount(); i++) {
							Unit rowunit;
							if (i < table.getUnitsInRowsList().size()) {
								rowunit = table.getUnitsInRowsList().get(i);
								groupattr.addMember(rowunit);
							}
							else {
								rowunit = groupattr.getBlankUnit();
							}
							int j = 0;
							for (Attribute rowattr : rowunit.getAttributes()) {
								if (rowattr.getType() == Attribute.Type.BOOLEAN) {
									boolean value = (Boolean) table.getTable().getValueAt(i, j);
									rowunit.setAttribute(new BooleanAttribute(rowattr.getTitle(), value));
								}
								else if (attr.getType() == Attribute.Type.DOUBLE) {
									double value = (Double) table.getTable().getValueAt(i, j);
									rowunit.setAttribute(new DoubleAttribute(rowattr.getTitle(), value));
								}
								else if (attr.getType() == Attribute.Type.INT) {
									int value = (Integer) table.getTable().getValueAt(i, j);
									rowunit.setAttribute(new IntAttribute(rowattr.getTitle(), value));
								}
								else if (attr.getType() == Attribute.Type.STRING) {
									String value = (String) table.getTable().getValueAt(i, j);
									rowunit.setAttribute(new StringAttribute(rowattr.getTitle(), value));
								}
								j++;
							}
						}
						table = new InputTablePane(_middleEnd, groupattr.getBlankUnit().getAttributes(), groupattr);
					}
					else if (attr.getType() == Attribute.Type.INT) {
						int value = Integer.parseInt(((JTextField) components.get(attr)).getText());
						unit.setAttribute(new IntAttribute(attr.getTitle(), value));
					}
					else if (attr.getType() == Attribute.Type.STRING) {
						String value = ((JTextField) components.get(attr)).getText();
						unit.setAttribute(new StringAttribute(attr.getTitle(), value));
					}
				}
				if (_grouping != null) {
					_grouping.addMember(unit);
					_grouping = null;
				}
				_middleEnd.repaintAll();
			}
		});
		_buttonPanel.add(savebutton);
		this.add(_buttonPanel);
		this.add(Box.createRigidArea(new Dimension(10, 10)));
		this.add(_tablePanel);
		this.setPreferredSize(new Dimension(0,0));
		this.repaint();
	}
}
