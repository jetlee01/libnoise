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

/**
 * Noise module that outputs the value selected from one of two source modules
 * chosen by the output value from a control module.
 * <p>
 * Unlike most other noise modules, the index value assigned to a source module
 * determines its role in the selection operation:
 * <ul>
 * <li>Source module 0 (upper left in the diagram) outputs a value.
 * <li>Source module 1 (lower left in the diagram) outputs a value.
 * <li>Source module 2 (bottom of the diagram) is known as the <i>control
 * module</i>. The control module determines the value to select. If the output
 * value from the control module is within a range of values known as the
 * <i>selection range</i>, this noise module outputs the value from the source
 * module with an index value of 1. Otherwise, this noise module outputs the
 * value from the source module with an index value of 0.
 * </ul>
 * 
 * To specify the bounds of the selection range, call the {@link #setBounds}
 * method.
 * <p>
 * An application can pass the control module to the {@link #setControlModule}
 * method instead of the {@link #setSourceModule} method. This may make the
 * application code easier to read.
 * <p>
 * By default, there is an abrupt transition between the output values from the
 * two source modules at the selection-range boundary. To smooth the transition,
 * pass a non-zero value to the {@link #setEdgeFalloff} method. Higher values
 * result in a smoother transition.
 * <p>
 * This noise module requires <b>three</b> source modules.
 */
public class Select extends Module {

	/** Edge-falloff value. Default value is 0.0. */
	protected float edgeFalloff = 0.0f;
	/** Lower bound of the selection range. Default value is -1.0f. */
	protected float lowerBound = -1.0f;
	/** Upper bound of the selection range. Default value is 1.0f. */
	protected float upperBound = 1.0f;

	protected Select(int sourceModuleCount) {
		super(sourceModuleCount);
	}

	public Select() {
		this(3);
	}

	/**
	 * Returns the control module.
	 * <p>
	 * A control module has been added to this noise module via a call to
	 * {@link #setSourceModule(int, Module)} or {@link #setControlModule}.
	 * <p>
	 * The control module determines the output value to select. If the output
	 * value from the control module is within a range of values known as the
	 * <i>selection range</i>, the {@link #getValue(float, float, float)} method
	 * outputs the value from the source module with an index value of 1.
	 * Otherwise, this method outputs the value from the source module with an
	 * index value of 0.
	 * 
	 * @return A reference to the control module.
	 * @exception NullPointerException
	 *                If the control module is null.
	 */
	public Module getControlModule() {
		if (sourceModule == null || sourceModule[2] == null) {
			throw new NullPointerException();
		}
		return sourceModule[2];
	}

	/**
	 * Returns the falloff value at the edge transition.
	 * <p>
	 * The falloff value is the width of the edge transition at either edge of
	 * the selection range.
	 * <p>
	 * By default, there is an abrupt transition between the output values from
	 * the two source modules at the selection-range boundary.
	 */
	public float getEdgeFalloff() {
		return edgeFalloff;
	}

	/**
	 * Returns the lower bound of the selection range.
	 * <p>
	 */
	public float getLowerBound() {
		return lowerBound;
	}

	/**
	 * Returns the upper bound of the selection range.
	 */
	public float getUpperBound() {
		return upperBound;
	}

	/**
	 * Sets the lower and upper bounds of the selection range.
	 * <p>
	 * The lower bound must be less than or equal to the upper bound.
	 * 
	 * @param lowerBound
	 *            The lower bound.
	 * @param upperBound
	 *            The upper bound.
	 * 
	 * @exception IllegalArgumentException
	 *                An invalid parameter was specified; see the preconditions
	 *                for more information.
	 */
	public void setBounds(float lowerBound, float upperBound) {
		assert lowerBound < upperBound;

		this.lowerBound = lowerBound;
		this.upperBound = upperBound;

		// Make sure that the edge falloff curves do not overlap.
		setEdgeFalloff(edgeFalloff);
	}

	/**
	 * Sets the control module.
	 * <p>
	 * The control module determines the output value to select. If the output
	 * value from the control module is within a range of values known as the
	 * <i>selection range</i>, the {@link #getValue(float, float, float)} method
	 * outputs the value from the source module with an index value of 1.
	 * Otherwise, this method outputs the value from the source module with an
	 * index value of 0.
	 * <p>
	 * This method assigns the control module an index value of 2. Passing the
	 * control module to this method produces the same results as passing the
	 * control module to the {@link #setSourceModule(int, Module)} method while
	 * assigning that noise module an index value of 2.
	 * <p>
	 * This control module must exist throughout the lifetime of this noise
	 * module unless another control module replaces that control module.
	 * 
	 * @param controlModule
	 */
	public void setControlModule(Module controlModule) {
		assert sourceModule != null;
		sourceModule[2] = controlModule;
	}

	/**
	 * Sets the falloff value at the edge transition.
	 * <p>
	 * The falloff value is the width of the edge transition at either edge of
	 * the selection range.
	 * <p>
	 * By default, there is an abrupt transition between the values from the two
	 * source modules at the boundaries of the selection range.
	 * <p>
	 * For example, if the selection range is 0.5 to 0.8, and the edge falloff
	 * value is 0.1, then the {@link #getValue(float, float, float)} method
	 * outputs:
	 * <ul>
	 * <li>the output value from the source module with an index value of 0 if
	 * the output value from the control module is less than 0.4 ( = 0.5 - 0.1).
	 * <li>a linear blend between the two output values from the two source
	 * modules if the output value from the control module is between 0.4 ( =
	 * 0.5 - 0.1) and 0.6 ( = 0.5 + 0.1).
	 * <li>the output value from the source module with an index value of 1 if
	 * the output value from the control module is between 0.6 ( = 0.5 + 0.1)
	 * and 0.7 ( = 0.8 - 0.1).
	 * <li>a linear blend between the output values from the two source modules
	 * if the output value from the control module is between 0.7 ( = 0.8 - 0.1
	 * ) and 0.9 ( = 0.8 + 0.1).
	 * <li>the output value from the source module with an index value of 0 if
	 * the output value from the control module is greater than 0.9 ( = 0.8 +
	 * 0.1).
	 * </ul>
	 * 
	 * @param edgeFalloff
	 *            The falloff value at the edge transition.
	 */
	public void setEdgeFalloff(float edgeFalloff) {
		// Make sure that the edge falloff curves do not overlap.
		float boundSize = upperBound - lowerBound;
		this.edgeFalloff = (edgeFalloff > boundSize / 2) ? boundSize / 2
				: edgeFalloff;
	}

	@Override
	public float getValue(float x, float y, float z) {
		assert (sourceModule[0] != null);
		assert (sourceModule[1] != null);
		assert (sourceModule[2] != null);

		float controlValue = sourceModule[2].getValue(x, y, z);
		float alpha;
		if (edgeFalloff > 0.0) {
			if (controlValue < (lowerBound - edgeFalloff)) {
				// The output value from the control module is below the
				// selector threshold; return the output value from the 
				// first source module.
				return sourceModule[0].getValue(x, y, z);

			} else if (controlValue < (lowerBound + edgeFalloff)) {
				// The output value from the control module is near the lower
				// end of the selector threshold and within the smooth curve.
				// Interpolate between the output values from the first and 
				// second source modules.
				float lowerCurve = (lowerBound - edgeFalloff);
				float upperCurve = (lowerBound + edgeFalloff);
				alpha = Interpolation.scurve3((controlValue - lowerCurve)
						/ (upperCurve - lowerCurve));
				return Interpolation.linearInterp(
						sourceModule[0].getValue(x, y, z),
						sourceModule[1].getValue(x, y, z), alpha);

			} else if (controlValue < (upperBound - edgeFalloff)) {
				// The output value from the control module is within the
				// selector threshold; return the output value from the second
				// source module.
				return sourceModule[1].getValue(x, y, z);

			} else if (controlValue < (upperBound + edgeFalloff)) {
				// The output value from the control module is near the upper
				// end of the selector threshold and within the smooth curve.
				// Interpolate between the output values from the first and second
				// source modules.
				float lowerCurve = (upperBound - edgeFalloff);
				float upperCurve = (upperBound + edgeFalloff);
				alpha = Interpolation.scurve3((controlValue - lowerCurve)
						/ (upperCurve - lowerCurve));
				return Interpolation.linearInterp(
						sourceModule[1].getValue(x, y, z),
						sourceModule[0].getValue(x, y, z), alpha);

			} else {
				// Output value from the control module is above the selector
				// threshold; return the output value from the first source
				// module.
				return sourceModule[0].getValue(x, y, z);
			}
		} else {
			if (controlValue < lowerBound || controlValue > upperBound) {
				return sourceModule[0].getValue(x, y, z);
			} else {
				return sourceModule[1].getValue(x, y, z);
			}
		}
	}

}
