package engine;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import assets.PlantTypes;
import commands.Command;
import commands.DigCommand;
import commands.EndTurnCommand;
import commands.PlaceCommand;
import util.Logger;

/**
 * This is the Command History Queue for the Player throughout a Game
 * @author David Wang
 */
public class CommandQueue implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private transient List<GameListener> listeners; 
	private static Logger LOG = new Logger("Command Queue");
	private Game game;
	private LinkedList<Command> undoQueue;
	private LinkedList<Command> redoQueue;
	private EndTurnCommand currentEndTurn;
	
	public CommandQueue(Game game, List<GameListener> listeners) {
		this.game = game;
		this.listeners = listeners;
		this.currentEndTurn = null;
		undoQueue = new LinkedList<Command>();
		redoQueue = new LinkedList<Command>();
	}
	
	/**
	 * Adds a Place Command to the Command History
	 * @param type the planttype that was placed
	 * @param x the location of the placement
	 * @param y the location of the placement
	 */
	public void registerPlace(PlantTypes type, int x, int y) {
		redoQueue.clear(); //a new command prevents redo-ing old commands
		undoQueue.addFirst(new PlaceCommand(type, x, y));
		LOG.debug("registered place command");
	}

	/**
	 * Adds a Digup Command to the Command History
	 * @param type the planttype that was dug up
	 * @param x the location of the placement
	 * @param y the location of the placement
	 */
	public void registerDig(PlantTypes type, int x, int y) {
		redoQueue.clear(); //a new command prevents redo-ing old commands
		undoQueue.addFirst(new DigCommand(type, x, y));
		LOG.debug("registered dig command");
	}

	/**
	 * Adds a lawn mower to the end turn mower list
	 * @param grids the array of grids the mower affected
	 */
	public void registerMow(int row) {
		currentEndTurn.addMowerRow(row);
		LOG.debug("registered lawn mower");
	}
	
	/**
	 * Adds an End Turn to the Command History
	 * @param board
	 */
	public void registerEndTurn(Board board) {
		redoQueue.clear(); //a new command prevents redo-ing old commands 
		currentEndTurn = new EndTurnCommand(board, game.getPurse());
		undoQueue.addFirst(currentEndTurn);
		 
		LOG.debug("registered end turn command");
	}
	
	/**
	 * Set the listeners of the command queue
	 */
	public void setGameListeners(List<GameListener> listeners) {
		
		this.listeners = listeners;
	}
	
	
	/**
	 * Undos the most recent command
	 * @return
	 */
	public boolean undo() {
		if (undoQueue.isEmpty()) {
			LOG.debug("No Commands to undo");
			return false;
		}
		
		Command c = undoQueue.removeFirst();
		switch (c.getCommand()){
			case DIGUP:
				redoQueue.addFirst(c);
				game.getBoard().placePlant(PlantTypes.toPlant(((DigCommand)c).getType()), ((DigCommand)c).getLocX(), ((DigCommand)c).getLocY()); //re-place the plant
				for (GameListener gl : listeners) {
					gl.updateGrid(((DigCommand)c).getLocX(),((DigCommand)c).getLocY());
				}
				LOG.debug("undo dig command");
				break;
			case PLACE:
				redoQueue.addFirst(c);
				game.getBoard().removePlant(((PlaceCommand)c).getLocX(), ((PlaceCommand)c).getLocY()); //remove the plant
				game.getPurse().addPoints(PlantTypes.toPlant(((PlaceCommand)c).getType()).getCost()); //refund the plant
				for (GameListener gl : listeners) {
					gl.updateGrid(((PlaceCommand)c).getLocX(),((PlaceCommand)c).getLocY());
					gl.updatePurse();
				}
				LOG.debug("undo place command");
				break;
			case ENDTURN:
				currentEndTurn = new EndTurnCommand(game.getBoard(), game.getPurse());
				redoQueue.addFirst(currentEndTurn);
				currentEndTurn.setMowerList(((EndTurnCommand)c).getMowerList()); //keep track of the mowers used for redo functionality
				
				game.getBoard().setBoard(((EndTurnCommand)c).getBoard());
				game.getPurse().setPoints(((EndTurnCommand)c).getResources());
				game.decrementTurns();
				
				if(!((EndTurnCommand)c).getMowerList().isEmpty()) //if a lawnmower was used
				{
					for (GameListener gl : listeners) {
						gl.updateAllGrids();
						gl.updatePurse();
						gl.updateTurnNumber();
						
						for(Integer m : ((EndTurnCommand)c).getMowerList())
						{
							gl.updateMower(m, game.getBoard().isMowerAvaliable(m)); // update the lawn mower image
							game.getBoard().setMoverAvaliable(m);
						}
					}
					break;
				}
				
				for (GameListener gl : listeners) {
					gl.updateAllGrids();
					gl.updatePurse();
					gl.updateTurnNumber();
				}
				
				LOG.debug("undo end turn command");
				break;
			default:
				break;
		}
		return true;
	}
	
	/**
	 * Redos the most recent undo. Allows redo until the no more undone commands OR user issues a new command
	 * @return
	 */
	public boolean redo() {
		if (redoQueue.isEmpty()) {
			LOG.debug("No Commands to undo");
			return false;
		}
		
		Command c = redoQueue.removeFirst();
		switch (c.getCommand()){
			case DIGUP: //redo a digup command
				undoQueue.addFirst(c); //allow us to undo redo
				game.getBoard().removePlant(((DigCommand)c).getLocX(), ((DigCommand)c).getLocY()); //re-place the plant
				for (GameListener gl : listeners) {
					gl.updateGrid(((DigCommand)c).getLocX(),((DigCommand)c).getLocY());
				}
				LOG.debug("redo dig command");
				break;
			case ENDTURN: //redo an end turn command
				currentEndTurn = new EndTurnCommand(game.getBoard(), game.getPurse()); 
				currentEndTurn.setMowerList(((EndTurnCommand)c).getMowerList()); //keep track of the lawn mower list (for undo functionality)
				undoQueue.addFirst(currentEndTurn);
				game.getBoard().setBoard(((EndTurnCommand)c).getBoard());
				game.getPurse().setPoints(((EndTurnCommand)c).getResources());
				game.incrementTurns();
				
				if(!((EndTurnCommand)c).getMowerList().isEmpty()) //if a lawnmower was used
				{
					for (GameListener gl : listeners) {
						gl.updateAllGrids();
						gl.updatePurse();
						gl.updateTurnNumber();
						
						for(Integer m : ((EndTurnCommand)c).getMowerList())
						{
							gl.updateMower(m, game.getBoard().isMowerAvaliable(m)); //update lawnmower image
							game.getBoard().removeMower(m);
						}
					}
					break;
				}
				
				for (GameListener gl : listeners) {
					gl.updateAllGrids();
					gl.updatePurse();
					gl.updateTurnNumber();
				}
				LOG.debug("redo end turn command");
				break;
			case PLACE: //redo a place command
				undoQueue.addFirst(c);
				game.getBoard().placePlant(PlantTypes.toPlant(((PlaceCommand)c).getType()), ((PlaceCommand)c).getLocX(), ((PlaceCommand)c).getLocY()); //place the plant
				game.getPurse().spendPoints(PlantTypes.toPlant(((PlaceCommand)c).getType()).getCost()); //re-spend the plant cost
				
				for (GameListener gl : listeners) {
					gl.updateGrid(((PlaceCommand)c).getLocX(),((PlaceCommand)c).getLocY());
					gl.updatePurse();
				}
				LOG.debug("redo place command");
				break;
			default:
				break;
		}
		return true;
	}
}
