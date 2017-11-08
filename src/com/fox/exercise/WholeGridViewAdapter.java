package com.fox.exercise;

import java.util.List;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.api.entity.UserRank;
import com.fox.exercise.bitmap.util.ImageResizer;
import com.fox.exercise.newversion.act.PersonalPageMainActivity;
import com.fox.exercise.util.RoundedImage;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WholeGridViewAdapter extends BaseAdapter {

    private Context context;
    private List<UserRank> userRankList;
    private LayoutInflater mInflater;
    //	private float px;
    Resources r = Resources.getSystem();
    private ImageResizer mImageWorker;

    public WholeGridViewAdapter(Context context, ImageResizer imageWorker, List<UserRank> userRanks) {
        this.context = context;
        this.mImageWorker = imageWorker;
        this.userRankList = userRanks;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//		px = SportsUtilities.getRealPixel_W(context, 154);
    }


    public void add(UserRank item) {
        userRankList.add(item);
    }

    public void clear() {
        userRankList.clear();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return userRankList.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return userRankList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    private static class ViewHolder {
        RelativeLayout alllayout;
        ImageView rankTextView;
        // ImageView imageView;
        RoundedImage cover_user_photo;
        TextView nameTextView;
        TextView numTextView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {


        final UserRank item = userRankList.get(position);

        RelativeLayout frameLayout;
        ImageView rankTextView;
        // ImageView userImageView;
        // CircularImage cover_user_photo;
        RoundedImage cover_user_photo;
        TextView nameTextView;
        TextView numTextView;

        RelativeLayout userlayout = null;
        RelativeLayout alllayout = null;

        ViewHolder viewHolder;

        if (convertView == null) {
            frameLayout = new RelativeLayout(context);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.FILL_PARENT,
                    RelativeLayout.LayoutParams.FILL_PARENT);
            frameLayout.setLayoutParams(new GridView.LayoutParams(params));

            View view = mInflater.inflate(R.layout.personal_msg, null);
            rankTextView = (ImageView) view.findViewById(R.id.ranking);
            // userImageView = (ImageView)
            // view.findViewById(R.id.userimage);
            cover_user_photo = (RoundedImage) view.findViewById(R.id.cover_user_photo);
            nameTextView = (TextView) view.findViewById(R.id.username);
            numTextView = (TextView) view.findViewById(R.id.usercal);

            userlayout = (RelativeLayout) view.findViewById(R.id.userlayout);
            alllayout = (RelativeLayout) view.findViewById(R.id.rankLinearLayout);
            frameLayout.addView(view, params);

            viewHolder = new ViewHolder();
            viewHolder.alllayout = alllayout;
            viewHolder.rankTextView = rankTextView;
            // viewHolder.imageView = userImageView;
            viewHolder.cover_user_photo = cover_user_photo;
            viewHolder.nameTextView = nameTextView;
            viewHolder.numTextView = numTextView;
            frameLayout.setTag(viewHolder);
        } else {
            frameLayout = (RelativeLayout) convertView;
        }

        viewHolder = (ViewHolder) frameLayout.getTag();
        // viewHolder.rankTextView.setText(Integer.toString(position + 1));
        if (position == 0) {
            viewHolder.rankTextView
                    .setBackgroundResource(R.drawable.rank_one);
        } else if (position == 1) {
            viewHolder.rankTextView
                    .setBackgroundResource(R.drawable.rank_two);
        } else if (position == 2) {
            viewHolder.rankTextView
                    .setBackgroundResource(R.drawable.rank_three);
        } else {
            viewHolder.rankTextView.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
        Log.i("***item.getSex()", "" + item.getSex());
        viewHolder.nameTextView.setText(item.getName().toString());
        //cal
        Log.i("*********item.rankNumber", "" + Integer.toString(item.getRankNumber()));
        viewHolder.numTextView.setText(Integer.toString(item.getRankNumber()));

        viewHolder.cover_user_photo.setImageBitmap(null);
        viewHolder.cover_user_photo.setBackgroundResource(item.getSex() == 1 ? R.drawable.sports_user_edit_portrait_male
                : R.drawable.sports_user_edit_portrait);
        String imgUrl = item.getImg();
        /*
		 * viewHolder.imageView.setTag(imgUrl);
		 * viewHolder.imageView.setOnClickListener(new
		 * View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { Intent intent = new
		 * Intent(context, PedometerActivity.class); intent.putExtra("ID",
		 * item.getId()); startActivity(intent); } });
		 * mImageWorker.loadImage(imgUrl, viewHolder.imageView, null, null,
		 * false);
		 */
        viewHolder.cover_user_photo.setTag(imgUrl);
        viewHolder.alllayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!SportsApp.getInstance().isOpenNetwork()) {
                    Toast.makeText(context, "网络未连接，请检查网络！", Toast.LENGTH_LONG).show();

                    return;
                }
//				Intent intent = new Intent(context, PedometerActivity.class);
                Intent intent = new Intent(context, PersonalPageMainActivity.class);
                intent.putExtra("ID", item.getId());
                context.startActivity(intent);
            }
        });

        mImageWorker
                .setLoadingImage(item.getSex() == 1 ? R.drawable.sports_user_edit_portrait_male
                        : R.drawable.sports_user_edit_portrait);

        mImageWorker.loadImage(imgUrl, viewHolder.cover_user_photo, null, null, false);
        frameLayout.setTag(viewHolder);

        return frameLayout;
    }

}
