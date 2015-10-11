//
// Copyright (C) 2004 Owen Jacobson
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
// The developer's email is ojacobson@lionsanctuary.net
//
package noise.models;

import noise.modules.Module;

/**
 * Model that defines the surface of a plane.
 * <p>
 * This model returns an output value from a noise module given the coordinates
 * of an input value located on the surface of an (x, z ) plane.
 * <p>
 * To generate an output value, pass the (x, z ) coordinates of an input value
 * to the {@link #getValue} method.
 * <p>
 * This model is useful for creating:
 * <ul>
 * <li>two-dimensional textures.
 * <li>terrain height maps for local areas
 * </ul>
 * This plane extends infinitely in both directions.
 */
public class Plane {

	private Module module;

	public Plane() {
	}
	
	public Plane(Module module) {
		this.module = module;
	}

	/**
	 * Returns the noise module that is used to generate the output values.
	 * 
	 * @return A reference to the noise module.
	 */
	public Module getModule() {
		return module;
	}

	/**
	 * Sets the noise module that is used to generate the output values.
	 * <p>
	 * This noise module must exist for the lifetime of this object, until you
	 * pass a new noise module to this method.
	 * 
	 * @param module
	 *            The noise module that is used to generate the output values.
	 */
	public void setModule(Module module) {
		this.module = module;
	}

	/**
	 * Returns the output value from the noise module given the (x, z )
	 * coordinates of the specified input value located on the surface of the
	 * plane.
	 * <p>
	 * 
	 * @param x
	 *            The x coordinate of the input value.
	 * @param z
	 *            The z coordinate of the input value.
	 * @return The output value from the noise module.
	 */
	public float getValue(float x, float z) {
		assert module != null;
		
		return module.getValue(x, 0, z);
	}
}
