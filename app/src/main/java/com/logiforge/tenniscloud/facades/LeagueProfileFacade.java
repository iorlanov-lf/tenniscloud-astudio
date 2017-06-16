package com.logiforge.tenniscloud.facades;

import com.logiforge.tenniscloud.db.LeagueProfileEmailTbl;
import com.logiforge.tenniscloud.model.LeagueProfile;

/**
 * Created by iorlanov on 5/19/17.
 */

public class LeagueProfileFacade {
    public static class Builder {
        LeagueProfile profile = null;
        boolean resolveEmailsFlag = false;
        boolean resolveFacilityFlag = false;

        public Builder(LeagueProfile profile) {
            this.profile = profile;
        }

        public Builder resolveEmails() {
            resolveEmailsFlag = true;
            return this;
        }

        public Builder resolveFacility() {
            resolveFacilityFlag = true;
            return this;
        }

        public LeagueProfile build() {
            if(profile != null) {
                if (resolveEmailsFlag) {
                    LeagueProfileEmailTbl emailTbl = new LeagueProfileEmailTbl();
                    profile.setEmails(emailTbl.findEmailsByProfileId(profile.id));
                }
            }

            return profile;
        }
    }
}
