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
 * Noise module that outputs the absolute value of the output value from a
 * source module.
 * <p>
 * This noise module requires one source module.
 */
public class Abs extends Module {

	protected Abs(int sourceModuleCount) {
		super(sourceModuleCount);
	}

	public Abs() {
		this(1);
	}

	@Override
	public float getValue(float x, float y, float z) {
		return Math.abs(sourceModule[0].getValue(x, y, z));
	}

}
