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
 * Noise module that outputs the larger of the two output values from two source
 * modules.
 * <p>
 * This noise module requires <b>two</b> source modules.
 */
public class Max extends Module {

	protected Max(int sourceModuleCount) {
		super(sourceModuleCount);
	}

	public Max() {
		this(2);
	}

	@Override
	public float getValue(float x, float y, float z) {
		assert (sourceModule[0] != null);
		assert (sourceModule[1] != null);

		float v0 = sourceModule[0].getValue(x, y, z);
		float v1 = sourceModule[1].getValue(x, y, z);
		return Math.max(v0, v1);
	}
}
