package core;

import entity.accessory.*;
import entity.enemy.Enemy;
import entity.item.*;
import entity.character.Character;
import entity.item.craftable.MalevolentKitchen;
import entity.item.craftable.UnlimitedHollowPurple;
import entity.misc.Chest;
import entity.misc.ExpOrb;
import entity.weapon.*;
import entity.weapon.evolvedWeapon.*;
import entityInterface.itemInterface.Craftable;
import entityInterface.itemInterface.Droppable;
import entityInterface.GameObject;
import entityInterface.Updatable;
import entityInterface.itemInterface.Unique;
import entityInterface.itemInterface.Usable;
import entityInterface.weaponInterface.Evolvable;
import entityInterface.weaponInterface.Unitable;
import javafx.geometry.BoundingBox;
import javafx.scene.image.Image;
import tile.TileManager;
import utils.CollisionChecker;
import utils.InputManager;
import utils.SceneManager;
import utils.SoundManager;
import vfx.VFXManager;

import java.util.*;

/**
 * Central controller for a single gameplay run.
 *
 * <p>{@code GameManager} acts as the game's Model in an MVC-like structure.
 * It owns all runtime state — the player character, live enemies, equipped
 * weapons and accessories, the backpack, exp orbs, chests, and the game timer.
 * Every frame the JavaFX {@link javafx.animation.AnimationTimer} in
 * {@link gui.GameCanvas} calls {@link #update(double)} to advance the
 * simulation; after {@code update()} the canvas calls the various render
 * methods to draw the current frame.
 *
 * <h2>Initialisation flow</h2>
 * <ol>
 *   <li>Constructor: build all master lists (characters, weapons, accessories,
 *       items), create the spawner, load BGM.</li>
 *   <li>{@link #startGame()}: copy the chosen character into the active slot,
 *       load the tile map, place starter weapons, transition to
 *       {@link GameState#PLAYING}.</li>
 *   <li>{@link #update(double)}: called every frame while state is
 *       {@link GameState#PLAYING}.</li>
 *   <li>{@link #resetGame()}: tear down runtime state and return to the
 *       main menu.</li>
 * </ol>
 *
 * <h2>Coordinate system</h2>
 * <p>The world is 7680 × 5760 pixels (80 × 60 tiles of 96 × 96 px each).
 * The screen viewport is 1920 × 1080 px, centred on the character.
 * Conversion helpers {@link #getScreenX(double)} and
 * {@link #getScreenY(double)} translate world coordinates to screen pixels.
 */
public class GameManager {

    /** Random source used for drop / chest / level-up selection. */
    private Random random = new Random();

    /** Current phase of the game; drives update logic and UI visibility. */
    private GameState currentState;

    /** Keyboard state; injected from the JavaFX scene and read every frame. */
    private InputManager inputManager;

    /** Unused list — kept for future multi-character support. */
    private ArrayList<Character> characterList;

    /** The currently active player-controlled character. */
    private Character character;

    /** Total seconds elapsed since the run started. */
    private double gameTimer;

    /** Current player level (incremented when {@link #currentExperience} hits the threshold). */
    private int level;

    /** Experience points accumulated toward the next level-up. */
    private int currentExperience;

    /** Experience required to reach the next level; scales with {@link #level}. */
    private int ExperienceForNextLevel;

    /** Enemies currently active in the world (alive or just killed this frame). */
    private ArrayList<Enemy> inGameEnemyList;

    /** Unused — reserved for future enemy master list. */
    private ArrayList<Enemy> AllEnemyList;

    /** Master list of items that can be crafted from materials. */
    private ArrayList<Item> allCraftableItemList;

    /** Master list of items that enemies can drop. */
    private ArrayList<Item> allDroppableItemList;

    /** 36-slot player backpack; {@code null} entries are empty slots. */
    private Item[] backpack;

    /** Items that have been dropped on the map and are awaiting pickup. */
    private ArrayList<Item> droppedItemList;

    /** Consumable items currently executing their timed effect (e.g. Soda buff). */
    private ArrayList<Item> usingItemList;

    /** Master list of exp-orb templates (currently just one type). */
    private ArrayList<ExpOrb> allExpOrbList;

    /** Exp orbs currently present on the map. */
    private ArrayList<ExpOrb> droppedExpOrbList;

    /**
     * Projectile / effect instances currently travelling through the world.
     * Populated by {@link entity.weapon.Weapon#use(double)}; cleared when
     * {@link entity.weapon.Weapon#isExpired()} returns {@code true}.
     */
    private ArrayList<Weapon> usingWeaponList;

    /**
     * Pool of weapons not yet owned by the player; offered at level-up.
     * Entries are removed when selected and returned when the choice is skipped.
     */
    private ArrayList<Weapon> allWeaponList;

    /** Six equipped weapon slots; {@code null} entries are empty. */
    private Weapon[] weaponList;

    /** Pool of accessories not yet owned; offered at level-up. */
    private ArrayList<Accessory> allAccessoryList;

    /** Six equipped accessory slots; {@code null} entries are empty. */
    private Accessory[] accessoryList;

    /** Manages the tile map — loading, storing, and providing tile data. */
    private TileManager tileManager;

    /** Prototype chest used to spawn new chest instances when enemies die. */
    private Chest chest;

    /** Chests currently placed on the map (up to 5 at a time). */
    private ArrayList<Chest> existingChestList;

    /** All selectable playable characters. */
    private Character[] allCharacterList;

    /** Controls enemy spawn rate and positioning. */
    private EnemySpawner spawner;

    /**
     * The enemy closest to the player this frame.  Recomputed in
     * {@link #update(double)} and read by auto-targeting weapons like
     * {@link entity.weapon.Blue}.
     */
    private Enemy closestTarget;

    /**
     * Holds the three items offered at the level-up screen, keyed by
     * {@code "choice0"}, {@code "choice1"}, {@code "choice2"}.
     */
    private final HashMap<String, GameObject> levelUpChoice = new HashMap<>();

    /**
     * Holds the type string ({@code "weapon"}, {@code "accessory"}, or
     * {@code "upgrade"}) for each choice, keyed by {@code "choice0Type"} etc.
     */
    private final HashMap<String, String> levelUpChoiceType = new HashMap<>();

    /** {@code true} if at least one valid choice was generated for the level-up screen. */
    private boolean haveChoice;

    /** When {@code true}, the game loop automatically uses all usable backpack items. */
    private boolean autoUseItem;

    // --- In-game derived stats modified by accessories ----------------------

    /** Seconds the character has been dead (triggers game-over after 3 s). */
    private double timeSinceDead;

    /** Multiplier applied to all experience gains (boosted by Growth accessory). */
    private double xPMultiplier;

    /** Flat damage reduction applied to incoming hits (not yet used in damage formula). */
    private double armor;

    /** Multiplier on passive HP regeneration rate (not yet used). */
    private double hpRecoveryMultiplier;

    /** Luck value; multiplies chest / drop roll thresholds (not yet used). */
    private double luck;

    /** {@code true} once the SubaruShirt one-shot revival has triggered this run. */
    private boolean revived;

    /**
     * Constructs and fully initialises a {@code GameManager} ready for use.
     *
     * <p>Master lists for characters, weapons, accessories, items, and exp orbs
     * are populated here.  The {@link EnemySpawner} is created and the first
     * BGM track is loaded into the {@link utils.SoundManager}.
     *
     * <p>The game starts in {@link GameState#MAIN_MENU}; call
     * {@link #startGame()} after the player has selected a character to begin
     * a run.
     */
    public GameManager() {
        this.currentState = GameState.MAIN_MENU;
        this.inputManager = new InputManager();
        backpack = new Item[36];
        weaponList = new Weapon[6];
        accessoryList = new Accessory[6];
        usingItemList = new ArrayList<>();
        usingWeaponList = new ArrayList<>();
        //allWeaponList =  new ArrayList<>();
        inGameEnemyList = new ArrayList<>();
        allExpOrbList = new ArrayList<>();
        droppedExpOrbList = new ArrayList<>();
        droppedItemList = new ArrayList<>();
        //allDroppableItemList = new ArrayList<>();
        //allCraftableItemList = new ArrayList<>();

        allExpOrbList.add(new ExpOrb(1,"redorb",this));
        this.chest = new Chest(this);
        existingChestList = new ArrayList<>();

        initializeAllCharacter();
        initializeAllWeapon();
        initializedAllAccessory();
        initializedAllDroppableItem();
        initializedAllCraftableItem();

        xPMultiplier = 1.00;
        armor = 0;
        hpRecoveryMultiplier = 1.00;
        luck = 1.00;

        SoundManager.getInstance().loadMediaPlayer("BGM1");
        this.spawner = new EnemySpawner(this);
    }

    /**
     * Tears down all runtime state and returns the game to the main menu.
     *
     * <p>All lists are cleared, weapon / accessory / item slots are reset,
     * the character reference is nulled, and the scene is switched back to
     * {@link GameState#MAIN_MENU}.  Called when the player quits mid-run or
     * after the game-over screen.
     */
    public void resetGame() {
        backpack = new Item[36];
        weaponList = new Weapon[6];
        accessoryList = new Accessory[6];
        initializeAllWeapon();
        initializedAllAccessory();
        initializedAllDroppableItem();
        initializedAllCraftableItem();
        usingItemList.clear();
        usingWeaponList.clear();
        inGameEnemyList.clear();
        droppedExpOrbList.clear();
        droppedItemList.clear();
        existingChestList.clear();
        character = null;

        xPMultiplier = 1.00;
        armor = 0;
        hpRecoveryMultiplier = 1.00;
        luck = 1.00;

        spawner.resetEnemyInSpawner();
        VFXManager.clear();
        this.setCurrentState(GameState.MAIN_MENU);
        SceneManager.switchToMenu();
    }
    /**
     * Begins a new run with the currently selected character.
     *
     * <p>Sets the character's spawn position, starts the BGM, loads the tile
     * map into {@link CollisionChecker}, initialises experience and timers,
     * places starter weapons, and transitions to {@link GameState#PLAYING}.
     */
    public void startGame() {
        character.setMapX(1920*2);
        character.setMapY(1440*2);
        SoundManager.getInstance().startBGM("BGM1");
        tileManager.loadMap();
        CollisionChecker.setTile(tileManager.mapTilesNum,tileManager.tiles);
        this.level = 1;
        this.currentExperience = 0;
        this.ExperienceForNextLevel = 5;
        this.timeSinceDead=0;
        revived = false;

        //putItemInBackpack(new MalevolentKitchen(this));
        //putItemInBackpack(new UnlimitedHollowPurple(this));
        //putItemInBackpack(new Soda(this));

        SceneManager.updateBackPack(backpack);

        this.weaponList[0] = (Weapon) character.getStarterWeapon().copy();
        //this.weaponList[0] = new MaximumOutputInfinity(this);
        //this.weaponList[1] = new Lazer(this,new Standard(this));
        this.weaponList[2] = new Red(this);
        //this.weaponList[3] = new MaximumCleave(this);
        //this.weaponList[4] = new MaximumDismantle(this);
        //this.weaponList[5] = new Lazer(this,new Standard(this));
        /*SixEye se = new SixEye(this);
        se.upgrade();
        se.upgrade();
        se.upgrade();
        se.upgrade();
        se.upgrade();
        se.upgrade();
        se.upgrade();
        accessoryList[0]=se;*/

        allWeaponList.removeIf(weapon -> Objects.equals(weapon.getName(), weaponList[0].getName()));

        SceneManager.updateWeaponAndAccessoryPanel(weaponList,accessoryList);

        //spawner.addEnemyToSpawner(new Enemy("FingerBearer",200,50,true,true));
        spawner.addEnemyToSpawner(new Enemy("slime",200,50,true,true));


        this.setGameTimer(0);
        this.currentState = GameState.PLAYING;

    }

    /**
     * Main simulation step — called every frame by the game loop.
     *
     * <p>Only executes when the state is {@link GameState#PLAYING}.
     * Responsibilities in order:
     * <ol>
     *   <li>Advance game timer and call the enemy spawner.</li>
     *   <li>Update the character (movement, i-frames, passive regen) or
     *       advance the death timer.</li>
     *   <li>Check SubaruShirt one-shot revival; trigger game-over after 3 s.</li>
     *   <li>Update enemies: remove dead ones (possibly spawning exp/drops/chests),
     *       deal contact damage, find the closest target for auto-aim weapons.</li>
     *   <li>Proc all equipped accessories.</li>
     *   <li>Activate equipped weapons and auto-use backpack items.</li>
     *   <li>Tick active weapon effects; remove expired ones.</li>
     *   <li>Tick active item effects; remove expired ones.</li>
     *   <li>Process exp-orb collection and item pickup.</li>
     *   <li>Process chest pickup triggers.</li>
     *   <li>Resolve enemy-to-enemy push collisions (sweep-and-prune).</li>
     *   <li>Update VFX (ground effects, floating texts, screen effects).</li>
     *   <li>Check for level-up; if reached, pick random choices and show UI.</li>
     *   <li>Update the HUD (clock, level bar, weapon cooldown overlays).</li>
     * </ol>
     *
     * @param accumulateDeltaTime seconds elapsed since the previous frame
     */
    public void update(double accumulateDeltaTime) {

        if (currentState != GameState.PLAYING) return;

        this.gameTimer += accumulateDeltaTime;

        spawner.update(level,gameTimer,accumulateDeltaTime);




        if(!character.isDead()){
            character.update(inputManager, accumulateDeltaTime);
        }
        else {
            timeSinceDead+=accumulateDeltaTime;
        }
        if(timeSinceDead>1.5 && !revived){
            for (Item item : backpack){
                if(item instanceof SubaruShirt){
                    timeSinceDead=0;
                    revived = true;
                    character.heal(10000);
                    break;
                }
            }
        }
        if(timeSinceDead>3){

            currentState=GameState.DEATH;
            SceneManager.showQuit();
            return;
        }


        closestTarget = null;
        double closestDistance = Double.MAX_VALUE;
        double playerMapX = character.getMapX();
        double playerMapY = character.getMapY();

        Iterator<Enemy> inGameEnemyListIterator = inGameEnemyList.iterator();
        while (inGameEnemyListIterator.hasNext()) {
            Enemy enemy = inGameEnemyListIterator.next();
            if (enemy.isDead()) {
                if(Math.random()>0.999 && existingChestList.size()<5) existingChestList.add(new Chest(chest,enemy.getMapX(),enemy.getMapY()));
                if(Math.random()>0.2) droppedExpOrbList.add(new ExpOrb(allExpOrbList.getFirst(), enemy.getMapX(), enemy.getMapY()));
                if(Math.random()>0.995) {
                    Item randomItem = getRandomDroppableItem();
                    randomItem.setMapX(enemy.getMapX());
                    randomItem.setMapY(enemy.getMapY());
                    droppedItemList.add(randomItem);
                    //System.out.println("Dropped");
                }
                inGameEnemyListIterator.remove();
            }
            else{
                if(enemy.getHitbox().intersects(character.getHitbox())){
                    character.receiveDamage(100);
                }
                enemy.update(accumulateDeltaTime, character.getMapX(), character.getMapY());
                double dx = enemy.getMapX() - playerMapX;
                double dy = enemy.getMapY() - playerMapY;

                double distanceSq = (dx * dx) + (dy * dy);
                if (distanceSq < closestDistance) {
                    closestDistance = distanceSq;
                    closestTarget = enemy;
                }
            }
        }

        for (Accessory accessory : this.accessoryList){
            if (accessory!=null)accessory.procEffect();
        }

        if (!character.isDead()) {
            for (Weapon weapon : weaponList) {
                if (weapon != null) {
                    weapon.use(accumulateDeltaTime);
                }
            }
            if (autoUseItem){
                for(int i = 0;i<backpack.length;i++){
                    if(backpack[i]==null)continue;
                    if(backpack[i] instanceof Usable usable) usable.use(i);
                }
            }
        }
        Iterator<Weapon> usingWeaponListIterator = usingWeaponList.iterator();
        while (usingWeaponListIterator.hasNext()) {
            Weapon weapon =  usingWeaponListIterator.next();
            weapon.update(accumulateDeltaTime);
            if (weapon.isExpired()) {
                usingWeaponListIterator.remove();
            }
        }

        Iterator<Item> usingItemListIterator = usingItemList.iterator();
        while (usingItemListIterator.hasNext()) {
            Updatable item = (Updatable) usingItemListIterator.next();
                item.update(accumulateDeltaTime);
            if (item.isExpired()) {
                usingItemListIterator.remove();
            }
        }
        if(!character.isDead()) {
            Iterator<ExpOrb> droppedExpOrbListIterator = droppedExpOrbList.iterator();
            while (droppedExpOrbListIterator.hasNext()) {
                ExpOrb expOrb = droppedExpOrbListIterator.next();
                if (character.getHitbox().intersects(expOrb.getHitbox())) {
                    currentExperience += (int) (expOrb.getXpAmount()*xPMultiplier);
                    droppedExpOrbListIterator.remove();
                } else {
                    expOrb.update(accumulateDeltaTime);
                }
            }

            Iterator<Item> droppedItemListIterator = droppedItemList.iterator();
            while (droppedItemListIterator.hasNext()) {
                Droppable droppedItem = (Droppable) droppedItemListIterator.next();
                if (character.getHitbox().intersects(droppedItem.getItemHitBox())) {

                    putItemInBackpack((Item) droppedItem);
                    droppedItemListIterator.remove();

                /*boolean added = false;
                for(Item item : backpack){
                    if(item != null && ((Entity)droppedItem).getName().equals(item.getName())){
                        item.setAmount(item.getAmount()+1);
                        droppedItemListIterator.remove();
                        SceneManager.updateBackPack(backpack);
                        added = true;
                        break;
                    }
                }
                if(!added){
                    for(int i = 0 ; i < backpack.length ; i++ ){
                        if(backpack[i] == null){
                            backpack[i] = (Item) droppedItem;
                            droppedItemListIterator.remove();
                            SceneManager.updateBackPack(backpack);
                            break;
                        }
                    }
                }*/
                } else {
                    droppedItem.updateAsDroppedItem(accumulateDeltaTime);
                }
            }

            Iterator<Chest> existingChestListIterator = existingChestList.iterator();
            while (existingChestListIterator.hasNext()) {
                Chest chest = existingChestListIterator.next();
                if (character.getHitbox().intersects(chest.getHitbox())) {
                    currentState = GameState.CHEST;
                    //getRandomChestChoice();
                    SceneManager.updateChestChoice(getRandomChestChoice());
                    SceneManager.showChestChoice();
                    existingChestListIterator.remove();
                    break;
                }
            }
        }

        handleEnemyCollisions();

        VFXManager.updateGround(accumulateDeltaTime,character.getMapX(),character.getMapY());
        VFXManager.update(accumulateDeltaTime);
        VFXManager.updateScreenEffect(accumulateDeltaTime,character.getMapX(),character.getMapY());

        if(currentExperience >= ExperienceForNextLevel && currentState == GameState.PLAYING){
            currentExperience -= ExperienceForNextLevel;
            level += 1;
            //this.weaponList[0].setLevel(Integer.parseInt(weaponList[0].getLevel()) + 1);
            this.ExperienceForNextLevel = level*10;
            this.currentState = GameState.LEVEL_UP;
            getRandomLevelUpChoice();
            //SceneManager.updateLevelUpChoice();
            //SceneManager.showLevelUpChoice();
            if(haveChoice){
                SceneManager.updateLevelUpChoice();
                SceneManager.showLevelUpChoice();
            }
            //SceneManager.updateWeaponAndAccessoryPanel(weaponList,accessoryList);
        }

        SceneManager.updateClock(gameTimer,level);
        SceneManager.updateLevelBar(currentExperience,ExperienceForNextLevel);
        SceneManager.tickWeaponCooldowns(weaponList);
    }
    /**
     * Resolves overlapping enemies using a sweep-and-prune broad-phase followed
     * by per-pair AABB overlap resolution.
     *
     * <p>Enemies are sorted by X coordinate so the inner loop can break early
     * when the next enemy's left edge exceeds the current enemy's right edge.
     * Overlapping pairs are pushed apart along the axis of least penetration
     * with a soft smoothing factor (0.2) to avoid sudden "teleporting".
     */
    private void handleEnemyCollisions() {
        inGameEnemyList.sort(Comparator.comparingDouble(Enemy::getMapX));

        for (int i = 0; i < inGameEnemyList.size(); i++) {
            Enemy a = inGameEnemyList.get(i);
            BoundingBox boundsA = a.getHitbox();

            for (int j = i + 1; j < inGameEnemyList.size(); j++) {
                Enemy b = inGameEnemyList.get(j);
                
                // Sweep and prune: if b's left edge is beyond a's right edge, break early
                if (b.getMapX() - (b.getWidth() / 2.0) > a.getMapX() + (a.getWidth() / 2.0)) {
                    break;
                }

                BoundingBox boundsB = b.getHitbox();

                if (boundsA.intersects(boundsB)) {
                    // 1. Find the edges
                    double overlapX = Math.min(boundsA.getMaxX(), boundsB.getMaxX()) -
                            Math.max(boundsA.getMinX(), boundsB.getMinX());

                    double overlapY = Math.min(boundsA.getMaxY(), boundsB.getMaxY()) -
                            Math.max(boundsA.getMinY(), boundsB.getMinY());

                    // 2. Resolve along the SHORTEST axis (to prevent "teleporting")
                    double ax = a.getMapX();
                    double ay = a.getMapY();
                    double bx = b.getMapX();
                    double by = b.getMapY();

                    // Use a soft resolution factor instead of forcing them out instantly
                    double smoothingFactor = 0.2;

                    if (overlapX < overlapY) {
                        // Push horizontally
                        if (boundsA.getCenterX() < boundsB.getCenterX()) {
                            a.setMapX(ax - overlapX * smoothingFactor);
                            b.setMapX(bx + overlapX * smoothingFactor);
                        } else {
                            a.setMapX(ax + overlapX * smoothingFactor);
                            b.setMapX(bx - overlapX * smoothingFactor);
                        }
                    } else {
                        // Push vertically
                        if (boundsA.getCenterY() < boundsB.getCenterY()) {
                            a.setMapY(ay - overlapY * smoothingFactor);
                            b.setMapY(by + overlapY * smoothingFactor);
                        } else {
                            a.setMapY(ay + overlapY * smoothingFactor);
                            b.setMapY(by - overlapY * smoothingFactor);
                        }
                    }
                }


            }
        }
    }

    private Enemy findClosestEnemy(double playerMapX, double playerMapY) {
        Enemy closest = null;
        double closestDistance = Double.MAX_VALUE;

        for (Enemy e : inGameEnemyList) {
            double dx = e.getMapX() - playerMapX;
            double dy = e.getMapY() - playerMapY;

            double distanceSq = (dx * dx) + (dy * dy);

            if (distanceSq < closestDistance) {
                closestDistance = distanceSq;
                closest = e;
            }
        }
        return closest;
    }
    private void initializeAllCharacter(){
        allCharacterList = new Character[16];
        Character character1 = new Character( "sukuna", 300,  1000,256,new Cleave(this),utils.SpriteManager.loadImage("character/icon/sukunaIcon.png"),utils.SpriteManager.loadImage("character/sprite/sukuna.png"));
        allCharacterList[0] = character1;
        Character character2 = new Character( "gojo", 300,  1000,256,new Infinity(  this),utils.SpriteManager.loadImage("character/icon/gojoIcon.png"),utils.SpriteManager.loadImage("character/sprite/gojo.png"));
        allCharacterList[1] = character2;
    }

    private void initializeAllWeapon(){
        allWeaponList = new ArrayList<>();

        allWeaponList.add(new Dismantle(this));
        allWeaponList.add(new Cleave(this));
        allWeaponList.add(new Red(this));
        allWeaponList.add(new Blue(this));
        allWeaponList.add(new Infinity(this));
        allWeaponList.add(new Bible(this));
        allWeaponList.add(new Standard( this));
    }
    private void initializedAllAccessory(){
        allAccessoryList = new ArrayList<>();
        allAccessoryList.add(new SukunaArm(this));
        allAccessoryList.add(new SukunaCloak(this));
        allAccessoryList.add(new SixEye(this));
        allAccessoryList.add(new GojoGlasses(this));
        allAccessoryList.add(new Blindfold(this));
        allAccessoryList.add(new KeyPad(this));
        //allAccessoryList.add(new AcolyteHat(this));
    }
    private void initializedAllDroppableItem(){
        allDroppableItemList = new ArrayList<>();
        allDroppableItemList.add(new Harvest(this));
        allDroppableItemList.add(new Soda(this));
        allDroppableItemList.add(new SukunaFinger(this));
        allDroppableItemList.add(new SubaruShirt(this));
    }
    private void initializedAllCraftableItem(){
        allCraftableItemList = new ArrayList<>();
        allCraftableItemList.add(new MalevolentKitchen(this));
        allCraftableItemList.add(new UnlimitedHollowPurple(this));
    }
    private void initializeAllEnemy(){

    }
    private void initializeAllBoss(){

    }
    private Item getRandomDroppableItem(){
        int index = (int)(Math.random() * allDroppableItemList.size());
        Item randomItem = allDroppableItemList.get(index);
        if(randomItem instanceof Unique)allDroppableItemList.remove(randomItem);
        return (Item)randomItem.copy();
    }
    private Weapon getRandomWeapon(){
        //int index = (int)(Math.random() * allWeaponList.size());
        return allWeaponList.get((int)(Math.random() * allWeaponList.size()));
    }
    private Accessory getRandomAccessory(){
        int index = (int)(Math.random()*allAccessoryList.size());
        return  allAccessoryList.get(index);
    }
    private void getRandomLevelUpChoice(){

        haveChoice = false;

        levelUpChoice.put("choice0",null);
        levelUpChoice.put("choice1",null);
        levelUpChoice.put("choice2",null);
        levelUpChoice.put("choice0Type",null);
        levelUpChoice.put("choice1Type",null);
        levelUpChoice.put("choice2Type",null);

        boolean isWeaponListFull = true;
        boolean isAccessoryListFull = true;

        ArrayList<String> choiceAvailable = new ArrayList<>();
        Collections.addAll(choiceAvailable, "weapon","upgradeWeapon","accessory","upgradeAccessory");

        for(Weapon weapon : weaponList){
            if (weapon == null) {
                isWeaponListFull = false;
                break;
            }
        }

        if(isWeaponListFull)choiceAvailable.remove("weapon");
        if(allWeaponList.isEmpty())choiceAvailable.remove("weapon");


        for(Accessory accessory : accessoryList){
            if (accessory == null) {
                isAccessoryListFull = false;
                break;
            }
        }

        if(isAccessoryListFull)choiceAvailable.remove("accessory");
        if(allAccessoryList.isEmpty())choiceAvailable.remove("accessory");

        ArrayList<String> upgradeableWeaponIndex = new ArrayList<>();
        Collections.addAll(upgradeableWeaponIndex, "0","1","2","3","4","5");
        for(int i = 0 ; i < weaponList.length ; i++){
            if(weaponList[i] == null){
                upgradeableWeaponIndex.remove(String.valueOf(i));
            }
            else if(weaponList[i].getLevel().equals("Max") ){
                upgradeableWeaponIndex.remove(String.valueOf(i));
            }
        }

        if(upgradeableWeaponIndex.isEmpty())choiceAvailable.remove("upgradeWeapon");

        ArrayList<String> upgradeableAccessoryIndex = new ArrayList<>();
        Collections.addAll(upgradeableAccessoryIndex, "0","1","2","3","4","5");
        for(int i = 0 ; i < accessoryList.length ; i++){
            if(accessoryList[i] == null){
               upgradeableAccessoryIndex.remove(String.valueOf(i));
            }
            else if(accessoryList[i].getLevel().equals("Max") ){
                upgradeableAccessoryIndex.remove(String.valueOf(i));
            }
        }

        if(upgradeableAccessoryIndex.isEmpty())choiceAvailable.remove("upgradeAccessory");

        if(choiceAvailable.isEmpty()){
            this.putItemInBackpack(getRandomDroppableItem());
            this.currentState = GameState.PLAYING;
        }
        else{

            for(int i = 0; i<3;i++) {
                if(choiceAvailable.isEmpty())continue;
                String choiceType = choiceAvailable.get(random.nextInt(choiceAvailable.size()));
                if (Objects.equals(choiceType, "weapon")) {
                    Weapon randomWeapon = getRandomWeapon();
                    levelUpChoice.put("choice"+i, randomWeapon);
                    levelUpChoiceType.put("choice"+i+"Type", "weapon");
                    allWeaponList.remove(randomWeapon);
                    if(allWeaponList.isEmpty())choiceAvailable.remove("weapon");
                    haveChoice = true;

                } else if (Objects.equals(choiceType, "upgradeWeapon")) {
                    int randomIndex = Integer.parseInt(upgradeableWeaponIndex.get(random.nextInt(upgradeableWeaponIndex.size())));
                    levelUpChoice.put("choice"+i,weaponList[randomIndex]);
                    levelUpChoiceType.put("choice"+i+"Type", "upgrade");
                    upgradeableWeaponIndex.remove(String.valueOf(randomIndex));
                    if(upgradeableWeaponIndex.isEmpty())choiceAvailable.remove("upgradeWeapon");
                    haveChoice = true;

                } else if (Objects.equals(choiceType, "accessory")) {
                    Accessory randomAccessory = getRandomAccessory();
                    levelUpChoice.put("choice"+i, randomAccessory);
                    levelUpChoiceType.put("choice"+i+"Type", "accessory");
                    allAccessoryList.remove(randomAccessory);
                    if(allAccessoryList.isEmpty())choiceAvailable.remove("accessory");
                    haveChoice = true;

                } else if (Objects.equals(choiceType, "upgradeAccessory")) {
                    int randomIndex = Integer.parseInt(upgradeableAccessoryIndex.get(random.nextInt(upgradeableAccessoryIndex.size())));
                    levelUpChoice.put("choice"+i,accessoryList[randomIndex]);
                    levelUpChoiceType.put("choice"+i+"Type", "upgrade");
                    upgradeableAccessoryIndex.remove(String.valueOf(randomIndex));
                    if(upgradeableAccessoryIndex.isEmpty())choiceAvailable.remove("upgradeAccessory");
                    haveChoice = true;
                }


            }
        }


    }

    private GameObject getRandomChestChoice(){

        ArrayList<String> choiceAvailable = new ArrayList<>();
        Collections.addAll(choiceAvailable, "upgradeWeapon","upgradeAccessory","evolve","unite","craft");


        ArrayList<String> upgradeableWeaponIndex = new ArrayList<>();
        Collections.addAll(upgradeableWeaponIndex, "0","1","2","3","4","5");
        for(int i = 0 ; i < weaponList.length ; i++){
            if(weaponList[i] == null){
                upgradeableWeaponIndex.remove(String.valueOf(i));
            }
            else if(weaponList[i].getLevel().equals("Max") ){
                upgradeableWeaponIndex.remove(String.valueOf(i));
            }
        }

        if(upgradeableWeaponIndex.isEmpty())choiceAvailable.remove("upgradeWeapon");

        ArrayList<String> upgradeableAccessoryIndex = new ArrayList<>();
        Collections.addAll(upgradeableAccessoryIndex, "0","1","2","3","4","5");
        for(int i = 0 ; i < accessoryList.length ; i++){
            if(accessoryList[i] == null){
                upgradeableAccessoryIndex.remove(String.valueOf(i));
            }
            else if(accessoryList[i].getLevel().equals("Max") ){
                upgradeableAccessoryIndex.remove(String.valueOf(i));
            }
        }

        if(upgradeableAccessoryIndex.isEmpty())choiceAvailable.remove("upgradeAccessory");

        ArrayList<String> evolvableWeaponIndex = new ArrayList<>();
        Collections.addAll(evolvableWeaponIndex, "0","1","2","3","4","5");
        for(int i = 0 ; i < weaponList.length ; i++){
            if(weaponList[i] == null){
                evolvableWeaponIndex.remove(String.valueOf(i));
            }
            else if(!(weaponList[i] instanceof Evolvable evolvable && evolvable.isEvolvable())){
                evolvableWeaponIndex.remove(String.valueOf(i));
            }
        }

        if(evolvableWeaponIndex.isEmpty())choiceAvailable.remove("evolve");

        ArrayList<String> unitableWeaponIndex = new ArrayList<>();
        Collections.addAll(unitableWeaponIndex, "0","1","2","3","4","5");
        for(int i = 0 ; i < weaponList.length ; i++){
            if(weaponList[i] == null){
                unitableWeaponIndex.remove(String.valueOf(i));
            }
            else if(!(weaponList[i] instanceof Unitable unitable && unitable.isUnitable())){
                unitableWeaponIndex.remove(String.valueOf(i));
            }
        }

        if(unitableWeaponIndex.isEmpty())choiceAvailable.remove("unite");

        ArrayList<Item> craftableItemAvailable = new ArrayList<>();

        for(Item item : allCraftableItemList){
            if(((Craftable) item).isCraftable()) craftableItemAvailable.add(item);
        }

        if(craftableItemAvailable.isEmpty())choiceAvailable.remove("craft");


        GameObject returnItem = null;
        if(choiceAvailable.isEmpty()){
            Item item = getRandomDroppableItem();
            this.putItemInBackpack(item);
            returnItem= item;
        }
        else{
            String choiceType = choiceAvailable.get(random.nextInt(choiceAvailable.size()));

            if (Objects.equals(choiceType, "upgradeWeapon")) {
                int randomIndex = Integer.parseInt(upgradeableWeaponIndex.get(random.nextInt(upgradeableWeaponIndex.size())));
                weaponList[randomIndex].upgrade();
                SceneManager.updateWeaponAndAccessoryPanel(weaponList,accessoryList);
                returnItem= weaponList[randomIndex];
            }
            else if (Objects.equals(choiceType, "upgradeAccessory")) {
                int randomIndex = Integer.parseInt(upgradeableAccessoryIndex.get(random.nextInt(upgradeableAccessoryIndex.size())));
                accessoryList[randomIndex].upgrade();
                SceneManager.updateWeaponAndAccessoryPanel(weaponList,accessoryList);
                returnItem= accessoryList[randomIndex];
            }
            else if (Objects.equals(choiceType, "evolve")) {
                int randomIndex = Integer.parseInt(evolvableWeaponIndex.get(random.nextInt(evolvableWeaponIndex.size())));
                ((Evolvable)weaponList[randomIndex]).Evolve(randomIndex);
                SceneManager.updateWeaponAndAccessoryPanel(weaponList,accessoryList);
                returnItem= weaponList[randomIndex];
            }
            else if (Objects.equals(choiceType, "unite")) {
                int randomIndex = Integer.parseInt(unitableWeaponIndex.get(random.nextInt(unitableWeaponIndex.size())));
                ((Unitable)weaponList[randomIndex]).Unite(randomIndex);
                SceneManager.updateWeaponAndAccessoryPanel(weaponList,accessoryList);
                returnItem=  weaponList[randomIndex];
            }
            else if (Objects.equals(choiceType, "craft")) {
                Item randomCraftableItem = craftableItemAvailable.get(random.nextInt(craftableItemAvailable.size()));
                if(randomCraftableItem instanceof Unique)allCraftableItemList.remove(randomCraftableItem);
                ((Craftable) randomCraftableItem).deductMaterial();
                putItemInBackpack(randomCraftableItem);
                returnItem= randomCraftableItem;
            }



        }
        return returnItem;

    }

    /**
     * Applies the player's level-up selection.
     *
     * <p>If the chosen object is a {@link entity.weapon.Weapon}:
     * if the player already has it equipped it is upgraded; otherwise it is
     * placed in the first empty weapon slot.  If it is an
     * {@link entity.accessory.Accessory}, the same logic applies to the
     * accessory slots.  All accessories' {@code procEffect()} is called after
     * placement to apply any immediate bonuses.
     *
     * @param choice one of {@code "choice0"}, {@code "choice1"}, {@code "choice2"}
     */
    public void putLevelUpChoice(String choice) {
        GameObject object = levelUpChoice.get(choice);
        boolean added = false;
        if(object instanceof Weapon weapon){
            for(int i = 0 ; i < weaponList.length ; i++){
                if(weaponList[i] == null) continue;
                if(Objects.equals(weapon.getName(), weaponList[i].getName())){
                    weaponList[i].upgrade();
                    added = true;
                    break;
                }

            }
            if (!added){
                for(int i = 0 ; i < weaponList.length ; i++) {
                    if (weaponList[i] == null) {
                        weaponList[i] = weapon;
                        break;
                    }
                }
            }
        }
        else if(object instanceof Accessory accessory) {
            for (int i = 0; i < accessoryList.length; i++) {
                if(accessoryList[i] == null) continue;
                if (Objects.equals(accessory.getName(), accessoryList[i].getName())) {
                    accessoryList[i].upgrade();
                    added = true;
                    break;
                }

            }
            if (!added) {
                for (int i = 0; i < accessoryList.length; i++) {
                    if (accessoryList[i] == null) {
                        accessoryList[i] = accessory;
                        break;
                    }
                }
            }
        }
        for(Accessory accessory: accessoryList){
            if(accessory!=null)accessory.procEffect();
        }
        SceneManager.updateWeaponAndAccessoryPanel(weaponList,accessoryList);


    }
    public void returnChoice(String choice){
        if(!levelUpChoiceType.get(choice+"Type").equals("upgrade")){
            GameObject object = levelUpChoice.get(choice);
            if(object instanceof Weapon weapon){
                allWeaponList.add(weapon);
            }
            else if(object instanceof Accessory accessory){
                allAccessoryList.add(accessory);
            }
        }
    }

    /**
     * Adds an item to the player's backpack.
     *
     * <p>If the backpack already contains an item with the same name, its
     * {@link entity.item.Item#setAmount(int) amount} is incremented.  Otherwise
     * the item is placed in the first {@code null} slot.  The HUD is updated
     * after every change.
     *
     * @param object the item to add (must not be {@code null})
     */
    public void putItemInBackpack(Item object) {
        boolean added = false;
        for(Item item : backpack){
            if(item != null && object.getName().equals(item.getName())){
                item.setAmount(item.getAmount()+1);
                SceneManager.updateBackPack(backpack);
                added = true;
                break;
            }
        }
        if(!added){
            for(int i = 0 ; i < backpack.length ; i++ ){
                if(backpack[i] == null){
                    backpack[i] = object;
                    SceneManager.updateBackPack(backpack);
                    break;
                }
            }
        }
    }

    /**
     * Returns {@code true} if a world-space position falls within the visible
     * screen area plus a one-tile (96 px) margin to avoid pop-in artefacts.
     *
     * @param mapX world X coordinate to test
     * @param mapY world Y coordinate to test
     * @return {@code true} if the position is within the extended viewport
     */
    public boolean isCloseEnoughToRender(double mapX, double mapY) {
        return  mapX > character.getMapX()-960-96 &&
                mapX < character.getMapX()+960+96 &&
                mapY > character.getMapY()-540-96 &&
                mapY < character.getMapY()+540+96;
    }
    /**
     * Converts a world X coordinate to a screen X coordinate.
     *
     * <p>The character is always drawn at screen X = 960 (horizontal centre of
     * a 1920 px canvas), so this subtracts the character's world X and adds 960.
     *
     * @param mapX world-space X to convert
     * @return screen-space X in pixels
     */
    public double getScreenX(double mapX) {
        return mapX - character.getMapX() + 960;
    }
    /**
     * Converts a world Y coordinate to a screen Y coordinate.
     *
     * <p>The character is always drawn at screen Y = 540 (vertical centre of
     * a 1080 px canvas), so this subtracts the character's world Y and adds 540.
     *
     * @param mapY world-space Y to convert
     * @return screen-space Y in pixels
     */
    public double getScreenY(double mapY) {
        return mapY - character.getMapY() + 540;
    }






    public GameState getCurrentState() {
        return currentState;
    }
    public void setCurrentState(GameState currentState) {
        this.currentState = currentState;
    }
    public InputManager getInputManager() {
        return inputManager;
    }
    public void setInputManager(InputManager inputManager) {
        this.inputManager = inputManager;
    }
    public Character getCharacter() {
        return character;
    }
    public void setCharacter(Character character) {
        this.character = character;
    }
    public double getGameTimer() {
        return gameTimer;
    }
    public void setGameTimer(double gameTimer) {
        this.gameTimer = gameTimer;
    }
    public void addEnemy(Enemy enemy){
        this.inGameEnemyList.add(enemy);
    }

    public Enemy getClosestTarget() {
        return closestTarget;
    }

    public Character[] getAllCharacterList() {
        return allCharacterList;
    }
    public ArrayList<ExpOrb> getDroppedExpOrbList(){
        return droppedExpOrbList;
    }
    public ArrayList<Chest> getExistingChestList(){
        return this.existingChestList;
    }

    public ArrayList<Enemy> getInGameEnemyList() {
        return inGameEnemyList;
    }

    public ArrayList<Weapon> getUsingWeaponList() {
        return usingWeaponList;
    }

    public ArrayList<Item> getUsingItemList() {
        return usingItemList;
    }

    public ArrayList<Item> getDroppedItemList() {
        return droppedItemList;
    }

    public Item[] getBackpack() {
        return backpack;
    }
    public Weapon[] getWeaponList(){
        return weaponList;
    }
    public Accessory[] getAccessoryList() {
        return accessoryList;
    }

    public void setTileManager(TileManager tileManager) {
        this.tileManager = tileManager;
    }

    public HashMap<String, GameObject> getLevelUpChoice() {
        return levelUpChoice;
    }
    public HashMap<String,String>  getLevelUpChoiceType(){
        return levelUpChoiceType;
    }
    /**
     * Multiplies the XP gain multiplier by the given factor.
     *
     * <p>Called by Growth-type accessories to make the player level up faster.
     *
     * @param multiplier the factor to apply (e.g. {@code 1.1} for +10 % XP)
     */
    public void increaseGrowth(double multiplier) {
        xPMultiplier*=multiplier;
    }
}
