package com.tistory.pflower.frontline.hud;

import java.io.IOException;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveByModifier;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSCounter;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import android.graphics.Typeface;
import android.util.Log;

import com.tistory.pflower.frontline.BaseActivity;
import com.tistory.pflower.frontline.BitmapFont;
import com.tistory.pflower.frontline.BitmapText;
import com.tistory.pflower.frontline.ResourceManager;
import com.tistory.pflower.frontline.object.Arm;
import com.tistory.pflower.frontline.object.Soldier;
import com.tistory.pflower.frontline.object.Torso;
import com.tistory.pflower.frontline.scene.GameScene;

public class HUDLayer extends HUD {

	public static final int HUD_HEIGHT = 100;
	public static final int SLOT_COUNT = 4;
	

	private HUDItem itemButtons[] = new HUDItem[SLOT_COUNT];
	private String itemName[] = new String[]{"pistol", "MG", "SG", "sna"};
	private HUDItem currentFocusedItem;
	private int currentItemFocus = 0;
	
	private Sprite dangerGlowing;
	private int glowingCnt = 0;
	
	private TiledSprite buyButton[] = new TiledSprite[SLOT_COUNT];
	
	private TiledSprite changeRightBtn;
	private TiledSprite changeLeftBtn;
	
	private Soldier character;
	private Arm arm;
	private Torso torso;
	private AnimatedSprite leg;

	private TiledSprite shield;
	private TiledSprite magazine;

	private BitmapText money;
	private IEntity textMoneyContainer;
	private IEntity textMeterContainer;
	private BitmapText meter;


	
	private Sound buy;
	private Sound click;
	
	private BitmapText kill;
	private IEntity textKillContainer;
	private BitmapText hit;
	private IEntity texthitContainer;
	private BuildableBitmapTextureAtlas gameTextureAtlas;
	private HUDItem frag;
	private boolean isDangerGlow;
	private GameScene scene;

    private Sprite heavyDam;
    private Sprite headshot;

    private Sprite gauge;

    private int headshotRemainTime;
    private int heavyDamRemainTime;


    @Override
	public void onDetached() 
	{
		gameTextureAtlas.unload();
	};
	
	public HUDLayer(final Soldier character, final GameScene scene)
	{
        this.scene = scene;
		this.arm = character.arms;
		this.torso = character.torso;
		this.leg = character.legs;
		this.character = character;
		
		
	     // �ݵ�� �ý��� ���̺귯�� �ε� �Ŀ� ȣ��
 		Log.d("HUD", "reload sound");
 		SoundFactory.setAssetBasePath("sound/hud/");
 		
 		BaseActivity activity = BaseActivity.getSharedInstance();
 		
 		try {
 			buy = SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, "Supply6"+ ".wav");
 			click = SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, "Click"+ ".wav");
 		} catch (IOException e1) {
 			// TODO Auto-generated catch block
 			e1.printStackTrace();
 		}
 		buy.setLooping(false);
 		click.setLooping(false);
 		
 		dangerGlowing = new Sprite(0, HUD_HEIGHT - BaseActivity.CAMERA_HEIGHT , ResourceManager.getSharedInstance().getResourceByName("danger"), BaseActivity.getSharedInstance().getVertexBufferObjectManager());
 		attachChild(dangerGlowing);
 		dangerGlowing.setVisible(false);

        heavyDam = new Sprite(0, HUD_HEIGHT - BaseActivity.CAMERA_HEIGHT , ResourceManager.getSharedInstance().getResourceByName("heavydam"), BaseActivity.getSharedInstance().getVertexBufferObjectManager());
        heavyDam.setPosition(5, HUD_HEIGHT - BaseActivity.CAMERA_HEIGHT + heavyDam.getHeight() * 4);
        attachChild(heavyDam);
        heavyDam.setVisible(false);

        headshot = new Sprite(0, HUD_HEIGHT - BaseActivity.CAMERA_HEIGHT , ResourceManager.getSharedInstance().getResourceByName("headshot"), BaseActivity.getSharedInstance().getVertexBufferObjectManager());
        headshot.setPosition(5, HUD_HEIGHT - BaseActivity.CAMERA_HEIGHT + heavyDam.getHeight() * 5);
        attachChild(headshot);
        headshot.setVisible(false);

        gauge = new Sprite(0, 0, ResourceManager.getSharedInstance().getResourceByName("gauge"), BaseActivity.getSharedInstance().getVertexBufferObjectManager());
        gauge.setPosition(BaseActivity.CAMERA_WIDTH - gauge.getWidth() - 32f, HUD_HEIGHT - BaseActivity.CAMERA_HEIGHT);
        gauge.setScaleX(2f);
        gauge.setScaleCenterY(0);
        attachChild(gauge);
        gauge.setVisible(false);

 		
		for (int i = 0; i < itemName.length; i ++) {
			itemButtons[i] = new HUDItem(0, 0, ResourceManager.getSharedInstance().getTiledResourceByName(itemName[i]), BaseActivity.getSharedInstance()
					.getVertexBufferObjectManager(), character, i, this, false)
			{
				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {	
					if(character.isDead)
						return true;
					
					if(pSceneTouchEvent.isActionDown())
					{
						buy.play();
						character.buyItem(currentItemFocus);
						setCurrentTileIndex(1);
					}
					if(pSceneTouchEvent.isActionUp())
						setCurrentTileIndex(0);
					return true;
				}
			};
			itemButtons[i].setScaleCenterY(0);
			itemButtons[i].setScale(2.0f);
			itemButtons[i].setPosition(BaseActivity.CAMERA_WIDTH / 2 - 1.0f * itemButtons[i].getWidth(), 0);
			itemButtons[i].setCurrentTileIndex(0);
			itemButtons[i].setVisible(false);
			
			attachChild(itemButtons[i]);
			registerTouchArea(itemButtons[i]);
		}
		
		currentFocusedItem = itemButtons[0];
		currentFocusedItem.setVisible(true);
		
		changeLeftBtn = new TiledSprite(0, 0, ResourceManager.getSharedInstance().getTiledResourceByName("arrow"), BaseActivity.getSharedInstance()
				.getVertexBufferObjectManager())
		{
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(!isVisible())
					return true;
				if(character.isDead)
					return true;
				if(torso.isOnReload)
					return true;
				
				if(pSceneTouchEvent.isActionDown())
				{
					itemButtons[currentItemFocus].setVisible(false);
					currentItemFocus = (currentItemFocus + 1) % SLOT_COUNT;
					arm.changeWeapon(currentItemFocus);
					itemButtons[currentItemFocus].setVisible(true);
					currentFocusedItem = itemButtons[currentItemFocus];
					
					setCurrentTileIndex(1);
				}
				if(pSceneTouchEvent.isActionUp())
					setCurrentTileIndex(0);
				
				return true;
			}
		};
		changeLeftBtn.setScaleCenterY(0);
		changeLeftBtn.setScale(-2.0f, 2.0f);
		changeLeftBtn.setPosition(BaseActivity.CAMERA_WIDTH / 2 - 3.0f * changeLeftBtn.getWidth(), 0);
		
		
		changeRightBtn = new TiledSprite(0, 0, ResourceManager.getSharedInstance().getTiledResourceByName("arrow"), BaseActivity.getSharedInstance()
				.getVertexBufferObjectManager())
		{
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(!isVisible())
					return true;
				if(character.isDead)
					return true;
				if(torso.isOnReload)
					return true;
				
				if(pSceneTouchEvent.isActionDown())
				{
					itemButtons[currentItemFocus].setVisible(false);
					currentItemFocus = (currentItemFocus + SLOT_COUNT - 1) % SLOT_COUNT;
					arm.changeWeapon(currentItemFocus);
					itemButtons[currentItemFocus].setVisible(true);
					currentFocusedItem = itemButtons[currentItemFocus];
					
					setCurrentTileIndex(1);
				}
				if(pSceneTouchEvent.isActionUp())
					setCurrentTileIndex(0);
				
				return true;
			}
		};
		changeRightBtn.setScaleCenterY(0);
		changeRightBtn.setScale(2f);
		changeRightBtn.setPosition(BaseActivity.CAMERA_WIDTH / 2 + 1.0f * changeRightBtn.getWidth(), 0);
		
		
		attachChild(changeLeftBtn);
		registerTouchArea(changeLeftBtn);
		
		attachChild(changeRightBtn);
		registerTouchArea(changeRightBtn);

				

		
		shield = new HUDItem(0, 0, ResourceManager.getSharedInstance().getTiledResourceByName("shield"), BaseActivity.getSharedInstance()
				.getVertexBufferObjectManager())
		{
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {	
				if(character.isDead)
					return true;
				if(torso.isOnReload)
					return true;
				if(pSceneTouchEvent.isActionDown())
				{
					shield.setCurrentTileIndex(1);
					arm.setVisible(false);
					torso.shieldUp();
				}
				if(pSceneTouchEvent.isActionUp())
				{
					shield.setCurrentTileIndex(0);
					torso.setDefault();
					character.torso.setShieldOn(false);
					arm.setVisible(true);
				}
				return true;
			}
		};
		shield.setPosition(BaseActivity.CAMERA_WIDTH / 2 + 5.0f * shield.getWidth(), 0);
		shield.setCurrentTileIndex(0);
		
		magazine = new HUDItem(0, 0, ResourceManager.getSharedInstance().getTiledResourceByName("magazine"), BaseActivity.getSharedInstance()
				.getVertexBufferObjectManager())
		{
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {	
				if(character.isDead)
					return true;
				if(torso.isOnReload)
					return true;

                if(pSceneTouchEvent.isActionDown())
                {
                    magazine.setCurrentTileIndex(1);
                }
                if(pSceneTouchEvent.isActionUp())
                {
                    magazine.setCurrentTileIndex(0);
                    character.reload();
                }
				return true;
			}
		};
		magazine.setPosition(BaseActivity.CAMERA_WIDTH / 2 - 7.0f * magazine.getWidth(), 0);
		magazine.setCurrentTileIndex(0);

        frag = new HUDItem(0, 0, ResourceManager.getSharedInstance().getTiledResourceByName("frag"), BaseActivity.getSharedInstance()
                .getVertexBufferObjectManager(), character, 4, this, true)
        {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.isActionUp()) {
                    frag.setCurrentTileIndex(0);
                    return true;
                }
                if (character.isDead)
                    return true;
                if (torso.isOnReload)
                    return true;
                if (pSceneTouchEvent.isActionDown()) {
                    int old = arm.currentWeapon;
                    arm.changeWeapon(4);

                    frag.setCurrentTileIndex(1);
                    character.shoot();
                    arm.changeWeapon(old);
                }
                return true;
            }
        };
        frag.setScale(2f);
        frag.setPosition(BaseActivity.CAMERA_WIDTH / 2 - 7.0f * magazine.getWidth(), magazine.getHeightScaled());
        frag.setCurrentTileIndex(0);

		money = new BitmapText(BaseActivity.CAMERA_WIDTH / 2, HUD_HEIGHT - BaseActivity.CAMERA_HEIGHT + 20f,1f,-15f, Integer.toString(character.money),
        		ResourceManager.getSharedInstance().getFontByName("mPFont"), BaseActivity.getSharedInstance().getEngine(), ResourceManager.getSharedInstance().getResourceByName("ptsch"));
        textMoneyContainer = money.getText();
        //textMoneyContainer.setScale(2f);
        attachChild(textMoneyContainer);
        
        meter = new BitmapText(32f, HUD_HEIGHT - BaseActivity.CAMERA_HEIGHT + 20f,1f,-15f, Integer.toString((int)GameScene.maximumX),
        		ResourceManager.getSharedInstance().getFontByName("mMFont"), BaseActivity.getSharedInstance().getEngine(), ResourceManager.getSharedInstance().getResourceByName("m"));
        textMeterContainer = meter.getText();
        //textMeterContainer.setScale(2f);
        attachChild(textMeterContainer);
        
        kill = new BitmapText(32f, HUD_HEIGHT - BaseActivity.CAMERA_HEIGHT + 50f,1f,-15f, Integer.toString((int)GameScene.maximumX),
        		ResourceManager.getSharedInstance().getFontByName("mKFont"), BaseActivity.getSharedInstance().getEngine(), ResourceManager.getSharedInstance().getResourceByName("kill"));
        textKillContainer = kill.getText();
        //textKillContainer.setScale(2f);
        attachChild(textKillContainer);
        
        hit = new BitmapText(0, -240f,1f,-15f, Integer.toString((int)GameScene.maximumX),
        		ResourceManager.getSharedInstance().getFontByName("mHFont"), BaseActivity.getSharedInstance().getEngine(), ResourceManager.getSharedInstance().getResourceByName("Hit"));
        texthitContainer = hit.getText();
        texthitContainer.setPosition(32, HUD_HEIGHT - BaseActivity.CAMERA_HEIGHT + heavyDam.getHeight() * 5 + headshot.getHeight() + 1.5f);
        //texthitContainer.setScale(2f);
        attachChild(texthitContainer);

		setPosition(0, BaseActivity.CAMERA_HEIGHT - HUD_HEIGHT);
		
		attachChild(frag);
		attachChild(shield);
		attachChild(magazine);
		
		registerTouchArea(frag);
		registerTouchArea(shield);
		registerTouchArea(magazine);
		
		setTouchAreaBindingOnActionDownEnabled(true);

        final FPSCounter fpsCounter = new FPSCounter();
        BaseActivity.getSharedInstance().getEngine().registerUpdateHandler(fpsCounter);

        final Text text = new Text(300, -300, activity.mFont, "FPS:XXXXXX", BaseActivity.getSharedInstance().getVertexBufferObjectManager());

        registerUpdateHandler(new TimerHandler(1 / 20.0f, true, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {
                text.setText("FPS: " + String.format("%.2f", fpsCounter.getFPS()));
            }
        }));

        attachChild(text);
		
	}
	
	@Override
	protected void onManagedUpdate(float pSecondsElapsed) 
	{
		super.onManagedUpdate(pSecondsElapsed);

        if(--headshotRemainTime <= 0) {
            headshot.setVisible(false);
        }
        if(--heavyDamRemainTime <= 0){
            heavyDam.setVisible(false);
        }

		if(isDangerGlow)
		{
			if(glowingCnt > 60)
			{
                ResourceManager.getSharedInstance().getSoundByName("Hit4").setVolume(5.0f);
                ResourceManager.getSharedInstance().getSoundByName("Hit4").play();
				glowingCnt = 0;
			}
			dangerGlowing.setColor(1f, 1f, 1f, (255 - 200 * (glowingCnt / 60)) / 255f);
			++glowingCnt;
		}
		
		itemButtons[currentItemFocus].update();
		frag.update();
		
		money.setText(Integer.toString(character.money));
		kill.setText(Integer.toString(character.killCnt));

        if(scene.hitCnt > 0) {
            hit.setText(Integer.toString(scene.hitCnt));
            gauge.setVisible(true);
            texthitContainer.setVisible(true);

            gauge.setScaleY(scene.hitRateChar / (float)BaseActivity.FPS_LIMITS);
            if(scene.hitRateChar <= 1) {
                gauge.setVisible(false);
            }
        }
		else
            texthitContainer.setVisible(false);
		meter.setText(Integer.toString((int)GameScene.maximumX / 32));
	}

	public void setDangerGlowing(boolean b) {

		isDangerGlow = b;
		dangerGlowing.setVisible(b);
	};

    public boolean isHeavyDamShowing() {
        return heavyDam.isVisible();
    }
    public void showHeavyDam()
    {
        if(character.isFacingRight())
            heavyDam.setX(BaseActivity.getSharedInstance().CAMERA_WIDTH - heavyDam.getWidth());
        else
            heavyDam.setX(0);
        heavyDam.setVisible(true);
        heavyDamRemainTime = BaseActivity.FPS_LIMITS;
    }

    public boolean isHeadshotShowing() {
        return headshot.isVisible();
    }
    public void showHeadshot()
    {
        if(character.isFacingRight())
            headshot.setX(BaseActivity.getSharedInstance().CAMERA_WIDTH - headshot.getWidth());
        else
            headshot.setX(0);
        headshot.setVisible(true);
        headshotRemainTime = BaseActivity.FPS_LIMITS;
    }
}