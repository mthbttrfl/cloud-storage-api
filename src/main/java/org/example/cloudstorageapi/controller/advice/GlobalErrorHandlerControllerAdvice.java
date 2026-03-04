package org.example.cloudstorageapi.controller.advice;

import lombok.extern.slf4j.Slf4j;
import org.example.cloudstorageapi.dto.resp.RespMessageDTO;
import org.example.cloudstorageapi.dto.resp.RespMessagesDTO;
import org.example.cloudstorageapi.exception.*;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

import static org.example.cloudstorageapi.constant.DataBaseErrorMessages.UNKNOWN_DB;
import static org.example.cloudstorageapi.constant.DataBaseErrorMessages.USERNAME_ALREADY_EXISTS;
import static org.example.cloudstorageapi.constant.ErrorMessages.UNKNOWN;
import static org.example.cloudstorageapi.constant.MultipartErrorMessages.INVALID_REQUEST;
import static org.example.cloudstorageapi.constant.MultipartErrorMessages.SIZE_EXCEED_FORMATTED;
import static org.example.cloudstorageapi.constant.SecurityErrorMessages.INVALID_USERNAME_OR_PASSWORD;
import static org.example.cloudstorageapi.constant.StorageErrorMessages.UNKNOWN_STORAGE;

@Slf4j
@RestControllerAdvice
public class GlobalErrorHandlerControllerAdvice {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<RespMessageDTO> authenticationException(AuthenticationException ex){

        log.error("AuthenticationException, message: {}", ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new RespMessageDTO(INVALID_USERNAME_OR_PASSWORD));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RespMessagesDTO> methodArgumentNotValidException(MethodArgumentNotValidException ex){

        log.error("MethodArgumentNotValidException, message: {}", ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RespMessagesDTO(
                ex.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<RespMessageDTO> dataIntegrityViolationException(DataIntegrityViolationException ex){

        log.error("DataIntegrityViolationException, message: {}", ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(new RespMessageDTO(USERNAME_ALREADY_EXISTS));
    }

    @ExceptionHandler(PathIllegalArgumentException.class)
    public ResponseEntity<RespMessageDTO> pathIllegalArgumentException(PathIllegalArgumentException ex){

        log.error("PathIllegalArgumentException, message: {}", ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RespMessageDTO(ex.getMessage()));
    }

    @ExceptionHandler(StorageConflictException.class)
    public ResponseEntity<RespMessageDTO> storageConflictException(StorageConflictException ex){

        log.error("StorageConflictException, message: {}", ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(new RespMessageDTO(ex.getMessage()));
    }

    @ExceptionHandler(StorageNotFoundException.class)
    public ResponseEntity<RespMessageDTO> storageNotFoundException(StorageNotFoundException ex){

        log.error("StorageNotFoundException, message: {}", ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RespMessageDTO(ex.getMessage()));
    }

    @ExceptionHandler(StorageMoveOrRenameException.class)
    public ResponseEntity<RespMessageDTO> storageMoveOrRenameException(StorageMoveOrRenameException ex){

        log.error("StorageMoveOrRenameException, message: {}", ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RespMessageDTO(ex.getMessage()));
    }

    @ExceptionHandler(StorageDirectoryCreateException.class)
    public ResponseEntity<RespMessageDTO> storageDirectoryCreateException(StorageDirectoryCreateException ex){

        log.error("StorageDirectoryCreateException, message: {}", ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RespMessageDTO(ex.getMessage()));
    }

    @ExceptionHandler(StorageUploadException.class)
    public ResponseEntity<RespMessageDTO> storageUploadException(StorageUploadException ex){

        log.error("StorageUploadException, message: {}", ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RespMessageDTO(ex.getMessage()));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<RespMessageDTO> maxUploadSizeExceededException(MaxUploadSizeExceededException ex) {

        log.error("MaxUploadSizeExceededException, message: {}", ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(new RespMessageDTO(SIZE_EXCEED_FORMATTED.formatted("5MB")));
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<RespMessageDTO> multipartException(MultipartException ex) {

        log.error("MultipartException, message: {}", ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RespMessageDTO(INVALID_REQUEST));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<RespMessageDTO> unsupportedMediaType(HttpMediaTypeNotSupportedException ex){

        log.error("HttpMediaTypeNotSupportedException, message: {}", ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(new RespMessageDTO(ex.getMessage()));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<RespMessageDTO> dataAccessException(DataAccessException ex){

        log.error("DataAccessException, message: {}", ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new RespMessageDTO(UNKNOWN_DB));
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<RespMessageDTO> storageException(StorageException ex){

        log.error("StorageException, message: {}", ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new RespMessageDTO(UNKNOWN_STORAGE));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RespMessageDTO> exception(Exception ex){

        log.error("Exception, message: {}", ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new RespMessageDTO(UNKNOWN));
    }
}