package com.tistory.pflower.frontline.object;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.tistory.pflower.frontline.object.Leg.Direction;

public class Leg extends AnimatedSprite {
	
	public enum Direction
	{
		DESENDING_RIGHT,
		DESENDING_LEFT,
		PLAIN
	}

	private Direction desending;
	private long durationF[] = {100, 100, 100, 100, 100, 100, 100, 100, 100};
	private long durationS[] = {100, 100, 100, 100, 100, 100, 100, 100, 100};
	
	public Leg(int i, int j, TiledTextureRegion _Arms,
			VertexBufferObjectManager vertexBufferObjectManager, Soldier character) {
		super(i, j, _Arms, vertexBufferObjectManager);
		
		animate(durationS, 0, 8, true);	
		setRotationCenter(10, 10);
		setScaleCenterX(getWidth() / 2);
		this.desending = Direction.PLAIN;
		
		setCullingEnabled(true);
	}
	

	public void setDesending(Direction desendingRight) {
		this.desending = desendingRight;
		
		if(desendingRight != Direction.PLAIN)
		{
			stopAnimation();
		}
		else
		{
			if(!isAnimationRunning())
				animate(durationF);
		}
	}


	public void moveFast() {
		animate(durationF, 0, 8, true);	
	}
	
	public void moveSlow() {
		animate(durationS, 0, 8, true);	
	}
}
