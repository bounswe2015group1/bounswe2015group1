package com.boun.swe.wawwe.Fragments;

import android.graphics.drawable.LevelListDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.boun.swe.wawwe.App;
import com.boun.swe.wawwe.MainActivity;
import com.boun.swe.wawwe.Models.User;
import com.boun.swe.wawwe.R;
import com.boun.swe.wawwe.Utils.API;
import com.fourmob.datetimepicker.date.DatePickerDialog;

import java.util.Calendar;

/**
 * Created by onurguler on 07/12/15.
 */
public class Signup extends BaseFragment implements DatePickerDialog.OnDateSetListener {

    private String DATEPICKER_TAG = "date_picker";

    EditText dateOfBirthEditText;

    public Signup() { }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View signupView = inflater.inflate(R.layout.layout_fragment_signup,
                container, false);

        final EditText emailEditText = (EditText) signupView.findViewById(R.id.userEmail);
        final EditText passwordEditText = (EditText) signupView.findViewById(R.id.password);
        final EditText fullNameEditText = (EditText) signupView.findViewById(R.id.fullname);
        final EditText locationEditText = (EditText) signupView.findViewById(R.id.location);
        dateOfBirthEditText = (EditText) signupView.findViewById(R.id.dateOfBirth);
        final CheckBox isRestaurantCheckBox = (CheckBox) signupView.findViewById(R.id.checkBox_isRestaurant);

        dateOfBirthEditText.setKeyListener(null);

        Button signupButton = (Button) signupView.findViewById(R.id.button_signUp);

        dateOfBirthEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                        Signup.this,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.setVibrate(false);
                datePickerDialog.setYearRange(1950, calendar.get(Calendar.YEAR));
                datePickerDialog.setCloseOnSingleTapDay(false);
                datePickerDialog.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String fullname = fullNameEditText.getText().toString();
                String location = locationEditText.getText().toString();
                String dateOfBirth = dateOfBirthEditText.getText().toString();
                boolean isRestaurant = isRestaurantCheckBox.isChecked();

                if (!isInputValid(new EditText[]{
                        emailEditText, passwordEditText, fullNameEditText,
                        locationEditText, dateOfBirthEditText})) {
                    return;
                }

                User user = new User(email, isRestaurant, password, fullname, location, dateOfBirth);

                API.addUser(getTag(), user,
                        new Response.Listener<User>() {
                            @Override
                            public void onResponse(User response) {

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(App.getInstance(),"Error Signing Up",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        return signupView;
    }

    private boolean isInputValid(EditText[] textFields) {
        boolean isValid = true;
        LevelListDrawable drawable;
        for (EditText textField: textFields) {
            String text = textField.getText().toString();

            drawable = (LevelListDrawable)
                    textField.getBackground();
            if (text.isEmpty()) {
                drawable.setLevel(2);
                if (isValid)
                    isValid = false;
            }
            else drawable.setLevel(1);
        }
        return isValid;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (context instanceof MainActivity) {
            MainActivity main = (MainActivity) context;
            main.getSupportActionBar().show();
        }
    }

    public static Signup getFragment(Bundle bundle) {
        Signup signupFragment = new Signup();
        signupFragment.setArguments(bundle);
        return signupFragment;
    }

    private void exitSignupFragment() {
        if (context instanceof MainActivity) {
            MainActivity main = (MainActivity) context;
//            main.getSupportFragmentManager().beginTransaction()
//                    .remove(Login.this).commit();
//            main.findViewById(R.id.container).invalidate();
            main.removeFragment(this);
            main.makeFragmentTransaction(Feeds.getFragment(new Bundle()));
        }
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, 0, 0);
        dateOfBirthEditText.setText(String.format("%d-%02d-%02d", year, month, day));
    }
}
