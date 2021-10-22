package com.books1.books.start;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.books.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class school_bottom_sheet extends BottomSheetDialogFragment {
    EditText name;
    EditText number;
    Spinner type;
    Button send;
    school_in school_in;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.school_bottom_sheet, container, false);
        name = v.findViewById(R.id.name);
        number = v.findViewById(R.id.number);
        type = v.findViewById(R.id.type);
        ArrayAdapter<CharSequence> type_adapter = ArrayAdapter.createFromResource(getContext(), R.array.types, android.R.layout.simple_spinner_item);
        type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(type_adapter);
        send = v.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                school_in.school(number.getText().toString(), type.getSelectedItem().toString(), name.getText().toString());
                dismiss();
            }
        });

        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        school_in = (school_in) context;
    }

    public interface school_in{
        public void school(String number, String type, String name);
    }
}
