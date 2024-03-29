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
 * Abstract base class for noise modules.
 * <p>
 * 
 * A <i>noise module</i> is an object that calculates and outputs a value given
 * a three-dimensional input value.
 * <p>
 * 
 * Each type of noise module uses a specific method to calculate an output
 * value. Some of these methods include:
 * <ul>
 * <li>Calculating a value using a coherent-noise function or some other
 * mathematical function.
 * <li>Mathematically changing the output value from another noise module in
 * various ways.
 * <li>Combining the output values from two noise modules in various ways.
 * </ul>
 * An application can use the output values from these noise modules in the
 * following ways:
 * <ul>
 * <li>It can be used as an elevation value for a terrain height map
 * <li>It can be used as a grayscale (or an RGB-channel) value for a procedural
 * texture
 * <li>It can be used as a position value for controlling the movement of a
 * simulated lifeform.
 * </ul>
 * 
 * A noise module defines a near-infinite 3-dimensional texture. Each position
 * in this "texture" has a specific value.
 * 
 * <p>
 * <b>Combining noise modules</b>
 * <p>
 * Noise modules can be combined with other noise modules to generate complex
 * output values. A noise module that is used as a source of output values for
 * another noise module is called a <i>source module</i>. Each of these source
 * modules may be connected to other source modules, and so on.
 * <p>
 * There is no limit to the number of noise modules that can be connected
 * together in this way. However, each connected noise module increases the time
 * required to calculate an output value.
 * <p>
 * 
 * <b>Noise-module categories</b>
 * <p>
 * The noise module classes that are included in libnoise can be roughly divided
 * into five categories:
 * <ul>
 * <li><i>Generator Modules</i><br>
 * A generator module outputs a value generated by a coherent-noise function or
 * some other mathematical function.
 * <p>
 * Examples of generator modules include:
 * <ul>
 * <li>noise.module.Const: Outputs a constant value.
 * <li>{@link noise.module.Perlin}: Outputs a value generated by a Perlin-noise
 * function.
 * <li>noise.module.Voronoi: Outputs a value generated by a Voronoi-cell
 * function.
 * </ul>
 * <li><i>Modifier Modules</i><br>
 * A modifer module mathematically modifies the output value from a source
 * module.
 * <p>
 * 
 * Examples of modifier modules include:
 * <ul>
 * <li>noise.module.Curve: Maps the output value from the source module onto an
 * arbitrary function curve.
 * <li>noise.module.Invert: Inverts the output value from the source module.
 * </ul>
 * 
 * <li><i>Combiner Modules</i><br>
 * A combiner module mathematically combines the output values from two or more
 * source modules together.
 * <p>
 * Examples of combiner modules include:
 * <ul>
 * <li>noise.module.Add: Adds the two output values from two source modules.
 * <li>noise.module.Max: Outputs the larger of the two output values from two
 * source modules.
 * </ul>
 * <li><i>Selector Modules</i><br>
 * A selector module uses the output value from a <i>control module</i> to
 * specify how to combine the output values from its source modules.
 * <p>
 * Examples of selector modules include:
 * <ul>
 * <li>noise.module.Blend: Outputs a value that is linearly interpolated between
 * the output values from two source modules; the interpolation weight is
 * determined by the output value from the control module.
 * <li>noise.module.Select: Outputs the value selected from one of two source
 * modules chosen by the output value from a control module.
 * </ul>
 * <li><i>Transformer Modules</i><br>
 * A transformer module applies a transformation to the coordinates of the input
 * value before retrieving the output value from the source module. A
 * transformer module does not modify the output value.
 * <p>
 * Examples of transformer modules include:
 * <ul>
 * <li>RotatePoint: Rotates the coordinates of the input value around the origin
 * before retrieving the output value from the source module.
 * <li>ScalePoint: Multiplies each coordinate of the input value by a constant
 * value before retrieving the output value from the source module.
 * </ul>
 * </ul>
 * 
 * <b>Connecting source modules to a noise module</b>
 * <p>
 * An application connects a source module to a noise module by passing the
 * source module to the SetSourceModule() method.
 * <p>
 * The application must also pass an <i>index value</i> to SetSourceModule() as
 * well. An index value is a numeric identifier for that source module. Index
 * values are consecutively numbered starting at zero.
 * <p>
 * To retrieve a reference to a source module, pass its index value to the
 * GetSourceModule() method.
 * <p>
 * Each noise module requires the attachment of a certain number of source
 * modules before it can output a value. For example, the noise::module::Add
 * module requires two source modules, while the noise::module::Perlin module
 * requires none. Call the GetSourceModuleCount() method to retrieve the number
 * of source modules required by that module.
 * <p>
 * For non-selector modules, it usually does not matter which index value an
 * application assigns to a particular source module, but for selector modules,
 * the purpose of a source module is defined by its index value. For example,
 * consider the noise::module::Select noise module, which requires three source
 * modules. The control module is the source module assigned an index value of
 * 2. The control module determines whether the noise module will output the
 * value from the source module assigned an index value of 0 or the output value
 * from the source module assigned an index value of 1.
 * <p>
 * 
 * <b>Generating output values with a noise module</b>
 * <p>
 * Once an application has connected all required source modules to a noise
 * module, the application can now begin to generate output values with that
 * noise module.
 * <p>
 * To generate an output value, pass the ( @a x, @a y, @a z ) coordinates of an
 * input value to the GetValue() method.
 * <p>
 * <b>Using a noise module to generate terrain height maps or textures</b>
 * <p>
 * One way to generate a terrain height map or a texture is to first allocate a
 * 2-dimensional array of floating-point values. For each array element, pass
 * the array subscripts as @a x and @a y coordinates to the GetValue() method
 * (leaving the @a z coordinate set to zero) and place the resulting output
 * value into the array element.
 * <p>
 * <b>Creating your own noise modules</b>
 * <p>
 * Create a class that publicly derives from noise.module.Module.
 * <p>
 * In the constructor, call the base class' constructor while passing the return
 * value from GetSourceModuleCount() to it.
 * <p>
 * Override the GetSourceModuleCount() pure virtual method. From this method,
 * return the number of source modules required by your noise module.
 * <p>
 * Override the GetValue() method. For generator modules, calculate and output a
 * value given the coordinates of the input value. For other modules, retrieve
 * the output values from each source module referenced in the protected @a
 * m_pSourceModule array, mathematically combine those values, and return the
 * combined value.
 * <p>
 * When developing a noise module, you must ensure that your noise module does
 * not modify any source module or control module connected to it; a noise
 * module can only modify the output value from those source modules. You must
 * also ensure that if an application fails to connect all required source
 * modules via the SetSourceModule() method and then attempts to call the
 * GetValue() method, your module will raise an assertion.
 * <p>
 * It shouldn't be too difficult to create your own noise module. If you still
 * have some problems, take a look at the source code for noise.module.Add,
 * which is a very simple noise module.
 * 
 */
public abstract class Module {

	/**
	 * An array containing the pointers to each source module required by this
	 * noise module.
	 */
	protected Module[] sourceModule;

	protected Module(int sourceModuleCount) {
		if (sourceModuleCount > 0) {
			sourceModule = new Module[sourceModuleCount];
		}
	}

	/**
	 * Returns a reference to a source module connected to this noise module.
	 * <p>
	 * The <em>index</em> value ranges from 0 to one less than the number of
	 * source modules required by this noise module.
	 * <p>
	 * A source module with the specified <em>index</em> value has been added to
	 * this noise module via a call to setSourceModule().
	 * <p>
	 * Each noise module requires the attachment of a certain number of source
	 * modules before an application can call the GetValue() method.
	 * 
	 * @param index
	 *            The index value assigned to the source module.
	 * @return A reference to the source module.
	 * 
	 * @exception IndexOutOfBoundsException
	 *                When
	 *                <code>index >= getSourceModuleCount() || index < 0</code>.
	 * @exception NullPointerException
	 */
	public Module getSourceModule(int index) {
		assert sourceModule != null;

		if (index >= getSourceModuleCount() || index < 0)
			throw new IndexOutOfBoundsException();

		if (sourceModule[index] == null)
			throw new NullPointerException();

		return sourceModule[index];
	}

	/**
	 * @return The number of source modules required by this noise module.
	 */
	public int getSourceModuleCount() {
		if (sourceModule != null)
			return sourceModule.length;
		else
			return 0;
	}

	/**
	 * Generates an output value given the coordinates of the specified input
	 * value.
	 * <p>
	 * All source modules required by this noise module have been passed to the
	 * setSourceModule() method.
	 * <p>
	 * Before an application can call this method, it must first connect all
	 * required source modules via the setSourceModule() method. If these source
	 * modules are not connected to this noise module, this method raises a
	 * debug assertion.
	 * <p>
	 * To determine the number of source modules required by this noise module,
	 * call the getSourceModuleCount() method.
	 * 
	 * @param x
	 *            The x coordinate of the input value.
	 * @param y
	 *            The y coordinate of the input value.
	 * @param z
	 *            The z coordinate of the input value.
	 * @return The output value.
	 */
	public abstract float getValue(float x, float y, float z);

	/**
	 * Connects a source module to this noise module.
	 * <p>
	 * 
	 * The index value ranges from 0 to one less than the number of source
	 * modules required by this noise module.
	 * <p>
	 * 
	 * A noise module mathematically combines the output values from the source
	 * modules to generate the value returned by
	 * {@link #getValue(float, float, float)}.
	 * <p>
	 * The index value to assign a source module is a unique identifier for that
	 * source module. If an index value has already been assigned to a source
	 * module, this noise module replaces the old source module with the new
	 * source module.
	 * <p>
	 * Before an application can call the {@link #getValue(float, float, float)}
	 * method, it must first connect all required source modules. To determine
	 * the number of source modules required by this noise module, call the
	 * {@link #getSourceModuleCount} method.
	 * <p>
	 * This source module must exist throughout the lifetime of this noise
	 * module unless another source module replaces that source module.
	 * <p>
	 * A noise module does not modify a source module; it only modifies its
	 * output values.
	 * 
	 * @param index
	 *            An index value to assign to this source module.
	 * @param source
	 *            The source module to attach.
	 * 
	 * @exception IndexOutOfBoundsException
	 *                When
	 *                <code>index >= getSourceModuleCount() || index < 0</code>.
	 */
	public void setSourceModule(int index, Module source) {
		assert sourceModule != null;

		if (index >= getSourceModuleCount() || index < 0) {
			throw new IndexOutOfBoundsException();
		}

		sourceModule[index] = source;
	}
}
