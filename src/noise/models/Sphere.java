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
package noise.models;

import noise.Latlon;
import noise.modules.Module;

/**
 * Model that defines the surface of a sphere.
 * <p>
 * This model returns an output value from a noise module given the coordinates
 * of an input value located on the surface of a sphere.
 * <p>
 * To generate an output value, pass the (latitude, longitude) coordinates of an
 * input value to the {@link #getValue} method.
 * <p>
 * This model is useful for creating:
 * <ul>
 * <li>seamless textures that can be mapped onto a sphere
 * <li>terrain height maps for entire planets
 * </ul>
 * This sphere has a radius of 1.0 unit and its center is located at the origin.
 */
public class Sphere {

	private Module module;

	public Sphere() {
	}

	public Sphere(Module module) {
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
	 * Returns the output value from the noise module given the (latitude,
	 * longitude) coordinates of the specified input value located on the
	 * surface of the sphere.
	 * <p>
	 * Use a negative latitude if the input value is located on the southern
	 * hemisphere.
	 * <p>
	 * Use a negative longitude if the input value is located on the western
	 * hemisphere.
	 * 
	 * @param lat
	 *            The latitude of the input value, in degrees.
	 * @param lon
	 *            The longitude of the input value, in degrees.
	 * @return The output value from the noise module.
	 */
	public float getValue(float lat, float lon) {
		assert module != null;
		float[] xyz = Latlon.latLonToXYZ(lat, lon);
		return module.getValue(xyz[0], xyz[1], xyz[2]);
	}
}
