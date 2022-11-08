// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import frc.robot.Log;
import frc.robot.Constants.CanID;
import frc.robot.Constants.Units;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DriveTrain extends SubsystemBase {
    private WPI_TalonSRX mLeftMotor;
    private WPI_TalonSRX mRightMotor;
    private DifferentialDrive mDrive;

    private final static double kGearRatio = 10.0;
    private final static double kWheelDiameter = 4.12 * Units.inches;
    private final static double kWheelCircumference = Math.PI * kWheelDiameter;
    private final static double kTicksPerRev = 2048; // Falcon encoder

    public DriveTrain() {
        mLeftMotor = new WPI_TalonSRX(CanID.LeftMotor);
        mRightMotor = new WPI_TalonSRX(CanID.RightMotor);
        mRightMotor.setInverted(true);

        mDrive = new DifferentialDrive(mLeftMotor, mRightMotor);
        addChild("Drive", mDrive);
    }

    public void arcadeDrive(double speed, double rot, boolean square, String source) {
        Log.Number("in.speed", speed, "source", source);
        Log.Number("in.rotation", rot, "source", source);
        mDrive.arcadeDrive(speed, rot, square);
    }

    public double getLeftDistance() {
        // Returns the left wheel travel distance, in meters
        double ticks = mLeftMotor.getSelectedSensorPosition();
        return encoderTicksToMeters(ticks);
    }

    public double getRightDistance() {
        // Returns the right wheel travel distance, in meters
        double ticks = mRightMotor.getSelectedSensorPosition();
        return encoderTicksToMeters(ticks);
    }

    public double getAverageDistance() {
        return (getLeftDistance() + getRightDistance()) / 2;
    }

    private static double encoderTicksToMeters(double ticks) {
        double motorRevs = ticks / kTicksPerRev;
        double wheelRevs = motorRevs / kGearRatio;
        return wheelRevs * kWheelCircumference;
    }

    // private static double metersToEncoderTicks(double meters) {
    // double wheelRevs = meters / kWheelCircumference;
    // double motorRevs = wheelRevs * kGearRatio;
    // return motorRevs * kTicksPerRev;
    // }

    private void LogMotor(WPI_TalonSRX motor, String side) {
        Log.Number("drive.pct", motor.getMotorOutputPercent(), "side", side);
    }

    public void LogData() {
        LogMotor(mLeftMotor, "left");
        LogMotor(mRightMotor, "right");
    }
}
