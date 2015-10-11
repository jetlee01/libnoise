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

import noise.NoiseGen;

/**
 * Noise module that outputs Voronoi cells.
 * <p>
 * In mathematics, a <i>Voronoi cell</i> is a region containing all the points
 * that are closer to a specific <i>seed point</i> than to any other seed point.
 * These cells mesh with one another, producing polygon-like formations.
 * <p>
 * By default, this noise module randomly places a seed point within each unit
 * cube. By modifying the <i>frequency</i> of the seed points, an application
 * can change the distance between seed points. The higher the frequency, the
 * closer together this noise module places the seed points, which reduces the
 * size of the cells. To specify the frequency of the cells, call the
 * {@link #setFrequency} method.
 * <p>
 * This noise module assigns each Voronoi cell with a random constant value from
 * a coherent-noise function. The <i>displacement value</i> controls the range
 * of random values to assign to each cell. The range of random values is +/-
 * the displacement value. Call the {@link #setDisplacement} method to specify
 * the displacement value.
 * <p>
 * To modify the random positions of the seed points, call the {@link #setSeed}
 * method.
 * <p>
 * This noise module can optionally add the distance from the nearest seed to
 * the output value. To enable this feature, call the {@link #enableDistance}
 * method. This causes the points in the Voronoi cells to increase in value the
 * further away that point is from the nearest seed point.
 * <p>
 * Voronoi cells are often used to generate cracked-mud terrain formations or
 * crystal-like textures.
 * <p>
 * This noise module requires no source modules.
 * 
 */
public class Voronoi extends Module {

	/**
	 * Scale of the random displacement to apply to each Voronoi cell. Default
	 * value is 1.0.
	 */
	protected float displacement = 1.0f;
	/**
	 * Determines if the distance from the nearest seed point is applied to the
	 * output value. Default value is false.
	 */
	protected boolean enableDistance = false;
	/** Frequency of the seed points. Default value is 1.0. */
	protected float frequency = 1.0f;
	/**
	 * Seed value used by the coherent-noise function to determine the positions
	 * of the seed points.Default value is 0.
	 */
	protected int seed = 0;

	protected Voronoi(int sourceModuleCount) {
		super(sourceModuleCount);
	}

	public Voronoi() {
		this(0);
	}

	/**
	 * Enables or disables applying the distance from the nearest seed point to
	 * the output value.
	 * <p>
	 * Applying the distance from the nearest seed point to the output value
	 * causes the points in the Voronoi cells to increase in value the further
	 * away that point is from the nearest seed point. Setting this value to @a
	 * true (and setting the displacement to a near-zero value) causes this
	 * noise module to generate cracked mud formations.
	 * 
	 * @param enable
	 *            Specifies whether to apply the distance to the output value or
	 *            not.
	 */
	public void enableDistance(boolean enable) {
		this.enableDistance = enable;
	}

	/**
	 * Enables applying the distance from the nearest seed point to the output
	 * value.
	 * 
	 * @see #enableDistance(boolean)
	 */
	public void enableDistance() {
		enableDistance = true;
	}

	/**
	 * Returns the displacement value of the Voronoi cells.
	 * <p>
	 * This noise module assigns each Voronoi cell with a random constant value
	 * from a coherent-noise function. The <i>displacement value</i> controls
	 * the range of random values to assign to each cell. The range of random
	 * values is +/- the displacement value.
	 */
	public float getDisplacement() {
		return displacement;
	}

	/**
	 * Returns the frequency of the seed points.
	 * <p>
	 * The frequency determines the size of the Voronoi cells and the distance
	 * between these cells.
	 */
	public float getFrequency() {
		return frequency;
	}

	/**
	 * Returns the seed value used by the Voronoi cells.
	 * <p>
	 * The positions of the seed values are calculated by a coherent-noise
	 * function. By modifying the seed value, the output of that function
	 * changes.
	 */
	public int getSeed() {
		return seed;
	}

	/**
	 * Determines if the distance from the nearest seed point is applied to the
	 * output value.
	 * <p>
	 * Applying the distance from the nearest seed point to the output value
	 * causes the points in the Voronoi cells to increase in value the further
	 * away that point is from the nearest seed point.
	 * 
	 * @return true if the distance is applied to the output value, false
	 *         otherwise.
	 */
	public boolean isDistanceEnabled() {
		return enableDistance;
	}

	/**
	 * Sets the displacement value of the Voronoi cells.
	 * <p>
	 * This noise module assigns each Voronoi cell with a random constant value
	 * from a coherent-noise function. The <i>displacement value</i> controls
	 * the range of random values to assign to each cell. The range of random
	 * values is +/- the displacement value.
	 * 
	 * @param displacement
	 *            The displacement value of the Voronoi cells.
	 */
	public void setDisplacement(float displacement) {
		this.displacement = displacement;
	}

	/**
	 * Sets the frequency of the seed points.
	 * <p>
	 * The frequency determines the size of the Voronoi cells and the distance
	 * between these cells.
	 * 
	 * @param frequency
	 *            The frequency of the seed points.
	 */
	public void setFrequency(float frequency) {
		this.frequency = frequency;
	}

	/**
	 * Sets the seed value used by the Voronoi cells.
	 * <p>
	 * The positions of the seed values are calculated by a coherent-noise
	 * function. By modifying the seed value, the output of that function
	 * changes.
	 * 
	 * @param seed
	 *            The seed value.
	 */
	public void setSeed(int seed) {
		this.seed = seed;
	}

	@Override
	public float getValue(float x, float y, float z) {
		// This method could be more efficient by caching the seed values. Fix
		// later.

		x *= frequency;
		y *= frequency;
		z *= frequency;

		int xInt = (x > 0.0 ? (int) x : (int) x - 1);
		int yInt = (y > 0.0 ? (int) y : (int) y - 1);
		int zInt = (z > 0.0 ? (int) z : (int) z - 1);

		float minDist = 2147483647;
		float xCandidate = 0;
		float yCandidate = 0;
		float zCandidate = 0;

		// Inside each unit cube, there is a seed point at a random position. Go
		// through each of the nearby cubes until we find a cube with a seed
		// point that is closest to the specified position.
		for (int zCur = zInt - 2; zCur <= zInt + 2; zCur++) {
			for (int yCur = yInt - 2; yCur <= yInt + 2; yCur++) {
				for (int xCur = xInt - 2; xCur <= xInt + 2; xCur++) {

					// Calculate the position and distance to the seed point
					// inside of this unit cube.
					float xPos = xCur
							+ NoiseGen.valueNoise3D(xCur, yCur, zCur, seed);
					float yPos = yCur
							+ NoiseGen.valueNoise3D(xCur, yCur, zCur, seed + 1);
					float zPos = zCur
							+ NoiseGen.valueNoise3D(xCur, yCur, zCur, seed + 2);
					float xDist = xPos - x;
					float yDist = yPos - y;
					float zDist = zPos - z;
					float dist = xDist * xDist + yDist * yDist + zDist * zDist;

					if (dist < minDist) {
						// This seed point is closer to any others found so far,
						// so record this seed point.
						minDist = dist;
						xCandidate = xPos;
						yCandidate = yPos;
						zCandidate = zPos;
					}
				}
			}
		}

		float value;
		if (enableDistance) {
			// Determine the distance to the nearest seed point.
			float xDist = xCandidate - x;
			float yDist = yCandidate - y;
			float zDist = zCandidate - z;
			value = (float) ((Math.sqrt(xDist * xDist + yDist * yDist + zDist
					* zDist)) * 1.7320508075688772935 - 1.0f);
		} else {
			value = 0.0f;
		}

		// Return the calculated distance with the displacement value applied.
		return value
				+ (displacement * NoiseGen.valueNoise3D(
						(int) (Math.floor(xCandidate)),
						(int) (Math.floor(yCandidate)),
						(int) (Math.floor(zCandidate)), 0));
	}

}
