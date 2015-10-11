package noise.modules;


/**
 * Noise module that randomly displaces the input value before returning the
 * output value from a source module.
 * <p>
 * <code>Turbulence</code> is the pseudo-random displacement of the input value.
 * The {@link #getValue(float, float, float)} method randomly displaces the ( x,
 * y, z ) coordinates of the input value before retrieving the output value from
 * the source module. To control the turbulence, an application can modify its
 * frequency, its power, and its roughness.
 * <p>
 * The frequency of the turbulence determines how rapidly the displacement
 * amount changes. To specify the frequency, call the {@link #setFrequency}
 * method.
 * <p>
 * The roughness of the turbulence determines the roughness of the changes to
 * the displacement amount. Low values smoothly change the displacement amount.
 * High values roughly change the displacement amount, which produces more
 * "kinky" changes. To specify the roughness, call the {@link #setRoughness}
 * method.
 * <p>
 * Use of this noise module may require some trial and error. Assuming that you
 * are using a generator module as the source module, you should first:
 * <ul>
 * <li>Set the frequency to the same frequency as the source module.
 * <li>Set the power to the reciprocal of the frequency.
 * </ul>
 * From these initial frequency and power values, modify these values until this
 * noise module produce the desired changes in your terrain or texture. For
 * example:
 * <ul>
 * <li>Low frequency (1/8 initial frequency) and low power (1/8 initial power)
 * produces very minor, almost unnoticeable changes.
 * <li>Low frequency (1/8 initial frequency) and high power (8 times initial
 * power) produces "ropey" lava-like terrain or marble-like textures.
 * <li>High frequency (8 times initial frequency) and low power (1/8 initial
 * power) produces a noisy version of the initial terrain or texture.
 * <li>High frequency (8 times initial frequency) and high power (8 times
 * initial power) produces nearly pure noise, which isn't entirely useful.
 * </ul>
 * Displacing the input values result in more realistic terrain and textures. If
 * you are generating elevations for terrain height maps, you can use this noise
 * module to produce more realistic mountain ranges or terrain features that
 * look like flowing lava rock. If you are generating values for textures, you
 * can use this noise module to produce realistic marble-like or "oily"
 * textures.
 * <p>
 * Internally, there are three Perlin noise modules that displace the input
 * value; one for the x, one for the y, and one for the z coordinate.
 * <p>
 * This noise module requires <b>one</b> source module.
 */
public class Turbulence extends Module {

	/** The power (scale) of the displacement. Defaut value is 1.0. */
	protected float power = 1.0f;
	/** Noise module that displaces the x coordinate. */
	protected Perlin xDistortModule = new Perlin();
	/** Noise module that displaces the y coordinate. */
	protected Perlin yDistortModule = new Perlin();
	/** Noise module that displaces the z coordinate. */
	protected Perlin zDistortModule = new Perlin();

	protected Turbulence(int sourceModuleCount) {
		super(sourceModuleCount);

		setSeed(0);
		setFrequency(1.0f);
		setRoughness(3);
	}

	public Turbulence() {
		this(1);
	}

	/**
	 * Returns the frequency of the turbulence.
	 * <p>
	 * The frequency of the turbulence determines how rapidly the displacement
	 * amount changes.
	 */
	public float getFrequency() {
		// Since each Perlin noise module has the same frequency, it
		// does not matter which module we use to retrieve the frequency.
		return xDistortModule.getFrequency();
	}

	/**
	 * Returns the power of the turbulence.
	 * <p>
	 * The power of the turbulence determines the scaling factor that is applied
	 * to the displacement amount.
	 */
	public float getPower() {
		return power;
	}

	/**
	 * Returns the roughness of the turbulence.
	 * <p>
	 * The roughness of the turbulence determines the roughness of the changes
	 * to the displacement amount. Low values smoothly change the displacement
	 * amount. High values roughly change the displacement amount, which
	 * produces more "kinky" changes.
	 */
	public int getRoughnessCount() {
		return xDistortModule.getOctaveCount();
	}

	/**
	 * Returns the seed value of the internal Perlin-noise modules that are used
	 * to displace the input values.
	 * <p>
	 * Internally, there are three Perlin noise modules that displace the input
	 * value; one for the x, one for the y, and one for the z coordinate.
	 */
	public int getSeed() {
		return xDistortModule.getSeed();
	}

	/**
	 * Sets the frequency of the turbulence.
	 * <p>
	 * The frequency of the turbulence determines how rapidly the displacement
	 * amount changes.
	 * 
	 * @param frequency
	 *            The frequency of the turbulence.
	 */
	public void setFrequency(float frequency) {
		// Set the frequency of each Perlin-noise module.
		xDistortModule.setFrequency(frequency);
		yDistortModule.setFrequency(frequency);
		zDistortModule.setFrequency(frequency);
	}

	/**
	 * Sets the power of the turbulence.
	 * <p>
	 * The power of the turbulence determines the scaling factor that is applied
	 * to the displacement amount.
	 * 
	 * @param power
	 *            The power of the turbulence.
	 */
	public void setPower(float power) {
		this.power = power;
	}

	/**
	 * Sets the roughness of the turbulence.
	 * <p>
	 * The roughness of the turbulence determines the roughness of the changes
	 * to the displacement amount. Low values smoothly change the displacement
	 * amount. High values roughly change the displacement amount, which
	 * produces more "kinky" changes.
	 * <p>
	 * Internally, there are three Perlin noise modules that displace the input
	 * value; one for the x, one for the y, and one for the z coordinate. The
	 * roughness value is equal to the number of octaves used by the Perlin
	 * noise modules.
	 * 
	 * @param roughness
	 */
	public void setRoughness(int roughness) {
		// Set the octave count for each Perlin-noise module.
		xDistortModule.setOctaveCount(roughness);
		yDistortModule.setOctaveCount(roughness);
		zDistortModule.setOctaveCount(roughness);
	}

	/**
	 * Sets the seed value of the internal noise modules that are used to
	 * displace the input values.
	 * <p>
	 * Internally, there are three Perlin noise modules that displace the input
	 * value; one for the x, one for the y, and one for the z coordinate. This
	 * noise module assigns the following seed values to the Perlin noise
	 * modules:
	 * <ul>
	 * <li>It assigns the seed value (seed + 0) to the x noise module.
	 * <li>It assigns the seed value (seed + 1) to the y noise module.
	 * <li>It assigns the seed value (seed + 2) to the z noise module.
	 * </ul>
	 * 
	 * @param seed
	 *            The seed value.
	 */
	public void setSeed(int seed) {
		// Set the seed of each noise::module::Perlin noise modules.  To prevent any
		// sort of weird artifacting, use a slightly different seed for each noise
		// module.
		xDistortModule.setSeed(seed);
		yDistortModule.setSeed(seed + 1);
		zDistortModule.setSeed(seed + 2);
	}

	@Override
	public float getValue(float x, float y, float z) {
		assert (sourceModule[0] != null);

		// Get the values from the three noise::module::Perlin noise modules and
		// add each value to each coordinate of the input value. There are also
		// some offsets added to the coordinates of the input values. This
		// prevents
		// the distortion modules from returning zero if the (x, y, z)
		// coordinates,
		// when multiplied by the frequency, are near an integer boundary. This
		// is
		// due to a property of gradient coherent noise, which returns zero at
		// integer boundaries.
		float x0, y0, z0;
		float x1, y1, z1;
		float x2, y2, z2;
		x0 = x + (12414.0f / 65536.0f);
		y0 = y + (65124.0f / 65536.0f);
		z0 = z + (31337.0f / 65536.0f);
		x1 = x + (26519.0f / 65536.0f);
		y1 = y + (18128.0f / 65536.0f);
		z1 = z + (60493.0f / 65536.0f);
		x2 = x + (53820.0f / 65536.0f);
		y2 = y + (11213.0f / 65536.0f);
		z2 = z + (44845.0f / 65536.0f);
		float xDistort = x + (xDistortModule.getValue(x0, y0, z0) * power);
		float yDistort = y + (yDistortModule.getValue(x1, y1, z1) * power);
		float zDistort = z + (zDistortModule.getValue(x2, y2, z2) * power);

		// Retrieve the output value at the offsetted input value instead of the
		// original input value.
		return sourceModule[0].getValue(xDistort, yDistort, zDistort);
	}

}
