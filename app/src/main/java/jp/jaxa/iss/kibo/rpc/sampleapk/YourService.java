package jp.jaxa.iss.kibo.rpc.sampleapk;
import android.util.Log;

import java.util.*;
import  jp.jaxa.iss.kibo.rpc.api.KiboRpcService;

import gov.nasa.arc.astrobee.Result;
import gov.nasa.arc.astrobee.android.gs.MessageType;
import android.os.SystemClock;
import gov.nasa.arc.astrobee.types.Point;
import gov.nasa.arc.astrobee.types.Quaternion;
import org.opencv.core.Mat;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.QRCodeDetector;
import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
/**
 * Class meant to handle commands from the Ground Data System and execute them in Astrobee
 */

public class YourService extends KiboRpcService {
////////////////////////
    ////// INITIALIZE//////
    //////////////////////

    @Override
    protected void runPlan1() {

        // the mission starts
        int point1;
        int point2;
        int current_node=2;
        String mQrContent="";
        List<Integer> target_node = new ArrayList<>();
        long time_remain_before=0;

        int closestNode = 0;

        int  start=0;
        int end=2;
        Map<Integer, Map<Integer, Double>> graph = new HashMap<>();
        graph.put(2, new HashMap<Integer, Double>());
        graph.put(3, new HashMap<Integer, Double>());
        graph.put(4, new HashMap<Integer, Double>());
        graph.put(5, new HashMap<Integer, Double>());
        graph.put(6, new HashMap<Integer, Double>());
        graph.put(7, new HashMap<Integer, Double>());
        graph.put(8, new HashMap<Integer, Double>());
        graph.put(9, new HashMap<Integer, Double>());
        graph.put(10, new HashMap<Integer, Double>());
        graph.put(11, new HashMap<Integer, Double>());
        graph.put(12, new HashMap<Integer, Double>());
        graph.put(13, new HashMap<Integer, Double>());
        graph.put(14, new HashMap<Integer, Double>());
        graph.put(15, new HashMap<Integer, Double>());
        graph.put(16, new HashMap<Integer, Double>());
        graph.get(2).put(3, 22092.0);
        graph.get(2).put(4, 19996.0);
        graph.get(2).put(5, 21344.0);
        graph.get(2).put(8, 28720.0);
        graph.get(2).put(10, 31752.0);
        graph.get(2).put(14, 39912.0);
        graph.get(3).put(4, 27472.0);
        graph.get(3).put(5, 24284.0);
        graph.get(3).put(8, 31666.6);
        graph.get(3).put(10, 36000.0);
        graph.get(3).put(14, 42206.0);
        graph.get(3).put(15, 37656.0);
        graph.get(4).put(5, 24280.0);
        graph.get(4).put(8, 27718.6);
        graph.get(4).put(6, 20917.3);
        graph.get(6).put(10, 18628.0);
        graph.get(4).put(14, 37928.0);
        graph.get(4).put(15, 34352.0);
        graph.get(5).put(8, 26665.6);
        graph.get(5).put(10, 30304.0);
        graph.get(5).put(14, 38760.0);
        graph.get(5).put(16, 33312.0);
        graph.get(8).put(11, 14548.0);
        graph.get(11).put(10, 23582.0);
        graph.get(8).put(14, 30340.0);
        graph.get(8).put(15, 25256.0);
        graph.get(10).put(14, 28022.4);
        graph.get(10).put(15, 24477.3);
        graph.get(14).put(15, 20000.0);

        Map<Integer, Point> dictionary = new HashMap<Integer, Point>();
        dictionary.put(2, new Point(10.927,-9.38315,5));
        dictionary.put(3, new Point(11.2225,-9.748,5.506));
        dictionary.put(4, new Point(10.632384,-9.125172,4.6));
        dictionary.put(5, new Point(11.5,-9.075,4.94));
        dictionary.put(6, new Point(10.706,-8.23719 ,4.74797));
        dictionary.put(7, new Point(11.381944, -8.566172, 4.9));
        dictionary.put(8, new Point(11.002, -8.08, 5.258));

        dictionary.put(10, new Point(10.706,-7.76007,4.62));
        dictionary.put(11, new Point(11.002, -8.3, 5.23));
        dictionary.put(12, new Point(10.974,-8.7685,5.4055));
        dictionary.put(13, new Point( 11.143,-6.7607,5.2));
        dictionary.put(14, new Point(10.35,-6.639,5.195));
        dictionary.put(15, new Point(11.143, -6.7607,  5.22));
        dictionary.put(16, new Point(11.143, -6.7607,  4.965));


        Map<Integer, Quaternion> orientations = new HashMap<Integer, Quaternion>();
        orientations.put(3, new Quaternion(0f, 0f, -0.707f, 0.707f));
        orientations.put(4, new Quaternion(0f, 0.707f, 0f, 0.707f));
        orientations.put(5, new Quaternion(0f, 0f, 0f, 1f));
        orientations.put(6, new Quaternion(0f, 0f, 0.707f, 0.707f));
        orientations.put(7, new Quaternion(0f, 0f, 0.707f, 0.707f));
        orientations.put(8, new Quaternion(0f, -0.707f, 0f, 0.707f));
        orientations.put(10, new Quaternion(0f, 0.707f, 0f, 0.707f));
        orientations.put(14, new Quaternion(0f, 0f, 1f, 0f));
        orientations.put(15, new Quaternion(0f, 0f, -0.707f, 0.707f));
        orientations.put(16, new Quaternion(0f, 0f, -0.707f, 0.707f));



        api.startMission();




//////////////////////////////////////////////////////////////////////////////////////////////



        moveToWrapper(10.927, -9.38315, 5, 0, 0.4942013, 0.2471006, 0.8334905);//main_road
        Mat img_node2 = api.getMatNavCam();
        api.saveMatImage(img_node2, "img_node2.png");
        mQrContent=scanQR(118);
        if(mQrContent==null){
            mQrContent=scanQR(119);
        }
        Log.i("Astronut:", "arrived node 2(main_road)");


        for(int round =0; round<8; round++){
            List<Long> timeRemaining = api.getTimeRemaining();
            long time_remain=0;
            String str = Integer.toString(round);
            Log.i("Astronut(round):", ""+str);

            List<Integer> target_list = api.getActiveTargets();

            Log.i("Astronut(target_real):",target_list.toString());
            Collections.replaceAll(target_list, 3, 10);
            Collections.replaceAll(target_list, 4, 14);
            Collections.replaceAll(target_list, 5, 8);
            Collections.replaceAll(target_list, 6, 5);
            Collections.replaceAll(target_list, 1, 3);
            Collections.replaceAll(target_list, 2, 4);
            Log.i("Astronut(target_tran):",target_list.toString());
            for(int i=0; i<target_list.size(); i++){
                target_node.add(target_list.get(i));
            }
            Log.i("Astronut(target_node):",target_node.toString());
            int range = target_node.size();
            for (int k = 0; k <= range; k++) {
                int Greater = 0;
                if(target_node.size()==0){
                    break;
                }
                start = current_node;
                List<Double> data = new ArrayList<>();
                for(int count=0; count<target_node.size();count++){
                    double data_list =rangefinder(dictionary.get(target_node.get(count)),dictionary.get(current_node));
                    data.add(data_list);
                }
                Log.i("Astronut(data):",data.toString());
                double lowest_data = findLowest(data);
                closestNode=target_node.get(data.indexOf(lowest_data));
                end = closestNode;
                if (start > end) {
                    int temp = start;
                    start = end;
                    end = temp;
                    Greater = 1;
                    Log.i("Astronut(point):","Bigger");

                }
                target_list.set(0, closestNode);
                Collections.replaceAll(target_list, 3, 1);
                Collections.replaceAll(target_list, 4, 2);
                Collections.replaceAll(target_list, 10, 3);
                Collections.replaceAll(target_list, 14, 4);
                Collections.replaceAll(target_list, 5, 6);
                Collections.replaceAll(target_list, 8, 5);

                Log.i("Astronut(target):","the target to shoot is: "+ target_list.get(0).toString());
                final Map<Integer, Double> distances = new HashMap<Integer, Double>();
                Map<Integer, Integer> previous = new HashMap<Integer, Integer>();
                PriorityQueue<Integer> queue = new PriorityQueue<Integer>(graph.size(), new Comparator<Integer>() {
                    public int compare(Integer a, Integer b) {
                        return distances.get(a).compareTo(distances.get(b));
                    }
                });


                for (int vertex : graph.keySet()) {
                    if (vertex == start) {
                        distances.put(vertex, 0.0);
                    } else {
                        distances.put(vertex, Double.MAX_VALUE);
                    }


                previous.put(vertex, null);
                queue.add(vertex);
            }
                while (!queue.isEmpty()) {
                    int current = queue.poll();
                    if (current == end) {
                        break;
                    }

                    for (int neighbor : graph.get(current).keySet()) {
                        double tentativeDistance = distances.get(current) + graph.get(current).get(neighbor);
                        if (tentativeDistance < distances.get(neighbor)) {
                            distances.put(neighbor, tentativeDistance);
                            previous.put(neighbor, current);
                            queue.remove(neighbor);
                            queue.add(neighbor);
                        }
                    }
                }

                List<Integer> path = new ArrayList<Integer>();
                double totalDistance = 0.0;
                int current = end;
                while (previous.get(current) != null) {
                    path.add(current);
                    int previousNode = previous.get(current);
                    double distance = graph.get(previousNode).get(current);
                    totalDistance += distance;
                    current = previousNode;
                }
                path.add(start);

                if (Greater != 1) {
                    Collections.reverse(path);
                    path.remove(0);
                } else {
                    path.remove(0);
                }


                Integer estLast=0;
                if(path.get(path.size() - 1) ==5){
                    estLast=16;
                }else{
                    estLast=15;
                }
                int first=path.get(path.size() - 1);
                int second=estLast;
                if (first > second) {
                    int temp = first;
                    first = second;
                    second = temp;



                }
                timeRemaining = api.getTimeRemaining();
                time_remain=timeRemaining.get(1);
                double time_from_last_to_goal = graph.get(first).get(second);
                Log.i("Astronut(time): ",Double.toString(time_remain));
                Log.i("Astronut(time_from_last_to_goal): ",Double.toString(time_from_last_to_goal));
                Log.i("Astronut(EST_time_to_goal): ",Double.toString(time_remain-(time_from_last_to_goal+totalDistance)));
                if(time_remain-(time_from_last_to_goal+totalDistance) <0){
                    api.notifyGoingToGoal();
                    path.clear();
                    target_list.clear();
                    target_list.add(15);
                    if(current_node==5){
                        path.add(16);
                    }else{
                        path.add(15);
                    }
                }



                Log.i("Astronut((time): ",Double.toString(totalDistance));
                for (int j = 0; j < path.size(); j++) {
                    int nums = path.get(j);
                    Point selectedPoint = dictionary.get(nums);
                    Quaternion selectedOrientation = orientations.get(nums);
                    if (j == (path.size() - 1)) {
                        Log.i("Astronut((path_bigger): ","FINAL");
                        moveToWrapper(selectedPoint.getX(), selectedPoint.getY(), selectedPoint.getZ(), selectedOrientation.getX(), selectedOrientation.getY(), selectedOrientation.getZ(), selectedOrientation.getW());
                        if(nums==3){
                            moveToWrapper(selectedPoint.getX(), selectedPoint.getY(), selectedPoint.getZ(), selectedOrientation.getX(), selectedOrientation.getY(), selectedOrientation.getZ(), selectedOrientation.getW());
                        }

                        if(target_list.get(0)==15 ){

                            api.reportMissionCompletion(mQrContent);

                            break;
                        }else {
                            Log.i("Astronut((path_bigger): ","SHOOT");

                            api.laserControl(true);
                            api.takeTargetSnapshot(target_list.get(0));
                            target_list.remove(0);
                            current_node = closestNode;
                            target_node.remove(target_node.indexOf(closestNode));
                        }


                        break;

                    }


                    moveToWrapper(selectedPoint.getX(), selectedPoint.getY(), selectedPoint.getZ(), 0, 0, 0.707, 0.707);
                    String move_point = Integer.toString(nums);
                    Log.i("Astronut(path):", "go to " + nums);



                }

            }

        }





    }

    public double rangefinder(Point point1, Point point2) {
        Point selectedPoint1 = point1;
        Point selectedPoint2 = point2;
        double distance = 0;
        distance = Math.sqrt(Math.pow((selectedPoint1.getX() - selectedPoint2.getX()), 2) + Math.pow((selectedPoint1.getY() - selectedPoint2.getY()), 2) + Math.pow((selectedPoint1.getZ() - selectedPoint2.getZ()), 2));
        return distance;
    }


    @Override
    protected void runPlan2() {
        // write your plan 2 here
    }

    @Override
    protected void runPlan3() {
        // write your plan 3 here
    }

    // You can add your method
    public void yourMethod() {


    }




    private boolean moveToWrapper(double pos_x, double pos_y, double pos_z,
                                  double qua_x, double qua_y, double qua_z,
                                  double qua_w) {

        final Point point = new Point(pos_x, pos_y, pos_z);
        final Quaternion quaternion = new Quaternion((float) qua_x, (float) qua_y,
                (float) qua_z, (float) qua_w);

        Result result = api.moveTo(point, quaternion, true);
        int loopCounter = 0;
        while (!result.hasSucceeded() && loopCounter < 3) {
            result = api.moveTo(point, quaternion, true);
            loopCounter++;
        }
        Log.d("move[count]", "" + loopCounter);
        return true;


    }



    public static double findLowest(List<Double> numbers) {
        double lowest = Double.MAX_VALUE;
        for (double num : numbers) {
            if (num < lowest) {
                lowest = num;
            }
        }
        return lowest;
    }
    private  String scanQR(Integer value){
        String data_QR = "";
        Map<String, String> ReportMessage = new HashMap<String, String>();
        ReportMessage.put("JEM", "STAY_AT_JEM");
        ReportMessage.put("COLUMBUS", "GO_TO_COLUMBUS");
        ReportMessage.put("RACK1", "CHECK_RACK_1");
        ReportMessage.put("ASTROBEE", "I_AM_HERE");
        ReportMessage.put("INTBALL", "LOOKING_FORWARD_TO_SEE_YOU");
        ReportMessage.put("BLANK", "NO_PROBLEM");


        // turn on the front flash light
        api.flashlightControlFront(0.05f);
        Mat qrImage = new Mat(api.getMatNavCam(),new Rect(600,300,400,300));
        // Mat qrImage =api.getMatNavCam();
        api.saveMatImage(qrImage, "qr.png");
        byte[] pixels_byte = new byte[643 * 482];
        // byte[] pixels_byte = new byte[1280 * 960];
        Image qr = new Image(643, 482, "Y800");
        // Image qr = new Image(1280 , 960, "Y800");
        ImageScanner reader = new ImageScanner();
        reader.setConfig(Symbol.NONE, Config.ENABLE, 0);
        reader.setConfig(Symbol.QRCODE, Config.ENABLE, 1);
        int width = qrImage.width();
        int height = qrImage.height();
        double scale_factor = 1.6075;
        int new_width = (int) (width * scale_factor);
        int new_height = (int) (height * scale_factor);
        Size newSize = new Size(new_width, new_height);
        Mat resized_img = new Mat();
        Imgproc.resize(qrImage, resized_img,newSize);
        Mat thresholded = new Mat();
        Imgproc.threshold(resized_img, thresholded, value, 255, Imgproc.THRESH_BINARY );
        api.saveMatImage(thresholded, "thresholded.png");
        thresholded.get(0, 0, pixels_byte);
        qr.setData(pixels_byte);
        int result = reader.scanImage(qr);
        Log.i("astronu(QR):t",Integer.toString(result));
        if (result != 0) {
            SymbolSet syms = reader.getResults();
            for (Symbol sym : syms) {
                data_QR = sym.getData();
            }
        }
        Log.i("QR", data_QR);

        // Set the QR code format to scan


        // turn off the front flash light
        api.flashlightControlFront(0.00f);
        return ReportMessage.get(data_QR);
    }


}