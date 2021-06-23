package com.example.animalcare.CRUD;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.animalcare.R;
import com.example.animalcare.ml.MobilenetV110224Quant;
import com.example.animalcare.models.Animal;
import com.example.animalcare.usersMainScreens.AdminHomeActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import id.zelory.compressor.Compressor;

import static android.content.ContentValues.TAG;

public class AddAnimalActivity extends AppCompatActivity {
    private ImageView animalImage;
    private EditText arrivingDateET, ageET, colorET, descriptionET;
    private RadioButton maleRB, femaleRB, dogRB, catRB, activRB, middleRB, inactivRB, smallRB, mediumRB, bigRB;
    private CheckBox diseaseCB;
    private AppCompatButton addAnimalBtn;

    private String arrivingDate, age, color, description;
    private double ageD;
    private String gender, species, animalID, image, breed = "";
    private boolean disease;
    private int personalityType, size;

    // cloud database
    public FirebaseFirestore db;
    public CollectionReference animalsCollection;
    private int collectionSize = 0;

    private ProgressDialog progressDialog;
    private Uri imageUri = null;
    private StorageReference storageReference;
    private Bitmap compressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_animal);

        initViews();
        firestoreInit();

        // get the number of animals in collection
        animalsCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    collectionSize++;
                }
                Toast.makeText(AddAnimalActivity.this, "No of animals: " + collectionSize, Toast.LENGTH_LONG).show();
                collectionSize++;
                animalID = "animal_" + collectionSize;

                progressDialog = new ProgressDialog(this);
                animalImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (ContextCompat.checkSelfPermission(AddAnimalActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                Toast.makeText(AddAnimalActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                                ActivityCompat.requestPermissions(AddAnimalActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                            } else {
                                choseImage();
                            }
                        } else {
                            choseImage();
                        }
                    }
                });


                addAnimalBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        progressDialog.setMessage("Storing Data...");
                        progressDialog.show();

                        assignData();

                        if(!emptyFields() && radioSelected() && imageUri != null) {

                            File newFile = new File(imageUri.getPath());
                            try {
                                compressed = new Compressor(AddAnimalActivity.this)
                                        .setMaxHeight(224).setMaxWidth(224)
                                        .setQuality(50).compressToBitmap(newFile);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            compressed.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                            byte[] thumbData = byteArrayOutputStream.toByteArray();


                            try {

                                // Resize image
                                Bitmap animalImgResized = Bitmap.createScaledBitmap(compressed, 224, 224, true);


                                MobilenetV110224Quant modelBreeds = MobilenetV110224Quant.newInstance(getBaseContext());
                                TensorBuffer inputImage = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.UINT8);
                                inputImage.loadBuffer(TensorImage.fromBitmap(animalImgResized).getBuffer());

                                // Runs model inference and gets result.
                                MobilenetV110224Quant.Outputs outputs = modelBreeds.process(inputImage);
                                TensorBuffer breeds = outputs.getOutputFeature0AsTensorBuffer();

                                float[] results = breeds.getFloatArray();
                                breed = getMaxBreed(results);

//                                float max = 0.0f;
//                                int poz = 0;
//                                for (int i = 0; i < results.length; i++) {
//                                    if (results[i] > max) {
//                                        max = results[i];
//                                        poz = i;
//                                    }
//                                }
//                                String l = get("labels.txt");
//                                String[] labelsArray = l.split("\n");

                                // Close model
                                modelBreeds.close();

                            } catch (IOException e) {
                                // Handle the exception
                            }

                            UploadTask image_path = storageReference.child("animal_image").child(animalID + ".jpg").putBytes(thumbData);

                            Task<Uri> urlTask = image_path.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }

                                    // Continue with the task to get the download URL
                                    return storageReference.child("animal_image").child(animalID + ".jpg").getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Uri downloadUri = task.getResult();
                                        image = downloadUri.toString();

                                        Animal newAnimal = new Animal(arrivingDate, ageD, gender, species, color, description, disease, personalityType, size, animalID, image);
                                        newAnimal.setBreed(breed);

                                        db.collection("Animals").document(animalID).set(newAnimal).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(AddAnimalActivity.this, "Animal Data is Stored Successfully", Toast.LENGTH_LONG).show();

                                                    // Redirect to All animals
                                                    Intent intent = new Intent(AddAnimalActivity.this, AnimalsListActivity.class);
                                                    finish();
                                                    startActivity(intent);
                                                }
                                                else {
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(AddAnimalActivity.this, "(FIRESTORE Error) : " + error, Toast.LENGTH_LONG).show();
                                                }
                                                progressDialog.dismiss();
                                            }

                                        });

                                    } else {
                                        // Handle failures
                                    }
                                }
                            });
                        }
                    }
                });
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
                collectionSize = 0;
            }
        });
    }

    private String getMaxBreed(float[] results) {
        float max = 0.0f;
        int poz = 0;
        for (int i = 0; i < results.length; i++) {
            if (results[i] > max) {
                max = results[i];
                poz = i;
            }
        }
        String l = get("labels.txt");
        String[] labelsArray = l.split("\n");
        return labelsArray[poz];
    }

    private void storeAnimalData(Task<UploadTask.TaskSnapshot> task) {
        Uri download_uri;
        if (task != null) {
            download_uri = task.getResult().getStorage().getDownloadUrl().getResult();
        }
        else {
            download_uri = imageUri;
        }

        image = download_uri.toString();
        Animal newAnimal = new Animal(arrivingDate, ageD, gender, species, color, description, disease, personalityType, size, animalID, image);

        db.collection("Animals").document(animalID).set(newAnimal).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(AddAnimalActivity.this, "Animal Data is Stored Successfully", Toast.LENGTH_LONG).show();
                }
                else {
                    String error = task.getException().getMessage();
                    Toast.makeText(AddAnimalActivity.this, "(FIRESTORE Error) : " + error, Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                animalImage.setImageURI(imageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void choseImage() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(AddAnimalActivity.this);
    }

    private boolean radioSelected() {
        if (!maleRB.isChecked() && !femaleRB.isChecked()) {
            Toast.makeText(AddAnimalActivity.this, "Select an option for gender", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!dogRB.isChecked() && !catRB.isChecked()) {
            Toast.makeText(AddAnimalActivity.this, "Select an option for species", Toast.LENGTH_LONG).show();
            return false;
        }
        if (activRB.isChecked() && middleRB.isChecked() && inactivRB.isChecked()) {
            Toast.makeText(AddAnimalActivity.this, "Select an option for personality", Toast.LENGTH_LONG).show();
            return false;
        }
        if (smallRB.isChecked() && mediumRB.isChecked() && bigRB.isChecked()) {
            Toast.makeText(AddAnimalActivity.this, "Select an option for size", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean emptyFields() {
        if (arrivingDate.equals("") || age.equals("") || color.equals("") || description.equals("")) {
            Toast.makeText(AddAnimalActivity.this, "All fields required!", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    public void firestoreInit() {
        db = FirebaseFirestore.getInstance();
        animalsCollection = db.collection("Animals");
        storageReference = FirebaseStorage.getInstance().getReference();
    }


    private void assignData() {
        arrivingDate = arrivingDateET.getText().toString();
        age = ageET.getText().toString();
        ageD = Double.parseDouble(age);
        color = colorET.getText().toString();
        description = descriptionET.getText().toString();

        gender = (maleRB.isChecked() ? Animal.MALE : Animal.FEMALE);
        species = (dogRB.isChecked() ? Animal.DOG : Animal.CAT);
        disease = diseaseCB.isChecked();
        personalityType = (activRB.isChecked() ? 3 : (inactivRB.isChecked() ? 1 : 2));
        size = (bigRB.isChecked() ? 3 : (mediumRB.isChecked() ? 2 : 1));

    }

    private void initViews() {
        animalImage = findViewById(R.id.activity_add_animal_img);
        arrivingDateET = findViewById(R.id.activity_add_animal_et_arriving_date);
        ageET = findViewById(R.id.activity_add_animal_et_age);
        colorET = findViewById(R.id.activity_add_animal_et_color);
        descriptionET = findViewById(R.id.activity_add_animal_et_description);
        maleRB = findViewById(R.id.activity_add_animal_rb_male);
        femaleRB = findViewById(R.id.activity_add_animal_rb_female);
        dogRB = findViewById(R.id.activity_add_animal_rb_dog);
        catRB = findViewById(R.id.activity_add_animal_rb_cat);
        activRB = findViewById(R.id.activity_add_animal_rb_active);
        middleRB = findViewById(R.id.activity_add_animal_rb_activ_inactiv);
        inactivRB = findViewById(R.id.activity_add_animal_rb_inactiv);
        smallRB = findViewById(R.id.activity_add_animal_rb_small);
        mediumRB = findViewById(R.id.activity_add_animal_rb_medium);
        bigRB = findViewById(R.id.activity_add_animal_rb_big);
        diseaseCB = findViewById(R.id.activity_add_animal_cb_disease);
        addAnimalBtn = findViewById(R.id.activity_add_animal_btn_add);
    }
    public String get(String fileName) {
        InputStream is = null;
        try {
            is = getApplication().getAssets().open(fileName);
            int lenght = is.available();
            byte[] buffer = new byte[lenght];
            is.read(buffer);
            String result = new String(buffer, "utf8");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}