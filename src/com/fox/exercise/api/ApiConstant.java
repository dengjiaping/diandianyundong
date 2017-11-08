package com.fox.exercise.api;

public class ApiConstant {
    public static final String userPwdForgetnew = "m=user&a=pwdforgetnew";// 忘记密码
    public static final String getinvitesports = "m=third&a=getinvitesports";// 获取约跑我的人列表
    public static final String URLM = "http://kupao.mobifox.cn";

    // mememme
    // public static final String URL = "http://dev-kupao.mobifox.cn";
    // public static final String DATA_URL = URL + "/Beauty/kupao.php?";

    // ----------------------------------
    // public static final String URL = "http://kupao.mobifox.cn";
    // public static final String DATA_URL = URL + "/Beauty/api.php/?";

    public static final String URL = "http://kupao.mobifox.cn";//上线url
//    public static final String URL = "http://dev-kupao.mobifox.cn";// 测试url
    public static final String DATA_URL = URL + "/Beauty/kupao.php?";

    public static final String SESSION_OUT = "session_out";

    public static final String homeDetail = "m=home&a=detail";
    public static final String homeImg = "m=home&a=img";
    public static final String homeShow = "m=home&a=show";
    public static final String userCreatebysport = "m=user&a=createbysport";// 用户注册
    public static final String userModifybysport = "m=user&a=modifybysport";// 修改用户信息
    public static final String userLogin = "m=user&a=sportslogin";// 登录
    public static final String openequipment = "m=user&a=openequipment";// openequipment
    public static final String userDetail = "m=user&a=detail";
    public static final String uploadImg = "m=upload&a=img";

    public static final String userSimple = "m=user&a=usermsg";// 通过用户id获取用户信息
    public static final String userDetailMsg = "m=user&a=userdetailmsg";

    public static final String savesportInfo = "m=user&a=savesportInfo";

    public static final String squareAds = "m=square&a=ads";
    public static final String squareHots = "m=square&a=hots";
    public static final String squareNews = "m=square&a=news";
    public static final String squareLabel = "m=square&a=label";

    public static final String picDetail = "m=pic&a=detail";
    public static final String picLikeimg = "m=pic&a=likeimg";
    public static final String picStatus = "m=pic&a=status";
    public static final String picLikes = "m=pic&a=likes";
    public static final String picLikesWav = "m=pic&a=likeswavsport";
    public static final String picComment = "m=pic&a=comment";
    public static final String getComment = "m=pic&a=getCommentList";
    public static final String newComment = "m=pic&a=newcomment";
    public static final String runWav = "m=pic&a=runwav";
    public static final String uploadComment = "m=pic&a=commentInfo";
    public static final String runWavSports = "m=pic&a=runwavsports";
    public static final String getSportsVisitor = "m=pic&a=getSportsVisitor";// 获取访客列表
    public static final String visitorSports = "m=pic&a=visitorSports";// 后台增加访客信息
    public static final String getSportsInfo = "m=pic&a=getSportsInfo";
    public static final String getSportsTaskAll = "m=pic&a=getSportsTaskAllNew";// 获取历史记录列表
    public static final String getSportsTaskPhone = "m=pic&a=getSportsTaskPhone";
    public static final String getSportsTaskWatch = "m=pic&a=getSportsTaskWatch";
    public static final String getSportsTaskById = "m=pic&a=getSportsTaskById";// 通过运动记录id获取详细信息
    public static final String getSportsTaskByDate = "m=pic&a=getSportsTaskByDate";
    public static final String getCurrentTimeById = "m=pic&a=getCurrentTimesById";
    public static final String getTaskTimeById = "m=pic&a=getTaskTimesById";
    public static final String getLastTaskInfo = "m=pic&a=getLastTaskInfo";// 获取用户最后一条运动记录信息
    public static final String searchUser = "m=user&a=searchuser";
    public static final String searchByName = "m=user&a=searchbynameforsport";// 通过字符串搜索附近的人
    public static final String searchFriendByName = "m=user&a=searchFriendbynameforsport";

    public static final String invitesport = "m=third&a=invitesport";// 发送约跑信息给某用户
    public static final String searchByPic = "m=user&a=searchbypic";
    public static final String searchPic = "m=user&a=searchpic";
    public static final String userPwdForget = "m=user&a=pwdforget";
    public static final String sendPhoneMessage = "m=user&a=ShortMessage";// 发送短信验证码

    public static final String deviceId = "m=home&a=deviceid";

    public static final String refreshrank = "m=more&a=refreshrank";
    // public static final String refreshrankbysport =
    // "m=more&a=refreshrankbysport";//获取用户信息
    public static final String refreshrankbysport = "m=user&a=refreshrankbysport";// 获取用户信息(改后的)
    public static final String modifymsg = "m=more&a=modifymsg";
    public static final String deleteimg = "m=more&a=deleteimg";
    public static final String userpics = "m=more&a=userpics";

    public static final String rankWeek = "m=rank&a=week";
    public static final String rankMonth = "m=rank&a=month";
    public static final String rankQuarter = "m=rank&a=quarter";
    public static final String rankTotal = "m=rank&a=total";

    public static final String weiboRegister = "m=weibo&a=register";
    public static final String weiboLogin = "m=weibo&a=login";
    // public static final String weiboNewLogin =
    // "m=weibo&a=sportslogin";//微博注册登录
    public static final String weiboNewLogin = "m=user&a=weibo_login";// 微博注册登录

    public static final String followPerson = "m=third&a=followperson";// 关注某人，即添加好友
    public static final String giveGift = "m=third&a=givegift";
    public static final String getGifts = "m=third&a=getgifts";
    public static final String coingoldChange = "m=third&a=coingoldchange";// 给某用户增加金币
    public static final String getNearby = "m=third&a=getnearbysports";// 获取附近的人列表
    public static final String getNearbyNew = "m=third&a=getnearbysportsNew";
    public static final String getNearbyBysex = "m=third&a=getnearbysportsBysex";// 按性别获取附近的人列表
    public static final String getNearbyName = "m=user&a=searchbyname";// 搜索好友
    public static final String getFriend = "m=third&a=getFriendsports";// 获取好友列表
    public static final String getFans = "m=third&a=getFanssports";// 获取粉丝列表
    public static final String getFriendList = "m=third&a=getFriendList";
    public static final String getFriendByName = "m=third&a=getFriendByName";// 通过字符串搜索好友
    public static final String getFansByName = "m=third&a=getFansByName";// 通过字符串搜索好友
    public static final String uploadLocal = "m=third&a=uploadlocal";// 上传用户当前经纬度
    public static final String userfollow = "m=third&a=usersportfollow";
    public static final String userGift = "m=third&a=usergift";
    public static final String userFan = "m=third&a=userfan3";

    public static final String modifyLauncher = "m=user&a=launcher_modify";
    public static final String weiboLauncher = "m=weibo&a=getlaunchermsg";
    public static final String weiboMeimei = "m=weibo&a=getmeimeimsg";
    public static final String weiboLauncherLogin = "m=weibo&a=launcher_login";
    public static final String refreshLauncherRank = "m=more&a=refreshlauncherrank";

    public static final String getDayGolds = "m=four&a=getgolds";
    public static final String judgePic = "m=four&a=judgepic";
    public static final String charmrank = "m=four&a=charmrank";
    public static final String wmqrank = "m=four&a=wmqrank";

    public static final String userVisitor = "m=four&a=getvisitor";
    public static final String visitor = "m=four&a=visitor";
    public static final String getCommentsMe = "m=four&a=getmeCommentsInfo";
    public static final String delComments = "m=four&a=delcommentsports";
    public static final String getMyLikes = "m=four&a=getmylikes";
    public static final String sendprimsg = "m=four&a=sendsportprimsg";// 发送私信
    public static final String getPrimsgAll = "m=four&a=getprimsgallbysport";// 获取私信列表
    public static final String getPrimsgOne = "m=four&a=getprimsgone";// 获取私信
    public static final String blackPeople = "m=four&a=blackpeople";
    public static final String getBlackList = "m=four&a=getblacklist";
    public static final String sunshine = "m=four&a=sunshine";
    public static final String getSunshine = "m=four&a=getsunshine";
    public static final String getSysmsg = "m=four&a=getSysMsgSports";// 获取系统消息列表
    public static final String ffollow = "m=four&a=ffollow";
    public static final String fupload = "m=four&a=fupload";
    public static final String fgift = "m=four&a=fgift";
    public static final String downimg = "m=four&a=downimg";
    public static final String activities = "m=four&a=sportsactivities";// 渠道活动送金币
    public static final String getmsgcounts = "m=four&a=getmsgBySportscounts";
    public static final String sportfollow = "m=four&a=sportfollow";

    public static final String getTradeNo = "m=four&a=gettradeno";

    public static final String navigation = "m=four&a=navigation";
    public static final String naviReplace = "m=four&a=navi_replace";
    public static final String authentication = "m=upload&a=authentication";
    public static final String verify = "m=four&a=verify";
    public static final String forbid = "m=four&a=forbid";
    public static final String examine = "m=four&a=examine";
    public static final String getExamine = "m=four&a=getexamine";
    public static final String getNotification = "m=four&a=getnotification";
    public static final String getHelp = "m=four&a=getsporthelp";
    public static final String cmcc = "m=four&a=cmcc";

    public static final String getphotoframes = "m=meimei&a=getphotoframes";
    public static final String getFramesById = "m=meimei&a=getframesbyid";

    public static final String instWeightAndHeight = "m=reduce&a=instweightandheight";
    public static final String uploadSportsHistoryWithImg = "m=reduce&a=uploadwithimg";
    public static final String sportsexamine = "m=reduce&a=examine";
    public static final String getsportsExamine = "m=reduce&a=getexamine";
    public static final String sportsRank = "m=reduce&a=sportsrank";
    // public static final String sportsRankNew =
    // "m=reduce&a=sportsranknew";//获取排行列表
    public static final String sportsRankNew = "m=third&a=sportsranknew";// 获取排行列表(改后的)
    public static final String getSportsActs = "m=reduce&a=getsportsacts";
    public static final String sportsActs = "m=reduce&a=sportsacts";
    public static final String getSportsDetails = "m=reduce&a=getsportsdetails";
    public static final String getDayImgs = "m=reduce&a=getdayimgs";
    public static final String getSportsImgDetail = "m=reduce&a=getsportimgdetail";
    public static final String getTime = "m=reduce&a=gettime";
    public static final String uploadCount = "m=reduce&a=uploadCount";
    public static final String uploadSportTask = "m=pic&a=addSportsTaskNew";// 上传运动记录
    public static final String updateSportTask = "m=pic&a=updateSportsTaskNew";// 更新运动记录
    // public static final String uploadSportTaskMedia =
    // "m=upload&a=uploadSportsImgorAudioNew";//上传多媒体文件
    public static final String uploadSportTaskMedia = "m=pic&a=uploadSportsImgorAudioNew";// 上传多媒体文件(改后的)
    public static final String uploadSportType = "m=pic&a=addSportsType";
    // public static final String sportsUploadMsg =
    // "m=reduce&a=sportsUploadMsg";
    public static final String sportsUploadMsg = "m=third&a=sportsUploadMsg";// 获取好友上传列表(改后的)
    public static final String getSportsMediaById = "m=pic&a=getSportsMediaById";
    public static final String getMediaListByTaskid = "m=pic&a=getMediaListByTaskidNew";// 通过运动记录id获取包含的多媒体文件列表
    public static final String deleteSportsTaskById = "m=pic&a=deleteSportsTaskById";// 删除一条运动记录
    public static final String deleteSportMediaById = "m=pic&a=deleteSportsMediaById";// 删除一个多媒体文件
    public static final String adShow = "m=Ad&a=index";
    public static boolean isservice = true;
    public static final String msgAction = "ACTION_DATA_TO_SERVICE";
    public static final String editAction = "ACTION_USERDATA_TO_SERVICE";
    public static final int CONNECTOK = 11;
    public static final int UPDATE_MSG = 0x001;
    public static final int UPDATE_FRIENDS_MSG = 0x002;
    public static final int UPDATE_DEFAULTMAP_MSG = 0x007;
    public static final int CLEAR_PRI_MSG = 0x008;
    public static final int UPDATE_COINS_MSG = 0x009;
    public static final int UPDATE_COINS_NOW = 0x010;
    public static final String getActionList = "m=Activity&a=lists";
    // 获取活动新接口
    public static final String getOnlineAction = "m=Webapp&a=online_activity";
    public static final String signupAction = "m=Webapp&a=sign_up_active";
    public static final String getNewActionList = "m=Webapp&a=lists";
    public static final String getActionInfo = "m=Activity&a=info";
    public static final String getActivityDetailInfo = "m=Webapp&a=activityinfo";
    public static final String getPayInfo = "m=Activity&a=pay";
    public static final String getApplyInfo = "m=Activity&a=apply_user";
    public static final String getActionRankList = "m=Activity&a=top_user";
    public static final String goApply = "m=Activity&a=apply";
    public static final String getFindList = "m=Find&a=lists";
    public static final String getFindMore = "m=Find&a=findinfo";
    public static final String addFind = "m=Find&a=add";
    public static final String likeFind = "m=Find&a=like";
    public static final String delFind = "m=Find&a=del";
    public static final String topFind = "m=Find&a=top_find";
    public static final String deltopFind = "m=Find&a=deltop_find";
    public static final String addComment = "m=Find&a=add_comment";
    public static final String delComment = "m=Find&a=del_comment";
    public static final String updateFindBg = "m=Find&a=findimg";
    public static final String updatePwNew = "m=user&a=passwd_edit";
    public static final String getSportsRecordInfo = "m=pic&a=getSportsRecordInfo";
    // 更新未读评论数据
    public static final String getNewCommentInfo = "m=Find&a=newcommentinfo";
    public static final String getNewCommentLists = "m=Find&a=newcommentlists";
    public static final int COINS_SUCCESS = 0x00100;
    public static final int COINS_FAIL = 0x00200;
    public static final int COINS_LIMIT = 0x00300;
    public static final String qqHealthBase = "https://openmobile.qq.com/v3/health/";

    public class ImgType {
        public static final int IMAGE_BIG = 1;
        public static final int IMAGE_SMALL = 2;
        public static final int IMAGE_MEDIUM = 3;
        public static final int IMAGE_ORI = 4;
    }

    public static class WeiboType {
        public static final String TengxunWeiBo = "txwb";
        public static final String SinaWeibo = "xlwb";
        public static final String QQzone = "qqzone";
        public static final String JDOption = "jdy";
        public static final String WeiXin = "weixin";
    }

    public static final String getTopList = "m=Find&a=topfindlist";// 获取好友动态置顶列表
    public static final String getGoodFriendsList = "m=Find&a=friendlists";// 获取好友动态列表
    public static final String getHuoDongLikesList = "m=Find&a=likeslist";// 获取动态点赞列表
    public static final String getCircleFindList = "m=Find&a=sysfindlists";// 获取发现列表
    public static final String getCircleFindListContent = "m=Find&a=sysfindinfo";// 获取发现列表详情
    public static final String upDateSportsTemp = "m=pic&a=todayStepNum";// 上传今日步数
    public static final String uploadSportsTemp = "m=pic&a=gettodayStepNum";// 下载今日步数
    public static final String uploadSleepData = "m=Health&a=addSleepEffect";// 上传睡眠数据
    public static final String uploadXinlvData = "m=Health&a=addHartRate";// 上传心率数据
    public static final String SportscountNum = "m=pic&a=getSportscountNum";// 获取运动记录条数
    public static final String getSleepDate = "m=Health&a=getSleepEffect";// 健康睡眠的获取
    public static final String getXinlvDate = "m=Health&a=getHartRate";// 健康心率的获取
    public static final String deleteXinlv = "m=Health&a=delHartRate";// 删除健康新绿记录
    public static final String panDianGetImage = "m=Datacount&a=datalastimg";// 盘点最后一张图片生成接口
    public static final String healthdatacount = "m=Health&a=healthdatacount";// 健康统计的添加
    public static final String addUserLoginTime = "m=Health&a=updatesettime";// 增加用户登陆时长
    public static final String getTrainlist = "m=Train&a=trainlist";// 训练计划的列表
    public static final String getTraininfo = "m=Train&a=traininfo";// 训练计划的详情
    public static final String getTrainActionInfo = "m=Train&a=trainactioninfo";// 训练计划的动作详情
    public static final String getTrainTaskList = "m=Train&a=traintasklist";// 训练成绩列表
    public static final String getTotalTrainTask = "m=Train&a=usertraincount";// 用户训练总和信息
    public static final String addTrainRecord = "m=Train&a=addtrain";// 训练成绩添加
    public static final String activein = "m=Webactivity&a=activein";// 外部活动最新的提取
    public static final String getwxInfo = "m=Apipay&a=weixinpaysign";// 请求签名
    public static final String Payid = "m=Apipay&a=creatorder";// 预支付的链接
    public static final String YePao = "m=Webactivity&a=activesigninfo";// 夜跑用户信息
    public static final String YeList = "m=Apipay&a=mlscreatorder";// 夜跑微信支付的统一下单
    public static final String YeDindanInfo = "m=Webactivity&a=activeorderinfo";// 夜跑根据订单号查询出订单的详情
    public static final String SuccessHtml = "m=Webactivity&a=successhtml&id=";// 夜跑支付成功页面
    public static final String FailHtml = "m=Webactivity&a=falsehtml&id=";// 夜跑支付失败页面
    //	public static final String ShoppingMall = "m=Webshop&a=orderinfo";// 金币商城的根据订单号查询订单详情
//	public static final String Qiandao = "m=Webshop&a=signedinfo";// 金币商城签到
//	public static final String getwxData = "m=Webshop&a=orinfo";// 金币商城获取订单信息
//	public static final String getGoodsInfo = "m=Webshop&a=goodsinfo";// 金币商城获取商品信息
    public static final String getwxData = "m=Webshopnew&a=orinfo";// 金币商城获取订单信息
    public static final String getGoodsInfo = "m=Webshopnew&a=goodsinfo";// 金币商城获取商品信息
    public static final String ShoppingMall = "m=Webshopnew&a=orderinfo";// 金币商城的根据订单号查询订单详情
    public static final String Qiandao = "m=Webshopnew&a=signedinfo";// 金币商城签到
    public static final String ServerTime = "m=Webshopnew&a=servertime";// 获取服务器时间
    public static final String getCurrentDayBuShu = "m=pic&a=getjisenseStepNum";// 提取某天步数详情，默认为当天的
    public static final String saveBuShuTongJiToNetWork = "m=pic&a=jisenseStepNum";// 上传今日步数
    public static final String getAllDayBuShu = "m=pic&a=getjisenseStepNumlist";// 取用户步数列表
    public static final String getShareData = "m=sports&a=pedometerRecordHtml";// 取用户步数列表
    public static final String getActInfo = "m=Square&a=lists"; //广场数据列表
    public static final String getActInfos = "m=Square&a=actlistinfo"; //广场分类下数据列表
    public static final String getShareIcon = "m=sports&a=SportsRecordHtml"; //运动记录的分享图片
    public static final String getZhangzisInfo = "m=Square&a=activityinfo&id=";// 涨姿势内容详情页
    public static final String getZhangzisGengduo = "m=Square&a=activitylist&channelnum=";// 涨姿势更多
    public static final String getShareAction = "m=sports&a=WebappRecordHtml&id=";// 活动分享的链接
    public static final String getActionTitle = "m=Square&a=actinfo";// 获取标题和图片的接口
    public static final String uploadPeisu = "m=pic&a=sportstaskdata";//上传配速数据
    public static final String getpeissu = "m=pic&a=getsportstaskdatalist";//获取配速的数据
    public static final String uploadSportsTwoInfo = "m=pic&a=sports_taskCountdata";//统一上传运动记录和配速或者登山信息
    public static final String uploadShowAndRewardInfo = "m=pic&a=sportsextraapi";//统一上传运动秀和奖励金币接口
    public static final String uploadBuShuList = "m=pic&a=jisenseStepNumDays";//批量上传步数
    public static final String getMedalInfo = "m=medal&a=MyMedalinfo";//获取勋章详情参数
    public static final String getShareMedal = "m=medal&a=MyMedalinfoshare&id=";// 勋章分享的链接
	public static final String medalWeb = "m=medal&a=OthersMedallist&uid=";//别人勋章列表
}
