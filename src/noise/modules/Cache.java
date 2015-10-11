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
 * Noise module that caches the last output value generated by a source module.
 * <p>
 * 
 * If an application passes an input value to the
 * {@link #getValue(float, float, float)} method that differs from the
 * previously passed-in input value, this noise module instructs the source
 * module to calculate the output value. This value, as well as the ( x, y, z)
 * coordinates of the input value, are stored (cached) in this noise module.
 * <p>
 * 
 * If the application passes an input value to the
 * {@link #getValue(float, float, float)} method that is equal to the previously
 * passed-in input value, this noise module returns the cached output value
 * without having the source module recalculate the output value.
 * <p>
 * If an application passes a new source module to the
 * {@link #setSourceModule(int, Module)} method, the cache is invalidated.
 * <p>
 * 
 * Caching a noise module is useful if it is used as a source module for
 * multiple noise modules. If a source module is not cached, the source module
 * will redundantly calculate the same output value once for each noise module
 * in which it is included.
 * <p>
 * This noise module requires <b>one</b> source module.
 */
public class Cache extends Module {

	/** The cached output value at the cached input value. */
	protected float cachedValue;
	/** Determines if a cached output value is stored in this noise module. */
	protected boolean isCached;
	/** x coordinate of the cached input value. */
	protected float xCache;
	/** y coordinate of the cached input value. */
	protected float yCache;
	/** z coordinate of the cached input value. */
	protected float zCache;

	protected Cache(int sourceModuleCount) {
		super(sourceModuleCount);
	}

	public Cache() {
		this(1);
	}

	@Override
	public float getValue(float x, float y, float z) {
		assert sourceModule[0] != null;

		if (!(isCached && x == xCache && y == yCache && z == zCache)) {
			cachedValue = sourceModule[0].getValue(x, y, z);
			xCache = x;
			yCache = y;
			zCache = z;
		}
		isCached = true;
		return cachedValue;
	}

	@Override
	public void setSourceModule(int index, Module source) {
		super.setSourceModule(index, source);

		isCached = false;
	}

}