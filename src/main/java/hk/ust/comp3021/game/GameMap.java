package hk.ust.comp3021.game;

import hk.ust.comp3021.entities.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;

import java.lang.IllegalArgumentException;


/**
 * A Sokoban game board.
 * GameBoard consists of information loaded from map data, such as
 * <li>Width and height of the game map</li>
 * <li>Walls in the map</li>
 * <li>Box destinations</li>
 * <li>Initial locations of boxes and player</li>
 * <p/>
 * GameBoard is capable to create many GameState instances, each representing an ongoing game.
 */
public class GameMap {

    /**
     * Create a new GameMap with width, height, set of box destinations and undo limit.
     *
     * @param maxWidth     Width of the game map.
     * @param maxHeight    Height of the game map.
     * @param destinations Set of box destination positions.
     * @param undoLimit    Undo limit.
     *                     Positive numbers specify the maximum number of undo actions.
     *                     0 means undo is not allowed.
     *                     -1 means unlimited. Other negative numbers are not allowed.
     */
    protected int maxWidth;
    protected int maxHeight;
    protected HashSet<Position> destinations;
    protected int undoLimit;

    private ArrayList<Position> walls;

    private ArrayList<Integer> init_player;

    private ArrayList<Position> init_player_loc;

    private ArrayList<Integer> init_box;

    private ArrayList<Position> init_box_loc;

    private ArrayList<Position> effective_loc;

    public GameMap(int maxWidth, int maxHeight, Set<Position> destinations, int undoLimit) {
        //TODO
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.destinations = new HashSet<>();
        this.destinations.addAll(destinations);
        this.undoLimit = undoLimit;
    }

    /**
     * Parses the map from a string representation.
     * The first line is undo limit.
     * Starting from the second line, the game map is represented as follows,
     * <li># represents a {@link Wall}</li>
     * <li>@ represents a box destination.</li>
     * <li>Any upper-case letter represents a {@link Player}.</li>
     * <li>
     * Any lower-case letter represents a {@link Box} that is only movable by the player with the corresponding upper-case letter.
     * For instance, box "a" can only be moved by player "A" and not movable by player "B".
     * </li>
     * <li>. represents an {@link Empty} position in the map, meaning there is no player or box currently at this position.</li>
     * <p>
     * Notes:
     * <li>
     * There can be at most 26 players.
     * All implementations of classes in the hk.ust.comp3021.game package should support up to 26 players.
     * </li>
     * <li>
     * For simplicity, we assume the given map is bounded with a closed boundary.
     * There is no need to check this point.
     * </li>
     * <li>
     * Example maps can be found in "src/main/resources".
     * </li>
     *
     * @param mapText The string representation.
     * @return The parsed GameMap object.
     * @throws IllegalArgumentException if undo limit is negative but not -1.
     * @throws IllegalArgumentException if there are multiple same upper-case letters, i.e., one player can only exist at one position.
     * @throws IllegalArgumentException if there are no players in the map.
     * @throws IllegalArgumentException if the number of boxes is not equal to the number of box destinations.
     * @throws IllegalArgumentException if there are boxes whose {@link Box#getPlayerId()} do not match any player on the game board,
     *                                  or if there are players that have no corresponding boxes.
     */
    public static GameMap parse(String mapText) {
        //TODO
        String[] extract = mapText.split("\n", 2);
        StringBuilder number = new StringBuilder();
        for (char num:extract[0].toCharArray()){
            if (num >= '0' && num <= '9' || num == '-'){
                number.append(num);
            }
        }
        int limit = Integer.parseInt(number.toString());
        String map = extract[1];
        if (limit < -1){
            throw new IllegalArgumentException("invalid undo limit");
        }
        ArrayList<Character> check = new ArrayList<>();
        int box_num = 0;
        int box_dest_num = 0;
        String[] lines = map.split("\n");
        int height = lines.length;
        HashSet<Position> dest_set = new HashSet<>();
        ArrayList<Position> walls_set = new ArrayList<>();
        ArrayList<Integer> init_player_set = new ArrayList<>();
        ArrayList<Position> init_player_loc_set= new ArrayList<>();
        ArrayList<Integer> init_box_set = new ArrayList<>();
        ArrayList<Position> init_box_loc_set = new ArrayList<>();
        ArrayList<Position> init_effective_loc = new ArrayList<>();

        int width = -1;

        for (int k = 0; k < height; k++) {
            char[] line = lines[k].toCharArray();
            boolean start = false;
            int counter = 0;
            for (int i = 0; i < line.length; i++) {
                if (line[i]!=' '){
                    start = true;
                }
                if (line[i]==' ') {
                    boolean substring_all_spaces = true;
                    for (int j = i; j <line.length; j++){
                        if (line[j] != ' ') {
                            substring_all_spaces = false;
                            break;
                        }
                    }
                    if (!substring_all_spaces){
                        start =false;
                    }
                }
                if (start){
                    counter++;
                }
                if (line[i] >= 'A' && line[i] <= 'Z' && !check.contains(line[i])) {
                    init_player_set.add(Character.getNumericValue(line[i])-Character.getNumericValue('A'));
                    init_player_loc_set.add(Position.of(i, k));
                    check.add(line[i]);
                    init_effective_loc.add(Position.of(i,k));
                } else if (line[i] >= 'A' && line[i] <= 'Z' && check.contains(line[i])) {
                    throw new IllegalArgumentException("duplicate players detected in the map");
                }
                if (line[i] == '@') {
                    box_dest_num++;
                    dest_set.add(Position.of(i, k));
                    init_effective_loc.add(Position.of(i,k));
                }
                if (line[i] >= 'a' && line[i] <= 'z') {
                    box_num++;
                    init_box_set.add(Character.getNumericValue(line[i])-Character.getNumericValue('a'));
                    init_box_loc_set.add(Position.of(i, k));
                    init_effective_loc.add(Position.of(i,k));
                }

                if (line[i] == '#') {
                    walls_set.add(Position.of(i, k));
                }
                if (line[i] == '.'){
                    init_effective_loc.add(Position.of(i,k));
                }
            }
            if (counter > width){
                width = counter;
            }
        }
        for (String s : lines) {
            char[] line = s.toCharArray();
            for (char c : line) {
                if (c >= 'a' && c <= 'z' && !check.contains(Character.toUpperCase(c))) {
                    throw new IllegalArgumentException("unmatched players");
                }
            }
        }
        if (box_num!=box_dest_num){
            throw new IllegalArgumentException("mismatch destinations");
        }
        if (check.size()==0){
            throw new IllegalArgumentException("no player");
        }
        for (char i: check){
            if (!mapText.contains(String.valueOf(Character.toLowerCase(i)))){
                throw new IllegalArgumentException("unmatched players");
            }
        }

        for (int i = 0;i<init_player_set.size()-1;i++){
            for (int k = i+1; k < init_player_set.size();k++){
                if (init_player_set.get(i)>init_player_set.get(k)){
                    int temp_player = init_player_set.get(i);
                    init_player_set.set(i, init_player_set.get(k));
                    init_player_set.set(k, temp_player);
                    Position temp_loc = init_player_loc_set.get(i);
                    init_player_loc_set.set(i, init_player_loc_set.get(k));
                    init_player_loc_set.set(k, temp_loc);
                }
            }
        }

        GameMap game_map = new GameMap(width, height, dest_set, limit);
        game_map.init_player_loc = init_player_loc_set;
        game_map.init_player = init_player_set;
        game_map.init_box = init_box_set;
        game_map.init_box_loc = init_box_loc_set;
        game_map.walls = walls_set;
        game_map.effective_loc = init_effective_loc;

        return game_map;
    }

    /**
     * Get the entity object at the given position.
     *
     * @param position the position of the entity in the game map.
     * @return Entity object.
     */
    @Nullable
    public Entity getEntity(Position position) {
        if (init_box_loc.contains(position)){
            return new Box(init_box.get(init_box_loc.indexOf(position)));
        }
        else if (init_player_loc.contains(position)){
            return new Player(init_player.get(init_player_loc.indexOf(position)));
        }
        else if (walls.contains(position)){
            return new Wall();
        }
        else if(effective_loc.contains(position)){
            return new Empty();
        }
        return null;
    }

    /**
     * Put one entity at the given position in the game map.
     *
     * @param position the position in the game map to put the entity.
     * @param entity   the entity to put into game map.
     */
    public void putEntity(Position position, Entity entity) {
        //TODO
        switch (entity){
            case Box o -> {
                init_box.add(o.getPlayerId());
                init_player_loc.add(position);
            }
            case Player p -> {
                init_player.add(p.getId());
                init_player_loc.add(position);
            }
            case Wall ignored -> walls.add(position);
            default ->{}
            }
    }

    /**
     * Get all box destination positions as a set in the game map.
     *
     * @return a set of positions.
     */
    public @NotNull @Unmodifiable Set<Position> getDestinations() {
        //
        return destinations;
    }

    /**
     * Get the undo limit of the game map.
     *
     * @return undo limit.
     */
    public Optional<Integer> getUndoLimit() {
        //TODO
        return Optional.of(undoLimit);
    }

    /**
     * Get all players' id as a set.
     *
     * @return a set of player id.
     */
    public Set<Integer> getPlayerIds() {
        //TODO
        HashSet<Integer> player = new HashSet<>();
        player.addAll(init_player);
        return player;
    }

    /**
     * Get the maximum width of the game map.
     *
     * @return maximum width.
     */
    public int getMaxWidth() {
        //TODO
        return maxWidth;
    }

    /**
     * Get the maximum height of the game map.
     *
     * @return maximum height.
     */
    public int getMaxHeight() {
        //TODO
        return maxHeight;
    }

    public ArrayList<Integer> getInit_box() {
        return init_box;
    }

    public ArrayList<Position> getInit_box_loc() {
        return init_box_loc;
    }

    public ArrayList<Integer> getInit_player() {
        return init_player;
    }

    public ArrayList<Position> getInit_player_loc() {
        return init_player_loc;
    }

    public ArrayList<Position> getEffective_loc() {
        return effective_loc;
    }
}
