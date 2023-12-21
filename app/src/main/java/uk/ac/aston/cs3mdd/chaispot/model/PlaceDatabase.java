package uk.ac.aston.cs3mdd.chaispot.model;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import uk.ac.aston.cs3mdd.chaispot.MainActivity;
import uk.ac.aston.cs3mdd.chaispot.MyApplication;


@Database(entities = {Reviews.class, Places.class}, version = 4, exportSchema = false)
public abstract class PlaceDatabase extends RoomDatabase {

    public static PlaceDatabase instance;
    public abstract ReviewDao reviewDao();
    public abstract PlaceDao placeDao();

    public static PlaceDatabase getInstance(Context context) {
        if(instance == null){
            Log.i(MainActivity.TAG, "Create a brand new place database instance");
            instance = Room.databaseBuilder(context, PlaceDatabase.class, "places_db")
                    .fallbackToDestructiveMigration()  // This will recreate the database if migrations fail
                    .build();
        }
        return instance;
    }

    public static Context getAppContext(){
        return MyApplication.getAppContext();
    }
}
