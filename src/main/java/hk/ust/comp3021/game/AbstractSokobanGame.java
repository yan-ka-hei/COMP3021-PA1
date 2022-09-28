package hk.ust.comp3021.game;

import hk.ust.comp3021.actions.Action;
import hk.ust.comp3021.actions.ActionResult;
import hk.ust.comp3021.actions.Move;
import hk.ust.comp3021.entities.Box;
import hk.ust.comp3021.entities.Empty;
import hk.ust.comp3021.entities.Player;
import hk.ust.comp3021.entities.Wall;
import org.jetbrains.annotations.NotNull;

import hk.ust.comp3021.actions.Exit;
import hk.ust.comp3021.actions.Undo;
import hk.ust.comp3021.actions.InvalidInput;

import java.util.Objects;

/**
 * A base implementation of Sokoban Game.
 */
public abstract class AbstractSokobanGame implements SokobanGame {
    @NotNull
    protected final GameState state;

    protected AbstractSokobanGame(@NotNull GameState gameState) {
        this.state = gameState;
    }

    /**
     * @return True is the game should stop running.
     * For example when the user specified to exit the game or the user won the game.
     */
    protected boolean shouldStop() {
        // TODO
        return state.isWin();
    }

    /**
     * @param action The action received from the user.
     * @return The result of the action.
     */
    protected ActionResult processAction(@NotNull Action action) {
        // TODO
        if (action instanceof Move m && Objects.requireNonNull(state.getPlayerPositionById(m.getInitiator())).x() == -10000 && Objects.requireNonNull(state.getPlayerPositionById(m.getInitiator())).y() == -10000){
            return new ActionResult.Failed(action, "Player not found.");
        }
        return switch (action){
            case Move.Down d ->{
                Position player_pos = state.getPlayerPositionById(d.getInitiator());
                Position down = Position.of(player_pos.x(), player_pos.y()+1);
                if (state.getEntity(down) instanceof Wall){
                    yield new ActionResult.Failed(action,"You hit a wall.\n");
                }
                else if(state.getEntity(down) instanceof Box){
                    Position check = Position.of(player_pos.x(), player_pos.y()+2);
                    if (state.getEntity(check) instanceof Box || state.getEntity(check) instanceof Wall || state.getEntity(check) instanceof Player){
                        yield new ActionResult.Failed(action,"Failed to push the box.\n");
                    }

                    else{
                        state.move(down, Position.of(down.x(), down.y()+1));
                        state.move(player_pos, Position.of(player_pos.x(), player_pos.y()+1));
                        state.checkpoint();
                        System.out.println(state.getEntity(Position.of(down.x(), down.y()+1)));
                        yield new ActionResult.Success(action);
                    }
                }

                else if(state.getAllPlayerPositions().contains(down)){
                    yield new ActionResult.Failed(action,"You hit another player.\n");
                }

                else if(state.getEntity(down) instanceof Empty){
                    state.move(player_pos, Position.of(player_pos.x(), player_pos.y()+1));
                    state.checkpoint();
                    yield new ActionResult.Success(action);
                }
                else{
                    yield new ActionResult.Failed(action,"na");
                }
            }

            case Move.Left l -> {
                Position player_pos = state.getPlayerPositionById(l.getInitiator());
                Position left = Position.of(player_pos.x()-1, player_pos.y());
                if (state.getEntity(left) instanceof Wall){
                    yield new ActionResult.Failed(action,"You hit a wall.\n");
                }
                else if(state.getEntity(left) instanceof Box){
                    Position check = Position.of(player_pos.x()-2, player_pos.y());
                    if (state.getEntity(check) instanceof Box || state.getEntity(check) instanceof Wall || state.getEntity(check) instanceof Player){
                        yield new ActionResult.Failed(action,"Failed to push the box.\n");
                    }
                    else{
                        state.move(left, Position.of(left.x()-1, left.y()));
                        state.move(player_pos, Position.of(player_pos.x()-1, player_pos.y()));
                        state.checkpoint();
                        yield new ActionResult.Success(action);
                    }
                }
                else if(state.getAllPlayerPositions().contains(left)){
                    yield new ActionResult.Failed(action,"You hit another player.\n");
                }
                else if(state.getEntity(left) instanceof Empty){
                    state.move(player_pos, Position.of(player_pos.x()-1, player_pos.y()));
                    state.checkpoint();
                    yield new ActionResult.Success(action);
                }
                else{
                    yield new ActionResult.Failed(action,"na");
                }
            }

            case Move.Right r -> {
                Position player_pos = state.getPlayerPositionById(r.getInitiator());
                Position right = Position.of(player_pos.x()+1, player_pos.y());
                if (state.getEntity(right) instanceof Wall){
                    yield new ActionResult.Failed(action,"You hit a wall.\n");
                }
                else if(state.getEntity(right) instanceof Box){
                    Position check = Position.of(player_pos.x()+2, player_pos.y());
                    if (state.getEntity(check) instanceof Box || state.getEntity(check) instanceof Wall || state.getEntity(check) instanceof Player){
                        yield new ActionResult.Failed(action,"Failed to push the box.\n");
                    }
                    else{
                        state.move(right, Position.of(right.x()+1, right.y()));
                        state.move(player_pos, Position.of(player_pos.x()+1, player_pos.y()));
                        state.checkpoint();
                        yield new ActionResult.Success(action);
                    }
                }
                else if(state.getAllPlayerPositions().contains(right)){
                    yield new ActionResult.Failed(action,"You hit another player.\n");
                }
                else if(state.getEntity(right) instanceof Empty){
                    state.move(player_pos, Position.of(player_pos.x()+1, player_pos.y()));
                    state.checkpoint();
                    yield new ActionResult.Success(action);
                }
                else{
                    yield new ActionResult.Failed(action,"na");
                }
            }

            case Move.Up u ->{
                Position player_pos = state.getPlayerPositionById(u.getInitiator());
                Position up = Position.of(player_pos.x(), player_pos.y()-1);
                if (state.getEntity(up) instanceof Wall){
                    yield new ActionResult.Failed(action,"You hit a wall.\n");
                }
                else if(state.getEntity(up) instanceof Box){
                    Position check = Position.of(player_pos.x(), player_pos.y()-2);
                    if (state.getEntity(check) instanceof Box || state.getEntity(check) instanceof Wall || state.getEntity(check) instanceof Player){
                        yield new ActionResult.Failed(action,"Failed to push the box.\n");
                    }
                    else{
                        state.move(up, Position.of(up.x(), up.y()-1));
                        state.move(player_pos, Position.of(player_pos.x(), player_pos.y()-1));
                        state.checkpoint();
                        yield new ActionResult.Success(action);
                    }
                }
                else if(state.getAllPlayerPositions().contains(up)){
                    yield new ActionResult.Failed(action,"You hit another player.\n");
                }
                else if(state.getEntity(up) instanceof Empty){
                    state.move(player_pos, Position.of(player_pos.x(), player_pos.y()-1));
                    state.checkpoint();
                    yield new ActionResult.Success(action);
                }
                else{
                    yield new ActionResult.Failed(action,"na");
                }
            }

            case Exit ignored2 -> new ActionResult.Success(action);

            case Undo ignored1 -> {
                if (state.getUndoQuota().orElse(0)==0){
                    yield new ActionResult.Failed(action, "You have run out of your undo quota.\n");
                }
                else{
                    state.undo();
                    yield new ActionResult.Success(action);
                }
            }

            case InvalidInput ignored -> new ActionResult.Failed(action,"Invalid Input.\n");
        };
    }
}
