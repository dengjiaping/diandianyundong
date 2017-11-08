package com.fox.exercise.login;

public class PhotoDetail {
    private int pid;
    private String ptitle;
    private String purl;
    private int likes;
    private long addtimes;
    private boolean ischeck;

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getPtitle() {
        return ptitle;
    }

    public void setPtitle(String ptitle) {
        this.ptitle = ptitle;
    }

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public long getAddtimes() {
        return addtimes;
    }

    public void setAddtimes(long addtimes) {
        this.addtimes = addtimes;
    }


    public boolean isIscheck() {
        return ischeck;
    }

    public void setIscheck(boolean ischeck) {
        this.ischeck = ischeck;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "photoDetail [pid=" + pid + ", ptitle=" + ptitle
                + ", purl=" + purl + ", likes=" + likes + ", addtimes="
                + addtimes + ", ischeck=" + ischeck + "]";
    }

}
