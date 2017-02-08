package com.tianxiabuyi.sports_medicine.question.util;

import com.tianxiabuyi.sports_medicine.model.QuesHistory;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2016/9/9.
 */
public class QHistoryDbUtil {
    private static QHistoryDbUtil ourInstance = new QHistoryDbUtil();
    private final DbManager db;

    public static QHistoryDbUtil getInstance() {
        return ourInstance;
    }

    private QHistoryDbUtil() {
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName("sport_medicine")
                .setDbVersion(1);
        db = x.getDb(daoConfig);
    }

    public List<QuesHistory> queryAll() {
        try {
            return db.selector(QuesHistory.class).orderBy("id",true).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void save(String text) {
        if (query(text) == null) {
            QuesHistory history = new QuesHistory();
            history.setContent(text);
            try {
                db.save(history);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    public QuesHistory query(String text) {
        try {
            return db.selector(QuesHistory.class).where("content", "=", text).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteById(long id) {
        try {
            db.deleteById(QuesHistory.class,id);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        try {
            db.delete(QuesHistory.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
