package surge.math;

public class ArrayManipulator
{
	public static void shiftRight(double[] values, double rep)
	{
		double last = rep;

		for(int index = values.length - 2; index >= 0; index--)
		{
			values[index + 1] = values[index];
		}

		values[0] = last;
	}

	public static void wrapRight(double[] values)
	{
		double last = values[values.length - 1];
		shiftRight(values, last);
	}

	public static void fill(double[] values, double value)
	{
		for(int i = 0; i < values.length; i++)
		{
			values[i] = value;
		}
	}
}
