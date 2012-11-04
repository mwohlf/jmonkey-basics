package net.wohlfart.user;

import net.wohlfart.model.StellarSystem;

import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

public class AvatarImpl implements IAvatar {

    private final Camera camera;
    private final StellarSystem stellarSystem;

    private final Vector3f translationForce = new Vector3f();
    private final Quaternion rotationForce = new Quaternion();

    public AvatarImpl(final StellarSystem stellarSystem, final Camera camera) {
        this.camera = camera;
        this.stellarSystem = stellarSystem;
    }

    @Override
    public void translationForce(final float x, final float y, final float z) {
        translationForce.set(x, y, z);
        stellarSystem.move(translationForce);
    }

    // values in radians
    @Override
    public void rotationForce(final float xAngle, final float yAngle, final float zAngle) {

        Vector3f xAxis = camera.getLeft();
        Vector3f yAxis = camera.getUp();
        Vector3f zAxis = camera.getDirection();

        Matrix3f xMat = new Matrix3f();
        xMat.fromAngleNormalAxis(xAngle % FastMath.TWO_PI, xAxis.normalize());
        xMat.mult(xAxis, xAxis);
        xMat.mult(yAxis, yAxis);
        xMat.mult(zAxis, zAxis);

        Matrix3f yMat = new Matrix3f();
        yMat.fromAngleNormalAxis(yAngle % FastMath.TWO_PI, yAxis.normalize());
        yMat.mult(xAxis, xAxis);
        yMat.mult(yAxis, yAxis);
        yMat.mult(zAxis, zAxis);

        Matrix3f zMat = new Matrix3f();
        zMat.fromAngleNormalAxis(zAngle % FastMath.TWO_PI, zAxis.normalize());
        zMat.mult(xAxis, xAxis);
        zMat.mult(yAxis, yAxis);
        zMat.mult(zAxis, zAxis);

        rotationForce.fromAxes(xAxis, yAxis, zAxis);
        rotationForce.normalizeLocal();
        camera.setAxes(rotationForce);
    }

    @Override
    public Vector3f getDirection() {
        return camera.getDirection();
    }

    @Override
    public Vector3f getLeft() {
        return camera.getLeft();
    }

    @Override
    public Vector3f getUp() {
        return camera.getUp();
    }

    @Override
    public Vector3f getWorldCoordinates(final Vector2f vector2f, float f) {
        return camera.getWorldCoordinates(vector2f, f);
    }

    @Override
    public Vector3f getWorldTranslation() {
        return stellarSystem.getWorldTranslation();
    }

    @Override
    public void clickAction(final Vector2f click2d) {
        Vector3f click3d = getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
        Vector3f direction = getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
        // aim the ray from the clicked spot forwards.
        Ray ray = new Ray(click3d, direction);
        stellarSystem.actionClickRay(ray);
    }

}
