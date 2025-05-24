package com.perm.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception générique pour les erreurs métier.
 * Cette exception est lancée lorsqu'une règle métier est violée.
 * Par exemple : tentative de création d'un compte avec un email déjà existant,
 * tentative d'affectation d'un moniteur à une plage horaire déjà occupée, etc.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BusinessException extends RuntimeException {

    /**
     * Code d'erreur métier (optionnel)
     */
    private final String errorCode;

    /**
     * Données supplémentaires liées à l'erreur (optionnel)
     */
    private final Object data;

    /**
     * Constructeur avec message uniquement
     *
     * @param message Message d'erreur
     */
    public BusinessException(String message) {
        super(message);
        this.errorCode = null;
        this.data = null;
    }

    /**
     * Constructeur avec message et cause
     *
     * @param message Message d'erreur
     * @param cause Cause de l'erreur
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = null;
        this.data = null;
    }

    /**
     * Constructeur avec message et code d'erreur
     *
     * @param message Message d'erreur
     * @param errorCode Code d'erreur métier
     */
    public BusinessException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.data = null;
    }

    /**
     * Constructeur complet avec message, code d'erreur et cause
     *
     * @param message Message d'erreur
     * @param errorCode Code d'erreur métier
     * @param cause Cause de l'erreur
     */
    public BusinessException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.data = null;
    }

    /**
     * Constructeur avec message, code d'erreur et données supplémentaires
     *
     * @param message Message d'erreur
     * @param errorCode Code d'erreur métier
     * @param data Données supplémentaires
     */
    public BusinessException(String message, String errorCode, Object data) {
        super(message);
        this.errorCode = errorCode;
        this.data = data;
    }

    /**
     * Obtenir le code d'erreur
     *
     * @return Code d'erreur métier
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Obtenir les données supplémentaires
     *
     * @return Données liées à l'erreur
     */
    public Object getData() {
        return data;
    }
}