/*
 * Copyright 2011 Henry Coles
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package org.pitest.mutationtest.execute;

import org.pitest.functional.SideEffect1;
import org.pitest.mutationtest.MutationStatusTestPair;
import org.pitest.util.CommunicationThread;
import org.pitest.util.SafeDataOutputStream;

import java.net.ServerSocket;
import java.util.Map;

/**
 *
 * @param <K> the type of the key for the identification of the mutation
 */
public class MutationTestCommunicationThread<K> extends CommunicationThread {


  private static class SendData implements SideEffect1<SafeDataOutputStream> {
    private final MinionArguments arguments;

    SendData(final MinionArguments arguments) {
      this.arguments = arguments;
    }

    @Override
    public void apply(final SafeDataOutputStream dos) {
      dos.write(this.arguments);
      dos.flush();
    }
  }

  private final Map<K, MutationStatusTestPair> idMap;

  public MutationTestCommunicationThread(final ServerSocket socket,
      final MinionArguments arguments,
      final Map<K, MutationStatusTestPair> idMap,
      final Class<K> keyType) {
    super(socket, new SendData(arguments), new Receive<K>(idMap, keyType));
    this.idMap = idMap;
  }

  public MutationStatusTestPair getStatus(final K id) {
    return this.idMap.get(id);
  }

}
