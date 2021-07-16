package net.pretronic.dkbans.reportreward.listener;

import net.pretronic.dkbans.api.event.report.DKBansReportCreateEvent;
import net.pretronic.dkbans.api.event.report.DKBansReportStateChangedEvent;
import net.pretronic.dkbans.api.player.report.PlayerReportEntry;
import net.pretronic.dkbans.api.player.report.ReportState;
import net.pretronic.dkbans.reportreward.config.DKBansReportRewardConfig;
import net.pretronic.dkcoins.api.user.DKCoinsUser;
import net.pretronic.libraries.event.Listener;
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

                DKCoinsUser user = reporter.getAs(DKCoinsUser.class);
                user.getDefaultAccount().getCredit(DKBansReportRewardConfig.CURRENCY)
                        .addAmount(null
                                ,reward
                                ,DKBansReportRewardConfig.TRANSACTION_REASON.replace("{player}",event.getReport().getPlayer().getName())
                                ,DKBansReportRewardConfig.TRANSACTION_CAUSE
                                ,new ArrayList<>());
            }
        }
    }
}
