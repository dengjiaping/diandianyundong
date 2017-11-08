package com.fox.exercise.api.entity;

import android.R.integer;

public class ActionInfos {

    public int id;
    public String thumb;
    public String title;
    public String description;
    public String start_time;
    public String end_time;
    public String inputtime;
    public int listorder;
    public String content;
    public String url;
    public int web_id;
    public int like_num;
    public int comment_num;

    public int getComment_num() {
        return comment_num;
    }

    public void setComment_num(int comment_num) {
        this.comment_num = comment_num;
    }

    public int getLike_num() {
        return like_num;
    }

    public void setLike_num(int like_num) {
        this.like_num = like_num;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getInputtime() {
        return inputtime;
    }

    public void setInputtime(String inputtime) {
        this.inputtime = inputtime;
    }

    public int getListorder() {
        return listorder;
    }

    public void setListorder(int listorder) {
        this.listorder = listorder;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getWeb_id() {
        return web_id;
    }

    public void setWeb_id(int web_id) {
        this.web_id = web_id;
    }

    @Override
    public String toString() {
        return "ActionInfos [id=" + id + ", thumb=" + thumb + ", title="
                + title + ", description=" + description + ", start_time="
                + start_time + ", end_time=" + end_time + ", inputtime="
                + inputtime + ", listorder=" + listorder + ", content="
                + content + ", url=" + url + ", web_id=" + web_id + "]";
    }

}
