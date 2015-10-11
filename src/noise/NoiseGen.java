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
package noise;

import static noise.Interpolation.*;

public class NoiseGen {

	static final int X_NOISE_GEN = 1619;
	static final int Y_NOISE_GEN = 31337;
	static final int Z_NOISE_GEN = 6971;
	static final int SEED_NOISE_GEN = 1013;
	static final int SHIFT_NOISE_GEN = 8;

	public static float makeInt32Range(float n) {
		if (n >= 1073741824.0f) {
			return (2.0f * fmod(n, 1073741824.0f)) - 1073741824.0f;
		} else if (n <= -1073741824.0) {
			return (2.0f * fmod(n, 1073741824.0f)) + 1073741824.0f;
		} else {
			return n;
		}
	}

	public static float fmod(float x, float y) {
		if (y == 0.0)
			return Float.NaN;
		return x % y;
	}

	public static final float gradientCoherentNoise3D(float x, float y,
			float z, int seed, NoiseQuality noiseQuality) {
		// Create a unit-length cube aligned along an integer boundary. This
		// cube
		// surrounds the input point.
		int x0 = (x > 0.0 ? (int) x : (int) x - 1);
		int x1 = x0 + 1;
		int y0 = (y > 0.0 ? (int) y : (int) y - 1);
		int y1 = y0 + 1;
		int z0 = (z > 0.0 ? (int) z : (int) z - 1);
		int z1 = z0 + 1;

		// Map the difference between the coordinates of the input value and the
		// coordinates of the cube's outer-lower-left vertex onto an S-curve.
		float xs = 0, ys = 0, zs = 0;
		switch (noiseQuality) {
		case QUALITY_FAST:
			xs = (x - x0);
			ys = (y - y0);
			zs = (z - z0);
			break;
		case QUALITY_STD:
			xs = scurve3(x - x0);
			ys = scurve3(y - y0);
			zs = scurve3(z - z0);
			break;
		case QUALITY_BEST:
			xs = scurve5(x - x0);
			ys = scurve5(y - y0);
			zs = scurve5(z - z0);
			break;
		}

		// Now calculate the noise values at each vertex of the cube. To
		// generate
		// the coherent-noise value at the input point, interpolate these eight
		// noise values using the S-curve value as the interpolant (trilinear
		// interpolation.)
		float n0, n1, ix0, ix1, iy0, iy1;
		n0 = gradientNoise3D(x, y, z, x0, y0, z0, seed);
		n1 = gradientNoise3D(x, y, z, x1, y0, z0, seed);
		ix0 = linearInterp(n0, n1, xs);
		n0 = gradientNoise3D(x, y, z, x0, y1, z0, seed);
		n1 = gradientNoise3D(x, y, z, x1, y1, z0, seed);
		ix1 = linearInterp(n0, n1, xs);
		iy0 = linearInterp(ix0, ix1, ys);
		n0 = gradientNoise3D(x, y, z, x0, y0, z1, seed);
		n1 = gradientNoise3D(x, y, z, x1, y0, z1, seed);
		ix0 = linearInterp(n0, n1, xs);
		n0 = gradientNoise3D(x, y, z, x0, y1, z1, seed);
		n1 = gradientNoise3D(x, y, z, x1, y1, z1, seed);
		ix1 = linearInterp(n0, n1, xs);
		iy1 = linearInterp(ix0, ix1, ys);

		return linearInterp(iy0, iy1, zs);
	}

	public static final float gradientNoise3D(double fx, double fy, double fz,
			int ix, int iy, int iz, int seed) {
		// Randomly generate a gradient vector given the integer coordinates of
		// the
		// input value. This implementation generates a random number and uses
		// it
		// as an index into a normalized-vector lookup table.
		int vectorIndex = (X_NOISE_GEN * ix + Y_NOISE_GEN * iy + Z_NOISE_GEN
				* iz + SEED_NOISE_GEN * seed) & 0xffffffff;
		vectorIndex ^= (vectorIndex >> SHIFT_NOISE_GEN);
		vectorIndex &= 0xff;

		double xvGradient = VectorTable.g_randomVectors[(vectorIndex << 2)];
		double yvGradient = VectorTable.g_randomVectors[(vectorIndex << 2) + 1];
		double zvGradient = VectorTable.g_randomVectors[(vectorIndex << 2) + 2];

		// Set up us another vector equal to the distance between the two
		// vectors
		// passed to this function.
		double xvPoint = (fx - ix);
		double yvPoint = (fy - iy);
		double zvPoint = (fz - iz);

		// Now compute the dot product of the gradient vector with the distance
		// vector. The resulting value is gradient noise. Apply a scaling value
		// so that this noise value ranges from -1.0 to 1.0.
		return (float) (((xvGradient * xvPoint) + (yvGradient * yvPoint) + (zvGradient * zvPoint)) * 2.12);
	}

	public static final int intValueNoise3D(int x, int y, int z, int seed) {
		// All constants are primes and must remain prime in order for this
		// noise
		// function to work correctly.
		int n = (X_NOISE_GEN * x + Y_NOISE_GEN * y + Z_NOISE_GEN * z + SEED_NOISE_GEN
				* seed) & 0x7fffffff;
		n = (n >> 13) ^ n;
		return (n * (n * n * 60493 + 19990303) + 1376312589) & 0x7fffffff;
	}

	public static final float valueCoherentNoise3D(float x, float y, float z,
			int seed, NoiseQuality quality) {
		// Create a unit-length cube aligned along an integer boundary. This
		// cube
		// surrounds the input point.
		int x0 = (x > 0.0 ? (int) x : (int) x - 1);
		int x1 = x0 + 1;
		int y0 = (y > 0.0 ? (int) y : (int) y - 1);
		int y1 = y0 + 1;
		int z0 = (z > 0.0 ? (int) z : (int) z - 1);
		int z1 = z0 + 1;

		// Map the difference between the coordinates of the input value and the
		// coordinates of the cube's outer-lower-left vertex onto an S-curve.
		float xs = 0, ys = 0, zs = 0;
		switch (quality) {
		case QUALITY_FAST:
			xs = (x - x0);
			ys = (y - y0);
			zs = (z - z0);
			break;
		case QUALITY_STD:
			xs = scurve3(x - x0);
			ys = scurve3(y - y0);
			zs = scurve3(z - z0);
			break;
		case QUALITY_BEST:
			xs = scurve5(x - x0);
			ys = scurve5(y - y0);
			zs = scurve5(z - z0);
			break;
		}

		// Now calculate the noise values at each vertex of the cube. To
		// generate
		// the coherent-noise value at the input point, interpolate these eight
		// noise values using the S-curve value as the interpolant (trilinear
		// interpolation.)
		float n0, n1, ix0, ix1, iy0, iy1;
		n0 = valueNoise3D(x0, y0, z0, seed);
		n1 = valueNoise3D(x1, y0, z0, seed);
		ix0 = linearInterp(n0, n1, xs);
		n0 = valueNoise3D(x0, y1, z0, seed);
		n1 = valueNoise3D(x1, y1, z0, seed);
		ix1 = linearInterp(n0, n1, xs);
		iy0 = linearInterp(ix0, ix1, ys);
		n0 = valueNoise3D(x0, y0, z1, seed);
		n1 = valueNoise3D(x1, y0, z1, seed);
		ix0 = linearInterp(n0, n1, xs);
		n0 = valueNoise3D(x0, y1, z1, seed);
		n1 = valueNoise3D(x1, y1, z1, seed);
		ix1 = linearInterp(n0, n1, xs);
		iy1 = linearInterp(ix0, ix1, ys);
		return linearInterp(iy0, iy1, zs);
	}

	public static final float valueNoise3D(int x, int y, int z, int seed) {
		return 1.0f - (intValueNoise3D(x, y, z, seed) / 1073741824.0f);
	}
	
	/**
	 * Clamps a value onto a clamping range.
	 * @param value The value to clamp.
	 * @param lowerBound The lower bound of the clamping range.
	 * @param upperBound The upper bound of the clamping range.
	 * @return <em>value</em> if <em>value</em> lies between <em>lowerBound</em> and <em>upperBound</em>.<br>
	 * <em>lowerBound</em> if <em>value</em> is less than <em>lowerBound</em><br>
	 * <em>upperBound</em> if <em>value</em> is greater than <em>upperBound</em>
	 */
	public static final int clampValue(int value, int lowerBound, int upperBound){
		if (value < lowerBound) {
		      return lowerBound;
		    } else if (value > upperBound) {
		      return upperBound;
		    } else {
		      return value;
		    }
	}
}
