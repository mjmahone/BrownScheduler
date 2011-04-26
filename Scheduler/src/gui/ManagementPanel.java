package gui;

import java.awt.GridBagLayout;
import javax.swing.JPanel;

public class ManagementPanel extends JPanel implements GUIConstants {

	public static final long serialVersionUID = 1L;

	/**
	 * This is the default constructor
	 */
	public ManagementPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	public void initialize() {
		this.setSize(600, 400); //TODO: make constants
		this.setBackground(java.awt.Color.blue);
		this.setLayout(new GridBagLayout());
	}

}  //  @jve:decl-index=0:visual-constraint="10,2"
