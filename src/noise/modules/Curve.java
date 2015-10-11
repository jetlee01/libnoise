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
 * Noise module that maps the output value from a source module onto an
 * arbitrary function curve.
 * <p>
 * This noise module maps the output value from the source module onto an
 * application-defined curve. This curve is defined by a number of <i>control
 * points</i>; each control point has an <i>input value</i> that maps to an
 * <i>output value</i>.
 * <p>
 * To add the control points to this curve, call the {@link #addControlPoint}
 * method.
 * <p>
 * Since this curve is a cubic spline, an application must add a minimum of four
 * control points to the curve. If this is not done, the
 * {@link #getValue(float, float, float)} method fails. Each control point can
 * have any input and output value, although no two control points can have the
 * same input value. There is no limit to the number of control points that can
 * be added to the curve.
 * <p>
 * This noise module requires one source module.
 */
public class Curve extends Module {

	/** Number of control points on the curve. */
	protected int pointCount;
	/** Array that stores the control points. */
	protected ControlPoint[] controlPoints;

	protected Curve(int sourceModuleCount) {
		super(sourceModuleCount);
	}

	public Curve() {
		this(1);
	}

	/**
	 * Adds a control point to the curve.
	 * <p>
	 * No two control points have the same input value.
	 * <p>
	 * It does not matter which order these points are added.
	 * 
	 * @param inputValue
	 *            The input value stored in the control point.
	 * @param outputValue
	 *            The output value stored in the control point.
	 * 
	 * @exception IllegalArgumentException
	 *                If an invalid parameter was specified; see the
	 *                preconditions for more information.
	 */
	public void addControlPoint(float inputValue, float outputValue) {
		// Find the insertion point for the new control point and insert the new
		// point at that position. The control point array will remain sorted by
		// input value.
		int insertionPos = findInsertionPos(inputValue);
		insertAtPos(insertionPos, inputValue, outputValue);
	}

	/**
	 * Deletes all the control points on the curve.
	 */
	public void clearAllControlPoints() {
		controlPoints = null;
		pointCount = 0;
	}

	public ControlPoint[] getControlPointArray() {
		return controlPoints;
	}

	@Override
	public float getValue(float x, float y, float z) {
		assert sourceModule[0] != null;
		assert pointCount >= 4;

		// Get the output value from the source module.
		float sourceModuleValue = sourceModule[0].getValue(x, y, z);

		// Find the first element in the control point array that has an input
		// value
		// larger than the output value from the source module.
		int indexPos;
		for (indexPos = 0; indexPos < pointCount; indexPos++) {
			if (sourceModuleValue < controlPoints[indexPos].inputValue) {
				break;
			}
		}

		// Find the four nearest control points so that we can perform cubic
		// interpolation.
		int index0 = NoiseGen.clampValue(indexPos - 2, 0, pointCount - 1);
		int index1 = NoiseGen.clampValue(indexPos - 1, 0, pointCount - 1);
		int index2 = NoiseGen.clampValue(indexPos, 0, pointCount - 1);
		int index3 = NoiseGen.clampValue(indexPos + 1, 0, pointCount - 1);

		// If some control points are missing (which occurs if the value from
		// the
		// source module is greater than the largest input value or less than
		// the
		// smallest input value of the control point array), get the
		// corresponding
		// output value of the nearest control point and exit now.
		if (index1 == index2) {
			return controlPoints[index1].outputValue;
		}

		// Compute the alpha value used for cubic interpolation.
		float input0 = controlPoints[index1].inputValue;
		float input1 = controlPoints[index2].inputValue;
		float alpha = (sourceModuleValue - input0) / (input1 - input0);

		// Now perform the cubic interpolation given the alpha value.
		return Interpolation.cubicInterp(controlPoints[index0].outputValue,
				controlPoints[index1].outputValue,
				controlPoints[index2].outputValue,
				controlPoints[index3].outputValue, alpha);
	}

	/**
	 * Determines the array index in which to insert the control point into the
	 * internal control point array.
	 * <p>
	 * No two control points have the same input value.
	 * <p>
	 * By inserting the control point at the returned array index, this class
	 * ensures that the control point array is sorted by input value. The code
	 * that maps a value onto the curve requires a sorted control point array.
	 * 
	 * @param inputValue
	 *            The input value of the control point.
	 * @return The array index in which to insert the control point.
	 * @exception IllegalArgumentException
	 *                If an invalid parameter was specified; see the
	 *                preconditions for more information.
	 */
	protected int findInsertionPos(float inputValue) {
		int insertionPos;
		for (insertionPos = 0; insertionPos < pointCount; insertionPos++) {
			if (inputValue < controlPoints[insertionPos].inputValue) {
				// We found the array index in which to insert the new control
				// point.
				// Exit now.
				break;
			} else if (inputValue == controlPoints[insertionPos].inputValue) {
				// Each control point is required to contain a unique input
				// value, so throw an exception.
				throw new IllegalArgumentException();
			}
		}
		return insertionPos;
	}

	/**
	 * Inserts the control point at the specified position in the internal
	 * control point array.
	 * <p>
	 * To make room for this new control point, this method reallocates the
	 * control point array and shifts all control points occurring after the
	 * insertion position up by one.
	 * <p>
	 * Because the curve mapping algorithm used by this noise module requires
	 * that all control points in the array must be sorted by input value, the
	 * new control point should be inserted at the position in which the order
	 * is still preserved.
	 * 
	 * @param insertionPos
	 *            The zero-based array position in which to insert the control
	 *            point.
	 * @param inputValue
	 *            The input value stored in the control point.
	 * @param outputValue
	 *            The output value stored in the control point.
	 */
	protected void insertAtPos(int insertionPos, float inputValue,
			float outputValue) {
		// Make room for the new control point at the specified position within the
		// control point array.  The position is determined by the input value of
		// the control point; the control points must be sorted by input value
		// within that array.
		ControlPoint[] newControlPoints = new ControlPoint[pointCount + 1];
		for(int i = 0;i < pointCount;i++){
			if(i < insertionPos){
				newControlPoints[i] = controlPoints[i];
			}else{
				newControlPoints[i + 1] = controlPoints[i];
			}
		}
		
		controlPoints = newControlPoints;
		pointCount++;
		
		// Now that we've made room for the new control point within the array, add
		// the new control point.
		if(controlPoints[insertionPos] == null)
			controlPoints[insertionPos] = new ControlPoint();
		controlPoints[insertionPos].inputValue = inputValue;
		controlPoints[insertionPos].outputValue = outputValue;
	}

	/**
	 * This class defines a control point.
	 * <p>
	 * Control points are used for defining splines.
	 */
	class ControlPoint {
		/** The input value. */
		float inputValue;
		/** The output value that is mapped from the input value. */
		float outputValue;
	}
}
