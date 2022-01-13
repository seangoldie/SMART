package algomusic2021.seangoldie.jsyn;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitInputPort;
import com.jsyn.swing.SoundTweaker;
import com.jsyn.unitgen.LineOut;

import algomusic2021.seangoldie.jsyn.voices.ChowningPairSustainUpdated;

public class Assignment2 
{

	public static void main(String[] args) 
	{
	
		final Synthesizer synth = JSyn.createSynthesizer();
		synth.start();
		
		ChowningPairSustainUpdated voice = new ChowningPairSustainUpdated();
		synth.add(voice);
		
		FuzzFilter filter = new FuzzFilter();
		
		
		LineOut out = new LineOut();
		synth.add(out);
		out.start(); 
		
		voice.getOutput().connect(0, filter.input, 0);
		
		filter.getOutput().connect(0, out.input, 0);
		filter.getOutput().connect(0, out.input, 1);

		JFrame jf = new JFrame();
		
		SoundTweaker tweaker = new SoundTweaker(synth, "Test voice", voice);
		jf.add(tweaker);

		jf.pack();
		jf.setVisible(true);
		
		jf.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent e) 
				{
					synth.stop();
					System.exit(0);
				}
			}
		);
	}
}
