package com.pac.performance.helpers;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.exceptions.RootDeniedException;
import com.stericson.RootTools.execution.CommandCapture;

public class RootHelper {

	public static void run(String run) {
		try {
			RootTools.getShell(true).add(new CommandCapture(100, run))
					.commandCompleted(0, 0);
		} catch (IOException e) {
		} catch (TimeoutException e) {
		} catch (RootDeniedException e) {
		}
	}

}
