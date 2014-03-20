//-----------------------------------------------------------------------------
//
// (C) Brandon Valosek, 2011 <bvalosek@gmail.com>
//
//-----------------------------------------------------------------------------

package com.pac.performance.cpuspy;

/**
 * main application class
 */
public class CpuSpyApp {

    /**
     * the long-living object used to monitor the system frequency states
     */
    private static CpuStateMonitor _monitor = new CpuStateMonitor();

    /**
     * @return the internal CpuStateMonitor object
     */
    public static CpuStateMonitor getCpuStateMonitor() {
        return _monitor;
    }

}
