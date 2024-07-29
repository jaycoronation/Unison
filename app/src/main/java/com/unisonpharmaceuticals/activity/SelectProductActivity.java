package com.unisonpharmaceuticals.activity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.unisonpharmaceuticals.R;
import com.unisonpharmaceuticals.fragment.FragmentMakeEntry;
import com.unisonpharmaceuticals.model.ReasonResponse;
import com.unisonpharmaceuticals.model.VariationResponse;
import com.unisonpharmaceuticals.utils.AppUtils;
import com.unisonpharmaceuticals.utils.DataUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectProductActivity extends AppCompatActivity
{
    private Activity activity;
    @BindView(R.id.ivBack)
    ImageView ivBack;

    @BindView(R.id.txtHeader_Dialog_ListView)
    TextView txtHeader;

    @BindView(R.id.txtSubmitDialog)
    TextView btnSubmit;

    @BindView(R.id.edtSearch_Dialog_ListView)
    AppCompatEditText edtSearch;

    @BindView(R.id.rvProduct)
    RecyclerView rvProduct;
    
    ProductUnitAdapter productUnitAdapter;
    public  ArrayList<VariationResponse.VariationsBean> listVariation = new ArrayList<>();
    private ArrayList<VariationResponse.VariationsBean> listVariationSearch = new ArrayList<>();
    private ArrayList<ReasonResponse.ReasonsBean> listReason = new ArrayList<>();
    private ArrayList<VariationResponse.VariationsBean> listSelectedProducts = new ArrayList<>();

    String clickFor,dbs,selectedReportCode;
    int mainListPos;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_product);
        activity = this;
        ButterKnife.bind(activity);
        try {
            clickFor = getIntent().getStringExtra("clickFor");
            dbs = getIntent().getStringExtra("dbs");
            selectedReportCode = getIntent().getStringExtra("selectedReportCode");
            mainListPos = getIntent().getIntExtra("mainListPos",0);

            if (getIntent().hasExtra("listVariationData"))
            {
                Gson gson = new Gson();
                String json = getIntent().getStringExtra("listVariationData");
                Type type = new TypeToken<ArrayList<VariationResponse.VariationsBean>>()
                {
                }.getType();

                listVariation = gson.fromJson(json, type);
            }
            else
            {
                listVariation = new ArrayList<>();
            }

            if (getIntent().hasExtra("listReasonData"))
            {
                Gson gson = new Gson();
                String json = getIntent().getStringExtra("listReasonData");
                Type type = new TypeToken<ArrayList<ReasonResponse.ReasonsBean>>()
                {
                }.getType();

                listReason = gson.fromJson(json, type);
            }
            else
            {
                listReason = new ArrayList<>();
            }

            if (getIntent().hasExtra("listSelectedProductsData"))
            {
                Gson gson = new Gson();
                String json = getIntent().getStringExtra("listSelectedProductsData");
                Type type = new TypeToken<ArrayList<VariationResponse.VariationsBean>>()
                {
                }.getType();

                listSelectedProducts = gson.fromJson(json, type);
            }
            else
            {
                listSelectedProducts = new ArrayList<>();
            }

            if(listVariation.size()>0 && listSelectedProducts.size()>0)
            {
                for (int i = 0; i < listVariation.size(); i++) {

                    for (int j = 0; j < listSelectedProducts.size(); j++)
                    {
                        if(listVariation.get(i).getProduct_id().equals(listSelectedProducts.get(j).getProduct_id()))
                        {
                            listVariation.set(i,listSelectedProducts.get(j));
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        initViews();
    }


    private void initViews()
    {
        rvProduct.setLayoutManager(new LinearLayoutManager(activity));
        rvProduct.setHasFixedSize(true);
        txtHeader.setText("Select Product");
        productUnitAdapter = new ProductUnitAdapter(listVariation, listReason, "Product", mainListPos, false, rvProduct);
        rvProduct.setAdapter(productUnitAdapter);
        btnSubmit.setVisibility(View.VISIBLE);
        edtSearch.setVisibility(View.VISIBLE);


        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                int textlength = edtSearch.getText().length();
                listVariationSearch.clear();
                for (int i = 0; i < listVariation.size(); i++) {
                    if (listVariation.get(i).getName().toLowerCase().contains(edtSearch.getText().toString().toLowerCase().trim()) ||
                            listVariation.get(i).getItem_id_code().toLowerCase().contains(edtSearch.getText().toString().toLowerCase().trim()) ||
                            listVariation.get(i).getItem_code().toLowerCase().contains(edtSearch.getText().toString().toLowerCase().trim())) {
                        listVariationSearch.add(listVariation.get(i));
                    }
                }
                productUnitAdapter = new ProductUnitAdapter(listVariation, listReason, "Product", mainListPos, true, rvProduct);
                rvProduct.setAdapter(productUnitAdapter);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               callBack();
            }
        });

    }

    private void callBack()
    {
        if (productUnitAdapter.listSelected().size() > 0) {
            if (productUnitAdapter.isAllSelected()) {
                AppUtils.hideKeyboard(edtSearch, activity);
                if (listSelectedProducts == null) {
                    listSelectedProducts = new ArrayList<>();
                }

                listSelectedProducts.clear();

                for (int i = 0; i < productUnitAdapter.listSelected().size(); i++) {
                    listSelectedProducts.add(productUnitAdapter.listSelected().get(i));
                }

                callHandler(productUnitAdapter.isAllSelected() ? 1 : 0,listSelectedProducts);
            } else {
                AppUtils.showToast(activity, "Please enter unit for all samples.");
            }
        } else {
            AppUtils.showToast(activity, "Please Select at least One sample.");
        }
    }

    private void callHandler(int isAllSelected,ArrayList<VariationResponse.VariationsBean> listSelected)
    {
        AppUtils.hideKeyboard(rvProduct,activity);
        if(FragmentMakeEntry.productUnit!=null)
        {
            Message msg = Message.obtain();
            msg.what = 666;
            msg.obj = listSelected;
            msg.arg1 = isAllSelected;
            FragmentMakeEntry.productUnit.sendMessage(msg);
        }

        activity.finish();
    }

    public class ProductUnitAdapter extends RecyclerView.Adapter<ProductUnitAdapter.ViewHolder>
    {
        private ArrayList<VariationResponse.VariationsBean> listProducAdapter;
        private ArrayList<ReasonResponse.ReasonsBean> listReasonAdapter;
        private String isFor = "";
        private int mainListPos = 0;
        private boolean isForSearch = false;
        private RecyclerView bottomSheetListView;
        private boolean isTextChange = false;

        ProductUnitAdapter(ArrayList<VariationResponse.VariationsBean> productList,
                              ArrayList<ReasonResponse.ReasonsBean> reasonList,
                              String isFor,
                              int mainListPos,
                              boolean isForSearch,
                              RecyclerView bottomSheetListView)
        {
            this.isFor = isFor;
            this.mainListPos = mainListPos;
            this.isForSearch = isForSearch;
            this.bottomSheetListView = bottomSheetListView;

            if (selectedReportCode.equalsIgnoreCase("STK")) {
                this.listProducAdapter = productList;
                if (isFor.equalsIgnoreCase("Product")) {
                    for (int i = 0; i < listProducAdapter.size(); i++) {
                        listProducAdapter.get(i).setChecked(true);
                    }
                }
            } else if (selectedReportCode.equalsIgnoreCase("TNS") || selectedReportCode.equalsIgnoreCase("SRD")) {
                this.listProducAdapter = new ArrayList<>();
                for (int i = 0; i < productList.size(); i++) {
                    if (productList.get(i).getProduct_type().equalsIgnoreCase("1")) {
                        this.listProducAdapter.add(productList.get(i));
                    }
                }
            } else {
                this.listProducAdapter = productList;
            }

            //For disaply only G and F if product reason is Gift
            if (isFor.equalsIgnoreCase("Reason")) {
                if (listSelectedProducts.get(mainListPos).getProduct_type().equalsIgnoreCase("1")) {
                    listReasonAdapter = DataUtils.getReasonsFroGift();
                } else {
                    listReasonAdapter = DataUtils.getReasonsFroNonGift();
                }
            }
        }

        @Override public ProductUnitAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowview_product_unit, parent, false);
            ViewHolder vh = new ViewHolder(v, new MyCustomEditTextListener());
            return vh;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            try {
                holder.holderPos = holder.getAbsoluteAdapterPosition();
                final VariationResponse.VariationsBean getSet;
                if (isForSearch) {
                    getSet = listVariationSearch.get(holder.holderPos);
                } else {
                    getSet = listProducAdapter.get(holder.holderPos);
                }

                holder.cbProduct.setChecked(getSet.isChecked());
                holder.cbProduct.setVisibility(View.VISIBLE);
                holder.txtProductCode.setText(getSet.getItem_id_code());
                holder.txtProduct.setText(getSet.getName());
                holder.edtUnit.setTag(holder.holderPos);
                holder.myCustomEditTextListener.updatePosition(holder.holderPos);

                /*  holder.edtUnit.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                                AppUtils.showToast(activity,"NEXT CLICK");
                                rvProduct.getLayoutManager().scrollToPosition(20);
                                holder.edtUnit.requestFocus();
                                AppUtils.showKeyboardNew(holder.edtUnit, activity);
                                return true;
                            }
                            return false;
                        }

                    });*/


                if (getSet.isChecked())
                {
                    holder.edtUnit.setVisibility(View.VISIBLE);
                    if (getSet.getStock().equals("") || getSet.getStock().equals("0")) {
                        if (getSet.getReason_code().equalsIgnoreCase("F")) {
                            holder.edtUnit.setText("0");
                            holder.edtUnit.setSelection(holder.edtUnit.getText().length());
                        } else {
                            holder.edtUnit.setText("");
                        }
                    } else {
                        holder.edtUnit.setText(String.valueOf(getSet.getStock()));
                        holder.edtUnit.setSelection(holder.edtUnit.getText().length());
                    }
                }
                else
                {
                    holder.edtUnit.setText("");
                    holder.edtUnit.setVisibility(View.INVISIBLE);
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //AppUtils.hideKeyboard(holder.edtUnit, activity);
                        if (dbs.equalsIgnoreCase("STK")) {
                            holder.cbProduct.setChecked(true);
                            getSet.setChecked(true);
                            holder.edtUnit.requestFocus();
                            AppUtils.showKeyboardNew(holder.edtUnit, activity);
                            return;
                        } else {
                            getSet.setChecked(!getSet.isChecked());
                            if (getSet.isChecked()) {
                                holder.edtUnit.setVisibility(View.VISIBLE);
                                holder.edtUnit.requestFocus();
                                AppUtils.showKeyboardNew(holder.edtUnit, activity);
                            } else {
                                //   AppUtils.hideKeyboard(holder.edtUnit, activity);
                                getSet.setStock("");
                                holder.edtUnit.setVisibility(View.INVISIBLE);
                            }
                            if (isForSearch) {
                                listVariationSearch.set(holder.holderPos, getSet);
                            } else {
                                listProducAdapter.set(holder.holderPos, getSet);
                            }
                            notifyItemChanged(holder.holderPos);
                        }
                    }
                });

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override public int getItemCount()
        {
            if (isForSearch) {
                return listVariationSearch.size();
            } else {
                return listProducAdapter.size();
            }
        }

        public ArrayList<VariationResponse.VariationsBean> listSelected() {
            ArrayList<VariationResponse.VariationsBean> listReturn = new ArrayList<VariationResponse.VariationsBean>();

            for (int i = 0; i < listProducAdapter.size(); i++) {
                if (listProducAdapter.get(i).isChecked()) {
                    listReturn.add(listProducAdapter.get(i));
                }
            }

            return listReturn;
        }

        public boolean isAllSelected() {
            boolean isSelected = true;
            if (dbs.equalsIgnoreCase("STK")) {
                isSelected = true;
                return true;
            } else {
                for (int i = 0; i < listProducAdapter.size(); i++) {
                    if (listProducAdapter.get(i).isChecked()) {
                        if (listProducAdapter.get(i).getReason_code().equalsIgnoreCase("F")) {
                            isSelected = true;
                        } else {
                            //For allow zero
                            if (listProducAdapter.get(i).getStock().equals("") /*||
                                listProducAdapter.get(i).getStock().equals("0")*/) {
                                isSelected = false;
                                break;
                            }
                        }
                    } else {
                        isSelected = true;
                    }
                }

            }

            return isSelected;
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView txtProduct, txtProductCode;
            AppCompatEditText edtUnit;
            CheckBox cbProduct;
            private int holderPos = -1;
            public MyCustomEditTextListener myCustomEditTextListener;
            
            public ViewHolder(View itemView , MyCustomEditTextListener myCustomEditTextListener)
            {
                super(itemView);
                txtProduct = (TextView) itemView.findViewById(R.id.txtProduct);
                txtProductCode = (TextView) itemView.findViewById(R.id.txtProductCode);
                cbProduct = (CheckBox) itemView.findViewById(R.id.cb);
                edtUnit = (AppCompatEditText) itemView.findViewById(R.id.edtUnit);
                this.myCustomEditTextListener = myCustomEditTextListener;
                edtUnit.addTextChangedListener(myCustomEditTextListener);
            }
        }

        private class MyCustomEditTextListener implements TextWatcher {
            private int position;

            public void updatePosition(int position) {
                this.position = position;
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                // no op
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i2, int i3) {
                try {
                    if (isForSearch) {
                        if (!s.toString().equals("")) {
                            if (listVariationSearch.get(position).isChecked()) {
                                listVariationSearch.get(position).setStock(s.toString());
                            } else {
                                listVariationSearch.get(position).setStock("");
                            }
                        }
                    }
                    else {
                        if (!s.toString().trim().equals("")) {
                            listProducAdapter.get(position).setStock(s.toString().trim());
                            //notifyItemChanged(position);
                                /*if (Integer.parseInt(s.toString()) == 0) {
                                    holder.variationsBean.setReason("Refuse Sample");
                                    holder.variationsBean.setReason_code("F");
                                    holder.variationsBean.setReason_id(ApiClient.REFUSE_REASON_ID);
                                } else {
                                    if (holder.variationsBean.getProduct_type().equalsIgnoreCase("0")) {
                                        if (!holder.variationsBean.getReason_code().equalsIgnoreCase("F")) {
                                            holder.variationsBean.setReason(holder.variationsBean.getReason());
                                            holder.variationsBean.setReason_code(holder.variationsBean.getReason_code());
                                        } else {
                                            holder.variationsBean.setReason("Regular Sample");
                                            holder.variationsBean.setReason_code("R");
                                        }
                                    } else {
                                        holder.variationsBean.setReason("Gift Article");
                                        holder.variationsBean.setReason_code("G");
                                    }
                                }

                                listProducAdapter.set(holder.getAdapterPosition(), holder.variationsBean);
                                */
                        }
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        callBack();
        super.onBackPressed();
    }

}
