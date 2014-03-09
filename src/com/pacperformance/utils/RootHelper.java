package com.pacperformance.utils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.roottools.RootTools;
import com.roottools.exceptions.RootDeniedException;
import com.roottools.execution.CommandCapture;

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
