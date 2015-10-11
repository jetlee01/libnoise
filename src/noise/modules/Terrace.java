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

import noise.Interpolation;
import noise.NoiseGen;

/**
 * Noise module that maps the output value from a source module onto a
 * terrace-forming curve.
 * <p>
 * This noise module maps the output value from the source module onto a
 * terrace-forming curve. The start of this curve has a slope of zero; its slope
 * then smoothly increases. This curve also contains <i>control points</i> which
 * resets the slope to zero at that point, producing a "terracing" effect.
 * <p>
 * To add a control point to this noise module, call the
 * {@link #addControlPoint} method.
 * <p>
 * An application must add a minimum of two control points to the curve. If this
 * is not done, the {@link #getValue(float, float, float)} method fails. The
 * control points can have any value, although no two control points can have
 * the same value. There is no limit to the number of control points that can be
 * added to the curve.
 * <p>
 * This noise module clamps the output value from the source module if that
 * value is less than the value of the lowest control point or greater than the
 * value of the highest control point.
 * <p>
 * This noise module is often used to generate terrain features such as your
 * stereotypical desert canyon.
 * <p>
 * This noise module requires <b>one</b> source module.
 * 
 */
public class Terrace extends Module {

	/** Number of control points stored in this noise module. */
	protected int controlPointCount;
	/**
	 * Determines if the terrace-forming curve between all control points is
	 * inverted.
	 */
	protected boolean invertTerraces;
	/** Array that stores the control points. */
	protected float[] controlPoints;

	protected Terrace(int sourceModuleCount) {
		super(sourceModuleCount);
	}

	public Terrace() {
		this(1);
	}

	/**
	 * Adds a control point to the terrace-forming curve.
	 * <p>
	 * Two or more control points define the terrace-forming curve. The start of
	 * this curve has a slope of zero; its slope then smoothly increases. At the
	 * control points, its slope resets to zero.
	 * <p>
	 * It does not matter which order these points are added.
	 * 
	 * @param value
	 *            The value of the control point to add.
	 * @exception IllegalArgumentException
	 *                If an invalid parameter was specified; see the
	 *                preconditions for more information.
	 */
	public void addControlPoint(float value) {
		// Find the insertion point for the new control point and insert the new
		// point at that position. The control point array will remain sorted by
		// value.
		int insertionPos = findInsertionPos(value);
		insertAtPos(insertionPos, value);
	}

	/** Deletes all the control points on the terrace-forming curve. */
	public void clearAllControlPoints() {
		controlPoints = null;
		controlPointCount = 0;
	}

	/**
	 * Returns a pointer to the array of control points on the terrace-forming
	 * curve.
	 * <p>
	 * Before calling this method, call GetControlPointCount() to determine the
	 * number of control points in this array.
	 * <p>
	 * Two or more control points define the terrace-forming curve. The start of
	 * this curve has a slope of zero; its slope then smoothly increases. At the
	 * control points, its slope resets to zero.
	 * 
	 * @return
	 */
	public float[] getControlPointArray() {
		return controlPoints;
	}

	/**
	 * Returns the number of control points on the terrace-forming curve.
	 */
	public int getControlPointCount() {
		return controlPointCount;
	}

	/**
	 * Enables or disables the inversion of the terrace-forming curve between
	 * the control points.
	 * 
	 * @param invert
	 *            Specifies whether to invert the curve between the control
	 *            points.
	 */
	public void invertTerraces(boolean invert) {
		invertTerraces = invert;
	}

	public void invertTerraces() {
		invertTerraces = true;
	}

	/**
	 * Determines if the terrace-forming curve between the control points is
	 * inverted.
	 * 
	 * @return True if the curve between the control points is inverted. False
	 *         otherwise.
	 */
	public boolean isTerracesInverted() {
		return invertTerraces;
	}

	/**
	 * Creates a number of equally-spaced control points that range from -1 to
	 * +1.
	 * <p>
	 * he number of control points must be greater than or equal to 2.
	 * <p>
	 * The previous control points on the terrace-forming curve are deleted.
	 * <p>
	 * Two or more control points define the terrace-forming curve. The start of
	 * this curve has a slope of zero; its slope then smoothly increases. At the
	 * control points, its slope resets to zero.
	 * 
	 * @param controlPointCount
	 *            The number of control points to generate.
	 */
	public void makeControlPoints(int controlPointCount) {
		if (controlPointCount < 2) {
			throw new IllegalArgumentException();
		}

		clearAllControlPoints();

		float terraceStep = 2.0f / (controlPointCount - 1.0f);
		float curValue = -1.0f;
		for (int i = 0; i < (int) controlPointCount; i++) {
			addControlPoint(curValue);
			curValue += terraceStep;
		}
	}

	/**
	 * Determines the array index in which to insert the control point into the
	 * internal control point array.
	 * <p>
	 * No two control points have the same value.
	 * <p>
	 * By inserting the control point at the returned array index, this class
	 * ensures that the control point array is sorted by value. The code that
	 * maps a value onto the curve requires a sorted control point array.
	 * 
	 * @param value
	 *            The value of the control point.
	 * @return The array index in which to insert the control point.
	 * @exception IllegalArgumentException
	 *                If An invalid parameter was specified; see the
	 *                preconditions for more information.
	 */
	protected int findInsertionPos(float value) {
		int insertionPos;
		for (insertionPos = 0; insertionPos < controlPointCount; insertionPos++) {
			if (value < controlPoints[insertionPos]) {
				// We found the array index in which to insert the new control
				// point.
				// Exit now.
				break;
			} else if (value == controlPoints[insertionPos]) {
				// Each control point is required to contain a unique value, so
				// throw an exception.
				throw new IllegalArgumentException();
			}
		}
		return insertionPos;
	}

	/**
	 * 
	 Inserts the control point at the specified position in the internal
	 * control point array.
	 * <p>
	 * To make room for this new control point, this method reallocates the
	 * control point array and shifts all control points occurring after the
	 * insertion position up by one.
	 * <p>
	 * Because the curve mapping algorithm in this noise module requires that
	 * all control points in the array be sorted by value, the new control point
	 * should be inserted at the position in which the order is still preserved.
	 * 
	 * @param insertionPosThe
	 *            zero-based array position in which to insert the control
	 *            point.
	 * @param value
	 *            The value of the control point.
	 */
	protected void insertAtPos(int insertionPos, float value) {
		// Make room for the new control point at the specified position within
		// the control point array. The position is determined by the value of
		// the control point; the control points must be sorted by value within
		// that array.
		float[] newControlPoints = new float[controlPointCount + 1];
		for (int i = 0; i < controlPointCount; i++) {
			if (i < insertionPos) {
				newControlPoints[i] = controlPoints[i];
			} else {
				newControlPoints[i + 1] = controlPoints[i];
			}
		}
		controlPoints = newControlPoints;
		++controlPointCount;

		// Now that we've made room for the new control point within the array,
		// add the new control point.
		controlPoints[insertionPos] = value;
	}

	@Override
	public float getValue(float x, float y, float z) {
		assert (sourceModule[0] != null);
		assert (controlPointCount >= 2);

		// Get the output value from the source module.
		float sourceModuleValue = sourceModule[0].getValue(x, y, z);

		// Find the first element in the control point array that has a value
		// larger than the output value from the source module.
		int indexPos;
		for (indexPos = 0; indexPos < controlPointCount; indexPos++) {
			if (sourceModuleValue < controlPoints[indexPos]) {
				break;
			}
		}

		// Find the two nearest control points so that we can map their values
		// onto a quadratic curve.
		int index0 = NoiseGen
				.clampValue(indexPos - 1, 0, controlPointCount - 1);
		int index1 = NoiseGen.clampValue(indexPos, 0, controlPointCount - 1);

		// If some control points are missing (which occurs if the output value
		// from the source module is greater than the largest value or less than
		// the smallest value of the control point array), get the value of the
		// nearest control point and exit now.
		if (index0 == index1) {
			return controlPoints[index1];
		}

		// Compute the alpha value used for linear interpolation.
		float value0 = controlPoints[index0];
		float value1 = controlPoints[index1];
		float alpha = (sourceModuleValue - value0) / (value1 - value0);
		if (invertTerraces) {
			alpha = 1.0f - alpha;
			// SwapValues (value0, value1);
			float tmp = value0;
			value0 = value1;
			value1 = tmp;
		}

		// Squaring the alpha produces the terrace effect.
		alpha *= alpha;

		// Now perform the linear interpolation given the alpha value.
		return Interpolation.linearInterp(value0, value1, alpha);
	}

}
