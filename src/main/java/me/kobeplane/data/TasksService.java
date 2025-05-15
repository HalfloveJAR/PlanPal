package me.kobeplane.data;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class TasksService {

    private Dao<TasksData, String> tasksDataDao;

    public TasksService(ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, TasksData.class);
            tasksDataDao = DaoManager.createDao(connectionSource, TasksData.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
