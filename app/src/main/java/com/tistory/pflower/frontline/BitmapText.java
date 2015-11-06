package com.tistory.pflower.frontline;

import org.andengine.engine.Engine;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.opengl.texture.region.TextureRegion;

import android.util.Log;


public class BitmapText{

	private Engine mEngine;
	private AttachLetters mAttachRunnable;
	private float pX;
	private float linePadding;
	private float letterPadding;
	private float pY;
	private String text;
	private BitmapFont font;
	private Engine engine;
    TextureRegion texture;
	

	public BitmapText(String text, BitmapFont font, Engine engine, TextureRegion texture){
		this(0f, 0f, 0f, 0f, text, font, engine, texture);
	}

	public BitmapText(float pX, float pY, String text, BitmapFont font, Engine engine, TextureRegion texture){
		this(pX, pY, 0f, 0f, text, font, engine, texture);
	}

	public BitmapText(float pX, float pY, float letterPadding, float linePadding, String text, BitmapFont font, Engine engine, TextureRegion texture){
		this.pX = pX;
		this.pY = pY;
		this.letterPadding = letterPadding;
		this.linePadding = linePadding;
		this.text = text;
		this.font = font;
		this.engine = engine;
		mAttachRunnable = new AttachLetters(text, font, new Entity(pX, pY), letterPadding, linePadding, texture);
		this.mEngine = engine;
        this.texture = texture;
	}
	
	public IEntity getText(){
		mEngine.runOnUpdateThread(mAttachRunnable);
		IEntity container = mAttachRunnable.getContainer();
		//container.setInitialPosition();
		return container;
	}

    public float getWidth() { return mAttachRunnable.getWidth(); }

    public float getHeight() { return mAttachRunnable.getHeight(); }
	
	private class AttachLetters implements Runnable {
		
		public IEntity getContainer(){ return container; }
		
		private String text;
		private BitmapFont font;
		private IEntity container;
		private float letterPadding;
		private float linePadding;
        private float width;
        private float height;
        private TextureRegion texture;
		
		public AttachLetters(String text, BitmapFont font, IEntity container, float letterPadding, float linePadding, TextureRegion texture){
			this.text = text;
			this.font = font;
			this.container = container;
			this.letterPadding = letterPadding;
			this.linePadding = linePadding;
            this.texture = texture;
		}
		
		public void setText(String text)
		{
			this.text = text;
		}
		
		public String getText()
		{
			return text;
		}

        public float getWidth() { return width; }

        public float getHeight() { return height; }

		public void run() {
			String[] splitByLines = text.split("\n");
			container.detachChildren();
			int lineCount = splitByLines.length;
			TiledSprite nextChar;
			for(int line=0; line < lineCount; line++){
				TiledSprite[] spriteString = font.getSpriteString(splitByLines[line]);
                Sprite back = null;
                if(texture != null)
                    back = new Sprite(0, 0, texture, BaseActivity.getSharedInstance().getVertexBufferObjectManager());
				int letterCount = spriteString.length;
				if(letterCount > 0){
					float letterWidth = spriteString[0].getWidth()  + letterPadding;
					float lineHeight  = spriteString[0].getHeight() + linePadding;
					for(int charIndex=0; charIndex < letterCount; charIndex++){
                        width += letterWidth;
                        height += lineHeight;
						nextChar = spriteString[charIndex];
						nextChar.setPosition(charIndex*letterWidth, line*lineHeight);
						container.attachChild(nextChar);
					}
                    if(texture != null) {
                        back.setPosition(letterCount*letterWidth, line * lineHeight + lineHeight / 2);
                        container.attachChild(back);
				    }
                }
			}
		}
	}

	public void setText(String string) {
		if(mAttachRunnable.getText().equals(string)) 
			return;
		mAttachRunnable.setText(string);
		mEngine.runOnUpdateThread(mAttachRunnable);
	}
}
