package com.unilorin.vividmotion.pre_cbtapp.models;

import java.util.List;

/**
 * Created by Tofunmi on 17/01/2017.
 */

public class SignOutUserRequestObject {
    public String emailAddress;
    public List<TestResult> testResultsPendingUpload;
    public List<Long> attemptedQuestionsIds;
}
