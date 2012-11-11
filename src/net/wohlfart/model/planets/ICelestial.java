package net.wohlfart.model.planets;

public interface ICelestial {

    public abstract float getRadius();

    public abstract CelestialType getType();

    public abstract float getRotSpeed();

    public abstract String getName();

    public abstract float getDistance();

}
