package algomusic2021.seangoldie.jsyn;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.data.SegmentedEnvelope;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.swing.SoundTweaker;
import com.jsyn.unitgen.Circuit;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.SineOscillator;
import com.jsyn.unitgen.UnitVoice;
import com.jsyn.unitgen.VariableRateMonoReader;
import com.softsynth.shared.time.TimeStamp;

/* Creating a circuit from code! Refer back to this if confused */

public class SineCircuit extends Circuit implements UnitVoice
{
	private SineOscillator sineOsc; // One oscillator for the circuit - declaration
	private SegmentedEnvelope segEnv; // Need this guy to hold envelope data - declaration
	private VariableRateMonoReader monoReader; // Need this guy to read the segEnv - declaration
	
	public SineCircuit() // Class constructor
	{
		add(sineOsc = new SineOscillator()); // sineOsc - initialization
		sineOsc.frequency.set(300);
		sineOsc.amplitude.set(0.5);
		add(monoReader = new VariableRateMonoReader()); // monoReader - initialization
		monoReader.output.connect(sineOsc.amplitude); // Connect the data reader to the amplitude
		buildSegmentedEnvelope();
	}
	
	private void buildSegmentedEnvelope() // Some data for each frame for the envelope
	// Format: pairs of data-points that are duration in seconds, and target value as a float
	{
		double[] envData = // Shorthand list initialization
			{ 
				0.1, 1.0, // Go up to 1.0 amplitude in 0.1 seconds
				0.2, 0.4, // Go down to 0.4 amplitude in 0.2 seconds
				0.1, 0.1, // Go down to 0.1 amplitude in 0.1 seconds
				0.3, 0.0  // Go down to 0.0 amplitude in 0.3 seconds
			};
		
		segEnv = new SegmentedEnvelope(envData); // Initialize the envelope, copies values into structure
		envData = null; // Don't need it anymore, values copied
		
		segEnv.setSustainBegin(1); // Start sustaining (looping) at frame 1
		segEnv.setSustainEnd(3);   // Stop loop point at frame 3
	}
	
	@Override
	public UnitOutputPort getOutput() 
	{	
		return sineOsc.output;
	}

	@Override
	public void noteOff(TimeStamp timeStamp) 
	{	
		monoReader.dataQueue.clear(timeStamp); // Clear the data buffer
		monoReader.dataQueue.queueOff(segEnv, true, timeStamp); 
		// Note off communicates with the mono reader, passing segEnv as queue-able data
		// True flag here stops the unit from continuing to calculate values (CPU saver)
	}

	@Override
	public void noteOn(double frequency, double amplitude, TimeStamp timeStamp) 
	{
		// Start the data queue for each note that is triggered
		sineOsc.frequency.set(frequency, timeStamp);
		monoReader.amplitude.set(amplitude, timeStamp);
		monoReader.dataQueue.clear(timeStamp); // Clear the data buffer
		monoReader.dataQueue.queueOn(segEnv, timeStamp);
	}
	
	// For testing this file as stand-alone:
	public static void main(String[] args) 
	{
		Synthesizer synth = JSyn.createSynthesizer();
		synth.start();
		
		SineCircuit oscCircuit = new SineCircuit();
		synth.add(oscCircuit);
		
		LineOut out = new LineOut();
		synth.add(out);
		
		oscCircuit.getOutput().connect(out);
		JFrame jf = new JFrame();
		
		SoundTweaker tweaker = new SoundTweaker(synth, "Test voice", oscCircuit);
		jf.add(tweaker);

		jf.pack();
		jf.setVisible(true);
		jf.addWindowListener( 
				new WindowAdapter() 
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
