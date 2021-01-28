/*
 * Copyright (C) 2012 The Android Open Source Project
 *
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
 */

package com.android.uiautomator2.actions;

import com.android.ddmlib.*;
import com.android.uiautomator2.DebugBridge;
import com.android.uiautomator2.UiAutomatorViewer;
import com.android.uiautomator2.instrumentation.TiRuntime;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import java.io.IOException;
import java.util.List;

public class StartAgentAction extends Action {
    private UiAutomatorViewer mViewer;

    public StartAgentAction(UiAutomatorViewer viewer) {
        super("&Agent");

        mViewer = viewer;
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return ImageHelper.loadImageDescriptorFromResource("images/agent_start.png");
    }

    private void attach() throws TimeoutException, AdbCommandRejectedException, SyncException, IOException, ShellCommandUnresponsiveException {
        IDevice device = DebugBridge.getDevices().get(0);
        String packegName = "com.taobao.trip";
        //String packegName = "com.example.animations30";
        String agent = "./libs/armeabi-v7a/libmethodtrace.so";
        //String agent = "./libs/arm64-v8a/libmethodtrace.so";
        String deviceAgent = " /data/local/tmp/";
        device.pushFile(agent, deviceAgent);
        device.executeShellCommand("run-as "+  packegName +
                " \"cp /data/local/tmp/libmethodtrace.so code_cache/\"", null);
        device.executeShellCommand("am attach-agent " +  packegName +
                " /data/data/" + packegName + "/code_cache/libmethodtrace.so", null);
    }

    private void attachInternal() {
        try {
            attach();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (AdbCommandRejectedException e) {
            e.printStackTrace();
        } catch (SyncException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ShellCommandUnresponsiveException e) {
            e.printStackTrace();
        }
    }

    private void startLog() {
        List<IDevice> devices = DebugBridge.getDevices();
        System.out.println(devices.toArray());
        TiRuntime.getInstance().start(devices.get(0));
    }

    @Override
    public void run() {
        startLog();
    }
}
