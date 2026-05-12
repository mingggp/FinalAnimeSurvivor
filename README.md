# Final Anime Survivor

A top-down bullet-hell / survivor game inspired by **Vampire Survivors**, themed around the anime **Jujutsu Kaisen**.  
Survive waves of enemies, collect powerful weapons and accessories, level up, and become the strongest.

---

## Authors

| Student ID | Name |
|---|---|
| 6831356521 | Popnipit Watcharapitak |
| 6831362221 | Voravich Thanyavinichakul |

---

## Requirements

| Tool | Version |
|---|---|
| Java JDK | 21+ |
| JavaFX | 24.0.2 |
| Gradle | 8.x (wrapper included) |

---

## How to Build & Run

### Run from source (Gradle)

```bash
./gradlew run
```

### Run the exported JAR

> The JAR must be placed **inside its own folder** before running.

```bash
mkdir game
cp FinalAnimeSurvivor.jar game/
cd game
java -jar FinalAnimeSurvivor.jar
```

---

## Controls

### In-Game

| Key | Action |
|---|---|
| `W` / `A` / `S` / `D` | Move character |
| `Tab` | Open / close backpack |
| `Q` | Toggle auto-use items |
| `Esc` | Pause / Quit dialog |

### Level-Up Menu

| Key | Action |
|---|---|
| `W` / `S` | Navigate choices |
| `Space` | Confirm selection |
| Mouse click | Select choice |

---

## Gameplay

### Characters

| Character | Starter Weapon | Specialty |
|---|---|---|
| **Sukuna** | Cleave | High melee damage |
| **Gojo** | Infinity (Mugen) | Continuous area damage |

### Weapons

| Weapon | Type | Description |
|---|---|---|
| OSU! Cursor (Standard) | Bouncing projectile | Cursor bounces around the screen dealing damage |
| Lapse Blue | Targeted circle | Drops a damage zone on the nearest enemy |
| Reversal Red | Projectile | Shoots a projectile that explodes on impact |
| Cleave | Melee | Always-on slash hitting the nearest enemy |
| Infinity (Mugen) | Aura | Continuous damage circle surrounding the player |
| Dismantle | Projectile | Piercing projectile passing through enemies |

### Evolved / Ultimate Weapons

Weapons can evolve when at max level and a **Chest** is collected:

| Base Weapon | Evolved Form |
|---|---|
| OSU! Cursor | Lazer |
| Lapse Blue | Maximum Blue |
| Reversal Red | Maximum Red |
| Cleave | Maximum Cleave |
| Infinity | Maximum Output Infinity |
| Dismantle | Maximum Dismantle |

### Accessories

| Accessory | Effect |
|---|---|
| Sukuna Arm | Increases weapon damage |
| Sukuna Cloak | Increases armor |
| Six Eyes (Rikugan) | Increases weapon size |
| Gojo Glasses | Increases weapon size |
| Blindfold | Increases XP gain |
| KeyPad | Boosts Standard weapon |

### Items (Backpack)

| Item | Type | Effect |
|---|---|---|
| Soda | Usable | Heals 1,000 HP (3-second cooldown; only usable below max HP) |
| Harvest | Passive | Increases HP recovery |
| Sukuna Finger | Unique/Passive | Boosts Sukuna's power |
| Subaru Shirt | Unique/Passive | Revives once on death |
| Malevolent Kitchen | Craftable | Crafted item with special effect |
| Unlimited Hollow Purple | Craftable | Craftable ultimate item |

### Level-Up System

- Gain XP from **Exp Orbs** dropped by enemies (80% drop rate).
- On level-up, choose **one of three options**: new weapon, upgrade existing weapon, or new/upgraded accessory.
- **Chests** (0.1% drop chance from enemies) offer evolution, unification, or crafting choices.

---

## Map

- Single tile-based map (`map1.txt`), 80 × 60 tiles, each tile is 96 × 96 px → **7,680 × 5,760** world size.
- Border tiles are solid walls (dirt); interior is open grass.
- Camera follows the player.

---

## Project Structure

```
FinalAnimeSurvivor/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── application/       # Entry point (Main, Launcher)
│   │   │   ├── core/              # Game loop, state, enemy spawner
│   │   │   ├── entity/            # All game entities
│   │   │   │   ├── character/     # Player character
│   │   │   │   ├── enemy/         # Enemy AI
│   │   │   │   ├── weapon/        # Weapons & evolved weapons
│   │   │   │   ├── accessory/     # Accessories
│   │   │   │   ├── item/          # Items (usable, craftable)
│   │   │   │   ├── misc/          # ExpOrb, Chest, render layers
│   │   │   │   └── map/           # Map entity
│   │   │   ├── entityInterface/   # Interfaces (GameObject, Renderable, Updatable, ...)
│   │   │   ├── gui/               # JavaFX UI canvases and panels
│   │   │   │   └── gameLayout/    # In-game HUD panels
│   │   │   ├── tile/              # Tile system (TileManager, Tile)
│   │   │   ├── utils/             # CollisionChecker, InputManager, SceneManager, SoundManager, SpriteManager
│   │   │   └── vfx/               # Visual effects (VFXManager, DamageText, Slash, SkillPopUp)
│   │   └── resources/             # Images, map files, BGM
│   └── test/
│       └── java/                  # JUnit 5 test classes
├── build.gradle.kts
└── settings.gradle.kts
```

---

## OOP Concepts

### Inheritance

```
Entity (abstract)
├── Character
├── Enemy
└── Item (abstract)
    ├── Soda, Harvest, SukunaFinger, SubaruShirt
    └── craftable/
        ├── MalevolentKitchen
        └── UnlimitedHollowPurple

Weapon (abstract)
├── Standard, Blue, Red, Cleave, Infinity, Dismantle, Bible
└── evolvedWeapon/
    ├── Lazer, MaximumBlue, MaximumRed, MaximumCleave,
    │   MaximumOutputInfinity, MaximumDismantle

Accessory (abstract)
├── SukunaArm, SukunaCloak, SixEye, GojoGlasses, Blindfold, KeyPad, AcolyteHat
```

### Interfaces

| Interface | Purpose |
|---|---|
| `GameObject` | Base interface — all game objects implement this |
| `Renderable` | Objects that can be drawn to screen |
| `Updatable` | Objects that update each game tick |
| `Craftable` | Items that can be crafted from materials |
| `Droppable` | Items that can be dropped by enemies |
| `Material` | Items that serve as crafting ingredients |
| `Unique` | Items limited to one per run |
| `Usable` | Items that can be activated by the player |
| `Evolvable` | Weapons that can evolve into a stronger form |
| `Unitable` | Weapons that can fuse with another |
| `DamageIncreasable` | Weapons that support damage upgrades |
| `CooldownDecreasable` | Weapons that support cooldown reduction |
| `SizeIncreasable` | Weapons/effects that support size upgrades |
| `DurationIncreasable` | Weapons that support duration upgrades |
| `SpeedIncreasable` | Weapons that support speed upgrades |
| `AmountIncreasable` | Weapons that support amount upgrades |

### Polymorphism

- `weapon.use(dt)` — each weapon subclass has its own attack behavior called uniformly through the `Weapon` loop in `GameManager`.
- `item.update(dt)` / `item.isExpired()` — items in `usingItemList` are updated polymorphically via the `Updatable` interface.
- `droppedItem.updateAsDroppedItem(dt)` — all droppable items move toward the player using the `Droppable` interface.
- `accessory.procEffect()` — each accessory applies its buff differently, called uniformly in the accessory loop.
- `gameObject.copy()` — prototype pattern: each entity implements its own deep copy for spawning.

### Access Modifiers

- Fields are `private`; behavior is exposed through `public` getters/setters.
- `SoundManager` uses a `private` constructor with a `public static getInstance()` (Singleton pattern).
- Internal helpers like `getRandomWeapon()`, `getRandomLevelUpChoice()` are `private` inside `GameManager`.
- `VFXManager` uses `static` methods as a utility class.

---

## JUnit Tests

Located in `src/test/java/`:

| Test Class | What It Tests |
|---|---|
| `CharacterTest` | Damage reception, healing, death state, I-frame immunity |
| `EnemyTest` | Damage, movement direction calculation |
| `ChestTest` | Chest initialization |
| `ExpOrbTest` | Exp orb initialization |
| `InputManagerTest` | Key press/release tracking |

Run all tests:

```bash
./gradlew test
```

---

## Known Issues & Bugs

| # | Area | Description |
|---|---|---|
| 1 | Sound | `SoundManager.playSFX()` uses `new File(path)` — SFX will not play when running from a JAR (must use classpath resource) |
| 2 | UI | No visual **cooldown bar** for weapons; the weapon slot only shows level number |
| 3 | Shrine | Multiple shrine sprite variants exist in resources; the displayed sprite may not match intended artwork |
| 4 | Soda | `use()` is blocked when HP is at maximum — prevents stacking heals proactively |
| 5 | JAR | SFX path bug prevents audio effects in packaged JAR |
| 6 | Map | Only one map is implemented; the tile set is minimal (grass + dirt only) |
| 7 | Null crash | `getClosestTarget()` can return `null`; weapons (Blue, Red) do not null-check before using it |
| 8 | SixEye | `procEffect()` uses `break` instead of `continue` — stops checking remaining weapon slots when a `null` slot is found mid-array |
| 9 | Bible | Weapon class exists but has no implemented attack behavior (placeholder) |
| 10 | BGM | `startBGM(String songName)` ignores its parameter and always plays the already-loaded media |
