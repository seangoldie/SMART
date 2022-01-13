package algomusic2021.seangoldie.jmsl;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.softsynth.jmsl.DimensionNameSpace;
import com.softsynth.jmsl.EventScheduler;
import com.softsynth.jmsl.JMSL;
import com.softsynth.jmsl.JMSLMixerContainer;
import com.softsynth.jmsl.MusicShape;
import com.softsynth.jmsl.jsyn2.JSynMusicDevice;
import com.softsynth.jmsl.jsyn2.JSynUnitVoiceInstrument;
import com.softsynth.jmsl.util.EventDistributions;
import com.softsynth.jmsl.view.MusicShapeEditor;

import algomusic2021.seangoldie.jsyn.voices.ChowningPairSustainUpdated;

public final class ChowningPairInstrument
{
	JMSLMixerContainer mixer;
	JSynUnitVoiceInstrument instrument;
	
	public JSynUnitVoiceInstrument getInstrument()
	{
		return instrument;
	}
	
	public JMSLMixerContainer getMixer()
	{
		return mixer;
	}
	
	public DimensionNameSpace getDimensionNameSpace()
	{
		return instrument.getDimensionNameSpace();
	}
	
	public void setDimensionNameSpaceValues(MusicShape shape, int times)
	{
		// Custom algorithm to generate random values within the bounds of the limits for each parameter
		// Can use any random or semi-random method here, as long as it is bounded 0-1
		
		for (int i=0; i<times; i++)
		{
			double[] values = new double[6];
			
			for (int j=0; j<6; j++)
			{
				double low = shape.getLowLimit(j);
				double high = shape.getHighLimit(j);
				
				// This is not very interesting, just a bounded random value:
				values[j] = Math.random() * (high - low) + low;
				
				// However, I simply could not figure out how to keep the other EventDistributions-based
				// values bounded to within the high and low limits. Clipping them didn't seem to work -
				// almost like the code wasn't even running
			}
			shape.add(values);
		}
	}
	
	private void initJMSL()
	{
		JMSL.scheduler = new EventScheduler();
		JMSL.scheduler.start();
		JMSL.clock.setAdvance(0.2);
	}
	
	private void initMusicDevices()
	{
		JSynMusicDevice.instance().open();
	}
	
	private void initMixer()
	{
		mixer = new JMSLMixerContainer();
		mixer.start();
	}
	
	private void buildInstrument()
	{
		ChowningPairSustainUpdated voice = new ChowningPairSustainUpdated();
		instrument = new JSynUnitVoiceInstrument( 2, voice.getClass().getName() ); // Two voices overlaid - the MusicShape seems to autogenerate values for each of them
		mixer.addInstrument(instrument);
	}
	
	private void init()
	{
		initJMSL();
		initMusicDevices();
		initMixer();
		buildInstrument();
	}
	
	public static void main(String[] args)
	{
		JFrame jFrame = new JFrame("Close to quit");
		jFrame.setLayout(new BorderLayout());
		jFrame.addWindowListener(
			new WindowAdapter() 
			{
				public void windowClosing(WindowEvent e) 
				{
					JMSL.closeMusicDevices();
					System.exit(0);
				}
			}
		);
		
		ChowningPairInstrument instance = new ChowningPairInstrument();
		instance.init();
		
		MusicShape shape = new MusicShape( instance.getDimensionNameSpace() );
		shape.setLimits(0, 0, 2); // Limit duration maximum to 2 sec - getting somewhat boring results with default 8
		shape.setLimits(5, 0, 2); // Limit distortion to 2x - getting really crazy amplitude ranges with default 10
		instance.setDimensionNameSpaceValues(shape, 16); // Random within limits
		shape.setInstrument( instance.getInstrument() );
		shape.setRepeats(5);
		shape.print();
		
		MusicShapeEditor shapeEditor = new MusicShapeEditor();
		shapeEditor.addMusicShape(shape);
		
		jFrame.add( BorderLayout.NORTH, shapeEditor.getComponent() );
		jFrame.add( BorderLayout.SOUTH, instance.getMixer().getPanAmpControlPanel() ); // PanAmpControlPanel gives you mixer controls
		jFrame.pack();
		jFrame.setVisible(true);
		shape.launch( JMSL.now() + JMSL.clock.getAdvance() );	
	}
}
