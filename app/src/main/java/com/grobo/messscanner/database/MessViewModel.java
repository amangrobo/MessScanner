package com.grobo.messscanner.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

public class MessViewModel extends AndroidViewModel {

    private MessDao messDao;

    public MessViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getDatabase(application);
        messDao = db.messDao();
    }

    public List<MessModel> getAllUsers() {
        loadAllUsersTask task = new loadAllUsersTask(messDao);
        try {
            return task.execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insert(MessModel messModel) {
        new insertAsyncTask(messDao).execute(messModel);
    }

    public void deleteAll() {
        new DeleteAllTask(messDao).execute();
    }

    public MessModel getUserByToken(String id) {
        LoadUserByTokenTask task = new LoadUserByTokenTask(messDao);
        try {
            return task.execute(id).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getUserCount() {
        userCountTask task = new userCountTask(messDao);
        try {
            return task.execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static class loadAllUsersTask extends AsyncTask<Void, Void, List<MessModel>> {
        private MessDao mAsyncTaskDao;
        loadAllUsersTask(MessDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected List<MessModel> doInBackground(Void... params) {
            return mAsyncTaskDao.loadAllUsers();
        }
    }

    private static class insertAsyncTask extends AsyncTask<MessModel, Void, Void> {
        private MessDao mAsyncTaskDao;
        insertAsyncTask(MessDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final MessModel... params) {
            mAsyncTaskDao.insertUser(params[0]);
            return null;
        }
    }

    private static class LoadUserByTokenTask extends AsyncTask<String, Void, MessModel> {
        private MessDao mAsyncTaskDao;
        LoadUserByTokenTask(MessDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected MessModel doInBackground(String... params) {
            return mAsyncTaskDao.loadUserByToken(params[0]);
        }
    }

    private static class userCountTask extends AsyncTask<Void, Void, Integer> {
        private MessDao mAsyncTaskDao;
        userCountTask(MessDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            return mAsyncTaskDao.getUserCount();
        }
    }

    private static class DeleteAllTask extends AsyncTask<Void, Void, Void> {
        private MessDao mAsyncTaskDao;
        DeleteAllTask(MessDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... params) {
            mAsyncTaskDao.deleteAllUsers();
            return null;
        }
    }

}
