package com.definiteplans.service;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.definiteplans.dao.UnsubscriberRepository;
import com.definiteplans.dom.Unsubscriber;


@Service
public class UnsubscriberService {
    private static final Logger logger = LoggerFactory.getLogger(UnsubscriberService.class);

    @Autowired private UnsubscriberRepository unsubscriberRepository;

    public Unsubscriber getById(String email) {
        if (email == null || email.length() == 0) {
            return null;
        }
        Optional<Unsubscriber> found = unsubscriberRepository.findById(email);
        return found.isPresent() ? found.get() : null;
    }

    public boolean save(Unsubscriber obj) {
        if (obj == null) {
            return false;
        }
        unsubscriberRepository.save(obj);
        return true;
    }


    public String decryptEmailToUnsub(String encStr) {
        String decryptedString = null;
//        try {
//            decryptedString = this.cipherService.base64DecodeAndDecrypt(encStr);
//        } catch (Exception e) {
//            logger.debug("error decrypting str {}", encStr, e);
//            return null;
//        }

        return decryptedString;
    }

    public String createEncryptedUnsubString(String email) throws UnsupportedEncodingException {
        //return this.cipherService.encryptAndBase64Encode(email);
        return null;
    }
}
