package me.kobeplane.data;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class TaskboardsService {

    private Dao<TaskboardsData, String> taskboardsDataDao;

    public TaskboardsService(ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, TaskboardsData.class);
            taskboardsDataDao = DaoManager.createDao(connectionSource, TaskboardsData.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}

