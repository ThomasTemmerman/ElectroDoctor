package be.ucll.electrodoctor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

//info: https://medium.com/@dimabatyuk/adding-clear-button-to-edittext-9655e9dbb721 (Kotlin)
public class EditTextClear {

    private static final String TAG = "EditTextClear";
    public static void enableClearButton(final EditText editText, Context context, final int clearIconsId) {
        Log.d(TAG, "Setting up clear button for EditText");
        Drawable clearIcon = ContextCompat.getDrawable(context, clearIconsId);
        if (clearIcon == null) {
            Log.e(TAG, "Clear icon drawable not found!");
            return;
        }
        clearIcon.setBounds(0, 0, clearIcon.getIntrinsicWidth(), clearIcon.getIntrinsicHeight());
        Log.d(TAG, "Clear icon loaded successfully");
        //Enkel icoon tonen als er tekst is
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    editText.setCompoundDrawables(null,null,clearIcon,null);
                } else {
                    editText.setCompoundDrawables(null,null,null,null);
                }
            }
        });
        //detecteren als er geklikt is op de clear icon
        editText.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "onTouch called - Action: " + event.getAction());
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Drawable drawableEnd = editText.getCompoundDrawables()[2];
                    if (drawableEnd != null) {
                        //touchable area defining
                        float touchX = event.getX();
                        int drawableWidth = drawableEnd.getBounds().width();//breedte icoon
                        int editTextWidth = editText.getWidth();//totale breedte van editveld
                        int touchAreaStart = editTextWidth - drawableWidth - editText.getPaddingRight();//berekenen van touchgebied
                        Log.d("EditTextClear", "Touch detected at X: " + touchX + ", touchAreaStart: " + touchAreaStart);


                        if (touchX >= touchAreaStart) {
                            editText.setText("");
                            editText.clearFocus();
                            return true;
                        }
                    }
                }
                    return false;
            }
        });
    }
}
