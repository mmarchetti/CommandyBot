// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.commands.TeleopDriveCommand;
import frc.robot.subsystems.Dispenser;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Thrower;

import edu.wpi.first.wpilibj2.command.*;
import static edu.wpi.first.wpilibj2.command.CommandGroupBase.*;

public class RobotContainer {
    // Subsystems & hardware
    private DriveTrain mDrive;
    private Thrower mThrower;
    private Dispenser mDispenser;
    private DTXboxController mController;

    // Feature flags and configuration parameters
    private final boolean haveThrower = true;
    private final double kThrowerWaitTime = 0.6;

    // Commands
    private Command mDriveCommand;
    private Command mStartThrowingCandyCommand;
    private Command mStopThrowingCandyCommand;
    private Command mAutoCommand;

    // A couple handy helpers to complement the static ones in CommandGroupBase.
    public Command wait(double seconds) {
        return new WaitCommand(seconds);
    }

    public Command instant(Runnable toRun, Subsystem... requirements) {
        return new InstantCommand(toRun, requirements);
    }

    public Command run(Runnable toRun, Subsystem... requirements) {
        return new RunCommand(toRun, requirements);
    }

    public RobotContainer() {
        mController = new DTXboxController(0);
        mDrive = new DriveTrain();

        if (haveThrower) {
            mThrower = new Thrower();
            mDispenser = new Dispenser();

            // @formatter:off
            mStartThrowingCandyCommand = sequence(
                instant(mThrower::start, mThrower),
                wait(kThrowerWaitTime),
                instant(mDispenser::start, mDispenser));

            mStopThrowingCandyCommand = sequence(
                instant(mDispenser::stop, mDispenser),
                wait(kThrowerWaitTime),
                instant(mThrower::stop, mThrower));
            // @formatter:on
        } else {
            System.out.println("Running without thrower");
        }

        mDriveCommand = new TeleopDriveCommand(mDrive, mController);
        mDrive.setDefaultCommand(mDriveCommand);
        configureButtonBindings();
    }

    private void configureButtonBindings() {
        if (haveThrower) {
            mController.X.whenPressed(mStartThrowingCandyCommand);
            mController.X.whenReleased(mStopThrowingCandyCommand);
        }
    }

    public Command getAutonomousCommand() {
        return mAutoCommand;
    }
}
