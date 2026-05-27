package java.ua.kpi.charity.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.ua.kpi.charity.dao.CampaignDao;
import java.ua.kpi.charity.entity.Campaign;

@Database(entities = {Campaign.class}, version = 1)
public abstract class CharityDatabase extends RoomDatabase {

    private static CharityDatabase INSTANCE;

    public abstract CampaignDao campaignDao();


    public static synchronized CharityDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CharityDatabase.class, "charity_db")
                    .fallbackToDestructiveMigration(true)
                    .build();
        }
        return INSTANCE;
    }
}