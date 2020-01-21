package com.example.carsharing.Other;

import android.widget.ListView;

import java.util.ArrayList;

public class Model_comment
{
    private String Comment;
    private Integer CoxmmentId;
    private Integer Status;


    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public Integer getCoxmmentId() {
        return CoxmmentId;
    }

    public void setCoxmmentId(Integer coxmmentId) {
        CoxmmentId = coxmmentId;
    }

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer status) {
        Status = status;
    }
}
