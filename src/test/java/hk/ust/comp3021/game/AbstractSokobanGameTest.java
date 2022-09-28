package hk.ust.comp3021.game;

import hk.ust.comp3021.actions.Action;
import hk.ust.comp3021.actions.ActionResult;
import hk.ust.comp3021.actions.Move;
import hk.ust.comp3021.utils.TestHelper;
import hk.ust.comp3021.utils.TestKind;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class AbstractSokobanGameTest {

    @Tag(TestKind.PUBLIC)
    @Test
    void testMove() {
        String mapText = """
            233
            ######
            #A..@#
            #....#
            #a...#
            ######
            """;
        final var testMap = TestHelper.parseGameMap(mapText);
        final var gameState = spy(new GameState(testMap));

        final var game = new SokobanGameForTesting(gameState);
        final var result = game.feedActionForProcessing(new Move.Down(0));

        assertTrue(result instanceof ActionResult.Success);
        verify(gameState, times(1)).move(any(), any());
    }

    @Tag(TestKind.PUBLIC)
    @Test
    void testHitWall() {
        String mapText = """
            233
            ######
            #A..@#
            ##...#
            #a...#
            ######
            """;
        final var testMap = TestHelper.parseGameMap(mapText);
        final var gameState = spy(new GameState(testMap));

        final var game = new SokobanGameForTesting(gameState);
        final var result = game.feedActionForProcessing(new Move.Down(0));

        assertTrue(result instanceof ActionResult.Failed);
        verify(gameState, never()).move(any(), any());
    }

    @Tag(TestKind.PUBLIC)
    @Test
    void testPushBox() {
        String mapText = """
            233
            ######
            #A..@#
            #a...#
            #....#
            ######
            """;
        final var testMap = TestHelper.parseGameMap(mapText);
        final var gameState = spy(new GameState(testMap));

        final var game = new SokobanGameForTesting(gameState);
        final var result = game.feedActionForProcessing(new Move.Down(0));

        assertTrue(result instanceof ActionResult.Success);
        verify(gameState, times(2)).move(any(), any());
        verify(gameState, times(1)).checkpoint();
    }


    @Tag(TestKind.PUBLIC)
    @Test
    void testShouldStopWhenWin() {
        final var gameState = mock(GameState.class);
        when(gameState.isWin()).thenReturn(true);

        final var game = new SokobanGameForTesting(gameState);
        assertTrue(game.shouldStop());
    }

    private static class SokobanGameForTesting extends AbstractSokobanGame {

        protected SokobanGameForTesting(GameState gameState) {
            super(gameState);
        }

        @Override
        public void run() {
        }

        public ActionResult feedActionForProcessing(Action action) {
            return processAction(action);
        }
    }
}
