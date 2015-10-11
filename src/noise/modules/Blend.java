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
 * Noise module that outputs a weighted blend of the output values from two
 * source modules given the output value supplied by a control module.
 * <p>
 * 
 * Unlike most other noise modules, the index value assigned to a source module
 * determines its role in the blending operation:
 * <ul>
 * <li>Source module 0 (upper left in the diagram) outputs one of the values to
 * blend.
 * <li>Source module 1 (lower left in the diagram) outputs one of the values to
 * blend.
 * <li>Source module 2 (bottom of the diagram) is known as the <i>control
 * module</i>. The control module determines the weight of the blending
 * operation. Negative values weigh the blend towards the output value from the
 * source module with an index value of 0. Positive values weigh the blend
 * towards the output value from the source module with an index value of 1.
 * </ul>
 * 
 * An application can pass the control module to the {@link #setControlModule}
 * method instead of the {@link #setSourceModule} method. This may make the
 * application code easier to read.
 * <p>
 * This noise module uses linear interpolation to perform the blending
 * operation.
 * <p>
 * This noise module requires three source modules.
 */
public class Blend extends Module {

	protected Blend(int sourceModuleCount) {
		super(sourceModuleCount);
	}

	public Blend() {
		this(3);
	}

	/**
	 * Returns the control module.
	 * <p>
	 * A control module has been added to this noise module via a call to
	 * {@link #setSourceModule} or {@link #setControlModule}.
	 * <p>
	 * The control module determines the weight of the blending operation.
	 * Negative values weigh the blend towards the output value from the source
	 * module with an index value of 0. Positive values weigh the blend towards
	 * the output value from the source module with an index value of 1.
	 * 
	 * @exception NullPointerException
	 *                if the <em>sourceModule</em> is null or the control module
	 *                is null.
	 */
	public Module getControlModule() {
		if (sourceModule == null || sourceModule[2] == null)
			throw new NullPointerException();

		return sourceModule[2];
	}

	/**
	 * Sets the control module.
	 * <p>
	 * The control module determines the weight of the blending operation.
	 * Negative values weigh the blend towards the output value from the source
	 * module with an index value of 0. Positive values weigh the blend towards
	 * the output value from the source module with an index value of 1.
	 * <p>
	 * This method assigns the control module an index value of 2. Passing the
	 * control module to this method produces the same results as passing the
	 * control module to the {@link #setSourceModule} method while assigning
	 * that noise module an index value of 2.
	 * <p>
	 * This control module must exist throughout the lifetime of this noise
	 * module unless another control module replaces that control module.
	 * 
	 * @param controlModule
	 *            The control module.
	 */
	public void setControlModule(Module controlModule) {
		assert sourceModule != null;

		sourceModule[2] = controlModule;
	}

	@Override
	public float getValue(float x, float y, float z) {
		assert sourceModule[0] != null;
		assert sourceModule[1] != null;
		assert sourceModule[2] != null;
		
		float v0 = sourceModule[0].getValue(x, y, z);
		float v1 = sourceModule[1].getValue(x, y, z);
		float alpha = (sourceModule[2].getValue(x, y, z) + 1.0f) * 0.5f;
		
		return Interpolation.linearInterp(v0, v1, alpha);
	}

}
