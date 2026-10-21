package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/*
 * Every tunable number for the robot, in one file.
 *
 * Hardware names, motor directions, maneuver powers, and autonomous timings all live here so that
 * tuning the robot means editing one file rather than hunting through the OpModes. DriveTrain reads
 * its configuration from here, and the OpModes read their maneuver parameters from here.
 *
 * Nothing in this file should be logic or state - only constants. If a value needs to change at
 * runtime, it belongs in DriveTrain, not here.
 */
public final class RobotConstants {

    private RobotConstants() {
        // Not instantiable: this class is only a namespace for the robot's constants.
    }

    // --------------------------------------------------------------------------------------------
    // Hardware map names - must match the names configured in the Robot Controller app
    // --------------------------------------------------------------------------------------------

    public static final String LEFT_MOTOR_NAME = "left_drive";
    public static final String RIGHT_MOTOR_NAME = "right_drive";

    // --------------------------------------------------------------------------------------------
    // Drive base configuration
    // --------------------------------------------------------------------------------------------

    /**
     * The direction convention for this robot, established by driving BOM_Aviva: positive power on
     * both motors drives the robot forward.
     * <p>
     * If the robot ever starts driving backwards, flip BOTH values here. Never flip a direction
     * inside an OpMode - that is how auto and teleop end up disagreeing on which way is forward.
     */
    public static final DcMotorSimple.Direction LEFT_DIRECTION = DcMotorSimple.Direction.FORWARD;
    public static final DcMotorSimple.Direction RIGHT_DIRECTION = DcMotorSimple.Direction.REVERSE;

    /**
     * What the drive motors do when power is removed.
     * <p>
     * BRAKE shorts the motor terminals. The spinning motor then acts as a generator: its back-EMF
     * drives a current through the windings, and that current produces a torque opposing the motion.
     * The torque is proportional to speed, so it fades out as the motor slows and no current flows
     * at all once the robot is stationary.
     * <p>
     * BRAKE is chosen because that deceleration comes from the motor's own constants rather than from
     * floor friction and bearing wear, so how far the robot rolls after power is cut is more
     * repeatable from match to match. FLOAT leaves the terminals open and the robot freewheels.
     * <p>
     * Two things BRAKE does not do. It cannot hold position against a push, because zero speed means
     * zero braking torque - it resists motion, not position. And braking from full speed produces a
     * brief high current spike, which is the robot's kinetic energy being dissipated in its own
     * windings rather than anything drawn from the battery.
     * <p>
     * Switch to DcMotor.ZeroPowerBehavior.FLOAT if teleop feels too jerky as the stick returns
     * through center. Note that the SDK enum is UNKNOWN / BRAKE / FLOAT - there is no COAST value,
     * despite that being the common name for it elsewhere in FTC. UNKNOWN cannot be passed to
     * setZeroPowerBehavior().
     */
    public static final DcMotor.ZeroPowerBehavior ZERO_POWER_BEHAVIOR = DcMotor.ZeroPowerBehavior.BRAKE;

    // --------------------------------------------------------------------------------------------
    // Maneuver powers - shared by autonomous legs
    // --------------------------------------------------------------------------------------------

    /** Power for straight-line driving. Lower is slower, but more repeatable. */
    public static final double DRIVE_POWER = 0.5;

    /** Power for spinning on the spot. */
    public static final double TURN_POWER = 0.5;

    // --------------------------------------------------------------------------------------------
    // Autonomous path - the values to re-measure against the field before trusting them
    // --------------------------------------------------------------------------------------------

    /** How long to drive forward out of the start zone, in seconds. */
    public static final double AUTO_DRIVE_SECONDS = 1.5;

    /** How long to spin right, in seconds. Only used when AUTO_SPIN_ENABLED is true. */
    public static final double AUTO_TURN_SECONDS = 0.576;
    public static final double DRIVE_AWAY_FROM_WALL_Seconds = 0.8;
    /**
     * Whether the autonomous path performs the spin leg.
     * <p>
     * Deliberately false for the current game: the drive-points scoring path does not need a turn,
     * so the robot drives straight in and stops. The spin itself is implemented and tested in
     * DriveTrain.spinFor(), so this stays ready for the launch path, which does need the robot
     * squared up to the element. Flip this to true when that path is added.
     */
    public static final boolean AUTO_SPIN_ENABLED = false;
}
