package algomusic2021.seangoldie.jmsl;

import javax.swing.JSlider;

public class HarmonicDataModelSlider implements HarmonicDataModel
{
	JSlider jSlider;
	
	public HarmonicDataModelSlider(JSlider jSlider) 
	{
        super();
        this.jSlider = jSlider;
    }

	public double getValue() 
	{
		return (double) jSlider.getValue() / 100;
	}

	@Override
	public double[] getValues()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
