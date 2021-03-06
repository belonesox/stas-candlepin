/**
 * Copyright (c) 2009 - 2012 Red Hat, Inc.
 *
 * This software is licensed to you under the GNU General Public License,
 * version 2 (GPLv2). There is NO WARRANTY for this software, express or
 * implied, including the implied warranties of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. You should have received a copy of GPLv2
 * along with this software; if not, see
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt.
 *
 * Red Hat trademarks are not licensed under GPLv2. No permission is
 * granted to use or replicate Red Hat trademarks that are incorporated
 * in this software or its documentation.
 */
package org.candlepin.model;

import static org.junit.Assert.assertEquals;

import org.candlepin.test.DatabaseTestFixture;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

import javax.inject.Inject;

/**
 * ContentCuratorTest
 */
public class ContentCuratorTest extends DatabaseTestFixture {
    @Inject private ContentCurator contentCurator;
    @Inject private OwnerCurator ownerCurator;

    private Content updates;
    private Owner owner;

    /* FIXME: Add Arches here */

    @Before
    public void setUp() {
        this.owner = new Owner("Example-Corporation");
        ownerCurator.create(owner);

        updates = new Content(
            this.owner,
            "Test Content 1", "100",
            "test-content-label-1", "yum-1", "test-vendor-1",
            "test-content-url-1", "test-gpg-url-1", "test-arch1,test-arch2");
        updates.setRequiredTags("required-tags");
        updates.setReleaseVer("releaseVer");
        updates.setMetadataExpire(new Long(1));
        updates.setModifiedProductIds(new HashSet<String>() { { add("productIdOne"); } });
    }

    @Test
    public void shouldUpdateContentWithNewValues() {
        Content toBeUpdated = new Content(
            this.owner,
            "Test Content", updates.getId(),
            "test-content-label", "yum", "test-vendor",
            "test-content-url", "test-gpg-url", "test-arch1");
        contentCurator.createContent(toBeUpdated, owner);

        updates.setUuid(toBeUpdated.getUuid());
        toBeUpdated = contentCurator.updateContent(updates, owner);

        assertEquals(toBeUpdated.getName(), updates.getName());
        assertEquals(toBeUpdated.getLabel(), updates.getLabel());
        assertEquals(toBeUpdated.getType(), updates.getType());
        assertEquals(toBeUpdated.getVendor(), updates.getVendor());
        assertEquals(toBeUpdated.getContentUrl(), updates.getContentUrl());
        assertEquals(toBeUpdated.getRequiredTags(), updates.getRequiredTags());
        assertEquals(toBeUpdated.getReleaseVer(), updates.getReleaseVer());
        assertEquals(toBeUpdated.getMetadataExpire(), updates.getMetadataExpire());
        assertEquals(toBeUpdated.getModifiedProductIds(), updates.getModifiedProductIds());
        assertEquals(toBeUpdated.getArches(), updates.getArches());
    }

    @Test
    public void importSameContentForMultipleProducts() {
        // TODO: This test may not have any value now since we no longer have a createOrUpdate
        // method that needs to work on the same content twice.

        Content c1 = new Content(owner, "mycontent", "5006", "mycontent", "yum",
            "vendor", "nobodycares", "nobodystillcares", "x86_64");
        Content c2 = new Content(owner, "mycontent", "5006", "mycontent", "yum",
            "vendor", "nobodycares", "nobodystillcares", "x86_64");
        contentCurator.createContent(c1, owner);
        contentCurator.updateContent(c2, owner);
    }
}
