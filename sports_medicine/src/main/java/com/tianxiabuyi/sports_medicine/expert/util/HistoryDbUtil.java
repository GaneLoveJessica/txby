package com.tianxiabuyi.sports_medicine.expert.util;

import com.tianxiabuyi.sports_medicine.model.ExpertHistory;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2016/9/9.
 */
public class HistoryDbUtil {
    private static HistoryDbUtil ourInstance = new HistoryDbUtil();
    private final DbManager db;

    public static HistoryDbUtil getInstance() {
        return ourInstance;
    }

    private HistoryDbUtil() {
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName("sport_medicine")
                .setDbVersion(1);
        db = x.getDb(daoConfig);
    }

    public List<ExpertHistory> queryAll() {
        try {
          return db.selector(ExpertHistory.class).orderBy("id",true).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void save(String text) {
        if (query(text) == null) {
            ExpertHistory history = new ExpertHistory();
            history.setContent(text);
            try {
                db.save(history);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    public ExpertHistory query(String text) {
        try {
            return db.selector(ExpertHistory.class).where("content", "=", text).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteById(long id) {
        try {
            db.deleteById(ExpertHistory.class,id);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        try {
            db.delete(ExpertHistory.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
