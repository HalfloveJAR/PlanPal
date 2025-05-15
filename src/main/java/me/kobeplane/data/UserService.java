package me.kobeplane.data;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

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

    public UserData getUserData(String email) {
        try {
            return userDataDao.queryBuilder().where().eq("email", email).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public UserData addUser(String email) throws SQLException {
        UserData userData = new UserData();
        userData.setEmail(email);
        userDataDao.create(userData);
        return userData;
    }

}
