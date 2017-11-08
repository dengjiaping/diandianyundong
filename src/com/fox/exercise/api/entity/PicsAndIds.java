package com.fox.exercise.api.entity;

public class PicsAndIds {
    /**
     * used for 3 square
     */
    private int id;
    private String imgUrl;
    private int likes;
    private ImageDetail imgDetail;

    public ImageDetail getImgDetail() {
        return imgDetail;
    }

    public void setImgDetail(ImageDetail imgDetail) {
        this.imgDetail = imgDetail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((imgUrl == null) ? 0 : imgUrl.hashCode());
        result = prime * result + likes;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PicsAndIds other = (PicsAndIds) obj;
        if (id != other.id)
            return false;
        if (imgUrl == null) {
            if (other.imgUrl != null)
                return false;
        } else if (!imgUrl.equals(other.imgUrl))
            return false;
        if (likes != other.likes)
            return false;
        return true;
    }

//	 public boolean equals(Object o) {
//	       if (!(o instanceof PicsAndIds))
//	           return false;
//	       PicsAndIds item = (PicsAndIds) o;
//	       return item.id == id&& item.imgUrl == imgUrl;
//	              
//	    }
//
//	 public int hashCode() {
//	       int result = 17;
//	       result = 37 * result + (int) id;
//	       result = 37 * result + this.imgUrl.hashCode();
//	      
//	       return result;
//	    }

}
