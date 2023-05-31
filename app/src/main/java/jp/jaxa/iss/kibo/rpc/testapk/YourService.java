package jp.jaxa.iss.kibo.rpc.testapk;

import org.opencv.core.Mat;
import java.util.HashMap;
import gov.nasa.arc.astrobee.types.Point;
import gov.nasa.arc.astrobee.types.Quaternion;
import jp.jaxa.iss.kibo.rpc.api.KiboRpcService;
import java.util.List;

/**
 * Class meant to handle commands from the Ground Data System and execute them in Astrobee
 */

public class YourService extends KiboRpcService {
    //storing coordinate/orientation data of targets 1-6, start, end, QR in hash maps
    final static int START = 0;
    final static int END = 8;
    final static int QR = 7;
    static HashMap<Integer, Point> coords = new HashMap<>();
    static HashMap<Integer, Quaternion> orientations = new HashMap<>();
    static{
        coords.put(1, new Point(11.2625, -10.58, 5.3625));
        coords.put(2, new Point(10.513384, -9.085172, 3.76203));
        coords.put(3, new Point(10.6031, -7.71007, 3.76093));
        coords.put(4, new Point(9.866984, -6.673972, 5.09531));
        coords.put(5, new Point(11.102, -8.0304, 5.9076));
        coords.put(6, new Point(12.023, -8.989, 4.8305));
        orientations.put(1, new Quaternion(0.707F,0,0,0.707F));
        orientations.put(2, new Quaternion(0,0,0,1F));
        orientations.put(3, new Quaternion(0.707F,0,0,0.707F));
        orientations.put(4, new Quaternion(-0.5F,0.5F,-0.5F,0.5F));
        orientations.put(5, new Quaternion(1,0,0,0));
        orientations.put(6, new Quaternion(0.5F,0.5F,-0.5F,-0.5F));
        coords.put(START, new Point(9.815, -9.806, 4.293));
        coords.put(END, new Point(11.143, -6.7607, 4.9654));
        coords.put(QR, new Point(11.381944, -8.566172, 3.76203));
        orientations.put(START, new Quaternion(1,0,0,0));
        orientations.put(END, new Quaternion(0,0,-0.707F, 0.707F));
        orientations.put(QR, new Quaternion(0,0,0,1));
    }
    static String yourMethod(){
        return "Hello";
    }


    @Override
    protected void runPlan1(){
            // the mission starts
            api.startMission();
            int loop_counter = 0;
            while (true){
                // get the list of active target id
                List<Integer> list = api.getActiveTargets();
                // move to a point
                Point point = new Point(10.4d, -10.1d, 4.47d);
                Quaternion quaternion = new Quaternion(0f, 0f, 0f, 1f);
                api.moveTo(point, quaternion, false);
                // get a camera image
                Mat image = api.getMatNavCam();
                // irradiate the laser
                api.laserControl(true);
                // take active target snapshots
                int target_id = 1;
                api.takeTargetSnapshot(target_id);
                /* ************************************************ */
                /* write your own code and repair the ammonia leak! */
                /* ************************************************ */
                // get remaining active time and mission time
                List<Long> timeRemaining = api.getTimeRemaining();
                // check the remaining milliseconds of mission time
                if (timeRemaining.get(1) < 60000){
                    break;
                }
                loop_counter++;
                if (loop_counter == 2){
                    break;
                }
            }
        // turn on the front flash light
        api.flashlightControlFront(0.05f);

        // get QR code content
        String mQrContent = yourMethod();
        // turn off the front flash light
        api.flashlightControlFront(0.00f);
        // notify that astrobee is heading to the goal
        api.notifyGoingToGoal();
        /* ********************************************************** */
        /* write your own code to move Astrobee to the goal positiion */
        /* ********************************************************** */
        // send mission completion
        api.reportMissionCompletion(mQrContent);
    }

    @Override
    protected void runPlan2(){
        // write your plan 2 here
    }

    @Override
    protected void runPlan3(){
        // write your plan 3 here
    }

}

