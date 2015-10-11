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

public class Interpolation {

	/**
	 * Performs cubic interpolation between two values bound between two other
	 * values.
	 * <p>
	 * The alpha value should range from 0.0 to 1.0. If the alpha value is 0.0,
	 * this function returns @a n1. If the alpha value is 1.0, this function
	 * returns @a n2.
	 * 
	 * @param n0
	 *            The value before the first value.
	 * @param n1
	 *            The first value.
	 * @param n2
	 *            The second value.
	 * @param n3
	 *            The value after the second value.
	 * @param a
	 *            The alpha value.
	 * @return The interpolated value.
	 */
	public static final float cubicInterp(float n0, float n1, float n2,
			float n3, float a) {
		float p = (n3 - n2) - (n0 - n1);
		float q = (n0 - n1) - p;
		float r = n2 - n0;
		float s = n1;
		return p * a * a * a + q * a * a + r * a + s;
	}

	/**
	 * Performs linear interpolation between two values.
	 * <p>
	 * The alpha value should range from 0.0 to 1.0. If the alpha value is 0.0,
	 * this function returns <em>n0</em>. If the alpha value is 1.0, this
	 * function returns <em>n1</em>.
	 * 
	 * @param n0
	 *            The first value.
	 * @param n1
	 *            The second value.
	 * @param a
	 *            The alpha value.
	 * @return The interpolated value.
	 */
	public static final float linearInterp(float n0, float n1, float a) {
		return ((1.0f - a) * n0) + (a * n1);
	}

	/**
	 * Maps a value onto a cubic S-curve.
	 * <p>
	 * 
	 * The derivitive of a cubic S-curve is zero at a = 0.0 and a = 1.0.
	 * 
	 * @param a
	 *            The value to map onto a cubic S-curve. <em>a</em> should range
	 *            from 0.0 to 1.0.
	 * @return The mapped value.
	 */
	public static final float scurve3(float a) {
		return (a * a * (3.0f - 2.0f * a));
	}

	/**
	 * Maps a value onto a quintic S-curve.
	 * <p>
	 * The first derivitive of a quintic S-curve is zero at a = 0.0 and a = 1.0.
	 * <p>
	 * The second derivitive of a quintic S-curve is zero at a = 0.0 and a = 1.0
	 * 
	 * @param a
	 *            The value to map onto a quintic S-curve. <em>a</em> should
	 *            range from 0.0 to 1.0.
	 * @return The mapped value.
	 */
	public static final float scurve5(float a) {
		float a3 = a * a * a;
		float a4 = a3 * a;
		float a5 = a4 * a;
		return (6.0f * a5) - (15.0f * a4) + (10.0f * a3);
	}
}
