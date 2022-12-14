// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean constants. This
 * class should not be used for any other purpose. All constants should be declared globally (i.e. public static). Do
 * not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes) wherever the constants are needed, to
 * reduce verbosity.
 */
public final class Constants {
    public final class CanID {
        // Motor IDs are perhaps all wrong
        public static final int LeftMotor = 7;
        public static final int RightMotor = 1;
        public static final int DispenserMotor = 98;
        public static final int ThrowerMotor = 99;
    }

    public final class Units {
        // All distances are in meters
        public static final double mm = 0.001;
        public static final double cm = 0.01;
        public static final double meters = 1;
        public static final double inches = 1 / 39.35;
        public static final double feet = inches * 12;
    }
}
