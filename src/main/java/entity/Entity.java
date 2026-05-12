package entity;


import entityInterface.GameObject;

public abstract class Entity implements GameObject {

    private String name;
    private double mapX;
    private double mapY;

    public Entity() {
    }


    public double getMapY() {
        return mapY;
    }
    public void setMapY(double mapY) {
        this.mapY = mapY;
    }
    public double getMapX() {
        return mapX;
    }
    public void setMapX(double mapX) {
        this.mapX = mapX;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
