package surge.util;

import org.bukkit.Bukkit;

public enum Protocol
{
	LATEST(10000),
	R1_12_2(340),
	R1_12_2_PRE(339),
	R1_12_1(338),
	R1_12(335),
	R1_11_2(316),
	R1_11_1(316),
	R1_11(315),
	R1_10_2(210),
	R1_10_1(210),
	R1_10(210),
	R1_9_4(110),
	R1_9_3(110),
	R1_9_2(109),
	R1_9_1(108),
	R1_9(107),
	R1_8_9(47),
	R1_8_8(47),
	R1_8_7(47),
	R1_8_6(47),
	R1_8_5(47),
	R1_8_4(47),
	R1_8_3(47),
	R1_8_2(47),
	R1_8_1(47),
	R1_8(47),
	R1_7_10(5),
	R1_7_9(5),
	R1_7_8(5),
	R1_7_7(5),
	R1_7_6(5),
	R1_7_5(4),
	R1_7_4(4),
	R1_7_3(4),
	R1_7_2(4),
	R1_7_1(4),
	B1_6_4(78),
	B1_6_3(77),
	B1_6_2(74),
	B1_6_1(73),
	B1_5_2(61),
	B1_5_1(60),
	B1_5(60),
	B1_4_7(51),
	B1_4_6(51),
	B1_4_5(49),
	B1_4_4(49),
	B1_4_2(47),
	B1_3_2(39),
	B1_3_1(39),
	B1_2_5(29),
	B1_2_4(29),
	EARLIEST(0),
	UNKNOWN(-10000);

	private int version;
	private String search;

	private Protocol(int version)
	{
		search = toString().replaceAll("_", ".").substring(1);
		this.version = version;

		if(toString().replaceAll("_", ".").startsWith("B") || toString().replaceAll("_", ".").startsWith("E"))
		{
			version -= 1000;
		}
	}

	public static Protocol getProtocolVersion()
	{
		for(Protocol i : values())
		{
			if(i.isServerVersion())
			{
				return i;
			}
		}

		return Protocol.UNKNOWN;
	}

	public ProtocolRange to(Protocol p)
	{
		return new ProtocolRange(this, p);
	}

	public boolean isServerVersion()
	{
		return Bukkit.getBukkitVersion().startsWith(getVersionString());
	}

	public String getVersionString()
	{
		return search;
	}

	public boolean isNettySupported()
	{
		return !toString().replaceAll("_", ".").startsWith("B");
	}

	public boolean isActualVersion()
	{
		return toString().replaceAll("_", ".").startsWith("B") || toString().replaceAll("_", ".").startsWith("R");
	}

	public int getVersion()
	{
		if(isActualVersion() && !isNettySupported())
		{
			return getMetaVersion() + 1000;
		}

		return getMetaVersion();
	}

	public int getMetaVersion()
	{
		return version;
	}
}
