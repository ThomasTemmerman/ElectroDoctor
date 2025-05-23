package be.ucll.electrodoctor;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import be.ucll.electrodoctor.dao.UserDao;
import be.ucll.electrodoctor.dao.WorkOrderDao;
import be.ucll.electrodoctor.entity.User;
import be.ucll.electrodoctor.entity.WorkOrder;

@Database(entities = {User.class, WorkOrder.class}, version = 4, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract WorkOrderDao workOrderDao();
    private static volatile AppDatabase INSTANCE;
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static AppDatabase getDatbase(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_database")
                            .addCallback(sRoomDatabaseCallback)
                            //.fallbackToDestructiveMigration()
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

            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    INSTANCE.clearAllTables();
                    User user = new User();
                    user.setUserName("test");
                    user.setPassword("test");
                    long userId = INSTANCE.userDao().insertUserWithId(user);

                    WorkOrder wo = new WorkOrder();
                    wo.setUserId(userId);
                    wo.setCity("Gent");
                    wo.setCustomerName("Doe");
                    wo.setDevice("Microwave");
                    wo.setProblemCode("12");
                    wo.setProcessed(false);
                    INSTANCE.workOrderDao().insertWorkOrder(wo);
                    WorkOrder wo2 = new WorkOrder();
                    wo2 = new WorkOrder();
                    wo2.setUserId(userId);
                    wo2.setCity("Brussel");
                    wo2.setCustomerName("Jane");
                    wo2.setDevice("Oven");
                    wo2.setProblemCode("14D");
                    wo2.setProcessed(false);
                    INSTANCE.workOrderDao().insertWorkOrder(wo2);
                    WorkOrder wo3 = new WorkOrder();
                    wo3 = new WorkOrder();
                    wo3.setUserId(userId);
                    wo3.setCity("Leuven");
                    wo3.setCustomerName("Dove");
                    wo3.setDevice("TV");
                    wo3.setProblemCode("69");
                    wo3.setProcessed(false);
                    INSTANCE.workOrderDao().insertWorkOrder(wo3);
                    WorkOrder wo4 = new WorkOrder();
                    wo4 = new WorkOrder();
                    wo4.setUserId(userId);
                    wo4.setCity("Brussel");
                    wo4.setCustomerName("Jeff");
                    wo4.setDevice("Dishwasher");
                    wo4.setProblemCode("12");
                    wo4.setProcessed(false);
                    INSTANCE.workOrderDao().insertWorkOrder(wo4);
                    WorkOrder wo5 = new WorkOrder();
                    wo5 = new WorkOrder();
                    wo5.setUserId(userId);
                    wo5.setCity("Gent");
                    wo5.setCustomerName("Jane");
                    wo5.setDevice("Oven");
                    wo5.setProblemCode("5");
                    wo5.setProcessed(false);
                    INSTANCE.workOrderDao().insertWorkOrder(wo5);
                }
            });
        }
    };
}
