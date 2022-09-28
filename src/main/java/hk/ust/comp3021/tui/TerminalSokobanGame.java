package hk.ust.comp3021.tui;


import hk.ust.comp3021.actions.Action;
import hk.ust.comp3021.actions.ActionResult;
import hk.ust.comp3021.actions.Exit;
import hk.ust.comp3021.game.AbstractSokobanGame;
import hk.ust.comp3021.game.GameState;
import hk.ust.comp3021.game.InputEngine;
import hk.ust.comp3021.game.RenderingEngine;
import hk.ust.comp3021.utils.NotImplementedException;

/**
 * A Sokoban game running in the terminal.
 */
public class TerminalSokobanGame extends AbstractSokobanGame {

    private final InputEngine inputEngine;

    private final RenderingEngine renderingEngine;

    /**
     * Create a new instance of TerminalSokobanGame.
     * Terminal-based game only support at most two players, although the hk.ust.comp3021.game package supports up to 26 players.
     * This is only because it is hard to control too many players in a terminal-based game.
     *
     * @param gameState       The game state.
     * @param inputEngine     the terminal input engin.
     * @param renderingEngine the terminal rendering engine.
     * @throws IllegalArgumentException when there are more than two players in the map.
     */
    public TerminalSokobanGame(GameState gameState, TerminalInputEngine inputEngine, TerminalRenderingEngine renderingEngine) {
        super(gameState);
        this.inputEngine = inputEngine;
        this.renderingEngine = renderingEngine;
        // TODO
        // Check the number of players
        if (gameState.getAllPlayerPositions().size() > 2){
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void run() {
        // TODO
        renderingEngine.message("Sokoban game is ready.\n");
        renderingEngine.render(state);
        renderingEngine.message("\nUndo Quota: "+String.valueOf(state.getUndoQuota().orElse(0))+"\n");
        while (true){
            var action = processAction(inputEngine.fetchAction());
            switch (action){
                case ActionResult.Failed fail -> renderingEngine.message(fail.getReason());
                case default -> {}
            }
            renderingEngine.render(state);
            renderingEngine.message("\nUndo Quota: "+String.valueOf(state.getUndoQuota().orElse(0))+"\n");
            if (action.getAction() instanceof Exit || shouldStop()){
                renderingEngine.message("Game exits.\n");
                break;
            }
        }


    }
}
