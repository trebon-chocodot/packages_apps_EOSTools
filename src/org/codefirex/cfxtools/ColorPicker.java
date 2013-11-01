
package org.codefirex.cfxtools;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import org.codefirex.cfxtools.ambilwarna.AmbilWarnaKotak;

public class ColorPicker extends Activity {
    public interface OnAmbilWarnaListener {
        void onCancel(ColorPicker dialog);

        void onOk(ColorPicker dialog, int color);
    }

    AlertDialog dialog;
    View viewHue;
    AmbilWarnaKotak viewSatVal;
    ImageView viewCursor;
    View viewOldColor;
    View viewNewColor;
    ImageView viewTarget;
    ViewGroup viewContainer;
    Button mCancel;
    Button mOk;
    float[] currentColorHsv = new float[3];

    /**
     * create an AmbilWarnaDialog. call this only from OnCreateDialog() or from
     * a background thread.
     * 
     * @param context current context
     * @param color current color
     * @param listener an OnAmbilWarnaListener, allowing you to get back error
     *            or
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int color = getIntent().getIntExtra("oldColor",
                getResources().getInteger(android.R.color.holo_blue_light));
        Color.colorToHSV(color, currentColorHsv);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ambilwarna_activity_dialog);

        final View view = findViewById(R.id.ambilwarna_dialogView);

        viewHue = findViewById(R.id.ambilwarna_viewHue);
        viewSatVal = (AmbilWarnaKotak) findViewById(R.id.ambilwarna_viewSatBri);
        viewCursor = (ImageView) findViewById(R.id.ambilwarna_cursor);
        viewOldColor = findViewById(R.id.ambilwarna_warnaLama);
        viewNewColor = findViewById(R.id.ambilwarna_warnaBaru);
        viewTarget = (ImageView) findViewById(R.id.ambilwarna_target);
        viewContainer = (ViewGroup) findViewById(R.id.ambilwarna_viewContainer);
        mCancel = (Button) findViewById(R.id.button_cancel);
        mOk = (Button) findViewById(R.id.button_ok);

        viewSatVal.setHue(getHue());
        viewOldColor.setBackgroundColor(color);
        viewNewColor.setBackgroundColor(color);

        viewHue.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE
                        || event.getAction() == MotionEvent.ACTION_DOWN
                        || event.getAction() == MotionEvent.ACTION_UP) {

                    float y = event.getY();
                    if (y < 0.f)
                        y = 0.f;
                    if (y > viewHue.getMeasuredHeight())
                        y = viewHue.getMeasuredHeight() - 0.001f; // to avoid
                                                                  // looping
                                                                  // from end to
                                                                  // start.
                    float hue = 360.f - 360.f / viewHue.getMeasuredHeight() * y;
                    if (hue == 360.f)
                        hue = 0.f;
                    setHue(hue);

                    // update view
                    viewSatVal.setHue(getHue());
                    moveCursor();
                    viewNewColor.setBackgroundColor(getColor());

                    return true;
                }
                return false;
            }
        });
        viewSatVal.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE
                        || event.getAction() == MotionEvent.ACTION_DOWN
                        || event.getAction() == MotionEvent.ACTION_UP) {

                    float x = event.getX(); // touch event are in dp units.
                    float y = event.getY();

                    if (x < 0.f)
                        x = 0.f;
                    if (x > viewSatVal.getMeasuredWidth())
                        x = viewSatVal.getMeasuredWidth();
                    if (y < 0.f)
                        y = 0.f;
                    if (y > viewSatVal.getMeasuredHeight())
                        y = viewSatVal.getMeasuredHeight();

                    setSat(1.f / viewSatVal.getMeasuredWidth() * x);
                    setVal(1.f - (1.f / viewSatVal.getMeasuredHeight() * y));

                    // update view
                    moveTarget();
                    viewNewColor.setBackgroundColor(getColor());

                    return true;
                }
                return false;
            }
        });

        dialog = new AlertDialog.Builder(ColorPicker.this)
                .setOnCancelListener(new OnCancelListener() {
                    // if back button is used, call back our listener.
                    @Override
                    public void onCancel(DialogInterface paramDialogInterface) {
                        ColorPicker.this.setResult(RESULT_CANCELED, new Intent());
                        ColorPicker.this.finish();
                    }
                })
                .create();

        mCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();               
            }            
        });

        mOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", String.valueOf(getColor()));
                dialog.dismiss();
                ColorPicker.this.setResult(RESULT_OK, returnIntent);
                ColorPicker.this.finish();                
            }            
        });
        // kill all padding from the dialog window
        dialog.setView(view, 0, 0, 0, 0);

        // move cursor & target on first draw
        ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int buttonWidth = Math.round(((viewSatVal.getWidth() + viewHue.getWidth()) / 2) + 12);
                mOk.setWidth(buttonWidth);
                mCancel.setWidth(buttonWidth);
                moveCursor();
                moveTarget();
                view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    protected void moveCursor() {
        float y = viewHue.getMeasuredHeight() - (getHue() * viewHue.getMeasuredHeight() / 360.f);
        if (y == viewHue.getMeasuredHeight())
            y = 0.f;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewCursor
                .getLayoutParams();
        layoutParams.leftMargin = (int) (viewHue.getLeft()
                - Math.floor(viewCursor.getMeasuredWidth() / 2) - viewContainer.getPaddingLeft());
        ;
        layoutParams.topMargin = (int) (viewHue.getTop() + y
                - Math.floor(viewCursor.getMeasuredHeight() / 2) - viewContainer.getPaddingTop());
        ;
        viewCursor.setLayoutParams(layoutParams);
    }

    protected void moveTarget() {
        float x = getSat() * viewSatVal.getMeasuredWidth();
        float y = (1.f - getVal()) * viewSatVal.getMeasuredHeight();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewTarget
                .getLayoutParams();
        layoutParams.leftMargin = (int) (viewSatVal.getLeft() + x
                - Math.floor(viewTarget.getMeasuredWidth() / 2) - viewContainer.getPaddingLeft());
        layoutParams.topMargin = (int) (viewSatVal.getTop() + y
                - Math.floor(viewTarget.getMeasuredHeight() / 2) - viewContainer.getPaddingTop());
        viewTarget.setLayoutParams(layoutParams);
    }

    private int getColor() {
        return Color.HSVToColor(currentColorHsv);
    }

    private float getHue() {
        return currentColorHsv[0];
    }

    private float getSat() {
        return currentColorHsv[1];
    }

    private float getVal() {
        return currentColorHsv[2];
    }

    private void setHue(float hue) {
        currentColorHsv[0] = hue;
    }

    private void setSat(float sat) {
        currentColorHsv[1] = sat;
    }

    private void setVal(float val) {
        currentColorHsv[2] = val;
    }

    public void show() {
        dialog.show();
    }

    public AlertDialog getDialog() {
        return dialog;
    }
}
