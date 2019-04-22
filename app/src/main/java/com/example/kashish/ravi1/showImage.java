package com.example.kashish.ravi1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.larvalabs.svgandroid.SVG;
//import com.larvalabs.svgandroid.SVGParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

import static java.lang.String.valueOf;

//import com.caverock.androidsvg.SVG;

public class showImage extends AppCompatActivity {


    Uri uri;
    private static final String TAG = "showImage";
    double scale=1;
    ImageView img;
    int[] viewCoords = new int[2];
    Thread vibrate;
    int imageX,imageY;
    String diagFile;
    int threshold = 50;

    String[] tags = {"path","line","ellipse","circle","rectangle","square" };

    Vector<int[]> corners;
    Vector<int[]> circles;
    Vector<int[]> ellipses;

    Vector<String> names;

    double lengthSquared(int x1, int y1, int x2, int y2){
        return Math.pow(x1-x2,2) + Math.pow(y1-y2,2);
    }

    double dot(int x1, int y1, int x2, int y2){
        return x1*x2 + y1*y2;
    }

    double getDistFromLineSeg(int x1, int y1, int x2, int y2, int x, int y){
        double l2 = lengthSquared(x1,y1,x2,y2);
        if(l2==0)return Math.sqrt(lengthSquared(x1,y1,x,y));
        else{
            double t = Math.max(0,Math.min(1,dot(x-x1,y-y1,x2-x1,y2-y1)/l2));
            double projectionX = x1 + t*(x2-x1);
            double projectionY = y1 + t*(y2-y1);
            return Math.sqrt(lengthSquared(x,y,(int)projectionX,(int)projectionY));
        }
    }

    void readSVG(String fileName){

//
                    try {
                        File file = new File(fileName);
                        corners = new Vector<>();
                        circles = new Vector<>();
                        ellipses = new Vector<>();
                        String line;
                        InputStream is = this.openFileInput(fileName);
                        InputStreamReader isr = new InputStreamReader(is);
                        BufferedReader reader = new BufferedReader(isr);
                        while ((line = reader.readLine()) != null) {
//                sb.append(line);
//            }
                            Log.d("Line ",line);
                            String[] a = line.split("<");
                            for(int i=0;i<a.length;i++){
//                    String[] stringCor = a[i].split(",");
//                    cor[0] = Integer.parseInt(stringCor[0]);
//                    cor[1] = Integer.parseInt(stringCor[1]);
//                    c.add(cor);
                                int tagId = -1;
                                for(int j=0;j<tags.length;j++){
                                    if(a[i].contains(tags[j])){
                                        tagId = j;
                                        Log.e(TAG,"found tag: "+tagId);
                                    }
                                }

                                if(tagId !=-1){
                                    switch (tagId){
                                        case 1: {

                                            int[] cor1 = new int[4];
//                                int[] cor2 = new int[2];

                                            a[i] = a[i].replaceAll("\\s+", "");

                                            int x1pos = a[i].indexOf("x1");
                                            int x2pos = a[i].indexOf("x2");
                                            int y1pos = a[i].indexOf("y1");
                                            int y2pos = a[i].indexOf("y2");

//                                String [] b = a[i].split("=");
                                            int x1 = Integer.parseInt(a[i].substring(x1pos + 4, y1pos - 1));
                                            int y1 = Integer.parseInt(a[i].substring(y1pos + 4, x2pos - 1));
                                            int x2 = Integer.parseInt(a[i].substring(x2pos + 4, y2pos - 1));
                                            char ch = a[i].charAt(y2pos + 3);
                                            int index = a[i].indexOf(ch, y2pos + 4);
                                            int y2 = Integer.parseInt(a[i].substring(y2pos + 4, index));


                                            cor1[0] = x1;
                                            cor1[1] = y1;
                                            cor1[2] = x2;
                                            cor1[3] = y2;

                                            Log.e(TAG,"ADding corner: "+cor1[0]+" "+cor1[1]+" "+cor1[2]+" "+cor1[3]);
//                                cor2[0] = x2;
//                                cor2[1] = y2;

                                            corners.add(cor1);
//                                c.add(cor2);
//                                int y2 = Integer.parseInt(a[i].substring(x1pos+4,y1pos));
                                            break;
                                        }
                                        case 3: {
                                            Log.e(TAG,"found circle");
                                            int[] cor = new int[3];
                                            a[i] = a[i].replaceAll("\\s+", "");

                                            int cxpos = a[i].indexOf("cx=");
                                            int cypos = a[i].indexOf("cy=");
                                            int rpos = a[i].indexOf("r=");



//                                Toast.makeText(this, "rPos: "+String.valueOf(rpos),Toast.LENGTH_SHORT).show();

                                            int cx = Integer.parseInt(a[i].substring(cxpos + 4, cypos - 1));
                                            int cy = Integer.parseInt(a[i].substring(cypos + 4, rpos - 1));
                                            char ch = a[i].charAt(rpos + 2);
                                            int index = a[i].indexOf(ch, rpos + 3);

                                            Log.d("rpos and ch: ",String.valueOf(rpos)+" "+ ch+", "+index+", "+a[i].substring(rpos+4,index));


                                            int r = Integer.parseInt(a[i].substring(rpos + 3, index));

                                            Log.e(TAG,"cx: "+cx+", cy: "+cy+" and r: "+r);

                                            cor[0] = cx;
                                            cor[1] = cy;
                                            cor[2] = r;
//                                cor[3] = y2;
//                                cor2[0] = x2;
//                                cor2[1] = y2;

                                            circles.add(cor);
//                                c.add(cor2);
//                                int y2 = Integer.parseInt(a[i].substring(x1pos+4,y1pos));
                                            break;
//                                int y2pos = a[i].indexOf("y2");
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                    // Handle any errors
//                }
//            });


//            AssetManager am = this.getAssets();
////            File file = new File(fileName);
//            InputStream fis = am.open(fileName);
//            reader = new BufferedReader(new InputStreamReader(getAssets().open(fileName)));
//            String         line = null;
//            StringBuilder  stringBuilder = new StringBuilder();
//            Log.e(TAG,"updated file: "+fileName);
////            reader = new BufferedReader(new FileReader(file));
//            InputStreamReader isr = new InputStreamReader(fis);
//            Bitmap bmp = null;
//            bmp = BitmapFactory.decodeStream(fis);
//            img.setImageBitmap(bmp);
//            BufferedReader bufferedReader = new BufferedReader(isr);
//            StringBuilder sb = new StringBuilder();
//            String line;

//            reader.close();



//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }



    boolean toVibrate(int x, int y){
//        x -= img.getWidth()/2;
//        y -= img.getHeight()/2;

//        double d1,d2,d3,d4 = getDistFromLineSeg(x1,y1,x2,y2,x,y),getDistFromLineSeg(),getDistFromLineSeg(),getDistFromLineSeg();
        double minDist = Double.MAX_VALUE;
//        Vector<int[]> corners = MainActivity.corners.get(diagNum);
        for(int i=0;i<corners.size();i++){
            double dist = getDistFromLineSeg(corners.get(i)[0],corners.get(i)[1],corners.get(i)[2],corners.get(i)[3],x,y);
            if(dist<minDist){
                minDist = dist;
                if(minDist < threshold){
                    return true;
                }
            }
        }

        for(int i=0;i<circles.size();i++){
            int[] c = circles.get(i);
            int cx = c[0];
            int cy = c[1];
            int r = c[2];

            if(Math.abs(Math.sqrt(lengthSquared(x,y,cx,cy)) - r) <= threshold)return true;
        }

        return false;
//        else return false;
    }

    void vibrate(){
        Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vib.vibrate(VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            vib.vibrate(20);
        }
    }

//    void showImageUsingURL(StorageReference storageRef,String fileName){
//        storageRef.child(fileName).getDownloadUrl().addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        }).addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Toast.makeText(showImage.this, "uri: "+uri.toString(), Toast.LENGTH_SHORT).show();
//                Log.e(TAG,"uri: "+uri.toString());
//                try {
//                    URL url = new URL(uri.toString());
//                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                    InputStream inputStream = urlConnection.getInputStream();
//                    SVG svg = SVGParser.getSVGFromInputStream(inputStream);
//                    Drawable drawable = svg.createPictureDrawable();
////                    return drawable;
//                    img.setImageDrawable(drawable);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
////        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
////        InputStream inputStream = urlConnection.getInputStream();
////        SVG svg = SVGParser. getSVGFromInputStream(inputStream);
////        Drawable drawable = svg.createPictureDrawable();
////        return drawable;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);


//        FirebaseStorage storage = FirebaseStorage.getInstance();
//
//        storageRef = storage.getReference();

//        names=  new Vector<>();
//        names.add("square");
//        names.add("rectangle");
//        names.add("equi_triangle");
//        names.add("iso_triangle");
//        names.add("scalene_triangle");
//        names.add("pentagon");

        Intent n = getIntent();

        diagFile = n.getStringExtra("file");

        Toast.makeText(this, "file is: "+diagFile,Toast.LENGTH_SHORT).show();



//        String x1 = valueOf(corners.get(0)[0]);
//        String y1 = valueOf(corners.get(0)[1]);
//        String x2 = valueOf(corners.get(0)[2]);
//        String y2 = valueOf(corners.get(0)[3]);

//        Toast.makeText(this, "corner[0] is: "+x1+" "+y1+" "+x2+" "+y2,Toast.LENGTH_SHORT).show();
        img = (ImageView) findViewById(R.id.imageView);
        img.getLocationOnScreen(viewCoords);
        String name = n.getStringExtra("showImage");
        Log.d("Name",name);
//        int id = getResources().getIdentifier(name, "drawable", getPackageName());
//        int resID = getResId("icon", R.drawable.class);
        int id = getId(name,R.drawable.class);
        Log.d("ID", valueOf(id));
        img.setImageResource(id);


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Log.e("Width", "" + width);
        Log.e("height", "" + height);


//        int minwh = Integer.min(width,height);
        img.setLayoutParams(new ConstraintLayout.LayoutParams(width,height));
//        Log.d("Position:", "x: "+String.valueOf(viewCoords[0])+" and y: "+String.valueOf(viewCoords[1]));
//        Log.d("image coord:", "x: "+String.valueOf(img.getWidth())+" and y: "+String.valueOf(img.getHeight()));
//        storeFile();
//        showImageUsingURL(storageRef,diagFile);

//        try {
//            File file = this.openFileInput(diagFile);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        InputStream inputStream = null;
//        try {
//            inputStream = this.openFileInput(diagFile);
//            InputStreamReader isr = new InputStreamReader(inputStream);
//            BufferedReader br = new BufferedReader(isr);
//            String line;
//            while ((line = br.readLine()) != null) {
//                Log.e(TAG,line);
//            }
//
//            inputStream = this.openFileInput(diagFile);
//
//
//            SVG svg = SVGParser.getSVGFromInputStream(inputStream);
//            Drawable drawable = svg.createPictureDrawable();
////                    return drawable;
//            img.setImageDrawable(drawable);
//            Toast.makeText(this, "img drawable set", Toast.LENGTH_SHORT).show();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            Log.e(TAG,"diagFile not found");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        storageRef.child(diagFile).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
//              @Override
//              public void onComplete(@NonNull Task<Uri> task) {
//                  if (task.isSuccessful()) {
//                      Toast.makeText(showImage.this, "uri: " + task.getResult().toString(), Toast.LENGTH_SHORT).show();
//                      Log.e(TAG, "uri: " + task.getResult().toString());
//                      uri = task.getResult();
//
//                      HttpImageRequestTask t = new HttpImageRequestTask();
//                      t.execute();
//                  }
//              }
//          });

        img.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int x,y;
                x = (int) event.getX();
                y = (int) event.getY();
                Log.d("Position:", "x: "+ valueOf(x)+" and y: "+ valueOf(y));

                imageX = x; //- viewCoords[0]; // viewCoods[0] is the X coordinate
                imageY = y; //- viewCoords[1];

                if(event.getAction()==MotionEvent.ACTION_MOVE || event.getAction()==MotionEvent.ACTION_DOWN){
                    Log.d("Touched:","x: "+ valueOf(imageX)+" and y: "+ valueOf(imageY));

                    if(toVibrate((int)(imageX/scale),(int)(imageY/scale))){
//                        Toast.makeText(showImage.this, "vibrated at: "+String.valueOf(imageX)+","+String.valueOf(imageY),Toast.LENGTH_SHORT).show();
                        vibrate();
                    }
                }
                return true;
            }
        });
        readSVG(diagFile);

//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int)(img.getWidth()*scale), (int)(img.getHeight()*scale));
//        img.setLayoutParams(layoutParams);
//        img.setLayoutParams().height = ;
    }

    public static int getId(String resourceName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resourceName);
            return idField.getInt(idField);
        } catch (Exception e) {
            throw new RuntimeException("No resource ID found for: "
                    + resourceName + " / " + c, e);
        }
    }

    @Override
    public void onWindowFocusChanged (boolean hasFocus) {

    }

//    private class HttpImageRequestTask extends AsyncTask<Void, Void, Drawable> {
//        @Override
//        protected Drawable doInBackground(Void... params) {
//            try {
//
//
//                    URL url = new URL(uri.toString());
//                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                    InputStream inputStream = urlConnection.getInputStream();
//                    SVG svg = SVGParser.getSVGFromInputStream(inputStream);
//                    Drawable drawable = svg.createPictureDrawable();
//                    return drawable;
//
////                        addOnFailureListener(new OnFailureListener() {
////                    @Override
////                    public void onFailure(@NonNull Exception e) {
////
////                    }
////                }).addOnSuccessListener(new OnSuccessListener<Uri>() {
////                    @Override
////                    public void onSuccess(Uri uri) {
////
////                    }
////                });
//
//
////                final URL url = new URL("http://upload.wikimedia.org/wikipedia/commons/e/e8/Svg_example3.svg");
////                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
////                InputStream inputStream = urlConnection.getInputStream();
////                SVG svg = SVGParser. getSVGFromInputStream(inputStream);
////                Drawable drawable = svg.createPictureDrawable();
////                return drawable;
//            } catch (Exception e) {
//                Log.e("MainActivity", e.getMessage(), e);
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Drawable drawable) {
//            // Update the view
//            updateImageView(drawable);
//        }
//    }

//    @SuppressLint("NewApi")
//    private void updateImageView(Drawable drawable){
//        if(drawable != null){
//
//            // Try using your library and adding this layer type before switching your SVG parsing
//            img.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//            img.setImageDrawable(drawable);
//        }
//    }
}
