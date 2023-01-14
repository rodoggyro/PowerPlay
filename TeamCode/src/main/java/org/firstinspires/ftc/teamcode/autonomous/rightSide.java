package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

@Autonomous (name="Left Side Autonomous")
public class rightSide extends LinearOpMode {
    OpenCvWebcam webcam;
    Servo grabber;
    robotClass robot = new robotClass(this);

    @Override
    public void runOpMode() throws InterruptedException {
        //init robot
        robot.init(hardwareMap);

        grabber = hardwareMap.get(Servo.class, "grabber");
        //refrence TeamShippingElementDector
        int cameraMonitorViewId = hardwareMap.appContext
                .getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        TeamSleeveDetector detector = new TeamSleeveDetector(telemetry);
        webcam.setPipeline(detector);
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {

            }
        });

        //grabber close
        robot.close();
        telemetry.addLine("closed");
//
        Thread.sleep(1000);
//
//            //raise the cone out of the way
        robot.liftMotor(1, -1900);

        waitForStart();

        //Starting values for varibles
        String locationOfTSE = "none";

        if (opModeIsActive()) {
            //determining where the tse is
            switch (detector.getLocation()) {
                case TOP:
                    locationOfTSE = "top";
                    break;
                case MIDDLE:
                    locationOfTSE = "middle";
                    break;
                case BOTTOM:
                    locationOfTSE = "bottom";
                    break;
                case NOT_FOUND:
                    locationOfTSE = "not Found";
                    break;
            }

            //strafe right
            robot.strafeLeft(0.5, 1200);

            Thread.sleep(1000);
            //move forward
            robot.move(0.8, 400);

            Thread.sleep(500);

//            robot.strafeLeft(0.5, 250);
            Thread.sleep(500);
            robot.gyroTurning(-50);
            Thread.sleep(1000);

            robot.move(0.2, 200);
            robot.liftMotor(1, -1500);
            Thread.sleep(500);
            robot.open();

            robot.stopMotors();
            Thread.sleep(1000);

            //parking

            if (locationOfTSE == "bottom") {
                robot.strafeLeft(0.5, 500);
                telemetry.addLine("bottom");
                telemetry.update();
                sleep(500);

                robot.move(0.5, 1500);
            } else if (locationOfTSE == "middle") {
                robot.strafeLeft(0.5, 500);
                telemetry.addLine("Middle");
                telemetry.update();
                sleep(500);

                robot.move(0.3, 1500);
            } else if (locationOfTSE == "top") {
                telemetry.addLine("top");
                telemetry.update();
                sleep(300);

//                robot.move(0.3, 1500);
            } else if (locationOfTSE == "not Found") {
                robot.move(0.5, 1500);

            }
            robot.liftMotor(1, 0);
        }
        webcam.stopStreaming();
    }
}
