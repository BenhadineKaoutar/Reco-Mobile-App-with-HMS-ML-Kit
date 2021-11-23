package com.cocoben.reco.ui.scan;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.cocoben.reco.R;
import com.cocoben.reco.databinding.FragmentScanBinding;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.mlsdk.MLAnalyzerFactory;
import com.huawei.hms.mlsdk.common.MLFrame;
import com.huawei.hms.mlsdk.text.MLLocalTextSetting;
import com.huawei.hms.mlsdk.text.MLText;
import com.huawei.hms.mlsdk.text.MLTextAnalyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ScanFragment extends Fragment {

    private static final int RESULT_OK = -1;
    private static final int GET_IMAGE_REQUEST_CODE = 22;
    ImageView ivBitmap;
    Button buttonAddImage;
    ListView listOfIngrediants;
    HashMap<String, IngrediantSample> inventory = new HashMap<String, IngrediantSample>();
    private ScanViewModel scanViewModel;
    private FragmentScanBinding binding;
    private MLTextAnalyzer mTextAnalyzer;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentScanBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ivBitmap = binding.ivBitmap;
        listOfIngrediants = binding.listIngredients;
        buttonAddImage = binding.buttonAddImage;

        buttonAddImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getImage();
            }
        });

        createMLTextAnalyzer();
        Toast.makeText(getActivity(), "Just wait me to prepare your device, you don't have to use the internet to scan the product. Please scan JUST THE INGREDIENT LIST To avoid getting some weird stuf", Toast.LENGTH_LONG).show();
        readIngredientsInventory();

        return root;
    }

    private void readIngredientsInventory() {

        InputStream is = getResources().openRawResource(R.raw.ingredients_inventory);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8)
        );
        String line = "";
        try {
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split("\",\"");
                IngrediantSample sample = new IngrediantSample();
                sample.setRef(tokens[0]);
                sample.setINCIName(tokens[1]);
                sample.setDescription(tokens[2]);
                sample.setFunction(tokens[3]);
                inventory.put(tokens[1], sample);
            }
        } catch (IOException e) {
            Log.wtf("MyActivity", "Error reading data file on line " + line, e);
        }

    }

    private void getImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, GET_IMAGE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap selectedBitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), imageUri);
                ivBitmap.setImageBitmap(selectedBitmap);
                asyncAnalyzeText(selectedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createMLTextAnalyzer() {
        MLLocalTextSetting setting = new MLLocalTextSetting.Factory()
                .setOCRMode(MLLocalTextSetting.OCR_DETECT_MODE)
                .setLanguage("en")
                .create();

        mTextAnalyzer = MLAnalyzerFactory.getInstance().getLocalTextAnalyzer(setting);
    }

    private void asyncAnalyzeText(Bitmap bitmap) {

        if (mTextAnalyzer == null) {
            createMLTextAnalyzer();
        }

        MLFrame frame = MLFrame.fromBitmap(bitmap);

        Task<MLText> task = mTextAnalyzer.asyncAnalyseFrame(frame);
        task.addOnSuccessListener(new OnSuccessListener<MLText>() {
            @Override
            public void onSuccess(MLText text) {
                String str = text.getStringValue();
                str = str.replace("\n", ",");
                str = str.replace(":", ",");
                str = str.replaceAll(",,", ",");

                List ingredients = new ArrayList<String>(Arrays.asList(str.split(",")));
                List<ScanItem> items = new ArrayList<>();
                for (Object s : ingredients) {
                    String hey = s.toString().toUpperCase().trim();
                    if (inventory.containsKey(hey)) {
                        items.add(new ScanItem(s.toString(), "", inventory.get(hey).Function.substring(0, inventory.get(hey).Function.length() - 1)));
                    }
                }

                ScanItemViewAdapter adapter = new ScanItemViewAdapter(getContext(), items);
                listOfIngrediants.setAdapter(adapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        try {
            if (mTextAnalyzer != null)
                mTextAnalyzer.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}