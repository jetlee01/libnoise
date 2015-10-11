//
// Copyright (C) 2003, 2004 Jason Bevins
//
// This library is free software; you can redistribute it and/or modify it
// under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation; either version 2.1 of the License, or (at
// your option) any later version.
//
// This library is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
// License (COPYING.txt) for more details.
//
// You should have received a copy of the GNU Lesser General Public License
// along with this library; if not, write to the Free Software Foundation,
// Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
// The developer's email is jlbezigvins@gmzigail.com (for great email, take
// off every 'zig'.)
//
package noise.modules;

/**
 * Noise module that applies a scaling factor and a bias to the output value
 * from a source module.
 * <p>
 * The {@link #getValue(float, float, float)} method retrieves the output value
 * from the source module, multiplies it with a scaling factor, adds a bias to
 * it, then outputs the value.
 * <p>
 * This noise module requires <b>one</b> source module.
 * 
 * @author Jet Lee
 * 
 */
public class ScaleBias extends Module {

	/**
	 * Bias to apply to the scaled output value from the source module. Default
	 * valus is 0.0.
	 */
	protected float bias = 0;
	/**
	 * Scaling factor to apply to the output value from the source module.
	 * Default valus is 1.0.
	 */
	protected float scale = 1.0f;

	protected ScaleBias(int sourceModuleCount) {
		super(sourceModuleCount);
	}

	public ScaleBias() {
		this(1);
	}

	/**
	 * Returns the bias to apply to the scaled output value from the source
	 * module.
	 */
	public float getBias() {
		return bias;
	}

	/**
	 * Returns the scaling factor to apply to the output value from the source
	 * module.
	 */
	public float getScale() {
		return scale;
	}

	/**
	 * Sets the bias to apply to the scaled output value from the source module.
	 * 
	 * @param bias
	 *            The bias to apply.
	 */
	public void setBias(float bias) {
		this.bias = bias;
	}

	/**
	 * Sets the scaling factor to apply to the output value from the source
	 * module.
	 * 
	 * @param scale
	 *            The scaling factor to apply.
	 */
	public void setScale(float scale) {
		this.scale = scale;
	}

	@Override
	public float getValue(float x, float y, float z) {
		assert (sourceModule[0] != null);

		return sourceModule[0].getValue(x, y, z) * scale + bias;
	}

}
