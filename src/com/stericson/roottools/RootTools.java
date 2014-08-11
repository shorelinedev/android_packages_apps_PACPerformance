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

// Modified by Willi Ye

package com.stericson.roottools;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import android.util.Log;

import com.pac.performance.utils.Constants;

public final class RootTools implements Constants {

    // --------------------
    // # Public Variables #
    // --------------------

    /**
     * Setting this to false will disable the handler that is used by default
     * for the 3 callback methods for Command.
     * 
     * By disabling this all callbacks will be called from a thread other than
     * the main UI thread.
     */
    public static boolean handlerEnabled = true;

    /**
     * Setting this will change the default command timeout.
     * 
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

    @SuppressWarnings("deprecation")
    public static String getOutput(String command) throws IOException,
            InterruptedException {
        Process process = Runtime.getRuntime().exec("sh");
        final DataOutputStream processStream = new DataOutputStream(
                process.getOutputStream());
        processStream.writeBytes("exec " + command + "\n");
        processStream.flush();

        int exit = 1;
        String output = null;
        if (process != null) {
            exit = process.waitFor();

            StringBuffer buffer = null;
            final DataInputStream inputStream = new DataInputStream(
                    process.getInputStream());

            if (inputStream.available() > 0) {
                buffer = new StringBuffer(inputStream.readLine());
                while (inputStream.available() > 0)
                    buffer.append("\n").append(inputStream.readLine());
            }
            inputStream.close();
            if (buffer != null) output = buffer.toString();
        }

        Log.d(TAG, "Output of " + command + ": " + output);

        return exit != 1 && exit == 0 ? output : "error";
    }
}