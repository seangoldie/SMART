package algomusic2021.seangoldie.jmsl;

import com.softsynth.jmsl.MusicShape;
import com.softsynth.jmsl.util.LinearInterpolator;

public class MusicShapeExercise 
{
	MusicShape shape;
	
	void buildMusicShape()
	{
		shape = new MusicShape(4);
		shape.setDimName(0, "Amplitude");
		
		shape.add(1,   2,   3,   4);
		shape.add(10,  20,  30,  40);
		shape.add(100, 200, 300, 400);
		
		var value = shape.get(1, 2);
		
		System.out.println(value);
		shape.set(value * 2, 1, 2);
		
		// To iterate over rows (elements) in the shape:
		
		for (int i=0; i<shape.size(); i++)
		{
			// To iterate over columns (dimensions) in the shape:
			
			for (int j=0; j<shape.dimension(); j++)
			{
				// Do something here I guess?
				// Set everything to zero
				
				shape.set(0.0, i, j);
			}
		}
		
		shape.print();
		
		MusicShape shape2 = new MusicShape(4);
		shape2.useStandardDimensionNameSpace();
		shape2.prefab();
		
		LinearInterpolator lerp = new LinearInterpolator(0, 0, 16, 1);
				
		for (int i=0; i<shape2.size(); i++)
		{
			// Crescendo over 17 elements from 0 to 1 in amplitude using lerp
			double currentAmp = lerp.interp(i);
			shape2.set(currentAmp, i, 0);
		}
		
		shape2.print();
		
		int pitchDim = shape2.getDimension("pitch");
		
	}
	
	public static void main(String[] args) 
	{
		MusicShapeExercise instance = new MusicShapeExercise();
		instance.buildMusicShape();
	}
}
