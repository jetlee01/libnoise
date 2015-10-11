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
 * Noise module that outputs concentric spheres.
 * <p>
 * This noise module outputs concentric spheres centered on the origin like the
 * concentric rings of an onion.
 * <p>
 * The first sphere has a radius of 1.0. Each subsequent sphere has a radius
 * that is 1.0 unit larger than the previous sphere.
 * <p>
 * The output value from this noise module is determined by the distance between
 * the input value and the the nearest spherical surface. The input values that
 * are located on a spherical surface are given the output value 1.0 and the
 * input values that are equidistant from two spherical surfaces are given the
 * output value -1.0.
 * <p>
 * An application can change the frequency of the concentric spheres. Increasing
 * the frequency reduces the distances between spheres. To specify the
 * frequency, call the {@link #setFrequency} method.
 * <p>
 * This noise module, modified with some low-frequency, low-power turbulence, is
 * useful for generating agate-like textures.
 * <p>
 * This noise module does not require any source modules.
 * 
 */
public class Spheres extends Module {

	/** Frequency of the concentric spheres. Default value is 1.0f. */
	private float frequency = 1.0f;

	protected Spheres(int sourceModuleCount) {
		super(sourceModuleCount);
	}

	public Spheres() {
		this(0);
	}

	/**
	 * Returns the frequency of the concentric spheres.
	 */
	public float getFrequency() {
		return frequency;
	}

	/**
	 * Sets the frequency of the concentric spheres.
	 * <p>
	 * Increasing the frequency increases the density of the concentric spheres,
	 * reducing the distances between them.
	 * 
	 * @param frequency
	 *            The frequency of the concentric spheres.
	 */
	public void setFrequency(float frequency) {
		this.frequency = frequency;
	}

	@Override
	public float getValue(float x, float y, float z) {
		x *= frequency;
		y *= frequency;
		z *= frequency;

		float distFromCenter = (float) Math.sqrt(x * x + y * y + z * z);
		float distFromSmallerSphere = distFromCenter
				- (float) Math.floor(distFromCenter);
		float distFromLargerSphere = 1.0f - distFromSmallerSphere;
		float nearestDist = Math.min(distFromSmallerSphere,
				distFromLargerSphere);
		return 1.0f - (nearestDist * 4.0f); // Puts it in the -1.0 to +1.0
											// range.
	}

}
