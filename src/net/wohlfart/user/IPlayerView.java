package net.wohlfart.user;

import com.jme3.math.Vector3f;

public interface IPlayerView {
	
	
	public void translationForce(float x, float y, float z);
		
	public void rotationForce(float xRot, float yRot, float zRot);

	public Vector3f getDirection();
	public Vector3f getLeft();
	public Vector3f getUp();

}
