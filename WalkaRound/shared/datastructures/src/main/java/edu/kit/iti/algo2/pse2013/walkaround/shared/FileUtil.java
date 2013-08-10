package edu.kit.iti.algo2.pse2013.walkaround.shared;

import java.io.File;

public class FileUtil {

	public static File getFile(String file) {
        // special cake for Mr. Lukas
		if ("Lukas".equals(System.getProperty("user.name")))
			return new File(System.getProperty("user.home") + File.separatorChar + "Desktop" + File.separatorChar + "Dropbox" + File.separatorChar + "Studium" + File.separatorChar + "PSE" + File.separatorChar + file);

        // check dropbox dir
        File path = new File(System.getProperty("user.home") + File.separatorChar + "Dropbox" + File.separatorChar + "Studium" + File.separatorChar + "PSE" + File.separatorChar + file);
        if (path.getParentFile().exists())
		    return path;

        // check resources dir (server only)
        path = new File("/home/walkaround/resources" + File.separatorChar + file);
        if (path.exists())
            return path;

        // check resources dir
        path = new File(FileUtil.class.getResource(File.separatorChar + file).getFile());
        if (path.getParentFile().exists())
            return path;

        throw new RuntimeException("file not available");
	}
}
