package hk.ust.comp3021.game;

import hk.ust.comp3021.entities.Box;
import hk.ust.comp3021.entities.Empty;
import hk.ust.comp3021.utils.TestHelper;
import hk.ust.comp3021.utils.TestKind;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {

    @Tag(TestKind.PUBLIC)
    @Test
    void testAllPlayerIds() {
        final var testMap = TestHelper.parseGameMap("""
            233
            ######
            #APp@#
            #xXa@@#
            ######
            """);
        final var gameState = new GameState(testMap);

        assertEquals(new HashSet<>(Arrays.asList(Position.of(1, 1), Position.of(2, 1), Position.of(2, 2))), gameState.getAllPlayerPositions());
    }

    @Tag(TestKind.PUBLIC)
    @Test
    void testWin() {
        final var testMap = TestHelper.parseGameMap("""
            233
            ######
            #A.a@#
            #..a@#
            ######
            """);
        final var gameState = new GameState(testMap);
        gameState.move(Position.of(3, 1), Position.of(4, 1));
        gameState.move(Position.of(3, 2), Position.of(4, 2));

        assertTrue(gameState.isWin());
    }

    @Tag(TestKind.PUBLIC)
    @Test
    void testMove() {
        final var gameState = new GameState(TestHelper.parseGameMap("""
            233
            ######
            #A.a@#
            #..a@#
            ######
            """
        ));

        gameState.move(Position.of(1, 1), Position.of(2, 1));
        assertEquals(Position.of(2, 1), gameState.getPlayerPositionById(0));
    }

    @Tag(TestKind.PUBLIC)
    @Test
    void testPushBox() {
        final var gameState = new GameState(TestHelper.parseGameMap("""
            233
            ######
            #.Aa@#
            #..a@#
            ######
            """
        ));

        gameState.move(Position.of(3, 1), Position.of(4, 1));
        gameState.move(Position.of(2, 1), Position.of(3, 1));

        assertEquals(Position.of(3, 1), gameState.getPlayerPositionById(0));
        assertInstanceOf(Box.class, gameState.getEntity(Position.of(4, 1)));
    }

    @Tag(TestKind.PUBLIC)
    @Test
    void testGetUndoLimit() {
        final var gameState = new GameState(TestHelper.parseGameMap("""
            233
            ######
            #.Aa@#
            #..a@#
            ######
            """
        ));
        assertEquals(233, gameState.getUndoQuota().orElse(null));
    }

    @Tag(TestKind.PUBLIC)
    @Test
    void testUndoWhenThereIsCheckpoint() {
        final var gameState = new GameState(TestHelper.parseGameMap("""
            233
            ######
            #.Aa@#
            #..a@#
            ######
            """
        ));
        gameState.move(Position.of(3, 1), Position.of(4, 1));
        gameState.move(Position.of(2, 1), Position.of(3, 1));
        gameState.checkpoint();

        gameState.undo();
        assertEquals(Position.of(2, 1), gameState.getPlayerPositionById(0));
        assertInstanceOf(Box.class, gameState.getEntity(Position.of(3, 1)));
        assertInstanceOf(Empty.class, gameState.getEntity(Position.of(4, 1)));

        assertEquals(232, gameState.getUndoQuota().orElse(null));
    }

}
