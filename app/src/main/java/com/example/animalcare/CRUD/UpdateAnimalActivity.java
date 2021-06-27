package com.example.animalcare.CRUD;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.animalcare.R;
import com.example.animalcare.ml.MobilenetV110224Quant;
import com.example.animalcare.models.Animal;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import id.zelory.compressor.Compressor;

import static android.content.ContentValues.TAG;
import static com.example.animalcare.animalsActivities.AnimalDetailsActivity.CurrentAnimal;
import static com.example.animalcare.animalsActivities.AnimalDetailsActivity.currentAnimalID;
import static com.example.animalcare.models.Animal.DOG;
import static com.example.animalcare.models.Animal.MALE;

public class UpdateAnimalActivity extends AppCompatActivity {
    private ImageView animalImage;
    private EditText arrivingDateET, ageET, colorET, descriptionET;
    private RadioButton maleRB, femaleRB, dogRB, catRB, activRB, middleRB, inactivRB, smallRB, mediumRB, bigRB;
    private CheckBox diseaseCB;
    private AppCompatButton updateAnimalBtn;

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
        setContentView(R.layout.activity_update_animal);

        initViews();
        firestoreInit();
        assignDataDefault();

        progressDialog = new ProgressDialog(this);
        animalImage.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(UpdateAnimalActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(UpdateAnimalActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(UpdateAnimalActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                } else {
                    choseImage();
                }
            } else {
                choseImage();
            }
        });

        updateAnimalBtn.setOnClickListener(u -> {
            assignData();

            if (!emptyFields() && radioSelected() && validInput()) {
                ageD = Double.parseDouble(age);
                progressDialog.setMessage("Storing Data...");
                progressDialog.show();

                if (imageUri != null) {
                    // Get image and compress
                    File newFile = new File(imageUri.getPath());
                    try {
                        compressed = new Compressor(UpdateAnimalActivity.this)
                                .setMaxHeight(224).setMaxWidth(224)
                                .setQuality(50).compressToBitmap(newFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Transform image in byte array
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    compressed.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] thumbData = byteArrayOutputStream.toByteArray();

                    // Find animal breed from image
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

                        // Close model
                        modelBreeds.close();

                    } catch (IOException e) {
                        // Handle the exception
                    }

                    // Store image and update animal data in fireStore
                    UploadTask image_path = storageReference.child("animal_image").child(animalID + ".jpg").putBytes(thumbData);
                    Task<Uri> urlTask = image_path.continueWithTask(task -> {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        // Continue with the task to get the download URL
                        return storageReference.child("animal_image").child(animalID + ".jpg").getDownloadUrl();
                    }).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            image = downloadUri.toString();

                            Animal newAnimal = new Animal(arrivingDate, ageD, gender, species, color, description, disease, personalityType, size, animalID, image);
                            newAnimal.setBreed(breed);

                            db.collection("Animals")
                                    .document(animalID)
                                    .set(newAnimal)
                                    .addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    progressDialog.dismiss();
                                    Toast.makeText(UpdateAnimalActivity.this, "Animal Data is Stored Successfully", Toast.LENGTH_LONG).show();

                                    // Redirect to All animals
                                    Intent intent = new Intent(UpdateAnimalActivity.this, AnimalsListActivity.class);
                                    finish();
                                    startActivity(intent);
                                }
                                else {
                                    String error = task1.getException().getMessage();
                                    Toast.makeText(UpdateAnimalActivity.this, "(FIRESTORE Error) : " + error, Toast.LENGTH_LONG).show();
                                }
                                progressDialog.dismiss();
                            });
                        } else {
                            // Handle failures
                        }
                    });
                }
                else {
                    Animal newAnimal = new Animal(arrivingDate, ageD, gender, species, color, description, disease, personalityType, size, animalID, image);
                    newAnimal.setBreed(breed);

                    db.collection("Animals").document(animalID).set(newAnimal).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(UpdateAnimalActivity.this, "Animal Data is Stored Successfully", Toast.LENGTH_LONG).show();

                            // Redirect to All animals
                            Intent intent = new Intent(UpdateAnimalActivity.this, AnimalsListActivity.class);
                            finish();
                            startActivity(intent);
                        }
                        else {
                            String error = task1.getException().getMessage();
                            Toast.makeText(UpdateAnimalActivity.this, "(FIRESTORE Error) : " + error, Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    });
                }
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

    private void assignDataDefault() {
        // Get the animal ID for the current animal
        SharedPreferences sharedpreferences = getSharedPreferences(CurrentAnimal, Context.MODE_PRIVATE);
        animalID = sharedpreferences.getString(currentAnimalID, "");

        // Get the data from FireStore for the current animal
        animalsCollection.document(animalID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                Animal animal = document.toObject(Animal.class);
                if (animal != null) {
                    arrivingDateET.setText(animal.getArrivingDate());
                    ageET.setText(String.valueOf(animal.getAge()));
                    colorET.setText(animal.getColor());
                    descriptionET.setText(animal.getDescription());

                    if (animal.getGender().equals(MALE)) {
                        maleRB.setChecked(true);
                        femaleRB.setChecked(false);
                    } else {
                        maleRB.setChecked(false);
                        femaleRB.setChecked(true);
                    }

                    if (animal.getSpecies().equals(DOG)) {
                        dogRB.setChecked(true);
                        catRB.setChecked(false);
                    } else {
                        dogRB.setChecked(false);
                        catRB.setChecked(true);
                    }

                    if (animal.getPersonalityType() == 1) {
                        inactivRB.setChecked(true);
                        activRB.setChecked(false);
                        middleRB.setChecked(false);
                    } else if (animal.getPersonalityType() == 2) {
                        inactivRB.setChecked(false);
                        activRB.setChecked(false);
                        middleRB.setChecked(true);
                    } else {
                        inactivRB.setChecked(false);
                        activRB.setChecked(true);
                        middleRB.setChecked(false);
                    }

                    if (animal.getSize() == 1) {
                        smallRB.setChecked(true);
                        mediumRB.setChecked(false);
                        bigRB.setChecked(false);
                    } else if (animal.getSize() == 2) {
                        smallRB.setChecked(false);
                        mediumRB.setChecked(true);
                        bigRB.setChecked(false);
                    } else {
                        smallRB.setChecked(false);
                        mediumRB.setChecked(false);
                        bigRB.setChecked(true);
                    }

                    diseaseCB.setChecked(animal.getDisease());

                    // Set animal image
                    if (animal.getImage() != null) {
                        image = animal.getImage();
                        Glide.with(this).load(animal.getImage()).into(animalImage);
                    }

                }
            } else {
                Log.d(TAG, "Failed with: ", task.getException());
            }
        });
    }

    public void firestoreInit() {
        db = FirebaseFirestore.getInstance();
        animalsCollection = db.collection("Animals");
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    private void assignData() {
        arrivingDate = arrivingDateET.getText().toString();
        age = ageET.getText().toString();
        color = colorET.getText().toString();
        description = descriptionET.getText().toString();

        gender = (maleRB.isChecked() ? MALE : Animal.FEMALE);
        species = (dogRB.isChecked() ? Animal.DOG : Animal.CAT);
        disease = diseaseCB.isChecked();
        personalityType = (activRB.isChecked() ? 3 : (inactivRB.isChecked() ? 1 : 2));
        size = (bigRB.isChecked() ? 3 : (mediumRB.isChecked() ? 2 : 1));

    }

    private void initViews() {
        animalImage = findViewById(R.id.activity_update_animal_img);
        arrivingDateET = findViewById(R.id.activity_update_animal_et_arriving_date);
        ageET = findViewById(R.id.activity_update_animal_et_age);
        colorET = findViewById(R.id.activity_update_animal_et_color);
        descriptionET = findViewById(R.id.activity_update_animal_et_description);
        maleRB = findViewById(R.id.activity_update_animal_rb_male);
        femaleRB = findViewById(R.id.activity_update_animal_rb_female);
        dogRB = findViewById(R.id.activity_update_animal_rb_dog);
        catRB = findViewById(R.id.activity_update_animal_rb_cat);
        activRB = findViewById(R.id.activity_update_animal_rb_active);
        middleRB = findViewById(R.id.activity_update_animal_rb_activ_inactiv);
        inactivRB = findViewById(R.id.activity_update_animal_rb_inactiv);
        smallRB = findViewById(R.id.activity_update_animal_rb_small);
        mediumRB = findViewById(R.id.activity_update_animal_rb_medium);
        bigRB = findViewById(R.id.activity_update_animal_rb_big);
        diseaseCB = findViewById(R.id.activity_update_animal_cb_disease);
        updateAnimalBtn = findViewById(R.id.activity_update_animal_btn_update);
    }

    private boolean emptyFields() {
        if (arrivingDate.equals("") || age.equals("") || color.equals("") || description.equals("")) {
            Toast.makeText(UpdateAnimalActivity.this, "All fields required!", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    private boolean radioSelected() {
        if (!maleRB.isChecked() && !femaleRB.isChecked()) {
            Toast.makeText(UpdateAnimalActivity.this, "Select an option for gender", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!dogRB.isChecked() && !catRB.isChecked()) {
            Toast.makeText(UpdateAnimalActivity.this, "Select an option for species", Toast.LENGTH_LONG).show();
            return false;
        }
        if (activRB.isChecked() && middleRB.isChecked() && inactivRB.isChecked()) {
            Toast.makeText(UpdateAnimalActivity.this, "Select an option for personality", Toast.LENGTH_LONG).show();
            return false;
        }
        if (smallRB.isChecked() && mediumRB.isChecked() && bigRB.isChecked()) {
            Toast.makeText(UpdateAnimalActivity.this, "Select an option for size", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean validInput() {

        if (!verifyDateFormat(arrivingDate)) {
            Toast.makeText(UpdateAnimalActivity.this, "Please enter a valid date! (Eg. 21/05/2021)", Toast.LENGTH_LONG).show();
            arrivingDateET.setText("");
            return false;
        }
        if (!verifyIfStringIsDouble(age)) {
            Toast.makeText(UpdateAnimalActivity.this, "The age should be a number! (Eg. 2.5)", Toast.LENGTH_LONG).show();
            ageET.setText("");
            return false;
        }
        return true;
    }

    private boolean verifyIfStringIsDouble (String input) {

        // regular expression for a floating point number
        String regex = "[+-]?[0-9]+(\\.[0-9]+)?([Ee][+-]?[0-9]+)?";

        // compiling regex
        Pattern p = Pattern.compile(regex);

        // Creates a matcher that will match input1 against regex
        Matcher m = p.matcher(input);

        // If match found and equal to input1
        if(m.find() && m.group().equals(input))
            return true;
        return false;
    }

    private boolean verifyDateFormat (String input) {
        // regular expression for a floating point number
        String regex = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[13-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";

        // compiling regex
        Pattern p = Pattern.compile(regex);

        // Creates a matcher that will match input1 against regex
        Matcher m = p.matcher(input);

        // If match found and equal to input
        if(m.find() && m.group().equals(input))
            return true;
        return false;
    }

    private void choseImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(UpdateAnimalActivity.this);
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

}