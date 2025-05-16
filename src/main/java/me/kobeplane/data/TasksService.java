package me.kobeplane.data;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

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

    public TasksData addTask(String title, String priority, boolean done, Date dueDate, TaskboardsData taskboardsData) throws SQLException {
        TasksData tasksData = new TasksData();
        tasksData.setName(title);
        tasksData.setPriority(priority);
        tasksData.setDone(done);
        tasksData.setDueDate(dueDate);
        tasksData.setTaskboardId(taskboardsData);
        tasksDataDao.create(tasksData);
        return tasksData;
    }

    public void createOrUpdateTask(TasksData task) throws SQLException {
        tasksDataDao.createOrUpdate(task);
    }

    public List<TasksData> getTasksForTaskboard(TaskboardsData taskboard) throws SQLException {
        QueryBuilder<TasksData, String> qb = tasksDataDao.queryBuilder();
        qb.where().eq("taskboardId_id", taskboard);
        return qb.query();
    }

}
