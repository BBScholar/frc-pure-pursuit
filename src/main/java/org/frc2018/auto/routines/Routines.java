package org.frc2018.auto.routines;

import java.util.ArrayList;
import org.frc2018.auto.actions.*;
import org.frc2018.auto.actions.ArmAction.ArmDirection;
import org.frc2018.auto.actions.ArmAction.IntakeDirection;

public class Routines {
    public static ArrayList<Routine> ROUTINES = new ArrayList<Routine>();
    private static Routine ro_oc, ri_tc, m_tc_r, m_tc_l, li_tc, lo_oc, baseline, nothing, tuning;


    static {
        // add routines to ROUTINES
        ro_oc = new Routine("rightouter_onecube");
        ri_tc = new Routine("rightinner_twocube");
        m_tc_l = new Routine("middle_twocube_left");
        m_tc_r = new Routine("middle_twocube_right");
        li_tc = new Routine("leftinner_twocube");
        lo_oc = new Routine("leftouter_onecube");
        baseline = new Routine("baseline");
        nothing = new Routine("nothing");
        tuning = new Routine("tuning");

        ROUTINES.add(ro_oc);
        ROUTINES.add(ri_tc);
        ROUTINES.add(m_tc_l);
        ROUTINES.add(m_tc_r);
        ROUTINES.add(li_tc);
        ROUTINES.add(lo_oc);
        ROUTINES.add(baseline);
        ROUTINES.add(nothing);
        ROUTINES.add(tuning);

        // define routines

        // ro_oc.addAction(new NothingCommand(7.0));
        ro_oc.addAction(new NothingAction(0.0));
        ro_oc.addAction(new ArmAction(ArmDirection.UP, IntakeDirection.HOLD, 0.4));
        ro_oc.addAction(new DriveCommand(3, false, -150));
        ro_oc.addAction(new TurnCommand(2, false, 90));
        ro_oc.addAction(new DriveCommand(1.5, false, -30));
        ro_oc.addAction(new DriveSlowCommand(1, false, -10));
        ro_oc.addAction(new ArmAction(0, true, ArmDirection.UP, 0.4));
        ro_oc.addAction(new OuttakeDriveCommand(1, true, 0.6));




        m_tc_l.addAction(new NothingCommand(0));
        m_tc_l.addAction(new ArmAction(0, true, true, 110));
        m_tc_l.addAction(new DriveCommand(2, false, -45));
        m_tc_l.addAction(new TurnCommand(2, false, 90));
        m_tc_l.addAction(new DriveCommand(2, false, -42));
        m_tc_l.addAction(new TurnCommand(2, false, -90));
        m_tc_l.addAction(new DriveCommand(1.5, false, -48));
        m_tc_l.addAction(new DriveSlowCommand(1, false, -10));
        m_tc_l.addAction(new ArmAction(0, true, false, 110));
        m_tc_l.addAction(new OuttakeDriveCommand(0.25, true, 0.6));
        // Two cube section
        m_tc_l.addAction(new DriveCommand(2, false, 65));
        m_tc_l.addAction(new ArmAction(0.5, true, true, -50));
        m_tc_l.addAction(new ArmAction(0, false, true, -50));
        m_tc_l.addAction(new TurnCommand(2, false, 135));
        m_tc_l.addAction(new ArmAction(0, false, false, -25));
        m_tc_l.addAction(new DriveCommand(1.25, false, 48));
        m_tc_l.addAction(new IntakeDriveCommand(1.5, false, 32, -1.0, false));
        m_tc_l.addAction(new IntakeCommand(0.4, -0.6, false));
        m_tc_l.addAction(new ArmAction(0.5, true, true, 110));
        m_tc_l.addAction(new DriveCommand(2, false, -50));
        m_tc_l.addAction(new TurnCommand(2, false, -135));
        m_tc_l.addAction(new DriveCommand(1.8, false, -52));
        m_tc_l.addAction(new DriveSlowCommand(0.5, false, -10));
        m_tc_l.addAction(new ArmAction(0, true, false, 110));
        m_tc_l.addAction(new OuttakeDriveCommand(1, true, 0.4));

        m_tc_r.addAction(new NothingCommand(0));
        m_tc_r.addAction(new ArmAction(0, true, true, 110));
        m_tc_r.addAction(new DriveCommand(2, false, -45));
        m_tc_r.addAction(new TurnCommand(2, false, -90));
        m_tc_r.addAction(new DriveCommand(2, false, -47));
        m_tc_r.addAction(new TurnCommand(2, false, 90));
        m_tc_r.addAction(new DriveCommand(1.5, false, -48));
        m_tc_r.addAction(new DriveSlowCommand(0.5, false, -10));
        m_tc_r.addAction(new ArmAction(0, true, false, 110));
        m_tc_r.addAction(new OuttakeDriveCommand(0.25, true, 0.6));
        // Two cube section
        m_tc_r.addAction(new DriveCommand(2, false, 60));
        m_tc_r.addAction(new ArmAction(0.5, true, true, -50));
        m_tc_r.addAction(new ArmAction(0, false, true, -50));
        m_tc_r.addAction(new TurnCommand(2, false, -135));
        m_tc_r.addAction(new ArmAction(0, false, false, -25));
        m_tc_r.addAction(new DriveCommand(1, false, 35));
        m_tc_r.addAction(new IntakeDriveCommand(1.5, false, 32, -1.0, false));
        m_tc_r.addAction(new IntakeCommand(0.4, -0.6, false));
        m_tc_r.addAction(new ArmAction(0.5, true, true, 110));
        m_tc_r.addAction(new DriveCommand(1.8, false, -50));
        m_tc_r.addAction(new TurnCommand(2, false, 135));
        m_tc_r.addAction(new DriveCommand(1.8, false, -52));
        m_tc_r.addAction(new DriveSlowCommand(0.5, false, -10));
        m_tc_r.addAction(new ArmAction(0, true, false, 110));
        m_tc_r.addAction(new OuttakeDriveCommand(1, true, 0.4));

        // lo_oc.addAction(new NothingCommand(7.0));
        lo_oc.addAction(new NothingCommand(0.0));
        lo_oc.addAction(new ArmAction(0, true, true, 110));
        lo_oc.addAction(new DriveCommand(3, false, -150));
        lo_oc.addAction(new TurnCommand(1.5, false, -90));
        lo_oc.addAction(new DriveCommand(1.5, false, -23));
        lo_oc.addAction(new ArmAction(0, true, false, 110));
        lo_oc.addAction(new OuttakeDriveCommand(1, true, 0.6));

        // drives 90 inches(just enough to cross baseline)
        // lo_nc.addAction(new NothingCommand(7.0));

        baseline.addAction(new ArmAction(0, true, false, 110));
        baseline.addAction(new DriveCommand(3, false, -106));
        //baseline.addAction(new TurnCommand(10, false, 90));

        nothing.addAction(new NothingCommand(0));

       // tuning.addAction(new ArmAction(1, true, true, 50));
        //tuning.addAction(new ArmAction(1, true, true, 80));
        tuning.addAction(new DriveCommand(3, false, -20));
        tuning.addAction(new TurnCommand(2, false, 90));
        tuning.addAction(new ArmAction(0, true, false, 110));
        tuning.addAction(new IntakeDriveCommand(10, false, 200, -1, true));
        tuning.addAction(new NothingCommand(10));

    }
}