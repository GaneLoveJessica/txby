package com.tianxiabuyi.sports_medicine.login.util;

import com.tianxiabuyi.sports_medicine.model.ThirdAccount;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2016/9/26.
 */

public class ThirdAccountDbUtil {
    private static ThirdAccountDbUtil ourInstance = new ThirdAccountDbUtil();
    private final DbManager db;

    public static ThirdAccountDbUtil getInstance() {
        return ourInstance;
    }

    private ThirdAccountDbUtil() {
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName("sport_medicine")
                .setDbVersion(1);
        db = x.getDb(daoConfig);
    }

    public void saveAll(List<ThirdAccount> accounts) {
        for (int i = 0; i < accounts.size(); i++) {
            try {
                db.save(accounts.get(i));
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    public ThirdAccount query(String source) {
        try {
            return db.selector(ThirdAccount.class).where("source","=",source).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void clear() {
        try {
            db.delete(ThirdAccount.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void removeAccount(String platform) {
        try {
            db.deleteById(ThirdAccount.class,platform);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void save(String source, String userId) {
        ThirdAccount thirdAccount = new ThirdAccount();
        thirdAccount.setSource(source);
        thirdAccount.setUnion_id(userId);
        try {
            db.save(thirdAccount);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
