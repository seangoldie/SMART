package algomusic2021.seangoldie.jsyn;

import com.jsyn.ports.UnitInputPort;
import com.jsyn.unitgen.UnitGenerator;
import com.jsyn.unitgen.UnitSink;
import com.softsynth.shared.time.TimeStamp;

public class CustomUnitSink implements UnitSink
{
	UnitInputPort input;
	UnitGenerator gen;
	
	
	@Override
	public UnitInputPort getInput() 
	{
		return input;
	}

	@Override
	public UnitGenerator getUnitGenerator() 
	{
		return gen;
	}

	@Override
	public void start() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start(TimeStamp timeStamp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop(TimeStamp timeStamp) {
		// TODO Auto-generated method stub
		
	}

}
