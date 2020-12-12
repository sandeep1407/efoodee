package com.uj.myapplications.customwidgets;

import android.widget.LinearLayout;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.uj.myapplications.R;
import com.uj.myapplications.interfaces.ValidNumberListener;
import com.uj.myapplications.utility.UtilityClass;


public class OTPView extends LinearLayout {
    private String TAG = getClass().getName();
    private Context context;
    private EditText editOne, editTwo, editThree, editFour;
    private TextWatcher watcher1, watcher2, watcher3, watcher4;
    private ValidNumberListener listener;
    private boolean a, b, c, d = false;

    public OTPView(Context context) {
        super(context);
        init(context);
    }

    public OTPView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public OTPView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        // inflating view here
        LayoutInflater.from(context).inflate(R.layout.viewotpcustom, this);
        // getting view here
        editOne = findViewById(R.id.one);
        editTwo = findViewById(R.id.two);
        editThree = findViewById(R.id.three);
        editFour = findViewById(R.id.four);
        // adding text watcher
        createWatchers();
        addTextChnageListeners();
        setClickListener();
    }

    private void addTextChnageListeners() {
        editOne.addTextChangedListener(watcher1);
        editTwo.addTextChangedListener(watcher2);
        editThree.addTextChangedListener(watcher3);
        editFour.addTextChangedListener(watcher4);
    }

    private void setClickListener() {

        editFour.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    // Toast.makeText(context, "cross button", Toast.LENGTH_SHORT).show();
                    editFour.setBackgroundResource(R.drawable.bg_grey_border_round);
                    editThree.requestFocus();
                    editThree.setSelection(editThree.getText().toString().trim().length());
                    d = false;
                    if (listener != null) {
                        listener.isPhoneNumberValide(a && b && c && d);
                    }

                }
                return false;
            }
        });
    }

    public void setValidNumberListener(ValidNumberListener listener) {
        this.listener = listener;
    }

    public void setOTPText(String strOTP) {
        if (!TextUtils.isEmpty(strOTP) && strOTP.length() == 4) {
            char arr[] = strOTP.toCharArray();
            // remove listeners
            removeListeners();
            editOne.setText("" + arr[0]);
            editTwo.setText("" + arr[1]);
            editThree.setText("" + arr[2]);
            editFour.setText("" + arr[3]);
            // again add listeners
            addTextChnageListeners();
            editFour.setSelection(editFour.getText().toString().trim().length());
            editFour.requestFocus();
        } else {
            Log.e(TAG, "setOTPText: can not set null or less than 4 length string");
        }
    }

    public String getOTPText() {
        return editOne.getText().toString().trim() + editTwo.getText().toString().trim() +
                editThree.getText().toString().trim() + editFour.getText().toString().trim();
    }

    private void removeListeners() {
        editOne.removeTextChangedListener(watcher1);
        editTwo.removeTextChangedListener(watcher2);
        editThree.removeTextChangedListener(watcher3);
        editFour.removeTextChangedListener(watcher4);
    }

    private void createWatchers() {
        watcher1 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, "beforeTextChanged: ");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    editOne.setBackgroundResource(R.drawable.bg_grey_border_round);
                    a = false;
                } else {
                    editOne.setBackgroundResource(R.drawable.bg_red_border_round);
                    editTwo.requestFocus();
                    a = true;
                }
                if (listener != null) {
                    listener.isPhoneNumberValide(a && b && c && d);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        watcher2 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, "beforeTextChanged: ");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    editTwo.setBackgroundResource(R.drawable.bg_grey_border_round);
                    editOne.requestFocus();
                    editOne.setSelection(editOne.getText().toString().trim().length());
                    b = false;
                } else {
                    editTwo.setBackgroundResource(R.drawable.bg_red_border_round);
                    editThree.requestFocus();
                    b = true;

                }
                if (listener != null) {
                    listener.isPhoneNumberValide(a && b && c && d);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        watcher3 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, "beforeTextChanged: ");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    editThree.setBackgroundResource(R.drawable.bg_grey_border_round);
                    editTwo.requestFocus();
                    editTwo.setSelection(editTwo.getText().toString().trim().length());
                    c = false;
                } else {
                    editThree.setBackgroundResource(R.drawable.bg_red_border_round);
                    editFour.requestFocus();
                    c = true;
                }
                if (listener != null) {
                    listener.isPhoneNumberValide(a && b && c && d);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        watcher4 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, "beforeTextChanged: ");

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count == 0) {
                    editFour.setBackgroundResource(R.drawable.bg_grey_border_round);
                    editThree.requestFocus();
                    editThree.setSelection(editThree.getText().toString().trim().length());
                    d = false;
                } else {
                    editFour.setBackgroundResource(R.drawable.bg_red_border_round);
                    UtilityClass.hideKeypad(context, editFour);
                    d = true;
                }
                if (listener != null) {
                    listener.isPhoneNumberValide(a && b && c && d);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }

    /*@Override
    public void isPhoneNumberValide(boolean b) {
        if (b) {
            // enable button here and change color
        } else {
            // disable button here and
        }
    }*/

    public void clearTextFromView() {
        editOne.setText("");
        editTwo.setText("");
        editThree.setText("");
        editFour.setText("");
        editOne.requestFocus();
    }
}