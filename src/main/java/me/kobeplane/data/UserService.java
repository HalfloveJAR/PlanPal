package me.kobeplane.data;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class UserService {

    private Dao<UserData, String> userDataDao;

    public UserService(ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, UserData.class);
            userDataDao = DaoManager.createDao(connectionSource, UserData.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
