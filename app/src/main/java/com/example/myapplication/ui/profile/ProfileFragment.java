package com.example.myapplication.ui.profile;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfileFragment extends Fragment {

    private static final int PICK_FILE_REQUEST = 1;
    private Uri fileUri;
    private AppCompatImageView img_profile;
    private TextView tv_phone;
    private TextView tv_email;
    private TextView tv_name;
    private FragmentProfileBinding binding;
    private String userID;
    private DatabaseReference databaseReference;
    private Button btnShowPassport;
    private Button btnUploadPassport;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        findViews(root);
        initViews(root);

        setUpPassportButtons();

        return root;
    }

    private void setUpPassportButtons() {
        btnUploadPassport.setOnClickListener(v -> {
            // Open file chooser to upload a file
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(Intent.createChooser(intent, "Select a file"), PICK_FILE_REQUEST);
        });

        btnShowPassport.setOnClickListener(v -> {
            // Fetch URL from Firebase and open the file
            databaseReference.child(userID).child("Passport").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String passportUrl = snapshot.getValue(String.class);
                    if (passportUrl != null && !passportUrl.isEmpty()) {
                        Uri uri = Uri.parse(passportUrl);
                        Log.d("ProfileFragment", "Passport URL retrieved: " + passportUrl);
                        checkFileExists(uri);
                    } else {
                        Log.d("ProfileFragment", "No Passport URL found.");
                        showFileNotFound();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("ProfileFragment", "Failed to load passport file", error.toException());
                    Toast.makeText(getContext(), "Failed to load passport file", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void checkFileExists(Uri uri) {
        // Create a StorageReference to check if the file exists
        StorageReference fileRef = FirebaseStorage.getInstance().getReferenceFromUrl(uri.toString());
        fileRef.getMetadata().addOnSuccessListener(metadata -> {
            // If metadata is retrieved, the file exists
            openFile(uri);
        }).addOnFailureListener(exception -> {
            // If metadata retrieval fails, the file does not exist
            showFileNotFound();
        });
    }

    private void showFileNotFound() {
        Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(200);
        }
        Toast.makeText(getContext(), "No file uploaded", Toast.LENGTH_SHORT).show();
    }

    private void findViews(View root) {
        tv_phone = root.findViewById(R.id.tv_phone);
        tv_email = root.findViewById(R.id.tv_email);
        tv_name = root.findViewById(R.id.tv_name);
        img_profile = root.findViewById(R.id.img_profile);
        btnUploadPassport = root.findViewById(R.id.btn_upload_passport);
        btnShowPassport = root.findViewById(R.id.btn_show_passport);
    }

    private void initViews(View root) {
        // Get user ID from Firebase authentication
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Glide.with(this)
                .load(user.getPhotoUrl())
                .centerCrop()
                .placeholder(R.drawable.profile)
                .into(img_profile);

        if (user != null) {
            userID = user.getUid();
        }

        if (user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty()) {
            tv_phone.setText("Phone: " + user.getPhoneNumber());
        } else {
            tv_phone.setVisibility(View.INVISIBLE);
        }

        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            tv_email.setText("Email: " + user.getEmail());
        } else {
            tv_email.setVisibility(View.INVISIBLE);
        }

        if (user.getDisplayName() != null && !user.getDisplayName().isEmpty()) {
            tv_name.setText("Hello, " + user.getDisplayName());
        } else {
            tv_name.setText("Hello, Traveler");
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContext().getContentResolver();
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private String getMimeType(Uri uri) {
        String mimeType = null;
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            ContentResolver contentResolver = getContext().getContentResolver();
            mimeType = contentResolver.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
        }
        return mimeType;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                fileUri = data.getData();
                uploadFileToFirebase(fileUri);
            }
        }
    }

    private void uploadFileToFirebase(Uri fileUri) {
        if (fileUri != null) {
            // Create a StorageReference to the user's folder
            StorageReference userStorageRef = FirebaseStorage.getInstance().getReference("passports/" + userID);

            // Create a reference to the file
            StorageReference fileRef = userStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(fileUri));

            fileRef.putFile(fileUri).addOnSuccessListener(taskSnapshot ->
                    fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Save download URL to Firebase Database
                        String downloadUrl = uri.toString();
                        databaseReference.child(userID).child("Passport").setValue(downloadUrl)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(), "Passport uploaded successfully", Toast.LENGTH_SHORT).show();
                                        Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                                        if (vibrator != null) {
                                            vibrator.vibrate(200);
                                        }
                                    } else {
                                        Toast.makeText(getContext(), "Failed to upload passport", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    })
            ).addOnFailureListener(e -> {
                Toast.makeText(getContext(), "Failed to upload passport", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void openFile(Uri uri) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, getMimeType(uri));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), "No application found to open this file", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("ProfileFragment", "Error opening file: " + e.getMessage(), e);
            Toast.makeText(getContext(), "Failed to open file", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
