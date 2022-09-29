/**
 * Copyright (c) 2017 Automation Anywhere. All rights reserved.
 * <p>
 * This software is the proprietary information of Automation Anywhere. You
 * shall use it only in accordance with the terms of the license agreement you
 * entered into with Automation Anywhere.
 */
package ai.obvs.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atmosphere.config.managed.Decoder;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public class AcknowledgementDecoder implements Decoder<String, SessionActivityAcknowledgement> {
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private static final Logger logger = LogManager.getLogger(AcknowledgementDecoder.class);

    @Override
    public SessionActivityAcknowledgement decode(String message) {
    	logger.debug(() -> String.format("Decoding message: %s", message));
        try {
            SessionActivityAcknowledgement d = new ObjectMapper().readValue(message, SessionActivityAcknowledgement.class);
            Set<ConstraintViolation<SessionActivityAcknowledgement>> violations = validator.validate(d);
            if (violations.size() > 0) {
                logger.info(() -> String.format("Violations detail: %s", violations.toString()));
                throw new ConstraintViolationException(violations);
            }
            return d;
        } catch (JsonProcessingException e) {
        }
        return null;
    }
}