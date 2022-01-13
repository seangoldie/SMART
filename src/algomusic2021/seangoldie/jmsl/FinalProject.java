package algomusic2021.seangoldie.jmsl;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;

import com.softsynth.jmsl.DimensionNameSpace;
import com.softsynth.jmsl.EventScheduler;
import com.softsynth.jmsl.JMSL;
import com.softsynth.jmsl.JMSLMixerContainer;
import com.softsynth.jmsl.JMSLRandom;
import com.softsynth.jmsl.MusicShape;
import com.softsynth.jmsl.jsyn2.JSynMusicDevice;
import com.softsynth.jmsl.jsyn2.JSynUnitVoiceInstrument;

import algomusic2021.seangoldie.jsyn.voices.FilteredSawThreeOscVoice;

public final class FinalProject
{
	/* SMART - Synthesized Musical Algorithms in Real Time
	 * Sean Goldie Algorithmic Composition Final Project Fall 2021, Professor
	 * Didkovsky, NYU
	 * 
	 * This program features a three-oscillator synthesizer playing chord tones. Its
	 * novelty is that it gives the user real-time control over the algorithmic
	 * parameters used in synthesis, in the form of a scalar that can be controlled
	 * from the GUI. The scalar is between 1 and 2 by default, and is multiplied against the
	 * generated values for:
	 * 
	 * Duration time 
	 * Amplitude 
	 * Hold 
	 * time LPF frequency 
	 * Osc 2 spread 
	 * Osc 3 spread
	 * 
	 * Thus, the scalar (and its control - currently a slider) represent the "chaos"
	 * in the system: a larger value directly leads to greater change between
	 * chords, and greater spread between simultaneous tones! The quality of the
	 * music becomes increasingly dissonant and stochastic as one moves away from
	 * more "musical" intervals as values for the scalar (thirds, fourths, and
	 * fifths for example - 1.2, 1.33, 1.5) but then comes "back around" to
	 * musicality when the next whole octave interval is reached (e.g. 2.0).
	 * 
	 * The system has the following modules that execute each repeat: 
	 * 1. A base note is selected by MIDI number and passed to Osc 1 (Osc 1 only ever plays the
	 * base musical tone) 
	 * 2. A set of ratios are selected (corresponding to major or minor tonality) 
	 * 3. The base note is translated into Hz, multiplied against the values in the ratio selected, 
	 * multiplied against the scalar, and passed to the other oscillators 
	 * 4. The other values are selected, multiplied by
	 * scalar, and passed to their destinations 
	 * 5. The next repeat time is selected
	 * 
	 *
	 * Next steps: Implement a CSV reader class and integrate it into setValues()
	 * (instead of just stochastic generation for values)
	 */

	// ===========================================================================

	/*
	 * Class-wide definitions for parameters. Change these if you want! Keep in mind
	 * that all of these will be scaled up by the user scalar
	 */

	double minorThird = 1.2;
	double majorThird = 1.25;
	double fifth = 1.5;
	int scalarMin = 1;
	int scalarMax = 2;
	double[][] intervalsToChooseFrom = { { majorThird, fifth }, { minorThird, fifth } };
	int osc2FreqSpreadDim, osc3FreqSpreadDim;
	double minSecondsRepeat = 0.5; // Minimum seconds between chords
	double maxSecondsRepeat = 2; // Maximum seconds between chords
	int programLength = 1000; // Number of chords that will sound in the program's lifetime
	int maximumChordLength = 2; // Maximum chord length in seconds
	int MIDINoteMin = 41; // Lowest MIDI note that will serve as base note
	int MIDINoteMax = 65; // Highest MIDI note that will serve as base note
	int polyphony = 4; // Polyphony of the synthesizer
	boolean loadDataFromFile = false; // Change to true if you want to load in data, false will be randomized
	String csvFilePath = "/Users/Sean/Documents/NYU_docs/Fall 2021/AlgoComp/Goldie_FinalProject/weather.csv";

	// ===========================================================================

	CSVReader csvReader;
	String fileName;
	JMSLMixerContainer mixer;
	JSynUnitVoiceInstrument instrument;
	JLabel valueLabel = new JLabel("Chaos value");

	// ===========================================================================

	public JSynUnitVoiceInstrument getInstrument()
	{
		return instrument;
	}

	public JLabel getValueLabel()
	{
		return valueLabel;
	}

	public JMSLMixerContainer getMixer()
	{
		return mixer;
	}

	public DimensionNameSpace getDimensionNameSpace()
	{
		return instrument.getDimensionNameSpace();
	}

	public double[] setValues(MusicShape shape, double scalar)
	{
		valueLabel.setText("Scalar=" + scalar);
		int dims = shape.getNumberOfNames();
		double[] values = new double[dims];
		double[] interval = intervalsToChooseFrom[(int) Math.round(Math.random())];
		double baseNote = JMSLRandom.choose(shape.getLowLimit(1), shape.getHighLimit(1));
		
		if (loadDataFromFile) // Use CSV reader to read in values
		{
			csvReader.readNextLine();
			values = csvReader.getValues();
		}
		
		else // Randomize values if not loading from file
		{
			for (int j = 0; j < dims; j++)
			{
				// Note setting
				if (j == 1)
				{
					values[j] = baseNote;
				}

				// Set OSC2 spread - apply intervals, apply scalar
				if (j == osc2FreqSpreadDim)
				{
					values[j] = interval[0] * scalar;
				}

				// Set OSC3 spread - apply intervals, apply scalar
				else if (j == osc3FreqSpreadDim)
				{
					values[j] = interval[1] * scalar;
				}

				else
				{
					values[j] = JMSLRandom.choose(shape.getLowLimit(j), shape.getHighLimit(j)) * scalar;
				}
			}
		}
		return values;
	}

	// ===========================================================================

	private void initJMSL()
	{
		JMSL.scheduler = new EventScheduler();
		JMSL.scheduler.start();
		JMSL.clock.setAdvance(0.5); // Give a long buffer time
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

	private void buildInstruments()
	{
		FilteredSawThreeOscVoice voice = new FilteredSawThreeOscVoice();

		instrument = new JSynUnitVoiceInstrument(polyphony, voice.getClass().getName());
		mixer.addInstrument(instrument);
	}

	private void init()
	{
		initJMSL();
		initMusicDevices();
		initMixer();
		buildInstruments();
		if (loadDataFromFile) csvReader = new CSVReader(fileName);
	}

	// ===========================================================================

	public static void main(String[] args)
	{
		JFrame jFrame = new JFrame("Synthesizing Musical Algorithms in Real Time");
		jFrame.setLayout(new BorderLayout());
		jFrame.setSize(500, 500);
		jFrame.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
				{
					JMSL.closeMusicDevices();
					System.exit(0);
				}
			});
		
		FinalProject instance = new FinalProject();

		// Slider to control octave spread. Need min to max, with double precision.
		// Sliders can only hold int so we give it values * 100 and divide by 100 in CustomMusicJob.
		JSlider jSlider = new JSlider(instance.scalarMin * 100, instance.scalarMax * 100);
		HarmonicDataModelSlider sliderWrapper = new HarmonicDataModelSlider(jSlider);

		jSlider.setValue(100); // Default to scalar of 1 - triad of tones in same register
		jSlider.setPreferredSize(new Dimension(1000, 100)); // Make him loooooong

		jFrame.getContentPane().add(BorderLayout.CENTER, jSlider);

		
		instance.init();

		jFrame.getContentPane().add(BorderLayout.SOUTH, instance.getValueLabel());

		DimensionNameSpace dns = instance.getDimensionNameSpace();

		MusicShape shape = new MusicShape(dns);

		shape.setLimits(1, instance.MIDINoteMin, instance.MIDINoteMax);
		shape.print();

		// Set limits to shorten time between chords
		shape.setLimits(0, 1, instance.programLength); // Limit duration
		shape.setLimits(3, 1, instance.programLength); // Limit hold

		instance.osc2FreqSpreadDim = shape.getDimension("Osc2FreqSpread");
		instance.osc3FreqSpreadDim = shape.getDimension("Osc3FreqSpread");

		CustomMusicJob mainJob = new CustomMusicJob(
				instance, 
				shape, 
				sliderWrapper, 
				instance.minSecondsRepeat, // Minimum seconds between repeats
				instance.maxSecondsRepeat // Maximum seconds between repeats
		);

		shape.setInstrument(instance.getInstrument());
		shape.print();
		mainJob.setRepeats(instance.programLength);
		mainJob.setInstrument(instance.getInstrument());
		
		jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		
		jFrame.pack();
		jFrame.setVisible(true);

		mainJob.launch(JMSL.now() + JMSL.clock.getAdvance());
		System.out.println("Launched successfully");
	}
}