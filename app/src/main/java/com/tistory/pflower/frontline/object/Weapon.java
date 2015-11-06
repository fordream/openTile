package com.tistory.pflower.frontline.object;

import java.io.IOException;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;

import com.tistory.pflower.frontline.BaseActivity;

import android.util.Log;

public class Weapon {
	
	enum RoundType
	{
		SINGLE_SHOT,
		MULTY_SHOT,
		GRANADE
	};
	
	String name;
	static int nextId = 0;
	public int id;
	public int ammo;
	public int magagine;
	public RoundType roundType;
	public int distance;
	
	public int MAX_AMMO;
	public int MAX_MAGAZINE;
	
	long[] frameDurration;
	int[] frames;
	
	Sound gunFire;

	public int delay;
	public int price;
	public int damage;
	
	public boolean IsPiece;
	public boolean IsInfinity;
	
	public int accurate;
	
	public Weapon(String name, int max_ammo, int max_magagine, long[] frameDurration, int[] frames, RoundType roundType,
			int distance, int delay, int price, int damage, boolean IsPiece, boolean IsInfinity, int accurate)
	{		
		this.id = nextId++;
		this.name = name;
		MAX_AMMO = max_ammo;
		MAX_MAGAZINE = max_magagine;
		this.frameDurration = frameDurration;
		this.frames = frames;
		this.roundType = roundType;
		this.distance = distance;
		this.delay = delay;
		this.price = price;
		this.damage = damage;
		this.IsPiece = IsPiece;
		this.IsInfinity = IsInfinity;
		this.accurate = accurate;
		reset();
	}
	public Weapon(Weapon weapon) {
		this.name = weapon.name;
		MAX_AMMO = weapon.MAX_AMMO;
		MAX_MAGAZINE = weapon.MAX_MAGAZINE;
		this.frameDurration = weapon.frameDurration;
		this.frames = weapon.frames;
		this.roundType = weapon.roundType;
		this.distance = weapon.distance;
		this.delay = weapon.delay;
		this.price = weapon.price;
		this.gunFire = weapon.gunFire;
		this.damage = weapon.damage;
		this.IsPiece = weapon.IsPiece;
		this.IsInfinity = weapon.IsInfinity;
		this.accurate = weapon.accurate;
		reset();
	}
	public static Weapon[] obtainNewWeaponSet()
	{
		Weapon[] c = new Weapon[weapons.length];

		for (int i = 0; i < weapons.length; i ++) {
			c[i] = new Weapon(weapons[i]);
		}
		return c;
	}
	
	public void reset()
	{
		ammo = MAX_AMMO;
		magagine = 0;
	}
	
	void loadResource()
	{
		// �ݵ�� �ý��� ���̺귯�� �ε� �Ŀ� ȣ��
		Log.d("Weapon", "load");
		SoundFactory.setAssetBasePath("sound/weapons/");
		BaseActivity activity = BaseActivity.getSharedInstance();

		try {
			gunFire = SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, name + "_fire"+ ".wav");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        gunFire.setVolume(0.5f);
		gunFire.setLooping(false);
	}
	
	public static void loadWeaponsResource()
	{
		for(int i = 0; i <weapons.length; i++ )
		{
			weapons[i].loadResource();
		}
	}
	
	static final Weapon weapons[] = { 
		new Weapon("Pistol", 14, 10, new long[]{100, 100, 100, 100}, new int[]{0, 3}, RoundType.SINGLE_SHOT, 400, 200, 0, 7, false, true, 80) , 
		new Weapon("Machine Gun", 30, 10, new long[]{100, 100, 100, 100}, new int[]{10, 13},  RoundType.SINGLE_SHOT, 400, 100, 100, 4, false, false, 70) , 
		new Weapon("Shot Gun", 5, 10, new long[]{100, 100, 100, 100, 100, 100, 100, 100}, new int[]{20, 27},  RoundType.MULTY_SHOT, 300, 800, 200, 10, false, false, 50) ,  
		new Weapon("Sniper Rifle", 8, 10, new long[]{100, 100, 100, 100, 100, 100, 100, 100, 100 }, new int[]{30, 38},  RoundType.SINGLE_SHOT, 500, 1200, 300, 5, true, false, 100) , 
		new Weapon("Frag Grenade", 14, 10, new long[]{100, 100, 100, 100, 100, 100, 100, 100, 100, 100}, new int[]{40, 49},  RoundType.GRANADE, 300, 800, 400, 100, false, false, 100) ,
		new Weapon("Pistol", 100, 9999, new long[]{100, 100, 100, 100}, new int[]{0, 3}, RoundType.SINGLE_SHOT, 500, 50, 99999, 50, false, true, 70) 
		};

	
	
}