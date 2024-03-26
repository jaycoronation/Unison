package com.unisonpharmaceuticals.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.unisonpharmaceuticals.R;
import com.unisonpharmaceuticals.classes.SessionManager;
import com.unisonpharmaceuticals.databse.UnisonDatabaseHelper;
import com.unisonpharmaceuticals.model.NewEntryGetSet;
import com.unisonpharmaceuticals.model.VariationResponse;
import com.unisonpharmaceuticals.model.ViewEntryChildGetSet;
import com.unisonpharmaceuticals.model.for_sugar.DBVariation;
import com.unisonpharmaceuticals.utils.AppUtils;
import com.unisonpharmaceuticals.utils.DataUtils;
import com.unisonpharmaceuticals.utils.MitsAutoHeightListView;
import com.unisonpharmaceuticals.views.BottomSheetListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PendingEntryDialogAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater = null;
    ArrayList<NewEntryGetSet> listPending;
    UnisonDatabaseHelper udbh;
    SQLiteDatabase sqlDB;
    PendingEntryChildAdapter adapter;
    LinearLayout llNoData;
    ListView listview;
    String dbs = "";

    private ArrayList<String> listRestriction = new ArrayList<>();
    private int countNew = 0;
    private int countEnhance = 0;
    private String focusForString = "";
    private String doctorId = "";
    private boolean isUpdateCalled = false;
    private String dateCurrent = "";
    ArrayList<String> listProduct = new ArrayList<String>();

    private SessionManager sessionManager;

    @SuppressWarnings("unused")
    private String product1 = "", product2 = "";

    public  static Handler handlerPendingAdapter;

    public PendingEntryDialogAdapter(Activity a, ArrayList<NewEntryGetSet> item, LinearLayout llNoData, ListView listview) {
        this.activity = a;
        this.listPending = item;
        this.llNoData = llNoData;
        this.listview = listview;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        sessionManager = new SessionManager(a);

        handlerPendingAdapter = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg)
            {
                if(msg.what==101)
                {
                    try {
                        listPending.get(msg.arg1).setProducts(String.valueOf(msg.obj));
                        notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if(msg.what==102)
                {
                    View rowView = inflater.inflate(R.layout.rowview_pending_entry, null);
                    ImageView ivDone = (ImageView) rowView.findViewById(R.id.ivDone);
                    ivDone.performClick();
                }
                return false;
            }
        });
    }

    public int getCount() {
        return listPending.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        View rowView = convertView;
        if (rowView == null) {
            holder = new ViewHolder();
            rowView = inflater.inflate(R.layout.rowview_pending_entry, null);

            holder.txtDBS = (TextView) rowView.findViewById(R.id.txtDBC);
            holder.edtWorkwith = (EditText) rowView.findViewById(R.id.edtWorkWith);
            holder.txtDoctor = (TextView) rowView.findViewById(R.id.txtDoctor);
            holder.ivMinus = (ImageView) rowView.findViewById(R.id.ivMinus);
            holder.ivEdit = (ImageView) rowView.findViewById(R.id.ivEdit);
            holder.ivDone = (ImageView) rowView.findViewById(R.id.ivDone);
            holder.listChild = (MitsAutoHeightListView) rowView.findViewById(R.id.lv_pending_child);
            holder.llFocusFor = (LinearLayout) rowView.findViewById(R.id.llFocusFor);
            holder.llAddFocus = (LinearLayout) rowView.findViewById(R.id.llAddFocus);//For Add focus products
            holder.llAdd = (LinearLayout) rowView.findViewById(R.id.llAdd);//For Add MAin products

            holder.edttext1 = (TextView) rowView.findViewById(R.id.edtProduct1);
            holder.edttext2 = (TextView) rowView.findViewById(R.id.edtProduct2);
            holder.edtReason1 = (TextView) rowView.findViewById(R.id.edtReason1);
            holder.edtReason2 = (TextView) rowView.findViewById(R.id.edtReason2);

            holder.llFocus1 = (LinearLayout) rowView.findViewById(R.id.llFocus1);
            holder.llFocus2 = (LinearLayout) rowView.findViewById(R.id.llFocus2);

            holder.img_focus_delete_1 = (ImageView) rowView.findViewById(R.id.img_focus_delete_1);
            holder.img_focus_delete_2 = (ImageView) rowView.findViewById(R.id.img_focus_delete_2);

            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        holder.ref = position;

        final NewEntryGetSet getSet = listPending.get(position);

        Log.e("<><><><><><>", "getView: "+getSet.isWorkWithSelf() + " ?????? "+getSet.getWork_with());

        dbs = listPending.get(holder.ref).getReport_type();

        holder.txtDBS.setText(dbs);
        if (getSet.getDr().equals(""))
        {
            holder.txtDoctor.setVisibility(View.GONE);
        }
        else
        {
            holder.txtDoctor.setVisibility(View.VISIBLE);
            String text = "<font color=#000000>"+getSet.getDr()+" ("+getSet.getSpeciality()+") : </font>"+"<font color=#019ce4>"+getSet.getDr_id()+" </font>";
            try
            {
                holder.txtDoctor.setText(Html.fromHtml(text));
            }
            catch (Exception e)
            {
                holder.txtDoctor.setText(getSet.getDr() + "("+getSet.getSpeciality()+") "+getSet.getDr_id());
                e.printStackTrace();
            }
        }

        //For Disply diferent data
        if (listPending.get(holder.ref).getReport_type().equals("DCR") ||
                listPending.get(holder.ref).getReport_type().equals("LCR") ||
                listPending.get(holder.ref).getReport_type().equals("ACR") ||
                listPending.get(holder.ref).getReport_type().equals("JCR") ||
                listPending.get(holder.ref).getReport_type().equals("ZCR"))
        {
            if (listPending.get(holder.ref).getNew_cycle().equals("1"))
            {
                holder.txtDBS.setText(listPending.get(holder.ref).getReport_type() + " : N");
            }
            else
            {
                holder.txtDBS.setText(listPending.get(holder.ref).getReport_type());
            }

            holder.edtWorkwith.setVisibility(View.VISIBLE);
            if (getSet.isWorkWithSelf())
            {
                holder.edtWorkwith.setText("Self");
            } else {
                holder.edtWorkwith.setText(getSet.getWork_with());
            }

            holder.txtDoctor.setVisibility(View.VISIBLE);
            holder.txtDBS.setVisibility(View.VISIBLE);
        }
        else if (listPending.get(holder.ref).getReport_type().equals("NCR"))
        {
            if (listPending.get(holder.ref).getNew_cycle().equals("1")) {
                holder.txtDBS.setText(listPending.get(holder.ref).getReport_type() + " : N");
            } else {
                holder.txtDBS.setText(listPending.get(holder.ref).getReport_type());
            }

            holder.edtWorkwith.setVisibility(View.VISIBLE);
            if (getSet.isWorkWithSelf()) {
                holder.edtWorkwith.setText("Self");
            } else {
                holder.edtWorkwith.setText(getSet.getWork_with());
            }
            holder.txtDoctor.setVisibility(View.VISIBLE);

            String text = "<font color=#000000>"+getSet.getDr()+" </font>";
            try
            {
                holder.txtDoctor.setText(Html.fromHtml(text));
            }
            catch (Exception e)
            {
                holder.txtDoctor.setText(getSet.getDr() + "("+getSet.getSpeciality()+") "+getSet.getDr_id());
                e.printStackTrace();
            }


            holder.txtDBS.setVisibility(View.VISIBLE);

        }
        else if (listPending.get(holder.ref).getReport_type().equals("ROR") ||
                listPending.get(holder.ref).getReport_type().equals("ROA"))
        {
            if (listPending.get(holder.ref).getNew_cycle().equals("1"))
            {
                holder.txtDBS.setText(listPending.get(holder.ref).getReport_type() + " : N");
            } else {
                holder.txtDBS.setText(listPending.get(holder.ref).getReport_type());
            }

            holder.edtWorkwith.setVisibility(View.VISIBLE);
            if (getSet.isWorkWithSelf()) {
                holder.edtWorkwith.setText("Self");
            } else {
                holder.edtWorkwith.setText(getSet.getWork_with());
            }

            holder.txtDoctor.setVisibility(View.GONE);
            holder.txtDBS.setVisibility(View.VISIBLE);

        }
        else if (listPending.get(holder.ref).getReport_type().equals("TNS") ||
                listPending.get(holder.ref).getReport_type().equals("SRD"))
        {
            if (listPending.get(holder.ref).getNew_cycle().equals("1")) {
                holder.txtDBS.setText(listPending.get(holder.ref).getReport_type() + " : N");
            } else {
                holder.txtDBS.setText(listPending.get(holder.ref).getReport_type());
            }
            holder.edtWorkwith.setVisibility(View.VISIBLE);
            if (getSet.isWorkWithSelf()) {
                holder.edtWorkwith.setText("Self");
            } else {
                holder.edtWorkwith.setText(getSet.getWork_with());
            }

            holder.txtDoctor.setVisibility(View.VISIBLE);
            holder.txtDBS.setVisibility(View.VISIBLE);
        }
        else if (listPending.get(holder.ref).getReport_type().equals("INT"))
        {
            holder.txtDBS.setText(listPending.get(holder.ref).getReport_type());
            holder.edtWorkwith.setText("No Of Internee :" + listPending.get(holder.ref).getInternee());
            holder.txtDoctor.setVisibility(View.GONE);

            holder.txtDBS.setVisibility(View.VISIBLE);
            holder.edtWorkwith.setVisibility(View.VISIBLE);
        }
        else if (listPending.get(holder.ref).getReport_type().equals("DBS"))
        {
            holder.txtDBS.setText(listPending.get(holder.ref).getReport_type());
            holder.edtWorkwith.setText("Amount : " + listPending.get(holder.ref).getInternee());

            holder.txtDBS.setVisibility(View.VISIBLE);
            holder.edtWorkwith.setVisibility(View.VISIBLE);
            holder.txtDoctor.setVisibility(View.VISIBLE);
        }
        else if (listPending.get(holder.ref).getReport_type().equals("RMK"))
        {
            holder.txtDBS.setText(listPending.get(holder.ref).getReport_type());
            holder.edtWorkwith.setText(listPending.get(holder.ref).getRemark());

            if (listPending.get(holder.ref).isEditable()) {
                holder.edtWorkwith.setEnabled(true);
            } else {
                holder.edtWorkwith.setEnabled(false);
            }

            holder.edtWorkwith.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {


                    NewEntryGetSet getset = listPending.get(holder.ref);
                    if (s.toString().equals("")) {
                        getset.setRemark("- N.A -");
                    } else {
                        getset.setRemark(s.toString());
                    }
                    listPending.set(holder.ref, getset);

                }
            });


            holder.txtDBS.setVisibility(View.VISIBLE);
            holder.edtWorkwith.setVisibility(View.VISIBLE);
            holder.txtDoctor.setVisibility(View.GONE);
        }
        else if (listPending.get(holder.ref).getReport_type().equals("ADV"))
        {
            //kiran
            holder.txtDBS.setText(listPending.get(holder.ref).getReport_type() + " : " + listPending.get(holder.ref).getAdvDate());
            holder.edtWorkwith.setText(listPending.get(holder.ref).getAdvice());
            holder.txtDoctor.setText(listPending.get(holder.ref).getEmpName().trim());
            if (listPending.get(holder.ref).isEditable()) {
                holder.edtWorkwith.setEnabled(true);
            } else {
                holder.edtWorkwith.setEnabled(false);
            }
            holder.edtWorkwith.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s)
                {
                    NewEntryGetSet getset = listPending.get(holder.ref);
                    if (s.toString().equals("")) {
                        getset.setAdvice("- N.A -");
                    } else {
                        getset.setAdvice(s.toString());
                    }
                    listPending.set(holder.ref, getset);
                }
            });

            holder.txtDBS.setVisibility(View.VISIBLE);
            holder.edtWorkwith.setVisibility(View.VISIBLE);
            holder.txtDoctor.setVisibility(View.VISIBLE);
        }
        else if (listPending.get(holder.ref).getReport_type().equals("STK"))
        {
            holder.txtDBS.setText(listPending.get(holder.ref).getReport_type());

            holder.txtDBS.setVisibility(View.VISIBLE);
            holder.edtWorkwith.setVisibility(View.GONE);
            holder.txtDoctor.setVisibility(View.GONE);
        }

        if (listPending.get(holder.ref).getReport_type().equals("DCR") ||
                listPending.get(holder.ref).getReport_type().equals("XCR") ||
                listPending.get(holder.ref).getReport_type().equalsIgnoreCase("ACR") ||
                listPending.get(holder.ref).getReport_type().equalsIgnoreCase("LCR") ||
                listPending.get(holder.ref).getReport_type().equalsIgnoreCase("JCR") ||
                listPending.get(holder.ref).getReport_type().equalsIgnoreCase("ZCR") ||
                listPending.get(holder.ref).getReport_type().equalsIgnoreCase("SRD") ||
                listPending.get(holder.ref).getReport_type().equalsIgnoreCase("TNS") ||
                listPending.get(holder.ref).getReport_type().equalsIgnoreCase("ROA") ||//added
                listPending.get(holder.ref).getReport_type().equalsIgnoreCase("ROR") ||//added
                listPending.get(holder.ref).getReport_type().equalsIgnoreCase("NCR") ||//added
                listPending.get(holder.ref).getReport_type().equalsIgnoreCase("INT"))//added
        {

            holder.txtDBS.setVisibility(View.VISIBLE);
            if (listPending.get(holder.ref).getNew_cycle().equals("1")) {
                holder.txtDBS.setText(listPending.get(holder.ref).getReport_type() + " : N");
            } else {
                holder.txtDBS.setText(listPending.get(holder.ref).getReport_type());
            }


            if(listPending.get(holder.ref).getReport_type().equalsIgnoreCase("INT")||
                    listPending.get(holder.ref).getReport_type().equalsIgnoreCase("ROR")||
                    listPending.get(holder.ref).getReport_type().equalsIgnoreCase("ROA"))
            {
                holder.txtDoctor.setVisibility(View.GONE);
            }
            else
            {
                holder.txtDoctor.setVisibility(View.VISIBLE);
            }

            Log.e("FOCUS STRING >> ", "getView: "+listPending.get(holder.ref).getFocusProducts() );

            List<String> items = new ArrayList<>();
            if (!listPending.get(holder.ref).getFocusProducts().equals(""))
            {
                holder.llFocusFor.setVisibility(View.VISIBLE);

                items = Arrays.asList(listPending.get(holder.ref).getFocusProducts().split(","));

                String[] tempAry = listPending.get(holder.ref).getFocusProducts().split(",");

                if (tempAry != null && tempAry.length > 0) {
                    items = new ArrayList<>();

                    for (int i = 0; i < tempAry.length; i++) {
                        String[] temp = tempAry[i].split("#");
                        doctorId = temp[0];
                        items.add(temp[1]);
                    }
                }


                listProduct = new ArrayList<>();
                ArrayList<String> listReason = new ArrayList<>();
                for (int i = 0; i < items.size(); i++) {
                    String[] temp = items.get(i).split("---");

                    listProduct.add(temp[0]);
                    listReason.add(temp[1]);
                }

                Log.e("list product size", listProduct.size() + " sdf");

                if (listProduct.size() == 1) {
                    holder.llFocus1.setVisibility(View.VISIBLE);
                    holder.llFocus2.setVisibility(View.GONE);

                    holder.edttext1.setText(listProduct.get(0));
                    holder.edtReason1.setText(listReason.get(0));

                    String[] arrProduct = listProduct.get(0).split(" : ");
                    product1 = arrProduct[0];
                    holder.edttext1.setText(listProduct.get(0));

                } else if (listProduct.size() == 2) {

                    holder.llFocus1.setVisibility(View.VISIBLE);
                    holder.llFocus2.setVisibility(View.VISIBLE);

                    holder.edttext1.setText(listProduct.get(0));
                    holder.edtReason1.setText(listReason.get(0));

                    holder.edttext2.setText(listProduct.get(1));
                    holder.edtReason2.setText(listReason.get(1));


                    String[] arrProduct = listProduct.get(0).split(" : ");
                    String[] arrProduct2 = listProduct.get(1).split(" : ");
                    product1 = arrProduct[0];
                    product2 = arrProduct2[0];

                }

                if (listPending.get(holder.ref).isEditable())
                {
                    holder.llAddFocus.setVisibility(View.VISIBLE);
                    holder.llAdd.setVisibility(View.VISIBLE);
                    holder.edttext1.setClickable(true);
                    holder.edttext2.setClickable(true);

                    holder.edtReason1.setClickable(true);
                    holder.edtReason2.setClickable(true);

                    //For Hide bottom focus add when already two items available
                    if(holder.llFocus1.getVisibility()==View.VISIBLE &&
                            holder.llFocus2.getVisibility()==View.VISIBLE)
                    {
                        Log.e("CONDITION        1", "getView: ");
                        holder.llAddFocus.setVisibility(View.GONE);
                    }
                    else if(holder.llFocus1.getVisibility()==View.VISIBLE &&
                            holder.llFocus1.getVisibility()==View.GONE)
                    {
                        Log.e("CONDITION        2", "getView: ");
                        holder.llAddFocus.setVisibility(View.VISIBLE);
                    }
                    else if(holder.llFocus1.getVisibility()==View.GONE &&
                            holder.llFocus2.getVisibility()==View.VISIBLE)
                    {
                        Log.e("CONDITION        3", "getView: ");
                        holder.llAddFocus.setVisibility(View.VISIBLE);
                    }

                    holder.img_focus_delete_1.setVisibility(View.VISIBLE);
                    holder.img_focus_delete_2.setVisibility(View.VISIBLE);


                }
                else
                {
                    holder.llAddFocus.setVisibility(View.GONE);
                    holder.llAdd.setVisibility(View.GONE);
                    holder.edttext1.setClickable(false);
                    holder.edttext2.setClickable(false);

                    holder.edtReason1.setClickable(false);
                    holder.edtReason2.setClickable(false);

                    holder.img_focus_delete_1.setVisibility(View.GONE);
                    holder.img_focus_delete_2.setVisibility(View.GONE);
                }
            }
            else
            {
                holder.llFocusFor.setVisibility(View.GONE);
                holder.llAddFocus.setVisibility(View.GONE);

                if (listPending.get(holder.ref).isEditable())
                {
                    holder.llAdd.setVisibility(View.VISIBLE);
                }
                else
                {
                    holder.llAdd.setVisibility(View.GONE);
                }

            }

            holder.edttext1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("List product size", listProduct.size() + " ");

                    if (listProduct != null && listProduct.size() > 0) {
                        try {
                            if (listPending.get(holder.ref).isEditable()) {
                                showFocusedDialog(holder.edttext1,
                                        holder.edttext2,
                                        "Product",
                                        "1",
                                        position);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            holder.edttext2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("List product size", listProduct.size() + " ");
                    if (listProduct != null && listProduct.size() > 0) {
                        try {
                            if (listPending.get(holder.ref).isEditable()) {
                                showFocusedDialog(holder.edttext1,
                                        holder.edttext2,
                                        "Product",
                                        "2",
                                        position);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            holder.edtReason1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (listPending.get(holder.ref).isEditable()) {
                            showFocusedDialog(holder.edtReason1,
                                    holder.edtReason2,
                                    "Reason",
                                    "1",
                                    position);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            holder.edtReason2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (listPending.get(holder.ref).isEditable()) {
                            showFocusedDialog(holder.edtReason1,
                                    holder.edtReason2,
                                    "Reason",
                                    "2",
                                    position);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });


            holder.img_focus_delete_1.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (holder.llFocus2.getVisibility() == View.GONE)
                    {
                        AppUtils.showToast(activity, "You have to enter atleast one product.");
                    }
                    else
                        {
                        showConfirmationDialog(holder.ref,
                                holder.edttext1,
                                holder.edttext2,
                                holder.llFocus1,
                                holder.llFocus2,
                                holder.edtReason1,
                                holder.edtReason2,
                                "1",
                                holder.llAddFocus);
                    }
                }
            });

            holder.img_focus_delete_2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    try {
                        showConfirmationDialog(holder.ref,
                                holder.edttext1,
                                holder.edttext2,
                                holder.llFocus1,
                                holder.llFocus2,
                                holder.edtReason1,
                                holder.edtReason2,
                                "2",
                                holder.llAddFocus);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


            holder.llAdd.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        NewEntryGetSet newEntryGetSet = listPending.get(position);
                        VariationResponse.VariationsBean getSet = new VariationResponse.VariationsBean();
                        getSet.setName("Product");
                        getSet.setItem_code("Product");
                        getSet.setItem_id_code("Product");
                        getSet.setReason("R");
                        getSet.setReason_code("R");
                        getSet.setStock("1");
                        getSet.setProduct_type("0");
                        getSet.setTemp(true);
                        ArrayList<VariationResponse.VariationsBean> listProduct = AppUtils.getArrayListFromJsonStringVariation(listPending.get(position).getProducts());
                        listProduct.add(getSet);
                        newEntryGetSet.setProducts(AppUtils.getStringFromArrayListVariations(listProduct));
                        listPending.set(position,newEntryGetSet);
                        if(adapter!=null)
                        {
                            adapter.notifyDataSetChanged();
                        }
                        notifyDataSetChanged();
                        List<NewEntryGetSet> listOffline = NewEntryGetSet.listAll(NewEntryGetSet.class);
                        ArrayList<NewEntryGetSet> listUserEntry = new ArrayList<>();
                        for (int i = 0; i < listOffline.size(); i++)
                        {
                            if(listOffline.get(i).getUser_id().equals(sessionManager.getUserId()))
                            {
                                listUserEntry.add(listOffline.get(i));
                            }
                        }

                        NewEntryGetSet newEntryGetSet1 = NewEntryGetSet.findById(NewEntryGetSet.class, listUserEntry.get(holder.ref).getId());
                        newEntryGetSet1.setProducts(AppUtils.getStringFromArrayListVariations(listProduct));
                        newEntryGetSet1.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            holder.llAddFocus.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (holder.llFocus1.getVisibility() == View.VISIBLE && holder.llFocus2.getVisibility() == View.VISIBLE) {
                            AppUtils.showToast(activity, "You can enter maximum 2 products.");
                        }
                        else
                        {

                            ArrayList<VariationResponse.VariationsBean> listFocusProduct = AppUtils.getArrayListFromJsonStringVariation(listPending.get(holder.ref).getProducts());

                            if (holder.llFocus1.getVisibility() == View.GONE) {

                                //holder.edttext1.setText(listFocusProduct.get(0).getItem_id_code());
                                holder.edttext1.setText("Select Product");
                                holder.edtReason1.setText("New");
                                holder.llFocus1.setVisibility(View.VISIBLE);
                            } else if (holder.llFocus2.getVisibility() == View.GONE) {
                                //holder.edttext2.setText(listFocusProduct.get(0).getItem_id_code());
                                holder.edttext2.setText("Select Product");
                                holder.edtReason2.setText("New");
                                holder.llFocus2.setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        }
        else
        {
            holder.llFocusFor.setVisibility(View.GONE);
            holder.llAddFocus.setVisibility(View.GONE);
        }

        holder.ivMinus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog(holder.ref);
            }
        });

        holder.ivEdit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    listPending.get(holder.ref).setEditable(true);
                    notifyDataSetChanged();
                    doctorId = listPending.get(holder.ref).getDr_id();
                    Log.e("Doctor Id", doctorId + " ");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        holder.ivDone.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AppUtils.hideKeyboard(holder.edtReason1, activity);

                Log.e("POSITION  >> ", "onClick: "+holder.ref );
                Log.e("ITEMS >>   >> ", "onClick: "+AppUtils.getArrayListFromJsonStringVariation(listPending.get(holder.ref).getProducts()).size());
                Log.e("TYPE >> ", "onClick: "+listPending.get(position).getReport_type() );

                boolean isValid = false;

                try
                {
                    doctorId = listPending.get(holder.ref).getDr_id();

                    if(!listPending.get(holder.ref).getReport_type().equals("RMK")||
                            !listPending.get(holder.ref).getReport_type().equals("STK") ||
                            !listPending.get(holder.ref).getReport_type().equals("ADV"))
                    {
                        listRestriction = new ArrayList<String>();
                        if (listPending.get(holder.ref).getFocusProducts() != null &&
                                listPending.get(holder.ref).getFocusProducts().length() > 0)
                        {
                            if (holder.llFocus1.getVisibility() == View.VISIBLE) {
                                listRestriction.add(holder.edtReason1.getText().toString().trim());
                            }
                            if (holder.llFocus2.getVisibility() == View.VISIBLE) {
                                listRestriction.add(holder.edtReason2.getText().toString().trim());
                            }

                            countNew = 0;
                            countEnhance = 0;

                            for (int i = 0; i < listRestriction.size(); i++) {
                                if (listRestriction.get(i).equalsIgnoreCase("New")) {
                                    countNew = countNew + 1;
                                } else if (listRestriction.get(i).equalsIgnoreCase("Enhance")) {
                                    countEnhance = countEnhance + 1;
                                }
                            }

                            if (countNew > 2) {
                                AppUtils.showToast(activity, "You can enter only 2 product as new.");
                                return;
                            } else if (countEnhance > 2) {
                                AppUtils.showToast(activity, "You can enter only 2 product as enhance.");
                                return;
                            }
                            else
                            {

                                ArrayList<String> listString = new ArrayList<String>();

                                String tempProduct1 = "", tempProduct2 = "", tempProduct3 = "", tempProduct4 = "";

                                if (holder.llFocus1.getVisibility() == View.VISIBLE)
                                {
                                    if (holder.edttext1.getText().toString().equalsIgnoreCase("") ||
                                            holder.edttext1.getText().toString().equalsIgnoreCase("Select Product")) {
                                        AppUtils.showToast(activity,"Please select focus for product.");
                                        return;
                                    }
                                    else
                                    {
                                        tempProduct1 = doctorId + "#" + holder.edttext1.getText().toString().trim() + "---" + holder.edtReason1.getText().toString().trim();
                                        listString.add(tempProduct1);
                                    }
                                }


                                if (holder.llFocus2.getVisibility() == View.VISIBLE)
                                {
                                    if (holder.edttext2.getText().toString().equalsIgnoreCase("") ||
                                            holder.edttext2.getText().toString().equalsIgnoreCase("Select Product"))
                                    {
                                        AppUtils.showToast(activity,"Please select focus for product.");
                                        return;
                                    }
                                    else
                                    {
                                        tempProduct2 = doctorId + "#" + holder.edttext2.getText().toString().trim() + "---" + holder.edtReason2.getText().toString().trim();
                                        listString.add(tempProduct2);
                                    }
                                }

                                String finalString = "";
                                for (int i = 0; i < listString.size(); i++) {
                                    if (finalString.length() == 0) {
                                        finalString = listString.get(i);
                                    } else {
                                        finalString = finalString + "," + listString.get(i);
                                    }
                                }

                                focusForString = finalString;

                                listPending.get(holder.ref).setFocusProducts(focusForString);

                                updateFocusProducts(focusForString, position);
                            }
                        }
                        else
                        {
                            focusForString = "";
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                List<NewEntryGetSet> listOffline1 = NewEntryGetSet.listAll(NewEntryGetSet.class);
                //Main Pending entry list
                ArrayList<NewEntryGetSet> listEntry1 = (ArrayList<NewEntryGetSet>) listOffline1;

                ArrayList<NewEntryGetSet> listUserEntry1 = new ArrayList<>();
                for (int i = 0; i < listEntry1.size(); i++)
                {
                    if(listEntry1.get(i).getUser_id().equals(sessionManager.getUserId()))
                    {
                        listUserEntry1.add(listEntry1.get(i));
                    }
                }

                ArrayList<VariationResponse.VariationsBean> listProduct = AppUtils.getArrayListFromJsonStringVariation(listUserEntry1.get(position).getProducts());

                if(listPending.get(holder.ref).getReport_type().equals("RMK")||
                        listPending.get(holder.ref).getReport_type().equals("STK")||
                        listPending.get(holder.ref).getReport_type().equals("ADV"))
                {
                    isValid = true;
                }
                else
                {
                    if(listProduct.size()>0)
                    {
                        for (int i = 0; i < listProduct.size(); i++)
                        {
                            if (listProduct.get(i).getName().equalsIgnoreCase("Product"))
                            {
                                listProduct.get(i).setTemp(true);
                                isValid = false;
                            } else
                            {
                                listProduct.get(i).setTemp(false);
                                isValid = true;
                            }
                        }
                    }
                }



                List<NewEntryGetSet> listOffline = NewEntryGetSet.listAll(NewEntryGetSet.class);
                //Main Pending entry list
                ArrayList<NewEntryGetSet> listEntry = (ArrayList<NewEntryGetSet>) listOffline;


                ArrayList<NewEntryGetSet> listUserEntry = new ArrayList<>();
                for (int i = 0; i < listEntry.size(); i++)
                {
                    if(listEntry.get(i).getUser_id().equals(sessionManager.getUserId()))
                    {
                        listUserEntry.add(listEntry.get(i));
                    }
                }


                /*Update Products list(as a string) in main pending entry */
                NewEntryGetSet newEntryGetSet = NewEntryGetSet.findById(NewEntryGetSet.class, listUserEntry.get(holder.ref).getId());

                if (listPending.get(holder.ref).getReport_type().equals("RMK")) {
                    newEntryGetSet.setRemark(holder.edtWorkwith.getText().toString());
                }

                if (listPending.get(holder.ref).getReport_type().equals("ADV")) {
                    newEntryGetSet.setAdvice(holder.edtWorkwith.getText().toString());
                }

                if (isValid)
                {
                    AppUtils.showToast(activity, "Changes Saved!");
                    if(adapter!=null && listProduct.size()>0)
                    {
                        newEntryGetSet.setProducts(AppUtils.getStringFromArrayListVariations(listProduct));
                    }
                    newEntryGetSet.save();
                    notifyDataSetChanged();
                    if (ActivityPendingEntry.handler != null)
                    {
                        Message message = Message.obtain();
                        message.what = 101;
                        ActivityPendingEntry.handler.sendMessage(message);
                    }
                }
                else
                {
                    AppUtils.showToast(activity, "Please Select Product First");
                }
            }

            private void updateFocusProducts(String updatedFocusString, int pos)
            {
                List<NewEntryGetSet> listOffline = NewEntryGetSet.listAll(NewEntryGetSet.class);
                /*Main Pending entry list*/
                ArrayList<NewEntryGetSet> listEntry = (ArrayList<NewEntryGetSet>) listOffline;

                ArrayList<NewEntryGetSet> listUserEntry = new ArrayList<>();
                for (int i = 0; i < listEntry.size(); i++)
                {
                    if(listEntry.get(i).getUser_id().equals(sessionManager.getUserId()))
                    {
                        listUserEntry.add(listEntry.get(i));
                    }
                }

                /*Update Focus Products list(as a string) in main pending entry */
                NewEntryGetSet newEntryGetSet = NewEntryGetSet.findById(NewEntryGetSet.class, listUserEntry.get(pos).getId());
                newEntryGetSet.setFocusProducts(updatedFocusString);
                newEntryGetSet.save();
            }
        });

        if (listPending.get(holder.ref).getReport_type().equals("DBS"))
        {
            holder.listChild.setVisibility(View.GONE);
        }
        else if (listPending.get(holder.ref).getReport_type().equals("RMK") || listPending.get(holder.ref).getReport_type().equals("ADV"))
        {
            holder.listChild.setVisibility(View.GONE);

            if (listPending.get(holder.ref).isEditable()) {
                holder.ivDone.setVisibility(View.VISIBLE);
                holder.ivEdit.setVisibility(View.GONE);
            } else {
                holder.ivDone.setVisibility(View.GONE);
                holder.ivEdit.setVisibility(View.VISIBLE);
            }
            holder.ivEdit.setEnabled(true);
        }
        else {
            if (!listPending.get(holder.ref).getProducts().equals("")) {
                holder.listChild.setVisibility(View.VISIBLE);

                ArrayList<VariationResponse.VariationsBean> listProduct = AppUtils.getArrayListFromJsonStringVariation(listPending.get(holder.ref).getProducts());

                adapter = new PendingEntryChildAdapter(activity, listProduct, listPending.get(holder.ref).isEditable(), listPending.get(holder.ref).getReport_type(), position);
                holder.listChild.setAdapter(adapter);

                if (listPending.get(holder.ref).isEditable()) {
                    holder.ivDone.setVisibility(View.VISIBLE);
                    holder.ivEdit.setVisibility(View.GONE);
                } else {
                    holder.ivDone.setVisibility(View.GONE);
                    holder.ivEdit.setVisibility(View.VISIBLE);
                }
                holder.ivEdit.setEnabled(true);
            } else {
                holder.listChild.setVisibility(View.GONE);
                holder.ivEdit.setEnabled(false);
            }
        }

        return rowView;
    }

    private void showConfirmationDialog(final int pos,
                                        final TextView edttext1,
                                        final TextView edttext2,
                                        final LinearLayout llFocusFor1,
                                        final LinearLayout llFocusFor2,
                                        final TextView edtReason1,
                                        final TextView edtReason2,
                                        final String cameFrom,
                                        final View llAddFocus) {
        try
        {

            final BottomSheetDialog dialog = new BottomSheetDialog(activity ,R.style.BottomSheetDialogThemeLogout);

            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            View sheetView = activity.getLayoutInflater().inflate(R.layout.bttom_layout_logout, null);
            dialog.setContentView(sheetView);

            TextView txtHeader = dialog.findViewById(R.id.tvHeader);
            TextView txtHeader2 = dialog.findViewById(R.id.tvDescription);

            TextView btnNo = dialog.findViewById(R.id.tvCancel);
            TextView btnYes = dialog.findViewById(R.id.tvConfirm);

            txtHeader.setText("Delete Product");

            String deleteProduct = "";
            if (cameFrom.equals("1"))
            {
                deleteProduct = edttext1.getText().toString().trim();
            }
            else if (cameFrom.equals("2"))
            {
                deleteProduct = edttext2.getText().toString().trim();
            }
            txtHeader2.setText("Do you want to delete " + deleteProduct+" ?");

            btnNo.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    dialog.cancel();
                }
            });

            btnYes.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        dialog.dismiss();
                        dialog.cancel();
                        llAddFocus.setVisibility(View.VISIBLE);
                        //manageVisibility(edttext1, edttext2, llFocusFor1, llFocusFor2, edtReason1, edtReason2, cameFrom);
                        manage(edttext1, edttext2, llFocusFor1, llFocusFor2, edtReason1, edtReason2, cameFrom);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void manageVisibility(final TextView edttext1,
                                  final TextView edttext2,
                                  final LinearLayout llFocusFor1,
                                  final LinearLayout llFocusFor2,
                                  final TextView edtReason1,
                                  final TextView edtReason2,
                                  final String cameFrom) {
        if (cameFrom.equalsIgnoreCase("1"))
        {
            if (llFocusFor2.getVisibility() == View.VISIBLE)
            {
                if (edttext2.getText().toString().trim().length() > 0)
                {
                    edttext1.setText(edttext2.getText().toString().trim());
                    edtReason1.setText(edtReason2.getText().toString().trim());
                    llFocusFor1.setVisibility(View.VISIBLE);
                    edttext2.setText("");
                }
            }
            else
            {
                llFocusFor1.setVisibility(View.GONE);
            }

            if (edttext1.getText().toString().trim().equals(""))
            {
                llFocusFor1.setVisibility(View.GONE);
            }
            else
            {
                llFocusFor1.setVisibility(View.VISIBLE);
            }


            if (edttext2.getText().toString().trim().equals(""))
            {
                llFocusFor2.setVisibility(View.GONE);
            } else {
                llFocusFor2.setVisibility(View.VISIBLE);
            }

        }
        else
        {
            if(llFocusFor1.getVisibility() == View.VISIBLE)
            {
                if(edttext1.getText().toString().length()>0)
                {
                    llFocusFor2.setVisibility(View.GONE);
                    edtReason2.setText("");
                    edttext2.setText("");
                }
            }
            else
            {

            }
        }
    }

    private void manage(final TextView edttext1,
                        final TextView edttext2,
                        final LinearLayout llFocusFor1,
                        final LinearLayout llFocusFor2,
                        final TextView edtReason1,
                        final TextView edtReason2,
                        final String cameFrom)
    {
        if(cameFrom.equals("1"))
        {
            llFocusFor1.setVisibility(View.GONE);
            edttext1.setText("Select Product");
            llFocusFor2.setVisibility(View.VISIBLE);
        }
        else
        {
            llFocusFor2.setVisibility(View.GONE);
            edttext2.setText("Select Product");
            llFocusFor1.setVisibility(View.VISIBLE);
        }
    }

    /*private void manageVisibility(final TextView edttext1, final TextView edttext2, final TextView edttext3, final TextView edttext4, final LinearLayout llFocusFor1, final LinearLayout llFocusFor2, final LinearLayout llFocusFor3,
                                  final LinearLayout llFocusFor4, final TextView edtReason1, final TextView edtReason2, final TextView edtReason3, final TextView edtReason4, final String cameFrom) {
        if (cameFrom.equalsIgnoreCase("1"))
        {
            if (llFocusFor2.getVisibility() == View.VISIBLE) {
                if (edttext2.getText().toString().trim().length() > 0) {
                    edttext1.setText(edttext2.getText().toString().trim());
                    edtReason1.setText(edtReason2.getText().toString().trim());
                    llFocusFor1.setVisibility(View.VISIBLE);
                    edttext2.setText("");
                }
            } else {
                llFocusFor1.setVisibility(View.GONE);
            }

            if (llFocusFor3.getVisibility() == View.VISIBLE) {
                if (edttext3.getText().toString().trim().length() > 0) {
                    edttext2.setText(edttext3.getText().toString().trim());
                    edtReason2.setText(edtReason3.getText().toString().trim());
                    llFocusFor2.setVisibility(View.VISIBLE);
                    edttext3.setText("");
                }
            } else {
                llFocusFor2.setVisibility(View.GONE);
            }

            if (llFocusFor4.getVisibility() == View.VISIBLE) {
                if (edttext4.getText().toString().trim().length() > 0) {
                    edttext3.setText(edttext4.getText().toString().trim());
                    edtReason3.setText(edtReason4.getText().toString().trim());
                    llFocusFor3.setVisibility(View.VISIBLE);
                    edttext4.setText("");
                }
            } else {
                llFocusFor3.setVisibility(View.GONE);
            }

            if (edttext1.getText().toString().trim().equals("")) {
                llFocusFor1.setVisibility(View.GONE);
            } else {
                llFocusFor1.setVisibility(View.VISIBLE);
            }


            if (edttext2.getText().toString().trim().equals("")) {
                llFocusFor2.setVisibility(View.GONE);
            } else {
                llFocusFor2.setVisibility(View.VISIBLE);
            }

            if (edttext3.getText().toString().trim().equals("")) {
                llFocusFor3.setVisibility(View.GONE);
            } else {
                llFocusFor3.setVisibility(View.VISIBLE);
            }

            if (edttext4.getText().toString().trim().equals("")) {
                llFocusFor4.setVisibility(View.GONE);
            } else {
                llFocusFor4.setVisibility(View.VISIBLE);
            }
        }
        else if (cameFrom.equalsIgnoreCase("2"))
        {
            if (llFocusFor3.getVisibility() == View.VISIBLE)
            {
                if (edttext3.getText().toString().trim().length() > 0) {
                    edttext2.setText(edttext3.getText().toString().trim());
                    edtReason2.setText(edtReason3.getText().toString().trim());
                    llFocusFor2.setVisibility(View.VISIBLE);
                    edttext3.setText("");
                }
            }
            else
            {
                llFocusFor2.setVisibility(View.GONE);
            }

            if (llFocusFor4.getVisibility() == View.VISIBLE) {
                if (edttext4.getText().toString().trim().length() > 0) {
                    edttext3.setText(edttext4.getText().toString().trim());
                    edtReason3.setText(edtReason4.getText().toString().trim());
                    llFocusFor3.setVisibility(View.VISIBLE);
                    edttext4.setText("");
                }
            } else {
                llFocusFor3.setVisibility(View.GONE);
            }

            if (edttext1.getText().toString().trim().equals("")) {
                llFocusFor1.setVisibility(View.GONE);
            } else {
                llFocusFor1.setVisibility(View.VISIBLE);
            }

			*//*if (edttext2.getText().toString().trim().equals(""))
			{
				llFocusFor2.setVisibility(View.GONE);
			}
			else
			{
				llFocusFor2.setVisibility(View.VISIBLE);
			}*//*

            if (edttext3.getText().toString().trim().equals("")) {
                llFocusFor3.setVisibility(View.GONE);
            } else {
                llFocusFor3.setVisibility(View.VISIBLE);
            }

            if (edttext4.getText().toString().trim().equals("")) {
                llFocusFor4.setVisibility(View.GONE);
            } else {
                llFocusFor4.setVisibility(View.VISIBLE);
            }
        } else if (cameFrom.equalsIgnoreCase("3")) {
            if (llFocusFor4.getVisibility() == View.VISIBLE) {
                if (edttext4.getText().toString().trim().length() > 0) {
                    edttext3.setText(edttext4.getText().toString().trim());
                    edtReason3.setText(edtReason4.getText().toString().trim());
                    llFocusFor3.setVisibility(View.VISIBLE);
                    edttext4.setText("");
                }
            } else {
                llFocusFor3.setVisibility(View.GONE);
            }

            if (edttext1.getText().toString().trim().equals("")) {
                llFocusFor1.setVisibility(View.GONE);
            } else {
                llFocusFor1.setVisibility(View.VISIBLE);
            }


            if (edttext2.getText().toString().trim().equals("")) {
                llFocusFor2.setVisibility(View.GONE);
            } else {
                llFocusFor2.setVisibility(View.VISIBLE);
            }

            if (edttext3.getText().toString().trim().equals("")) {
                llFocusFor3.setVisibility(View.GONE);
            } else {
                llFocusFor3.setVisibility(View.VISIBLE);
            }

            if (edttext4.getText().toString().trim().equals("")) {
                llFocusFor4.setVisibility(View.GONE);
            } else {
                llFocusFor4.setVisibility(View.VISIBLE);
            }
        } else if (cameFrom.equalsIgnoreCase("4")) {
            edttext4.setText("");
            edtReason4.setText("");
            llFocusFor4.setVisibility(View.GONE);

            if (edttext1.getText().toString().trim().equals("")) {
                llFocusFor1.setVisibility(View.GONE);
            } else {
                llFocusFor1.setVisibility(View.VISIBLE);
            }


            if (edttext2.getText().toString().trim().equals("")) {
                llFocusFor2.setVisibility(View.GONE);
            } else {
                llFocusFor2.setVisibility(View.VISIBLE);
            }

            if (edttext3.getText().toString().trim().equals("")) {
                llFocusFor3.setVisibility(View.GONE);
            } else {
                llFocusFor3.setVisibility(View.VISIBLE);
            }

            if (edttext4.getText().toString().trim().equals("")) {
                llFocusFor4.setVisibility(View.GONE);
            } else {
                llFocusFor4.setVisibility(View.VISIBLE);
            }
        }
    }*/

    private void showDeleteDialog(final int position) {
        try
        {
            /*final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().getAttributes().windowAnimations = R.style.Animations_SmileWindow;
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            dialog.setContentView(R.layout.dialog_confirm);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            Window window = dialog.getWindow();
            lp.copyFrom(window.getAttributes());
            //This makes the dialog take up the full width
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);

            Button btnNo = (Button) dialog.findViewById(R.id.btnNo_Dialog_Confirm);
            Button btnYes = (Button) dialog.findViewById(R.id.btnYes_Dialog_Confirm);

            TextView txtHeader = (TextView) dialog.findViewById(R.id.txtHeader_Dialog_Delete);
            TextView txtHeader2 = (TextView) dialog.findViewById(R.id.txtHeader2_Dialog_Delete);*/

            final BottomSheetDialog dialog = new BottomSheetDialog(activity ,R.style.BottomSheetDialogThemeLogout);

            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            View sheetView = activity.getLayoutInflater().inflate(R.layout.bttom_layout_logout, null);
            dialog.setContentView(sheetView);

            TextView txtHeader = dialog.findViewById(R.id.tvHeader);
            TextView txtHeader2 = dialog.findViewById(R.id.tvDescription);

            TextView btnNo = dialog.findViewById(R.id.tvCancel);
            TextView btnYes = dialog.findViewById(R.id.tvConfirm);

            txtHeader.setText("Delete Sample Details");
            txtHeader2.setText("Are you sure want to Delete sample details?");

            btnNo.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    dialog.cancel();
                }
            });

            btnYes.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    dialog.cancel();
                    try
                    {
                        if(listPending.get(position).getReport_type().equalsIgnoreCase("STK"))
                        {
                            sessionManager.setIsSTKDone(false);
                        }

                        NewEntryGetSet entry = NewEntryGetSet.findById(NewEntryGetSet.class, listPending.get(position).getId());
                        entry.delete();

                        //List<NewEntryGetSet> books = NewEntryGetSet.find(NewEntryGetSet.class, "dr_id = ?", listPending.get(position).getDr_id());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    listPending.remove(position);

                    notifyDataSetChanged();

                    if (listPending.size() == 0) {
                        llNoData.setVisibility(View.VISIBLE);
                        listview.setVisibility(View.GONE);

                        AppUtils.showToast(activity, "All Pending Entry Removed.");

						/*Toast toast = Toast.makeText(activity, "All Pending Entry Removed.", Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();*/
                    }
                }
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String Currentdate = "";

    public void updateData(String product, String unit, String reason, String remarks, String apiString, String id, String focus, ViewHolder holder) {
        // Current Date
        Date d = new Date();
        try {
            Currentdate = new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy").parse(d.toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        UnisonDatabaseHelper sdbh = new UnisonDatabaseHelper(activity);
        SQLiteDatabase sqlDB = sdbh.getWritableDatabase();

        try {
            ContentValues cv = new ContentValues();
            cv.put(UnisonDatabaseHelper.PRODUCT, product);
            cv.put(UnisonDatabaseHelper.REASON, reason);
            cv.put(UnisonDatabaseHelper.UNIT, unit);
            cv.put(UnisonDatabaseHelper.REMARKS, remarks);
            cv.put(UnisonDatabaseHelper.API_STRING, apiString);
            cv.put(UnisonDatabaseHelper.FOCUS_FOR_STRING, focus);

            String whereClause = UnisonDatabaseHelper.DATE + "='" + Currentdate + "' AND " + UnisonDatabaseHelper.ID + "='" + id + "'";
            sqlDB.update(UnisonDatabaseHelper.DAILY_ENTRY_TABLE, cv, whereClause, null);
            sqlDB.close();
            sdbh.close();

            Log.e("Updated", "Trueee");

            isUpdateCalled = true;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqlDB != null) {
                sqlDB.close();
                sdbh.close();
            }
        }
    }

    public String getApiStringSTK(ArrayList<ViewEntryChildGetSet> list) {
        String api = "";

        ArrayList<String> listString = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            String test = list.get(i).getProduct().trim() + " " + list.get(i).getQuantity().trim();
            listString.add(test);
        }

        String temp = listString + "";
        api = temp.replace("[", "").replace("]", "").replace(",", "");
        return api;
    }


    public String getApiString(ArrayList<NewEntryGetSet> list) {
        String api = "";
        ArrayList<String> listString = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            //String test = list.get(i).getProducts().trim() + " " + list.get(i).getQuantity().trim() + " " + list.get(i).getReason().trim();
            String test = list.get(i).getProducts().trim() + " " + list.get(i).getInternee().trim() + " " + list.get(i).getRemark().trim();
            listString.add(test);
        }

        String temp = listString + "";
        api = temp.replace("[", "").replace("]", "").replace(",", "");

        return api;
    }


    private class ViewHolder {
        TextView txtDBS, txtDoctor;
        EditText edtWorkwith;
        ImageView ivMinus, ivEdit, ivDone;
        MitsAutoHeightListView listChild;
        LinearLayout llFocusFor, llFocus1, llFocus2, llAdd,llAddFocus;
        TextView edttext2, edtReason2,edtReason1,edttext1;
        ImageView img_focus_delete_1, img_focus_delete_2;
        int ref;
    }

    ProductAdapter productAdapter;

    private void showFocusedDialog(
            final TextView edittext1,
            final TextView edittext2,
            final String cameFrom,
            final String pos,
            final int mainPos) {
        try
        {
            /*LayoutInflater layoutInflater = LayoutInflater.from(activity);
            View promptView = layoutInflater.inflate(R.layout.dialog_listview, null);

            final Dialog dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
            dialog.setContentView(promptView);*/

            final Dialog dialog= new BottomSheetDialog(activity ,R.style.MaterialDialogSheetTemp);

            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            final View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_listview, null);
            dialog.setContentView(sheetView);

            dialog.findViewById(R.id.ivBack).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    dialog.cancel();
                }
            });

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    AppUtils.hideKeyboard(sheetView,activity);
                }
            });

            final BottomSheetListView listView = (BottomSheetListView) dialog.findViewById(R.id.lv_Dialog);
            TextView txtHeader = (TextView) dialog.findViewById(R.id.txtHeader_Dialog_ListView);

            final ArrayList<String> array = new ArrayList<String>();
            array.add("New");
            array.add("Enhance");

            final TextInputLayout inputSearch = (TextInputLayout) dialog.findViewById(R.id.inputSearch);
            final EditText edtSearch = (EditText) dialog.findViewById(R.id.edtSearch_Dialog_ListView);

            final ArrayList<VariationResponse.VariationsBean> listVariationSearch = new ArrayList<>();

            if (cameFrom.equalsIgnoreCase("Product"))
            {
                txtHeader.setText("Select Product");
                if (listProduct != null && listProduct.size() > 0) {

                    final ArrayList<VariationResponse.VariationsBean> listVariation;
                    List<DBVariation> books = DBVariation.listAll(DBVariation.class);
                    listVariation = new ArrayList<>();
                    for (int i = 0; i < books.size(); i++) {
                        DBVariation bean = books.get(i);
                        VariationResponse.VariationsBean variationsBean = new VariationResponse.VariationsBean();
                        variationsBean.setProduct_id(bean.getProduct_id());
                        variationsBean.setVariation_id(bean.getVariation_id());
                        variationsBean.setName(bean.getName());
                        variationsBean.setItem_code(bean.getItem_code());
                        variationsBean.setItem_id_code(bean.getItem_id_code());
                        variationsBean.setReason(bean.getReason());
                        variationsBean.setReason_id(bean.getReason_id());
                        variationsBean.setReason_code(bean.getReason_code());
                        variationsBean.setStock(String.valueOf(bean.getStock()));
                        variationsBean.setChecked(bean.isChecked());
                        variationsBean.setProduct_type(bean.getProduct_type());
                        listVariation.add(variationsBean);
                    }

                    inputSearch.setVisibility(View.VISIBLE);

                    productAdapter = new ProductAdapter(activity, listVariation, "Product", dialog, edittext1, edittext2, pos, false, new ArrayList<VariationResponse.VariationsBean>());
                    listView.setAdapter(productAdapter);


                    edtSearch.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                            int textlength = edtSearch.getText().length();

                            listVariationSearch.clear();
                            for (int i = 0; i < listVariation.size(); i++) {
                                if (listVariation.get(i).getName().toLowerCase().contains(edtSearch.getText().toString().toLowerCase().trim()) ||
                                        listVariation.get(i).getItem_id_code().toLowerCase().contains(edtSearch.getText().toString().toLowerCase().trim())) {
                                    listVariationSearch.add(listVariation.get(i));
                                }
                            }

                            Log.e("Called >> ", "onTextChanged: ");

                            productAdapter = new ProductAdapter(activity, listVariation, "Product", dialog, edittext1, edittext2, pos, true, listVariationSearch);
                            listView.setAdapter(productAdapter);
                        }

                        @Override
                        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                        }

                        @Override
                        public void afterTextChanged(Editable arg0) {
                        }
                    });
                }
            } else {
                inputSearch.setVisibility(View.GONE);
                ArrayAdapter<String> mHistory = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, array);
                listView.setAdapter(mHistory);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            Log.e("Text with position", cameFrom + " && " + array.get(position));
                            //listRestriction.add(array.get(position));
                            if (pos.equals("1")) {
                                edittext1.setText(array.get(position));
                            } else if (pos.equalsIgnoreCase("2")) {
                                edittext2.setText(array.get(position));
                            }

                            if (dialog != null) {
                                dialog.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

            /*dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    product1 = "";
                    product2 = "";
                }
            });

            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    product1 = "";
                    product2 = "";
                }
            });*/

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private class ProductAdapter extends BaseAdapter {
        private Activity activity;
        private LayoutInflater inflater = null;
        ArrayList<VariationResponse.VariationsBean> items;
        String spinnerRef;
        @SuppressWarnings("unused")
        int pos = 0;
        private TextView edttext1, edttext2;
        Dialog dialog;
        String focusPosition = "";
        boolean isForSearch = false;
        ArrayList<VariationResponse.VariationsBean> listVariationSearch;


        public ProductAdapter(Activity a,
                              ArrayList<VariationResponse.VariationsBean> item,
                              String ref,
                              Dialog dialog,
                              TextView edttext1,
                              TextView edttext2,
                              String pos1,
                              boolean isSearch,
                              ArrayList<VariationResponse.VariationsBean> listVAriationSearch) {
            this.activity = a;
            ArrayList<VariationResponse.VariationsBean> listTemp = new ArrayList<>();
            for (int i = 0; i < item.size(); i++)
            {
                if(item.get(i).getProduct_type().equalsIgnoreCase("1") || item.get(i).getName().equalsIgnoreCase("product"))
                {
                }
                else
                {
                    listTemp.add(item.get(i));
                }
            }
            try {
                listTemp.addAll(DataUtils.getSalesProduct(sessionManager));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            this.items = listTemp;
            this.spinnerRef = ref;
            this.dialog = dialog;
            this.edttext1 = edttext1;
            this.edttext2 = edttext2;
            this.focusPosition = pos1;
            this.isForSearch = isSearch;
            this.listVariationSearch = listVAriationSearch;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            if (isForSearch) {
                return listVariationSearch.size();
            } else {
                return items.size();
            }
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            View rowView = convertView;
            if (convertView == null) {
                try {
                    rowView = inflater.inflate(R.layout.rowview_mkt_code, null);

                    holder = new ViewHolder();

                    holder.txtmktCode = (TextView) rowView.findViewById(R.id.txtName);

                    rowView.setTag(holder);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                holder = (ViewHolder) rowView.getTag();
            }

            final VariationResponse.VariationsBean getSet;
            if (isForSearch) {
                getSet = listVariationSearch.get(position);
            } else {
                getSet = items.get(position);
            }

            try {
                if (spinnerRef.equals("Product")) {
                    holder.txtmktCode.setText(getSet.getItem_id_code() + " : " + getSet.getName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            rowView.setOnClickListener(v -> {
                try {

                    if (focusPosition.equalsIgnoreCase("1"))
                    {
                        Log.e("PRODUCT 2  ", "onClick: " + product2);
                        Log.e("COMPARE   ", "onClick: " + getSet.getItem_id_code());

                        if (product2.trim().equalsIgnoreCase(getSet.getItem_id_code()))
                        {
                            AppUtils.showToast(activity, "Please select another product.");
                        }
                        else
                            {
                            product1 = getSet.getItem_id_code();
                            edttext1.setText(getSet.getItem_id_code() + " : "+getSet.getName());
                        }
                    }
                    else if (focusPosition.equalsIgnoreCase("2"))
                    {
                        Log.e("PRODUCT 1  ", "onClick: " + product1);
                        Log.e("COMPARE   ", "onClick: " + getSet.getItem_id_code());

                        if (product1.trim().equalsIgnoreCase(getSet.getItem_id_code()))
                        {
                            AppUtils.showToast(activity, "Please select another product.");

                        } else {
                            product2 = getSet.getItem_id_code();
                            edttext2.setText(getSet.getItem_id_code() + " : "+getSet.getName());
                        }
                    }

                    dialog.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            return rowView;
        }

        private class ViewHolder {
            TextView txtmktCode;
        }
    }

    private String getNameFromFocusCode(int mainPendinListPostion,String code)
    {
        String productName = "";
        ArrayList<VariationResponse.VariationsBean> listProduct = AppUtils.getArrayListFromJsonStringVariation(listPending.get(mainPendinListPostion).getProducts());
        for (int i = 0; i < listProduct.size(); i++)
        {
            if(listProduct.get(i).getItem_id_code().equalsIgnoreCase(code))
            {
                productName = listProduct.get(i).getName();
            }
        }

        return productName;
    }
}