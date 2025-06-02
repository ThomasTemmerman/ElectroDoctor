package be.ucll.electrodoctor.core;

import android.content.Context;

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

@Database(entities = {User.class, WorkOrder.class}, version = 2, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract WorkOrderDao workOrderDao();
    private static volatile AppDatabase INSTANCE;
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_database")
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            executorService.execute(new Runnable() {
                @Override
                public void run() {
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
                    wo.setDetailedProblemDescription("Mijn microgolfoven werkt niet meer!");
                    wo.setProcessed(false);
                    INSTANCE.workOrderDao().insertWorkOrder(wo);
                    WorkOrder wo2;
                    wo2 = new WorkOrder();
                    wo2.setUserId(userId);
                    wo2.setCity("Brussel");
                    wo2.setCustomerName("Jane");
                    wo2.setDevice("Oven");
                    wo2.setProblemCode("14D");
                    wo2.setDetailedProblemDescription("Mijn oven is ontploft!");
                    wo2.setProcessed(false);
                    INSTANCE.workOrderDao().insertWorkOrder(wo2);
                    WorkOrder wo3;
                    wo3 = new WorkOrder();
                    wo3.setUserId(userId);
                    wo3.setCity("Leuven");
                    wo3.setCustomerName("Dove");
                    wo3.setDevice("TV");
                    wo3.setDetailedProblemDescription("Mijn TV heeft geen signaal meer!");
                    wo3.setProblemCode("69");
                    wo3.setProcessed(false);
                    INSTANCE.workOrderDao().insertWorkOrder(wo3);
                    WorkOrder wo4;
                    wo4 = new WorkOrder();
                    wo4.setUserId(userId);
                    wo4.setCity("Brussel");
                    wo4.setCustomerName("Jeff");
                    wo4.setDevice("Dishwasher");
                    wo4.setProblemCode("12");
                    wo4.setProcessed(false);
                    INSTANCE.workOrderDao().insertWorkOrder(wo4);
                    WorkOrder wo5;
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
