package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/*
 * The team's shared tank-drive base.
 *
 * Any OpMode that moves the robot should go through this class rather than holding its own copy of
 * the drive motors. It keeps in one place the three things that were previously duplicated in every
 * OpMode, and therefore free to drift out of sync:
 *   - the drive motor names used by the Driver Station robot map
 *   - the left/right direction convention, so teleop and auto agree on which way is forward
 *   - the rule that the robot only moves while the OpMode is in its Run phase
 *
 * This class holds behavior only, never tunable values: the hardware names, directions, zero power
 * behavior and maneuver parameters all come from RobotConstants. Tune the robot there.
 *
 * Maneuvers here are time based. That is repeatable enough for a short scoring auto, but the distance
 * travelled still drifts with battery voltage, motor wear and floor surface. If encoders are mapped
 * to the drive wheels later, add distance based methods to this class and the OpModes that call it
 * will not need to change shape.
 */
public class DriveTrain {

    private final LinearOpMode opMode;
    private final DcMotor leftDrive;
    private final DcMotor rightDrive;

    /** Reused by every timed maneuver, so no maneuver allocates its own timer. */
    private final ElapsedTime maneuverTimer = new ElapsedTime();

    private double lastLeftPower;
    private double lastRightPower;

    /**
     * Looks up the drive motors and configures them.
     *
     * @param opMode the OpMode that owns this drive base; supplies the hardware map and telemetry,
     *               and is watched so that maneuvers stop when the OpMode stops
     */
    public DriveTrain(LinearOpMode opMode) {
        this.opMode = opMode;

        HardwareMap hardwareMap = opMode.hardwareMap;
        leftDrive = hardwareMap.get(DcMotor.class, RobotConstants.LEFT_MOTOR_NAME);
        rightDrive = hardwareMap.get(DcMotor.class, RobotConstants.RIGHT_MOTOR_NAME);

        leftDrive.setDirection(RobotConstants.LEFT_DIRECTION);
        rightDrive.setDirection(RobotConstants.RIGHT_DIRECTION);

        // BRAKE shorts the terminals so each maneuver decelerates using the motor's own back-EMF
        // instead of rolling on friction. Configured in RobotConstants.
        leftDrive.setZeroPowerBehavior(RobotConstants.ZERO_POWER_BEHAVIOR);
        rightDrive.setZeroPowerBehavior(RobotConstants.ZERO_POWER_BEHAVIOR);
    }

    /**
     * Drives straight for a fixed time, then powers the wheels off.
     *
     * @param power   -1.0 to 1.0; positive drives forward, negative drives backward
     * @param seconds how long to hold that power
     */
    public void driveFor(double power, double seconds) {
        runFor(power, power, seconds);
    }

    /**
     * Spins the robot on the spot for a fixed time, then powers the wheels off.
     *
     * @param power   -1.0 to 1.0; positive spins to the right (clockwise), negative to the left
     * @param seconds how long to hold that power
     */
    public void spinFor(double power, double seconds) {
        runFor(power, -power, seconds);
    }

    /**
     * POV / arcade drive for teleop: blends forward-reverse with turning.
     * Call once per game loop iteration; the wheels keep their power until the next call.
     *
     * @param drive -1.0 to 1.0 forward / reverse stick value
     * @param turn  -1.0 to 1.0 left / right stick value
     */
    public void driveArcade(double drive, double turn) {
        setPower(drive + turn, drive - turn);
    }

    /**
     * Tank drive for teleop: sets each wheel independently.
     *
     * @param leftPower  -1.0 to 1.0 power for the left wheel
     * @param rightPower -1.0 to 1.0 power for the right wheel
     */
    public void tankDrive(double leftPower, double rightPower) {
        setPower(leftPower, rightPower);
    }

    /**
     * Removes power from both wheels. With the default BRAKE behavior the motors decelerate using
     * their own back-EMF instead of freewheeling, so the robot stops at a repeatable spot.
     */
    public void stop() {
        setPower(0.0, 0.0);
    }

    /** Power last commanded to the left wheel, for telemetry. */
    public double getLeftPower() {
        return lastLeftPower;
    }

    /** Power last commanded to the right wheel, for telemetry. */
    public double getRightPower() {
        return lastRightPower;
    }

    /**
     * Holds the given wheel powers for a fixed time, then powers them off.
     * <p>
     * This is the only place timing is handled, so every maneuver inherits the same safety rule: the
     * wheels are only powered while the OpMode is in its Run phase. If the driver presses STOP, or the
     * match clock expires, the maneuver ends immediately instead of running out its remaining time.
     * That matters because LinearOpMode.sleep() is only shorthand for Thread.sleep() and does not
     * notice STOP on its own.
     */
    private void runFor(double leftPower, double rightPower, double seconds) {
        setPower(leftPower, rightPower);
        maneuverTimer.reset();

        while (opMode.opModeIsActive() && maneuverTimer.seconds() < seconds) {
            opMode.telemetry.addData("Maneuver", "%.2f s of %.2f s", maneuverTimer.seconds(), seconds);
            opMode.telemetry.update();
        }

        stop();
    }

    /** Sends powers to the wheels, clamped to the legal -1.0 to 1.0 range. */
    private void setPower(double leftPower, double rightPower) {
        lastLeftPower = Range.clip(leftPower, -1.0, 1.0);
        lastRightPower = Range.clip(rightPower, -1.0, 1.0);
        leftDrive.setPower(lastLeftPower);
        rightDrive.setPower(lastRightPower);
    }
}
