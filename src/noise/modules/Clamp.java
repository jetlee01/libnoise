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
 * Noise module that clamps the output value from a source module to a ange of
 * values.
 * <p>
 * 
 * The range of values in which to clamp the output value is called the
 * <i>clamping range</i>.
 * <p>
 * 
 * If the output value from the source module is less than the lower bound of
 * the clamping range, this noise module clamps that value to the lower bound.
 * If the output value from the source module is greater than the upper bound of
 * the clamping range, this noise module clamps that value to the upper bound.
 * <p>
 * 
 * To specify the upper and lower bounds of the clamping range, call the
 * {@link #setBounds} method.
 * <p>
 * 
 * This noise module requires one source module.
 * 
 */
public class Clamp extends Module {

	/** lower bound of the clamping range. */
	protected float lowerBound;
	/** upper bound of the clamping range. */
	protected float upperBound;

	/**
	 * Default lower bound of the clamping range for the Clamp noise module.
	 */
	public static final float DEFAULT_CLAMP_LOWER_BOUND = -1.0f;

	/**
	 * Default upper bound of the clamping range for the Clamp noise module.
	 */
	public static final float DEFAULT_CLAMP_UPPER_BOUND = 1.0f;

	protected Clamp(int sourceModuleCount) {
		super(sourceModuleCount);
	}

	/**
	 * Constructor.
	 * <p>
	 * The default lower bound of the clamping range is set to
	 * {@link #DEFAULT_CLAMP_LOWER_BOUND}.<br>
	 * The default upper bound of the clamping range is set to
	 * {@link #DEFAULT_CLAMP_LOWER_BOUND}.<br>
	 */
	public Clamp() {
		this(1);
	}

	/**
	 * Returns the lower bound of the clamping range.
	 * <p>
	 * If the output value from the source module is less than the lower bound
	 * of the clamping range, this noise module clamps that value to the lower
	 * bound.
	 */
	public float getLowerBound() {
		return lowerBound;
	}

	/**
	 * Returns the upper bound of the clamping range.
	 * <p>
	 * If the output value from the source module is greater than the upper
	 * bound of the clamping range, this noise module clamps that value to the
	 * upper bound.
	 */
	public float getUpperBound() {
		return upperBound;
	}

	/**
	 * Sets the lower and upper bounds of the clamping range.
	 * <p>
	 * The lower bound must be less than or equal to the upper bound.
	 * <p>
	 * If the output value from the source module is less than the lower bound
	 * of the clamping range, this noise module clamps that value to the lower
	 * bound. If the output value from the source module is greater than the
	 * upper bound of the clamping range, this noise module clamps that value to
	 * the upper bound.
	 * 
	 * @param lower
	 *            The lower bound.
	 * @param upper
	 *            The upper bound.
	 */
	public void setBounds(float lower, float upper) {
		assert lower <= upper;

		if (lower > upper) {
			throw new IllegalArgumentException();
		}

		lowerBound = lower;
		upperBound = upper;
	}

	@Override
	public float getValue(float x, float y, float z) {
		assert sourceModule != null;

		float value = sourceModule[0].getValue(x, y, z);
		if (value < lowerBound) {
			return lowerBound;
		} else if (value > upperBound) {
			return upperBound;
		} else {
			return value;
		}
	}

}
