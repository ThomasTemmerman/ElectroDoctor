package be.ucll.electrodoctor;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import be.ucll.electrodoctor.dao.UserDao;
import be.ucll.electrodoctor.entity.User;

@Database(entities = {User.class}, version = 2, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatbase(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    INSTANCE.clearAllTables();
                    User user = new User();
                    user.setUserName("test");
                    user.setPassword("test");
                    INSTANCE.userDao().insertUser(user);
                }
            });
        }
    };
}
