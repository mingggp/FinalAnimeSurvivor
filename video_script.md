# บทพูดสำหรับวิดีโอนำเสนอ — Final Anime Survivor
# ประมาณเวลา: ~10 นาที

---

## [00:00 – 00:30] บทนำ — แนะนำตัวและโปรเจกต์

> **[แสดงหน้าจอ: Main Menu ของเกม หรือ Thumbnail โปรเจกต์]**

"สวัสดีครับ ผมชื่อ Popnipit และเพื่อนผมชื่อ Voravich
เราสองคนได้พัฒนาโปรเจกต์ชื่อว่า **Final Anime Survivor**
เป็นวิดีโอเกมแนว Survivor แบบ Top-down
ได้รับแรงบันดาลใจมาจากเกม Vampire Survivors
โดยนำธีมตัวละครและอาวุธจากอนิเมะ Jujutsu Kaisen มาใช้ครับ
เกมนี้พัฒนาด้วยภาษา Java และ JavaFX
เดี๋ยวเราจะพาชมการเล่น พร้อมอธิบายแนวคิด OOP ที่ใช้ในการออกแบบโค้ดครับ"

---

## [00:30 – 04:30] Demo การเล่นเกม

### [00:30 – 01:00] Main Menu และ Character Select

> **[แสดงหน้าจอ: Main Menu → กดปุ่ม Play]**

"เริ่มต้นที่หน้า Main Menu ครับ มีปุ่มหลักสามปุ่มคือ
Play, Settings และ Quit
ผมจะกด Play เพื่อเข้าสู่หน้าเลือกตัวละครครับ"

> **[แสดงหน้าจอ: กด Settings จาก Main Menu]**

"ก่อนเริ่มเกม ลองดูหน้า Settings ก่อนครับ
ที่นี่ปรับเสียงได้สองแบบแยกกันเลย
แถบบนคือ BGM Volume สำหรับเพลงพื้นหลัง
แถบล่างคือ SFX Volume สำหรับเสียงเอฟเฟกต์ต่าง ๆ ครับ
กด Back กลับไป Main Menu แล้วค่อย Play ครับ"

> **[แสดงหน้าจอ: Character Select — เลือก Sukuna หรือ Gojo]**

"ในโปรเจกต์นี้มีตัวละครให้เลือกสองตัวครับ
ตัวแรกคือ **Sukuna** ใช้อาวุธเริ่มต้น Cleave ฟันดาบระยะประชิด
และตัวที่สองคือ **Gojo** ใช้อาวุธเริ่มต้น Infinity หรือ Mugen
เป็นวงกลมดาเมจรอบตัวตลอดเวลา
ผมจะเลือก Sukuna แล้วเริ่มเกมเลยครับ"

### [01:00 – 02:30] Gameplay — การเอาตัวรอดและเก็บ XP

> **[แสดงหน้าจอ: เกมกำลังรัน — ผู้เล่นวิ่ง เก็บ Orb โจมตีศัตรู]**

"ในเกม ผู้เล่นจะควบคุมตัวละครด้วย W A S D ครับ
ศัตรูจะ Spawn รอบ ๆ ตัวเราและวิ่งเข้ามาโจมตีตลอดเวลา
เมื่อเราฆ่าศัตรู มีโอกาส 80% ที่จะดรอป Exp Orb สีแดงครับ
วิ่งเข้าไปเก็บ หรือถ้า Orb อยู่ในรัศมี Magnet
มันจะลอยเข้ามาหาเราเองครับ

แถบ XP อยู่ด้านล่างสุดของหน้าจอ
เต็มเมื่อไหร่ก็จะ Level Up ครับ"

> **[ชี้ที่ช่องอาวุธและ Cooldown Bar ด้านซ้ายบน]**

"สังเกตที่ช่อง Weapon Slot ด้านซ้ายบนครับ
จะมี Cooldown Bar เป็น Overlay สีดำโปร่งใสปิดอยู่
ยิ่ง Bar หดลงเรื่อย ๆ แปลว่าอาวุธใกล้จะพร้อมยิงอีกครั้งครับ
เมื่อ Bar หมดก็คือ Cooldown หมด พร้อมใช้แล้ว"

"เกมยังมีเสียงเอฟเฟกต์ประกอบด้วยครับ
ตั้งแต่เสียงกดปุ่ม เสียงเก็บ Exp Orb เสียง Level Up
และเสียงโจมตีเฉพาะของ Sukuna และ Gojo ครับ"

### [02:30 – 03:15] ระบบ Level Up

> **[แสดงหน้าจอ: เมนู Level Up — สามตัวเลือก]**

"เมื่อ Level Up จะมีเมนูขึ้นมาให้เลือก 1 ใน 3 ตัวเลือกครับ
อาจเป็น อาวุธใหม่ อัปเกรดอาวุธที่มีอยู่ หรือ Accessory ใหม่
ใช้ W S เลื่อนเลือก แล้วกด Space หรือคลิก เพื่อยืนยันครับ"

### [03:15 – 04:00] ระบบ Chest และ Evolve

> **[แสดงหน้าจอ: วิ่งเข้า Chest — เมนู Chest Choice]**

"นอกจาก XP Orb แล้ว ศัตรูมีโอกาสดรอป Chest สีทองด้วยครับ
เมื่อวิ่งเข้าไปเก็บ Chest จะมีตัวเลือกพิเศษให้ เช่น
Evolve อาวุธที่ Max Level ให้แข็งแกร่งขึ้น
หรือ Craft ไอเทมพิเศษอย่าง Malevolent Kitchen หรือ Unlimited Hollow Purple ครับ"

### [04:00 – 04:30] ระบบ Backpack และ Items

> **[แสดงหน้าจอ: กด Tab เปิด Backpack — ไอเทมต่าง ๆ]**

"กด **Tab** เพื่อเปิดกระเป๋าครับ ข้างในมีของที่ดรอปจากศัตรู
เช่น Soda กดใช้เพื่อฟื้น HP 1,000 มี Cooldown 3 วินาที
Subaru Shirt ฟื้นคืนชีพได้หนึ่งครั้ง
และ Harvest เรียก Vacuum Effect ดูดของรอบ ๆ เข้ามาทันทีครับ"

### [04:30 – 05:00] Pause Screen และ Death Screen

> **[แสดงหน้าจอ: กด ESC ระหว่างเกม]**

"ระหว่างเกมกด ESC ได้เลยครับ จะขึ้น Pause Screen
มีปุ่ม Resume กลับเข้าเกม, Settings เปิดตั้งค่าเสียงได้แม้อยู่ในเกม
และ Main Menu ออกจากเกมพร้อมหยุดเพลงอัตโนมัติครับ

และเมื่อตัวละครตาย เกมจะแสดงหน้าผลลัพธ์
พร้อมปุ่ม Restart กลับไปเลือกตัวละคร
หรือ Main Menu เพื่อออกไปหน้าหลักครับ"

---

## [05:00 – 08:00] การออกแบบโปรแกรม — OOP Concepts

### [05:00 – 05:45] Inheritance

> **[แสดงหน้าจอ: Slide UML หรือ โค้ดใน IDE]**

"มาดูการออกแบบโค้ดกันครับ
โปรเจกต์นี้ใช้ **Inheritance** เพื่อแบ่งปัน Attribute และ Behavior ร่วมกัน

ตัวอย่างเช่น ลำดับชั้นของ Weapon ครับ
Weapon เป็น Abstract Class ที่เก็บ Field ร่วมอย่าง level, maxLevel, cooldown, timeSinceUse และ icon
แล้วอาวุธแต่ละชนิดอย่าง Standard, Blue, Red, Cleave, Bible ต่างสืบทอดมาจาก Weapon
และ Evolved Weapon อย่าง MaximumBlue ก็ขยายมาจาก Blue อีกทีนึงครับ

สังเกตว่า timeSinceUse ที่บอกว่าผ่านไปกี่วินาทีตั้งแต่ยิงครั้งล่าสุด
ประกาศอยู่ใน Weapon Base Class เลย ไม่ต้องเขียนซ้ำในอาวุธทุกตัว
และยังช่วยให้ UI อ่านค่า Cooldown Progress ได้โดยไม่ต้องรู้ Class จริงด้วยครับ

ในทำนองเดียวกัน Entity เป็น Abstract Class
ที่ Character และ Enemy สืบทอดมาเพื่อใช้ตำแหน่ง mapX, mapY
และ Item เป็น Abstract Class ที่ Soda, Harvest, SubaruShirt สืบทอดมาครับ"

### [05:15 – 06:00] Interface

> **[แสดงหน้าจอ: โฟลเดอร์ entityInterface/ ใน IDE]**

"โปรเจกต์ใช้ **Interface** อย่างกว้างขวางครับ
Interface ช่วยให้เราแยก Contract ออกจาก Implementation
ทำให้ GameManager จัดการ Entity หลายประเภทผ่าน Interface เดียวกันได้

Interface หลักมีเช่น GameObject เป็นฐานของทุก Object ในเกม
Renderable สำหรับ Object ที่วาดบนหน้าจอ
Updatable สำหรับ Object ที่อัปเดตทุก Frame
Usable สำหรับ Item ที่ผู้เล่นใช้งานได้
Evolvable สำหรับอาวุธที่ Evolve ได้
และ Craftable สำหรับ Item ที่ Craft ได้จาก Material ครับ

ตัวอย่างเช่น Soda Implement Usable, Droppable และ Updatable พร้อมกัน
ทำให้ GameManager เรียก use(), updateAsDroppedItem() และ update() ได้
โดยไม่ต้อง Cast ให้ยุ่งยากครับ

อีกตัวอย่างนึงคือ Bible ซึ่งเป็นอาวุธวงโคจรหนังสือรอบตัวผู้เล่น
Implement ถึง 6 Interface พร้อมกัน ทั้ง DamageIncreasable, SizeIncreasable,
AmountIncreasable, SpeedIncreasable, DurationIncreasable และ CooldownDecreasable
ทำให้ Accessory ทุกชนิดสามารถ Buff Bible ได้ตามที่ Implement ไว้ครับ"

### [06:00 – 07:00] Polymorphism

> **[แสดงหน้าจอ: โค้ด GameManager — Weapon Loop และ Item Loop]**

"**Polymorphism** ถูกใช้ใน GameManager อย่างชัดเจนครับ
ในส่วน Game Loop มี Loop ที่วน weaponList
แล้วเรียก weapon.use(dt) บน Weapon แต่ละตัว
แต่ละ Weapon Override use() ด้วยพฤติกรรมต่างกัน
Standard ยิง Cursor เด้ง, Blue วางวงกลม, Cleave ฟันระยะประชิด,
Bible หมุนหนังสือรอบตัว — แต่ GameManager ไม่ต้องรู้ว่าเป็น Weapon ชนิดไหนเลยครับ

เช่นเดียวกับ accessory.procEffect() ใน Accessory Loop
SixEye เพิ่มขนาดอาวุธ, SukunaArm เพิ่มดาเมจ แต่ Loop เดียวกันทำงานได้ครับ

และ gameObject.copy() ที่ใช้ Prototype Pattern
ทำให้ spawn ศัตรูหรือ Item ใหม่โดยไม่ต้องรู้ Class จริงครับ"

### [07:00 – 07:30] Access Modifier และ Encapsulation

> **[แสดงหน้าจอ: โค้ด Entity.java และ SoundManager — Singleton]**

"ในเรื่อง **Access Modifier** ครับ
Field ทุก Field ของ Entity เช่น mapX, mapY, name ถูกประกาศเป็น **private**
เข้าถึงผ่าน Getter/Setter เท่านั้น เพื่อป้องกันการแก้ไขโดยตรงจากภายนอก

ส่วน Field ที่ subclass ต้องใช้บ่อย เช่น level ใน Weapon กับ Accessory
ประกาศเป็น **protected** เพื่อให้ subclass อ่านได้โดยตรงโดยไม่ต้องผ่าน Getter ครับ

Method ภายใน GameManager อย่าง getRandomWeapon() เป็น **private** ไม่เปิดเผยออกนอก
และ SoundManager ใช้ **Singleton Pattern**
ด้วย private constructor และ public static getInstance()
เพื่อให้มี Instance เดียวตลอดโปรแกรมครับ"

---

## [07:30 – 08:30] JUnit Tests

> **[แสดงหน้าจอ: โฟลเดอร์ test/ ใน IDE — รัน Test]**

"มาดู **Unit Test** กันครับ
เราเขียน JUnit 5 Test ไว้ 5 ไฟล์ ครอบคลุม Logic สำคัญของ Class หลักครับ"

> **[เปิดไฟล์ WeaponTest.java]**

"เริ่มจาก **WeaponTest** ครับ ทดสอบ Method getCooldownProgress()
ซึ่งคืนค่าระหว่าง 0 ถึง 1 บอกว่า Cooldown หมดไปแล้วกี่เปอร์เซ็นต์

test แรกตรวจว่า ถ้า timeSinceUse เป็น 0 คือยิงไปใหม่ ๆ ก็ต้องได้ 0.0
test ที่สองตรวจว่า ถ้า timeSinceUse เท่ากับ cooldown พอดี ต้องได้ 1.0 หรือพร้อมยิงแล้ว
test ที่สามตรวจ Edge Case ว่า timeSinceUse เกิน cooldown ก็ต้องยังได้แค่ 1.0
ไม่เกินนี้ เพราะถ้าพังจะทำให้ UI Bar แสดงผิด
และ test สุดท้ายตรวจว่า Weapon ที่ cooldown เป็น 0 ต้องได้ 1.0 เสมอ
เพื่อป้องกัน divide-by-zero ครับ"

> **[เปิดไฟล์ AccessoryTest.java]**

"**AccessoryTest** ทดสอบระบบ Upgrade ของ Accessory ครับ
test ตรวจว่า Level เริ่มต้นเป็น 1 เสมอ
เมื่อ Upgrade แล้ว Level เพิ่มขึ้น และเมื่อถึง maxLevel แล้ว getLevel() ต้องคืน 'Max'
ไม่เพิ่มอีกแม้จะ Upgrade ต่อ เพราะถ้าพัง Level อาจล้น Integer ได้ครับ"

> **[เปิดไฟล์ ItemTest.java]**

"**ItemTest** ทดสอบ Base Class ของ Item ครับ
ตรวจว่า amount เริ่มต้นเป็น 0 setAmount เก็บค่าถูกต้อง
และ tag() เปลี่ยน Flag taggedByMagnet จาก false เป็น true
Flag นี้สำคัญมากเพราะ Harvest Item ใช้มันดูดของเข้าหาผู้เล่นครับ"

> **[เปิดไฟล์ CollisionCheckerTest.java]**

"**CollisionCheckerTest** ทดสอบ Collision กับ Tile Map ครับ
เราสร้าง Tile Grid 3x3 จำลองในหน่วยความจำ
โดยมีผนังล้อมรอบและมีช่องว่างตรงกลาง
แล้วเทส 4 ทิศ ว่าถ้าตัวละครเดินชนผนัง ตัวแปร dx หรือ dy จะต้องถูก Zero ออก
และมี test พิเศษที่ตรวจว่าถ้า setTile(null, null) แล้ว
Collision Checker ต้องคืนค่าโดยไม่ทำอะไร เพราะถ้าพังจะ Crash ทันทีครับ"

> **[เปิดไฟล์ GameStateTest.java]**

"และ **GameStateTest** ทดสอบ Enum GameState ครับ
ตรวจว่า State สำคัญทั้ง 7 ตัวอย่าง MAIN_MENU, PLAYING, LEVEL_UP, DEATH ยังอยู่ครบ
เพราะถ้ามีคนลบหรือเปลี่ยนชื่อ State โดยไม่ตั้งใจ SceneManager จะทำงานผิดพลาดครับ"

> **[รัน test ทั้งหมดให้เห็น passed]**

"รัน test ทั้งหมดด้วย ./gradlew test ครับ"

> **[แสดงผล: Test Passed ทั้งหมด]**

"Test ผ่านทุกตัวครับ"

---

## [08:30 – 09:15] โครงสร้างโปรแกรมและ GitHub

> **[แสดงหน้าจอ: GitHub Repository หน้า Code]**

"โปรเจกต์แบ่ง Package อย่างชัดเจนครับ
core สำหรับ GameManager, EnemySpawner และ GameState
entity สำหรับทุก Entity ในเกม
entityInterface สำหรับ Interface ทั้งหมด
gui สำหรับ JavaFX UI
utils สำหรับ CollisionChecker, SoundManager, SceneManager และ SpriteManager
และ vfx สำหรับ Visual Effect

SpriteManager เป็น Utility ที่เราเขียนเพิ่มขึ้นมาโดยเฉพาะ
เพื่อแก้ปัญหาการโหลดรูปภาพใน JAR File
โดยใช้ getResourceAsStream แทน new Image(path) ครับ

ทุกอย่างรวมถึง Code, JAR, Report และ UML Diagram
อัปโหลดไว้บน GitHub เรียบร้อยแล้วครับ"

---

## [09:15 – 09:30] สรุปและจบ

> **[แสดงหน้าจอ: เกมรันอยู่ — ผู้เล่นกำลังสู้ศัตรู]**

"สรุปคือ Final Anime Survivor เป็นเกม Survivor ที่มีระบบซับซ้อน
มีอาวุธ 7 ชนิดพร้อม Evolved Form, Accessory 6 ชนิด, ระบบเสียง BGM/SFX แยกกัน
ระบบ Level Up, Chest, Backpack, Tile Map, Collision Detection, Cooldown Bar และ VFX
ทั้งหมดออกแบบตามหลัก OOP อย่างเป็นระบบครับ

ขอบคุณที่รับชมครับ หากมีคำถามสามารถถามได้เลยครับ"

---

## หมายเหตุสำหรับการอัด

| ช่วงเวลา | หน้าจอที่ควรแสดง |
|---|---|
| 00:00–00:30 | Main Menu / Title Card |
| 00:30–01:00 | กด Play → Character Select |
| 01:00–02:30 | Gameplay: วิ่ง, สู้, เก็บ Orb, Cooldown Bar |
| 02:30–03:15 | เมนู Level Up |
| 03:15–04:00 | เปิด Chest |
| 04:00–04:30 | เปิด Backpack (Tab), ใช้ Soda |
| 04:30–05:00 | Pause Screen (ESC), Death Screen |
| 05:00–05:45 | Slide/IDE: Inheritance UML |
| 05:45–06:30 | IDE: entityInterface/ folder |
| 06:00–07:00 | IDE: GameManager weapon loop |
| 07:00–07:30 | IDE: SoundManager singleton + Entity private fields |
| 07:30–08:00 | IDE: WeaponTest.java + AccessoryTest.java |
| 08:00–08:30 | IDE: CollisionCheckerTest + GameStateTest + รัน Test |
| 08:30–09:15 | GitHub repo / Project structure |
| 09:15–09:30 | Gameplay / End screen |
