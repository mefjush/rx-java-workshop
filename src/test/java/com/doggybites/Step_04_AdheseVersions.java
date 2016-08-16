package com.doggybites;

import org.junit.Test;
import rx.Observable;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class Step_04_AdheseVersions {

    private AsyncAdheseVersionService asyncAdheseVersionService = new AsyncAdheseVersionService();

    @Test
    public void gets_latest_adhese_version_asynchronously() {
        Observable<String> latestVersion = asyncAdheseVersionService.getLatestVersion();

        assertThat(latestVersion.toBlocking().first(), is("2.0.22.11"));
    }

    @Test
    public void gets_nrc_and_ipm_version() {
        Observable<String> nrc = asyncAdheseVersionService.getCustomerVersion("nrc");
        Observable<String> ipm = asyncAdheseVersionService.getCustomerVersion("ipm");

        Observable<String> both = nrc.concatWith(ipm);

        assertThat(both.count().toBlocking().first(), is(2));
    }

    @Test
    public void gets_any_customer_version_whichever_available_quicker() {
        Observable<String> nrc = asyncAdheseVersionService.getCustomerVersion("nrc");
        Observable<String> ipm = asyncAdheseVersionService.getCustomerVersion("ipm");

        Observable<String> any = nrc.mergeWith(ipm).first();

        assertThat(any.toBlocking().first(), is("2.0.22.7"));
    }
}
