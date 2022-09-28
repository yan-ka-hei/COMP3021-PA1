package hk.ust.comp3021;

import hk.ust.comp3021.utils.TestKind;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SokobanGameFactoryTest {

    @TempDir
    private Path tempDir;

    @Tag(TestKind.PUBLIC)
    @Test
    void testLoadFromFile() throws IOException {
        final var mapFile = tempDir.resolve("tempMap.map");
        String rectangularMap = """
            233
            ######
            #A..@#
            #...@#
            #....#
            #.a..#
            #..a.#
            ######
            """;
        Files.writeString(mapFile, rectangularMap);
        final var gameMap = SokobanGameFactory.loadGameMap(mapFile);
        assertEquals(6, gameMap.getMaxWidth());
        assertEquals(7, gameMap.getMaxHeight());
        assertEquals(233, gameMap.getUndoLimit().orElse(null));
    }

    @Tag(TestKind.PUBLIC)
    @Test
    void testReadFromFile() throws IOException {
        final var mapFile = Path.of("G:\\My Drive\\year_3_fall\\comp3021\\pa1\\COMP3021-F22-PA-Student-Version\\src\\main\\resources\\map00.map");

        final var gameMap = SokobanGameFactory.loadGameMap(mapFile);
        assertEquals(6, gameMap.getMaxWidth());
        assertEquals(7, gameMap.getMaxHeight());
        assertEquals(233, gameMap.getUndoLimit().orElse(null));
    }


}
