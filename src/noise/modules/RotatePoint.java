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
 * Noise module that rotates the input value around the origin before returning
 * the output value from a source module.
 * <p>
 * 
 * The {@link #getValue(float, float, float)} method rotates the coordinates of
 * the input value around the origin before returning the output value from the
 * source module. To set the rotation angles, call the {@link #setAngles}
 * method. To set the rotation angle around the individual x, y, or z axes, call
 * the {@link #setXAngle}, {@link #setYAngle} or {@link #setZAngle} methods,
 * respectively.
 * <p>
 * The coordinate system of the input value is assumed to be "left-handed" (x
 * increases to the right, y increases upward, and z increases inward.)
 * <p>
 * This noise module requires <b>one</b> source module.
 */
public class RotatePoint extends Module {

	/**
	 * An entry within the 3x3 rotation matrix used for rotating the input
	 * value.
	 */
	protected float x1Matrix;

	/**
	 * An entry within the 3x3 rotation matrix used for rotating the input
	 * value.
	 */
	protected float x2Matrix;

	/**
	 * An entry within the 3x3 rotation matrix used for rotating the input
	 * value.
	 */
	protected float x3Matrix;

	/** x rotation angle applied to the input value, in degrees. */
	protected float xAngle;

	/**
	 * An entry within the 3x3 rotation matrix used for rotating the input
	 * value.
	 */
	protected float y1Matrix;

	/**
	 * An entry within the 3x3 rotation matrix used for rotating the input
	 * value.
	 */
	protected float y2Matrix;

	/**
	 * An entry within the 3x3 rotation matrix used for rotating the input
	 * value.
	 */
	protected float y3Matrix;

	/** y rotation angle applied to the input value, in degrees. */
	protected float yAngle;

	/**
	 * An entry within the 3x3 rotation matrix used for rotating the input
	 * value.
	 */
	protected float z1Matrix;

	/**
	 * An entry within the 3x3 rotation matrix used for rotating the input
	 * value.
	 */
	protected float z2Matrix;

	/**
	 * An entry within the 3x3 rotation matrix used for rotating the input
	 * value.
	 */
	protected float z3Matrix;

	/** z rotation angle applied to the input value, in degrees. */
	protected float zAngle;

	public RotatePoint() {
		this(1);
	}

	protected RotatePoint(int sourceModuleCount) {
		super(sourceModuleCount);

		setAngles(0, 0, 0);
	}

	/**
	 * Returns the rotation angle around the x axis to apply to the input value.
	 */
	public float getXAngle() {
		return xAngle;
	}

	/**
	 * Returns the rotation angle around the y axis to apply to the input value.
	 */
	public float getYAngle() {
		return yAngle;
	}

	/**
	 * Returns the rotation angle around the z axis to apply to the input value.
	 */
	public float getZAngle() {
		return zAngle;
	}

	/**
	 * Sets the rotation angles around all three axes to apply to the input
	 * value.
	 * <p>
	 * The {@link #getValue(float, float, float)} method rotates the coordinates
	 * of the input value around the origin before returning the output value
	 * from the source module.
	 * 
	 * @param xAngle
	 *            The rotation angle around the x axis, in degrees.
	 * @param yAngle
	 *            The rotation angle around the y axis, in degrees.
	 * @param zAngle
	 *            The rotation angle around the z axis, in degrees.
	 */
	public void setAngles(float xAngle, float yAngle, float zAngle) {
		double xCos, yCos, zCos, xSin, ySin, zSin;
		double xr = Math.toRadians(xAngle);
		double yr = Math.toRadians(yAngle);
		double zr = Math.toRadians(zAngle);

		xCos = Math.cos(xr);
		yCos = Math.cos(yr);
		zCos = Math.cos(zr);
		xSin = Math.sin(xr);
		ySin = Math.sin(yr);
		zSin = Math.sin(zr);

		x1Matrix = (float) (ySin * xSin * zSin + yCos * zCos);
		y1Matrix = (float) (xCos * zSin);
		z1Matrix = (float) (ySin * zCos - yCos * xSin * zSin);
		x2Matrix = (float) (ySin * xSin * zCos - yCos * zSin);
		y2Matrix = (float) (xCos * zCos);
		z2Matrix = (float) (-yCos * xSin * zCos - ySin * zSin);
		x3Matrix = (float) (-ySin * xCos);
		y3Matrix = (float) xSin;
		z3Matrix = (float) (yCos * xCos);

		this.xAngle = xAngle;
		this.yAngle = yAngle;
		this.zAngle = zAngle;
	}

	/**
	 * Sets the rotation angle around the x axis to apply to the input value.
	 * <p>
	 * 
	 * @param xAngle
	 *            The rotation angle around the x axis, in degrees.
	 * @see #setAngles(float, float, float)
	 */
	public void setXAngle(float xAngle) {
		setAngles(xAngle, yAngle, zAngle);
	}

	/**
	 * Sets the rotation angle around the y axis to apply to the input value.
	 * <p>
	 * 
	 * @param yAngle
	 *            The rotation angle around the y axis, in degrees.
	 * @see #setAngles(float, float, float)
	 */
	public void setYAngle(float yAngle) {
		setAngles(xAngle, yAngle, zAngle);
	}

	/**
	 * Sets the rotation angle around the z axis to apply to the input value.
	 * <p>
	 * 
	 * @param zAngle
	 *            The rotation angle around the z axis, in degrees.
	 * @see #setAngles(float, float, float)
	 */
	public void setZAngle(float zAngle) {
		setAngles(xAngle, yAngle, zAngle);
	}

	@Override
	public float getValue(float x, float y, float z) {
		assert (sourceModule[0] != null);

		float nx = (x1Matrix * x) + (y1Matrix * y) + (z1Matrix * z);
		float ny = (x2Matrix * x) + (y2Matrix * y) + (z2Matrix * z);
		float nz = (x3Matrix * x) + (y3Matrix * y) + (z3Matrix * z);
		return sourceModule[0].getValue(nx, ny, nz);
	}

}
