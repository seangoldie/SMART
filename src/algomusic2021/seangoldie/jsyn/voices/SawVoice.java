package algomusic2021.seangoldie.jsyn.voices;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.Circuit;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.SawtoothOscillator;
import com.jsyn.unitgen.UnitGenerator;
import com.jsyn.unitgen.UnitVoice;
import com.softsynth.shared.time.TimeStamp;

public class SawVoice extends Circuit implements UnitVoice {

	private static SawtoothOscillator sawOsc;
	
	public SawVoice() {
		add(sawOsc = new SawtoothOscillator());
		sawOsc = new SawtoothOscillator();
		sawOsc.amplitude.set(0);
	}

	public UnitOutputPort getOutput() {
		return sawOsc.getOutput();
	}


	public UnitGenerator getUnitGenerator() {
		return sawOsc.getUnitGenerator();
	}

	public void noteOff(TimeStamp timeStamp) {
		sawOsc.amplitude.set(0, timeStamp);
	}

	public void noteOn(double frequency, double amplitude, TimeStamp timeStamp) {
		sawOsc.amplitude.set(amplitude, timeStamp);
	}

	public void setPort(String portName, double value, TimeStamp timeStamp) {
		
	}

	public void usePreset(int presetIndex) {
		
	}
	
	public static void main(int argc, String[] argv) {
		Synthesizer synth = JSyn.createSynthesizer();
		synth.start();
		
		LineOut out = new LineOut();
		synth.add(sawOsc);
	}

}
