package com.example.psmsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class Admin_Recipes_Add extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference ref;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private ImageView profile;
    private final int PICK_IMAGE_REQUEST = 71;

    private EditText edit_name, edit_calories ,edit_serve,edit_categories;
    private Uri fileImage;
    Bitmap selectedImage;
    StorageReference storageReference;
    FirebaseStorage storage;
    ImageView imageView;

    Button add,image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__recipes__add);

        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        edit_name = findViewById(R.id.et_Name);
        edit_calories = findViewById(R.id.et_Calories);
        edit_serve = findViewById(R.id.et_Serve);
        edit_categories = findViewById(R.id.et_Categories);

        add = (Button)findViewById(R.id.add_button);

        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setRecipes();
            }
        });

        image = (Button)findViewById(R.id.image_button);

        image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });



    }

    public void setRecipes() {


                String name = edit_name.getText().toString().trim();
                String calories = edit_calories.getText().toString().trim();
                String serve = edit_serve.getText().toString().trim();
                String categories = edit_categories.getText().toString().trim();

                //validation to check if user age textview is empty
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getApplicationContext(), "Recipes Name cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                //validation to check if username textview is empty
                if (TextUtils.isEmpty(calories)) {
                    Toast.makeText(getApplicationContext(), "Calories cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                //validation to check if user age textview is empty
                if (TextUtils.isEmpty(serve)) {
                    Toast.makeText(getApplicationContext(), "Serving cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                //validation to check if user age textview is empty
                if (TextUtils.isEmpty(categories)) {
                    Toast.makeText(getApplicationContext(), "Categories cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }


                final Recipes updated_recipes = new Recipes();
                updated_recipes.setName(name);
                updated_recipes.setServe(serve);
                updated_recipes.setCalories(calories);
                updated_recipes.setCategories(categories);


                if (fileImage != null && selectedImage != null) {
                    StorageReference stoRefImage = storageReference.child("image/" + UUID.randomUUID().toString());
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();

                    stoRefImage.putBytes(data)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                                    while (!urlTask.isSuccessful()) ;
                                    Uri downloadUrl = urlTask.getResult();
                                    if (downloadUrl != null) {
                                        String urlimage = downloadUrl.toString();
                                        updated_recipes.setUrl_image(urlimage);

                                        ref.child("Recipes_List").push().setValue(updated_recipes, new DatabaseReference.CompletionListener() {
                                            public void onComplete(DatabaseError error, @NonNull DatabaseReference ref) {
                                                Toast.makeText(getApplicationContext(), "Add Recipes successfully", Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(AddMenuActivity.this, MenuActivity.class);
//                                                startActivity(intent);
                                                finish();
                                            }
                                        });

                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Admin_Recipes_Add.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                            .getTotalByteCount());
                                }
                            });

                } else {
                    ref.child("Recipes_List").push().setValue(updated_recipes, new DatabaseReference.CompletionListener() {
                        public void onComplete(DatabaseError error, @NonNull DatabaseReference ref) {
                            Toast.makeText(getApplicationContext(), "Add Recipes successfully", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(AddMenuActivity.this,MenuActivity.class);
//                            startActivity(intent);
                            finish();
                        }
                    });

                }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_IMAGE_REQUEST:
                if (resultCode == Activity.RESULT_OK
                        && data != null && data.getData() != null) {
                    fileImage = data.getData();

                    try {
                        selectedImage = getBitmap(fileImage);
//                        imageStream = getContentResolver().openInputStream(fileImage);
//                        selectedImage = BitmapFactory.decodeStream(imageStream);

//                        imageView.setImageBitmap(selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    //choose image from gallery
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private class getAndSetImage extends AsyncTask<String, Void, Void> {

        Bitmap bmp;
        URL url;
        String urlString;

        public getAndSetImage(String url) {
            urlString = url;
        }

        @Override
        protected Void doInBackground(String... voids) {
            try {
                url = new URL(urlString);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (bmp != null) {
                profile.setImageBitmap(bmp);
            }
        }
    }

    private Bitmap getBitmap(Uri uri) throws IOException {

        InputStream in = null;
        try {
            final int IMAGE_MAX_SIZE = 480000; // 1.2MP
            in = getContentResolver().openInputStream(uri);

            // Decode image size
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            in.close();


            int scale = 1;
            while ((options.outWidth * options.outHeight) * (1 / Math.pow(scale, 2)) >
                    IMAGE_MAX_SIZE) {
                scale++;
            }

            Bitmap resultBitmap = null;
            in = getContentResolver().openInputStream(uri);
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                options = new BitmapFactory.Options();
                options.inSampleSize = scale;
                resultBitmap = BitmapFactory.decodeStream(in, null, options);

                // resize to desired dimensions
                int height = resultBitmap.getHeight();
                int width = resultBitmap.getWidth();
                Log.d("", "1th scale operation dimenions - width: " + width + ", height: " + height);

                double y = Math.sqrt(IMAGE_MAX_SIZE
                        / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(resultBitmap, (int) x,
                        (int) y, true);
                resultBitmap.recycle();
                resultBitmap = scaledBitmap;

                System.gc();
            } else {
                resultBitmap = BitmapFactory.decodeStream(in);
            }
            in.close();

            Log.d("", "bitmap size - width: " + resultBitmap.getWidth() + ", height: " + resultBitmap.getHeight());
            return resultBitmap;
        } catch (IOException e) {
            Log.e("", e.getMessage(), e);
//            throw new Exception("");
//            return null;
            throw e;
        }
    }


}
