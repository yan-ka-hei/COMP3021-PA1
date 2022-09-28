package hk.ust.comp3021.game;

import hk.ust.comp3021.entities.*;
import hk.ust.comp3021.utils.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.PipedOutputStream;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.ArrayList;



/**
 * The state of the Sokoban Game.
 * Each game state represents an ongoing game.
 * As the game goes, the game state changes while players are moving while the original game map stays the unmodified.
 * <b>The game state should not modify the original game map.</b>
 * <p>
 * GameState consists of things changing as the game goes, such as:
 * <li>Current locations of all crates.</li>
 * <li>A move history.</li>
 * <li>Current location of player.</li>
 * <li>Undo quota left.</li>
 */
public class GameState {

    private GameMap map;
    private ArrayList<Position> player_loc;
    private ArrayList<Integer> player;
    private ArrayList<Position> box_loc;
    private ArrayList<Integer> box;
    private int undo;

    private ArrayList<ArrayList<Position>> move_history;
    private ArrayList<ArrayList<Position>> checkpoint;
    private ArrayList<Position> effective_loc;

    /**
     * Create a running game state from a game map.
     *
     * @param map the game map from which to create this game state.
     */
    public GameState(@NotNull GameMap map) {
        this.map = map;
        player_loc = map.getInit_player_loc();
        player = map.getInit_player();
        undo = map.getUndoLimit().get();
        box = map.getInit_box();
        box_loc = map.getInit_box_loc();
        effective_loc = map.getEffective_loc();
        move_history = new ArrayList<ArrayList<Position>>();
        checkpoint = new ArrayList<ArrayList<Position>>();
    }

    public GameState(){}


    /**
     * Get the current position of the player with the given id.
     *
     * @param id player id.
     * @return the current position of the player.
     */
    public @Nullable Position getPlayerPositionById(int id) {
        // TODO
        if (player.contains(id)) {
            return player_loc.get(player.indexOf(id));
        } else {
            throw new NotImplementedException();
        }
    }

    /**
     * Get current positions of all players in the game map.
     *
     * @return a set of positions of all players.
     */
    public @NotNull Set<Position> getAllPlayerPositions() {
        // TODO
       var player_loc_set = new HashSet<Position>();
       for (Position i: player_loc){
           player_loc_set.add(i);
       }
       return player_loc_set;
    }

    /**
     * Get the entity that is currently at the given position.
     *
     * @param position the position of the entity.
     * @return the entity object.
     */
    public @Nullable Entity getEntity(@NotNull Position position) {
        // TODO
        if (box_loc.contains(position)){
            return new Box(box.get(box_loc.indexOf(position)));
        }
        else if (player_loc.contains(position)){
            return new Player(player.get(player_loc.indexOf(position)));
        }
        else if (map.getEntity(position) instanceof Wall){
            return new Wall();
        }
        else if (effective_loc.contains(position)){
            return new Empty();
        }
        return null;
    }

    /**
     * Get all box destination positions as a set in the game map.
     * This should be the same as that in {@link GameMap} class.
     *
     * @return a set of positions.
     */
    public @NotNull @Unmodifiable Set<Position> getDestinations() {
        // TODO
        return map.getDestinations();
    }

    /**
     * Get the undo quota currently left, i.e., the maximum number of undo actions that can be performed from now on.
     * If undo is unlimited,
     *
     * @return the undo quota left (using {@link Optional#of(Object)}) if the game has an undo limit;
     * {@link Optional#empty()} if the game has unlimited undo.
     */
    public Optional<Integer> getUndoQuota() {
        // TODO
        return Optional.of(undo);
    }

    /**
     * Check whether the game wins or not.
     * The game wins only when all box destinations have been occupied by boxes.
     *
     * @return true is the game wins.
     */
    public boolean isWin() {
// TODO
        for (Position dest: getDestinations()){
            if (box_loc.contains(dest) == false){
                return false;
            }
        }
        return true;
    }

    /**
     * Move the entity from one position to another.
     * This method assumes the validity of this move is ensured.
     * <b>The validity of the move of the entity in one position to another need not to check.</b>
     *
     * @param from The current position of the entity to move.
     * @param to   The position to move the entity to.
     */


    public void move(Position from, Position to) {
        // TODO
        ArrayList<Position> move = new ArrayList<Position>();
        move.add(from);
        move.add(to);
        if (player_loc.contains(from)){
            player_loc.set(player_loc.indexOf(from), to);
        }
        else if (box_loc.contains(from)){
            box_loc.set(box_loc.indexOf(from), to);
        }

        move_history.add(move);
    }

    /**
     * Record a checkpoint of the game state, including:
     * <li>All current positions of entities in the game map.</li>
     * <li>Current undo quota</li>
     * <p>
     * Checkpoint is used in {@link GameState#undo()}.
     * Every undo actions reverts the game state to the last checkpoint.
     */
    public void checkpoint() {

        checkpoint.removeAll(checkpoint);
        for (ArrayList<Position> check:move_history) {
            checkpoint.add(check);
        }
        move_history.removeAll(move_history);
    }

    /**
     * Revert the game state to the last checkpoint in history.
     * This method assumes there is still undo quota left, and decreases the undo quota by one.
     * <p>
     * If there is no checkpoint recorded, i.e., before moving any box when the game starts,
     * revert to the initial game state.
     */
    public void undo() {
        // TODO
        if(undo != 0){
            for (ArrayList<Position> check : checkpoint) {
                if (player_loc.contains(check.get(1))) {
                    player_loc.set(player_loc.indexOf(check.get(1)), check.get(0));
                } else if (box_loc.contains(check.get(1))) {
                    box_loc.set(box_loc.indexOf(check.get(1)), check.get(0));
                }
            }
        }

        if (undo > 0){
            undo--;
        }
    }

    /**
     * Get the maximum width of the game map.
     * This should be the same as that in {@link GameMap} class.
     *
     * @return maximum width.
     */
    public int getMapMaxWidth() {
        // TODO
        return map.getMaxWidth();
    }

    /**
     * Get the maximum height of the game map.
     * This should be the same as that in {@link GameMap} class.
     *
     * @return maximum height.
     */
    public int getMapMaxHeight() {
        // TODO
        return map.getMaxHeight();
    }
}
