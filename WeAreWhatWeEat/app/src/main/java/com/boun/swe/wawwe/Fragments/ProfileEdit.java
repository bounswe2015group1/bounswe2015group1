package com.boun.swe.wawwe.Fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.boun.swe.wawwe.App;
import com.boun.swe.wawwe.MainActivity;
import com.boun.swe.wawwe.Models.User;
import com.boun.swe.wawwe.R;
import com.boun.swe.wawwe.Utils.API;
import com.boun.swe.wawwe.Utils.Commons;
import com.fourmob.datetimepicker.date.DatePickerDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Mert on 09/11/15.
 */
public class ProfileEdit extends LeafFragment implements DatePickerDialog.OnDateSetListener {

    private String DATEPICKER_TAG = "date_picker";

    EditText email;
    EditText password;
    EditText fullName;
    EditText location;
    TextView dateOfBirthText;
    LinearLayout holderDateOfBirth;
    
    Date dateOfBirth;

    public ProfileEdit() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        TAG = App.getInstance().getString(R.string.title_menu_profileEdit);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View profileView = inflater.inflate(R.layout.layout_fragment_profile_edit,
                container, false);

        User user = App.getUser();

        ImageView avatar = (ImageView) profileView.findViewById(R.id.profileEdit_image);
        email = (EditText) profileView.findViewById(R.id.profileEdit_email);
        password = (EditText) profileView.findViewById(R.id.profileEdit_password);
        fullName = (EditText) profileView.findViewById(R.id.profileEdit_fullName);
        location = (EditText) profileView.findViewById(R.id.profileEdit_location);
        dateOfBirthText = (TextView) profileView.findViewById(R.id.profileEdit_dateOfBirth);
        holderDateOfBirth = (LinearLayout) profileView.findViewById(R.id.profileEdit_holder_dateOfBirth);
        holderDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                        ProfileEdit.this,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.setVibrate(false);
                datePickerDialog.setYearRange(1950, calendar.get(Calendar.YEAR));
                datePickerDialog.setCloseOnSingleTapDay(false);
                datePickerDialog.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });

        setUserInfo(user);

        return profileView;
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        dateOfBirth = Commons.getDate(year, month, day);
        dateOfBirthText.setText(Commons.prettifyDate(dateOfBirth)[1]);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);
        menu.findItem(R.id.menu_profile_editDone).setIcon(R.mipmap.ic_done_black_24dp);
        menu.findItem(R.id.menu_profile_add).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_profile_editDone:
                if (fullName.isEnabled()) {
                    final User user = App.getUser();

                    String mail = email.getText().toString();
                    final String pass = password.getText().toString();
                    String name = fullName.getText().toString();
                    String loc = location.getText().toString();
                    String date_str = dateOfBirthText.getText().toString();
                    DateFormat format = new SimpleDateFormat("d MMMM yyyy", Locale.ENGLISH);
                    Date date = null;
                    try {
                        date = format.parse(date_str);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (!mail.isEmpty())
                        user.setEmail(mail);
                    if (!pass.isEmpty())
                        user.setPassword(pass);
                    if (!name.isEmpty())
                        user.setFullName(name);
                    if (!loc.isEmpty())
                        user.setLocation(loc);
                    if (!date_str.isEmpty())
                        user.setDateOfBirth(date);

                    API.updateUser(getTag(), user,
                            new Response.Listener<User>() {
                                @Override
                                public void onResponse(User response) {
                                    response.setPassword(pass);
                                    App.setUser(response);
                                    if (context instanceof MainActivity) {
                                        MainActivity main = (MainActivity) context;
                                        main.onBackPressed();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(context, context.getString(R.string.error_updateUser),
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setUserInfo(User user) {
        if (user == null) return;

        if (user.getEmail() != null)
            email.setText(user.getEmail());
        if (user.getPassword() != null)
            password.setText(user.getPassword());
        if (user.getFullName() != null)
            fullName.setText(user.getFullName());
        if (user.getLocation() != null)
            location.setText(user.getLocation());
        if (user.getDateOfBirth() != null)
            dateOfBirthText.setText(Commons
                    .prettifyDate(user.getDateOfBirth())[1]);
    }

    public static ProfileEdit getFragment(Bundle bundle) {
        ProfileEdit profileEditFragment = new ProfileEdit();
        profileEditFragment.setArguments(bundle);
        return profileEditFragment;
    }
}