package net.pretronic.dkbans.reportreward.listener;

import net.pretronic.dkbans.api.event.report.DKBansReportStateChangedEvent;
import net.pretronic.dkbans.api.player.report.PlayerReportEntry;
import net.pretronic.dkbans.api.player.report.ReportState;
import net.pretronic.libraries.event.Listener;

import java.util.UUID;

public class DKBansListener {

    @Listener
    public void onDKBansReportStateChanged(DKBansReportStateChangedEvent event) {
        if(event.getNewState() == ReportState.NEW) {
            event.get
        }
        if(event.getNewState() == ReportState.ACCEPTED) {
            for (PlayerReportEntry entry : event.getReport().getEntries()) {
                UUID reporterId = entry.getReporterId();
            }
        }
    }
}
