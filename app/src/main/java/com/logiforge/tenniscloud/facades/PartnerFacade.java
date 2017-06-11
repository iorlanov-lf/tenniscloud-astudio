package com.logiforge.tenniscloud.facades;

import com.logiforge.tenniscloud.db.PartnerEmailTbl;
import com.logiforge.tenniscloud.db.PartnerPhoneTbl;
import com.logiforge.tenniscloud.db.PartnerTbl;
import com.logiforge.tenniscloud.model.Partner;

/**
 * Created by iorlanov on 5/19/17.
 */

public class PartnerFacade {
    public static class Builder {
        Partner partner = null;
        boolean resolveEmailsFlag = false;
        boolean resolvePhonesFlag = false;

        public Builder(Partner partner) {
            this.partner = partner;
        }

        public Builder resolveEmails() {
            resolveEmailsFlag = true;
            return this;
        }

        public Builder resolvePhones() {
            resolvePhonesFlag = true;
            return this;
        }

        public Partner build() {
            if(partner != null) {
                if (resolveEmailsFlag) {
                    PartnerEmailTbl emailTbl = new PartnerEmailTbl();
                    partner.setEmails(emailTbl.findEmailsByPartnerId(partner.id));
                }

                if (resolvePhonesFlag) {
                    PartnerPhoneTbl phoneTbl = new PartnerPhoneTbl();
                    partner.setPhones(phoneTbl.findPhonesByPartnerId(partner.id));
                }
            }

            return partner;
        }
    }
}
