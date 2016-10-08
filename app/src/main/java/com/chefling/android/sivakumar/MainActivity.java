package com.chefling.android.sivakumar;


import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class MainActivity extends AppCompatActivity {

    /**
     * Persist URI image to crop URI if specific permissions are required
     */
    private Uri mCropImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // widgets Declaration
        final EditText recipe_name = (EditText) findViewById(R.id.recipe_name);
        final EditText desc = (EditText) findViewById(R.id.desc);
        final Spinner recipe_type = (Spinner) findViewById(R.id.recipe_type);
        RadioGroup chef_exp = (RadioGroup) findViewById(R.id.radio_group);
        final TextView cooking_time = (TextView) findViewById(R.id.time_picker);
        final TextView cancel_text = (TextView) findViewById(R.id.cancel);
        Button next_button = (Button) findViewById(R.id.next);
        final Spinner serves = (Spinner) findViewById(R.id.serves);

        // serve adaptor for serve spinner
        ArrayAdapter<CharSequence> serve_adaptor = ArrayAdapter.createFromResource(
                this, R.array.serves, R.layout.spinner_design);
        serve_adaptor.setDropDownViewResource(R.layout.spinner_design);
        serves.setAdapter(serve_adaptor);

        // Checked change Listener for chef experience radio group
        chef_exp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.beginner:
                        Toast.makeText(getApplicationContext(), "beginner", Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.sous_chef:
                        Toast.makeText(getApplicationContext(), "sous chef", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.master:
                        Toast.makeText(getApplicationContext(), "master", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });

        // Cooking time listener for select time from clock.
        cooking_time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
//                int hour = 0;
//                int minute = 0;
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        cooking_time.setText(selectedHour + ":" + selectedMinute + " Hrs");
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        /**
          Next Button on click listener*/
        next_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                String recipe_name_string = recipe_name.getText().toString();
                if (recipe_name_string.matches("")) {
                    Toast.makeText(MainActivity.this, "please enter a recipe name", Toast.LENGTH_SHORT).show();
                    recipe_name.requestFocus();
                    return;
                }
                String recipe_type_string = recipe_type.getSelectedItem().toString();
                if (recipe_type_string.matches("choose your recipe type")) {
                    Toast.makeText(MainActivity.this, "please choose recipe type", Toast.LENGTH_SHORT).show();
                    recipe_type.requestFocus();
                    return;
                }
                String serves_string = serves.getSelectedItem().toString();
                if (serves_string.matches("Serves")) {
                    Toast.makeText(MainActivity.this, "please choose serves", Toast.LENGTH_SHORT).show();
                    serves.requestFocus();
                    return;
                }
                String cooking_time_string = cooking_time.getText().toString();
                if (cooking_time_string.matches("Cooking Time")) {
                    Toast.makeText(MainActivity.this, "please select cooking time", Toast.LENGTH_SHORT).show();
                    cooking_time.requestFocus();
                    return;
                }
                String desc_string = desc.getText().toString();
                if (desc_string.matches("")) {
                    Toast.makeText(MainActivity.this, "please fill the description", Toast.LENGTH_SHORT).show();
                    desc.requestFocus();
                    return;
                }

                Toast.makeText(MainActivity.this, "Your recipe is saved.", Toast.LENGTH_SHORT).show();

            }
        });

        // Cancel on click Listener for close app
        cancel_text.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // TODO:
                // This function closes Activity Two
                // Hint: use Context's finish() method
                finish();
            }
        });


    }


    /**
     * Start pick image activity with chooser.
     */
    public void onSelectImageClick(View view) {
        CropImage.startPickImageActivity(this);
    }

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result of pick image chooser
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                ((ImageView) findViewById(R.id.quick_start_cropped_image)).setImageURI(result.getUri());
//                Toast.makeText(this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();
                Toast.makeText(this, "photo successfully uploaded ", Toast.LENGTH_LONG).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // required permissions granted, start crop image activity
            startCropImageActivity(mCropImageUri);
        } else {
            Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Start crop image activity for the given image.
     */
    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }


}
