package uk.ac.bradford.dungeongame;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;
import uk.ac.bradford.dungeongame.Entity.EntityType;

/**
 * The GameEngine class is responsible for managing information about the game,
 * creating levels, the player and monsters, as well as updating information
 * when a key is pressed while the game is running.
 *
 * @author prtrundl
 */
public class GameEngine {

    /**
     * An enumeration type to represent different types of tiles that make up a
     * dungeon level. Each type has a corresponding image file that is used to
     * draw the right tile to the screen for each tile in a level. Floors are
     * open for monsters and the player to move into, walls should be
     * impassable, stairs allow the player to progress to the next level of the
     * dungeon, and chests can yield a reward when moved over.
     *
     * @param args
     */
    public static void main(String[] args) {

    }

    public enum TileType {
        WALL, FLOOR, CHEST, STAIRS, FIRE

    }

    /**
     * The width of the dungeon level, measured in tiles. Changing this may
     * cause the display to draw incorrectly, and as a minimum the size of the
     * GUI would need to be adjusted.
     */
    public static final int DUNGEON_WIDTH = 25;

    /**
     * The height of the dungeon level, measured in tiles. Changing this may
     * cause the display to draw incorrectly, and as a minimum the size of the
     * GUI would need to be adjusted.
     */
    public static final int DUNGEON_HEIGHT = 18;

    /**
     * The maximum number of monsters that can be generated on a single level of
     * the dungeon. This attribute can be used to fix the size of an array (or
     * similar) that will store monsters.
     */
    public static final int MAX_MONSTERS = 40;

    /**
     * The chance of a wall being generated instead of a floor when generating
     * the level. 1.0 is 100% chance, 0.0 is 0% chance.
     */
    public static final double WALL_CHANCE = 0.05;

    /**
     * A random number generator that can be used to include randomised choices
     * in the creation of levels, in choosing places to spawn the player and
     * monsters, and to randomise movement and damage. This currently uses a
     * seed value of 123 to generate random numbers - this helps you find bugs
     * by giving you the same numbers each time you run the program. Remove the
     * seed value if you want different results each game.
     */
    private Random rng = new Random();

    /**
     * The current level number for the dungeon. As the player moves down stairs
     * the level number should be increased and can be used to increase the
     * difficulty e.g. by creating additional monsters with more health.
     */
    private int depth = 1;  //current dunegeon level

    /**
     * The GUI associated with a GameEngine object. THis link allows the engine
     * to pass level (tiles) and entity information to the GUI to be drawn.
     */
    private GameGUI gui;

    /**
     * The 2 dimensional array of tiles the represent the current dungeon level.
     * The size of this array should use the DUNGEON_HEIGHT and DUNGEON_WIDTH
     * attributes when it is created.
     */
    private TileType[][] tiles;

    /**
     * An ArrayList of Point objects used to create and track possible locations
     * to spawn the player and monsters.
     */
    private ArrayList<Point> spawns;

    /**
     * An Entity object that is the current player. This object stores the state
     * information for the player, including health and the current position
     * (which is a pair of co-ordinates that corresponds to a tile in the
     * current level)
     */
    private Entity player;

    /**
     * An array of Entity objects that represents the monsters in the current
     * level of the dungeon. Elements in this array should be of the type
     * Entity, meaning that a monster is alive and needs to be drawn or moved,
     * or should be null which means nothing is drawn or processed for movement.
     * Null values in this array are skipped during drawing and movement
     * processing. Monsters (Entity objects) that die due to player attacks can
     * be replaced with the value null in this array which removes them from the
     * game.
     */
    private Entity[] monsters;

    /**
     * Constructor that creates a GameEngine object and connects it with a
     * GameGUI object.
     *
     * @param gui The GameGUI object that this engine will pass information to
     * in order to draw levels and entities to the screen.
     */
    public GameEngine(GameGUI gui) {
        this.gui = gui;
        startGame();
    }

    /**
     * Generates a new dungeon level. The method builds a 2D array of TileType
     * values that will be used to draw tiles to the screen and to add a variety
     * of elements into each level. Tiles can be floors, walls, stairs (to
     * progress to the next level of the dungeon) or chests. The method should
     * contain the implementation of an algorithm to create an interesting and
     * varied level each time it is called.
     *
     * @return A 2D array of TileTypes representing the tiles in the current
     * level of the dungeon. The size of this array should use the width and
     * height of the dungeon.
     */
    private TileType[][] generateLevel() {
        //Add your code here to build a 2D array containing TileType values to create a level
       
        tiles = new TileType[DUNGEON_WIDTH][DUNGEON_HEIGHT];
        
        for (int t = 0; t < DUNGEON_WIDTH; t++) {
         
            for (int f = 0; f < DUNGEON_HEIGHT; f++) {
                tiles[t][f]= TileType.FLOOR;  
                int test = rng.nextInt(100);              
              
                if (test <= (WALL_CHANCE * 100)) {
                    tiles[t][f] = TileType.WALL;
                }
                if (test <= (WALL_CHANCE)) {
                    tiles[t][f] = TileType.FIRE;
                }
            }
        }
        
        int randomX = rng.nextInt(25);
        int randomY = rng.nextInt(18);
        if (depth < 11) {
            for (int i = 0; i < 11 - depth; i++) {
                tiles [randomX] [randomY] = TileType.STAIRS;
                randomX = rng.nextInt(25);
                randomY = rng.nextInt(18);
            }
        } else {
            tiles [randomX] [randomY] = TileType.STAIRS;
        }
        return tiles;  //return the 2D array
    }

    /**
     * Generates spawn points for the player and monsters. The method processes
     * the tiles array and finds tiles that are suitable for spawning, i.e.
     * tiles that are not walls or stairs. Suitable tiles should be added to the
     * ArrayList that will contain Point objects - Points are a simple kind of
     * object that contain an X and a Y co-ordinate stored using the int
     * primitive type and are part of the Java language (search for the Point
     * API documentation and examples of their use)
     *
     * @return An ArrayList containing Point objects representing suitable X and
     * Y co-ordinates in the current level that the player or monsters can be
     * spawned in
     */
    private ArrayList<Point> getSpawns() {
        ArrayList<Point> s = new ArrayList<Point>();
        
        //Add code here to find tiles in the level array that are suitable spawn points
        //Add these points to the ArrayList s
        
        for (int i = 0; i < DUNGEON_WIDTH; i++) {
            for (int j = 0; j < DUNGEON_HEIGHT; j++) {
                if (tiles [i] [j] == TileType.FLOOR) {
                    s.add(new Point(i, j));
                }
            }
        }
        int i = 0;
        for (Point p = s.get(i); i < s.size(); i++) {
            if (p.x != 0 && p.x != DUNGEON_WIDTH && p.y != 0 && p.y != DUNGEON_HEIGHT) {
                if (tiles [p.x -1] [p.y] != TileType.FLOOR) {
                   s.remove(p);
                }
                if (tiles [p.x +1] [p.y] != TileType.FLOOR) {
                    s.remove(p);
                }
                if (tiles [p.x] [p.y -1] != TileType.FLOOR) {
                 s.remove(p);
                }
                if (tiles [p.x] [p.y +1] != TileType.FLOOR) {
                    s.remove(p);
                }
                if (tiles [p.x -1] [p.y +1] != TileType.FLOOR) {
                    s.remove(p);
                }
                if (tiles [p.x -1] [p.y -1] != TileType.FLOOR) {
                    s.remove(p);
                }
                if (tiles [p.x +1] [p.y +1] != TileType.FLOOR) {
                    s.remove(p);
                }
                if (tiles [p.x +1] [p.y -1] != TileType.FLOOR) {
                    s.remove(p);
                } 
            }
         }
             
        return s;
    }
    
    /**
     * Spawns monsters in suitable locations in the current level. The method
     * uses the spawns ArrayList to pick suitable positions to add monsters,
     * removing these positions from the spawns ArrayList as they are used
     * (using the remove() method) to avoid multiple monsters spawning in the
     * same location. The method creates monsters by instantiating the Entity
     * class, setting health, and setting the X and Y position for the monster
     * using the X and Y values in the Point object removed from the spawns
     * ArrayList.
     *
     * @return A array of Entity objects representing the monsters for the
     * current level of the dungeon
     */
    private Entity[] spawnMonsters() {
        Entity monstersArray[];
        monstersArray = new Entity[5];
        
        for (int i = 0; i < 5; i++) {
            Point points = spawns.get(rng.nextInt(spawns.size()));
            Entity monster;
            
            int test = rng.nextInt(100);
            
            if (test < 40) {
                monster = new Entity (15, points.x, points.y, Entity.EntityType.SPECIAL_MONSTER);
                monstersArray[i] = monster;
                spawns.remove(points);
                continue;
            }
            
            monster = new Entity(5, points.x, points.y , Entity.EntityType.MONSTER);
            monstersArray[i] = monster;
            spawns.remove(points);
        }
        
        return monstersArray;    //Should be changed to return an array of monsters instead of null
    }

    /**
     * Spawns a player entity in the game. The method uses the spawns ArrayList
     * to select a suitable location to spawn the player and removes the Point
     * from the spawns ArrayList. The method instantiates the Entity class and
     * assigns values for the health, position and type of Entity.
     *
     * @return An Entity object representing the player in the game
     */
    private Entity spawnPlayer() {
        Point points = spawns.get(rng.nextInt(spawns.size()));
        Entity character = new Entity(20, points.x, points.y , Entity.EntityType.PLAYER);
        spawns.remove(points);
        return character; //Should be changed to return an Entity (the player) instead of null
    }

    /**
     * Handles the movement of the player when attempting to move left in the
     * game. This method is called by the DungeonInputHandler class when the
     * user has pressed the left arrow key on the keyboard. The method checks
     * whether the tile to the left of the player is empty for movement and if
     * it is updates the player object's X and Y locations with the new
     * position. If the tile to the left of the player is not empty the method
     * will not update the player position, but may make other changes to the
     * game, such as damaging a monster in the tile to the left, or breaking a
     * wall etc.
     */
    public void movePlayerLeft() {
        Point from;
        from = new Point(player.getX(), player.getY());
        Point to;
        to = new Point(player.getX() - 1, player.getY());

        if (from.x > 0) {
            if (tiles [to.x] [to.y] == TileType.FLOOR || tiles [to.x] [to.y] == TileType.STAIRS) {
                if (!spawns.contains(to)) {
                    for (Entity monster : monsters) {
                        if (monster == null) continue;
                        Point monsterPos = new Point(monster.getX(), monster.getY());
                        if (to.x == monsterPos.x) {
                            if (to.y == monsterPos.y) {
                                hitMonster(monster);
                                return;
                            }
                        }
                    }                               
                }
                
                spawns.add(from);
                player.setPosition(to.x, to.y);
                spawns.remove(to);
            } else if (tiles [to.x] [to.y] == TileType.FIRE) {
                hitPlayer();
            }
        }    
    }

    /**
     * Handles the movement of the player when attempting to move right in the
     * game. This method is called by the DungeonInputHandler class when the
     * user has pressed the right arrow key on the keyboard. The method checks
     * whether the tile to the right of the player is empty for movement and if
     * it is updates the player object's X and Y locations with the new
     * position. If the tile to the right of the player is not empty the method
     * will not update the player position, but may make other changes to the
     * game, such as damaging a monster in the tile to the right, or breaking a
     * wall etc.
     */
    public void movePlayerRight() {
        Point from;
        from = new Point(player.getX(), player.getY());
        Point to;
        to = new Point(player.getX() + 1, player.getY());

        if (from.x < DUNGEON_WIDTH -1) {
            if (tiles [to.x] [to.y] == TileType.FLOOR || tiles [to.x] [to.y] == TileType.STAIRS) {
                if (!spawns.contains(to)) {
                    for (Entity monster : monsters) {
                        if (monster == null) continue;
                        Point monsterPos = new Point(monster.getX(), monster.getY());
                        if (to.x == monsterPos.x) {
                            if (to.y == monsterPos.y) {
                                hitMonster(monster);
                                return;
                            }
                        }
                    }                               
                }                      
                
                spawns.add(from);
                player.setPosition(to.x, to.y);
                spawns.remove(to);
            } else if (tiles [to.x] [to.y] == TileType.FIRE) {
                hitPlayer();
            }
        }    
    }

    /**
     * Handles the movement of the player when attempting to move up in the
     * game. This method is called by the DungeonInputHandler class when the
     * user has pressed the up arrow key on the keyboard. The method checks
     * whether the tile above the player is empty for movement and if it is
     * updates the player object's X and Y locations with the new position. If
     * the tile above the player is not empty the method will not update the
     * player position, but may make other changes to the game, such as damaging
     * a monster in the tile above the player, or breaking a wall etc.
     */
    public void movePlayerUp() {
        Point from;
        from = new Point(player.getX(), player.getY());
        Point to;
        to = new Point(player.getX(), player.getY() - 1);

        if (from.y > 0) {
            if (tiles [to.x] [to.y] == TileType.FLOOR || tiles [to.x] [to.y] == TileType.STAIRS) {
                if (!spawns.contains(to)) {
                    for (Entity monster : monsters) {
                        if (monster == null) continue;
                        Point monsterPos = new Point(monster.getX(), monster.getY());
                        if (to.x == monsterPos.x) {
                            if (to.y == monsterPos.y) {
                                hitMonster(monster);
                                return;
                            }
                        }
                    }                               
                }                      
                
                spawns.add(from);
                player.setPosition(to.x, to.y);
                spawns.remove(to);
            } else if (tiles [to.x] [to.y] == TileType.FIRE) {
                hitPlayer();
            }
        }    
    }

    /**
     * Handles the movement of the player when attempting to move right in the
     * game. This method is called by the DungeonInputHandler class when the
     * user has pressed the down arrow key on the keyboard. The method checks
     * whether the tile below the player is empty for movement and if it is
     * updates the player object's X and Y locations with the new position. If
     * the tile below the player is not empty the method will not update the
     * player position, but may make other changes to the game, such as damaging
     * a monster in the tile below the player, or breaking a wall etc.
     */
    public void movePlayerDown() {
        Point from;
        from = new Point(player.getX(), player.getY());
        Point to;
        to = new Point(player.getX(), player.getY() + 1);
        
        if (from.y < DUNGEON_HEIGHT -1) {
            if (tiles [to.x] [to.y] == TileType.FLOOR || tiles [to.x] [to.y] == TileType.STAIRS) {
                if (!spawns.contains(to)) {
                    for (Entity monster : monsters) {
                        if (monster == null) continue;
                        Point monsterPos = new Point(monster.getX(), monster.getY());
                        if (to.x == monsterPos.x) {
                            if (to.y == monsterPos.y) {
                                hitMonster(monster);
                                return;
                            }
                        }
                    }                               
                }                     
                
                spawns.add(from);
                player.setPosition(to.x, to.y);
                spawns.remove(to);
            } else if (tiles [to.x] [to.y] == TileType.FIRE) {
                hitPlayer();
            }
        }    
    }

    /**
     * Reduces a monster's health in response to the player attempting to move
     * into the same square as the monster (attacking the monster).
     *
     * @param m The Entity which is the monster that the player is attacking
     */
    private void hitMonster(Entity m) {
        m.changeHealth(-1);
    }

    /**
     * Moves all monsters on the current level. The method processes all
     * non-null elements in the monsters array and calls the moveMonster method
     * for each one.
     */
    private void moveMonsters() {
        for (Entity m : monsters) {
            if (m != null) {
                int move = rng.nextInt(100);
                if (move < 60 && m.getType() == Entity.EntityType.MONSTER) continue;
                moveMonster(m);
            }
        }
    }

    /**
     * Moves a specific monster in the game. The method updates the X and Y
     * attributes of the monster Entity to reflect its new position.
     *
     * @param m The Entity (monster) that needs to be moved
     */
    private Point choice (Entity m) {
        Point from = new Point(m.getX(), m.getY());
        Point target = new Point(player.getX(), player.getY());
        Point up = new Point(from.x, from.y -1);        
        Point down = new Point(from.x, from.y +1);        
        Point left = new Point(from.x -1, from.y);
        Point right = new Point(from.x +1, from.y);       
        Point neighbors [] = {left, right, up, down};
        int fCost [];
        fCost = new int[4];
        int move [];
        move = new int [2];
        move[0] = DUNGEON_WIDTH * DUNGEON_HEIGHT + 1;
        
        for (int i = 0; i < neighbors.length; i++) {
            int dx;
            int dy;
            int h;
            int x = 10;
            
            dx = Math.abs(neighbors[i].x - target.x) * 10;
            dy = Math.abs(neighbors[i].y - target.y) * 10;
            h = dx + dy;
            fCost[i] = h + x;
        }
        
        for (int i = 0; i < fCost.length; i++) {
            if (fCost[i] < move[0]) {
                if (neighbors[i].x < 0 || neighbors[i].x > DUNGEON_WIDTH) continue;
                if (neighbors[i].y < 0 || neighbors[i].y > DUNGEON_HEIGHT) continue;       
                if (tiles [neighbors[i].x] [neighbors[i].y] == TileType.WALL || tiles [neighbors[i].x] [neighbors[i].y] == TileType.STAIRS) continue;         
                move[0] = fCost[i];
            }
        }
        
        for (int j = 0; j < fCost.length; j++) {
            if (fCost[j] == move[0]) {
                move[1] = j;
            }
        }

        Point choice = neighbors[move[1]];
        return choice;
    }
    
    private void moveMonster(Entity m) {
        Point from = new Point(m.getX(), m.getY());
        Point target = new Point(player.getX(), player.getY());
        Point to = choice(m);
        
        if (!spawns.contains(to)) {
            if (to.x == target.x && to.y == target.y) {
                hitPlayer();
            } else if (tiles [to.x] [to.y] == TileType.FIRE) {
                m.changeHealth(2);
            } 
        } else {
            m.setPosition(to.x, to.y);
            spawns.add(from);
            spawns.remove(to);
        }      
        
    }

    /**
     * Reduces the health of the player when hit by a monster - a monster next
     * to the player can attack it instead of moving and should call this method
     * to reduce the player's health
     */
    private void hitPlayer() {
        player.changeHealth(-2);
    }

    /**
     * Processes the monsters array to find any Entity in the array with 0 or
     * less health. Any Entity in the array with 0 or less health should be set
     * to null; when drawing or moving monsters the null elements in the
     * monsters array are skipped.
     */
    private void cleanDeadMonsters() {
        for (int m = 0; m < monsters.length; m++) {
            if (monsters[m] == null) continue;
            if (monsters[m].getHealth() < 1) {
                monsters[m] = null;
            }
        }
    }

    /**
     * Called in response to the player moving into a Stair tile in the game.
     * The method increases the dungeon depth, generates a new level by calling
     * the generateLevel method, fills the spawns ArrayList with suitable spawn
     * locations and spawns monsters. Finally it places the player in the new
     * level by calling the placePlayer() method. Note that a new player object
     * should not be created here unless the health of the player should be
     * reset.
     */
    private void descendLevel() {
        depth += 1;
        tiles = generateLevel();
        spawns = getSpawns();
        monsters = spawnMonsters();
        player = player;
        placePlayer();
    }
    /**
     * Places the player in a dungeon level by choosing a spawn location from
     * the spawns ArrayList, removing the spawn position as it is used. The
     * method sets the players position in the level by calling its setPosition
     * method with the x and y values of the Point taken from the spawns
     * ArrayList.
     */
    private void placePlayer() {
        Point point = spawns.get(rng.nextInt(spawns.size()));
        spawns.remove(point);
        player.setPosition(point.x,point.y);
    }

    /**
     * Performs a single turn of the game when the user presses a key on the
     * keyboard. The method cleans dead monsters, moves any monsters still alive
     * and then checks if the player is dead, exiting the game or resetting it
     * after an appropriate output to the user is given. It checks if the player
     * moved into a stair tile and calls the descendLevel method if it does.
     * Finally it requests the GUI to redraw the game level by passing it the
     * tiles, player and monsters for the current level.
     */
    public void doTurn() {
        cleanDeadMonsters();
        moveMonsters();
        if (player != null) {       //checks a player object exists
            if (player.getHealth() < 1) {
                System.exit(0);     //exits the game when player is dead
            }
            if (tiles[player.getX()][player.getY()] == TileType.STAIRS) {
                descendLevel();     //moves to next level if the player is on Stairs
            }
        }
        gui.updateDisplay(tiles, player, monsters);     //updates GUI
    }

    /**
     * Starts a game. This method generates a level, finds spawn positions in
     * the level, spawns monsters and the player and then requests the GUI to
     * update the level on screen using the information on tiles, player and
     * monsters.
     */
    public void startGame() {
        tiles = generateLevel();
        spawns = getSpawns();
        monsters = spawnMonsters();
        player = spawnPlayer();
        gui.updateDisplay(tiles, player, monsters);
    }
}