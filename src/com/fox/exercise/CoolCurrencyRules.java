package com.fox.exercise;

import com.umeng.analytics.MobclickAgent;


public class CoolCurrencyRules extends AbstractBaseFragment {

    @Override
    public void beforeInitView() {
        title = getString(R.string.cool_coins_rules);
    }

    @Override
    public void setViewStatus() {
        setContentView(R.layout.cool_currency_rules);
    }

    @Override
    public void onPageResume() {
        MobclickAgent.onPageStart("CoolCurrencyRules");
    }

    @Override
    public void onPagePause() {
        MobclickAgent.onPageEnd("CoolCurrencyRules");
    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub

    }


}
