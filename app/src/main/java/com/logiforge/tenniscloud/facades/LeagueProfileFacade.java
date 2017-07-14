package com.logiforge.tenniscloud.facades;

import com.logiforge.tenniscloud.db.LeagueProfileEmailTbl;
import com.logiforge.tenniscloud.db.LeagueProfilePhoneTbl;
import com.logiforge.tenniscloud.model.LeagueProfile;

/**
 * Created by iorlanov on 5/19/17.
 */

public class LeagueProfileFacade {
    public static class Builder {
        LeagueProfile profile = null;
        boolean resolveEmailsAndPhones = false;
        boolean resolveFacility = false;

        public Builder(LeagueProfile profile) {
            this.profile = profile;
        }

        public Builder resolveEmailsAndPhones() {
            resolveEmailsAndPhones = true;
            return this;
        }

        public Builder resolveFacility() {
            resolveFacility = true;
            return this;
        }

        public LeagueProfile build() {
            if(profile != null) {
                if (resolveEmailsAndPhones) {
                    LeagueProfileEmailTbl emailTbl = new LeagueProfileEmailTbl();
                    profile.setEmails(emailTbl.findEmailsByProfileId(profile.id));

                    LeagueProfilePhoneTbl phoneTbl = new LeagueProfilePhoneTbl();
                    profile.setPhones(phoneTbl.findPhonesByProfileId(profile.id));
                }
            }

            return profile;
        }
    }
}
