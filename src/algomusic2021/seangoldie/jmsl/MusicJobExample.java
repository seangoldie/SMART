package algomusic2021.seangoldie.jmsl;

import com.softsynth.jmsl.JMSL;
import com.softsynth.jmsl.MusicJob;
import com.softsynth.jmsl.SequentialCollection;
import com.softsynth.jmsl.UniformRandomBehavior;

public class MusicJobExample extends MusicJob
{
	public double repeat(double playTime)
	{
		System.out.print(this.getName() + "repeating at " + playTime);
		System.out.print(this.getName() + "repeated " + getRepeatCount() + "number of times");
		return playTime;
	}
	
	public static void main(String[] args)
	{
		MusicJobExample j1 = new MusicJobExample();
		j1.setRepeats(10);
		j1.setRepeatPause(1); // Another delay of 1
		j1.launch(JMSL.now());
		
		MusicJobExample j2 = new MusicJobExample();
		j2.setRepeats(10);
		j2.setRepeatPause(2);
		j2.launch(JMSL.now());
		
		// Parallel collections run jobs in parallel threads
//		ParallelCollection parCol = new ParallelCollection();
//		parCol.add(j1);
//		parCol.add(j2);
//		parCol.setRepeats(3);
//		parCol.setRepeatPause(0.5);

		// Sequential collections run jobs in sequential threads (below, j1 then j2)
		SequentialCollection seqCol = new SequentialCollection();
		seqCol.setBehavior(new UniformRandomBehavior());
		seqCol.add(j1);
		seqCol.add(j2);
		seqCol.setRepeats(5);
		seqCol.setRepeatPause(0.5);
	}
}
