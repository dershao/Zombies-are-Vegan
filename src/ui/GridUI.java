package ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import assets.ZombieTypes;
import engine.Grid;

public class GridUI extends JPanel {
	private static final long serialVersionUID = -6720923029166827998L;

	private static final int MAX_ZOMBIE_TYPES = 3;
	
	private JPanel plantPanel;
	private Grid grid;
	private int row;
	private int col;
	
	public GridUI(Grid grid) {
		setLayout(new GridBagLayout());
		
		plantPanel = new JPanel();
		plantPanel.setOpaque(false);
		
		this.grid = grid;
		this.row = grid.getRow();
		this.col = grid.getCol();
		this.setOpaque(false);
		
		renderPlant();
		renderZombies();
	}
	
	/**
	 * 
	 */
	public void renderPlant() {
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.VERTICAL;
		c.weighty = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.NORTHWEST;
		
		JLabel plantLabel = new JLabel();
		
		Image plantImage;

		if (grid.getPlant() != null) {
			plantImage = Images.getPlantImage(grid.getPlant().getPlantType());
			
			if (plantImage != null) {
				plantImage = plantImage.getScaledInstance(100, 100, Image.SCALE_DEFAULT);
				plantPanel.setPreferredSize(new Dimension(100, 100));
				plantLabel.setIcon(new ImageIcon(plantImage));
			} else {
				plantLabel.setText(grid.getPlant().toString());
			}
			
			plantPanel.add(plantLabel);
		} 

		plantPanel.setOpaque(false);
		add(plantPanel, c);
	}
	
	/**
	 * 
	 */
	public void renderZombies() {
		
		GridBagConstraints c = new GridBagConstraints();
	
		int numberOfZombieTypes = 0;
		
		for (ZombieTypes zombieType : grid.getZombieTypeCount().keySet()) {
			
			if (numberOfZombieTypes < MAX_ZOMBIE_TYPES) {
				
				ZombiePanel zombiePanel = new ZombiePanel(zombieType, grid.getZombieTypeCount().get(zombieType));
				zombiePanel.setOpaque(false);
				zombiePanel.setBorder(BorderFactory.createEmptyBorder());
				zombiePanel.setPreferredSize(new Dimension(100, 100));
				
				c.fill = GridBagConstraints.VERTICAL;
				c.weighty = 0.5;
				c.gridx = 1;
				c.gridy = numberOfZombieTypes;
				
				add(zombiePanel, c);
				
				numberOfZombieTypes++;
			
			} else {
				
				JPanel fullZombieListPanel = new JPanel();
				
				c.fill = GridBagConstraints.VERTICAL;
				c.weighty = 0.5;
				c.gridx = 1;
				c.gridy = numberOfZombieTypes;
				
				add(fullZombieListPanel, c);
			}
		}
	}

	/**
	 * Get the row of the grid 
	 * 
	 * @return
	 */
	public int getRow() {

		return row;
	}

	/**
	 * Get the column the grid
	 * 
	 * @return
	 */
	public int getCol() {

		return col;
	}

	/**
	 * Set the row of where this grid is located
	 * 
	 * @param row
	 */
	public void setRow(int row) {

		this.row = row;
	}

	/**
	 * Set the column of where this grid is located
	 * 
	 * @param col
	 */
	public void setCol(int col) {

		this.col = col;
	}
}
