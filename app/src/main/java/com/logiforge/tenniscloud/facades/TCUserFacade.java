package com.logiforge.tenniscloud.facades;

import com.logiforge.lavolta.android.facade.LavoltaFacade;
import com.logiforge.tenniscloud.db.TCUserEmailTbl;
import com.logiforge.tenniscloud.db.TCUserPhoneTbl;
import com.logiforge.tenniscloud.db.TCUserTbl;
import com.logiforge.tenniscloud.model.TCUser;

/**
 * Created by iorlanov on 5/8/17.
 */

public class TCUserFacade {

    public static Builder builder() {
        TCUserFacade userFacade = new TCUserFacade();
        TCUser user = userFacade.getSelf();
        return builder(user);
    }

    public static Builder builder(TCUser user) {
        return new Builder(user);
    }

    public static class Builder {
        TCUser user;
        boolean resolveEmailsFlag = false;
        boolean resolvePhonesFlag = false;

        public Builder(TCUser user) {
            this.user = user;
        }

        public Builder resolveEmails() {
            resolveEmailsFlag = true;
            return this;
        }

        public Builder resolvePhones() {
            resolvePhonesFlag = true;
            return this;
        }

        public TCUser build() {
            if(resolveEmailsFlag) {
                TCUserEmailTbl emailTbl = new TCUserEmailTbl();
                user.setEmails(emailTbl.findEmailsByUserId(user.id));
            }

            if(resolvePhonesFlag) {
                TCUserPhoneTbl phoneTbl = new TCUserPhoneTbl();
                user.setPhones(phoneTbl.findPhonesByUserId(user.id));
            }

            return user;
        }
    }

    public TCUser getSelf() {
        LavoltaFacade lavoltaFacade = new LavoltaFacade();
        String userName = lavoltaFacade.getUserName();

        TCUserTbl userTbl = new TCUserTbl();
        return userTbl.getByUserName(userName);
    }
}
