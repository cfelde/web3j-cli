/*
 * Copyright 2019 Web3 Labs LTD.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.web3j.console;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.mockito.Matchers.contains;
import static org.mockito.Matchers.startsWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.web3j.console.SampleKeys.PASSWORD;

public class KeyImporterTest {

    static final char[] WALLET_PASSWORD = PASSWORD.toCharArray();
    IODevice console = mock(IODevice.class);

    @Test
    public void testSpecifyPrivateKey(@TempDir Path tempDirPath) {
        prepareWalletCreation(SampleKeys.PRIVATE_KEY_STRING, tempDirPath);
    }

    @Test
    public void testLoadPrivateKeyFromFile(@TempDir Path tempDirPath) {
        prepareWalletCreation(
                KeyImporterTest.class
                        .getResource("/keyfiles/" + "sample-private-key.txt")
                        .getFile(),
                tempDirPath);
    }

    private void prepareWalletCreation(String input, Path tempDirPath) {
        when(console.readLine(startsWith("Please enter the hex encoded private key")))
                .thenReturn(input);
        when(console.readPassword(contains("password")))
                .thenReturn(WALLET_PASSWORD, WALLET_PASSWORD);
        when(console.readLine(contains("Please enter a destination directory location")))
                .thenReturn(tempDirPath.toString());

        KeyImporter.main(console);

        verify(console).printf(contains("successfully created in"));
    }
}
