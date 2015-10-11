package noise.modules;

//
//Copyright (C) 2003, 2004 Jason Bevins
//
//This library is free software; you can redistribute it and/or modify it
//under the terms of the GNU Lesser General Public License as published by
//the Free Software Foundation; either version 2.1 of the License, or (at
//your option) any later version.
//
//This library is distributed in the hope that it will be useful, but WITHOUT
//ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
//FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
//License (COPYING.txt) for more details.
//
//You should have received a copy of the GNU Lesser General Public License
//along with this library; if not, write to the Free Software Foundation,
//Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
//The developer's email is jlbezigvins@gmzigail.com (for great email, take
//off every 'zig'.)
//

/**
 * Noise module that maps the output value from a source module onto an
 * exponential curve.
 * <p>
 * 
 * Because most noise modules will output values that range from -1.0 to +1.0,
 * this noise module first normalizes this output value (the range becomes 0.0
 * to 1.0), maps that value onto an exponential curve, then rescales that value
 * back to the original range.
 * <p>
 * 
 * This noise module requires <b>one</b> source module.
 * 
 */
public class Exponent extends Module {

	/**
	 * Exponent to apply to the output value from the source module. Default
	 * value is 1.0.
	 */
	private float exponent = 1.0f;

	protected Exponent(int sourceModuleCount) {
		super(sourceModuleCount);
	}

	public Exponent() {
		this(1);
	}

	@Override
	public float getValue(float x, float y, float z) {
		assert (sourceModule[0] != null);

		float value = sourceModule[0].getValue(x, y, z);
		return (float) (Math.pow(Math.abs((value + 1.0) / 2.0), exponent) * 2.0 - 1.0);
	}

	/**
	 * Returns the exponent value to apply to the output value from the source
	 * module.
	 * 
	 * @return The exponent value.
	 */
	public float getExponent() {
		return exponent;
	}

	/**
	 * Sets the exponent value to apply to the output value from the source
	 * module.
	 * <p>
	 * 
	 * @param exponent
	 *            The exponent value.
	 */
	public void setExponent(float exponent) {
		this.exponent = exponent;
	}

}
