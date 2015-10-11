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

import noise.NoiseGen;
import noise.NoiseQuality;

/**
 * Noise module that outputs three-dimensional "billowy" noise.
 * <p>
 * This noise module generates "billowy" noise suitable for clouds and rocks.
 * <p>
 * This noise module is nearly identical to noise.module.Perlin except this
 * noise module modifies each octave with an absolute-value function. See the
 * documentation of noise.module.Perlin for more information.
 * 
 */
public class Billow extends Module {

	/** Default frequency for the noise.module.Billow noise module. */
	static final float DEFAULT_BILLOW_FREQUENCY = 1.0f;

	/** Default lacunarity for the the noise.module.Billow noise module. */
	static final float DEFAULT_BILLOW_LACUNARITY = 2.0f;

	/**
	 * Default number of octaves for the the noise.module.Billow noise module.
	 */
	static final int DEFAULT_BILLOW_OCTAVE_COUNT = 6;

	/**
	 * Default persistence value for the the noise.module.Billow noise module.
	 */
	static final float DEFAULT_BILLOW_PERSISTENCE = 0.5f;

	/** Default noise quality for the the noise.module.Billow noise module. */
	static final NoiseQuality DEFAULT_BILLOW_QUALITY = NoiseQuality.QUALITY_STD;

	/** Default noise seed for the the noise.module.Billow noise module. */
	static final int DEFAULT_BILLOW_SEED = 0;

	/**
	 * Maximum number of octaves for the the noise.module.Billow noise module.
	 */
	static final int BILLOW_MAX_OCTAVE = 30;

	/** Frequency of the first octave. */
	protected float frequency;
	/** Frequency multiplier between successive octaves. */
	protected float lacunarity;
	/** Quality of the billowy noise. */
	NoiseQuality noiseQuality;
	/** Total number of octaves that generate the billowy noise. */
	protected int octaveCount;
	/** Persistence value of the billowy noise. */
	protected float persistence;
	/** Seed value used by the billowy-noise function. */
	protected int seed;

	protected Billow(int sourceModuleCount) {
		super(sourceModuleCount);

		frequency = DEFAULT_BILLOW_FREQUENCY;
		lacunarity = DEFAULT_BILLOW_LACUNARITY;
		noiseQuality = DEFAULT_BILLOW_QUALITY;
		octaveCount = DEFAULT_BILLOW_OCTAVE_COUNT;
		persistence = DEFAULT_BILLOW_PERSISTENCE;
		seed = DEFAULT_BILLOW_SEED;
	}
	
	/**
	 * Constructor.
	 * <p>
	 * The default frequency is set to {@link #DEFAULT_BILLOW_FREQUENCY}. <br>
	 * The default lacunarity is set to {@link #DEFAULT_BILLOW_LACUNARITY}. <br>
	 * The default number of octaves is set to
	 * {@link #DEFAULT_BILLOW_OCTAVE_COUNT}. <br>
	 * The default persistence value is set to
	 * {@link #DEFAULT_BILLOW_PERSISTENCE}. <br>
	 * The default seed value is set to {@link #DEFAULT_BILLOW_SEED}.
	 */
	public Billow(){
		this(0);
	}

	/**
	 * @return The frequency of the first octave.
	 */
	public float getFrequency() {
		return frequency;
	}

	/**
	 * The lacunarity is the frequency multiplier between successive octaves.
	 * 
	 * @return The lacunarity of the billowy noise.
	 */
	public float getLacunarity() {
		return lacunarity;
	}

	/**
	 * @return The quality of the billowy noise.
	 */
	public NoiseQuality getNoiseQuality() {
		return noiseQuality;
	}

	/**
	 * Returns the number of octaves that generate the billowy noise.
	 * <p>
	 * The number of octaves controls the amount of detail in the billowy noise.
	 */
	public int getOctaveCount() {
		return octaveCount;
	}

	/**
	 * The persistence value controls the roughness of the billowy noise.
	 * 
	 * @return The persistence value of the billowy noise.
	 */
	public float getPersistence() {
		return persistence;
	}

	/**
	 * Returns the seed value used by the billowy-noise function.
	 */
	public int getSeed() {
		return seed;
	}

	/**
	 * Sets the frequency of the first octave.
	 * 
	 * @param frequency
	 */
	public void setFrequency(float frequency) {
		this.frequency = frequency;
	}

	/**
	 * Sets the lacunarity of the billowy noise.
	 * <p>
	 * The lacunarity is the frequency multiplier between successive octaves.<br>
	 * For best results, set the lacunarity to a number between 1.5 and 3.5.
	 * 
	 * @param lacunarity
	 */
	public void setLacunarity(float lacunarity) {
		this.lacunarity = lacunarity;
	}

	/**
	 * Sets the quality of the billowy noise.
	 * <p>
	 * See noise.NoiseQuality for definitions of the various coherent-noise
	 * qualities.
	 * 
	 * @param noiseQuality
	 */
	public void setNoiseQuality(NoiseQuality noiseQuality) {
		this.noiseQuality = noiseQuality;
	}

	/**
	 * Sets the number of octaves that generate the billowy noise.
	 * <p>
	 * The number of octaves ranges from 1 to noise.module.BILLOW_MAX_OCTAVE.
	 * <p>
	 * The number of octaves controls the amount of detail in the billowy noise.
	 * <p>
	 * The larger the number of octaves, the more time required to calculate the
	 * billowy-noise value.
	 * 
	 * @exception IllegalArgumentException
	 *                An invalid parameter was specified; see the preconditions
	 *                for more information.
	 */
	public void setOctaveCount(int octaveCount) {
		if (octaveCount < 1 || octaveCount > BILLOW_MAX_OCTAVE) {
			throw new IllegalArgumentException();
		}
		this.octaveCount = octaveCount;
	}

	/**
	 * Sets the persistence value of the billowy noise.
	 * <p>
	 * The persistence value controls the roughness of the billowy noise.
	 * <p>
	 * For best results, set the persistence value to a number between 0.0 and
	 * 1.0.
	 * 
	 * @param persistence
	 */
	public void setPersistence(float persistence) {
		this.persistence = persistence;
	}

	/**
	 * Sets the seed value used by the billowy-noise function.
	 */
	public void setSeed(int seed) {
		this.seed = seed;
	}

	@Override
	public float getValue(float x, float y, float z) {
		float value = 0.0f;
		float signal = 0.0f;
		float curPersistence = 1.0f;
		float nx, ny, nz;
		int seed;

		x *= frequency;
		y *= frequency;
		z *= frequency;

		for (int curOctave = 0; curOctave < octaveCount; curOctave++) {

			// Make sure that these floating-point values have the same range as
			// a 32-bit integer so that we can pass them to the coherent-noise
			// functions.
			nx = NoiseGen.makeInt32Range(x);
			ny = NoiseGen.makeInt32Range(y);
			nz = NoiseGen.makeInt32Range(z);

			// Get the coherent-noise value from the input value and add it to
			// the final result.
			seed = (this.seed + curOctave) & 0xffffffff;
			signal = NoiseGen.gradientCoherentNoise3D(nx, ny, nz, seed, noiseQuality);
			signal = 2.0f * Math.abs(signal) - 1.0f;
			value += signal * curPersistence;

			// Prepare the next octave.
			x *= lacunarity;
			y *= lacunarity;
			z *= lacunarity;
			curPersistence *= persistence;
		}
		value += 0.5f;

		return value;
	}

	public static void main(String[] args) {
		System.out.println(Integer.MAX_VALUE >> 1);
	}

}
