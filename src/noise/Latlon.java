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
package noise;

public class Latlon {

	/**
	 * Converts latitude/longitude coordinates on a unit sphere into 3D
     * Cartesian coordinates.
     * 
	 * @param lat The latitude, in degrees.
	 * @param lon The longitude, in degrees.
	 * @return The Cartesian coordinates in the 3D space.
	 */
	public static final float[] latLonToXYZ(float lat, float lon) {
		final double a = Math.toRadians(lat);
		final double o = Math.toRadians(lon);

		double r = Math.cos(a);
		float x = (float) (r * Math.cos(o));
		float y = (float) Math.sin(a);
		float z = (float) (r * Math.sin(o));
		
		return new float[]{x,y,z};
	}
}
