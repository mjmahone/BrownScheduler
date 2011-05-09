package gui;

import backbone.*;
import middleend.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class UnitPanel extends JPanel implements GUIConstants {

	private MiddleEnd _middleEnd;
	private JPanel _mainPanel, _buttonPanel, _tablePanel;
	private Grouping<Unit> _grouping;
	
	public UnitPanel(MiddleEnd m, Unit u) {
		_middleEnd = m;
		_grouping = u.getMemberOf();
		_mainPanel = new JPanel();
		_buttonPanel = new JPanel();
		_tablePanel = new JPanel();
		initialize(u, "Save Changes");
	}
	
	public UnitPanel(MiddleEnd m, Unit u, Grouping<Unit> g) {
		_middleEnd = m;
		_grouping = g;
		_mainPanel = new JPanel();
		_buttonPanel = new JPanel();
		_tablePanel = new JPanel();
		initialize(u, "Save Changes and Add Another New Unit");
	}
	
	public void initialize(final Unit unit, String buttonstring) {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		_mainPanel.setLayout(new BoxLayout(_mainPanel, BoxLayout.X_AXIS));
		_tablePanel.setLayout(new BoxLayout(_tablePanel, BoxLayout.Y_AXIS));
//		_tablePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, INPUTTABLE_HEIGHT));
		final HashMap<Attribute, JComponent> components = new HashMap<Attribute, JComponent>();
		for (final Attribute attr : unit.getAttributes()) {
			JLabel titleLabel = Utility.getTitleLabel(attr);
			if (attr instanceof GroupingAttribute) {
				GroupingAttribute<Unit> g = (GroupingAttribute<Unit>) attr;
				components.put(attr, new InputTablePane(_middleEnd, g.getBlankUnit().getAttributes(), g));
			}
			JComponent comp = Utility.getField(attr);
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
							_tablePanel.add(((InputTablePane) components.get(attr)).getTable().getTableHeader());
							_tablePanel.add(components.get(attr));
						}
						else if (hasPanel) {
							_tablePanel.removeAll();
						}
						else {
							_tablePanel.removeAll();
							_tablePanel.add(((InputTablePane) components.get(attr)).getTable().getTableHeader());
							_tablePanel.add(components.get(attr));
						}
						_tablePanel.repaint();
					}
				});
			}
			if (!components.containsKey(attr))
				components.put(attr, comp);
			JPanel toAdd = new JPanel();
			toAdd.setLayout(new BoxLayout(toAdd, BoxLayout.Y_AXIS));
			toAdd.add(titleLabel);
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
						GroupingAttribute<Unit> groupattr = (GroupingAttribute) attr;
						for (int i = 0; i < table.getTable().getRowCount(); i++) {
							Unit rowunit;
							boolean rowunitisnull = true;
							boolean alreadyadded = false;
							if (i < table.getUnitsInRowsList().size()) {
								rowunit = table.getUnitsInRowsList().get(i);
								alreadyadded = true;
							}
							else {
								rowunit = groupattr.getBlankUnit();
							}
							int j = 0;
							for (Attribute rowattr : rowunit.getAttributes()) {
								if (rowattr.getType() == Attribute.Type.BOOLEAN) {
									boolean value = false;
									if (table.getTable().getValueAt(i, j) != null) {
										value = (Boolean) table.getTable().getValueAt(i, j);
										rowunitisnull = false;
									}
									rowunit.setAttribute(new BooleanAttribute(rowattr.getTitle(), value));
								}
								else if (rowattr.getType() == Attribute.Type.DOUBLE) {
									double value = 0;
									if (table.getTable().getValueAt(i, j) != null) {
										value = (Double) table.getTable().getValueAt(i, j);
										rowunitisnull = false;
									}
									rowunit.setAttribute(new DoubleAttribute(rowattr.getTitle(), value));
								}
								else if (rowattr.getType() == Attribute.Type.INT) {
									int value = 0;
									if (table.getTable().getValueAt(i, j) != null) {
										value = (Integer) table.getTable().getValueAt(i, j);
										rowunitisnull = false;
									}
									rowunit.setAttribute(new IntAttribute(rowattr.getTitle(), value));
								}
								else if (rowattr.getType() == Attribute.Type.STRING) {
									String value = "";
									if (table.getTable().getValueAt(i, j) != null) {
										value = (String) table.getTable().getValueAt(i, j);
										rowunitisnull = false;
									}
									rowunit.setAttribute(new StringAttribute(rowattr.getTitle(), value));
								}
								else if (rowattr.getType() == Attribute.Type.UNIT) {
									Unit value = null;
									if (table.getTable().getValueAt(i, j) != null) {
										DefaultCellEditor editor = (DefaultCellEditor) table.getTable().getCellEditor(i, j);
										UnitAttributeComboBox combobox = (UnitAttributeComboBox) editor.getComponent();
										value = combobox.getSelectedUnit();
										rowunitisnull = false;//TODO:Does this work?
									}
									rowunit.setAttribute(new UnitAttribute(rowattr.getTitle(), value));
								}
								j++;
							}
							if (!rowunitisnull && !alreadyadded) {
								groupattr.addMember(rowunit);
							}
						}
						table = new InputTablePane(_middleEnd, groupattr.getBlankUnit().getAttributes(), groupattr);
						components.put(attr, table);
					}
					else if (attr.getType() == Attribute.Type.INT) {
						int value = Integer.parseInt(((JTextField) components.get(attr)).getText());
						unit.setAttribute(new IntAttribute(attr.getTitle(), value));
					}
					else if (attr.getType() == Attribute.Type.STRING) {
						String value = ((JTextField) components.get(attr)).getText();
						unit.setAttribute(new StringAttribute(attr.getTitle(), value));
					}
					else if (attr.getType() == Attribute.Type.UNIT) {
						Unit value = ((UnitAttributeComboBox) components.get(attr)).getSelectedUnit();
						unit.setAttribute(new UnitAttribute(attr.getTitle(), value));
					}
				}
				if (_grouping != null) {
					_grouping.addMember(unit);
					_grouping = null;
				}
				_middleEnd.repaintAll();
			}
		});
		final JButton actuallydeletebutton = new JButton("Actually delete this unit");
		actuallydeletebutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_mainPanel.removeAll();
				_buttonPanel.removeAll();
				_tablePanel.removeAll();
				unit.deleteFromGrouping();
				_middleEnd.repaintAll();
			}
		});
		actuallydeletebutton.setVisible(false);
		JButton deletebutton = new JButton("Delete this unit");
		deletebutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actuallydeletebutton.setVisible(true);
				_buttonPanel.repaint();
			}
		});
		_buttonPanel.add(savebutton);
		_buttonPanel.add(deletebutton);
		_buttonPanel.add(actuallydeletebutton);
		this.add(_buttonPanel);
		this.add(Box.createRigidArea(new Dimension(10, 10)));
		this.add(_tablePanel);
		this.setPreferredSize(new Dimension(0,0));
		this.repaint();
	}
}
