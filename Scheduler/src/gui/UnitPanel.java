package gui;

import backbone.*;
import middleend.*;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class UnitPanel extends JPanel {

	private MiddleEnd _middleEnd;
	private Utility _util;
	private JPanel _mainPanel, _buttonPanel, _tablePanel;
	
	public UnitPanel(MiddleEnd m, Unit u) {
		_middleEnd = m;
		_util = new Utility();
		_mainPanel = new JPanel();
		_buttonPanel = new JPanel();
		_tablePanel = new JPanel();
		initialize(u);
	}
	
	public void initialize(final Unit unit) {
		this.setLayout(new GridLayout(0,1));
		_mainPanel.setLayout(new BoxLayout(_mainPanel, BoxLayout.X_AXIS));
		final HashMap<Attribute, JComponent> components = new HashMap<Attribute, JComponent>();
		for (final Attribute attr : unit.getAttributes()) {
			if (attr instanceof GroupingAttribute) {
				GroupingAttribute<Unit> g = (GroupingAttribute<Unit>) attr;
				components.put(attr, new InputTablePane(_middleEnd, g.getBlankUnit().getAttributes(), g.getMembers()));
			}
			JComponent comp = _util.getField(attr);
			if (comp instanceof JButton) {
				((JButton) comp).addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (_tablePanel.getComponent(0) == components.get(attr)) {
							_tablePanel.removeAll();
						}
						else {
							_tablePanel.removeAll();
							_tablePanel.add(components.get(attr));
						}
					}
				});
			}
			if (!components.containsKey(attr))
				components.put(attr, comp);
			_mainPanel.add(comp);
		}
		this.add(_mainPanel);
		JButton savebutton = new JButton();
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
							Unit rowunit = groupattr.getBlankUnit();
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
						unit.setAttribute(new GroupingAttribute(groupattr.getTitle()));
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
			}
		});
		_buttonPanel.add(savebutton);
		this.add(_buttonPanel);
		this.add(_tablePanel);
	}
}
