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

public class GameManager {
    
    private Random random = new Random();
    private GameState currentState;
    private InputManager inputManager;
    private ArrayList<Character> characterList;
    private Character character;
    private double gameTimer;
    private int level;
    private int currentExperience;
    private int ExperienceForNextLevel;
    private ArrayList<Enemy> inGameEnemyList;
    private ArrayList<Enemy> AllEnemyList;
    private ArrayList<Item> allCraftableItemList;
    private ArrayList<Item> allDroppableItemList;
    private Item[] backpack;
    private ArrayList<Item> droppedItemList;
    private ArrayList<Item> usingItemList;
    private ArrayList<ExpOrb> allExpOrbList;
    private ArrayList<ExpOrb> droppedExpOrbList;
    private ArrayList<Weapon> usingWeaponList;
    private ArrayList<Weapon> allWeaponList;
    private Weapon[] weaponList;
    private ArrayList<Accessory> allAccessoryList;
    private Accessory[] accessoryList;
    private TileManager tileManager;
    private Chest chest;
    private ArrayList<Chest> existingChestList;

    private Character[] allCharacterList;

    private EnemySpawner spawner;
    private Enemy closestTarget;


    private final HashMap<String, GameObject> levelUpChoice = new HashMap<>();
    private final HashMap<String, String> levelUpChoiceType = new HashMap<>();
    private boolean haveChoice;

    private boolean autoUseItem;

    // InGameStat
    private double timeSinceDead;
    private double xPMultiplier;
    private double armor;
    private double hpRecoveryMultiplier;
    private double luck;
    private boolean revived;

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

    public void resetGame(){
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
    public void startGame(){
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
    }
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
        Character character1 = new Character( "sukuna", 300,  1000,256,new Cleave(this),new Image("character/icon/sukunaIcon.png"),new Image("character/sprite/sukuna.png"));
        allCharacterList[0] = character1;
        Character character2 = new Character( "gojo", 300,  1000,256,new Infinity(  this),new Image("character/icon/gojoIcon.png"),new Image("character/sprite/gojo.png"));
        allCharacterList[1] = character2;
    }

    private void initializeAllWeapon(){
        allWeaponList = new ArrayList<>();

        allWeaponList.add(new Dismantle(this));
        allWeaponList.add(new Cleave(this));
        allWeaponList.add(new Red(this));
        allWeaponList.add(new Blue(this));
        allWeaponList.add(new Infinity(this));
        //allWeaponList.add(new Bible(this));
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

    public void putLevelUpChoice(String choice){
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

    public void putItemInBackpack(Item object){
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

    public boolean isCloseEnoughToRender(double mapX,double mapY){
        return  mapX > character.getMapX()-960-96 &&
                mapX < character.getMapX()+960+96 &&
                mapY > character.getMapY()-540-96 &&
                mapY < character.getMapY()+540+96;
    }
    public double getScreenX(double mapX){
        return mapX - character.getMapX() + 960;
    }
    public double getScreenY(double mapY){
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
    public void increaseGrowth(double multiplier){
        xPMultiplier*=multiplier;
    }
}
