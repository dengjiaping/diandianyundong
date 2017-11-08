package com.fox.exercise.api.entity;

import java.io.Serializable;
import java.util.List;

public class UserDetail implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 8144335151472080869L;
    private int uid;
    private String uname;
    private String uimg;
    private List<PicsAndIds> imgs;
    private int counts;
    private int totalRank;
    private int totalLikes;
    private String sex;
    private String birthday;
    private int height;
    private int weight;
    private int fanCounts;
    private int followCounts;
    private int giftCounts;
    private String phoneno;
    private int followStatus;
    private int coins;
    private int golds;
    private int fcount;
    private int actmsgs;

    private int charms;
    private int upcharms;
    private int wealths;
    private int upwealths;
    private int kind;
    private int upkind;
    private int charmsrank;
    private int kindrank;
    private int wealthsrank;
    private boolean isOwner;
    private int sportstatus = 4;
    private String equtype;
    private String editdate;
    private String email;
    private String findimg;
    private MsgCounts msgCounts = new MsgCounts(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

    private int authStatus;
    private String authPic;
    private String province;//省
    private String city;//市
    private String area;//区
    private String sportsdata;//	距离
    private int time;//天数
    private String signature;//个性签名
    private double bmi;
    private int count_num;//运动次数
    private double sprots_Calorie;//总卡路里
    private int find_count_num;
    private int medal_num;//勋章个数

    public int getMedal_num() {
        return medal_num;
    }

    public void setMedal_num(int medal_num) {
        this.medal_num = medal_num;
    }
/*private String findImg;

	public String getFindImg() {
		return findImg;
	}

	public void setFindImg(String findImg) {
		this.findImg = findImg;
	}*/

    public int getFind_count_num() {
        return find_count_num;
    }

    public void setFind_count_num(int find_count_num) {
        this.find_count_num = find_count_num;
    }

    public int getCount_num() {
        return count_num;
    }

    public void setCount_num(int count_num) {
        this.count_num = count_num;
    }

    public double getSprots_Calorie() {
        return sprots_Calorie;
    }

    public void setSprots_Calorie(double sprots_Calorie) {
        this.sprots_Calorie = sprots_Calorie;
    }

    public int getActmsgs() {
        return actmsgs;
    }

    public void setActmsgs(int actmsgs) {
        this.actmsgs = actmsgs;
    }

    public String getEditdate() {
        return editdate;
    }

    public void setEditdate(String editdate) {
        this.editdate = editdate;
    }

    public int getFcount() {
        return fcount;
    }

    public void setFcount(int fcount) {
        this.fcount = fcount;
    }

    public int getSportstatus() {
        return sportstatus;
    }

    public void setSportstatus(int sportstatus) {
        this.sportstatus = sportstatus;
    }

    public String getEqutype() {
        return equtype;
    }

    public void setEqutype(String equtype) {
        this.equtype = equtype;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(int authStatus) {
        this.authStatus = authStatus;
    }

    public String getAuthPic() {
        return authPic;
    }

    public void setAuthPic(String authPic) {
        this.authPic = authPic;
    }

    public MsgCounts getMsgCounts() {
        return msgCounts;
    }

    public void setMsgCounts(MsgCounts msgCounts) {
        this.msgCounts = msgCounts;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCharms() {
        return charms;
    }

    public void setCharms(int charms) {
        this.charms = charms;
    }

    public int getUpcharms() {
        return upcharms;
    }

    public void setUpcharms(int upcharms) {
        this.upcharms = upcharms;
    }

    public int getWealths() {
        return wealths;
    }

    public void setWealths(int wealths) {
        this.wealths = wealths;
    }

    public int getUpwealths() {
        return upwealths;
    }

    public void setUpwealths(int upwealths) {
        this.upwealths = upwealths;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public int getUpkind() {
        return upkind;
    }

    public void setUpkind(int upkind) {
        this.upkind = upkind;
    }

    public int getCharmsrank() {
        return charmsrank;
    }

    public void setCharmsrank(int charmsrank) {
        this.charmsrank = charmsrank;
    }

    public int getKindrank() {
        return kindrank;
    }

    public void setKindrank(int kindrank) {
        this.kindrank = kindrank;
    }

    public int getWealthsrank() {
        return wealthsrank;
    }

    public void setWealthsrank(int wealthsrank) {
        this.wealthsrank = wealthsrank;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean isOwner) {
        this.isOwner = isOwner;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getGolds() {
        return golds;
    }

    public void setGolds(int golds) {
        this.golds = golds;
    }

    public int getFollowStatus() {
        return followStatus;
    }

    public void setFollowStatus(int followStatus) {
        this.followStatus = followStatus;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public int getFanCounts() {
        return fanCounts;
    }

    public void setFanCounts(int fanCounts) {
        this.fanCounts = fanCounts;
    }

    public int getFollowCounts() {
        return followCounts;
    }

    public void setFollowCounts(int followCounts) {
        this.followCounts = followCounts;
    }

    public int getGiftCounts() {
        return giftCounts;
    }

    public void setGiftCounts(int giftCounts) {
        this.giftCounts = giftCounts;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUimg() {
        return uimg;
    }

    public void setUimg(String uimg) {
        this.uimg = uimg;
    }

    public List<PicsAndIds> getImgs() {
        return imgs;
    }

    public void setImgs(List<PicsAndIds> imgs) {
        this.imgs = imgs;
    }

    public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }

    public int getTotalRank() {
        return totalRank;
    }

    public void setTotalRank(int totalRank) {
        this.totalRank = totalRank;
    }

    public int getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(int totalLikes) {
        this.totalLikes = totalLikes;
    }

    public String getFindimg() {
        return findimg;
    }

    public void setFindimg(String findimg) {
        this.findimg = findimg;
    }

    @Override
    public String toString() {
        return "UserDetail [uid=" + uid + ", uname=" + uname + ", uimg=" + uimg
                + ", imgs=" + imgs + ", counts=" + counts + ", totalRank="
                + totalRank + ", totalLikes=" + totalLikes + ", sex=" + sex
                + ", birthday=" + birthday + ", height=" + height + ", weight="
                + weight + ", fanCounts=" + fanCounts + ", followCounts="
                + followCounts + ", giftCounts=" + giftCounts + ", phoneno="
                + phoneno + ", followStatus=" + followStatus + ", coins="
                + coins + ", golds=" + golds + ", fcount=" + fcount
                + ", actmsgs=" + actmsgs + ", charms=" + charms + ", upcharms="
                + upcharms + ", wealths=" + wealths + ", upwealths="
                + upwealths + ", kind=" + kind + ", upkind=" + upkind
                + ", charmsrank=" + charmsrank + ", kindrank=" + kindrank
                + ", wealthsrank=" + wealthsrank + ", isOwner=" + isOwner
                + ", sportstatus=" + sportstatus + ", equtype=" + equtype
                + ", editdate=" + editdate + ", email=" + email + ", findimg="
                + findimg + ", msgCounts=" + msgCounts + ", authStatus="
                + authStatus + ", authPic=" + authPic + ", province="
                + province + ", city=" + city + ", area=" + area
                + ", sportsdata=" + sportsdata + ", time=" + time
                + ", signature=" + signature + "]";
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getSportsdata() {
        return sportsdata;
    }

    public void setSportsdata(String sportsdata) {
        this.sportsdata = sportsdata;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public double getBmi() {
        return bmi;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
    }


}
