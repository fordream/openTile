package com.wooseok.music;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

public class SimpleNotePlayer extends Thread{
	public static final int OCTAVE_COUNT = 8; // 옥타브 수
	public static final String KEYS = "cdefgab"; // C Major 스케일 음계
	public static final String[] PITCHES = new String[]{"c", "cs", "d", "ds", "e", "f", "fs", "g", "gs", "a", "as", "b"}; // Chromatic 스케일 음계

	private final int[] DUMMY_BYTES = new int[]{
			0x4d, 0x54, 0x68, 0x64, 0x00, 0x00, 0x00, 0x06, 0x00, 0x01, 0x00, 0x02, 0x00, 0xc0, 0x4d, 0x54,
			0x72, 0x6b, 0x00, 0x00, 0x00, 0x2e, 0x00, 0xff, 0x03, 0x02, 0x61, 0x31, 0x00, 0xff, 0x01, 0x20,
			0x47, 0x65, 0x6e, 0x65, 0x72, 0x61, 0x74, 0x65, 0x64, 0x20, 0x62, 0x79, 0x20, 0x4e, 0x6f, 0x74,
			0x65, 0x57, 0x6f, 0x72, 0x74, 0x68, 0x79, 0x20, 0x43, 0x6f, 0x6d, 0x70, 0x6f, 0x73, 0x65, 0x72,
			0x00, 0xff, 0x2f, 0x00, 0x4d, 0x54, 0x72, 0x6b, 0x00, 0x00, 0x00, 0x22, 0x00, 0xff, 0x21, 0x01,
			0x00, 0x00, 0xff, 0x03, 0x02, 0x61, 0x31, 0x00, 0xc4, 0x01, 0x00, 0xb4, 0x07, 0x7f, 0x00, 0xb4,
			0x0a, 0x40, 0x00, 0x94, 0x39, 0x6e, 0x5e, 0x94, 0x39, 0x00, 0x00, 0xff, 0x2f, 0x00
	};
	// General MIDI Programs
	public static final String[] GM_PROGRAMS = new String[] {
			"Acoustic Grand Piano", "Bright Acoustic Piano",
			"Electric Grand Piano", "Honky-tonk Piano", "Electric Piano 1",
			"Electric Piano 2", "Harpsichord", "Clavinet", "Celesta",
			"Glockenspiel", "Music Box", "Vibraphone", "Marimba", "Xylophone",
			"Tubular Bells", "Dulcimer", "Drawbar Organ", "Percussive Organ",
			"Rock Organ", "Church Organ", "Reed Organ", "Accordion",
			"Harmonica", "Tango Accordion", "Acoustic Guitar (nylon)",
			"Acoustic Guitar (steel)", "Electric Guitar (jazz)",
			"Electric Guitar (clean)", "Electric Guitar (muted)",
			"Overdriven Guitar", "Distortion Guitar", "Guitar harmonics",
			"Acoustic Bass", "Electric Bass (finger)", "Electric Bass (pick)",
			"Fretless Bass", "Slap Bass 1", "Slap Bass 2", "Synth Bass 1",
			"Synth Bass 2", "Violin", "Viola", "Cello", "Contrabass",
			"Tremolo Strings", "Pizzicato Strings", "Orchestral Harp",
			"Timpani", "String Ensemble 1", "String Ensemble 2",
			"Synth Strings 1", "Synth Strings 2", "Choir Aahs", "Voice Oohs",
			"Synth Choir", "Orchestra Hit", "Trumpet", "Trombone", "Tuba",
			"Muted Trumpet", "French Horn", "Brass Section", "Synth Brass 1",
			"Synth Brass 2", "Soprano Sax", "Alto Sax", "Tenor Sax",
			"Baritone Sax", "Oboe", "English Horn", "Bassoon", "Clarinet",
			"Piccolo", "Flute", "Recorder", "Pan Flute", "Blown Bottle",
			"Shakuhachi", "Whistle", "Ocarina", "Lead 1 (square)",
			"Lead 2 (sawtooth)", "Lead 3 (calliope)", "Lead 4 (chiff)",
			"Lead 5 (charang)", "Lead 6 (voice)", "Lead 7 (fifths)",
			"Lead 8 (bass + lead)", "Pad 1 (new age)", "Pad 2 (warm)",
			"Pad 3 (polysynth)", "Pad 4 (choir)", "Pad 5 (bowed)",
			"Pad 6 (metallic)", "Pad 7 (halo)", "Pad 8 (sweep)", "FX 1 (rain)",
			"FX 2 (soundtrack)", "FX 3 (crystal)", "FX 4 (atmosphere)",
			"FX 5 (brightness)", "FX 6 (goblins)", "FX 7 (echoes)",
			"FX 8 (sci-fi)", "Sitar", "Banjo", "Shamisen", "Koto", "Kalimba",
			"Bag pipe", "Fiddle", "Shanai", "Tinkle Bell", "Agogo",
			"Steel Drums", "Woodblock", "Taiko Drum", "Melodic Tom",
			"Synth Drum", "Reverse Cymbal", "Guitar Fret Noise",
			"Breath Noise", "Seashore", "Bird Tweet", "Telephone Ring",
			"Helicopter", "Applause", "Gunshot" };

	private SoundPool sPool; // 사운드 풀
	private Map<String, Integer> sMap;
	private AudioManager mAudioManager;

	
	private int imgKeyWidth, programNo, octaveShift, trackNo;

	private Context mCtx;

	public void clear() {
		if(sMap != null)
			sMap.clear();
	}

	public SimpleNotePlayer(Context ctx, int instrumentNo, int trackNo) {
		mCtx = ctx;
		this.trackNo = trackNo;
		sPool = getSoundPool(instrumentNo);
		mAudioManager = (AudioManager)mCtx.getSystemService(mCtx.AUDIO_SERVICE);
	}

	public void playSound(final int soundKey, final float volume, final int rate) {
		new Runnable(){
			@Override
			public void run() {
				// C0 in this program is 0 -> A0 in this program is 9
				// A0 in general MIDI is 21
				// So, offset is 12
				if(soundKey == 0) return;
				sPool.play(soundKey-12, volume, volume, 0, 0, rate);
			}
		}.run();
	}

	private SoundPool getSoundPool(final int programNo){
		final SoundPool sPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				Map<String, Integer> tmpMap = new HashMap<String, Integer>();
				//미디 파일 생성
				try {
					createMidiFiles(programNo, 0);
				} catch (IOException e) {
					e.printStackTrace();
				}
				String dir = mCtx.getDir("", mCtx.MODE_PRIVATE).getAbsolutePath();
				for(int i=1;i<=OCTAVE_COUNT;i++){
					for (int j=0;j<PITCHES.length;j++){
						String soundPath = dir+File.separator+ trackNo + PITCHES[j]+i+".mid";
						tmpMap.put(PITCHES[j]+i, sPool.load(soundPath, 1));
					}
				}
				sMap = tmpMap;
			}
		});
		thread.start();
		return sPool;
	}

	public void createMidiFiles(int patch, int octaveShift) throws IOException{
		int pitch = 0x3c-36+(octaveShift*12);

		int[] bytes = DUMMY_BYTES;
		String[] pitches = SimpleNotePlayer.PITCHES;

		String dir = mCtx.getDir("", Context.MODE_PRIVATE).getAbsolutePath();

		for(int i=1;i<= SimpleNotePlayer.OCTAVE_COUNT;i++){
			for (int j=0;j<pitches.length;j++){
				File file = new File(dir+File.separator+ trackNo + pitches[j]+i+".mid");
				if(!file.exists())file.createNewFile();
				FileOutputStream fos = new FileOutputStream(file);
				bytes[100] = pitch;
				bytes[104] = pitch;
				bytes[89] = patch;

				for(int c=0;c<bytes.length;c++) fos.write(bytes[c]);
				fos.close();

				pitch++;
			}
		}
	}
}