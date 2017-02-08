package com.tianxiabuyi.sports_medicine.question.util;

import com.tianxiabuyi.sports_medicine.model.CommunityHistory;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2016/9/9.
 */
public class CHistoryDbUtil {
    private static CHistoryDbUtil ourInstance = new CHistoryDbUtil();
    private final DbManager db;

    public static CHistoryDbUtil getInstance() {
        return ourInstance;
    }

    private CHistoryDbUtil() {
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName("sport_medicine")
                .setDbVersion(1);
        db = x.getDb(daoConfig);
    }

    public List<CommunityHistory> queryAll() {
        try {
            return db.selector(CommunityHistory.class).orderBy("id",true).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void save(String text) {
        if (query(text) == null) {
            CommunityHistory history = new CommunityHistory();
            history.setContent(text);
            try {
                db.save(history);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    public CommunityHistory query(String text) {
        try {
            return db.selector(CommunityHistory.class).where("content", "=", text).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteById(long id) {
        try {
            db.deleteById(CommunityHistory.class,id);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        try {
            db.delete(CommunityHistory.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
