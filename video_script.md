# บทพูดสำหรับวิดีโอนำเสนอ — Final Anime Survivor
# ประมาณเวลา: ~9 นาที 30 วินาที

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

### [02:30 – 03:15] ระบบ Level Up

> **[แสดงหน้าจอ: เมนู Level Up — สามตัวเลือก]**

"เมื่อ Level Up จะมีเมนูขึ้นมาให้เลือก 1 ใน 3 ตัวเลือกครับ
อาจเป็น อาวุธใหม่ อัปเกรดอาวุธที่มีอยู่ หรือ Accessory ใหม่
ใช้ W S เลื่อนเลือก แล้วกด Space หรือคลิก เพื่อยืนยันครับ
ผมจะเลือกอาวุธ Lapse Blue เพิ่มเข้ามา"

### [03:15 – 04:00] ระบบ Chest และ Evolve

> **[แสดงหน้าจอ: วิ่งเข้า Chest — เมนู Chest Choice]**

"นอกจาก XP Orb แล้ว ศัตรูมีโอกาสดรอป Chest สีทองด้วยครับ
เมื่อวิ่งเข้าไปเก็บ Chest จะมีตัวเลือกพิเศษให้ เช่น
Evolve อาวุธที่ Max Level ให้แข็งแกร่งขึ้น
หรือ Craft ไอเทมพิเศษอย่าง Malevolent Kitchen หรือ Unlimited Hollow Purple ครับ"

### [04:00 – 04:30] ระบบ Backpack และ Items

> **[แสดงหน้าจอ: เปิด Backpack — ไอเทมต่าง ๆ]**

"กด Tab เพื่อเปิดกระเป๋าครับ ข้างในมีของที่ดรอปจากศัตรู
เช่น Soda กดใช้เพื่อฟื้น HP 1,000 มี Cooldown 3 วินาที
Subaru Shirt ฟื้นคืนชีพได้หนึ่งครั้ง
กด Q เพื่อเปิดโหมด Auto-use สำหรับการดื่ม Soda อัตโนมัติครับ"

---

## [04:30 – 07:30] การออกแบบโปรแกรม — OOP Concepts

### [04:30 – 05:15] Inheritance

> **[แสดงหน้าจอ: Slide UML หรือ โค้ดใน IDE]**

"มาดูการออกแบบโค้ดกันครับ
โปรเจกต์นี้ใช้ **Inheritance** เพื่อแบ่งปัน Attribute และ Behavior ร่วมกัน

ตัวอย่างเช่น ลำดับชั้นของ Weapon ครับ
Weapon เป็น Abstract Class ที่เก็บ Field ร่วมอย่าง level, maxLevel, cooldown และ icon
แล้วอาวุธแต่ละชนิดอย่าง Standard, Blue, Red, Cleave ต่างสืบทอดมาจาก Weapon
และ Evolved Weapon อย่าง MaximumBlue ก็ขยายมาจาก Blue อีกทีนึงครับ

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
โดยไม่ต้อง Cast ให้ยุ่งยากครับ"

### [06:00 – 07:00] Polymorphism

> **[แสดงหน้าจอ: โค้ด GameManager — Weapon Loop และ Item Loop]**

"**Polymorphism** ถูกใช้ใน GameManager อย่างชัดเจนครับ
ในส่วน Game Loop มี Loop ที่วน weaponList
แล้วเรียก weapon.use(dt) บน Weapon แต่ละตัว
แต่ละ Weapon Override use() ด้วยพฤติกรรมต่างกัน
Standard ยิง Cursor เด้ง, Blue วางวงกลม, Cleave ฟันระยะประชิด
แต่ GameManager ไม่ต้องรู้ว่าเป็น Weapon ชนิดไหนเลยครับ

เช่นเดียวกับ accessory.procEffect() ใน Accessory Loop
SixEye เพิ่มขนาดอาวุธ, SukunaArm เพิ่มดาเมจ แต่ Loop เดียวกันทำงานได้ครับ

และ gameObject.copy() ที่ใช้ Prototype Pattern
ทำให้ spawn ศัตรูหรือ Item ใหม่โดยไม่ต้องรู้ Class จริงครับ"

### [07:00 – 07:30] Access Modifier และ Encapsulation

> **[แสดงหน้าจอ: โค้ด SoundManager — Singleton]**

"ในเรื่อง **Access Modifier** ครับ
Field ทุกตัวของ Entity เป็น private เข้าถึงผ่าน Getter/Setter
Method ภายใน GameManager อย่าง getRandomWeapon() เป็น private ไม่เปิดเผยออกนอก
และ SoundManager ใช้ **Singleton Pattern**
ด้วย private constructor และ public static getInstance()
เพื่อให้มี Instance เดียวตลอดโปรแกรมครับ"

---

## [07:30 – 08:30] JUnit Tests

> **[แสดงหน้าจอ: โฟลเดอร์ test/ ใน IDE — รัน Test]**

"มาดู **Unit Test** กันครับ
เราเขียน JUnit 5 Test สำหรับ Logic สำคัญของโปรแกรม

CharacterTest ทดสอบว่า receiveDamage() ลด HP ถูกต้อง
heal() ฟื้น HP ถูกต้องและไม่เกิน maxHP
isDead() เป็น true เมื่อ HP ≤ 0
และ I-frame ทำงานถูกต้องคือตัวละครไม่รับดาเมจซ้ำใน 0.2 วินาที

EnemyTest ทดสอบ receiveDamage() ของศัตรู
และตรวจสอบว่าทิศทางการเคลื่อนที่คำนวณถูกต้อง

InputManagerTest ทดสอบการรับ Key Press และ Key Release

รัน Test ด้วยคำสั่ง ./gradlew test ครับ"

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
utils สำหรับ CollisionChecker, SoundManager, SceneManager
และ vfx สำหรับ Visual Effect

ทุกอย่างรวมถึง Code, JAR, Report และ UML Diagram
อัปโหลดไว้บน GitHub เรียบร้อยแล้วครับ"

---

## [09:15 – 09:30] สรุปและจบ

> **[แสดงหน้าจอ: เกมรันอยู่ — ผู้เล่นกำลังสู้ศัตรู]**

"สรุปคือ Final Anime Survivor เป็นเกม Survivor ที่มีระบบซับซ้อน
มีอาวุธ 6 ชนิดพร้อม Evolved Form, Accessory 6 ชนิด
ระบบ Level Up, Chest, Backpack, Tile Map และ Collision Detection
ทั้งหมดออกแบบตามหลัก OOP อย่างเป็นระบบครับ

ขอบคุณที่รับชมครับ หากมีคำถามสามารถถามได้เลยครับ"

---

## หมายเหตุสำหรับการอัด

| ช่วงเวลา | หน้าจอที่ควรแสดง |
|---|---|
| 00:00–00:30 | Main Menu / Title Card |
| 00:30–01:00 | กด Play → Character Select |
| 01:00–02:30 | Gameplay: วิ่ง, สู้, เก็บ Orb |
| 02:30–03:15 | เมนู Level Up |
| 03:15–04:00 | เปิด Chest |
| 04:00–04:30 | เปิด Backpack, ใช้ Soda |
| 04:30–05:15 | Slide/IDE: Inheritance UML |
| 05:15–06:00 | IDE: entityInterface/ folder |
| 06:00–07:00 | IDE: GameManager weapon loop |
| 07:00–07:30 | IDE: SoundManager singleton |
| 07:30–08:30 | IDE: Test files + รัน Test |
| 08:30–09:15 | GitHub repo / Project structure |
| 09:15–09:30 | Gameplay / End screen |
