package algomusic2021.seangoldie.jsyn;

import com.jsyn.ports.UnitInputPort;
import com.jsyn.unitgen.UnitFilter;

public class FuzzFilter extends UnitFilter 
{

	private UnitInputPort inputPort;
	
	public FuzzFilter() 
	{
		addPort(inputPort = new UnitInputPort("inputPort"));
		inputPort.setup(0, 0.5, 1.0);
	}
	
	public UnitInputPort getInputPort()
	{
		return inputPort;
	}
	
	@Override
	public void generate(int start, int limit) 
	{
		// Let's grab the signal
		double[] inputs = input.getValues();
		double[] outputs = output.getValues();
		double[] other = inputPort.getValues();
		
		for (int i=start; i<limit; i++) 
		{
			double sample = inputs[i];
			double otherValue = other[i];
			outputs[i] = sample + ( Math.pow(sample, otherValue) );
		}
	}
}
