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
 * Noise module that uses three source modules to displace each coordinate of
 * the input value before returning the output value from a source module.
 * <p>
 * Unlike most other noise modules, the index value assigned to a source module
 * determines its role in the displacement operation:
 * <ul>
 * <li>Source module 0 (left in the diagram) outputs a value.
 * <li>Source module 1 (lower left in the diagram) specifies the offset to apply
 * to the x coordinate of the input value.
 * <li>Source module 2 (lower center in the diagram) specifies the offset to
 * apply to the y coordinate of the input value.
 * <li>Source module 3 (lower right in the diagram) specifies the offset to
 * apply to the z coordinate of the input value.
 * </ul>
 * The {@link #getValue(float, float, float)} method modifies the (x, y, z )
 * coordinates of the input value using the output values from the three
 * displacement modules before retrieving the output value from the source
 * module.
 * <p>
 * 
 * The noise.module.Turbulence noise module is a special case of the
 * displacement module; internally, there are three Perlin-noise modules that
 * perform the displacement operation.
 * <p>
 * This noise module requires <b>four</b> source modules.
 */
public class Displace extends Module {

	protected Displace(int sourceModuleCount) {
		super(sourceModuleCount);
	}

	public Displace() {
		this(4);
	}

	@Override
	public float getValue(float x, float y, float z) {
		assert (sourceModule[0] != null);
		assert (sourceModule[1] != null);
		assert (sourceModule[2] != null);
		assert (sourceModule[3] != null);

		// Get the output values from the three displacement modules. Add each
		// value to the corresponding coordinate in the input value.
		float xDisplace = x + (sourceModule[1].getValue(x, y, z));
		float yDisplace = y + (sourceModule[2].getValue(x, y, z));
		float zDisplace = z + (sourceModule[3].getValue(x, y, z));

		// Retrieve the output value using the offsetted input value instead of
		// the original input value.
		return sourceModule[0].getValue(xDisplace, yDisplace, zDisplace);
	}

	/**
	 * Returns the x displacement module.
	 * <p>
	 * This displacement module has been added to this noise module via a call
	 * to {@link #setSourceModule(int, Module)} or {@link #setXDisplaceModule}.
	 * <p>
	 * The {@link #getValue(float, float, float)} method displaces the input
	 * value by adding the output value from this displacement module to the x
	 * coordinate of the input value before returning the output value from the
	 * source module.
	 * 
	 * @return A reference to the x displacement module.
	 */
	public Module getXDisplaceModule() {
		if (sourceModule == null || sourceModule[1] == null)
			throw new NullPointerException();

		return sourceModule[1];
	}

	/**
	 * Returns the y displacement module.
	 * <p>
	 * This displacement module has been added to this noise module via a call
	 * to {@link #setSourceModule(int, Module)} or {@link #setYDisplaceModule}.
	 * <p>
	 * The {@link #getValue(float, float, float)} method displaces the input
	 * value by adding the output value from this displacement module to the y
	 * coordinate of the input value before returning the output value from the
	 * source module.
	 * 
	 * @return A reference to the y displacement module.
	 */
	public Module getYDisplaceModule() {
		if (sourceModule == null || sourceModule[2] == null)
			throw new NullPointerException();

		return sourceModule[2];
	}

	/**
	 * Returns the z displacement module.
	 * <p>
	 * This displacement module has been added to this noise module via a call
	 * to {@link #setSourceModule(int, Module)} or {@link #setZDisplaceModule}.
	 * <p>
	 * The {@link #getValue(float, float, float)} method displaces the input
	 * value by adding the output value from this displacement module to the z
	 * coordinate of the input value before returning the output value from the
	 * source module.
	 * 
	 * @return A reference to the z displacement module.
	 */
	public Module getZDisplaceModule() {
		if (sourceModule == null || sourceModule[3] == null)
			throw new NullPointerException();

		return sourceModule[3];
	}

	/**
	 * Sets the x displacement module.
	 * <p>
	 * The {@link #getValue(float, float, float)} method displaces the input
	 * value by adding the output value from this displacement module to the x
	 * coordinate of the input value before returning the output value from the
	 * source module.
	 * <p>
	 * This method assigns an index value of 1 to the x displacement module.
	 * Passing this displacement module to this method produces the same results
	 * as passing this displacement module to the
	 * {@link #setSourceModule(int, Module)} method while assigning it an index
	 * value of 1.
	 * <p>
	 * This displacement module must exist throughout the lifetime of this noise
	 * module unless another displacement module replaces it.
	 * 
	 * @param xDisplayModule
	 *            Displacement module that displaces the x coordinate.
	 */
	public void setXDisplaceModule(Module xDisplayModule) {
		assert sourceModule != null;
		sourceModule[1] = xDisplayModule;
	}

	/**
	 * Sets the y displacement module.
	 * <p>
	 * The {@link #getValue(float, float, float)} method displaces the input
	 * value by adding the output value from this displacement module to the y
	 * coordinate of the input value before returning the output value from the
	 * source module.
	 * <p>
	 * This method assigns an index value of 2 to the y displacement module.
	 * Passing this displacement module to this method produces the same results
	 * as passing this displacement module to the
	 * {@link #setSourceModule(int, Module)} method while assigning it an index
	 * value of 2.
	 * <p>
	 * This displacement module must exist throughout the lifetime of this noise
	 * module unless another displacement module replaces it.
	 * 
	 * @param yDisplayModule
	 *            Displacement module that displaces the y coordinate.
	 */
	public void setYDisplaceModule(Module yDisplayModule) {
		assert sourceModule != null;
		sourceModule[2] = yDisplayModule;
	}

	/**
	 * Sets the z displacement module.
	 * <p>
	 * The {@link #getValue(float, float, float)} method displaces the input
	 * value by adding the output value from this displacement module to the z
	 * coordinate of the input value before returning the output value from the
	 * source module.
	 * <p>
	 * This method assigns an index value of 3 to the z displacement module.
	 * Passing this displacement module to this method produces the same results
	 * as passing this displacement module to the
	 * {@link #setSourceModule(int, Module)} method while assigning it an index
	 * value of 3.
	 * <p>
	 * This displacement module must exist throughout the lifetime of this noise
	 * module unless another displacement module replaces it.
	 * 
	 * @param zDisplayModule
	 *            Displacement module that displaces the z coordinate.
	 */
	public void setZDisplaceModule(Module zDisplayModule) {
		assert sourceModule != null;
		sourceModule[3] = zDisplayModule;
	}

	/**
	 * Sets the x, y, and z displacement modules.
	 * <p>
	 * The {@link #getValue(float, float, float)} method displaces the input
	 * value by adding the output value from each of the displacement modules to
	 * the corresponding coordinates of the input value before returning the
	 * output value from the source module.
	 * <p>
	 * This method assigns an index value of 1 to the x displacement module, an
	 * index value of 2 to the y displacement module, and an index value of 3 to
	 * the z displacement module.
	 * <p>
	 * These displacement modules must exist throughout the lifetime of this
	 * noise module unless another displacement module replaces it.
	 * 
	 * @param xDisplayModule
	 *            Displacement module that displaces the x coordinate.
	 * @param yDisplayModule
	 *            Displacement module that displaces the y coordinate.
	 * @param zDisplayModule
	 *            Displacement module that displaces the z coordinate.
	 */
	public void setDisplaceModules(Module xDisplayModule,
			Module yDisplayModule, Module zDisplayModule) {
		assert sourceModule != null;

		sourceModule[1] = xDisplayModule;
		sourceModule[2] = yDisplayModule;
		sourceModule[3] = zDisplayModule;
	}
}
