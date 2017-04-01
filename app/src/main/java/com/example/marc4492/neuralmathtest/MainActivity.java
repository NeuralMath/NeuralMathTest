package com.example.marc4492.neuralmathtest;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int INPUT = 784;
    private static final int HIDDEN = 200;
    private static final int OUTPUT = 10;
    private static final double TRAININGRATE = 0.005;

    private static final String fileWeightsItoH = Environment.getExternalStorageDirectory().getPath() + "/NeuralMath/weightsItoH.txt";
    private static final String fileWeightsHtoO = Environment.getExternalStorageDirectory().getPath() + "/NeuralMath/weightsHtoO.txt";

    private String[] charList =
            {
                    "0",
                    "1",
                    "2",
                    "3",
                    "4",
                    "5",
                    "6",
                    "7",
                    "8",
                    "9"
            };

    private DrawingPage drawPage;
    private ImageDecoder imageDecoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawPage = (DrawingPage) findViewById(R.id.drawPage);
        drawPage.getDrawView().setListener(new DrawingView.DrawnListener() {
            @Override
            public void drawn(Bitmap b) {
                setBitmap(b);
            }
        });

        try {
            imageDecoder = new ImageDecoder(this, INPUT, HIDDEN, OUTPUT, TRAININGRATE, fileWeightsItoH, fileWeightsHtoO, charList);
        }
        catch (Exception ex)
        {
            Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Set le bitmap pour l'AI
     *
     * @param btm       L'image
     */
    public void setBitmap(Bitmap btm)
    {
        try {
            drawPage.getTextEquation().append(imageDecoder.findSting(btm));
        }
        catch(Exception ex)
        {
            Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
