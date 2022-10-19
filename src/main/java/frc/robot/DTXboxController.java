package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class DTXboxController extends XboxController {
    public JoystickButton A, B, X, Y, LB, RB;

    DTXboxController(int port) {
        super(port);

        // Handy for binding commands. Need the rest of them.
        A = new JoystickButton(this, Button.kA.value);
        B = new JoystickButton(this, Button.kB.value);
        X = new JoystickButton(this, Button.kX.value);
        Y = new JoystickButton(this, Button.kY.value);
        LB = new JoystickButton(this, Button.kLeftBumper.value);
        RB = new JoystickButton(this, Button.kRightBumper.value);
    }
}
