package io.github.xueluwu.android.organizeyourcloset.Closet;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.github.xueluwu.android.organizeyourcloset.Adapter.GridViewAdapter;
import io.github.xueluwu.android.organizeyourcloset.DatabaseHandler;
import io.github.xueluwu.android.organizeyourcloset.Item;
import io.github.xueluwu.android.organizeyourcloset.R;

import static android.app.Activity.RESULT_OK;
import static io.github.xueluwu.android.organizeyourcloset.Closet.ClosetFilterActivity.FILTER_FOR_CLOSET_OR_CALENDAR;
import static io.github.xueluwu.android.organizeyourcloset.Closet.ClosetFilterActivity.INTENT_SELECTED_BRANDS;
import static io.github.xueluwu.android.organizeyourcloset.Closet.ClosetFilterActivity.INTENT_SELECTED_CATEGORIES;
import static io.github.xueluwu.android.organizeyourcloset.Closet.ClosetFilterActivity.INTENT_SELECTED_KINDS;
import static io.github.xueluwu.android.organizeyourcloset.Closet.ClosetFilterActivity.INTENT_SELECTED_OWNERS;
import static io.github.xueluwu.android.organizeyourcloset.Closet.ClosetFilterActivity.INTENT_SELECTED_SEASONS;
import static io.github.xueluwu.android.organizeyourcloset.Closet.ClosetFilterActivity.INTENT_SELECTED_SIZES;

//public class ClosetFragment extends Fragment implements AdapterView.OnItemSelectedListener {
public class ClosetFragment extends Fragment {
    private static final String DEBUG_TAG = "ClosetFragment";
    private DatabaseHandler db;
    private ArrayList<String> selectedKinds, selectedCategories, selectedSeasons;
    private ArrayList<String> selectedSizes, selectedBrands, selectedOwners;
    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private Bitmap bp;
    private String mCurrentPhotoPath;


    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState
    ) {
        getActivity().setTitle(io.github.xueluwu.android.organizeyourcloset.R.string.closet);
        setHasOptionsMenu(true);

        return inflater.inflate(R.layout.fragment_closet, container, false);
    }

    //ActionBar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.ac_add:
                showAddPhotoDialog();
                break;
            case R.id.ac_filter:
                Intent intent = new Intent(getActivity(), ClosetFilterActivity.class);
                intent.putExtra(FILTER_FOR_CLOSET_OR_CALENDAR, "closet");
                intent.putStringArrayListExtra(INTENT_SELECTED_KINDS, selectedKinds);
                intent.putStringArrayListExtra(INTENT_SELECTED_CATEGORIES, selectedCategories);
                intent.putStringArrayListExtra(INTENT_SELECTED_SEASONS, selectedSeasons);
                intent.putStringArrayListExtra(INTENT_SELECTED_SIZES, selectedSizes);
                intent.putStringArrayListExtra(INTENT_SELECTED_BRANDS, selectedBrands);
                intent.putStringArrayListExtra(INTENT_SELECTED_OWNERS, selectedOwners);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(io.github.xueluwu.android.organizeyourcloset.R.menu.closet_action_bar,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        selectedKinds = intent.getStringArrayListExtra(INTENT_SELECTED_KINDS);
        if (selectedKinds == null) {
            selectedKinds = new ArrayList<>();
        }
        selectedCategories = intent.getStringArrayListExtra(INTENT_SELECTED_CATEGORIES);
        if (selectedCategories == null) {
            selectedCategories = new ArrayList<>();
        }
        selectedSeasons = intent.getStringArrayListExtra(INTENT_SELECTED_SEASONS);
        if (selectedSeasons == null) {
            selectedSeasons = new ArrayList<>();
        }
        selectedSizes = intent.getStringArrayListExtra(INTENT_SELECTED_SIZES);
        if (selectedSizes == null) {
            selectedSizes = new ArrayList<>();
        }
        selectedBrands = intent.getStringArrayListExtra(INTENT_SELECTED_BRANDS);
        if (selectedBrands == null) {
            selectedBrands = new ArrayList<>();
        }
        selectedOwners = intent.getStringArrayListExtra(INTENT_SELECTED_OWNERS);
        if (selectedOwners == null) {
            selectedOwners = new ArrayList<>();
        }

        db = new DatabaseHandler(getActivity());
    }

    @Override
    public void onStart() {
        super.onStart();

        showRecords(
                selectedKinds,
                selectedCategories,
                selectedSeasons,
                selectedSizes,
                selectedBrands,
                selectedOwners
        );

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }

    private void showAddPhotoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(R.array.add_photo_array, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case (0):
                        callCamera();
                        break;
                    case (1):
                        callGallery();
                        break;
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void callGallery(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 2);
    }

    private void callCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.d(DEBUG_TAG, "IOException");
            }
            // continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(
                        getActivity(),
                        "io.github.xueluwu.android.organizeyourcloset.fileprovider",
                        photoFile
                );
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, 1);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpeg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bitmap yourImage = decodeFile(mCurrentPhotoPath, 400);
            // Photos rotate in some devices, rotate back
            try {
                ExifInterface ei = new ExifInterface(mCurrentPhotoPath);
                int orientation = ei.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED
                );
                //                    Bitmap rotateBitmap = null;
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        yourImage = rotateImage(yourImage, 90);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        yourImage = rotateImage(yourImage, 180);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        yourImage = rotateImage(yourImage, 270);
                        break;
                    case ExifInterface.ORIENTATION_NORMAL:
                    default:
                        yourImage = yourImage;
                }
            } catch (IOException e) {
                Log.w("TAG", "-- Error in setting image");
            } catch (OutOfMemoryError oom) {
                Log.w("TAG", "-- OOM Error in setting image");
            }

            // convert bitmap to byte
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            yourImage.compress(Bitmap.CompressFormat.JPEG, 20, stream);
            byte byteArray1[] = stream.toByteArray();

            Intent intent = new Intent(getActivity(), ClosetItemActivity.class);
            intent.putExtra("image", byteArray1);
            intent.putExtra("add_show_edit", "add");
            startActivity(intent);
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            Uri choosenImage = data.getData();
            if(choosenImage !=null){
                bp=decodeUri(choosenImage, 400);
                //image = profileImage(bp);   // image in byte[], passed to AddItemActivity
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bp.compress(Bitmap.CompressFormat.JPEG, 20, stream);
                byte[] byteArray2 = stream.toByteArray();

                Intent intent = new Intent(getActivity(), ClosetItemActivity.class);
                intent.putExtra("image", byteArray2);
                intent.putExtra("add_show_edit", "add");
                startActivity(intent);
            }
        }
    }

    // Rotate the bitmap
    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(
                source, 0, 0, source.getWidth(), source.getHeight(), matrix, true
        );
    }

    //Convert and resize our image to 400dp for faster uploading our images to DB
    private Bitmap decodeUri(Uri selectedImage, int REQUIRED_SIZE) {
        try {
            // Decode image size
            BitmapFactory.Options option = new BitmapFactory.Options();
            option.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(
                    getActivity().getContentResolver().openInputStream(selectedImage),
                    null,
                    option);

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = option.outWidth, height_tmp = option.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(
                    getActivity().getApplicationContext().getContentResolver().openInputStream(selectedImage),
                    null,
                    o2
            );
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    // convert photo path to Bitmap
    private Bitmap decodeFile(String path, int REQUIRED_SIZE) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o);

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeFile(path, o2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    //Convert bitmap to bytes
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    private byte[] profileImage(Bitmap b){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 0, bos);
        return bos.toByteArray();
    }

    //Retrieve data from the database and set to the grid view
    private void showRecords(
            ArrayList<String> selectedKinds,
            ArrayList<String> selectedCategories,
            ArrayList<String> selectedSeasons,
            ArrayList<String> selectedSizes,
            ArrayList<String> selectedBrands,
            ArrayList<String> selectedOwners
    ){
        ArrayList<Item> items = db.getAllItems(
                selectedKinds,
                selectedCategories,
                selectedSeasons,
                selectedSizes,
                selectedBrands,
                selectedOwners
        );

        gridView = (GridView) getActivity().findViewById(R.id.gridview);
        gridAdapter = new GridViewAdapter(getActivity(), R.layout.grid_item_layout, items);
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Item item = (Item) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), ClosetItemActivity.class);
                intent.putExtra("item_id", item.getID());
                intent.putExtra("add_show_edit", "show");
                startActivity(intent);
            }
        });
    }
}