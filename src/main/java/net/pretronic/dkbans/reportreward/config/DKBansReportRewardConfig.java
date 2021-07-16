package net.pretronic.dkbans.reportreward.config;

import net.pretronic.dkcoins.api.DKCoins;
import net.pretronic.dkcoins.api.currency.Currency;
import net.pretronic.libraries.document.annotations.OnDocumentConfigurationLoad;

public class DKBansReportRewardConfig {

    public static double REWARD_MIN = 50.0;
    public static double REWARD_MAX = 100.0;

    public static String TRANSACTION_CURRENCY = "Coins";
    public static String TRANSACTION_CAUSE = "REPORT_REWARD";
    public static String TRANSACTION_REASON = "Reward for successful report ({player})";

    public transient static Currency CURRENCY;

    @OnDocumentConfigurationLoad
    public static void load(){
        CURRENCY = DKCoins.getInstance().getCurrencyManager().getCurrency(TRANSACTION_CURRENCY);
        if(CURRENCY == null) throw new IllegalArgumentException("Transaction currency "+TRANSACTION_CURRENCY+" was not found");
    }
}
