package com.zahid.coolshoptest.managers;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.google.gson.Gson;
import com.zahid.coolshoptest.model.AccountModel;
import com.zahid.coolshoptest.model.SessionModel;

import static com.zahid.coolshoptest.utils.Constants.AccountModeTAG;
import static com.zahid.coolshoptest.utils.Constants.SessionModelTAG;


public class PersistenceManager {

    Context context;
    SharedPreferences preference;

    public PersistenceManager(Context context) {
        this.context = context;
        this.preference = PreferenceManager.getDefaultSharedPreferences(context);

    }
    public void saveLoggedInAccount(AccountModel account) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(account);
        preference.edit().putString(AccountModeTAG, jsonStr).apply();
    }

    public boolean isSessionAvaible() {
        return preference.contains(SessionModelTAG);
    }

    public AccountModel getLoggedInUser() {
        Gson gson = new Gson();
        return gson.fromJson(preference.getString(AccountModeTAG, null), AccountModel.class);
    }

    public void updateImagUrl(AccountModel accountModel){
        AccountModel accountModelnew = getLoggedInUser();
        accountModelnew.setAvatarUrl(accountModel.getAvatarUrl());
        saveLoggedInAccount(accountModelnew);
    }

    public void updateEmail(AccountModel accountModel){
        AccountModel accountModelnew = getLoggedInUser();
        accountModelnew.setEmail(accountModel.getEmail());
        saveLoggedInAccount(accountModelnew);
    }


    public void saveSession(SessionModel sessionModel) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(sessionModel);
        preference.edit().putString(SessionModelTAG, jsonStr).apply();
    }
    public SessionModel getSession() {
        Gson gson = new Gson();
        return gson.fromJson(preference.getString(SessionModelTAG, null), SessionModel.class);
    }
    public void removeSession() {
        preference.edit().remove(SessionModelTAG).apply();
    }

}
