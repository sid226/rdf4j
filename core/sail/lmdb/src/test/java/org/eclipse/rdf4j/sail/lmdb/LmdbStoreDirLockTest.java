/*******************************************************************************
 * Copyright (c) 2015 Eclipse RDF4J contributors, Aduna, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Distribution License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *******************************************************************************/
package org.eclipse.rdf4j.sail.lmdb;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;

import org.eclipse.rdf4j.sail.SailLockedException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class LmdbStoreDirLockTest {

	@Rule
	public TemporaryFolder tempDir = new TemporaryFolder();

	@Test
	public void testLocking() throws Exception {
		File dataDir = tempDir.newFolder();
		LmdbStore sail = new LmdbStore(dataDir, "spoc,posc");
		sail.init();

		try {
			LmdbStore sail2 = new LmdbStore(dataDir, "spoc,posc");
			sail2.init();
			try {
				fail("initialized a second lmdb store with same dataDir");
			} finally {
				sail2.shutDown();
			}
		} catch (SailLockedException e) {
			// Expected: should not be able to open two lmdb stores with the
			// same dataDir
			assertNotNull(e);
		} finally {
			sail.shutDown();
		}
	}
}
