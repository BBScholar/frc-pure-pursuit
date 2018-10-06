package org.frc2018.subsystems;


public class Drive {
    
    private static Drive m_instance = new Drive();

    public static Drive getInstance() {
        return m_instance;
    }


    private Drive() {
        
    }


}