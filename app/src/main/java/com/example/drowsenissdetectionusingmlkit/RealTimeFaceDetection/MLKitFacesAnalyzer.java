package com.example.drowsenissdetectionusingmlkit.RealTimeFaceDetection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.util.Log;
import android.view.TextureView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.camera.core.CameraX;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.google.firebase.ml.vision.common.FirebaseVisionPoint;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceContour;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;

import java.util.List;

public final  class MLKitFacesAnalyzer implements ImageAnalysis.Analyzer {
    private static final String TAG = "MLKitFacesAnalyzer";
    private FirebaseVisionFaceDetector faceDetector;
    private TextureView tv;
    private  ImageView iv;
    private  Bitmap bitmap;
    private  Canvas canvas;
    private Paint dotPaint;
    private  Paint linePaint;
    private float widthScaleFactor = 1.0f;
    private float heightScaleFactor = 1.0f;
    private FirebaseVisionImage fbImage;
    private CameraX.LensFacing lens;
    private  Context context;
    private static int fps=0;
    static float F;
    static float m;
   static float d1,d2,d3,d4,d5,d6=0;
    static float x1,y1,x2,y2,x11,y11,x22,y22,x33,y33,x44,y44,x55,y55,x66,y66,x77,y77,x88,y88,x99,y99,x1010,y1010;
    static float A,B;

    MLKitFacesAnalyzer(TextureView tv, ImageView iv, CameraX.LensFacing lens, Context context) {
        this.tv = tv;
        this.iv = iv;
        this.lens = lens;
        this.context=context;
    }

    @Override
    public void analyze(ImageProxy image, int rotationDegrees) {
        if (image == null || image.getImage() == null) {
            return;
        }
        int rotation = degreesToFirebaseRotation(rotationDegrees);
        fbImage = FirebaseVisionImage.fromMediaImage(image.getImage(), rotation);


        initDrawingUtils();

        initDetector();
        detectFaces();
    }

    private void initDrawingUtils() {
        bitmap = Bitmap.createBitmap(tv.getWidth(), tv.getHeight(), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        dotPaint = new Paint();
        dotPaint.setTextSize(10000);
        dotPaint.setColor(Color.RED);
        dotPaint.setStyle(Paint.Style.FILL);
        dotPaint.setStrokeWidth(2f);
        dotPaint.setAntiAlias(true);
        linePaint = new Paint();
        linePaint.setColor(Color.GREEN);
        linePaint.setTextSize(200);

        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(2f);
        widthScaleFactor = canvas.getWidth() / (fbImage.getBitmap().getWidth() * 1.0f);
        heightScaleFactor = canvas.getHeight() / (fbImage.getBitmap().getHeight() * 1.0f);
    }

    private void initDetector() {
        FirebaseVisionFaceDetectorOptions detectorOptions = new FirebaseVisionFaceDetectorOptions
                .Builder()
                .setContourMode(FirebaseVisionFaceDetectorOptions.ALL_CONTOURS)
//                .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                .build();
        faceDetector = FirebaseVision
                .getInstance()
                .getVisionFaceDetector(detectorOptions);

    }

    private void detectFaces() {
        faceDetector.detectInImage(fbImage).addOnSuccessListener(firebaseVisionFaces -> {
                    if (!firebaseVisionFaces.isEmpty()) {
                        processFaces(firebaseVisionFaces);


                    } else {
                        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.MULTIPLY);

                    }

                }).addOnFailureListener(e -> Log.i(TAG, e.toString()));

    }


    private  void processFaces(List<FirebaseVisionFace> faces) {
        for (FirebaseVisionFace face : faces) {
            drawContours(face.getContour(FirebaseVisionFaceContour.LEFT_EYE).getPoints());
            drawContours(face.getContour(FirebaseVisionFaceContour.RIGHT_EYE).getPoints());
            String s=d1+"  "+d2+"  "+fps+" ";
            canvas.drawText(s,0,0,linePaint);

            x1=face.getContour(FirebaseVisionFaceContour.RIGHT_EYE).getPoints().get(1).getX();
            y1=face.getContour(FirebaseVisionFaceContour.RIGHT_EYE).getPoints().get(1).getY();
            x2=face.getContour(FirebaseVisionFaceContour.RIGHT_EYE).getPoints().get(15).getX();
            y2=face.getContour(FirebaseVisionFaceContour.RIGHT_EYE).getPoints().get(15).getY();


            x11=face.getContour(FirebaseVisionFaceContour.RIGHT_EYE).getPoints().get(7).getX();
            y11=face.getContour(FirebaseVisionFaceContour.RIGHT_EYE).getPoints().get(7).getY();
            x22=face.getContour(FirebaseVisionFaceContour.RIGHT_EYE).getPoints().get(9).getX();
            y22=face.getContour(FirebaseVisionFaceContour.RIGHT_EYE).getPoints().get(9).getY();


            x33=face.getContour(FirebaseVisionFaceContour.RIGHT_EYE).getPoints().get(0).getX();
            y33=face.getContour(FirebaseVisionFaceContour.RIGHT_EYE).getPoints().get(0).getY();
            x44=face.getContour(FirebaseVisionFaceContour.RIGHT_EYE).getPoints().get(8).getX();
            y44=face.getContour(FirebaseVisionFaceContour.RIGHT_EYE).getPoints().get(8).getY();


            x55=face.getContour(FirebaseVisionFaceContour.LEFT_EYE).getPoints().get(1).getX();
            y55=face.getContour(FirebaseVisionFaceContour.LEFT_EYE).getPoints().get(1).getY();
            x66=face.getContour(FirebaseVisionFaceContour.LEFT_EYE).getPoints().get(15).getX();
            y66=face.getContour(FirebaseVisionFaceContour.LEFT_EYE).getPoints().get(15).getY();

            x77=face.getContour(FirebaseVisionFaceContour.LEFT_EYE).getPoints().get(7).getX();
            y77=face.getContour(FirebaseVisionFaceContour.LEFT_EYE).getPoints().get(7).getY();
            x88=face.getContour(FirebaseVisionFaceContour.LEFT_EYE).getPoints().get(9).getX();
            y88=face.getContour(FirebaseVisionFaceContour.LEFT_EYE).getPoints().get(9).getY();


            x99=face.getContour(FirebaseVisionFaceContour.LEFT_EYE).getPoints().get(0).getX();
            y99=face.getContour(FirebaseVisionFaceContour.LEFT_EYE).getPoints().get(0).getY();
            x1010=face.getContour(FirebaseVisionFaceContour.LEFT_EYE).getPoints().get(8).getX();
            y1010=face.getContour(FirebaseVisionFaceContour.LEFT_EYE).getPoints().get(8).getY();

            float EAR1=(Math.abs(x2-x1)+Math.abs(x22-x11))/(2*Math.abs(x44-x33));
            float EAR2=(Math.abs(x66-x55)+Math.abs(x88-x77))/(2*Math.abs(x1010-x99));
            float EAR=(EAR1+EAR2)/2;




             d1= (float) Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2));
             d2=(float) Math.sqrt(Math.pow(x22-x11,2)+Math.pow(y22-y11,2));
             d3=(float) Math.sqrt(Math.pow(x44-x33,2)+Math.pow(y44-y33,2));
            d4=(float) Math.sqrt(Math.pow(x66-x55,2)+Math.pow(y66-y55,2));
            d5=(float) Math.sqrt(Math.pow(x88-x77,2)+Math.pow(y88-y77,2));
            d6=(float) Math.sqrt(Math.pow(x1010-x99,2)+Math.pow(y1010-y99,2));
             A= (float) ((d1+d2)/(d3*2.0));
             B= (float) ((d4+d5)/(d6*2.0));
             F=(A+B);
             m=F;
             System.out.println(F+"   "+EAR);
            if (F<m)
                m=F;
            if(F< 0.16)
            {
                fps+=1;
                if(fps>60)
                {
                    Toast.makeText(context,fps+" "+F,Toast.LENGTH_LONG).show();
                    ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                    toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP,150);
                }


            }
            else fps=0;






//          drawContours(face.getContour(FirebaseVisionFaceContour.FACE).getPoints());
//          drawContours(face.getContour(FirebaseVisionFaceContour.LEFT_EYEBROW_BOTTOM).getPoints());
//          drawContours(face.getContour(FirebaseVisionFaceContour.RIGHT_EYEBROW_BOTTOM).getPoints());
//          drawContours(face.getContour(FirebaseVisionFaceContour.LEFT_EYEBROW_TOP).getPoints());
//          drawContours(face.getContour(FirebaseVisionFaceContour.RIGHT_EYEBROW_TOP).getPoints());
//          drawContours(face.getContour(FirebaseVisionFaceContour.LOWER_LIP_BOTTOM).getPoints());
//          drawContours(face.getContour(FirebaseVisionFaceContour.LOWER_LIP_TOP).getPoints());
//          drawContours(face.getContour(FirebaseVisionFaceContour.UPPER_LIP_BOTTOM).getPoints());
//          drawContours(face.getContour(FirebaseVisionFaceContour.UPPER_LIP_TOP).getPoints());
//          drawContours(face.getContour(FirebaseVisionFaceContour.NOSE_BRIDGE).getPoints());
//          drawContours(face.getContour(FirebaseVisionFaceContour.NOSE_BOTTOM).getPoints());
        }
        iv.setImageBitmap(bitmap);
    }

    private void drawContours(List<FirebaseVisionPoint> points) {
        int counter = 0;
        for (FirebaseVisionPoint point : points) {
            if (counter != points.size() - 1) {
                canvas.drawLine(translateX(point.getX()),
                        translateY(point.getY()),
                        translateX(points.get(counter + 1).getX()),
                        translateY(points.get(counter + 1).getY()),
                        linePaint);
            } else {
                canvas.drawLine(translateX(point.getX()),
                        translateY(point.getY()),
                        translateX(points.get(0).getX()),
                        translateY(points.get(0).getY()),
                        linePaint);
            }
            counter++;
            canvas.drawCircle(translateX(point.getX()), translateY(point.getY()), 6, dotPaint);
        }
    }

    private float translateY(float y) {
        return y * heightScaleFactor;
    }

    private float translateX(float x) {
        float scaledX = x * widthScaleFactor;
        if (lens == CameraX.LensFacing.FRONT) {
            return canvas.getWidth() - scaledX;
        } else {
            return scaledX;
        }
    }


    private int degreesToFirebaseRotation(int degrees) {
        switch (degrees) {
            case 0:
                return FirebaseVisionImageMetadata.ROTATION_0;
            case 90:
                return FirebaseVisionImageMetadata.ROTATION_90;
            case 180:
                return FirebaseVisionImageMetadata.ROTATION_180;
            case 270:
                return FirebaseVisionImageMetadata.ROTATION_270;
            default:
                throw new IllegalArgumentException("Rotation must be 0, 90, 180, or 270.");
        }
    }
//    private void checkFaceExpression(FirebaseVisionFace face) {
//        if (face.getSmilingProbability() > 0.5) {
//            Log.d(TAG, "**** Smiling ***");
//            listener.onSuccess(FACE_STATUS.SMILING);
//        }
//        if (face.getLeftEyeOpenProbability() < 0.2 &amp;&amp;
//        face.getLeftEyeOpenProbability() != -1 &amp;&amp;
//        face.getRightEyeOpenProbability() > 0.5) {
//            Log.d(TAG, "Right Open..");
//            listener.onSuccess(FACE_STATUS.RIGHT_EYE_OPEN_LEFT_CLOSE)
//        }
//        if (face.getRightEyeOpenProbability() < 0.2 &amp;&amp;
//        face.getRightEyeOpenProbability() != -1 &amp;&amp;
//        face.getLeftEyeOpenProbability() > 0.5) {
//            Log.d(TAG, "Left Open..");
//            listener.onSuccess(FACE_STATUS.LEFT_EYE_OPEN_RIGHT_CLOSE);
//        }
//        listener.onSuccess(FACE_STATUS.LEFT_OPEN_RIGHT_OPEN);
//    }

}
