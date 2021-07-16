package net.pretronic.dkbans.reportreward;

import net.pretronic.dkbans.reportreward.config.DKBansReportRewardConfig;
import net.pretronic.dkbans.reportreward.listener.DKBansListener;
import net.pretronic.libraries.plugin.lifecycle.Lifecycle;
import net.pretronic.libraries.plugin.lifecycle.LifecycleState;
import org.mcnative.licensing.context.platform.McNativeLicenseIntegration;
import org.mcnative.licensing.exceptions.CloudNotCheckoutLicenseException;
import org.mcnative.licensing.exceptions.LicenseNotValidException;
import org.mcnative.runtime.api.plugin.MinecraftPlugin;

public class DKBansReportRewardPlugin extends MinecraftPlugin {

    public static final String RESOURCE_ID = "0ccfe964-e655-11eb-8ba0-0242ac180002";
    public static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnLAylke98H9uvaZiY2HoNfnmMBkg+GOhxtxnuYaSfhXslHFa2DPwxT6PVPEsyph+puYzPmT9YUzd27ESSZmJ/4ua35M+gpMfIh6F1jUw9DPKQ+AQdcx1Y4KKWDHN+JdRKAyg7vit/iK71WKVOxMMSWKgAk36jsJ67RPsovkPRQ+tYQ9GSq8Wv9gpFEOu+fHcyI7pOg4kfbtvx+7qUM2y7Di7ZU9VWutCQfPD96Xpe6G2ZWUBjDFwBCEP7yqgmfVurLzhc50xb1yXocmJJkQKlT7SatwISvW8eEPsRZyx4THaNQLwVbjLc7IkbIwPI+JLM2l62uQcgojHA1YGrKML2QIDAQAB";

    @Lifecycle(state = LifecycleState.LOAD)
    public void onLoad(LifecycleState state){
        getLogger().info("DKBansReportReward is starting, please wait..");

        try{
            McNativeLicenseIntegration.newContext(this,RESOURCE_ID,PUBLIC_KEY).verifyOrCheckout();
        }catch (LicenseNotValidException | CloudNotCheckoutLicenseException e){
            getLogger().error("--------------------------------");
            getLogger().error("-> Invalid license");
            getLogger().error("-> Error: "+e.getMessage());
            getLogger().error("--------------------------------");
            getLogger().info("DKBansReportReward is shutting down");
            getLoader().shutdown();
            return;
        }
        getConfiguration().load(DKBansReportRewardConfig.class);

        getRuntime().getLocal().getEventBus().subscribe(this,new DKBansListener());

        getLogger().info("DKBansReportReward started successfully");
    }
}
