// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import frc.robot.Constants.CanID;

import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Dispenser extends SubsystemBase {
    private WPI_TalonSRX mMotor;

    private boolean running = false;

    public Dispenser() {
        mMotor = new WPI_TalonSRX(CanID.DispenserMotor);
        addChild("Dispenser motor", mMotor);
    }

    public void start() {
        running = true;
    }

    public void stop() {
        running = false;
    }

    @Override
    public void periodic() {
        double power = 0;
        if (running) {
            power = SmartDashboard.getNumber("Dispense power", 0.3);
            System.out.println("Setting dispenser power: " + power);
        }
        mMotor.set(TalonSRXControlMode.PercentOutput, power);
    }
}
