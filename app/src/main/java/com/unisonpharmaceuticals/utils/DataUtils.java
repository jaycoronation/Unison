package com.unisonpharmaceuticals.utils;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.unisonpharmaceuticals.classes.SessionManager;
import com.unisonpharmaceuticals.model.NewEntryGetSet;
import com.unisonpharmaceuticals.model.ReasonResponse;
import com.unisonpharmaceuticals.model.VariationResponse;
import com.unisonpharmaceuticals.model.for_sugar.DBReason;
import com.unisonpharmaceuticals.model.for_sugar.DBVariation;
import com.unisonpharmaceuticals.network.ApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Kiran Patel on 26-Sep-18.
 */
public class DataUtils
{
    public static String getJsonStringFromPendingEntry(SessionManager sessionManager,ArrayList<NewEntryGetSet> list)
    {
        String jsonString = "";

        try {
            JSONArray mainArray = new JSONArray();
            for (int i = 0; i < list.size(); i++)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject();

                    jsonObject.put("area_id",list.get(i).getArea_id());
                    jsonObject.put("speciality_id",list.get(i).getSpeciality_id());
                    jsonObject.put("report_type",list.get(i).getReport_type());

                    if(list.get(i).getReport_type().equals("NCR"))
                    {
                        jsonObject.put("doctor",list.get(i).getNCRDrData());
                    }

                    if(list.get(i).isWorkWithSelf())
                    {
                        jsonObject.put("work_with_self",true);
                    }
                    else
                    {
                        jsonObject.put("work_with_self",false);

                        List<String> workWithIds = Arrays.asList(list.get(i).getWork_with_id().split(","));
                        JSONArray workArray = new JSONArray();
                        for (int j = 0; j < workWithIds.size(); j++)
                        {
                            if(workWithIds.get(j).trim().length() != 0)
                            {
                                workArray.put(Integer.parseInt(workWithIds.get(j)));
                            }
                        }

                        jsonObject.put("work_with",workArray);

                    }
                    jsonObject.put("visit_time",list.get(i).getDdtdate());
                    jsonObject.put("doctor_id",list.get(i).getDr_id());
                    jsonObject.put("remark",list.get(i).getRemark());

                    jsonObject.put("adv_employee",list.get(i).getEmpId());
                    jsonObject.put("advDate",list.get(i).getAdvDate());
                    jsonObject.put("advice",list.get(i).getAdvice());

                    jsonObject.put("no_of_internee",list.get(i).getInternee());
                    jsonObject.put("is_new_cycle",list.get(i).getNew_cycle());
                    jsonObject.put("staff_id",sessionManager.getUserId());

                    ArrayList<VariationResponse.VariationsBean> newList = new ArrayList<>();
                    List<DBVariation> mainProductList = DBVariation.listAll(DBVariation.class);
                    ArrayList<VariationResponse.VariationsBean> listProducts = AppUtils.getArrayListFromJsonStringVariation(list.get(i).getProducts());

                    for (int j = 0; j < mainProductList.size(); j++)
                    {
                        VariationResponse.VariationsBean variationsBean = new VariationResponse.VariationsBean();
                        variationsBean.setProduct_id(mainProductList.get(j).getProduct_id());
                        variationsBean.setVariation_id(mainProductList.get(j).getVariation_id());
                        variationsBean.setName(mainProductList.get(j).getName());
                        variationsBean.setItem_code(mainProductList.get(j).getItem_code());
                        variationsBean.setItem_id_code(mainProductList.get(j).getItem_id_code());
                        variationsBean.setReason(mainProductList.get(j).getReason());
                        variationsBean.setReason_code(mainProductList.get(j).getReason_code());
                        variationsBean.setChecked(mainProductList.get(j).isChecked());
                        variationsBean.setStock(String.valueOf(mainProductList.get(j).getStock()));
                        variationsBean.setReason_id(mainProductList.get(j).getReason_id());
                        for (int k = 0; k < listProducts.size(); k++)
                        {
                            if(listProducts.get(k).getVariation_id().equalsIgnoreCase(mainProductList.get(j).getVariation_id()))
                            {
                                variationsBean.setStock(listProducts.get(k).getStock());
                                variationsBean.setReason_id(listProducts.get(k).getReason_id());
                                variationsBean.setReason(listProducts.get(k).getReason());
                                variationsBean.setReason_code(listProducts.get(k).getReason_code());
                            }
                            else
                            {
                                continue;
                            }
                        }
                        newList.add(variationsBean);

                    }


                    JSONArray productArray = new JSONArray();
                    JSONArray reasonArray = new JSONArray();
                    for (int j = 0; j < newList.size(); j++)
                    {
                        JSONObject proObject = new JSONObject();
                        JSONObject reasonObject = new JSONObject();
                        proObject.put(newList.get(j).getVariation_id(),newList.get(j).getStock());
                        //reasonObject.put(newList.get(j).getVariation_id(),newList.get(j).getReason_id());
                        reasonObject.put(newList.get(j).getVariation_id(),getIDFromReasonCode(newList.get(j).getReason_code()));
                        productArray.put(proObject);
                        reasonArray.put(reasonObject);
                    }

                    jsonObject.put("products",productArray);
                    jsonObject.put("reasons",reasonArray);


                    if(list.get(i).getFocusProducts().length()>0)
                    {
                        List<String> items = new ArrayList<>();
                        items = Arrays.asList(list.get(i).getFocusProducts().split(","));
                        String[] tempAry = list.get(i).getFocusProducts().split(",");

                        if (tempAry != null && tempAry.length > 0)
                        {
                            items = new ArrayList<>();

                            for (int l = 0; l < tempAry.length; l++)
                            {
                                String[] temp = tempAry[l].split("#");
                                items.add(temp[1]);
                            }
                        }

                        //getView: ALL---New
                        /*For Get Id From VariationCode*/
                        ArrayList<String> listWithVariationCode = new ArrayList<>();//List Of Code
                        ArrayList<String> listTypes = new ArrayList<>();//List of type= New and Enhance
                        if(items.size()>0)
                        {
                            for (int j = 0; j < items.size(); j++)
                            {
                                String[] prAry = items.get(j).split("---");
                                listWithVariationCode.add(prAry[0]);
                                listTypes.add(prAry[1]);
                            }
                        }

                        for (int k = 0; k < listWithVariationCode.size(); k++)
                        {
                            String[] arrColon = listWithVariationCode.get(k).split(":");
                            listWithVariationCode.set(k,arrColon[0].toString().trim());
                        }

                        //Check this list
                        /*For added sales in newList for get sales products id for focus for*/
                        List<VariationResponse.VariationsBean> tempList = new ArrayList<>();
                        tempList.clear();
                        tempList.addAll(newList);
                        tempList.addAll(DataUtils.getSalesProduct(sessionManager));

                        try {
                            for (int l = 0; l < tempList.size(); l++)
                            {
                                for (int j = 0; j < listWithVariationCode.size(); j++)
                                {
                                    if(tempList.get(l).getItem_id_code().equalsIgnoreCase(listWithVariationCode.get(j)))
                                    {
                                        listWithVariationCode.remove(j);
                                        listWithVariationCode.add(tempList.get(l).getVariation_id());
                                    }
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }



                        /*try {
                            for (int l = 0; l < newList.size(); l++)
                            {
                                for (int j = 0; j < listWithVariationCode.size(); j++)
                                {
                                    if(newList.get(l).getItem_id_code().equalsIgnoreCase(listWithVariationCode.get(j)))
                                    {
                                        listWithVariationCode.remove(j);
                                        listWithVariationCode.add(newList.get(l).getVariation_id());
                                    }
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }*/

                        if(listWithVariationCode.size()==1)
                        {
                            jsonObject.put("focusForProduct1",listWithVariationCode.get(0));
                            jsonObject.put("focusForItemType1",listTypes.get(0));

                            jsonObject.put("focusForProduct2","");
                            jsonObject.put("focusForItemType2","");
                        }
                        else if(listWithVariationCode.size()==2)
                        {
                            jsonObject.put("focusForProduct1",listWithVariationCode.get(0));
                            jsonObject.put("focusForItemType1",listTypes.get(0));

                            jsonObject.put("focusForProduct2",listWithVariationCode.get(1));
                            jsonObject.put("focusForItemType2",listTypes.get(1));
                        }
                    }
                    else
                    {
                        jsonObject.put("focusForProduct1","");
                        jsonObject.put("focusForItemType1","");

                        jsonObject.put("focusForProduct2","");
                        jsonObject.put("focusForItemType2","");
                    }

                    mainArray.put(i,jsonObject);

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

                jsonString = mainArray.toString();

            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return jsonString;
    }

    public static ArrayList<ReasonResponse.ReasonsBean> getReasonsFroGift()
    {
        ArrayList<ReasonResponse.ReasonsBean> list = new ArrayList<>();
        List<DBReason> reasons = DBReason.listAll(DBReason.class);
        for (int i = 0; i < reasons.size(); i++)
        {
            try {
                if(reasons.get(i).getReason_code().equalsIgnoreCase("G") ||
                        reasons.get(i).getReason_code().equalsIgnoreCase("F"))
                {
                    ReasonResponse.ReasonsBean bean = new ReasonResponse.ReasonsBean();
                    bean.setReason_code(reasons.get(i).getReason_code());
                    bean.setReason(reasons.get(i).getReason());
                    bean.setReason_id(reasons.get(i).getReason_id());
                    list.add(bean);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return  list;
    }

    public static ArrayList<ReasonResponse.ReasonsBean> getReasonsFroNonGift()
    {
        ArrayList<ReasonResponse.ReasonsBean> list = new ArrayList<>();
        List<DBReason> reasons = DBReason.listAll(DBReason.class);
        for (int i = 0; i < reasons.size(); i++)
        {
            try
            {
                if(!reasons.get(i).getReason_code().equalsIgnoreCase("G"))
                {
                    ReasonResponse.ReasonsBean bean = new ReasonResponse.ReasonsBean();
                    bean.setReason_code(reasons.get(i).getReason_code());
                    bean.setReason(reasons.get(i).getReason());
                    bean.setReason_id(reasons.get(i).getReason_id());
                    list.add(bean);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return  list;
    }

    public static ArrayList<NewEntryGetSet> listUserEntries(SessionManager sessionManager)
    {
        ArrayList<NewEntryGetSet> list = new ArrayList<>();
        try {
            List<NewEntryGetSet> books = NewEntryGetSet.listAll(NewEntryGetSet.class);
            for (int i = 0; i < books.size(); i++)
            {
                if(books.get(i).getUser_id().equals(sessionManager.getUserId()))
                {
                    list.add(books.get(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static String getIDFromReasonCode(String reasonCode)
    {
        String newReasonCode = "";
        if(reasonCode.equalsIgnoreCase(""))
        {
            return ApiClient.SAMPLE_REASON_ID;
        }
        else
        {
            List<DBReason> reasons = DBReason.listAll(DBReason.class);
            for (int i = 0; i < reasons.size(); i++)
            {
                if(reasons.get(i).getReason_code().equalsIgnoreCase(reasonCode))
                {
                    newReasonCode = reasons.get(i).getReason_id();
                }
            }
            return newReasonCode;
        }
    }

    public static List<VariationResponse.VariationsBean> getSalesProduct(SessionManager sessionManager)
    {
        List<VariationResponse.VariationsBean> list = new ArrayList<>();
        try {
            Gson gson = new Gson();
            list=gson.fromJson(sessionManager.getOnlySalesProduct(), new TypeToken<List<VariationResponse.VariationsBean>>(){}.getType());
        }
        catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return list;
    }
}
