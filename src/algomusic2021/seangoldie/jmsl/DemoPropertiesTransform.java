package algomusic2021.seangoldie.jmsl;

import com.softsynth.jmsl.JMSLRandom;
import com.softsynth.jmsl.score.Note;
import com.softsynth.jmsl.score.NotePropertiesTransform;
import com.softsynth.jmsl.score.Score;
import com.softsynth.jmsl.score.SelectionBuffer;

public class DemoPropertiesTransform extends NotePropertiesTransform
{
	// Class demo of transforms for score
	
	public DemoPropertiesTransform()
	{
		super();
		setName("Randomize all selected");
	}
	
	public void operate(Score score, SelectionBuffer selectionBuffer) 
	{
		for (int i=0; i<selectionBuffer.size(); i++)
		{
			Note n = (Note) selectionBuffer.get(i);
			n.setPitchData( Math.round(n.getPitchData() * JMSLRandom.choose(0.1, 2.0))); // idk arbitrary operation
		}
	}	
}
