//
// Copyright (C) 2004 Jason Bevins
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
 * Noise module that moves the coordinates of the input value before returning
 * the output value from a source module.
 * <p>
 * The {@link #getValue(float, float, float)} method moves the (x, y, z )
 * coordinates of the input value by a translation amount before returning the
 * output value from the source module. To set the translation amount, call the
 * {@link #setTranslation} method. To set the translation amount to apply to the
 * individual x, y, or z coordinates, call the {@link #setXTranslation},
 * {@link #setYTranslation} or {@link #setZTranslation} methods, respectively.
 * <p>
 * This noise module requires one source module.
 */
public class TranslatePoint extends Module {

	/**
	 * Translation amount applied to the x coordinate of the input value.
	 * Default value is 0.0.
	 */
	protected float xTranslation;
	/**
	 * Translation amount applied to the y coordinate of the input value.
	 * Default value is 0.0.
	 */
	protected float yTranslation;
	/**
	 * Translation amount applied to the z coordinate of the input value.
	 * Default value is 0.0.
	 */
	protected float zTranslation;

	protected TranslatePoint(int sourceModuleCount) {
		super(sourceModuleCount);
	}

	public TranslatePoint() {
		this(0);
	}

	/**
	 * Returns the translation amount to apply to the x coordinate of the input
	 * value.
	 */
	public float getXTranslation() {
		return xTranslation;
	}

	/**
	 * Returns the translation amount to apply to the y coordinate of the input
	 * value.
	 */
	public float getYTranslation() {
		return yTranslation;
	}

	/**
	 * Returns the translation amount to apply to the z coordinate of the input
	 * value.
	 */
	public float getZTranslation() {
		return zTranslation;
	}

	/**
	 * Sets the translation amount to apply to the input value.
	 * 
	 * @param translation
	 *            The translation amount to apply.
	 */
	public void setTranslation(float translation) {
		xTranslation = translation;
		yTranslation = translation;
		zTranslation = translation;
	}

	/**
	 * Sets the translation amounts to apply to the (x, y, z ) coordinates of
	 * the input value.
	 * 
	 * @param x
	 *            The translation amount to apply to the x coordinate.
	 * @param y
	 *            The translation amount to apply to the y coordinate.
	 * @param z
	 *            The translation amount to apply to the z coordinate.
	 */
	public void setTranslation(float x, float y, float z) {
		xTranslation = x;
		yTranslation = y;
		zTranslation = z;
	}

	/**
	 * Sets the translation amount to apply to the x coordinate of the input
	 * value.
	 * 
	 * @param x
	 *            The translation amount to apply to the x coordinate.
	 */
	public void setXTranslation(float x) {
		xTranslation = x;
	}

	/**
	 * Sets the translation amount to apply to the y coordinate of the input
	 * value.
	 * 
	 * @param y
	 *            The translation amount to apply to the y coordinate.
	 */
	public void setYTranslation(float y) {
		yTranslation = y;
	}

	/**
	 * Sets the translation amount to apply to the z coordinate of the input
	 * value.
	 * 
	 * @param z
	 *            The translation amount to apply to the z coordinate.
	 */
	public void setZTranslation(float z) {
		zTranslation = z;
	}

	@Override
	public float getValue(float x, float y, float z) {
		assert sourceModule[0] != null;
		return sourceModule[0].getValue(x + xTranslation, y + yTranslation, z
				+ zTranslation);
	}

}
