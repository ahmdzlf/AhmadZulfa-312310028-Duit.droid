package com.azarcorp.duitdroid.AddPengeluaran;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.azarcorp.duitdroid.database.DataBaseClient;
import com.azarcorp.duitdroid.database.DataBaseDao;
import com.azarcorp.duitdroid.model.ModelDatabase;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddPengeluaranViewModel extends AndroidViewModel {

    private DataBaseDao databaseDao;

    public AddPengeluaranViewModel(@NonNull Application application) {
        super(application);

        databaseDao = DataBaseClient.getInstance(application).getAppDatabase().databaseDao();
    }

    public void addPengeluaran(final String type, final String note, final String date, final int price, final String fotoUri) {
        Completable.fromAction(new Action() {
                    @Override
                    public void run() throws Exception {
                        ModelDatabase pengeluaran = new ModelDatabase();
                        pengeluaran.tipe = type;
                        pengeluaran.keterangan = note;
                        pengeluaran.tanggal = date;
                        pengeluaran.jmlUang = price;
                        pengeluaran.fotoUri = fotoUri;
                        databaseDao.insertPengeluaran(pengeluaran);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void updatePengeluaran(final int uid, final String note, final String date, final int price, final String fotoUri) {
        Completable.fromAction(new Action() {
                    @Override
                    public void run() throws Exception {
                        databaseDao.updateDataPengeluaran(note, date, price, fotoUri, uid);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
