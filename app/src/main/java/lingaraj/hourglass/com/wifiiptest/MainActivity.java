package lingaraj.hourglass.com.wifiiptest;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import lingaraj.hourglass.com.wifiiptest.databinding.CardNetworkSelectionBinding;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";
    CardNetworkSelectionBinding view_binding;
    private AnimatorSet mSetRightOut;
    private AnimatorSet mSetLeftIn;
    private boolean mIsBackVisible = false;
    private View mCardFrontLayout;
    private View mCardBackLayout;
    private View network_selection_dialog;
    private Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
          showActioDialog();
    }

    private void findViews() {
        mCardBackLayout = view_binding.cardBack;
        mCardFrontLayout = view_binding.cardFront;
    }
    private void loadAnimations() {
        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.out_animation);
        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.in_animation);
    }





    private void showActioDialog() {
        Activity activity = MainActivity.this;
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        network_selection_dialog = LayoutInflater.from(activity).inflate(R.layout.card_network_selection,null);
        view_binding = DataBindingUtil.bind(network_selection_dialog);
        findViews();
        loadAnimations();
        changeCameraDistance();
        view_binding.includedFront.cloudNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        view_binding.includedFront.localNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // showIpAddressInputLayout();
                 // flipCard((View) view.getParent());
                //flipCard((View) view.getParent().getParent().getParent().getParent().getParent());
                flipCard(view);
                 }
        });

       view_binding.includedBack.ipAddressEdittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    updateClick(v);

                }
                return false;
            }
        });
        view_binding.includedBack.ipAddressEdittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showKeyboard();
            }
        });
        dialog.setContentView(network_selection_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();



    }

    private void showKeyboard()
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
        imm.showSoftInput(view_binding.includedBack.ipAddressEdittext, InputMethodManager.SHOW_IMPLICIT);
        Log.d(TAG,"Keyboard Shown");
    }

    private void hideKeyboard(View view)
    {
        if (view!=null)
        {
            InputMethodManager imm = (InputMethodManager)getSystemService(getApplication().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            Log.d(TAG,"Keyboard hidden");
        }
    }

    @SuppressLint("NewApi")
    private void changeCameraDistance() {
        int distance = 8000;
        float scale = getResources().getDisplayMetrics().density * distance;
        mCardFrontLayout.setCameraDistance(scale);
        mCardBackLayout.setCameraDistance(scale);
    }

    public void updateClick(View view)
    {
        String input_text = view_binding.includedBack.ipAddressEdittext.getText().toString();
        if (input_text.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Enter Server url to continue",Toast.LENGTH_SHORT).show();
            showKeyboard();
        }
        else
        {
            if (input_text.contains(":"))
            {
                String[] parts = input_text.split(":");
                String ip_address = parts[0];
                String port_number = parts[1];
                Log.d(TAG,"Entered Ip Address/port:"+ip_address+"/"+port_number);
                if (RegexHelpers.isProperIpAddress(ip_address))
                {
                    Log.d(TAG,"Proper Ip Address Entered");
                    dialog.dismiss();
                }
                else
                {
                    Log.d(TAG,"Renter the ip address,Previously Entered IP address was improper");
                    clearEdittext();
                    showKeyboard();

                }
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Enter proper IP address with port Number to continue",Toast.LENGTH_SHORT).show();
            }

        }
        Log.d(TAG,"Update button clicked");
    }

    private void clearEdittext() {
        view_binding.includedBack.ipAddressEdittext.setText("");
        Log.d(TAG,"cleard Editext");
    }

    public void cancelClick(View view)
    {
        Log.d(TAG,"Cancel Button Clicked");
        flipCard(view);

    }

    public void flipCard(View view) {
        if (!mIsBackVisible) {
            mSetRightOut.setTarget(mCardFrontLayout);
            mSetLeftIn.setTarget(mCardBackLayout);
            mSetRightOut.start();
            mSetLeftIn.start();
            mIsBackVisible = true;
            showKeyboard();
        } else {
            mSetRightOut.setTarget(mCardBackLayout);
            mSetLeftIn.setTarget(mCardFrontLayout);
            mSetRightOut.start();
            mSetLeftIn.start();
            mIsBackVisible = false;
            hideKeyboard(view);
        }
    }


}
