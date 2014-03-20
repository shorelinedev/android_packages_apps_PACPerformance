/*
 * This file is part of the RootTools Project: http://code.google.com/p/roottools/
 * 
 * Copyright (c) 2012 Stephen Erickson, Chris Ravenscroft, Dominik Schuermann, Adam Shanks
 * 
 * This code is dual-licensed under the terms of the Apache License Version 2.0 and
 * the terms of the General Public License (GPL) Version 2.
 * You may use this code according to either of these licenses as is most appropriate
 * for your project on a case-by-case basis.
 *
 * The terms of each license can be found in the root directory of this project's repository as well as at:
 *
 * * http://www.apache.org/licenses/LICENSE-2.0
 * * http://www.gnu.org/licenses/gpl-2.0.txt
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under these Licenses is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See each License for the specific language governing permissions and
 * limitations under that License.
 */

package com.stericson.roottools;

import android.util.Log;

import com.stericson.roottools.exceptions.RootDeniedException;
import com.stericson.roottools.execution.Shell;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public final class RootTools {

    // --------------------
    // # Public Variables #
    // --------------------

    public static String TAG = "Custom RootTools";

    public static boolean debugMode = false;

    /**
     * Setting this to false will disable the handler that is used by default
     * for the 3 callback methods for Command.
     * <p/>
     * By disabling this all callbacks will be called from a thread other than
     * the main UI thread.
     */
    public static boolean handlerEnabled = true;

    /**
     * Setting this will change the default command timeout.
     * <p/>
     * The default is 20000ms
     */
    public static int default_Command_Timeout = 20000;

    // ---------------------------
    // # Public Variable Getters #
    // ---------------------------

    // ------------------
    // # Public Methods #
    // ------------------

    /**
     * This will open or return, if one is already open, a shell, you are
     * responsible for managing the shell, reading the output and for closing
     * the shell when you are done using it.
     * 
     * @param timeout
     *            an <code>int</code> to Indicate the length of time to wait
     *            before giving up on opening a shell.
     * @throws TimeoutException
     * @throws RootDeniedException
     * @throws IOException
     */
    public static Shell getShell(int timeout) throws IOException,
            TimeoutException, RootDeniedException {
        return Shell.startRootShell(timeout);
    }

    /**
     * This will open or return, if one is already open, a shell, you are
     * responsible for managing the shell, reading the output and for closing
     * the shell when you are done using it.
     * 
     * @throws TimeoutException
     * @throws RootDeniedException
     * @throws IOException
     */
    public static Shell getShell() throws IOException, TimeoutException,
            RootDeniedException {
        return RootTools.getShell(25000);
    }

    /**
     * This method allows you to output debug messages only when debugging is
     * on. This will allow you to add a debug option to your app, which by
     * default can be left off for performance. However, when you need debugging
     * information, a simple switch can enable it and provide you with detailed
     * logging.
     * <p/>
     * This method handles whether or not to log the information you pass it
     * depending whether or not RootTools.debugMode is on. So you can use this
     * and not have to worry about handling it yourself.
     * 
     * @param msg
     *            The message to output.
     */
    public static void log(String msg) {
        log(msg, 3, null);
    }

    /**
     * This method allows you to output debug messages only when debugging is
     * on. This will allow you to add a debug option to your app, which by
     * default can be left off for performance. However, when you need debugging
     * information, a simple switch can enable it and provide you with detailed
     * logging.
     * <p/>
     * This method handles whether or not to log the information you pass it
     * depending whether or not RootTools.debugMode is on. So you can use this
     * and not have to worry about handling it yourself.
     * 
     * @param msg
     *            The message to output.
     * @param type
     *            The type of log, 1 for verbose, 2 for error, 3 for debug
     * @param e
     *            The exception that was thrown (Needed for errors)
     */
    public static void log(String msg, int type, Exception e) {
        if (msg != null && !msg.equals("")) {
            if (debugMode)
                switch (type) {
                case 1:
                    Log.v(TAG, msg);
                    break;
                case 2:
                    Log.e(TAG, msg, e);
                    break;
                case 3:
                    Log.d(TAG, msg);
                    break;
                }
        }
    }
}
