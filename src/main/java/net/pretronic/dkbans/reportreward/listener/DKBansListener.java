package net.pretronic.dkbans.reportreward.listener;

import net.pretronic.dkbans.api.event.report.DKBansReportCreateEvent;
import net.pretronic.dkbans.api.event.report.DKBansReportStateChangedEvent;
import net.pretronic.dkbans.api.player.report.PlayerReportEntry;
import net.pretronic.dkbans.api.player.report.ReportState;
import net.pretronic.dkbans.reportreward.config.DKBansReportRewardConfig;
import net.pretronic.dkbans.reportreward.config.Messages;
import net.pretronic.dkcoins.api.DKCoins;
import net.pretronic.dkcoins.api.currency.Currency;
import net.pretronic.dkcoins.api.user.DKCoinsUser;
import net.pretronic.libraries.event.Listener;
import net.pretronic.libraries.message.bml.variable.VariableSet;
import org.mcnative.runtime.api.McNative;
import org.mcnative.runtime.api.Setting;
import org.mcnative.runtime.api.player.MinecraftPlayer;

import java.util.ArrayList;
import java.util.UUID;

public class DKBansListener {

    @Listener
    public void onDKBansReportStateChanged(DKBansReportCreateEvent event) {
        PlayerReportEntry entry = event.getReportEntry();
        UUID reporterId = entry.getReporterId();
        MinecraftPlayer reporter = McNative.getInstance().getPlayerManager().getPlayer(reporterId);
        Setting setting = reporter.getSetting("DKBansReportReward","report.count");
        int count = setting != null ? setting.getIntValue()+1 : 1;
        reporter.setSetting("DKBansReportReward","report.count",count);
    }

    @Listener
    public void onDKBansReportStateChanged(DKBansReportStateChangedEvent event) {
        if(event.getNewState() == ReportState.ACCEPTED) {
            for (PlayerReportEntry entry : event.getReport().getEntries()) {
                UUID reporterId = entry.getReporterId();
                MinecraftPlayer reporter = McNative.getInstance().getPlayerManager().getPlayer(reporterId);

                //successful
                Setting setting = reporter.getSetting("DKBansReportReward","report.successful");
                int successful = setting != null ? setting.getIntValue()+1 : 1;
                reporter.setSetting("DKBansReportReward","report.successful",successful);

                //count
                Setting setting1 = reporter.getSetting("DKBansReportReward","report.count");
                int count = setting1 != null ? setting1.getIntValue() : 1;

                double rate = ((double)successful)/((double)count);
                double reward = DKBansReportRewardConfig.REWARD_MIN + ((DKBansReportRewardConfig.REWARD_MAX-DKBansReportRewardConfig.REWARD_MIN) * rate)  ;

                Currency currency = DKCoins.getInstance().getCurrencyManager().getCurrency(DKBansReportRewardConfig.TRANSACTION_CURRENCY);
                if(currency == null) throw new IllegalArgumentException("Transaction currency "+DKBansReportRewardConfig.TRANSACTION_CURRENCY+" was not found");

                DKCoinsUser user = reporter.getAs(DKCoinsUser.class);
                user.getDefaultAccount().getCredit(currency)
                        .addAmount(null
                                ,reward
                                ,DKBansReportRewardConfig.TRANSACTION_REASON.replace("{player}",event.getReport().getPlayer().getName())
                                ,DKBansReportRewardConfig.TRANSACTION_CAUSE
                                ,new ArrayList<>());
                if(reporter.isOnline()){
                    reporter.getAsOnlinePlayer().sendMessage(Messages.REWARD, VariableSet.create()
                            .add("reward",reward)
                            .addDescribed("player",event.getReport().getPlayer()));
                }
            }
        }
    }
}
