// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import frc.robot.Constants.CanID;
import frc.robot.Constants.Units;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Thrower extends SubsystemBase {
    private WPI_TalonSRX mThrower;

    private boolean running = false;

    // These are estimated and/or depend on the encoder
    private final static double kGearRatio = 10.0;
    private final static double kThrowerDiameter = 10 * Units.inches;
    private final static double kThrowerCircumference = Math.PI * kThrowerDiameter;
    private final static double kTicksPerRev = 2048;

    public Thrower() {
        mThrower = new WPI_TalonSRX(CanID.ThrowerMotor);
        mThrower.setNeutralMode(NeutralMode.Coast);
        // etc...
    }

    public void start() {
        running = true;
    }

    public void stop() {
        running = false;
    }

    private double throwerMotorVelocity(double exitSpeedMetersPerSecond) {
        // Calculate thrower motor velocity in ticks per 100ms
        double wheelRevsPerSec = exitSpeedMetersPerSecond / kThrowerCircumference;
        double motorRevsPerSec = wheelRevsPerSec * kGearRatio;
        double ticksPerSec = motorRevsPerSec * kTicksPerRev;
        return ticksPerSec / 10;
    }

    @Override
    public void periodic() {
        if (running) {
            double speed = SmartDashboard.getNumber("Thrower speed", 5.0);
            mThrower.set(TalonSRXControlMode.Velocity, throwerMotorVelocity(speed));
        } else {
            mThrower.set(TalonSRXControlMode.PercentOutput, 0);
        }
    }
}
