package me.kobeplane.data;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

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

    public TaskboardsData getTaskboardData(String ID) {
        try {
            return taskboardsDataDao.queryForId(ID);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public TaskboardsData addTaskboard(String title, UserData userData) throws SQLException {
        TaskboardsData taskboardsData = new TaskboardsData();
        taskboardsData.setName(title);
        taskboardsData.setUserId(userData);
        taskboardsDataDao.create(taskboardsData);
        return taskboardsData;
    }

    public List<TaskboardsData> getTaskboardsForUser(UserData user) throws SQLException {
        QueryBuilder<TaskboardsData, String> qb = taskboardsDataDao.queryBuilder();
        qb.where().eq("userId_id", user);
        return qb.query();
    }

}

