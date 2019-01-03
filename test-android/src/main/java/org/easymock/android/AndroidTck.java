/*
 * Copyright 2013-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.easymock.android;

import android.app.Activity;
import android.app.Instrumentation;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import static org.easymock.EasyMock.*;

/**
 * Tests making sure EasyMock behave correctly on Android
 *
 * @author Henri Tremblay
 */
public class AndroidTck extends Instrumentation {

    @Override
   public void onCreate(Bundle arguments) {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      PrintStream printStream;
      try {
         printStream = new PrintStream(outputStream, true, Charset.defaultCharset().name());
      } catch (UnsupportedEncodingException e) {
         throw new RuntimeException(e);
      }
      System.setOut(printStream);

      System.setProperty("dexmaker.dexcache", getTargetContext().getCacheDir().getPath());

      testInterface();
      testObject();

      Bundle bundle = new Bundle();
      String fromStdout;
      try {
         fromStdout = outputStream.toString(Charset.defaultCharset().name());
      } catch (UnsupportedEncodingException e) {
         throw new RuntimeException(e);
      }
      bundle.putString(Instrumentation.REPORT_KEY_STREAMRESULT, fromStdout);
      finish(Activity.RESULT_OK, bundle);
   }

   private void testObject() {
      Activity mock = createMock(Activity.class);
      mock.onLowMemory();
      expect(mock.getTaskId()).andReturn(10);
      System.out.println("replay");
      replay(mock);
      mock.onLowMemory();
      if(mock.getTaskId() != 10) {
          throw new AssertionError("Should have been 10");
      }
      System.out.println("verify");
      verify(mock);
   }

   private void testInterface() {
      Parcelable mock = createMock(Parcelable.class);
      mock.writeToParcel(anyObject(Parcel.class), eq(4));
      expect(mock.describeContents()).andReturn(11);
      System.out.println("replay");
      replay(mock);
      mock.writeToParcel(null, 4);
      if(mock.describeContents() != 11) {
          throw new AssertionError("Should have been 11");
      }
      System.out.println("verify");
      verify(mock);
   }
}
