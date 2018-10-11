package org.frc2018.auto;

import java.lang.Thread;
/**
 * This class selects, runs, and stops (if necessary) a specified autonomous mode.
 */
public class AutoModeExecuter {
    private AutoModeBase m_auto_mode;
    private Thread m_thread = null;

    public void setAutoMode(AutoModeBase new_auto_mode) {
        m_auto_mode = new_auto_mode;
    }

    public void start() {
        if (m_thread == null) {
            m_thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    if(m_auto_mode!= null) {
                        try {
                            m_auto_mode.run();
                        } catch(Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            });

            m_thread.start();
        }

    }

    public void stop() {
        if (m_auto_mode != null) {
            m_auto_mode.stop();
        }

        m_thread = null;
    }

}