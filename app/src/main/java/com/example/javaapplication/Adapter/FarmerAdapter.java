package com.example.javaapplication.Adapter;

import static java.security.AccessController.getContext;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javaapplication.Helper.FarmerRepository;
import com.example.javaapplication.R;
import com.example.javaapplication.Datamodel.Farmer;
import com.example.javaapplication.ViewModel.FarmerListViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FarmerAdapter extends RecyclerView.Adapter<FarmerAdapter.FarmerViewHolder> {

    private List<Farmer> farmerList;
    private Context context;
    private FarmerRepository farmerRepository;
    private boolean isUpdated=false;
    private FarmerListViewModel mViewModel;

    public interface Fetchnewdata{
        void fetchnewdata(Boolean val);
    }

    private Fetchnewdata fetchnewdata;

    public FarmerAdapter(Context context, List<Farmer> farmerList,Fetchnewdata fetchnewdata) {
        this.context = context;
        this.farmerList = farmerList;
        this.fetchnewdata = fetchnewdata;
        farmerRepository=new FarmerRepository(context);

    }

    @NonNull
    @Override
    public FarmerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_row, parent, false);
        return new FarmerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FarmerViewHolder holder, int position) {

        Farmer farmer = farmerList.get(position);
        if(farmer!=null){


        // Bind data to views
        holder.nationalIdTextView.setText(farmer.getNationalID());
        holder.farmIdTextView.setText(farmer.getFarmID());
        holder.farmerNameTextView.setText(farmer.getFarmerName());

        // Action icons click listeners
        holder.editIcon.setOnClickListener(v -> {
            showEditPopup(holder.itemView, farmer);
            // Add your edit logic here
        });

        holder.deleteIcon.setOnClickListener(v -> {
            farmerRepository.DeleteFarmer(farmer.getNationalID(), new FarmerRepository.Callbackfordelete() {
                @Override
                public void onResult(Boolean farmers) {
                    fetchnewdata.fetchnewdata(true);
                }
            });
            // Add your delete logic here
        });


        holder.itemView.setOnClickListener(v -> showPopupWindow(holder.itemView, farmer));
    }
        else {
            farmerList.clear();
        }
    }

    @Override
    public int getItemCount() {
        return farmerList.size();
    }

    public static class FarmerViewHolder extends RecyclerView.ViewHolder {

        TextView nationalIdTextView, farmIdTextView, farmerNameTextView;
        ImageView editIcon, deleteIcon, viewIcon;

        public FarmerViewHolder(@NonNull View itemView) {
            super(itemView);

            nationalIdTextView = itemView.findViewById(R.id.nationalIdTextView);
            farmIdTextView = itemView.findViewById(R.id.farmIdTextView);
            farmerNameTextView = itemView.findViewById(R.id.farmerNameTextView);

            editIcon = itemView.findViewById(R.id.editIcon);
            deleteIcon = itemView.findViewById(R.id.deleteIcon);
            viewIcon = itemView.findViewById(R.id.viewIcon);
        }
    }

    private void showEditPopup(View parentView, Farmer farmer) {
        // Inflate the popup layout
        View popupView = LayoutInflater.from(context).inflate(R.layout.pop_edit_farmer, null);
        PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true);

        // Initialize components
        EditText etEditNationalID = popupView.findViewById(R.id.etEditNationalID);
        EditText etEditFarmID = popupView.findViewById(R.id.etEditFarmID);
        EditText etEditFarmerName = popupView.findViewById(R.id.etEditFarmerName);
        EditText etEditCoopName = popupView.findViewById(R.id.etEditCoopName);
        EditText etEditCoopID = popupView.findViewById(R.id.etEditCoopId);
        Button btnSelectDob = popupView.findViewById(R.id.btnSelectDob);
        RadioGroup rgGender = popupView.findViewById(R.id.rgGender);
        Button btnSaveEdit = popupView.findViewById(R.id.btnSaveEdit);
        Button btnCancelEdit = popupView.findViewById(R.id.btnCancelEdit);

        // Populate existing data
        etEditNationalID.setText(farmer.getNationalID());
        etEditFarmID.setText(farmer.getFarmID());
        etEditFarmerName.setText(farmer.getFarmerName());
        etEditCoopName.setText(farmer.getCoopName());
        etEditCoopID.setText(farmer.getCoopID());
        btnSelectDob.setText(farmer.getDob());
        if ("Male".equalsIgnoreCase(farmer.getGender())) {
            rgGender.check(R.id.rbMale);
        } else {
            rgGender.check(R.id.rbFemale);
        }

        // Date Picker for DOB
        btnSelectDob.setOnClickListener(v -> {
            // Initialize the calendar
            Calendar calendar = Calendar.getInstance();
            try {
                // Parse existing DOB if available
                String existingDob = farmer.getDob();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date date = sdf.parse(existingDob);
                calendar.setTime(date);
            } catch (Exception e) {
                // Default to current date if parsing fails
            }

            // Show DatePickerDialog
            new DatePickerDialog(context, (view, year, month, dayOfMonth) -> {
                String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                btnSelectDob.setText(selectedDate);
            },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        // Save updated data
        btnSaveEdit.setOnClickListener(v -> {
            farmer.setNationalID(etEditNationalID.getText().toString());
            farmer.setFarmID(etEditFarmID.getText().toString());
            farmer.setFarmerName(etEditFarmerName.getText().toString());
            farmer.setCoopName(etEditCoopName.getText().toString());
            farmer.setCoopID(etEditCoopID.getText().toString());
            farmer.setDob(btnSelectDob.getText().toString());
            farmer.setGender(rgGender.getCheckedRadioButtonId() == R.id.rbMale ? "Male" : "Female");

            // Update database

             farmerRepository.updateFarmer(farmer, farmer.getNationalID(), new FarmerRepository.Callbackforupdate() {
                @Override
                public void onResult(Boolean farmers) {
                   isUpdated=farmers;
                    if(farmers){
                        fetchnewdata.fetchnewdata(true);

                    }else{

                    }

                }
            });



            // Refresh RecyclerView
            notifyDataSetChanged();
            popupWindow.dismiss();
        });

        // Cancel button
        btnCancelEdit.setOnClickListener(v -> popupWindow.dismiss());

        // Show popup
        popupWindow.showAtLocation(parentView, Gravity.CENTER, 0, 0);
    }



    private void showPopupWindow(View anchorView, Farmer farmer) {
        // Inflate the popup layout
        View popupView = LayoutInflater.from(context).inflate(R.layout.popup_farmer_details, null);

        // Create the PopupWindow
        PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true);

        // Initialize the UI components
        TextView tvNationalID = popupView.findViewById(R.id.tvNationalID);
        TextView tvFarmID = popupView.findViewById(R.id.tvFarmID);
        TextView tvFarmerName = popupView.findViewById(R.id.tvFarmerName);
        TextView tvCoopName = popupView.findViewById(R.id.tvCoopName);
        TextView tvCoopID = popupView.findViewById(R.id.tvCoopID);
        TextView tvDob = popupView.findViewById(R.id.tvDob);
        TextView tvGender = popupView.findViewById(R.id.tvGender);
        ImageView ivFarmerPhoto = popupView.findViewById(R.id.ivFarmerPhoto);
        Button btnClosePopup = popupView.findViewById(R.id.btnClosePopup);

        // Populate the data
        tvNationalID.setText("National ID: " + farmer.getNationalID());
        tvFarmID.setText("Farm ID: " + farmer.getFarmID());
        tvFarmerName.setText("Farmer Name: " + farmer.getFarmerName());
        tvCoopName.setText("Cooperative Name: " + farmer.getCoopName());
        tvCoopID.setText("Cooperative ID: " + farmer.getCoopID());
        tvDob.setText("Date of Birth: " + farmer.getDob());
        tvGender.setText("Gender: " + farmer.getGender());

        // Set farmer photo if available
        if (farmer.getFarmerPhoto() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(farmer.getFarmerPhoto(), 0, farmer.getFarmerPhoto().length);
            ivFarmerPhoto.setImageBitmap(bitmap);
        } else {
//            ivFarmerPhoto.setImageResource(R.drawable.ic_placeholder);
        }

        // Set close button action
        btnClosePopup.setOnClickListener(v -> popupWindow.dismiss());


        // Show the PopupWindow in the center of the screen
        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0);

//        // Show the PopupWindow anchored to the RecyclerView item
//        popupWindow.showAsDropDown(anchorView, 0, 0);
    }
}
