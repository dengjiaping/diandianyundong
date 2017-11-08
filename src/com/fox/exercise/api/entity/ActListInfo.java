package com.fox.exercise.api.entity;

/**
 * @author YH
 */
public class ActListInfo {

    //广场推荐
    public int id;
    public String title;
    public String thumb;
    public String description;
    public String start_time;
    public String end_time;
    public String inputtime;
    public int listorder;
    public String url;
    public String content;
    public String thumbgengduo;
    public String thumblist;

    public String getThumbgengduo() {
        return thumbgengduo;
    }

    public void setThumbgengduo(String thumbgengduo) {
        this.thumbgengduo = thumbgengduo;
    }

    public String getThumblist() {
        return thumblist;
    }

    public void setThumblist(String thumblist) {
        this.thumblist = thumblist;
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

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ActListInfo [id=" + id + ", title=" + title + ", thumb="
                + thumb + ", description=" + description + ", start_time="
                + start_time + ", end_time=" + end_time + ", inputtime="
                + inputtime + ", listorder=" + listorder + ", url=" + url
                + ", content=" + content + "]";
    }

}
