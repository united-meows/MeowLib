package pisi.unitedmeows.meowlib;

import java.io.Serializable;
import java.util.HashMap;

import pisi.unitedmeows.meowlib.async.BasicTaskPool;
import pisi.unitedmeows.meowlib.async.ITaskPool;
import pisi.unitedmeows.meowlib.etc.IAction;
import pisi.unitedmeows.meowlib.etc.MLibSetting;
import pisi.unitedmeows.meowlib.etc.MLibSettings;
import pisi.unitedmeows.meowlib.ex.Ex;
import pisi.unitedmeows.meowlib.ex.ExceptionManager;
import pisi.unitedmeows.meowlib.variables.ubyte;
import pisi.unitedmeows.meowlib.variables.uint;

public class MeowLib {
	/* change this to meowlib map */
	private static HashMap<MLibSettings, MLibSetting<Serializable>> SETTINGS;
	private static ITaskPool taskPool;
	static {
		SETTINGS = new HashMap<>(); /* use EnumMap sometime for better performance */
		taskPool = new BasicTaskPool();
		setup();
	}

	private static void setup() {
		for (final MLibSettings setting : MLibSettings.values()) SETTINGS.put(setting, new MLibSetting<Serializable>(setting, (Serializable) setting.getValue()));
		taskPool.setup();
	}

	/* change this method name */
	public static HashMap<MLibSettings, MLibSetting<Serializable>> mLibSettings() { return SETTINGS; }

	public static ubyte ubyte(final byte value) { return new ubyte(value); }

	public static ubyte ubyte(final int value) { return new ubyte(ubyte.convert(value)); }

	public static uint uint(final int value) { return new uint(value); }

	public static uint uint(final long value) { return new uint(uint.convert(value)); }

	public static <X extends Ex> void throwEx(final X ex) { ExceptionManager.throwEx(ex); }

	public static <X extends Ex> X lastError() { return ExceptionManager.lastError(); }

	public static void useTaskPool(final ITaskPool newPool) {
		if (taskPool != null) taskPool.close();
		taskPool = newPool;
		taskPool.setup();
	}

	public static ITaskPool getTaskPool() { return taskPool; }

	public static Exception run(final IAction action) {
		try {
			action.run();
			return null;
		} catch (final Exception ex) {
			return ex;
		}
	}
}
