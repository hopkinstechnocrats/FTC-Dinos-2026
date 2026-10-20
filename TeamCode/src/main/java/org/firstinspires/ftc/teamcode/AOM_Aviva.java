package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/*
 * Autonomous OpMode: drive out of the start zone and score the drive points.
 *
 * All driving goes through DriveTrain, which is shared with BOM_Aviva, so this OpMode uses the same
 * motor names and the same left/right direction convention as teleop. The maneuver timings come
 * from RobotConstants along with every other tunable number on the robot.
 *
 * The spin leg is present but switched off via RobotConstants.AUTO_SPIN_ENABLED - the current
 * drive-points path needs no turn. It is ready for the launch path, which does.
 *
 * Timings are time based, so they drift with battery voltage and load. Re-measure them against the
 * actual field marks before trusting them in a match.
 */
@Autonomous(name = "AOM_Aviva", group = "Aviva")
public class AOM_Aviva extends LinearOpMode {

    private DriveTrain driveTrain;

    @Override
    public void runOpMode() {
        // Looks up the drive motors and applies the shared direction and braking configuration.
        driveTrain = new DriveTrain(this);

        telemetry.addData("Status", "Initialized");
        telemetry.addData("Path", "Waiting for START");
        telemetry.update();

        // Wait for the game to start (driver presses START)
        waitForStart();

        // Each maneuver powers the wheels off when it finishes, and ends early if the driver presses
        // STOP or the match clock expires.

        // Drive forward out of the start zone.
        telemetry.addLine("Driving forward");
        telemetry.update();
        driveTrain.driveFor(RobotConstants.DRIVE_POWER, RobotConstants.AUTO_DRIVE_SECONDS);

        // Spin right to face the element. Off for the current game; see AUTO_SPIN_ENABLED.
        if (RobotConstants.AUTO_SPIN_ENABLED) {
            telemetry.addLine("Spinning right");
            telemetry.update();
            driveTrain.spinFor(RobotConstants.TURN_POWER, RobotConstants.AUTO_TURN_SECONDS);
        }

        // Complete - DriveTrain has already braked both wheels to a stop.
        telemetry.addLine("Complete");
        telemetry.update();
    }
}
