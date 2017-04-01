package com.example.marc4492.neuralmathtest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Author: Created by Mathieu on 2017-03-08.
 * A layout containing a DrawingView, some options button and a output area
 * This layout can be set for right-handed or left-handed
 */

public class DrawingPage extends LinearLayout {

    private DrawingView drawView;
    private LinearLayout layoutBtn;
    private EditText txtEquation;

    /**
     * Creation des deux parties de la page
     *
     * @param context       Context de l'app
     * @param attrs         ?
     */
    public DrawingPage(Context context, AttributeSet attrs) {
        super(context, attrs);

        drawView = new DrawingView(context);
        drawView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f));

        txtEquation = new EditText(context);
        txtEquation.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0.5f));

        Button btnRetry = new Button(context);
        btnRetry.setText(R.string.clear);
        //setting the button click listener
        btnRetry.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                drawView.clear();
            }
        });

        //Creation of the button layout
        layoutBtn = new LinearLayout(context);
        layoutBtn.setOrientation(LinearLayout.VERTICAL);
        layoutBtn.addView(btnRetry);

        setOrientation(LinearLayout.HORIZONTAL);
        addViewsInThePage();
}

    /**
     * Getter de l'edittext pour l'eq
     * @return      EditText
     */
    public EditText getTextEquation()
    {
        return txtEquation;
    }

    /**
     * Getter du drawing view
     * @return      Drawing View
     */
    public DrawingView getDrawView()
    {
        return drawView;
    }

    /**
     * This add view for right-handed by default when the DrawingPage is created
     */
    private void addViewsInThePage(){
        addView(txtEquation);
        addView(drawView);
        addView(layoutBtn);
    }

    /**
     * Set the layout for left-handed User
     */
    public void setLayoutForLeftHended(){
        removeAllViewsInLayout();
        addView(layoutBtn);
        addView(drawView);
        addView(txtEquation);
    }

    /**
     * Set the layout for right-handed User
     */
    public void setLayoutForRigntHanded(){
        removeAllViewsInLayout();
        addView(txtEquation);
        addView(drawView);
        addView(layoutBtn);
    }
}