module application {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.media;

    opens application to javafx.fxml;

    exports application;
    exports core;
    exports entity;
    exports entity.accessory;
    exports entity.character;
    exports entity.enemy;
    exports entity.item;
    exports entity.item.craftable;
    exports entity.map;
    exports entity.misc;
    exports entity.weapon;
    exports entity.weapon.evolvedWeapon;
    exports entityInterface;
    exports entityInterface.itemInterface;
    exports entityInterface.weaponInterface;
    exports gui;
    exports gui.gameLayout;
    exports tile;
    exports utils;
    exports vfx;
}
