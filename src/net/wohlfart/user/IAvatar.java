package net.wohlfart.user;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

public interface IAvatar {
	
	
	public void translationForce(float x, float y, float z);
		
	public void rotationForce(float xRot, float yRot, float zRot);

	public Vector3f getDirection();
	public Vector3f getLeft();
	public Vector3f getUp();
	
	public void actionClick(Vector2f vector2f);

	public Vector3f getWorldCoordinates(Vector2f vector2f, float f);

}
