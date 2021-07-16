package net.pretronic.dkbans.reportreward;

import net.pretronic.dkbans.reportreward.config.DKBansReportRewardConfig;
import net.pretronic.libraries.plugin.lifecycle.Lifecycle;
import net.pretronic.libraries.plugin.lifecycle.LifecycleState;
import org.mcnative.licensing.exceptions.CloudNotCheckoutLicenseException;
import org.mcnative.licensing.exceptions.LicenseNotValidException;
import org.mcnative.runtime.api.plugin.MinecraftPlugin;

public class DKBansReportRewardPlugin extends MinecraftPlugin {

    public static final String RESOURCE_ID = "a120140d-bbdf-11eb-8ba0-0242ac180002";
    public static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwcUY5PfVh6jLu7pP27cAtp95HwNpxfvE5rnv1ptBmYRUMIJS1vCutfjlEogfTMZL2aiKhdAKW4wuGNzzGPKxVcV/iVRiGtJPHRPpdvIgPkGVrglZVDrV8pcyhLd7L42erskdUZl9iAvXx3KZVp/5q/njRz0n9ZWGU7KHs4+ngpCRyFw1N1Kr/tZkqz+BgLXNT3Fxv2EU6qDpLNutsXkojmO+oMUsz8sFDPf38aFzn8lNNpFNTxdJgaaMEZN12G9WKj6XmKIdbq04Qcb7oX0BhugqPbvW0bULG+ija9aEOevdyFLrKrDT34qUmo8PmDPh1/FZmazsPTv1HIWkNhY1qwIDAQAB";

    @Lifecycle(state = LifecycleState.LOAD)
    public void onLoad(LifecycleState state){
        getLogger().info("DKBansReportReward is starting, please wait..");

        try{
            //McNativeLicenseIntegration.newContext(this,RESOURCE_ID,PUBLIC_KEY).verifyOrCheckout();
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


        getLogger().info("DKBansReportReward started successfully");
    }
}
