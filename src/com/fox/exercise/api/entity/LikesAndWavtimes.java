package com.fox.exercise.api.entity;

public class LikesAndWavtimes {
    private int likes;
    private int wavtimes;
    private int commentCount;

    public LikesAndWavtimes(int likes, int wavtimes, int commentCount) {
        this.likes = likes;
        this.wavtimes = wavtimes;
        this.commentCount = commentCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getWavtimes() {
        return wavtimes;
    }

    public void setWavtimes(int wavtimes) {
        this.wavtimes = wavtimes;
    }

    @Override
    public String toString() {
        return "LikesAndWavtimes [likes=" + likes + ", wavtimes=" + wavtimes
                + ", commentCount=" + commentCount + "]";
    }


}
