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

import noise.NoiseGen;
import noise.NoiseQuality;

/**
 * Noise module that outputs 3-dimensional ridged-multifractal noise.
 * <p>
 * This noise module, heavily based on the Perlin-noise module, generates
 * ridged-multifractal noise. Ridged-multifractal noise is generated in much of
 * the same way as Perlin noise, except the output of each octave is modified by
 * an absolute-value function. Modifying the octave values in this way produces
 * ridge-like formations.
 * <p>
 * Ridged-multifractal noise does not use a persistence value. This is because
 * the persistence values of the octaves are based on the values generated from
 * from previous octaves, creating a feedback loop (or that's what it looks like
 * after reading the code.)
 * <p>
 * This noise module outputs ridged-multifractal-noise values that usually range
 * from -1.0 to +1.0, but there are no guarantees that all output values will
 * exist within that range.
 * <p>
 * <b>Note: </b>For ridged-multifractal noise generated with only one octave,
 * the output value ranges from -1.0 to 0.0.
 * <p>
 * Ridged-multifractal noise is often used to generate craggy mountainous
 * terrain or marble-like textures.
 * <p>
 * This noise module does not require any source modules.
 * <p>
 * <b>Octaves</b>
 * <p>
 * The number of octaves control the <i>amount of detail</i> of the
 * ridged-multifractal noise. Adding more octaves increases the detail of the
 * ridged-multifractal noise, but with the drawback of increasing the
 * calculation time.<br>
 * An application may specify the number of octaves that generate
 * ridged-multifractal noise by calling the {@link #setOctaveCount} method.
 * <p>
 * <b>Frequency</b>
 * <p>
 * An application may specify the frequency of the first octave by calling the
 * {@link #setFrequency} method.
 * <p>
 * <b>Lacunarity</b>
 * <p>
 * The lacunarity specifies the frequency multipler between successive octaves.<br>
 * The effect of modifying the lacunarity is subtle; you may need to play with
 * the lacunarity value to determine the effects. For best results, set the
 * lacunarity to a number between 1.5 and 3.5.
 * <p>
 * <b>References &amp; Acknowledgments</b>
 * <p>
 * <a href=http://www.texturingandmodeling.com/Musgrave.html>F. Kenton
 * "Doc Mojo" Musgrave's texturing page</a> - This page contains links to source
 * code that generates ridged-multfractal noise, among other types of noise. The
 * source file <a
 * href=http://www.texturingandmodeling.com/CODE/MUSGRAVE/CLOUD/fractal.c>
 * fractal.c</a> contains the code I used in my ridged-multifractal class (see
 * the @a RidgedMultifractal() function.) This code was written by F. Kenton
 * Musgrave, the person who created <a
 * href=http://www.pandromeda.com/>MojoWorld</a>. He is also one of the authors
 * in <i>Texturing and Modeling: A Procedural Approach</i> (Morgan Kaufmann,
 * 2002. ISBN 1-55860-848-6.)
 */
public class RidgedMulti extends Module {

	/**
	 * Maximum number of octaves for the RidgedMulti noise module.
	 */
	public static final int RIDGED_MAX_OCTAVE = 30;
	/** Frequency of the first octave. Default value is 1.0. */
	protected float frequency = 1.0f;
	/** Frequency multiplier between successive octaves. Default value is 2.0. */
	protected float lacunarity = 2.0f;

	/** Quality of the ridged-multifractal noise. Default value is QUALITY_STD. */
	protected NoiseQuality noiseQuality = NoiseQuality.QUALITY_STD;
	/**
	 * Total number of octaves that generate the ridged-multifractal noise.
	 * Default value is 6.
	 */
	protected int octaveCount = 6;
	/** Contains the spectral weights for each octave. */
	protected float[] spectralWeights = new float[RIDGED_MAX_OCTAVE];
	/**
	 * Seed value used by the ridged-multifractal-noise function. Default value
	 * is 0.
	 */
	protected int seed = 0;

	protected RidgedMulti(int sourceModuleCount) {
		super(sourceModuleCount);
	}

	@Override
	public float getValue(float x, float y, float z) {
		// Multifractal code originally written by F. Kenton "Doc Mojo"
		// Musgrave, 1998. Modified by jas for use with libnoise.
		x *= frequency;
		y *= frequency;
		z *= frequency;

		float signal = 0.0f;
		float value = 0.0f;
		float weight = 1.0f;

		// These parameters should be user-defined; they may be exposed in a
		// future version of libnoise.
		float offset = 1.0f;
		float gain = 2.0f;

		for (int curOctave = 0; curOctave < octaveCount; curOctave++) {

			// Make sure that these floating-point values have the same range as
			// a 32-bit integer so that we can pass them to the coherent-noise
			// functions.
			float nx, ny, nz;
			nx = NoiseGen.makeInt32Range(x);
			ny = NoiseGen.makeInt32Range(y);
			nz = NoiseGen.makeInt32Range(z);

			// Get the coherent-noise value.
			int seed = (this.seed + curOctave) & 0x7fffffff;
			signal = NoiseGen.gradientCoherentNoise3D(nx, ny, nz, seed,
					noiseQuality);

			// Make the ridges.
			signal = Math.abs(signal);
			signal = offset - signal;

			// Square the signal to increase the sharpness of the ridges.
			signal *= signal;

			// The weighting from the previous octave is applied to the signal.
			// Larger values have higher weights, producing sharp points along
			// the ridges.
			signal *= weight;

			// Weight successive contributions by the previous signal.
			weight = signal * gain;
			if (weight > 1.0f) {
				weight = 1.0f;
			}
			if (weight < 0.0f) {
				weight = 0.0f;
			}

			// Add the signal to the output value.
			value += (signal * spectralWeights[curOctave]);

			// Go to the next octave.
			x *= lacunarity;
			y *= lacunarity;
			z *= lacunarity;
		}

		return (value * 1.25f) - 1.0f;
	}

	/**
	 * Returns the frequency of the first octave.
	 */
	public float getFrequency() {
		return frequency;
	}

	/**
	 * Sets the frequency of the first octave.
	 * 
	 * @param frequency
	 *            The frequency of the first octave.
	 */
	public void setFrequency(float frequency) {
		this.frequency = frequency;
	}

	/**
	 * Returns the lacunarity of the ridged-multifractal noise.
	 * <p>
	 * The lacunarity is the frequency multiplier between successive octaves.
	 */
	public float getLacunarity() {
		return lacunarity;
	}

	/**
	 * Sets the lacunarity of the ridged-multifractal noise.
	 * <p>
	 * The lacunarity is the frequency multiplier between successive octaves.
	 * <p>
	 * For best results, set the lacunarity to a number between 1.5 and 3.5.
	 * 
	 * @param lacunarity
	 *            The lacunarity of the Perlin noise.
	 */
	public void setLacunarity(float lacunarity) {
		this.lacunarity = lacunarity;
		calcSpectralWeights();
	}

	/**
	 * Calculates the spectral weights for each octave.
	 * <p>
	 * This method is called when the lacunarity changes.
	 */
	protected void calcSpectralWeights() {
		// This exponent parameter should be user-defined; it may be exposed in
		// a
		// future version of libnoise.
		float h = 1.0f;

		float frequency = 1.0f;
		for (int i = 0; i < RIDGED_MAX_OCTAVE; i++) {
			// Compute weight for each frequency.
			spectralWeights[i] = (float) Math.pow(frequency, -h);
			frequency *= lacunarity;
		}
	}

	/**
	 * Returns the quality of the ridged-multifractal noise.
	 * <p>
	 * See noise.NoiseQuality for definitions of the various coherent-noise
	 * qualities.
	 */
	public NoiseQuality getNoiseQuality() {
		return noiseQuality;
	}

	/**
	 * Sets the quality of the ridged-multifractal noise.
	 * <p>
	 * See noise.NoiseQuality for definitions of the various coherent-noise
	 * qualities.
	 * 
	 * @param noiseQuality
	 *            The quality of the ridged-multifractal noise.
	 */
	public void setNoiseQuality(NoiseQuality noiseQuality) {
		this.noiseQuality = noiseQuality;
	}

	/**
	 * Returns the number of octaves that generate the ridged-multifractal
	 * noise.
	 * <p>
	 * The number of octaves controls the amount of detail in the
	 * ridged-multifractal noise.
	 */
	public int getOctaveCount() {
		return octaveCount;
	}

	/**
	 * Sets the number of octaves that generate the ridged-multifractal noise.
	 * <p>
	 * The number of octaves ranges from 1 to {@link #RIDGED_MAX_OCTAVE}.
	 * <p>
	 * The number of octaves controls the amount of detail in the
	 * ridged-multifractal noise.
	 * <p>
	 * The larger the number of octaves, the more time required to calculate the
	 * ridged-multifractal-noise value.
	 * 
	 * @param octaveCount
	 *            The number of octaves that generate the ridged-multifractal
	 *            noise.
	 * @exception IllegalArgumentException
	 *                If an invalid parameter was specified; see the
	 *                preconditions for more information.
	 */
	public void setOctaveCount(int octaveCount) {
		if (octaveCount < 1 || octaveCount > RIDGED_MAX_OCTAVE)
			throw new IllegalArgumentException();

		this.octaveCount = octaveCount;
	}

	/**
	 * Returns the seed value used by the ridged-multifractal-noise function.
	 */
	public int getSeed() {
		return seed;
	}

	/**
	 * Sets the seed value used by the ridged-multifractal-noise function.
	 * <p>
	 * 
	 * @param seed
	 *            The seed value.
	 */
	public void setSeed(int seed) {
		this.seed = seed;
	}

}
