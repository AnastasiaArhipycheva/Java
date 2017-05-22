/**
 * Created by Администратор on 20.05.2017.
 */
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import org.json.*;

public class API {

    //   private static MainActivity.PostPointAsyncTask task;
//
//    static {
//        try {
//            task = new MainActivity.PostPointAsyncTask();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


 //   public static int count_update=0;
    public static boolean flag = false;
    public static float x1, x2, y1, y2;
    public static int current;
    public static ArrayList<Shape> shapes = new ArrayList<Shape>();
    public static ArrayList<Color> shapeStroke = new ArrayList<Color>();


    private static final int PORT =2551;

    private static PrintWriter out = null;
    private static BufferedReader in = null;



    public static BufferedReader getIn() {
        return in;
    }

    public static void setIn(BufferedReader in) {
        API.in = in;
    }

    public static PrintWriter getOut() {
        return out;
    }

    public static void setOut(PrintWriter out) {
        API.out = out;
    }

    private static Socket socket;

    public static void createSocket() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket("192.168.1.197", PORT);   //ip телефона 192.168.43.64 //ip дом 192.168.1.197
                    setOut(new PrintWriter(socket.getOutputStream(), true));
                    setIn(new BufferedReader(new InputStreamReader(socket.getInputStream())));


                    while (true)
                    {
                        String line = in.readLine();
                        System.out.println(line);
                        receiveJson(line);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }



    public static void sendString(String msg) {
        PrintWriter out = getOut();
        // out = new ObjectOutputStream(socket.getOutputStream());
        out.println(msg);

        setOut(out);
        //out.flush();
        System.out.println(msg);

    }

    public static void receiveString() {
        BufferedReader in = getIn();
        try {
            // out = new ObjectOutputStream(socket.getOutputStream());
            // update[count_update] = new JSONObject(in.readUTF());
            //count_update++;
            receiveJson(in.readLine());
            setIn(in);
            //out.writeUTF(msg);
            //    setOut(out);
            //    out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } /*finally {
            if(out != null)
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
           }
         */


    }

    public static void receiveJson(String json) {
        try {
            Object ob = json;
     //       JSONObject object = new JSONObject((JSONObject)ob);  (String)
            JSONObject object = new JSONObject((String)ob);
            if(object.optString("type").equals("point"))
                receivePoint(object);
            if(object.optString("type").equals("line"))
                receiveLine(object);
            if(object.optString("type").equals("circle"))
                receiveCircle(object);
            if(object.optString("type").equals("rect"))
                receiveRect(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void deleteflag() {
        flag =false;
    }

    public static void receivePoint(JSONObject json) {
        Shape aShape = null;
      //  Color col = Color.LIGHT_GRAY;
        flag = true;
        x1 = (float) json.optDouble("x");
        y1 = (float) json.optDouble("y");
        Color col = new Color(Integer.parseInt((String)json.optString("color")));
     //   current = 0;
        aShape = drawBrush((int)x1, (int)y1, 5, 5);
        shapes.add(aShape);
        shapeStroke.add(col);
        //    System.out.println("point"+ "x" +x+ "y" +y);
    //    Paint.DrawResive(0, (int)x, (int)y, 0, 0);
    }

    private static Ellipse2D.Float drawBrush(
            int x1, int y1, int brushStrokeWidth, int brushStrokeHeight) {
        return new Ellipse2D.Float(
                x1, y1, brushStrokeWidth, brushStrokeHeight);
    }

    public static void receiveLine(JSONObject json) {
        float x1 = (float) json.optDouble("x1");
        float x2 = (float) json.optDouble("x2");
        float y1 = (float) json.optDouble("y1");
        float y2 = (float) json.optDouble("y2");
    //    Paint.DrawResive(1, (int)x1, (int)y1, (int)x2, (int)y2);
    }

    public static void receiveCircle(JSONObject json) {
        float x1 = (float) json.optDouble("x1");
        float x2 = (float) json.optDouble("x2");
        float y1 = (float) json.optDouble("y1");
        float y2 = (float) json.optDouble("y2");
    //    Paint.DrawResive(2, (int)x1, (int)y1, (int)x2, (int)y2);
    }

    public static void receiveRect(JSONObject json) {
        float x1 = (float) json.optDouble("x1");
        float x2 = (float) json.optDouble("x2");
        float y1 = (float) json.optDouble("y1");
        float y2 = (float) json.optDouble("y2");
    //    Paint.DrawResive(3, (int)x1, (int)y1, (int)x2, (int)y2);
    }



    public static void sendPoint(float x, float y, Color color) {
        JSONObject object = new JSONObject();
        try {
            object.put("color", Integer.toString(color.getRGB()));
            object.put("type", "point");
            object.put("x", x);
            object.put("y", y);
            String msg = object.toString();
            sendString(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void sendLine(float x1, float y1, float x2, float y2) {
        JSONObject object = new JSONObject();
        try {
            object.put("type", "line");
            object.put("x1", x1);
            object.put("x2", x2);
            object.put("y1", y1);
            object.put("y2", y2);
            String msg = object.toString();
            sendString(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void sendCircle(float x1, float y1, float x2, float y2) {
        JSONObject object = new JSONObject();
        try {
            object.put("type", "circle");
            object.put("x1", x1);
            object.put("x2", x2);
            object.put("y1", y1);
            object.put("y2", y2);
            String msg = object.toString();
            sendString(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static void sendRect(float x1, float y1, float x2, float y2) {
        JSONObject object = new JSONObject();
        try {
            object.put("type", "rect");
            object.put("x1", x1);
            object.put("x2", x2);
            object.put("y1", y1);
            object.put("y2", y2);
            String msg = object.toString();
            sendString(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
