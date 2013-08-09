package edu.kit.iti.algo2.pse2013.walkaround.shared;

import java.io.File;

public class DropboxUtil {
	public static String getDropbox() {
		if (System.getProperty("user.name").equals("Lukas")) {
			return System.getProperty("user.home") + File.separatorChar + "Desktop" + File.separatorChar + "Dropbox" + File.separatorChar + "Studium" + File.separatorChar + "PSE";
		}
		return System.getProperty("user.home") + File.separatorChar + "Dropbox" + File.separatorChar + "Studium" + File.separatorChar + "PSE";
	}
}
