package gui;

import middleend.*;
import backbone.*;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

/**
 * This class is the table used to allow the editing of GroupingAttributes
 * of a unit.
 */
public class InputTablePane extends JScrollPane implements GUIConstants {

	private MiddleEnd _middleEnd;
	private JTable _table;
	private GroupingAttribute<Unit> _unitsInRows;
	
	/**
	 * Constructor.
	 * @param middleEnd
	 * @param headers
	 * @param group
	 */
	public InputTablePane(MiddleEnd middleEnd, List<Attribute> headers, GroupingAttribute<Unit> group) {
		super(new JTable());
		for (int i = 0; i < this.getComponentCount(); i++) {
			if (this.getComponent(i) instanceof JTable) {
				_table = (JTable) this.getComponent(i);
				break;
			}
		}
		_middleEnd = middleEnd;
		_unitsInRows = group;
		this.initialize(headers, group);
	}
	
	/**
	 * Getter for the table in this scrollpane
	 * @return JTable
	 */
	public JTable getTable() {
		return _table;
	}
	

	/**
	 * Initializes this InputTablePane.
	 * @param headers
	 * @param group
	 */
	private void initialize(final List<Attribute> headers, GroupingAttribute<Unit> group) {
		this.setPreferredSize(INPUTTABLE_SIZE);
		this.setMaximumSize(new Dimension(Integer.MAX_VALUE, INPUTTABLE_HEIGHT));
		this.setVisible(false);
		this.setToolTipText("Right click and click \"Delete\" to delete a row and delete the unit from this table (not permanently). Use CTRL+V to paste in this table from an Excel spreadsheet.");
		_table = new JTable() {
//			public TableCellEditor getCellEditor(int row, int column) {
//					if (headers.get(column).getType() == Attribute.Type.UNIT) {
//						UnitAttribute<Unit> header = (UnitAttribute) headers.get(column);
//						UnitAttribute<Unit> uatt = new UnitAttribute<Unit>(header.getTitle(), header.getMemberOf());
//						UnitAttributeComboBox combobox = new UnitAttributeComboBox(uatt);
//						return new DefaultCellEditor(combobox);
//					}
//					else {
//						return super.getCellEditor(row, column);
//					}
//				}
		};
		_table.setSize(INPUTTABLE_SIZE);
		_table.setRowHeight(ROW_HEIGHT);
		_table.getTableHeader().setReorderingAllowed(false);

		// Unit and Grouping Attributes can't be edited in the table
		Iterator<Attribute> iter = headers.iterator();
		while (iter.hasNext()) {
			Attribute a = iter.next();
			if ((a.getType() == Attribute.Type.UNIT) || (a.getType() == Attribute.Type.GROUPING))
				iter.remove();
		}

		// Adds existing members of the GroupingAttribute to the table
		List<List<Attribute>> data = new ArrayList<List<Attribute>>();
		for (Unit u : group.getMembers()) {
			data.add(u.getAttributes());
		}
		// Sets the table model
		_table.setModel(new InputTableModel(headers, data, group.isEditable()));
		// Adds extra blank rows to the bottom of the table
		for (int i = 0; i < DEFAULT_TABLE_BLANK_ROWS; i++) {
			((DefaultTableModel) _table.getModel()).insertRow(_table.getRowCount(), new Object[0]);
		}
//		for (int i = 0; i < _table.getColumnModel().getColumnCount(); i++) {
//			if (headers.get(i).getType() == Attribute.Type.UNIT) {
//				TableColumn unitcolumn = _table.getColumnModel().getColumn(i);
//				UnitAttribute<Unit> header = (UnitAttribute) headers.get(i);
//				UnitAttribute<Unit> uatt = new UnitAttribute<Unit>(header.getTitle(), header.getMemberOf());
//				UnitAttributeComboBox combobox = new UnitAttributeComboBox(uatt);
//				unitcolumn.setCellEditor(new DefaultCellEditor(combobox));
//			}
//		}

		// Brings up a popup menu when the user right clicks in the table,
		// allowing the user to delete selected rows (and their corresponding
		// units) from the table
		final JPopupMenu popup = new JPopupMenu();
		JMenuItem menuitem = new JMenuItem("Delete these rows");
		popup.add(menuitem);
		menuitem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<Integer> rows = new ArrayList<Integer>();
				for (int i : _table.getSelectedRows())
					rows.add(i);
				Collections.sort(rows);
				Collections.reverse(rows);
				for (int i : rows) {
					if ( i < _unitsInRows.getMembers().size())
						_unitsInRows.deleteMember(_unitsInRows.getMembers().get(i));
					InputTableModel model = (InputTableModel) _table.getModel();
					model.removeRow(i);
				}
			}
		});
		_table.addMouseListener(new MouseAdapter()	{
			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e))
					showPopup(e);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e))
					showPopup(e);
			}
			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e))
					showPopup(e);
			}
			
			private void showPopup(MouseEvent e) {
				if (e.isPopupTrigger()) {
					popup.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
		// This class allows copying and pasting support from Excel
		ExcelAdapter exceladapt = new ExcelAdapter(_table);
		// Adding the table
		this.getViewport().add(_table);
	}
	
	/**
	 * Returns a list of the units already in the table,
	 * even before editing.
	 * 
	 * @return List<Unit>
	 */
	public List<Unit> getUnitsInRowsList() {
		return _unitsInRows.getMembers();
	}
	
	/**
	 * TableModel for the table in the InputTablePane.
	 */
	private class InputTableModel extends DefaultTableModel {
		private Attribute[] _headers;
		private boolean _editable;
		
		/** 
		 * Constructor.
		 * @param headers
		 * @param data
		 * @param editable
		 */
		public InputTableModel(List<Attribute> headers, List<List<Attribute>> data, boolean editable) {
			_editable = editable;
			_headers = headers.toArray(new Attribute[0]);
			List<Object[]> d = new ArrayList<Object[]>();
			// Adds the values of existing units to the table
			for (List<Attribute> list : data) {
				ArrayList<Object> row = new ArrayList<Object>();
				for (Attribute attr : list) {
					if (attr.getType() == Attribute.Type.BOOLEAN) {
						row.add(new Boolean(((BooleanAttribute) attr).getAttribute()));
					}
					else if (attr.getType() == Attribute.Type.DOUBLE) {
						row.add(new Double(((DoubleAttribute) attr).getAttribute()));
					}
					else if (attr.getType() == Attribute.Type.INT) {
						row.add(new Integer(((IntAttribute) attr).getAttribute()));
					}
					else if (attr.getType() == Attribute.Type.STRING) {
						row.add(((StringAttribute) attr).getAttribute());
					}
//					else if (attr.getType() == Attribute.Type.UNIT) {
//						if ((((UnitAttribute) attr).att) == null) {
//							row.add("");
//						}
//						else {
//							row.add(((UnitAttribute) attr).att.getName());
//						}
//					}
				}
				d.add(row.toArray(new Object[0]));
			}
			// Inserts blank rows into the table
			Object[][] dataarray = d.toArray(new Object[0][0]);
			this.setDataVector(dataarray, _headers);
			this.addTableModelListener(new TableModelListener() {
				@Override
				public void tableChanged(TableModelEvent e) {
					if ((e.getLastRow() == (getRowCount()-1)) && (e.getType() == TableModelEvent.UPDATE)) {
						for (int i = 0; i < DEFAULT_TABLE_BLANK_ROWS; i++) {
							insertRow(getRowCount(), new Object[0]);
						}
					}
				}
			});
		}
		
		/**
		 * Returns whether the cell is editable.
		 * 
		 * @param row
		 * @param columb
		 * @return boolean
		 */
		@Override
		public boolean isCellEditable(int row, int column) {
			return _editable;
		}
		
		/**
		 * Returns the title of a column.
		 * 
		 * @param int
		 * @return String
		 */
		@Override
		public String getColumnName(int i) {
			return _headers[i].getTitle();
		}
		
		/**
		 * Returns the class of a column.
		 * 
		 * @param int
		 * @return Class<?>
		 */
		@Override
		public Class<?> getColumnClass(int i) {
			Attribute a = _headers[i];
			if (a.getType() == Attribute.Type.BOOLEAN) {
				return Boolean.class;
			}
			else if (a.getType() == Attribute.Type.DOUBLE) {
				return Double.class;
			}
			else if (a.getType() == Attribute.Type.INT) {
				return Integer.class;
			}
			else if (a.getType() == Attribute.Type.STRING) {
				return String.class;
			}
			return Object.class;
		}

		/**
		 * Returns the number of columns in this model.
		 * 
		 * @return int
		 */
		@Override
		public int getColumnCount() {
			return _headers.length;
		}
	}
}
