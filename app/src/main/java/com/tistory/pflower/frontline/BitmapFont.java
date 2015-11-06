package com.tistory.pflower.frontline;


import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import org.andengine.entity.sprite.TiledSprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.util.Log;

public class BitmapFont {

	private enum FontType {
		USES_CHARSET, USES_CHAR_ARRAY
	}

	private FontType mType;
	private TiledTextureRegion mTexture;
	private Object mCharset;
	private VertexBufferObjectManager mVertManager;

	public BitmapFont(char[] charset, TiledTextureRegion texture, VertexBufferObjectManager vertManager) {
		this(new String(charset), texture, vertManager);
	}
	
	public BitmapFont(String charset, TiledTextureRegion texture, VertexBufferObjectManager vertManager) {
		this((Object)charset, texture, FontType.USES_CHAR_ARRAY, vertManager);
	}

	public BitmapFont(TiledTextureRegion texture, VertexBufferObjectManager vertManager) {
		this(Charset.defaultCharset(), texture,vertManager );
	}

	public BitmapFont(Charset charset, TiledTextureRegion texture, VertexBufferObjectManager vertManager) {
		this(charset, texture, FontType.USES_CHARSET, vertManager);
	}

	private BitmapFont(Object charset, TiledTextureRegion texture, FontType type, VertexBufferObjectManager vertManager) {
		this.mType = type;
		this.mCharset = charset;
		this.mTexture = texture;
		this.mVertManager = vertManager;
	}

	public TiledSprite[] getSpriteString(String text) {

		TiledSprite[] result = null;
		TiledSprite nextSprite;
		switch (mType) {
			case USES_CHARSET:
				try {
					Charset charset = (Charset) mCharset;
					ByteBuffer buff = charset.encode(text);
					int buffSize = buff.capacity();
					result = new TiledSprite[buffSize];
	
					for (int i = 0; i < buffSize; i++) {
						nextSprite = new TiledSprite(0, 0, mTexture.deepCopy(), mVertManager);
						nextSprite.setCurrentTileIndex((int) buff.get(i));
						result[i] = nextSprite;
					}
				} catch (ClassCastException cce) {
					Log.e("Error", cce.getMessage());
				}
			break;

			case USES_CHAR_ARRAY:
				try {
					String char_array = (String) mCharset;
					int textLength = text.length();
					result = new TiledSprite[textLength];
					for (int i = 0; i < textLength; i++) {
						nextSprite = new TiledSprite(0, 0, mTexture.deepCopy(), mVertManager);
						int nextCharIndex = char_array.indexOf(text.charAt(i));
						nextSprite.setCurrentTileIndex(nextCharIndex);
						result[i] = nextSprite;
					}
				} catch (ClassCastException cce) {
					Log.e("Error", cce.getMessage());
				}
			break;

			default:
				throw new UnsupportedCharsetException("Charset type not defined.");
		}

		return result;
	}

}
