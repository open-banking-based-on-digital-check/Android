package com.example.loginactivity;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class EndorsementApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("Endorsement.realm")
                .schemaVersion(0)
                .build();

        Realm.setDefaultConfiguration(config);
    }

    public void EndorsementApplication() {
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("Endorsement.realm")
                .schemaVersion(0)
                .build();

        Realm.setDefaultConfiguration(config);
    }

    public void setEndorsement(String userId, Data data) {

        //앱에 설치된 Realm파일을 찾아서 가져오는 코드
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("Endorsement.realm")
                .schemaVersion(0)
                .build();

        Realm.setDefaultConfiguration(config);
        Realm realm = Realm.getDefaultInstance();
        final EndorsementDB EndorsementDB = new EndorsementDB(userId, data.getStrEnrollment());

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //Realm에 생성한 다이어리를 저장하는 코드
                realm.copyToRealm(EndorsementDB);
            }
        });
    }

    public EndorsementDB getEndorsement(String userId) {

        //앱에 설치된 Realm파일을 찾아서 가져오는 코드
        Realm realm = Realm.getDefaultInstance();
        EndorsementDB EndorsementDB = realm.where(EndorsementDB.class).equalTo("userId", userId).findFirst();

        return EndorsementDB;
    }
}
