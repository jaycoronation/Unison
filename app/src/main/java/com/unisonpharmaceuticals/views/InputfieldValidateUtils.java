package com.unisonpharmaceuticals.views;

import android.app.Activity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.unisonpharmaceuticals.utils.AppUtils;


public class InputfieldValidateUtils {
	
	/**
     * to request focus of edittext and open virtual keyboard
     * @param view edittext
     */
    public static void requestFocus(View view, final Activity activity)
    {
        try {
			if (view.requestFocus())
			{
			    activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
			}
        } catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
     * validate if edittext is empty, show error
     * @param editText
     * @param inputLayout
     * @param error error message to show user
     * @return true is valid else false
     */
    public static boolean validateEmpty(final Activity activity, final EditText editText, final TextInputLayout inputLayout, final String error)
    {
        if (editText.getText().toString().trim().length() == 0)
        {
        	inputLayout.setError(error);
            requestFocus(editText, activity);
            return false;
        }
        else
        {
        	inputLayout.setErrorEnabled(false);
        }

        return true;
    }

    public static boolean validateCharacterOnly(final Activity activity, final EditText editText, final TextInputLayout inputLayout, final String error)
    {
        if (!AppUtils.isAlpha(editText.getText().toString().trim().replace(" ", "")))
        {
            inputLayout.setError(error);
            requestFocus(editText, activity);
            return false;
        }
        else
        {
            inputLayout.setErrorEnabled(false);
        }

        return true;
    }

    public static boolean validatePassword(final Activity activity, final EditText editText, final TextInputLayout inputLayout, final String error, final String errorInvalid)
    {
        if (editText.getText().toString().trim().length() == 0)
        {
            inputLayout.setError(error);
            requestFocus(editText, activity);
            return false;
        }
        else if (!AppUtils.validPassword(editText.getText().toString().trim()))
        {
            inputLayout.setError(errorInvalid);
            requestFocus(editText, activity);
            return false;
        }
        else
        {
            inputLayout.setErrorEnabled(false);
        }

        return true;
    }

    public static boolean validateAge(final Activity activity, final EditText editText, final TextInputLayout inputLayout, final String error, final String errorInvalid)
    {
        if (editText.getText().toString().trim().length() == 0)
        {
            inputLayout.setError(error);
            requestFocus(editText, activity);
            return false;
        }
        else if (Integer.parseInt(editText.getText().toString().trim()) > 19)
        {
            inputLayout.setError(errorInvalid);
            requestFocus(editText, activity);
            return false;
        }
        else
        {
            inputLayout.setErrorEnabled(false);
        }

        return true;
    }
    
    /**
     * validate if old password is same as new one, both must be different
     * @param edtOldPwd
     * @param edtNewPwd
     * @param inputLayout
     * @return true if valid else false
     */
    public static boolean validateNewPassword(final Activity activity, final EditText edtOldPwd, final EditText edtNewPwd, final TextInputLayout inputLayout, final String errorEmpty, final String errorInvalid, final String errorDifferent)
    {
    	if (edtNewPwd.getText().toString().trim().length() == 0)
        {
        	inputLayout.setError(errorEmpty);
            requestFocus(edtNewPwd, activity);
            return false;
        }
        else if (!AppUtils.validPassword(edtNewPwd.getText().toString().trim()))
        {
            inputLayout.setError(errorInvalid);
            requestFocus(edtNewPwd, activity);
            return false;
        }
        else if (edtOldPwd.getText().toString().trim().equals(edtNewPwd.getText().toString().trim()))
        {
        	inputLayout.setError(errorDifferent);
            requestFocus(edtNewPwd, activity);
            return false;
        }
        else
        {
        	inputLayout.setErrorEnabled(false);
        }

        return true;
    }
    
    /**
     * validate if new password is same as confirm password, both muse be same
     * @param edtNewPwd
     * @param edtConfirmPwd
     * @param inputLayout
     * @param error error message to show user
     * @return true if valid else false 
     */
    public static boolean validateConfirmPassword(final Activity activity, final EditText edtNewPwd, final EditText edtConfirmPwd, final TextInputLayout inputLayout, final String errorEmpty, final String errorInvalid, final String errorSame)
    {
    	if (edtConfirmPwd.getText().toString().trim().length() == 0)
        {
        	inputLayout.setError(errorEmpty);
            requestFocus(edtConfirmPwd, activity);
            return false;
        }
        else if (!AppUtils.validPassword(edtConfirmPwd.getText().toString().trim()))
        {
            inputLayout.setError(errorInvalid);
            requestFocus(edtConfirmPwd, activity);
            return false;
        }
    	else if (!edtNewPwd.getText().toString().trim().equals(edtConfirmPwd.getText().toString().trim()))
        {
        	inputLayout.setError(errorSame);
            requestFocus(edtConfirmPwd, activity);
            return false;
        }
        else
        {
        	inputLayout.setErrorEnabled(false);
        }

        return true;
    }
    
    /**
     * validate entered email address
     * @param edtEmail
     * @param inputLayout
     * @param error error message to show user
     * @return true if valid else false
     */
    public static boolean validateEmail(final Activity activity, final EditText edtEmail, final TextInputLayout inputLayout, final String errorEmpty, final String errorInvalid)
    {
    	if (edtEmail.getText().toString().trim().length() == 0)
        {
        	inputLayout.setError(errorEmpty);
            requestFocus(edtEmail, activity);
            return false;
        }
    	else if (!AppUtils.validateEmail(edtEmail.getText().toString().trim()))
        {
        	inputLayout.setError(errorInvalid);
            requestFocus(edtEmail, activity);
            return false;
        }
        else
        {
        	inputLayout.setErrorEnabled(false);
        }

        return true;
    }

    public static boolean validateName(final Activity activity, final EditText edtName, final TextInputLayout inputLayout, final String errorEmpty, final String errorInvalid)
    {
        if (edtName.getText().toString().trim().length() == 0)
        {
            inputLayout.setError(errorEmpty);
            requestFocus(edtName, activity);
            return false;
        }
        else if (!AppUtils.isAlpha(edtName.getText().toString().trim().replace(" ", "")))
        {
            inputLayout.setError(errorInvalid);
            requestFocus(edtName, activity);
            return false;
        }
        else
        {
            inputLayout.setErrorEnabled(false);
        }

        return true;
    }

    public static boolean validateEmptyAutoComplete(final Activity activity, final AutoCompleteTextView editText, final TextInputLayout inputLayout, final String error)
    {
        if (editText.getText().toString().trim().length() == 0)
        {
            inputLayout.setError(error);
            requestFocus(editText, activity);
            return false;
        }
        else
        {
            inputLayout.setErrorEnabled(false);
        }

        return true;
    }
}
