package input;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import assets.Plant;
import assets.PlantTypes;
import javax.swing.BorderFactory;

import assets.Plant;
import engine.Board;
import engine.Game;
import engine.Purse;
import ui.Card;
import ui.GameUI;
import ui.GridUI;

public class GameController {
	
	private Game game;
	private GameUI ui;
	private boolean firstClick; //Every click should toggle the first flag
	private Card selectedCard; //The selected card on click #1
	private Board gameBoard;
	private Purse userResources;
	
	// Selected to remove a plant
	private boolean removingPlant; 
	
	public GameController(GameUI ui, Game game) {
		this.game = game;
		this.ui = ui;
		this.selectedCard = null;
		this.firstClick = true;
		this.gameBoard = this.game.getBoard();
		this.userResources = this.game.getPurse();
		
		this.ui.addGridListeners(new GridListener());
		this.ui.addMenuButtonListeners(new MenuBarListener());
		this.ui.addUnitSelectionListeners(new UnitSelectListener());
		this.ui.addGameButtonListeners(new GameButtonListener());
		firstClick = true;
	}
	
	private class GameButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
		}		
	}
	
	private class MenuBarListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			switch(e.getActionCommand()) {
				case "Menu":
					break;
				case "End turn":
					break;
				case "Quit":
					break;
			}
		}
	}
	
	private class GridListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent arg0) {
			GridUI source = (GridUI) arg0.getSource();
			
			final int sourceRow = source.getRow();
			final int sourceCol = source.getCol();
			
			Plant selectedPlant = selectedCard.getPlant();
			if (selectedCard != null) {
				if (userResources.canSpend(selectedPlant.getCost())) {
					if (gameBoard.getGrid(sourceRow, sourceCol).setPlant(selectedPlant)) {
						userResources.spendPoints(selectedPlant.getCost());
						source.renderPlant();
					}
				}
				selectedCard = null;
				ui.revertHighlight(selectedCard);
			} else if (removingPlant) {
				gameBoard.getGrid(sourceRow, sourceCol).setPlant(null);
				source.renderPlant();
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			GridUI source = (GridUI) arg0.getSource();
			
			source.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			GridUI source = (GridUI) arg0.getSource();
			
			source.setBorder(BorderFactory.createEmptyBorder());
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// not implemented
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// not implemented
		}
		
	}
	
	
	private class UnitSelectListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			System.out.println("Mouseclicks");
			Card card = (Card)e.getSource(); 
	        //if this is the first pick and a square with a piece was picked,
	        // remember the piece, check if it is viable and highlight the card
			if(firstClick)
			{
				selectedCard = card; //save the selected card (to perhaps compare for second click)
				ui.setHighlight(card);
				firstClick = false;
			}
			else //indicates that the second click is on another unit card 
			{
				ui.revertHighlight(selectedCard);
				ui.setHighlight(card);
				selectedCard = card;
				firstClick = true;
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class LawnMowerListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}

	}
}
