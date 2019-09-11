// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.firebase.firestore;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.heartbeatinfo.DefaultHeartBeatInfo;
import com.google.firebase.heartbeatinfo.HeartBeatInfo;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class HeartBeatTesterTest {

    @Test
    public void testHeartBeatTime() {
        HeartBeatInfo info = new DefaultHeartBeatInfo(ApplicationProvider.getApplicationContext());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        long startTime = System.currentTimeMillis();
        info.getHeartBeatCode("foo");
        long endTime = System.currentTimeMillis();
        Map<String, Long> data = new HashMap<>();
        data.put("HeartBeatTime", endTime-startTime);
        db.collection("HeartBeatTime").document("BJ")
                .set(data, SetOptions.merge());

    }
}