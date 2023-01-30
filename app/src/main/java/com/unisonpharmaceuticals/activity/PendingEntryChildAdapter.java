package com.unisonpharmaceuticals.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.unisonpharmaceuticals.R;
import com.unisonpharmaceuticals.classes.SessionManager;
import com.unisonpharmaceuticals.model.NewEntryGetSet;
import com.unisonpharmaceuticals.model.ReasonResponse;
import com.unisonpharmaceuticals.model.VariationResponse;
import com.unisonpharmaceuticals.model.VariationResponse.VariationsBean;
import com.unisonpharmaceuticals.model.for_sugar.DBReason;
import com.unisonpharmaceuticals.model.for_sugar.DBVariation;
import com.unisonpharmaceuticals.network.ApiClient;
import com.unisonpharmaceuticals.utils.AppUtils;
import com.unisonpharmaceuticals.utils.DataUtils;
import com.unisonpharmaceuticals.views.BottomSheetListView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PendingEntryChildAdapter extends BaseAdapter
{
	private Activity activity;
    private LayoutInflater inflater=null;
    ArrayList<VariationResponse.VariationsBean> listPending;
    ProductUnitAdapter productUnitAdapter;
    boolean isEditable = false;
    String dbcCode="";
	ArrayList<VariationResponse.VariationsBean> listVariation = new ArrayList<>();
	ArrayList<VariationResponse.VariationsBean> listVariationSearch = new ArrayList<>();
	ArrayList<ReasonResponse.ReasonsBean> listReason;
	int mainListPos = 0;
	public static Handler handler;
	private SessionManager sessionManager;
	private InputFilter filter;

	public PendingEntryChildAdapter(Activity a, ArrayList<VariationResponse.VariationsBean> item, boolean isEditable, String dbcCode,int mainListPos)
	{
		sessionManager = new SessionManager(a);
        this.activity = a;
        this.listPending = item;
        this.isEditable = isEditable;
        this.dbcCode = dbcCode;
        this.mainListPos = mainListPos;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        filter = new InputFilter() {
			@Override
			public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
				for (int i = start; i < end; ++i) {
					if (!Pattern.compile("[1234567890]*").matcher(String.valueOf(source.charAt(i))).matches()) {
						return "";
					}
				}

				return null;
			}
		};

		handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg)
			{
				if(msg.what == 111)
				{
					Log.e("Dr Id > ", "handleMessage: "+msg.obj);

					VariationResponse.VariationsBean getSet = new VariationResponse.VariationsBean();
					getSet.setName("Product");
					getSet.setItem_code("Product");
					getSet.setItem_id_code("Product");
					getSet.setReason("R");
					getSet.setReason_code("R");
					getSet.setStock("1");
					listPending.add(0, getSet);
					listPending.set(0,getSet);
					notifyDataSetChanged();
				}
				return false;
			}
		});
    }
 
    public int getCount() 
    {
        return listPending.size();
    }
 
    public Object getItem(int position)
    {
        return position;
    }
 
    public long getItemId(int position) 
    {
        return position;
    }
    
    public ArrayList<VariationResponse.VariationsBean> getList()
    {
    	return listPending;
    }

	public View getView(final int position, View convertView, ViewGroup parent)
    {
		final ViewHolder holder;
		
        View rowView = convertView;
        if(convertView == null)
        {
    		rowView = inflater.inflate(R.layout.rowview_pendingentry_child, null);
    		
    		holder = new ViewHolder();
    		
    		holder.edtProduct = (EditText) rowView.findViewById(R.id.edtProduct_Rowview_Child);
			holder.edtReason = (EditText) rowView.findViewById(R.id.edtReason_Rowview_Child);
			holder.edtQuantity = (EditText) rowView.findViewById(R.id.edtQuality_Rowview_Child);
			holder.txtReasonTitle = (TextView) rowView.findViewById(R.id.txtReasonTitle);
			holder.llTitle = (LinearLayout)rowView.findViewById(R.id.llTitle);
			holder.llProduct = (LinearLayout)rowView.findViewById(R.id.llProduct);
			holder.img_delete = (ImageView)rowView.findViewById(R.id.img_delete);

    		rowView.setTag(holder);
        }
        else
	    {
	        holder = (ViewHolder) rowView.getTag();
	    }

        holder.ref = position;

		if (holder.ref == 0)
		{
			holder.llTitle.setVisibility(View.VISIBLE);
		}
        else
        {
        	holder.llTitle.setVisibility(View.GONE);
        }

        /*if(isEditable)
		{
			if(position == listPending.size()-1)
			{
				holder.llAdd.setVisibility(View.VISIBLE);
			}
			else
			{
				holder.llAdd.setVisibility(View.GONE);
			}
		}
		else
		{
			holder.llAdd.setVisibility(View.GONE);
		}*/

        if (dbcCode.equals("STK"))
        {
			holder.edtReason.setVisibility(View.GONE);
			holder.txtReasonTitle.setVisibility(View.GONE);
			holder.edtQuantity.setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter(4)});
		}
        else
        {
        	holder.edtReason.setVisibility(View.VISIBLE);
        	holder.txtReasonTitle.setVisibility(View.VISIBLE);
			holder.edtQuantity.setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter(2)});
        }
        
        if (isEditable)
        {
        	holder.edtProduct.setInputType(InputType.TYPE_NULL);
			holder.edtReason.setEnabled(true);
			//holder.edtQuantity.setEnabled(true);

			/*if(listPending.get(position).getReason_code().equalsIgnoreCase("F"))
			{
				holder.edtQuantity.setEnabled(false);
			}
			else
			{
				holder.edtQuantity.setEnabled(true);
			}*/

			if (!dbcCode.equals("STK"))
			{
				holder.edtProduct.setEnabled(true);
				holder.img_delete.setVisibility(View.VISIBLE);
				//holder.llAdd.setVisibility(View.VISIBLE);
				if(listPending.get(position).getReason_code().equalsIgnoreCase("F"))
				{
					holder.edtQuantity.setEnabled(false);
				}
				else
				{
					holder.edtQuantity.setEnabled(true);
				}
			}
			else
			{
				holder.edtProduct.setEnabled(false);
				holder.img_delete.setVisibility(View.GONE);
				holder.edtQuantity.setEnabled(true);
				//holder.llAdd.setVisibility(View.GONE);
			}
		}
        else
        {
        	holder.edtProduct.setEnabled(false);
			holder.edtReason.setEnabled(false);
			holder.edtQuantity.setEnabled(false);
			holder.img_delete.setVisibility(View.GONE);
			//holder.llAdd.setVisibility(View.GONE);
        }
        
        holder.edtProduct.setText(listPending.get(holder.ref).getItem_id_code());

		if (!dbcCode.equals("STK"))
		{
			holder.edtQuantity.setText(String.valueOf(listPending.get(holder.ref).getStock()));
		}
		else
		{
			if(listPending.get(holder.ref).getStock().equalsIgnoreCase(""))
			{
				holder.edtQuantity.setText("0");
			}
			else
			{
				holder.edtQuantity.setText(String.valueOf(listPending.get(holder.ref).getStock()));
			}
		}

        if (listPending.get(holder.ref).getStock().equals("") || listPending.get(holder.ref).getStock().equals("0"))
        {
        	if (!dbcCode.equalsIgnoreCase("stk"))
        	{
        		VariationResponse.VariationsBean getset = listPending.get(holder.ref);
        		
        		getset.setReason("F");
        		
        		listPending.set(holder.ref, getset);
        	}
		}
        
        holder.edtReason.setText(listPending.get(holder.ref).getReason_code());
        
//        Log.e("Quantity and reason in pending entry child list", listPending.get(position).getQuantity() +" "+listPending.get(position).getReason()+" asdf");
        
        try
        {
			holder.edtQuantity.addTextChangedListener(new TextWatcher()
			{
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count)
				{
					VariationResponse.VariationsBean entryItems = listPending.get(holder.ref);
					if (s.toString().equals(""))
					{
						try {
							if (!dbcCode.equals("STK"))
                            {
                                entryItems.setStock("0");
                                entryItems.setReason("Refuse Sample");
                                entryItems.setReason_code("F");
                                entryItems.setReason_id(ApiClient.REFUSE_REASON_ID);
                                holder.edtReason.setText("F");
                                listPending.set(position, entryItems);
                            }
                            else
                            {
                                entryItems.setStock("");
                            }
						}
						catch (Exception e) {
							e.printStackTrace();
						}
					}
					else
					{
						try {
							//Changed by kiran, Change given by Riteshbhai
							entryItems.setStock(s.toString());
							if(Integer.parseInt(s.toString())==0)
                            {
                                entryItems.setReason("Refuse Sample");
                                entryItems.setReason_code("F");
                                entryItems.setReason_id(ApiClient.REFUSE_REASON_ID);
                                holder.edtReason.setText("F");
                            }
                            else
                            {
                                if(entryItems.getProduct_type().equalsIgnoreCase("0"))
                                {
                                    if(!entryItems.getReason_code().equalsIgnoreCase("F"))
                                    {
                                        entryItems.setReason(entryItems.getReason());
                                        entryItems.setReason_code(entryItems.getReason_code());
                                        holder.edtReason.setText(entryItems.getReason_code());
                                    }
                                    else
                                    {
                                        entryItems.setReason("Regular Sample");
                                        entryItems.setReason_code("R");
                                        holder.edtReason.setText("R");
                                    }
                                }
                                else
                                {
                                    entryItems.setReason("Gift Article");
                                    entryItems.setReason_code("G");
                                    holder.edtReason.setText("G");
                                }
                            }
							listPending.set(holder.ref, entryItems);
						}
						catch (NumberFormatException e) {
							e.printStackTrace();
						}
					}

					try {
						listPending.set(holder.ref, entryItems);
						List<NewEntryGetSet> listOffline = NewEntryGetSet.listAll(NewEntryGetSet.class);
						ArrayList<NewEntryGetSet> listUserEntry = new ArrayList<>();
						for (int i = 0; i < listOffline.size(); i++)
                        {
                            if(listOffline.get(i).getUser_id().equals(sessionManager.getUserId()))
                            {
                                listUserEntry.add(listOffline.get(i));
                            }
                        }

						NewEntryGetSet newEntryGetSet = NewEntryGetSet.findById(NewEntryGetSet.class, listUserEntry.get(mainListPos).getId());
						newEntryGetSet.setProducts(AppUtils.getStringFromArrayListVariations(listPending));
						newEntryGetSet.save();
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after)
				{
					
				}
				
				@Override
				public void afterTextChanged(Editable s)
				{

				}
			});
		}
        catch (Exception e)
        {
			e.printStackTrace();
		}
        
        holder.edtProduct.setOnClickListener(new OnClickListener()
        {
			@Override
			public void onClick(View v)
			{
				showDialog("Product",position,holder);
			}
		});
        
        holder.edtReason.setOnClickListener(new OnClickListener()
        {
			
			@Override
			public void onClick(View v)
			{
				if(holder.edtProduct.getText().toString().equalsIgnoreCase("Product") ||
						holder.edtProduct.getText().toString().equalsIgnoreCase("") ||
						listPending.get(position).getProduct_id().equalsIgnoreCase("0"))
				{
					AppUtils.showToast(activity, "Please select product!");
				}
				else
				{
					showDialog("Reason",position,holder);
				}
			}
		});
        
        holder.img_delete.setOnClickListener(new OnClickListener()
        {
			@Override
			public void onClick(View arg0)
			{
				if (listPending.size()>1)
				{
					showConfirmationDialog(position,listPending.get(holder.ref).getName());
				}
				else
				{
					AppUtils.showToast(activity,"You have to enter atleast one entry");
				}
			}
		});

        return rowView;
    }

	public String getApiString()
	{
		String api = "";
		ArrayList<String> listString =  new ArrayList<String>();
		for (int i = 0; i < listPending.size(); i++) 
		{
			String test = listPending.get(i).getName().trim() + " " + listPending.get(i).getStock()+ " " + listPending.get(i).getReason().trim();
			listString.add(test);
		}
		
		String temp = listString + "";
		api = temp.replace("[", "").replace("]", "").replace(",", "");
		
		return api;
	}

	protected void showDialog(final String clickFor, final int mainListPos, final PendingEntryChildAdapter.ViewHolder holder)
	{
		try
		{

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
			TextView btnSubmit = (TextView) dialog.findViewById(R.id.txtSubmitDialog);

			TextInputLayout inputSearch = (TextInputLayout) dialog.findViewById(R.id.inputSearch);
			final EditText edtSearch = (EditText) dialog.findViewById(R.id.edtSearch_Dialog_ListView);

			List<DBVariation> books = DBVariation.listAll(DBVariation.class);
			listVariation = new ArrayList<>();
			for (int i = 0; i < books.size(); i++)
			{
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

			List<DBReason> reasons = DBVariation.listAll(DBReason.class);
			listReason = new ArrayList<>();
			for (int i = 0; i < reasons.size(); i++)
			{
				DBReason bean = reasons.get(i);
				ReasonResponse.ReasonsBean reasonBean = new ReasonResponse.ReasonsBean();
				reasonBean.setReason(bean.getReason());
				reasonBean.setReason_code(bean.getReason_code());
				reasonBean.setReason_id(bean.getReason_id());
				reasonBean.setComment(bean.getComment());
				reasonBean.setTimestamp(bean.getTimestamp());
				listReason.add(reasonBean);
			}

			if(clickFor.equals("Product"))
			{
				inputSearch.setVisibility(View.VISIBLE);
				txtHeader.setText("Select Product");
				productUnitAdapter = new ProductUnitAdapter(listVariation,listReason,dialog,"Product",mainListPos,holder,false);
				listView.setAdapter(productUnitAdapter);
				btnSubmit.setVisibility(View.VISIBLE);

				edtSearch.addTextChangedListener(new TextWatcher() {
					@Override
					public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
					{
						int textlength = edtSearch.getText().length();
						listVariationSearch.clear();
						for (int i = 0; i < listVariation.size(); i++)
						{
							if (listVariation.get(i).getName().toLowerCase().contains(edtSearch.getText().toString().toLowerCase().trim()) ||
									listVariation.get(i).getItem_id_code().toLowerCase().contains(edtSearch.getText().toString().toLowerCase().trim()))
							{
								listVariationSearch.add(listVariation.get(i));
							}
						}

						productUnitAdapter = new ProductUnitAdapter(listVariation,listReason,dialog,"Product",mainListPos,holder,true);
						listView.setAdapter(productUnitAdapter);

					}

					@Override
					public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
					}

					@Override
					public void afterTextChanged(Editable arg0) {
					}
				});
			}
			else if(clickFor.equals("Reason"))
			{
				inputSearch.setVisibility(View.GONE);
				txtHeader.setText("Select Reason");
				productUnitAdapter = new ProductUnitAdapter(listVariation,listReason,dialog,"Reason",mainListPos,holder,false);
				listView.setAdapter(productUnitAdapter);
				btnSubmit.setVisibility(View.INVISIBLE);
			}

			btnSubmit.setVisibility(View.GONE);

			dialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public class ProductUnitAdapter extends BaseAdapter
	{
		private LayoutInflater inflater = null;
		private ArrayList<VariationResponse.VariationsBean> listProducAdapter;
		private ArrayList<ReasonResponse.ReasonsBean> listReasonAdapter;
		private String isFor ="";
		Dialog dialog;
		private int mainListPos = 0;
		boolean isForSearch = false;
		PendingEntryChildAdapter.ViewHolder pendingEntryChildAdapterHolder;

		public ProductUnitAdapter(ArrayList<VariationResponse.VariationsBean> productList,
								  ArrayList<ReasonResponse.ReasonsBean> reasonList,
								  Dialog dialog,
								  String isFor,
								  int mainListPos,
								  PendingEntryChildAdapter.ViewHolder pendingEntryChildAdapterHolder,
								  boolean isForSearch)
		{
			//this.listProducAdapter = productList;
			this.listReasonAdapter = reasonList;
			this.dialog = dialog;
			this.isFor = isFor;
			this.mainListPos = mainListPos;
			this.pendingEntryChildAdapterHolder = pendingEntryChildAdapterHolder;
			this.isForSearch = isForSearch;
			inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			if(isFor.equalsIgnoreCase("Reason"))
			{
				if(listPending.get(mainListPos).getProduct_type().equalsIgnoreCase("1"))
				{
					listReasonAdapter = DataUtils.getReasonsFroGift();
				}
				else
				{
					listReasonAdapter = DataUtils.getReasonsFroNonGift();
				}
			}

			if(isFor.equalsIgnoreCase("Product"))
			{
				this.listProducAdapter = new ArrayList<>();
				if(dbcCode.equalsIgnoreCase("TNS") || dbcCode.equalsIgnoreCase("SRD"))
				{
					for (int i = 0; i < productList.size(); i++) {
						if(productList.get(i).getProduct_type().equalsIgnoreCase("1"))
						{
							this.listProducAdapter.add(productList.get(i));
						}
					}
				}
				else
				{
					this.listProducAdapter = productList;
				}
			}
			else
			{
				this.listProducAdapter = productList;
			}
		}

		public int getCount()
		{
			if(isFor.equalsIgnoreCase("Product"))
			{
				if(isForSearch)
				{
					return listVariationSearch.size();
				}
				else
				{
					return listProducAdapter.size();
				}
			}
			else
			{
				return listReasonAdapter.size();
			}
		}

		public Object getItem(int position)
		{
			return position;
		}

		public long getItemId(int position)
		{
			return position;
		}


		public View getView(final int position, View convertView, final ViewGroup parent)
		{
			final ProductUnitAdapter.ViewHolder holder ;

			if(convertView == null)
			{
				holder = new ProductUnitAdapter.ViewHolder();
				convertView = inflater.inflate(R.layout.rowview_product_unit, null);
				holder.txtProduct = (TextView)convertView.findViewById(R.id.txtProduct);
				holder.txtProductCode = (TextView)convertView.findViewById(R.id.txtProductCode);
				holder.cbProduct = (CheckBox)convertView.findViewById(R.id.cb);
				holder.edtUnit = (EditText)convertView.findViewById(R.id.edtUnit);

				convertView.setTag(holder);
			}
			else
			{
				holder = (ProductUnitAdapter.ViewHolder) convertView.getTag();
			}

			if(isFor.equalsIgnoreCase("Product"))
			{
				final VariationResponse.VariationsBean getSet;
				if(isForSearch)
				{
					getSet= listVariationSearch.get(position);
				}
				else
				{
					getSet= listProducAdapter.get(position);
				}

				holder.cbProduct.setVisibility(View.GONE);
				holder.txtProductCode.setText(getSet.getItem_id_code());
				holder.txtProduct.setText(getSet.getName());
				holder.edtUnit.setVisibility(View.GONE);
				convertView.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						dialog.dismiss();
						updatePendingEntry(isFor,mainListPos,getSet,"","","",pendingEntryChildAdapterHolder);
					}
				});
			}
			else
			{
				holder.cbProduct.setVisibility(View.GONE);
				final ReasonResponse.ReasonsBean getSet = listReasonAdapter.get(position);
				holder.cbProduct.setVisibility(View.GONE);
				holder.txtProductCode.setText(getSet.getReason_code());
				holder.txtProduct.setText(getSet.getReason());

				convertView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v)
					{
						dialog.dismiss();
						updatePendingEntry(isFor,mainListPos,new VariationsBean(),getSet.getReason_code(),getSet.getReason(),getSet.getReason_id(),pendingEntryChildAdapterHolder);
					}
				});

			}


			return convertView;
		}

		public ArrayList<VariationResponse.VariationsBean> listSelected()
		{
			ArrayList<VariationResponse.VariationsBean> listReturn = new ArrayList<VariationResponse.VariationsBean>();

			for (int i = 0; i < listProducAdapter.size(); i++)
			{
				if (listProducAdapter.get(i).isChecked())
				{
					listReturn.add(listProducAdapter.get(i));
				}
			}

			return listReturn;
		}

		public boolean isAllSelected()
		{
			boolean isSelected = true;

			for (int i = 0; i < listProducAdapter.size(); i++)
			{
				if (listProducAdapter.get(i).isChecked())
				{
					if (listProducAdapter.get(i).getStock().equals("") || listProducAdapter.get(i).getStock().equals("0"))
					{
						isSelected = false;
						break;
					}
				}
				else
				{
					isSelected = true;
				}
			}
			return isSelected;
		}

		private class ViewHolder
		{
			TextView txtProduct,txtProductCode;
			EditText edtUnit;
			CheckBox cbProduct;
		}
	}
	
	private void showConfirmationDialog(final int pos,final String productName)
	{
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
			txtHeader2.setText("Do You Want To Delete"+" "+productName+"?");
			
			btnNo.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					dialog.dismiss();
					dialog.cancel();
				}
			});
			
			btnYes.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					try {
						dialog.dismiss();
						dialog.cancel();

						List<NewEntryGetSet> listOffline = NewEntryGetSet.listAll(NewEntryGetSet.class);
						/*Main Pending entry list*/
						ArrayList<NewEntryGetSet> listUserEntry = new ArrayList<>();
						for (int i = 0; i < listOffline.size(); i++)
						{
							if(listOffline.get(i).getUser_id().equals(sessionManager.getUserId()))
							{
								listUserEntry.add(listOffline.get(i));
							}
						}

						ArrayList<VariationsBean> list = AppUtils.getArrayListFromJsonStringVariation(listUserEntry.get(mainListPos).getProducts());
						list.remove(pos);
						listPending.remove(pos);

						/*Update Products list(as a string) in main pending entry */
						NewEntryGetSet newEntryGetSet = NewEntryGetSet.findById(NewEntryGetSet.class, listUserEntry.get(mainListPos).getId());
						newEntryGetSet.setProducts(AppUtils.getStringFromArrayListVariations(list));
						newEntryGetSet.save();
						notifyDataSetChanged();

						//For notify main pending asapter
						if(PendingEntryDialogAdapter.handlerPendingAdapter!=null)
						{
							Message message = Message.obtain();
							message.what = 101;
							message.obj = AppUtils.getStringFromArrayListVariations(listPending);
							message.arg1 = mainListPos;
							PendingEntryDialogAdapter.handlerPendingAdapter.sendMessage(message);
						}

					} catch (Exception e) {
						e.printStackTrace();
						dialog.dismiss();
						dialog.cancel();
					}

				}
			});
			dialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private class ViewHolder
    {
		EditText edtQuantity,edtProduct,edtReason;
		LinearLayout llTitle,llProduct;
		View viewLine;
		ImageView img_delete;
		TextView txtReasonTitle;
		int ref;
    }

    private void updatePendingEntry(String isFor,
									int subPosition,
									VariationsBean variationsBean,
									String reasonCode,
									String reason,
									String reasonId,
									PendingEntryChildAdapter.ViewHolder holder)
	{

		Log.e("var ........ ", "updatePendingEntry: " +variationsBean.getProduct_type() +"\n"+" qty "+listPending.get(subPosition).getStock() +"\n"+"ereason >"+listPending.get(subPosition).getReason_code());


		VariationResponse.VariationsBean bean = listPending.get(subPosition);
		if(isFor.equalsIgnoreCase("Product"))
		{
			/*Check that same product should not select two times*/
			try {
				for (int i = 0; i < listPending.size(); i++)
                {
                    if(listPending.get(i).getItem_id_code().equalsIgnoreCase(variationsBean.getItem_id_code()))
                    {
                        AppUtils.showToast(activity,"Please choose another");
                        return;
                    }
                }

				/*Sub ProductList*/
				bean.setName(variationsBean.getName());
				bean.setItem_code(variationsBean.getItem_code());
				bean.setItem_id_code(variationsBean.getItem_id_code());

				if(variationsBean.getProduct_type().equalsIgnoreCase("1"))
				{
					bean.setReason_code("G");
                    bean.setStock(listPending.get(subPosition).getStock());
				}
				else
				{
					//bean.setReason_code("R");
					if(listPending.get(subPosition).getStock().equalsIgnoreCase("") ||
							listPending.get(subPosition).getStock().equalsIgnoreCase("0"))
					{
						bean.setReason_code("F");
					}
					else
					{
						bean.setReason_code(listPending.get(subPosition).getReason_code());
						bean.setStock(listPending.get(subPosition).getStock());
					}
				}


				//bean.setStock("1");
				bean.setProduct_type(variationsBean.getProduct_type());
				bean.setVariation_id(variationsBean.getVariation_id());



				holder.edtProduct.setText(bean.getItem_id_code());
				listPending.set(subPosition,bean);

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

				NewEntryGetSet newEntryGetSet = NewEntryGetSet.findById(NewEntryGetSet.class, listUserEntry.get(mainListPos).getId());
				newEntryGetSet.setProducts(AppUtils.getStringFromArrayListVariations(listPending));
				newEntryGetSet.save();

				//For notify main pending asapter
				if(PendingEntryDialogAdapter.handlerPendingAdapter!=null)
                {
                    Message message = Message.obtain();
                    message.what = 101;
                    message.obj = AppUtils.getStringFromArrayListVariations(listPending);
                    message.arg1 = mainListPos;
                    PendingEntryDialogAdapter.handlerPendingAdapter.sendMessage(message);
                }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(isFor.equalsIgnoreCase("Reason"))
		{

			bean.setReason(reason);
			bean.setReason_code(reasonCode);
			bean.setReason_id(reasonId);

			if(bean.getReason_code().equalsIgnoreCase("F"))
			{
				try
				{
					bean.setStock("0");
					holder.edtQuantity.setText("0");
					holder.edtQuantity.setEnabled(false);
					holder.edtQuantity.setSelection(bean.getStock().toString().length());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else
			{
			    if(listPending.get(subPosition).getStock().equalsIgnoreCase("") ||
						listPending.get(subPosition).getStock().equalsIgnoreCase("0"))
				{
					bean.setStock("1");
				}
				else
				{
					bean.setStock(listPending.get(subPosition).getStock());
				}

				holder.edtQuantity.setText(bean.getStock());
				holder.edtQuantity.setEnabled(true);
				holder.edtQuantity.setSelection(bean.getStock().toString().length());
			}

			listPending.set(subPosition,bean);
			holder.edtReason.setText(reasonCode);
			//notifyDataSetChanged();

			List<NewEntryGetSet> listOffline = NewEntryGetSet.listAll(NewEntryGetSet.class);
			ArrayList<NewEntryGetSet> listUserEntry = new ArrayList<>();
			for (int i = 0; i < listOffline.size(); i++)
			{
				if(listOffline.get(i).getUser_id().equals(sessionManager.getUserId()))
				{
					listUserEntry.add(listOffline.get(i));
				}
			}

			NewEntryGetSet newEntryGetSet = NewEntryGetSet.findById(NewEntryGetSet.class, listUserEntry.get(mainListPos).getId());
			newEntryGetSet.setProducts(AppUtils.getStringFromArrayListVariations(listPending));
			newEntryGetSet.save();

			if(PendingEntryDialogAdapter.handlerPendingAdapter!=null)
			{
				Message message = Message.obtain();
				message.what = 101;
				message.obj = AppUtils.getStringFromArrayListVariations(listPending);
				message.arg1 = mainListPos;
				PendingEntryDialogAdapter.handlerPendingAdapter.sendMessage(message);
			}
		}

	}
}
