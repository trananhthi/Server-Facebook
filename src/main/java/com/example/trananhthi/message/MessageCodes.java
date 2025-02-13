package com.example.trananhthi.message;
/**
 * Message Id
 */
public class MessageCodes {
    /** endpoint.notFound=Endpoint not found: {0} */
    public static final String ENDPOINT_NOTFOUND = "endpoint.notFound";
    /** email.existed=Email {0} existed */
    public static final String EMAIL_EXISTED = "email.existed";
    /** signup.success=Sign up with email {0} successfully */
    public static final String SIGNUP_SUCCESS = "signup.success";
    /** signup.failed=Sign up with email {0} failed */
    public static final String SIGNUP_FAILED = "signup.failed";
    /** lackof.email.or.password=Email or password is missing */
    public static final String LACKOF_EMAIL_OR_PASSWORD = "lackof.email.or.password";
    /** email.notConfirmed=Account with email {0} is not confirmed */
    public static final String EMAIL_NOTCONFIRMED = "email.notConfirmed";
    /** email.or.password.incorrect=Email or password is incorrect */
    public static final String EMAIL_OR_PASSWORD_INCORRECT = "email.or.password.incorrect";
    /** refresh.token.isinexistent=Refresh token is inexistent */
    public static final String REFRESH_TOKEN_ISINEXISTENT = "refresh.token.isinexistent";
    /** unknown.error=Unknown error */
    public static final String UNKNOWN_ERROR = "unknown.error";
    /** account.notExist=Account does not exist: {0} */
    public static final String ACCOUNT_NOTEXIST = "account.notExist";
    /** account.emailConfirmed=Account is already confirmed */
    public static final String ACCOUNT_EMAILCONFIRMED = "account.emailConfirmed";
    /** resend.confirmcode.success=Resend confirm code to email {0} successfully */
    public static final String RESEND_CONFIRMCODE_SUCCESS = "resend.confirmcode.success";
    /** confirmcode.expired=Confirm code is expired */
    public static final String CONFIRMCODE_EXPIRED = "confirmcode.expired";
    /** confirmcode.incorrect=Confirm code is incorrect */
    public static final String CONFIRMCODE_INCORRECT = "confirmcode.incorrect";
    /** account.confirmation.success=Account confirmation successfully */
    public static final String ACCOUNT_CONFIRMATION_SUCCESS = "account.confirmation.success";
    /** something.wrong=Something went wrong */
    public static final String SOMETHING_WRONG = "something.wrong";
    /** post.notFound=Post not found: {0} */
    public static final String POST_NOTFOUND = "post.notFound";
    /** user.notFound=User not found with id: {0} */
    public static final String USER_NOTFOUND = "user.notFound";
    /** privacy.notValid=Privacy is not valid */
    public static final String PRIVACY_NOTVALID = "privacy.notValid";
    /** userPost.notFound=Post not found with id: {0} */
    public static final String USERPOST_NOTFOUND = "userPost.notFound";
    /** userPost.notBelong=You do not have permission to edit this post */
    public static final String USERPOST_NOTBELONG = "userPost.notBelong";
    /** chatroom.notFound=Chat room not found with id: {0} */
    public static final String CHATROOM_NOTFOUND = "chatroom.notFound";
}
