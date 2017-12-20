package com.walmartlabs.concord.it.console;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 Wal-Mart Store, Inc.
 * -----
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =====
 */

import com.walmartlabs.concord.server.api.process.*;
import com.walmartlabs.concord.server.console.CustomFormService;
import com.walmartlabs.concord.server.console.FormSessionResponse;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.ByteArrayInputStream;
import java.util.List;

import static com.walmartlabs.concord.it.common.ITUtils.archive;
import static com.walmartlabs.concord.it.common.ServerClient.waitForStatus;
import static org.junit.Assert.*;

@Ignore
public class FormBrandingIT extends AbstractIT {

    @Test(timeout = 30000)
    public void testBranding() throws Exception {
        byte[] payload = archive(FormBrandingIT.class.getResource("formBranding").toURI());

        // ---

        ProcessResource processResource = proxy(ProcessResource.class);
        StartProcessResponse spr = processResource.start(new ByteArrayInputStream(payload), null, false, null);

        waitForStatus(processResource, spr.getInstanceId(), ProcessStatus.SUSPENDED);

        // ---

        FormResource formResource = proxy(FormResource.class);

        List<FormListEntry> forms = formResource.list(spr.getInstanceId());
        assertEquals(1, forms.size());

        // ---

        FormListEntry form = forms.get(0);
        assertTrue(form.isCustom());

        CustomFormService customFormService = proxy(CustomFormService.class);
        FormSessionResponse fsr = customFormService.startSession(spr.getInstanceId(), form.getFormInstanceId());
        assertNotNull(fsr);

        // ---

        getDriver().navigate().to("http://" + ITConstants.REMOTE_USER + ":" + ITConstants.REMOTE_PASSWORD + "@localhost:8080" + fsr.getUri());

        // ---

        WebElement firstName = waitFor("First Name", By.name("firstName"));
        firstName.clear();
        firstName.sendKeys("John");

        WebElement lastName = waitFor("Last Name", By.name("lastName"));
        lastName.clear();
        lastName.sendKeys("Smith");

        WebElement submitButton = waitFor("Submit button", By.cssSelector("[type=submit]"));
        submitButton.click();
    }
}
