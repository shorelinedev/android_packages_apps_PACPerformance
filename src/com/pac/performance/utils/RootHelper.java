package com.pac.performance.utils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.stericson.roottools.RootTools;
import com.stericson.roottools.exceptions.RootDeniedException;
import com.stericson.roottools.execution.CommandCapture;

public class RootHelper {

	public static void run(String run) {
		try {
			RootTools.getShell(true).add(new CommandCapture(0, run))
					.commandCompleted(0, 0);
		} catch (IOException e) {
		} catch (TimeoutException e) {
		} catch (RootDeniedException e) {
		}
	}

}
