package com.tidings.backend.domain;

import java.util.Date;

public class Tweet extends Document {
    private Date creationDate;

    public Tweet() {
    }

    public Date creationDate() {
        return creationDate;
    }
}