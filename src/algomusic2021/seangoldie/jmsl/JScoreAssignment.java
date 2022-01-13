package algomusic2021.seangoldie.jmsl;

import com.softsynth.jmsl.Instrument;
import com.softsynth.jmsl.JMSL;
import com.softsynth.jmsl.JMSLRandom;
import com.softsynth.jmsl.MusicDevice;
import com.softsynth.jmsl.MusicShape;
import com.softsynth.jmsl.jsyn2.JSynMusicDevice;
import com.softsynth.jmsl.jsyn2.JSynUnitVoiceInstrument;
import com.softsynth.jmsl.score.Orchestra;
import com.softsynth.jmsl.score.Score;
import com.softsynth.jmsl.score.ScoreFrame;
import com.softsynth.jmsl.score.transcribe.BeatDivisionSchemeList;
import com.softsynth.jmsl.score.transcribe.Transcriber;

import algomusic2021.seangoldie.jsyn.voices.FilteredSawThreeOscVoice;

public class JScoreAssignment 
{
	Score score;
	ScoreFrame scoreFrame;

	Orchestra orchestra;
	int length = 100; // Change for a longer or shorter piece

	private void buildOrchestra() 
	{
		orchestra = new Orchestra();
		FilteredSawThreeOscVoice voice = new FilteredSawThreeOscVoice();
		Instrument voiceInstrument = new JSynUnitVoiceInstrument(1, voice.getClass().getName());
		orchestra.addInstrument(voiceInstrument, "ThreeOscSawVoice");
	}

	private void initMusicDevices() 
	{
		MusicDevice dev = JSynMusicDevice.instance();
		dev.edit(new java.awt.Frame());
		dev.open();
	}

	void build() 
	{
		JMSL.clock.setAdvance(0.1);
		initMusicDevices();

		buildOrchestra();

		score = new Score(1, 800, 500, "JScore Assignment");
		score.setScoreSubtitle("(and some Final Project debugging)");
		score.setOrchestra(orchestra);
		
		DemoPropertiesTransform transform = new DemoPropertiesTransform();

		transcribeAlgorithmicMelody();

		scoreFrame = new ScoreFrame();
		scoreFrame.addScore(score);
		scoreFrame.addNotePropertiesTransform(transform);
		scoreFrame.pack();

		scoreFrame.setVisible(true);
	}

	private void transcribeAlgorithmicMelody() 
	{
		MusicShape algoMel = new MusicShape(
				orchestra.getInstrument(0).getDimensionNameSpace()
				);

		for (int i=0; i<length; i++) 
		{
			// Set some values
			double dur = JMSLRandom.choose( 
					algoMel.getLowLimit(0), 
					algoMel.getHighLimit(0) 
					);

			double pitch = 60 + JMSLRandom.choose(24);
			
			double amp = 0.5;
			double hold = dur * Math.random();
			double filterFreq = JMSLRandom.choose(
					algoMel.getLowLimit(4),
					algoMel.getHighLimit(4)
					);
					
			double osc1Spread = JMSLRandom.choose(
					algoMel.getLowLimit(5),
					algoMel.getHighLimit(5)
					);
			
			double osc2Spread = JMSLRandom.choose(
					algoMel.getLowLimit(6),
					algoMel.getHighLimit(6)
					);
			
			// Add the values
			algoMel.add(algoMel.getDefaultArray());
			algoMel.set(dur, i, 0);
			algoMel.set(pitch, i, 1);
			algoMel.set(amp, i, 2);
			algoMel.set(hold, i, 3);
			algoMel.set(filterFreq, i, 4);
			algoMel.set(osc1Spread, i, 5);
			algoMel.set(osc2Spread, i, 6);
		}
		
		algoMel.integrate(0);

		BeatDivisionSchemeList.defaultSetup();
		Transcriber transcriber = new Transcriber();
		transcriber.setSourceMusicShape(algoMel);
		transcriber.setScore(score);
		score.rewind();
		try 
		{
			transcriber.transcribe();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static void main(String[] args) 
	{
		JScoreAssignment instance = new JScoreAssignment();
		instance.build();
	}
}
