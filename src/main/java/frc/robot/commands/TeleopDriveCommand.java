package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveTrain;

public class TeleopDriveCommand extends CommandBase {
    private DriveTrain mDrive;
    private XboxController mController;

    public TeleopDriveCommand(DriveTrain drive, XboxController controller) {
        mDrive = drive;
        mController = controller;
        addRequirements(drive);
    }

    @Override
    public void execute() {
        double speed = mController.getLeftY();
        double rotation = mController.getRightX();
        mDrive.arcadeDrive(speed, rotation, true);
    }
}
