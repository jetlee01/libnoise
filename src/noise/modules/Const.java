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
 * Noise module that outputs a constant value.<p>
 * To specify the constant value, call the {@link #setConstValue} method.<p>
 * This noise module is not useful by itself, but it is often used as a
   source module for other noise modules.<p>
   This noise module does not require any source modules.
 */
public class Const extends Module{

	protected float constValue;
	
	/**
	 * Constructor.<p>
	 * 
	 * The Default const value is 0.0.
	 */
	public Const() {
		this(0);
	}
	
	protected Const(int sourceModuleCount) {
		super(sourceModuleCount);
	}
	
    public void setConstValue(float constValue){
    	this.constValue = constValue;
    }
    
	@Override
	public float getValue(float x, float y, float z) {
		return constValue;
	}

}
