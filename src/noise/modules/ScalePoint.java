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
 * Noise module that scales the coordinates of the input value before returning
 * the output value from a source module.
 * <p>
 * 
 * The {@link #getValue(float, float, float)} method multiplies the ( x, y, z )
 * coordinates of the input value with a scaling factor before returning the
 * output value from the source module. To set the scaling factor, call the
 * {@link #setScale} method. To set the scaling factor to apply to the
 * individual x, y, or z coordinates, call the {@link #setXScale},
 * {@link #setYScale} or {@link #setZScale} methods, respectively.
 * <p>
 * This noise module requires <b>one</b> source module.
 */
public class ScalePoint extends Module {

	/**
	 * Scaling factor applied to the x coordinate of the input value. Default
	 * value is 1.0.
	 */
	protected float xScale = 1.0f;
	/**
	 * Scaling factor applied to the y coordinate of the input value. Default
	 * value is 1.0.
	 */
	protected float yScale = 1.0f;
	/**
	 * Scaling factor applied to the z coordinate of the input value. Default
	 * value is 1.0.
	 */
	protected float zScale = 1.0f;

	public ScalePoint() {
		this(1);
	}

	protected ScalePoint(int sourceModuleCount) {
		super(sourceModuleCount);
	}

	/**
	 * Returns the scaling factor applied to the x coordinate of the input
	 * value.
	 */
	public float getXScale() {
		return xScale;
	}

	/**
	 * Returns the scaling factor applied to the y coordinate of the input
	 * value.
	 */
	public float getYScale() {
		return yScale;
	}

	/**
	 * Returns the scaling factor applied to the z coordinate of the input
	 * value.
	 */
	public float getZScale() {
		return zScale;
	}

	/**
	 * Sets the scaling factor to apply to the input value.
	 * <p>
	 * 
	 * @param scale
	 *            The scaling factor to apply.
	 */
	public void setScale(float scale) {
		xScale = scale;
		yScale = scale;
		zScale = scale;
	}

	/**
	 * Sets the scaling factor to apply to the (x, y, z ) coordinates of the
	 * input value.
	 * <p>
	 * 
	 * @param xScale
	 *            The scaling factor to apply to the x coordinate.
	 * @param yScale
	 *            The scaling factor to apply to the x coordinate.
	 * @param zScale
	 *            The scaling factor to apply to the x coordinate.
	 */
	public void setScale(float xScale, float yScale, float zScale) {
		this.xScale = xScale;
		this.yScale = yScale;
		this.zScale = zScale;
	}

	/**
	 * Sets the scaling factor to apply to the x coordinate of the input value.
	 * 
	 * @param xScale
	 *            The scaling factor to apply to the x coordinate.
	 */
	public void setXScale(float xScale) {
		this.xScale = xScale;
	}

	/**
	 * Sets the scaling factor to apply to the y coordinate of the input value.
	 * 
	 * @param yScale
	 *            The scaling factor to apply to the y coordinate.
	 */
	public void setYScale(float yScale) {
		this.yScale = yScale;
	}

	/**
	 * Sets the scaling factor to apply to the z coordinate of the input value.
	 * 
	 * @param zScale
	 *            The scaling factor to apply to the z coordinate.
	 */
	public void setZScale(float zScale) {
		this.zScale = zScale;
	}

	@Override
	public float getValue(float x, float y, float z) {
		assert (sourceModule[0] != null);

		return sourceModule[0].getValue(x * xScale, y * yScale, z * zScale);
	}

}
