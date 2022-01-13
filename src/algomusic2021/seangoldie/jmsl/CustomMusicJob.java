package algomusic2021.seangoldie.jmsl;

import com.softsynth.jmsl.JMSLRandom;
import com.softsynth.jmsl.MusicJob;
import com.softsynth.jmsl.MusicShape;

public class CustomMusicJob extends MusicJob
{
	FinalProject mainClass;
	MusicShape musicShape;
	HarmonicDataModel scalar;
	double minTime;
	double maxTime;
	
	public CustomMusicJob(
			FinalProject mainClass, 
			MusicShape musicShape, 
			HarmonicDataModel scalar,
			double minTime,
			double maxTime
			)
	{
		this.mainClass = mainClass;
		this.musicShape = musicShape;
		this.scalar = scalar;
		this.minTime = minTime;
		this.maxTime = maxTime;
	}
	
	public double start(double playTime)
	{
		// Slider goes between 0 and 100, so bring it down to 0-1 (octave spread)
		double[] dataToPlay = mainClass.setValues(musicShape, scalar.getValue());
		this.setRepeatPause(JMSLRandom.choose(minTime, maxTime));
		
		getInstrument().play(playTime, 1, dataToPlay);
		
		return playTime;
	}
	
	public double repeat(double playTime)
	{
		double[] dataToPlay = mainClass.setValues(musicShape, scalar.getValue());
		this.setRepeatPause(JMSLRandom.choose(minTime, maxTime) * scalar.getValue());
//		this.setRepeatPause(JMSLRandom.choose(minTime, maxTime));
		getInstrument().play(playTime, 1, dataToPlay);
		
		return playTime;
	}

}
