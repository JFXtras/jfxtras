/**
 * Copyright (c) 2011-2020, JFXtras
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *    Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *    Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *    Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL JFXRAS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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
