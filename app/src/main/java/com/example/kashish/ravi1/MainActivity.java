package com.example.kashish.ravi1;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

//import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    boolean isTab = false;

    String TAG = "MainActivity";
    private Spinner s;
    public static Vector<Vector<int[]>> corners;
    public static List<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<String>();
//        list.add("square");
//        list.add("rectangle");
//        list.add("equilateral triangle");
//        list.add("isoceles triangle");
//        list.add("scalene triangle");
//        list.add("pentagon");
        list.add("Complex1");
        list.add("Complex2");
        list.add("Circle");
        list.add("Rectangle");
//        list.add("Semicircle");
        list.add("Square");
        list.add("Star");
        list.add("Triangle");
//        list.add("Ellipse");

//        final ArrayList<String> files = new ArrayList<>();

        final ArrayList<String> filesMobile = new ArrayList<>();
        filesMobile.add("Complex1.svg");
        filesMobile.add("Complex2.svg");
        filesMobile.add("Circle.svg");
        filesMobile.add("Rectangle.svg");
//        files.add("Semicircle.svg");
        filesMobile.add("Square.svg");
        filesMobile.add("Star.svg");
        filesMobile.add("Triangle.svg");
        filesMobile.add("Ellipse.svg");

        ArrayList<String> filesTab = new ArrayList<>();
        filesTab.add("Complex1.svg");
        filesTab.add("Complex2.svg");
        filesTab.add("Circle.svg");
        filesTab.add("Rectangle.svg");
//        files.add("Semicircle.svg");
        filesTab.add("Square.svg");
        filesTab.add("Star.svg");
        filesTab.add("Triangle.svg");
//        filesTab.add("Ellipse.svg");


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        if(width == 1920)isTab = true;

        Toast.makeText(this, "Height: "+height+" and width: "+width, Toast.LENGTH_SHORT).show();
        Log.e(TAG,"Height: "+height+" and width: "+width);

        scaleSVG ss = new scaleSVG();
        ss.scale(this, "Complex1.svg",width,height);
        ss.scale(this, "Complex2.svg",width,height);
        ss.scale(this, "Circle.svg",width,height);
        ss.scale(this, "Rectangle.svg",width,height);
        ss.scale(this, "Square.svg",width,height);
        ss.scale(this, "Star.svg",width,height);
        ss.scale(this, "Triangle.svg",width,height);
        ss.scale(this, "Ellipse.svg",width,height);

        s = (Spinner) findViewById(R.id.spinner);
        s.setPrompt("Choose a diagram");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(dataAdapter);

        Button b = (Button) findViewById(R.id.button_showImage);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n = new Intent(MainActivity.this, showImage.class);
                String showFile = filesMobile.get(s.getSelectedItemPosition());
                n.putExtra("file",showFile);
                if(isTab) {
                    showFile = "ic_" + showFile.toLowerCase().substring(0, showFile.length() - 4) + "_1_tab";
                }
                else{
                    showFile = "ic_"+showFile.toLowerCase().substring(0,showFile.length()-4)+"_1";
                }
                n.putExtra("showImage",showFile);
                startActivity(n);
            }
        });

//        corners = new Vector<>();
//        BufferedReader reader = null;
//        try {
//            reader = new BufferedReader(new InputStreamReader(getAssets().open("Cordinates.txt")));
//            String         line = null;
//            StringBuilder  stringBuilder = new StringBuilder();
//            while((line = reader.readLine()) != null) {
//                Log.d("Line ",line);
//                String[] a = line.split(" ");
//                Vector<int[]> c = new Vector<>();
//                for(int i=2;i<a.length;i++){
//                    int[] cor = new int[2];
//                    String[] stringCor = a[i].split(",");
//                    cor[0] = Integer.parseInt(stringCor[0]);
//                    cor[1] = Integer.parseInt(stringCor[1]);
//                    c.add(cor);
//                }
//                corners.add(c);
//            }
//            reader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//// Vibrate for 500 milliseconds
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    vib.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
//                } else {
//                    //deprecated in API 26
//                    vib.vibrate(500);
//                }
//            }
//        });


    }
}
