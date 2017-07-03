package com.github.sandokandias.payments.interfaces.rest.model;


import com.github.sandokandias.payments.infrastructure.util.i18n.I18nMessage;
import lombok.Data;

import java.util.Set;

@Data
public class ErrorResponse {
    private Set<I18nMessage> errors;
}
