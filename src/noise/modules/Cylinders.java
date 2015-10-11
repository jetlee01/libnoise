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
 * Noise module that outputs concentric cylinders.
 * <p>
 * This noise module outputs concentric cylinders centered on the origin. These
 * cylinders are oriented along the y axis similar to the concentric rings of a
 * tree. Each cylinder extends infinitely along the y axis.
 * <p>
 * The first cylinder has a radius of 1.0. Each subsequent cylinder has a radius
 * that is 1.0 unit larger than the previous cylinder.
 * <p>
 * The output value from this noise module is determined by the distance between
 * the input value and the the nearest cylinder surface. The input values that
 * are located on a cylinder surface are given the output value 1.0 and the
 * input values that are equidistant from two cylinder surfaces are given the
 * output value -1.0.
 * <p>
 * An application can change the frequency of the concentric cylinders.
 * Increasing the frequency reduces the distances between cylinders. To specify
 * the frequency, call the {@link #setFrequency(float)} method.
 * <p>
 * This noise module, modified with some low-frequency, low-power turbulence, is
 * useful for generating wood-like textures.
 * <p>
 * This noise module does not require any source modules.
 */
public class Cylinders extends Module {

	/** Frequency of the concentric cylinders. Default value is 1.0 */
	protected float frequency = 1.0f;

	public Cylinders() {
		this(0);
	}

	protected Cylinders(int sourceModuleCount) {
		super(sourceModuleCount);
	}

	@Override
	public float getValue(float x, float y, float z) {
		x *= frequency;
		z *= frequency;

		double distFromCenter = Math.sqrt(x * x + z * z);
		double distFromSmallerSphere = distFromCenter - Math.floor(distFromCenter);
		double distFromLargerSphere = 1.0 - distFromSmallerSphere;
		double nearestDist = Math.min(distFromSmallerSphere, distFromLargerSphere);
		return (float) (1.0 - (nearestDist * 4.0)); // Puts it in the -1.0 to +1.0 range.
	}

	/**
	 * Returns the frequency of the concentric cylinders.
	 * <p>
	 * Increasing the frequency increases the density of the concentric
	 * cylinders, reducing the distances between them.
	 */
	public float getFrequency() {
		return frequency;
	}

	/**
	 * Sets the frequenct of the concentric cylinders.
	 * <p>
	 * Increasing the frequency increases the density of the concentric
	 * cylinders, reducing the distances between them.
	 * 
	 * @param frequency
	 *            The frequency of the concentric cylinders.
	 */
	public void setFrequency(float frequency) {
		this.frequency = frequency;
	}
}
