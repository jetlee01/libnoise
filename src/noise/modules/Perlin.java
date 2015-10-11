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
 * Noise module that outputs 3-dimensional Perlin noise.
 * <p>
 * Perlin noise is the sum of several coherent-noise functions of
 * ever-increasing frequencies and ever-decreasing amplitudes.
 * <p>
 * An important property of Perlin noise is that a small change in the input
 * value will produce a small change in the output value, while a large change
 * in the input value will produce a random change in the output value.
 * <p>
 * This noise module outputs Perlin-noise values that usually range from -1.0 to
 * +1.0, but there are no guarantees that all output values will exist within
 * that range.
 * <p>
 * For a better description of Perlin noise, see the links in the <i>References
 * and Acknowledgments</i> section.
 * <p>
 * This noise module does not require any source modules.
 * <p>
 * <b>Octaves</b>
 * <p>
 * The number of octaves control the <i>amount of detail</i> of the Perlin
 * noise. Adding more octaves increases the detail of the Perlin noise, but with
 * the drawback of increasing the calculation time. <br>
 * An octave is one of the coherent-noise functions in a series of
 * coherent-noise functions that are added together to form Perlin noise.<br>
 * An application may specify the frequency of the first octave by calling the
 * {@link #setFrequency} method.<br>
 * An application may specify the number of octaves that generate Perlin noise
 * by calling the {@link #setOctaveCount} method.<br>
 * These coherent-noise functions are called octaves because each octave has, by
 * default, double the frequency of the previous octave. Musical tones have this
 * property as well; a musical C tone that is one octave higher than the
 * previous C tone has double its frequency.
 * <p>
 * <b>Frequency</b>
 * <p>
 * An application may specify the frequency of the first octave by calling the
 * {@link #setFrequency} method.
 * <p>
 * <b>Persistence</b>
 * <p>
 * The persistence value controls the <i>roughness</i> of the Perlin noise.
 * Larger values produce rougher noise.<br>
 * The persistence value determines how quickly the amplitudes diminish for
 * successive octaves. The amplitude of the first octave is 1.0. The amplitude
 * of each subsequent octave is equal to the product of the previous octave's
 * amplitude and the persistence value. So a persistence value of 0.5 sets the
 * amplitude of the first octave to 1.0; the second, 0.5; the third, 0.25; etc.<br>
 * An application may specify the persistence value by calling the
 * {@link #setPersistence} method.
 * <p>
 * <b>Lacunarity</b>
 * <p>
 * The lacunarity specifies the frequency multipler between successive octaves.<br>
 * The effect of modifying the lacunarity is subtle; you may need to play with
 * the lacunarity value to determine the effects. For best results, set the
 * lacunarity to a number between 1.5 and 3.5.<br>
 * <b>References &amp; acknowledgments</b>
 * <p>
 * <a href=http://www.noisemachine.com/talk1/>The Noise Machine</a> - From the
 * master, Ken Perlin himself. This page contains a presentation that describes
 * Perlin noise and some of its variants. He won an Oscar for creating the
 * Perlin noise algorithm!<br>
 * <a href=http://freespace.virgin.net/hugo.elias/models/m_perlin.htm> Perlin
 * Noise</a> - Hugo Elias's webpage contains a very good description of Perlin
 * noise and describes its many applications. This page gave me the inspiration
 * to create libnoise in the first place. Now that I know how to generate Perlin
 * noise, I will never again use cheesy subdivision algorithms to create terrain
 * (unless I absolutely need the speed.)<br>
 * <a href=http://www.robo-murito.net/code/perlin-noise-math-faq.html>The Perlin
 * noise math FAQ</a> - A good page that describes Perlin noise in plain English
 * with only a minor amount of math. During development of libnoise, I noticed
 * that my coherent-noise function generated terrain with some "regularity" to
 * the terrain features. This page describes a better coherent-noise function
 * called <i>gradient noise</i>. This version of noise::module::Perlin uses
 * gradient coherent noise to generate Perlin noise.
 */
public class Perlin extends Module {

	public static final int PERLIN_MAX_OCTAVE = 30;
	/** Frequency of the first octave. Default value is 1.0. */
	protected float frequency = 1.0f;
	/** Frequency multiplier between successive octaves. Default value is 2.0. */
	protected float lacunarity = 2.0f;

	/** Quality of the Perlin noise. Default value is QUALITY_STD. */
	protected NoiseQuality noiseQuality = NoiseQuality.QUALITY_STD;
	/**
	 * Total number of octaves that generate the Perlin noise. Default value is
	 * 6.
	 */
	protected int octaveCount = 6;
	/** Persistence of the Perlin noise. Default value is 0.5. */
	protected float persistence = 0.5f;
	/** Seed value used by the Perlin-noise function. Default value is 0. */
	protected int seed = 0;

	public Perlin() {
		this(0);
	}

	protected Perlin(int sourceModuleCount) {
		super(sourceModuleCount);
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
			signal = NoiseGen.gradientCoherentNoise3D(nx, ny, nz, seed,
					noiseQuality);
			value += signal * curPersistence;

			// Prepare the next octave.
			x *= lacunarity;
			y *= lacunarity;
			z *= lacunarity;
			curPersistence *= persistence;
		}

		return value;
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
	 * Returns the lacunarity of the Perlin noise.
	 * <p>
	 * The lacunarity is the frequency multiplier between successive octaves.
	 */
	public float getLacunarity() {
		return lacunarity;
	}

	/**
	 * Sets the lacunarity of the Perlin noise.
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
	}

	/**
	 * Returns the quality of the Perlin noise.
	 * <p>
	 * See noise.NoiseQuality for definitions of the various coherent-noise
	 * qualities.
	 */
	public NoiseQuality getNoiseQuality() {
		return noiseQuality;
	}

	/**
	 * Sets the quality of the Perlin noise.
	 * <p>
	 * See noise.NoiseQuality for definitions of the various coherent-noise
	 * qualities.
	 * 
	 * @param noiseQuality
	 *            The quality of the Perlin noise.
	 */
	public void setNoiseQuality(NoiseQuality noiseQuality) {
		this.noiseQuality = noiseQuality;
	}

	/**
	 * Returns the number of octaves that generate the Perlin noise.
	 * <p>
	 * The number of octaves controls the amount of detail in the Perlin noise.
	 */
	public int getOctaveCount() {
		return octaveCount;
	}

	/**
	 * Sets the number of octaves that generate the Perlin noise.
	 * <p>
	 * The number of octaves ranges from 1 to {@link #PERLIN_MAX_OCTAVE}.
	 * <p>
	 * The number of octaves controls the amount of detail in the Perlin noise.
	 * <p>
	 * The larger the number of octaves, the more time required to calculate the
	 * Perlin-noise value.
	 * 
	 * @param octaveCount
	 *            The number of octaves that generate the Perlin noise.
	 * @exception IllegalArgumentException
	 *                If an invalid parameter was specified; see the
	 *                preconditions for more information.
	 */
	public void setOctaveCount(int octaveCount) {
		if (octaveCount < 1 || octaveCount > PERLIN_MAX_OCTAVE)
			throw new IllegalArgumentException();

		this.octaveCount = octaveCount;
	}

	/**
	 * Returns the persistence value of the Perlin noise.
	 * <p>
	 * The persistence value controls the roughness of the Perlin noise.
	 */
	public float getPersistence() {
		return persistence;
	}

	/**
	 * Sets the persistence value of the Perlin noise.
	 * <p>
	 * The persistence value controls the roughness of the Perlin noise.
	 * <p>
	 * For best results, set the persistence to a number between 0.0 and 1.0.
	 * 
	 * @param persistence
	 *            The persistence value of the Perlin noise.
	 */
	public void setPersistence(float persistence) {
		this.persistence = persistence;
	}

	/**
	 * Returns the seed value used by the Perlin-noise function.
	 */
	public int getSeed() {
		return seed;
	}

	/**
	 * Sets the seed value used by the Perlin-noise function.
	 * <p>
	 * 
	 * @param seed
	 *            The seed value.
	 */
	public void setSeed(int seed) {
		this.seed = seed;
	}
}
