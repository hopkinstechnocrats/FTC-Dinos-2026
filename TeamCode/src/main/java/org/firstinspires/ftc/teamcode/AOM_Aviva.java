package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

// The @Autonomous annotation registers this code on the Driver Station menu
@Autonomous(name="AOM_Aviva", group="Robot")
public class AOM_Aviva extends LinearOpMode {

    // Declare hardware variables (Adjust these to match your robot configuration)
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;

    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        // Send telemetry to the Driver Station
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables.
        // The strings here must match the names configured on your Robot Controller / Driver Hub
        leftDrive  = hardwareMap.get(DcMotor.class, "left_drive");
        rightDrive = hardwareMap.get(DcMotor.class, "right_drive");

        // Most robots need one motor reversed to drive forward cleanly
        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // Step 1: Drive Forward for 1.5 seconds
        telemetry.addData("Path", "Driving Forward");
        telemetry.update();

        leftDrive.setPower(0.5);
        rightDrive.setPower(0.5);
        sleep(700); // Time in milliseconds

        // Step 2: Spin/Turn Right for 0.8 seconds
        telemetry.addData("Path", "Turning Right");
        telemetry.update();

        leftDrive.setPower(0);
        rightDrive.setPower(0);
        sleep(0);

        // Step 3: Stop the robot
        telemetry.addData("Path", "Complete");
        telemetry.update();

        leftDrive.setPower(0.0);
        rightDrive.setPower(0.0);
    }
}
