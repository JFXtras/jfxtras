package jfxtras.scene.control.agenda.test;

public final class OSInfo {
	
	private OSInfo() { }
	
	public static final OS MY_OS = findOS();

	public enum OS {
		WINDOWS, UNIX, POSIX_UNIX, MAC, OTHER
	}

	private static OS findOS() {
		String osName = System.getProperty("os.name").toLowerCase();
        if (osName.indexOf("windows") != -1) {
            return OS.WINDOWS;
        } else if (osName.indexOf("linux") != -1
                || osName.indexOf("mpe/ix") != -1
                || osName.indexOf("freebsd") != -1
                || osName.indexOf("irix") != -1
                || osName.indexOf("digital unix") != -1
                || osName.indexOf("unix") != -1) {
            return OS.UNIX;
        } else if (osName.indexOf("mac os x") != -1) {
            return OS.MAC;
        } else if (osName.indexOf("sun os") != -1
                || osName.indexOf("sunos") != -1
                || osName.indexOf("solaris") != -1) {
            return OS.POSIX_UNIX;
        } else if (osName.indexOf("hp-ux") != -1
                || osName.indexOf("aix") != -1) {
            return OS.POSIX_UNIX;
        } else {
            return OS.OTHER;
        }
	}
}
